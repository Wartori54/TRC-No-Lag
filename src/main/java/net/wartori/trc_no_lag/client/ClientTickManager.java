package net.wartori.trc_no_lag.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderTickCounter;
import net.wartori.trc_no_lag.mixin.MinecraftClientAccessor;
import net.wartori.trc_no_lag.mixin.RenderTickCounterAccessor;

@Environment(EnvType.CLIENT)
public class ClientTickManager {
    public static final RenderTickCounter stableRTC = new RenderTickCounter(20, 0);


    public static void resetTickTime() {
        ((RenderTickCounterAccessor) ((MinecraftClientAccessor) MinecraftClient.getInstance()).getRenderTickCounter()).setTickTime(50);
    }
}
