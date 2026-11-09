package com.desertapocalypse.client;

import com.desertapocalypse.DesertApocalypseMod;
import com.desertapocalypse.thirst.ClientData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Клиентские эффекты песчаной бури: плотный песчаный туман и летящий песок. */
@Mod.EventBusSubscriber(modid = DesertApocalypseMod.MOD_ID, value = Dist.CLIENT)
public final class StormClient {
    private static float intensity = 0.0F;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        ClientLevel level = mc.level;
        if (mc.player == null || level == null) {
            intensity = 0.0F;
            return;
        }
        // под крышей бури не видно и не слышно
        boolean target = ClientData.storm && level.canSeeSky(mc.player.blockPosition());
        intensity = Mth.clamp(intensity + (target ? 0.01F : -0.02F), 0.0F, 1.0F);

        if (intensity > 0.05F && !mc.isPaused()) {
            BlockParticleOption sand = new BlockParticleOption(ParticleTypes.BLOCK, Blocks.SAND.defaultBlockState());
            int count = (int) (intensity * 16.0F);
            for (int i = 0; i < count; i++) {
                double x = mc.player.getX() + (level.random.nextDouble() - 0.5D) * 24.0D;
                double y = mc.player.getY() + level.random.nextDouble() * 8.0D - 1.0D;
                double z = mc.player.getZ() + (level.random.nextDouble() - 0.5D) * 24.0D;
                level.addParticle(sand, x, y, z,
                        1.2D + level.random.nextDouble(), 0.05D, (level.random.nextDouble() - 0.5D) * 0.4D);
            }
        }
    }

    @SubscribeEvent
    public static void onRenderFog(ViewportEvent.RenderFog event) {
        if (intensity <= 0.01F) {
            return;
        }
        event.setNearPlaneDistance(Mth.lerp(intensity, event.getNearPlaneDistance(), 0.0F));
        event.setFarPlaneDistance(Mth.lerp(intensity, event.getFarPlaneDistance(), 18.0F));
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onFogColor(ViewportEvent.ComputeFogColor event) {
        if (intensity <= 0.01F) {
            return;
        }
        event.setRed(Mth.lerp(intensity, event.getRed(), 0.74F));
        event.setGreen(Mth.lerp(intensity, event.getGreen(), 0.60F));
        event.setBlue(Mth.lerp(intensity, event.getBlue(), 0.36F));
    }

    private StormClient() {}
}
