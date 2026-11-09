package com.desertapocalypse.client;

import com.desertapocalypse.DesertApocalypseMod;
import com.desertapocalypse.entity.AbstractDesertMob;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;

/** Рендер твари пустыни: модель зомби/игрока со своей текстурой и масштабом. */
public class DesertMobRenderer extends HumanoidMobRenderer<AbstractDesertMob, HumanoidModel<AbstractDesertMob>> {
    private final ResourceLocation texture;
    private final float scale;

    protected DesertMobRenderer(EntityRendererProvider.Context context, String textureName, float scale) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.ZOMBIE)), 0.5F * scale);
        this.texture = new ResourceLocation(DesertApocalypseMod.MOD_ID, "textures/entity/" + textureName + ".png");
        this.scale = scale;
    }

    @Override
    public ResourceLocation getTextureLocation(AbstractDesertMob entity) {
        return texture;
    }

    @Override
    protected void scale(AbstractDesertMob entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(scale, scale, scale);
    }

    /** Песчаный скиталец: рост игрока. */
    public static final class Stalker extends DesertMobRenderer {
        public Stalker(EntityRendererProvider.Context context) {
            super(context, "desert_stalker", 1.0F);
        }
    }

    /** Песчаный колосс: 4.2 блока = 1.8 * 2.333. */
    public static final class Colossus extends DesertMobRenderer {
        public Colossus(EntityRendererProvider.Context context) {
            super(context, "desert_colossus", 4.2F / 1.8F);
        }
    }
}
