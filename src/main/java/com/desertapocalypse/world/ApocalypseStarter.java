package com.desertapocalypse.world;

import com.desertapocalypse.ApocalypseConfig;
import com.desertapocalypse.ModBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;

/** Запускает апокалипсис: ставит Сердце Пустыни в 10-100 блоках от игрока. */
public final class ApocalypseStarter {

    public static void start(ServerLevel level, ApocalypseData data, ServerPlayer player) {
        RandomSource random = level.getRandom();
        int min = ApocalypseConfig.MIN_START_DISTANCE.get();
        int max = Math.max(min, ApocalypseConfig.MAX_START_DISTANCE.get());

        BlockPos best = player.blockPosition();
        for (int i = 0; i < 40; i++) {
            double angle = random.nextDouble() * Math.PI * 2.0D;
            double dist = min + random.nextDouble() * (max - min);
            int x = Mth.floor(player.getX() + Math.cos(angle) * dist);
            int z = Mth.floor(player.getZ() + Math.sin(angle) * dist);
            level.getChunk(x >> 4, z >> 4); // загрузить/сгенерировать чанк
            int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
            best = new BlockPos(x, y, z);
            if (level.getFluidState(best.below()).isEmpty()) {
                break; // нашли сушу
            }
        }

        if (!level.getFluidState(best.below()).isEmpty()) {
            level.setBlock(best.below(), Blocks.SANDSTONE.defaultBlockState(), 3);
        }
        level.setBlock(best, ModBlocks.DESERT_HEART.get().defaultBlockState(), 3);

        data.started = true;
        data.center = best;
        data.radius = ApocalypseConfig.START_RADIUS.get();
        data.ticks = 0L;
        data.stormTicks = 0;
        data.slowTicks = 0;
        data.nextStormIn = 4800 + random.nextInt(6000);
        data.setDirty();

        // сразу превращаем область вокруг сердца в пустыню
        int r = (int) Math.ceil(data.radius) + 1;
        double r2 = data.radius * data.radius;
        for (int dx = -r; dx <= r; dx++) {
            for (int dz = -r; dz <= r; dz++) {
                if (dx * dx + dz * dz > r2) {
                    continue;
                }
                int x = best.getX() + dx;
                int z = best.getZ() + dz;
                level.getChunk(x >> 4, z >> 4);
                DesertSpreader.convertColumn(level, x, z);
            }
        }

        level.getServer().getPlayerList().broadcastSystemMessage(
                Component.translatable("message.desertapocalypse.start").withStyle(ChatFormatting.GOLD), false);
    }

    private ApocalypseStarter() {}
}
