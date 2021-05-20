package com.aether.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.SingleStateFeatureConfig;

import java.util.Random;

public class AetherBoulderFeature extends Feature<SingleStateFeatureConfig> {

    public AetherBoulderFeature(Codec<SingleStateFeatureConfig> configCodec) {
        super(configCodec);
    }

    public boolean generate(StructureWorldAccess structureWorldAccess, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, SingleStateFeatureConfig singleStateFeatureConfig) {

        blockPos = structureWorldAccess.getTopPosition(Heightmap.Type.WORLD_SURFACE_WG, blockPos).add(random.nextInt(16), 0, random.nextInt(16));

        for(; blockPos.getY() > 3; blockPos = blockPos.down()) {
            if (!structureWorldAccess.isAir(blockPos.down())) {
                Block block = structureWorldAccess.getBlockState(blockPos.down()).getBlock();
                if (isSoil(block) || isStone(block)) {
                    break;
                }
            }
        }

        if (blockPos.getY() <= 3) {
            return false;
        } else {
            for(int i = 0; i < 3; ++i) {
                int j = random.nextInt(3);
                int k = random.nextInt(3);
                int l = random.nextInt(3);
                float f = (float)(j + k + l) * 0.333F + 0.5F;

                for (BlockPos blockPos2 : BlockPos.iterate(blockPos.add(-j, -k, -l), blockPos.add(j, k, l))) {
                    if (blockPos2.getSquaredDistance(blockPos) <= (double) (f * f)) {
                        structureWorldAccess.setBlockState(blockPos2, singleStateFeatureConfig.state, 4);
                    }
                }

                blockPos = blockPos.add(-1 + random.nextInt(3), -random.nextInt(2), -1 + random.nextInt(3));
            }

            return true;
        }
    }
}
