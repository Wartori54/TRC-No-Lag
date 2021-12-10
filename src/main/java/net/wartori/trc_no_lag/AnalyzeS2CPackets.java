package net.wartori.trc_no_lag;

import net.fabricmc.fabric.api.networking.v1.PacketSender;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.wartori.trc_no_lag.mixin.MinecraftClientAccessor;
import net.wartori.trc_no_lag.mixin.RenderTickCounterAccessor;

public class AnalyzeS2CPackets {

    public static void executeUpdateTickRate(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        float newTickTime = buf.readFloat();
        ((RenderTickCounterAccessor) ((MinecraftClientAccessor) client).getRenderTickCounter()).setTickTime(newTickTime);
        TickManager.tickTime = newTickTime;
    }
}
