package net.wartori.trc_no_lag.mixin;

import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.util.math.MathHelper;
import net.wartori.trc_no_lag.TickManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SoundSystem.class)
public class SoundSystemMixin {

    @Inject(method = "getAdjustedPitch", at = @At("HEAD"), cancellable = true)
    private void adjustPitch(SoundInstance sound, CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(50.0F / TickManager.tickTime * MathHelper.clamp(sound.getPitch(), 0.5F, 2.0F));
        cir.cancel();
    }
}
