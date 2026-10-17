package net.id.paradise_lost.compat;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

/**
 * Wilder Wild's snow blanket walks down a column for as long as it only finds air. Over the void of Paradise Lost
 * that never ends, because blocks below the world still read as air, so the chunk generator thread spins forever.
 * This is the same search with the bottom of the world as a limit.
 */
public final class WilderWildSnowSearch {
    private static final TagKey<Block> SEARCH_THROUGH = TagKey.create(
            Registries.BLOCK,
            ResourceLocation.fromNamespaceAndPath("wilderwild", "snow_generation_can_search_through")
    );

    private WilderWildSnowSearch() {
    }

    public static int findLowestHeightForSnow(WorldGenLevel level, int x, int z, Operation<Integer> original) {
        int surface = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
        int minY = level.getMinBuildHeight();
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos(x, surface, z);

        if (surface - 1 >= minY && !canSearchThrough(level.getBlockState(cursor.setY(surface - 1)))) {
            return original.call(level, x, z);
        }

        int height = surface;
        while (height > minY && canSearchThrough(level.getBlockState(cursor.setY(height).move(Direction.DOWN)))) {
            height -= 1;
        }
        return height;
    }

    private static boolean canSearchThrough(BlockState state) {
        return (state.is(SEARCH_THROUGH) || state.isAir()) && state.getFluidState().isEmpty();
    }
}
