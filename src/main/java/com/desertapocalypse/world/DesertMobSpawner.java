package com.desertapocalypse.world;

import com.desertapocalypse.ModEntities;
import com.desertapocalypse.entity.DesertColossusEntity;
import com.desertapocalypse.entity.DesertMasterEntity;
import com.desertapocalypse.entity.DesertStalkerEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;

/** Спавнит Мастера пустыни, Песчаных скитальцев и Песчаных колоссов рядом с игроками. */
public final class DesertMobSpawner {

    /** Вызывается раз в 2 секунды для каждого игрока в Верхнем мире. */
    public static void tick(ServerLevel level, ApocalypseData data, ServerPlayer player) {
        double edge = data.distanceToEdge(player.getX(), player.getZ());
        RandomSource random = level.getRandom();
        boolean storm = data.stormTicks > 0;

        // Мастер пустыни живёт около зоны пустыни (один на ~160 блоков)
        if (edge < 80.0D && random.nextFloat() < 0.04F) {
            AABB area = player.getBoundingBox().inflate(160.0D);
            if (level.getEntitiesOfClass(DesertMasterEntity.class, area).isEmpty()) {
                BlockPos pos = findPos(level, player, data, 20, 40, false);
                if (pos != null) {
                    spawn(level, ModEntities.DESERT_MASTER.get(), pos);
                }
            }
        }

        // Враги - только внутри пустыни
        if (edge > 0.0D || level.getDifficulty() == Difficulty.PEACEFUL) {
            return;
        }

        int day = data.dayNumber();

        // Скитальцы: чем старше апокалипсис и во время бури - тем их больше
        int cap = Math.min(2 + day / 2 + (storm ? 2 : 0), 8);
        if (random.nextFloat() < (storm ? 0.5F : 0.25F)) {
            AABB near = player.getBoundingBox().inflate(64.0D);
            if (level.getEntitiesOfClass(DesertStalkerEntity.class, near).size() < cap) {
                BlockPos pos = findPos(level, player, data, 20, 36, true);
                if (pos != null) {
                    spawn(level, ModEntities.DESERT_STALKER.get(), pos);
                }
            }
        }

        // Колоссы: редкие, со 2-го дня, один на ~100 блоков
        if (day >= 2 && random.nextFloat() < (storm ? 0.02F : 0.008F)) {
            AABB wide = player.getBoundingBox().inflate(96.0D);
            if (level.getEntitiesOfClass(DesertColossusEntity.class, wide).isEmpty()) {
                BlockPos pos = findPos(level, player, data, 28, 44, true);
                if (pos != null) {
                    spawn(level, ModEntities.DESERT_COLOSSUS.get(), pos);
                }
            }
        }
    }

    private static BlockPos findPos(ServerLevel level, ServerPlayer player, ApocalypseData data,
                                    int minDist, int maxDist, boolean needDesert) {
        RandomSource random = level.getRandom();
        for (int i = 0; i < 8; i++) {
            double angle = random.nextDouble() * Math.PI * 2.0D;
            double dist = minDist + random.nextDouble() * (maxDist - minDist);
            int x = Mth.floor(player.getX() + Math.cos(angle) * dist);
            int z = Mth.floor(player.getZ() + Math.sin(angle) * dist);
            if (needDesert && !data.isInside(x + 0.5D, z + 0.5D)) {
                continue;
            }
            if (!level.hasChunkAt(new BlockPos(x, 64, z))) {
                continue;
            }
            int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
            BlockPos pos = new BlockPos(x, y, z);
            if (!level.getFluidState(pos.below()).isEmpty()) {
                continue;
            }
            return pos;
        }
        return null;
    }

    private static <T extends Mob> void spawn(ServerLevel level, EntityType<T> type, BlockPos pos) {
        T mob = type.create(level);
        if (mob == null) {
            return;
        }
        mob.moveTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, level.getRandom().nextFloat() * 360.0F, 0.0F);
        if (!level.noCollision(mob)) {
            mob.discard();
            return;
        }
        level.addFreshEntity(mob);
    }

    private DesertMobSpawner() {}
}
