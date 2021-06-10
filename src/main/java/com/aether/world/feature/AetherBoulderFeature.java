package com.aether.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.SingleStateFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class AetherBoulderFeature extends Feature<SingleStateFeatureConfig> {

    public AetherBoulderFeature(Codec<SingleStateFeatureConfig> configCodec) {
        super(configCodec);
    }

    public boolean generate(FeatureContext<SingleStateFeatureConfig> context) {
        BlockPos blockPos = context.getOrigin();
        blockPos = context.getWorld().getTopPosition(Heightmap.Type.WORLD_SURFACE_WG, blockPos).add(context.getRandom().nextInt(16), 0, context.getRandom().nextInt(16));

        for(; blockPos.getY() > 3; blockPos = blockPos.down()) {
            if (!context.getWorld().isAir(blockPos.down())) {
                BlockState block = context.getWorld().getBlockState(blockPos.down());
                if (isSoil(block) || isStone(block)) {
                    break;
                }
            }
        }

        if (blockPos.getY() <= 3) {
            return false;
        } else {
            for(int i = 0; i < 3; ++i) {
                int j = context.getRandom().nextInt(3);
                int k = context.getRandom().nextInt(3);
                int l = context.getRandom().nextInt(3);
                float f = (float)(j + k + l) * 0.333F + 0.5F;

                for (BlockPos blockPos2 : BlockPos.iterate(blockPos.add(-j, -k, -l), blockPos.add(j, k, l))) {
                    if (blockPos2.getSquaredDistance(blockPos) <= (double) (f * f)) {
                        context.getWorld().setBlockState(blockPos2, context.getConfig().state, 4);
                    }
                }

                blockPos = blockPos.add(-1 + context.getRandom().nextInt(3), -context.getRandom().nextInt(2), -1 + context.getRandom().nextInt(3));
            }

            return true;
        }
    }
}
