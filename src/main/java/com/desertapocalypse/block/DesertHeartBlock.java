package com.desertapocalypse.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/** Сердце Пустыни: неразрушимый источник апокалипсиса. */
public class DesertHeartBlock extends Block {
    public DesertHeartBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        for (int i = 0; i < 3; i++) {
            double x = pos.getX() + random.nextDouble();
            double y = pos.getY() + 1.0D;
            double z = pos.getZ() + random.nextDouble();
            level.addParticle(ParticleTypes.FLAME, x, y, z, 0.0D, 0.04D, 0.0D);
        }
        if (random.nextInt(3) == 0) {
            level.addParticle(ParticleTypes.LARGE_SMOKE,
                    pos.getX() + 0.5D, pos.getY() + 1.1D, pos.getZ() + 0.5D, 0.0D, 0.06D, 0.0D);
        }
    }
}
