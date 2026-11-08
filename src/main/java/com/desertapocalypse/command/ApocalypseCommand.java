package com.desertapocalypse.command;

import com.desertapocalypse.ModBlocks;
import com.desertapocalypse.world.ApocalypseData;
import com.desertapocalypse.world.ApocalypseStarter;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Blocks;

import java.util.Locale;

/** /apocalypse info | restart | setradius <r> | setday <n> */
public final class ApocalypseCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("apocalypse")
                .then(Commands.literal("info")
                        .executes(ctx -> info(ctx.getSource())))
                .then(Commands.literal("restart")
                        .requires(src -> src.hasPermission(2))
                        .executes(ctx -> restart(ctx.getSource())))
                .then(Commands.literal("setradius")
                        .requires(src -> src.hasPermission(2))
                        .then(Commands.argument("radius", DoubleArgumentType.doubleArg(0.0D, 100000.0D))
                                .executes(ctx -> setRadius(ctx.getSource(),
                                        DoubleArgumentType.getDouble(ctx, "radius")))))
                .then(Commands.literal("setday")
                        .requires(src -> src.hasPermission(2))
                        .then(Commands.argument("day", IntegerArgumentType.integer(1, 1000))
                                .executes(ctx -> setDay(ctx.getSource(),
                                        IntegerArgumentType.getInteger(ctx, "day"))))));
    }

    private static int info(CommandSourceStack source) {
        ApocalypseData data = ApocalypseData.get(source.getServer().overworld());
        if (!data.started) {
            source.sendSuccess(() -> Component.translatable("command.desertapocalypse.not_started"), false);
            return 0;
        }
        String day = String.valueOf(data.dayNumber());
        String radius = String.format(Locale.ROOT, "%.1f", data.radius);
        String speed = String.format(Locale.ROOT, "%.2f", data.speedPerSecond() * 60.0D);
        String heart = source.hasPermission(2)
                ? data.center.getX() + " " + data.center.getY() + " " + data.center.getZ()
                : "???";
        source.sendSuccess(() -> Component.translatable("command.desertapocalypse.info",
                day, radius, speed, heart), false);
        return 1;
    }

    private static int restart(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        ServerLevel overworld = source.getServer().overworld();
        if (player.level() != overworld) {
            source.sendFailure(Component.translatable("command.desertapocalypse.wrong_dimension"));
            return 0;
        }
        ApocalypseData data = ApocalypseData.get(overworld);
        if (data.started && overworld.getBlockState(data.center).is(ModBlocks.DESERT_HEART.get())) {
            overworld.setBlock(data.center, Blocks.AIR.defaultBlockState(), 3);
        }
        ApocalypseStarter.start(overworld, data, player);
        source.sendSuccess(() -> Component.translatable("command.desertapocalypse.restart"), true);
        return 1;
    }

    private static int setRadius(CommandSourceStack source, double radius) {
        ApocalypseData data = ApocalypseData.get(source.getServer().overworld());
        if (!data.started) {
            source.sendFailure(Component.translatable("command.desertapocalypse.not_started"));
            return 0;
        }
        data.radius = radius;
        data.setDirty();
        source.sendSuccess(() -> Component.translatable("command.desertapocalypse.setradius",
                String.format(Locale.ROOT, "%.1f", radius)), true);
        return 1;
    }

    private static int setDay(CommandSourceStack source, int day) {
        ApocalypseData data = ApocalypseData.get(source.getServer().overworld());
        if (!data.started) {
            source.sendFailure(Component.translatable("command.desertapocalypse.not_started"));
            return 0;
        }
        data.ticks = (long) (day - 1) * ApocalypseData.DAY_TICKS;
        data.setDirty();
        source.sendSuccess(() -> Component.translatable("command.desertapocalypse.setday", String.valueOf(day)), true);
        return 1;
    }

    private ApocalypseCommand() {}
}
