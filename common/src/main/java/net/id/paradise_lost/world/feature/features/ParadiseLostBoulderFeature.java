package net.id.paradise_lost.world.feature.features;

import com.mojang.serialization.Codec;
import net.id.paradise_lost.tag.ParadiseLostBlockTags;
import net.id.paradise_lost.world.feature.configs.BoulderFeatureConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class ParadiseLostBoulderFeature extends Feature<BoulderFeatureConfig> {

    public ParadiseLostBoulderFeature(Codec<BoulderFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<BoulderFeatureConfig> context) {
        BlockPos blockPos = context.origin();
        WorldGenLevel structureWorldAccess = context.level();
        RandomSource random = context.random();

        var config = context.config();

        for (; blockPos.getY() > structureWorldAccess.getMinY() + 3; blockPos = blockPos.below()) {
            if (!structureWorldAccess.isEmptyBlock(blockPos.below())) {
                BlockState blockState = structureWorldAccess.getBlockState(blockPos.below());
                if ((isDirt(blockState) || blockState.is(ParadiseLostBlockTags.BASE_PARADISE_LOST_STONE)) && random.nextBoolean()) {
                    break;
                }
            }
        }

        if (blockPos.getY() <= structureWorldAccess.getMinY() + 3) {
            return false;
        } else {

            var tries = config.tries().sample(random);
            var size = config.size().sample(random);

            for (int i = 0; i < 3; ++i) {
                int j = random.nextInt(size);
                int k = random.nextInt(size);
                int l = random.nextInt(size);
                float f = (float) (j + k + l) * 0.333F + 0.5F;

                for (BlockPos bodyPos : BlockPos.betweenClosed(blockPos.offset(-j, -k, -l), blockPos.offset(j, k, l))) {
                    if (bodyPos.distSqr(blockPos) <= (double) (f * f)) {
                        structureWorldAccess.setBlock(bodyPos, config.body().getState(random, bodyPos), Block.UPDATE_INVISIBLE);
                    }
                }

                blockPos = blockPos.offset(-1 + random.nextInt(4), -random.nextInt(4), -1 + random.nextInt(4));
            }

            return true;
        }
    }
}
