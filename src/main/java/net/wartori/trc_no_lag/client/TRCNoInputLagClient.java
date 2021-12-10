package net.wartori.trc_no_lag.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;
import net.wartori.trc_no_lag.AnalyzeS2CPackets;
import net.wartori.trc_no_lag.TRCNoInputLag;

@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
public class TRCNoInputLagClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(new Identifier(TRCNoInputLag.MOD_ID, "update_tick_rate"), AnalyzeS2CPackets::executeUpdateTickRate);
        
    }
}
