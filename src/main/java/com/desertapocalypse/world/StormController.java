package com.desertapocalypse.world;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RandomSource;

/** Песчаные бури: иногда бывают, иногда нет. */
public final class StormController {

    public static void tick(MinecraftServer server, ApocalypseData data) {
        RandomSource random = server.overworld().getRandom();
        if (data.stormTicks > 0) {
            data.stormTicks--;
            if (data.stormTicks == 0) {
                data.nextStormIn = 6000 + random.nextInt(18000); // 5-20 минут тишины
                data.setDirty();
                broadcast(server, "message.desertapocalypse.storm_end", ChatFormatting.GRAY);
            }
        } else {
            data.nextStormIn--;
            if (data.nextStormIn <= 0) {
                if (random.nextFloat() < 0.65F) {
                    start(server, data, 3600 + random.nextInt(4800)); // 3-7 минут
                } else {
                    data.nextStormIn = 6000 + random.nextInt(12000); // в этот раз бури не будет
                    data.setDirty();
                }
            }
        }
    }

    public static void start(MinecraftServer server, ApocalypseData data, int ticks) {
        data.stormTicks = ticks;
        data.setDirty();
        broadcast(server, "message.desertapocalypse.storm_start", ChatFormatting.RED);
    }

    public static void stop(MinecraftServer server, ApocalypseData data) {
        boolean wasActive = data.stormTicks > 0;
        data.stormTicks = 0;
        data.nextStormIn = 12000;
        data.setDirty();
        if (wasActive) {
            broadcast(server, "message.desertapocalypse.storm_end", ChatFormatting.GRAY);
        }
    }

    private static void broadcast(MinecraftServer server, String key, ChatFormatting color) {
        server.getPlayerList().broadcastSystemMessage(Component.translatable(key).withStyle(color), false);
    }

    private StormController() {}
}
