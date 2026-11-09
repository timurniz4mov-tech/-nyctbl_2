package com.desertapocalypse.item;

import com.desertapocalypse.DesertApocalypseMod;
import com.desertapocalypse.ModItems;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

public final class DesertArmorMaterial implements ArmorMaterial {
    public static final DesertArmorMaterial INSTANCE = new DesertArmorMaterial();

    @Override
    public int getDurabilityForType(ArmorItem.Type type) {
        switch (type) {
            case HELMET:
                return 160;
            case CHESTPLATE:
                return 240;
            case LEGGINGS:
                return 224;
            default:
                return 192;
        }
    }

    @Override
    public int getDefenseForType(ArmorItem.Type type) {
        switch (type) {
            case HELMET:
                return 2;
            case CHESTPLATE:
                return 5;
            case LEGGINGS:
                return 4;
            default:
                return 2;
        }
    }

    @Override
    public int getEnchantmentValue() {
        return 14;
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_LEATHER;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.of(ModItems.DESERT_COIN.get());
    }

    @Override
    public String getName() {
        return DesertApocalypseMod.MOD_ID + ":desert_wanderer";
    }

    @Override
    public float getToughness() {
        return 0.5F;
    }

    @Override
    public float getKnockbackResistance() {
        return 0.0F;
    }

    private DesertArmorMaterial() {}
}
