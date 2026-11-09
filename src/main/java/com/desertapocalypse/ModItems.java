package com.desertapocalypse;

import com.desertapocalypse.item.DesertArmorItem;
import com.desertapocalypse.item.DesertCompassItem;
import com.desertapocalypse.item.DuneSealItem;
import com.desertapocalypse.item.OasisCrystalItem;
import com.desertapocalypse.item.SunBladeItem;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, DesertApocalypseMod.MOD_ID);

    public static final RegistryObject<Item> DESERT_HEART_ITEM = ITEMS.register("desert_heart",
            () -> new BlockItem(ModBlocks.DESERT_HEART.get(), new Item.Properties()));

    public static final RegistryObject<Item> DESERT_COMPASS = ITEMS.register("desert_compass",
            () -> new DesertCompassItem(new Item.Properties().stacksTo(1)));

    /** Валюта Мастера пустыни: падает с тварей пустыни. */
    public static final RegistryObject<Item> DESERT_COIN = ITEMS.register("desert_coin",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> SUN_BLADE = ITEMS.register("sun_blade",
            () -> new SunBladeItem(new Item.Properties()));

    public static final RegistryObject<Item> OASIS_CRYSTAL = ITEMS.register("oasis_crystal",
            () -> new OasisCrystalItem(new Item.Properties().stacksTo(16)));

    public static final RegistryObject<Item> DUNE_SEAL = ITEMS.register("dune_seal",
            () -> new DuneSealItem(new Item.Properties().stacksTo(16)));

    public static final RegistryObject<Item> DESERT_HELMET = ITEMS.register("desert_helmet",
            () -> new DesertArmorItem(ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> DESERT_CHESTPLATE = ITEMS.register("desert_chestplate",
            () -> new DesertArmorItem(ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> DESERT_LEGGINGS = ITEMS.register("desert_leggings",
            () -> new DesertArmorItem(ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> DESERT_BOOTS = ITEMS.register("desert_boots",
            () -> new DesertArmorItem(ArmorItem.Type.BOOTS, new Item.Properties()));

    public static final RegistryObject<Item> DESERT_STALKER_SPAWN_EGG = ITEMS.register("desert_stalker_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.DESERT_STALKER, 0xC29A5C, 0xFF9A00, new Item.Properties()));
    public static final RegistryObject<Item> DESERT_COLOSSUS_SPAWN_EGG = ITEMS.register("desert_colossus_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.DESERT_COLOSSUS, 0x8C6A3E, 0xFF2814, new Item.Properties()));
    public static final RegistryObject<Item> DESERT_MASTER_SPAWN_EGG = ITEMS.register("desert_master_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.DESERT_MASTER, 0x3C5082, 0xE6CD96, new Item.Properties()));

    private ModItems() {}
}
