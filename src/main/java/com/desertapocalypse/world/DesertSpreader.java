package com.desertapocalypse.world;

import com.desertapocalypse.ApocalypseConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.DeadBushBlock;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.ArrayList;
import java.util.List;

/** Превращает мир в пустыню: трава/земля -> песок, камень -> песчаник, растения гибнут. */
public final class DesertSpreader {

    /** Вызывается каждый тик для каждого игрока: обрабатывает случайные колонки рядом с ним. */
    public static void spreadAround(ServerLevel level, ApocalypseData data, ServerPlayer player) {
        int range = ApocalypseConfig.SAMPLE_RANGE.get();
        double cx = data.center.getX() + 0.5D;
        double cz = data.center.getZ() + 0.5D;
        double pdx = player.getX() - cx;
        double pdz = player.getZ() - cz;
        double reach = data.radius + range;
        if (pdx * pdx + pdz * pdz > reach * reach) {
            return;
        }

        RandomSource random = level.getRandom();
        int samples = ApocalypseConfig.SAMPLES_PER_TICK.get();
        double r2 = data.radius * data.radius;
        int px = player.getBlockX();
        int pz = player.getBlockZ();
        BlockPos.MutableBlockPos probe = new BlockPos.MutableBlockPos();

        for (int i = 0; i < samples; i++) {
            int x = px + random.nextInt(range * 2 + 1) - range;
            int z = pz + random.nextInt(range * 2 + 1) - range;
            double dx = x + 0.5D - cx;
            double dz = z + 0.5D - cz;
            if (dx * dx + dz * dz > r2) {
                continue;
            }
            probe.set(x, 64, z);
            if (!level.hasChunkAt(probe)) {
                continue;
            }
            convertColumn(level, x, z);
        }
    }

    /** Превращает одну вертикальную колонку мира в пустыню. */
    public static void convertColumn(ServerLevel level, int x, int z) {
        int minY = level.getMinBuildHeight();
        int y = level.getHeight(Heightmap.Types.WORLD_SURFACE, x, z) - 1;
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        List<BlockPos> removals = null;
        BlockPos groundPos = null;
        BlockState ground = null;
        int steps = 0;

        while (y > minY && steps < 64) {
            pos.set(x, y, z);
            BlockState state = level.getBlockState(pos);
            if (state.isAir()) {
                y--;
                steps++;
                continue;
            }
            if (isRemovable(state)) {
                if (removals == null) {
                    removals = new ArrayList<>();
                }
                removals.add(pos.immutable());
                y--;
                steps++;
                continue;
            }
            ground = state;
            groundPos = pos.immutable();
            break;
        }

        if (ground == null) {
            return;
        }

        BlockState first = replacement(level, groundPos, ground, 0);
        if (first == null) {
            return; // нечто постороннее (постройка, руда...) - не трогаем
        }

        if (removals != null) {
            for (BlockPos p : removals) {
                level.setBlock(p, Blocks.AIR.defaultBlockState(), 2);
            }
        }

        if (first == ground) {
            return; // уже пустыня
        }

        boolean becameSand = false;
        for (int d = 0; d < 4; d++) {
            BlockPos p = groundPos.below(d);
            BlockState current = d == 0 ? ground : level.getBlockState(p);
            BlockState repl = d == 0 ? first : replacement(level, p, current, d);
            if (repl == null) {
                break;
            }
            if (repl != current) {
                level.setBlock(p, repl, 2);
                if (d == 0 && repl.is(Blocks.SAND)) {
                    becameSand = true;
                }
            }
        }

        if (becameSand) {
            decorate(level, groundPos.above());
        }
    }

    private static void decorate(ServerLevel level, BlockPos above) {
        if (!level.getBlockState(above).isAir()) {
            return;
        }
        RandomSource random = level.getRandom();
        int roll = random.nextInt(400);
        if (roll < 8) {
            level.setBlock(above, Blocks.DEAD_BUSH.defaultBlockState(), 2);
        } else if (roll == 8) {
            for (Direction dir : Direction.Plane.HORIZONTAL) {
                if (!level.getBlockState(above.relative(dir)).isAir()) {
                    return;
                }
            }
            level.setBlock(above, Blocks.CACTUS.defaultBlockState(), 2);
            if (random.nextBoolean() && level.getBlockState(above.above()).isAir()) {
                level.setBlock(above.above(), Blocks.CACTUS.defaultBlockState(), 2);
            }
        }
    }

    /** Блоки, которые "умирают" (исчезают) в пустыне. */
    private static boolean isRemovable(BlockState state) {
        Block block = state.getBlock();
        if (block instanceof DeadBushBlock) {
            return false;
        }
        return state.is(BlockTags.LEAVES)
                || state.is(BlockTags.LOGS)
                || state.is(BlockTags.FLOWERS)
                || state.is(BlockTags.SAPLINGS)
                || block instanceof BushBlock
                || block instanceof VineBlock
                || block instanceof SnowLayerBlock
                || block instanceof SugarCaneBlock
                || block instanceof HugeMushroomBlock
                || state.is(Blocks.PUMPKIN)
                || state.is(Blocks.MELON);
    }

    private static boolean isDesertBlock(BlockState state) {
        return state.is(BlockTags.SAND)
                || state.is(Blocks.SANDSTONE)
                || state.is(Blocks.RED_SANDSTONE)
                || state.is(Blocks.TERRACOTTA)
                || state.is(Blocks.RED_TERRACOTTA)
                || state.is(Blocks.ORANGE_TERRACOTTA)
                || state.is(Blocks.YELLOW_TERRACOTTA)
                || state.is(Blocks.WHITE_TERRACOTTA)
                || state.is(Blocks.BROWN_TERRACOTTA)
                || state.is(Blocks.LIGHT_GRAY_TERRACOTTA);
    }

    /**
     * На что заменить блок. null = блок не трогаем (и дальше вниз не идём);
     * тот же самый state = уже пустыня.
     */
    private static BlockState replacement(ServerLevel level, BlockPos pos, BlockState state, int depth) {
        if (isDesertBlock(state)) {
            return state;
        }
        if (depth == 0 && state.getBlock() instanceof LiquidBlock && state.getFluidState().is(FluidTags.WATER)) {
            return Blocks.SANDSTONE.defaultBlockState(); // вода "испаряется" в песчаную корку
        }
        if (state.is(BlockTags.DIRT)
                || state.is(Blocks.FARMLAND)
                || state.is(Blocks.DIRT_PATH)
                || state.is(Blocks.GRAVEL)
                || state.is(Blocks.CLAY)
                || state.is(Blocks.SNOW_BLOCK)
                || state.is(Blocks.ICE)
                || state.is(Blocks.PACKED_ICE)
                || state.is(Blocks.BLUE_ICE)
                || state.is(Blocks.POWDER_SNOW)) {
            BlockState below = level.getBlockState(pos.below());
            boolean unsupported = below.isAir() || !below.getFluidState().isEmpty();
            return unsupported ? Blocks.SANDSTONE.defaultBlockState() : Blocks.SAND.defaultBlockState();
        }
        if (depth <= 1 && state.is(BlockTags.BASE_STONE_OVERWORLD)) {
            return Blocks.SANDSTONE.defaultBlockState();
        }
        return null;
    }

    private DesertSpreader() {}
}
