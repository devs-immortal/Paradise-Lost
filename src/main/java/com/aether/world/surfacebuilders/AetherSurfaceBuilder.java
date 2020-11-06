package com.aether.world.surfacebuilders;

import java.util.Random;

import com.aether.blocks.AetherBlocks;
import com.mojang.serialization.Codec;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;

public class AetherSurfaceBuilder extends SurfaceBuilder<TernarySurfaceConfig> {
    public AetherSurfaceBuilder(Codec<TernarySurfaceConfig> codec) {
        super(codec);
    }

    @Override
    public void generate(Random var1, Chunk var2, Biome var3, int var4, int var5, int var6, double var7, BlockState var9, BlockState var10, int var11, long var12, TernarySurfaceConfig var14) {
        BlockState blockState_6 = AetherBlocks.AETHER_GRASS.getDefaultState();
        BlockState blockState_7 = AetherBlocks.AETHER_DIRT.getDefaultState();
        BlockPos.Mutable blockPos$Mutable_1 = new BlockPos.Mutable();

        int int_5 = -1;
        int int_6 = (int) (3.0D + var1.nextDouble() * 0.25D);

        for (int int_9 = 128; int_9 >= 0; --int_9) {
            blockPos$Mutable_1.set(var4, int_9, var5);

            BlockState blockState_8 = var2.getBlockState(blockPos$Mutable_1);

            if (blockState_8.isAir()) {
                int_5 = -1;
            } else if (blockState_8.getBlock() == var9.getBlock()) {
                if (int_5 == -1) {
                    if (int_6 <= 0) {
                        blockState_6 = Blocks.AIR.getDefaultState();
                        blockState_7 = var9;
                    }

                    int_5 = int_6;

                    if (int_9 >= 0) {
                        var2.setBlockState(blockPos$Mutable_1, blockState_6, false);
                    } else {
                        var2.setBlockState(blockPos$Mutable_1, blockState_7, false);
                    }
                } else if (int_5 > 0) {
                    --int_5;

                    var2.setBlockState(blockPos$Mutable_1, blockState_7, false);
                }
            }
        }
    }
}
