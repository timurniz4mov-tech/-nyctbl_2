package com.desertapocalypse;

import com.desertapocalypse.item.DesertCompassItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
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

    private ModItems() {}
}
