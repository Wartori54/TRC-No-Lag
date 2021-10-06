package net.wartori.trc_no_lag.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;
import net.wartori.trc_no_lag.TickManager;

public class TickRateCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> builder = CommandManager.literal("tickrate").requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2));
        builder.then(CommandManager.literal("get").executes(context -> {
            context.getSource().sendFeedback(new TranslatableText("command.tickrate.get", 1000 / TickManager.tickTime), false);
            return 1;
        }));
        builder.then(CommandManager.literal("set")
                .then(CommandManager.argument("tps", FloatArgumentType.floatArg(0.001F)).executes(context -> {
                    TickManager.updateTickTime(1000 / FloatArgumentType.getFloat(context, "tps"));
                    context.getSource().sendFeedback(new TranslatableText("command.tickrate.set", FloatArgumentType.getFloat(context, "tps")), false);
                    return 1;
                })));
        builder.then(CommandManager.literal("sync")
                .executes(context -> {
                    TickManager.syncPlayerTickTime(context.getSource().getPlayer());
                    return 1;
                })
                .then(CommandManager.argument("target", EntityArgumentType.player()).executes(context -> {
                    TickManager.syncPlayerTickTime(EntityArgumentType.getPlayer(context, "target"));
                    return 1;
                })));
        dispatcher.register(builder);
    }
}
