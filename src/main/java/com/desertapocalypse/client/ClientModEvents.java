package com.desertapocalypse.client;

import com.desertapocalypse.DesertApocalypseMod;
import com.desertapocalypse.ModEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DesertApocalypseMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientModEvents {

    @SubscribeEvent
    public static void registerOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAbove(VanillaGuiOverlay.FOOD_LEVEL.id(), "thirst", HudOverlay.THIRST);
        event.registerAboveAll("apocalypse_info", HudOverlay.INFO);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.DESERT_STALKER.get(), DesertMobRenderer.Stalker::new);
        event.registerEntityRenderer(ModEntities.DESERT_COLOSSUS.get(), DesertMobRenderer.Colossus::new);
        event.registerEntityRenderer(ModEntities.DESERT_MASTER.get(), DesertMasterRenderer::new);
    }

    private ClientModEvents() {}
}
