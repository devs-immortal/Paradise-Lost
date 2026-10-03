package net.id.paradise_lost.world.feature.features;

import com.mojang.serialization.Codec;
import net.id.paradise_lost.world.feature.configs.FrozenAwareFeatureConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

/**
 * Adds short cascades where a river channel meets a 1–3 block terrace drop,
 * or a longer void spill at island cliffs. Requires nearby water so it does
 * not sheet-flood dry plateaus.
 */
public class VoidSpillFeature extends Feature<FrozenAwareFeatureConfig> {
    private static final int SMALL_MAX = 4;
    private static final int VOID_MIN = 8;

    public VoidSpillFeature(Codec<FrozenAwareFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<FrozenAwareFeatureConfig> context) {
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        boolean frozen = context.config().frozen();

        int x = context.origin().getX();
        int z = context.origin().getZ();
        int surfaceY = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z) - 1;
        BlockPos surface = new BlockPos(x, surfaceY, z);

        if (!hasNearbyWater(level, surface)) {
            return false;
        }

        for (Direction dir : Direction.Plane.HORIZONTAL) {
            int nx = x + dir.getStepX();
            int nz = z + dir.getStepZ();
            int neighborSurface = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, nx, nz) - 1;
            int terraceDrop = surfaceY - neighborSurface;
            if (terraceDrop >= 1 && terraceDrop <= SMALL_MAX) {
                return placeTerraceFall(level, surface, dir, terraceDrop, frozen, random);
            }
        }

        for (Direction dir : Direction.Plane.HORIZONTAL) {
            BlockPos edge = surface.relative(dir);
            int edgeSurface = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, edge.getX(), edge.getZ()) - 1;
            if (Math.abs(edgeSurface - surfaceY) > 2) {
                continue;
            }
            int drop = airDrop(level, edge);
            if (drop >= VOID_MIN) {
                return placeVoidFall(level, surface, dir, Math.min(drop, 6), frozen, random);
            }
        }
        return false;
    }

    private static boolean hasNearbyWater(WorldGenLevel level, BlockPos origin) {
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        for (int dx = -3; dx <= 3; dx++) {
            for (int dz = -3; dz <= 3; dz++) {
                for (int dy = -4; dy <= 1; dy++) {
                    cursor.set(origin.getX() + dx, origin.getY() + dy, origin.getZ() + dz);
                    if (level.getFluidState(cursor).is(FluidTags.WATER)
                            || level.getBlockState(cursor).is(Blocks.ICE)
                            || level.getBlockState(cursor).is(Blocks.PACKED_ICE)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static int airDrop(WorldGenLevel level, BlockPos start) {
        int drop = 0;
        BlockPos.MutableBlockPos cursor = start.mutable();
        while (drop < 24 && cursor.getY() > level.getMinBuildHeight() && level.isEmptyBlock(cursor)) {
            drop++;
            cursor.move(Direction.DOWN);
        }
        return drop;
    }

    private static boolean placeTerraceFall(
            WorldGenLevel level, BlockPos surface, Direction dir, int drop,
            boolean frozen, RandomSource random
    ) {
        BlockState water = Blocks.WATER.defaultBlockState();
        BlockState ice = Blocks.ICE.defaultBlockState();
        BlockPos.MutableBlockPos cursor = surface.mutable();
        // Thin stream over the lip, not a wide sheet
        for (int w = -1; w <= 1; w++) {
            Direction across = dir.getClockWise();
            BlockPos lip = surface.relative(across, w);
            level.setBlock(lip, frozen ? ice : water, Block.UPDATE_CLIENTS);
        }
        cursor.set(surface.relative(dir));
        for (int i = 0; i < drop; i++) {
            if (!level.isEmptyBlock(cursor) && !level.getBlockState(cursor).canBeReplaced()) {
                break;
            }
            level.setBlock(cursor, frozen && random.nextBoolean() ? ice : water, Block.UPDATE_CLIENTS);
            cursor.move(Direction.DOWN);
        }
        return true;
    }

    private static boolean placeVoidFall(
            WorldGenLevel level, BlockPos surface, Direction dir, int cascade,
            boolean frozen, RandomSource random
    ) {
        BlockState water = Blocks.WATER.defaultBlockState();
        BlockState ice = Blocks.ICE.defaultBlockState();
        BlockState packed = Blocks.PACKED_ICE.defaultBlockState();
        level.setBlock(surface, frozen ? ice : water, Block.UPDATE_CLIENTS);
        BlockPos.MutableBlockPos cursor = surface.relative(dir).mutable();
        for (int i = 0; i < cascade; i++) {
            if (!level.isEmptyBlock(cursor) && !level.getBlockState(cursor).canBeReplaced()) {
                break;
            }
            BlockState fill = frozen ? (random.nextInt(3) == 0 ? packed : ice) : water;
            level.setBlock(cursor, fill, Block.UPDATE_CLIENTS);
            cursor.move(Direction.DOWN);
        }
        return true;
    }
}
