package com.aether.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;

public class AetherBoulderFeature extends Feature<BlockStateConfiguration> {

    public AetherBoulderFeature(Codec<BlockStateConfiguration> configCodec) {
        super(configCodec);
    }

    public boolean place(FeaturePlaceContext<BlockStateConfiguration> context) {
        BlockPos blockPos = context.origin();
        blockPos = context.level().getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, blockPos).offset(context.random().nextInt(16), 0, context.random().nextInt(16));

        for(; blockPos.getY() > 3; blockPos = blockPos.below()) {
            if (!context.level().isEmptyBlock(blockPos.below())) {
                BlockState block = context.level().getBlockState(blockPos.below());
                if (isDirt(block) || isStone(block)) {
                    break;
                }
            }
        }

        if (blockPos.getY() <= 3) {
            return false;
        } else {
            for(int i = 0; i < 3; ++i) {
                int j = context.random().nextInt(3);
                int k = context.random().nextInt(3);
                int l = context.random().nextInt(3);
                float f = (float)(j + k + l) * 0.333F + 0.5F;

                for (BlockPos blockPos2 : BlockPos.betweenClosed(blockPos.offset(-j, -k, -l), blockPos.offset(j, k, l))) {
                    if (blockPos2.distSqr(blockPos) <= (double) (f * f)) {
                        context.level().setBlock(blockPos2, context.config().state, 4);
                    }
                }

                blockPos = blockPos.offset(-1 + context.random().nextInt(3), -context.random().nextInt(2), -1 + context.random().nextInt(3));
            }

            return true;
        }
    }
}
