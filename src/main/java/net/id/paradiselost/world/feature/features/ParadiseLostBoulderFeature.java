package net.id.paradiselost.world.feature.features;

import com.mojang.serialization.Codec;
import net.id.paradiselost.tag.ParadiseLostBlockTags;
import net.id.paradiselost.world.feature.configs.BoulderFeatureConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class ParadiseLostBoulderFeature extends Feature<BoulderFeatureConfig> {

    public ParadiseLostBoulderFeature(Codec<BoulderFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<BoulderFeatureConfig> context) {
        BlockPos blockPos = context.getOrigin();
        StructureWorldAccess structureWorldAccess = context.getWorld();
        Random random = context.getRandom();

        var config = context.getConfig();

        for (; blockPos.getY() > structureWorldAccess.getBottomY() + 3; blockPos = blockPos.down()) {
            if (!structureWorldAccess.isAir(blockPos.down())) {
                BlockState blockState = structureWorldAccess.getBlockState(blockPos.down());
                if ((isSoil(blockState) || blockState.isIn(ParadiseLostBlockTags.BASE_PARADISE_LOST_STONE)) && random.nextBoolean()) {
                    break;
                }
            }
        }

        if (blockPos.getY() <= structureWorldAccess.getBottomY() + 3) {
            return false;
        } else {

            var tries = config.tries().get(random);
            var size = config.size().get(random);

            for (int i = 0; i < 3; ++i) {
                int j = random.nextInt(size);
                int k = random.nextInt(size);
                int l = random.nextInt(size);
                float f = (float) (j + k + l) * 0.333F + 0.5F;

                for (BlockPos bodyPos : BlockPos.iterate(blockPos.add(-j, -k, -l), blockPos.add(j, k, l))) {
                    if (bodyPos.getSquaredDistance(blockPos) <= (double) (f * f)) {
                        structureWorldAccess.setBlockState(bodyPos, config.body().getBlockState(random, bodyPos), Block.NO_REDRAW);
                    }
                }

                blockPos = blockPos.add(-1 + random.nextInt(4), -random.nextInt(4), -1 + random.nextInt(4));
            }

            return true;
        }
    }
}
