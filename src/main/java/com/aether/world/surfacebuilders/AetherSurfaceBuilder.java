package com.aether.world.surfacebuilders;

import com.aether.blocks.AetherBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;

import java.util.Random;

public class AetherSurfaceBuilder extends SurfaceBuilder<TernarySurfaceConfig> {
    public AetherSurfaceBuilder(Codec<TernarySurfaceConfig> codec) {
        super(codec);
    }

    @Override
    public void generate(Random random, Chunk chunk, Biome biome, int x, int z, int height, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, long seed, TernarySurfaceConfig surfaceBlocks) {
        BlockState topState = AetherBlocks.AETHER_GRASS.getDefaultState();
        BlockState subState = AetherBlocks.AETHER_DIRT.getDefaultState();
        BlockPos.Mutable mutableBlockPos = new BlockPos.Mutable();

        int int_5 = -1;
        int int_6 = (int) (3.0D + random.nextDouble() * 0.25D);

        for (int yLevel = 128; yLevel >= 0; --yLevel) {
            mutableBlockPos.set(x, yLevel, z);

            BlockState chunkBlockState = chunk.getBlockState(mutableBlockPos);

            if (chunkBlockState.isAir()) {
                int_5 = -1;
            } else if (chunkBlockState.getBlock() == defaultBlock.getBlock()) {
                if (int_5 == -1) {
                    if (int_6 <= 0) {
                        topState = Blocks.AIR.getDefaultState();
                        subState = defaultBlock;
                    }

                    int_5 = int_6;

                    if (yLevel >= 0) {
                        chunk.setBlockState(mutableBlockPos, topState, false);
                    } else {
                        chunk.setBlockState(mutableBlockPos, subState, false);
                    }
                } else if (int_5 > 0) {
                    --int_5;

                    chunk.setBlockState(mutableBlockPos, subState, false);
                }
            }
        }
    }
}
