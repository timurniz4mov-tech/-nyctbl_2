package com.desertapocalypse.world;

import com.desertapocalypse.ApocalypseConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

/** Сохраняемое состояние апокалипсиса (хранится в Верхнем мире). */
public class ApocalypseData extends SavedData {
    private static final String NAME = "desertapocalypse_data";
    public static final int DAY_TICKS = 24000;

    public boolean started = false;
    public BlockPos center = BlockPos.ZERO;
    public double radius = 0.0D;
    public long ticks = 0L;

    public static ApocalypseData get(ServerLevel anyLevel) {
        ServerLevel overworld = anyLevel.getServer().overworld();
        return overworld.getDataStorage().computeIfAbsent(ApocalypseData::load, ApocalypseData::new, NAME);
    }

    public static ApocalypseData load(CompoundTag tag) {
        ApocalypseData data = new ApocalypseData();
        data.started = tag.getBoolean("started");
        data.center = new BlockPos(tag.getInt("cx"), tag.getInt("cy"), tag.getInt("cz"));
        data.radius = tag.getDouble("radius");
        data.ticks = tag.getLong("ticks");
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putBoolean("started", started);
        tag.putInt("cx", center.getX());
        tag.putInt("cy", center.getY());
        tag.putInt("cz", center.getZ());
        tag.putDouble("radius", radius);
        tag.putLong("ticks", ticks);
        return tag;
    }

    /** Номер дня апокалипсиса (с 1). */
    public int dayNumber() {
        return (int) (ticks / DAY_TICKS) + 1;
    }

    /** Скорость роста в блоках в секунду: с каждым днём всё быстрее. */
    public double speedPerSecond() {
        double day = ticks / (double) DAY_TICKS;
        double speed = ApocalypseConfig.BASE_SPEED.get()
                * Math.pow(1.0D + ApocalypseConfig.DAILY_ACCELERATION.get(), day);
        return Math.min(speed, ApocalypseConfig.MAX_SPEED.get());
    }

    public boolean isInside(double x, double z) {
        return distanceToEdge(x, z) <= 0.0D;
    }

    /** Расстояние до границы пустыни: &gt; 0 снаружи, &lt;= 0 внутри. */
    public double distanceToEdge(double x, double z) {
        double dx = x - (center.getX() + 0.5D);
        double dz = z - (center.getZ() + 0.5D);
        return Math.sqrt(dx * dx + dz * dz) - radius;
    }
}
