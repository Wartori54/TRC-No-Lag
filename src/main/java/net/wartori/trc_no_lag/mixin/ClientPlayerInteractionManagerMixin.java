package net.wartori.trc_no_lag.mixin;

import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.wartori.trc_no_lag.TRCNoInputLag;
import net.wartori.trc_no_lag.TickManager;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {
    @Shadow private int blockBreakingCooldown;

    @Shadow private float blockBreakingSoundCooldown;

    @Inject(method = "updateBlockBreakingProgress", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;syncSelectedSlot()V", shift = At.Shift.AFTER), cancellable = true)
    private void updateBlockBreakingCooldown(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (this.blockBreakingCooldown > 0) {
            if (TickManager.getTicksToGetDone(true) > 0) {
                --this.blockBreakingCooldown;
            }
            cir.setReturnValue(true);
//            TRCNoInputLag.logger.info(TickManager.ticksToDo);
        }
    }

    @Redirect(method = "updateBlockBreakingProgress", at = @At(value = "FIELD", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;blockBreakingCooldown:I", opcode = Opcodes.GETFIELD, ordinal = 0))
    private int removeBlockBreakingCooldownUpdate(ClientPlayerInteractionManager clientPlayerInteractionManager) {
        return 0;
    }

    @Redirect(method = "updateBlockBreakingProgress", at = @At(value = "FIELD", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;blockBreakingSoundCooldown:F", opcode = Opcodes.PUTFIELD, ordinal = 0))
    private void proxyUpdateBlockBreakingSoundCooldown(ClientPlayerInteractionManager clientPlayerInteractionManager, float value) {
        this.blockBreakingSoundCooldown += 50 / TickManager.tickTime;
    }
}

