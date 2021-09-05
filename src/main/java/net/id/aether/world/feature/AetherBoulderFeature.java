package net.id.aether.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.SingleStateFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.Iterator;
import java.util.Random;

public class AetherBoulderFeature extends Feature<SingleStateFeatureConfig> {

    public AetherBoulderFeature(Codec<SingleStateFeatureConfig> configCodec) {
        super(configCodec);
    }

    public boolean generate(FeatureContext<SingleStateFeatureConfig> context) {
        BlockPos blockPos = context.getOrigin();
        StructureWorldAccess structureWorldAccess = context.getWorld();
        blockPos = structureWorldAccess.getTopPosition(Heightmap.Type.WORLD_SURFACE_WG, blockPos);
        Random random = context.getRandom();

        if (random.nextFloat() >= 0.02F) {
            return false;
        }

        blockPos = blockPos.add(random.nextInt(16) - 8, 0, random.nextInt(16) - 8);

        SingleStateFeatureConfig singleStateFeatureConfig;
        for (singleStateFeatureConfig = context.getConfig(); blockPos.getY() > structureWorldAccess.getBottomY() + 3; blockPos = blockPos.down()) {
            if (!structureWorldAccess.isAir(blockPos.down())) {
                BlockState blockState = structureWorldAccess.getBlockState(blockPos.down());
                if ((isSoil(blockState) || isStone(blockState)) && random.nextBoolean()) {
                    break;
                }
            }
        }

        if (blockPos.getY() <= structureWorldAccess.getBottomY() + 3) {
            return false;
        } else {
            for (int i = 0; i < 3; ++i) {
                int j = random.nextInt(5);
                int k = random.nextInt(5);
                int l = random.nextInt(5);
                float f = (float) (j + k + l) * 0.333F + 0.5F;
                Iterator<BlockPos> var11 = BlockPos.iterate(blockPos.add(-j, -k, -l), blockPos.add(j, k, l)).iterator();

                while (var11.hasNext()) {
                    BlockPos blockPos2 = var11.next();
                    if (blockPos2.getSquaredDistance(blockPos) <= (double) (f * f)) {
                        structureWorldAccess.setBlockState(blockPos2, singleStateFeatureConfig.state, 4);
                    }
                }

                blockPos = blockPos.add(-1 + random.nextInt(4), -random.nextInt(4), -1 + random.nextInt(4));
            }

            return true;
        }
    }
}
