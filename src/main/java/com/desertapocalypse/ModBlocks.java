package com.desertapocalypse;

import com.desertapocalypse.block.DesertHeartBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, DesertApocalypseMod.MOD_ID);

    public static final RegistryObject<Block> DESERT_HEART = BLOCKS.register("desert_heart",
            () -> new DesertHeartBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(-1.0F, 3600000.0F)
                    .sound(SoundType.STONE)
                    .lightLevel(state -> 12)));

    private ModBlocks() {}
}
