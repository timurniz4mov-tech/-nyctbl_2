package com.desertapocalypse.client;

import com.desertapocalypse.DesertApocalypseMod;
import com.desertapocalypse.entity.DesertMasterEntity;
import net.minecraft.client.model.VillagerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class DesertMasterRenderer extends MobRenderer<DesertMasterEntity, VillagerModel<DesertMasterEntity>> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(DesertApocalypseMod.MOD_ID, "textures/entity/desert_master.png");

    public DesertMasterRenderer(EntityRendererProvider.Context context) {
        super(context, new VillagerModel<>(context.bakeLayer(ModelLayers.WANDERING_TRADER)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(DesertMasterEntity entity) {
        return TEXTURE;
    }
}
