package com.desertapocalypse;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(DesertApocalypseMod.MOD_ID)
public class DesertApocalypseMod {
    public static final String MOD_ID = "desertapocalypse";

    public DesertApocalypseMod() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.BLOCKS.register(modBus);
        ModItems.ITEMS.register(modBus);
        ModEntities.ENTITY_TYPES.register(modBus);
        modBus.addListener(this::commonSetup);
        modBus.addListener(this::addCreativeTabs);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ApocalypseConfig.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(ModNetwork::register);
    }

    private void addCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(ModItems.DESERT_COMPASS);
            event.accept(ModItems.OASIS_CRYSTAL);
            event.accept(ModItems.DUNE_SEAL);
        }
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(ModItems.DESERT_HEART_ITEM);
        }
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(ModItems.SUN_BLADE);
            event.accept(ModItems.DESERT_HELMET);
            event.accept(ModItems.DESERT_CHESTPLATE);
            event.accept(ModItems.DESERT_LEGGINGS);
            event.accept(ModItems.DESERT_BOOTS);
        }
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.DESERT_COIN);
        }
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(ModItems.DESERT_STALKER_SPAWN_EGG);
            event.accept(ModItems.DESERT_COLOSSUS_SPAWN_EGG);
            event.accept(ModItems.DESERT_MASTER_SPAWN_EGG);
        }
    }
}
