package net.wartori.trc_no_lag.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Util;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.wartori.trc_no_lag.TRCNoInputLag;
import net.wartori.trc_no_lag.TickManager;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Shadow protected abstract void handleInputEvents();

    @Shadow @Nullable public Screen currentScreen;

    @Shadow @Final public GameRenderer gameRenderer;
    public int ticksToDo = 0;

    @ModifyVariable(method = "render", at = @At(value = "STORE"), ordinal = 1)
    private int updateStableRTC(int i) {
        TickManager.stableTicksToDo = TickManager.stableRTC.beginRenderTick(Util.getMeasuringTimeMs());
        return i;
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Mouse;updateMouse()V"))
    private void checkInputs(boolean tick, CallbackInfo ci) {
        if (currentScreen == null) {
            for (int i = 0; i < Math.min(10, TickManager.stableTicksToDo); i++) {
                this.handleInputEvents();
                this.gameRenderer.updateTargetedEntity(1.0F);
            }

        }

    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;handleInputEvents()V"))
    private void handleInputEventsRedirect(MinecraftClient minecraftClient) {
    }

    @Inject(method = "handleInputEvents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;handleBlockBreaking(Z)V"), cancellable = true)
    private void preHandleBlockBreaking(CallbackInfo ci) {


//        if (ticksToDo == 0) {
//            if (this.interactionManager != null && ((ClientPlayerInteractionManagerAccessor) this.interactionManager).getBlockBreakingCooldown() != 0) {
//                this.handleBlockBreaking(this.currentScreen == null && this.options.keyAttack.isPressed() && this.mouse.isCursorLocked());
//            } else if (this.interactionManager != null && this.crosshairTarget instanceof BlockHitResult blockTarget && !blockTarget.getBlockPos().equals(this.lastHitBlock)) {
//                this.lastHitBlock = blockTarget.getBlockPos();
//                this.handleBlockBreaking(this.currentScreen == null && this.options.keyAttack.isPressed() && this.mouse.isCursorLocked());
//
//            }
//            ci.cancel();
//        }
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/RenderTickCounter;beginRenderTick(J)I"))
    private int storeTicksToDo(RenderTickCounter renderTickCounter, long timeMillis) {
        TickManager.ticksToDo = renderTickCounter.beginRenderTick(timeMillis);
        TickManager.addTicksToGetDone(TickManager.ticksToDo);
//        TRCNoInputLag.logger.info(ticksToDo);
        return TickManager.ticksToDo;
    }

    @Inject(method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;)V", at = @At("TAIL"))
    private void onDisconnect(Screen screen, CallbackInfo ci) {
        TickManager.resetTickTime();
    }
}
