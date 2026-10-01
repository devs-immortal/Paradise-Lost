package net.id.paradise_lost.world.feature.features;

import com.mojang.serialization.Codec;
import net.id.paradise_lost.tag.ParadiseLostBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.DeltaFeature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.DeltaFeatureConfiguration;
import java.util.HashSet;
import java.util.Set;

public class ParadiseLostDeltaFeature extends DeltaFeature {

    public ParadiseLostDeltaFeature(Codec<DeltaFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<DeltaFeatureConfiguration> context) {
        boolean modified = false;
        RandomSource random = context.random();
        WorldGenLevel world = context.level();
        DeltaFeatureConfiguration featureConfig = context.config();
        final BlockPos origin = context.origin();
        boolean bl2 = random.nextDouble() < 0.9D;
        int i = bl2 ? featureConfig.rimSize().sample(random) : 0;
        int j = bl2 ? featureConfig.rimSize().sample(random) : 0;
        boolean bl3 = bl2 && i != 0 && j != 0;

        int xSize = featureConfig.size().sample(random);
        int zSize = featureConfig.size().sample(random);
        int size = Math.max(xSize, zSize);

        Set<BlockPos> filledPositions = new HashSet<>();

        var rim = featureConfig.rim();
        var contents = featureConfig.contents();

        for (BlockPos currentPos : BlockPos.withinManhattan(origin, xSize, 0, zSize)) {
            if (currentPos.distManhattan(origin) > size) {
                break;
            }

            if (canPlace(world, currentPos, contents, filledPositions)) {
                if (bl3) {
                    modified = true;
                    setBlock(world, currentPos, rim);
                    filledPositions.add(currentPos);
                }

                BlockPos blockPos3 = currentPos.offset(i, 0, j);
                if (canPlace(world, blockPos3, contents, filledPositions)) {
                    modified = true;
                    setBlock(world, blockPos3, contents);
                    filledPositions.add(blockPos3);
                }
            }
        }

        return modified;
    }

    private static boolean canPlace(LevelAccessor world, BlockPos pos, BlockState contents, Set<BlockPos> filledPositions) {
        BlockState blockState = world.getBlockState(pos);

        if (!blockState.is(ParadiseLostBlockTags.FLUID_REPLACEABLES)) {
            return false;
        }

        if (blockState.is(contents.getBlock())) {
            return false;
        } else if (blockState.getDestroySpeed(world, pos) <= -1) {
            return false;
        } else {
            for (Direction direction : Direction.values()) {
                var currentPos = pos.relative(direction);
                if (filledPositions.contains(currentPos)) {
                    continue;
                }
                boolean isAir = !world.getBlockState(currentPos).isSolid();
                if (isAir && direction != Direction.UP || !isAir && direction == Direction.UP) {
                    return false;
                }
            }

            return true;
        }
    }
}
