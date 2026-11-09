package com.desertapocalypse.event;

import com.desertapocalypse.DesertApocalypseMod;
import com.desertapocalypse.ModEntities;
import com.desertapocalypse.entity.DesertColossusEntity;
import com.desertapocalypse.entity.DesertMasterEntity;
import com.desertapocalypse.entity.DesertStalkerEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DesertApocalypseMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModBusEvents {

    @SubscribeEvent
    public static void onAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.DESERT_STALKER.get(), DesertStalkerEntity.createAttributes().build());
        event.put(ModEntities.DESERT_COLOSSUS.get(), DesertColossusEntity.createAttributes().build());
        event.put(ModEntities.DESERT_MASTER.get(), DesertMasterEntity.createMasterAttributes().build());
    }

    private ModBusEvents() {}
}
