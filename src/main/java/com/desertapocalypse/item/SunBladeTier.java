package com.desertapocalypse.item;

import com.desertapocalypse.ModItems;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public final class SunBladeTier implements Tier {
    public static final SunBladeTier INSTANCE = new SunBladeTier();

    @Override
    public int getUses() {
        return 1100;
    }

    @Override
    public float getSpeed() {
        return 7.0F;
    }

    @Override
    public float getAttackDamageBonus() {
        return 3.0F;
    }

    @Override
    public int getLevel() {
        return 3;
    }

    @Override
    public int getEnchantmentValue() {
        return 15;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.of(ModItems.DESERT_COIN.get());
    }

    private SunBladeTier() {}
}
