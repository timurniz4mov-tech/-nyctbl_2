package com.desertapocalypse.event;

import com.desertapocalypse.DesertApocalypseMod;
import com.desertapocalypse.command.ApocalypseCommand;
import com.desertapocalypse.thirst.ThirstHandler;
import com.desertapocalypse.world.ApocalypseData;
import com.desertapocalypse.world.ApocalypseStarter;
import com.desertapocalypse.world.DesertMobSpawner;
import com.desertapocalypse.world.DesertSpreader;
import com.desertapocalypse.world.StormController;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Locale;

@Mod.EventBusSubscriber(modid = DesertApocalypseMod.MOD_ID)
public final class ApocalypseEvents {

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        MinecraftServer server = event.getServer();
        ServerLevel overworld = server.overworld();
        if (overworld == null) {
            return;
        }
        ApocalypseData data = ApocalypseData.get(overworld);

        if (!data.started) {
            // ждём первого игрока в Верхнем мире
            if (server.getTickCount() % 20 == 0) {
                for (ServerPlayer player : overworld.players()) {
                    if (!player.isSpectator()) {
                        ApocalypseStarter.start(overworld, data, player);
                        break;
                    }
                }
            }
        } else {
            data.ticks++;
            if (data.slowTicks > 0) {
                data.slowTicks--;
            }
            data.radius += data.effectiveSpeedPerSecond() / 20.0D;

            if (data.ticks % ApocalypseData.DAY_TICKS == 0) {
                announceNewDay(server, data);
            }
            if (data.ticks % 200 == 0) {
                data.setDirty();
            }

            StormController.tick(server, data);

            for (ServerPlayer player : overworld.players()) {
                if (player.isSpectator()) {
                    continue;
                }
                DesertSpreader.spreadAround(overworld, data, player);
                if ((server.getTickCount() + player.getId()) % 40 == 0) {
                    DesertMobSpawner.tick(overworld, data, player);
                }
            }
        }

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            ThirstHandler.tick(player, data);
        }
    }

    private static void announceNewDay(MinecraftServer server, ApocalypseData data) {
        String speed = String.format(Locale.ROOT, "%.1f", data.effectiveSpeedPerSecond() * 60.0D);
        server.getPlayerList().broadcastSystemMessage(
                Component.translatable("message.desertapocalypse.new_day", data.dayNumber(), speed)
                        .withStyle(ChatFormatting.YELLOW), false);
    }

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ThirstHandler.giveStarterKit(player);
            ThirstHandler.sync(player);
        }
    }

    @SubscribeEvent
    public static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ThirstHandler.sync(player);
        }
    }

    @SubscribeEvent
    public static void onChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ThirstHandler.sync(player);
        }
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        ApocalypseCommand.register(event.getDispatcher());
    }

    private ApocalypseEvents() {}
}
