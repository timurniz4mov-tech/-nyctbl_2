package com.desertapocalypse.thirst;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

/** Хранение уровня жажды в постоянных данных игрока (0..20). */
public final class Thirst {
    public static final float MAX = 20.0F;
    private static final String ROOT = "PlayerPersisted";
    private static final String KEY = "dap_thirst";
    private static final String KIT = "dap_kit";

    private static CompoundTag data(Player player) {
        CompoundTag root = player.getPersistentData();
        if (!root.contains(ROOT, Tag.TAG_COMPOUND)) {
            root.put(ROOT, new CompoundTag());
        }
        return root.getCompound(ROOT);
    }

    public static float get(Player player) {
        CompoundTag tag = data(player);
        return tag.contains(KEY) ? tag.getFloat(KEY) : MAX;
    }

    public static void set(Player player, float value) {
        data(player).putFloat(KEY, Mth.clamp(value, 0.0F, MAX));
    }

    public static void add(Player player, float value) {
        set(player, get(player) + value);
    }

    public static boolean hasKit(Player player) {
        return data(player).getBoolean(KIT);
    }

    public static void setKit(Player player, boolean value) {
        data(player).putBoolean(KIT, value);
    }

    private Thirst() {}
}
