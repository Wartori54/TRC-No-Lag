package net.wartori.trc_no_lag.mixin;

import net.minecraft.server.MinecraftServer;
import net.wartori.trc_no_lag.TRCNoInputLag;
import net.wartori.trc_no_lag.TickManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @ModifyConstant(method = "runServer", constant = @Constant(longValue = 50L))
    long modifyTickTime(long tickTime) {
        return (long) TickManager.tickTime;
    }

    @Inject(method = "save", at = @At("RETURN"))
    private void saveTickRate(boolean suppressLogs, boolean flush, boolean force, CallbackInfoReturnable<Boolean> cir) {
        if (TickManager.saveTickRate()) {
            TRCNoInputLag.logger.info("tickrate.txt saved");
        } else {
            TRCNoInputLag.logger.warn("tickrate.txt not saved!");
        }
    }
}
