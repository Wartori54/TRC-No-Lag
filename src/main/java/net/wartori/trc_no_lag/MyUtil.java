package net.wartori.trc_no_lag;

import net.minecraft.client.MinecraftClient;

public class MyUtil {

    // just so it can exist in EnvType.SERVER, but not get executed
    public static MinecraftClient safeGetMinecraftClient() {
        return MinecraftClient.getInstance();
    }
}
