package net.wartori.trc_no_lag;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.io.Files;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.WorldSavePath;
import net.wartori.trc_no_lag.mixin.MinecraftClientAccessor;
import net.wartori.trc_no_lag.mixin.RenderTickCounterAccessor;

import java.io.*;
import java.nio.file.Path;
import java.util.Iterator;

public class TickManager {
    public static float tickTime = 50;
    public static MinecraftServer server = null;
    private static final Splitter COLON_SPLITTER = Splitter.on(":").limit(2);

    public static void updateTickTime(float newTickTime) {
        if (server == null) {
            return;
        }
        if (tickTime != newTickTime) {
            tickTime = newTickTime;
            server.getPlayerManager().getPlayerList().forEach(playerEntity -> {
                updatePlayerTickTime(playerEntity, newTickTime);
            });
        }
    }

    private static void updatePlayerTickTime(ServerPlayerEntity playerEntity, float newTickTime) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeFloat(newTickTime);
        ServerPlayNetworking.send(playerEntity, new Identifier(TRCNoInputLag.MOD_ID, "update_tick_rate"), buf);
    }

    public static void syncPlayerTickTime(ServerPlayerEntity playerEntity) {
        updatePlayerTickTime(playerEntity, tickTime);
    }


    @Environment(EnvType.CLIENT)
    public static void resetTickTime() {
        ((RenderTickCounterAccessor) ((MinecraftClientAccessor) MinecraftClient.getInstance()).getRenderTickCounter()).setTickTime(50);
    }

    public static boolean saveTickRate() {
        Path worldDir = server.getSavePath(WorldSavePath.ROOT);
        File tickrateFile = new File(String.valueOf(worldDir), "tickrate.txt");
        try {
            boolean created = tickrateFile.createNewFile();
            if (created) {
                TRCNoInputLag.logger.info("Created tickrate file");
            }
            FileWriter tickrateFileWriter = new FileWriter(tickrateFile);
            tickrateFileWriter.write("Ticktime: " + tickTime);
            tickrateFileWriter.close();
            return true;
        } catch (IOException ex) {
            // TODO: 2/10/21 log file creation error
            TRCNoInputLag.logger.error("Error while saving tickrate.txt:");
            TRCNoInputLag.logger.error(ex.getMessage());
            return false;
        }
    }

    @SuppressWarnings("UnstableApiUsage")
    public static boolean loadTickTime() {
        Path worldDir = server.getSavePath(WorldSavePath.ROOT);
        File tickrateFile = new File(String.valueOf(worldDir), "tickrate.txt");
        if (tickrateFile.exists()) {
            try {
                BufferedReader tickrateFileReader = Files.newReader(tickrateFile, Charsets.UTF_8);
                tickrateFileReader.lines().forEach((line) -> {
                    Iterator<String> iterator = COLON_SPLITTER.split(line).iterator();
                    iterator.next();
                    try {
                        tickTime = Float.parseFloat(iterator.next().trim());
                    } catch (NumberFormatException ex) {
                        TRCNoInputLag.logger.warn("Cant parse tickrate.txt");
                    }
                });
                return true;
            } catch (FileNotFoundException ex) {
                TRCNoInputLag.logger.error("Error while loading tickrate.txt:");
                TRCNoInputLag.logger.error(ex.getMessage());
                return false;
            }
        } else {
            return false;
        }
    }
}
