package net.wartori.trc_no_lag;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.wartori.trc_no_lag.command.TickRateCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TRCNoInputLag implements ModInitializer {
    public static final String MOD_ID = "trc-no-lag";
    public static final Logger logger = LogManager.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            TickRateCommand.register(dispatcher);
        });
        ServerLifecycleEvents.SERVER_STARTED.register(minecraftServer -> {
            TickManager.server = minecraftServer;
            if (TickManager.loadTickTime()) {
                logger.info("Tickrate.txt loaded");
            } else {
                logger.warn("Error while loading tickrate.txt");
            }
        });

    }
}
