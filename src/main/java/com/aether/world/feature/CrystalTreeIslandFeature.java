package com.aether.world.feature;

import com.aether.blocks.AetherBlocks;
import com.aether.blocks.natural.AetherLeavesBlock;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.Random;

public class CrystalTreeIslandFeature extends Feature<NoneFeatureConfiguration> {
    public CrystalTreeIslandFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        int checkSize = 4;
        for(int x = -checkSize; x >= checkSize; x++) {
            for(int y = -5; y >= 8; y++) {
                for(int z = -checkSize; z >= checkSize; z++) {
                    if (!context.level().getBlockState(context.origin().offset(x,y,z)).is(Blocks.AIR)) {
                        return false;
                    }
                }
            }
        }

        float f = (float)(context.random().nextInt(3) + 3);

        for(int y = 0; f > 0.5F; --y) {
            for(int x = Mth.floor(-f); x <= Mth.ceil(f); ++x) {
                for(int z = Mth.floor(-f); z <= Mth.ceil(f); ++z) {
                    if ((float)(x * x + z * z) <= (f + 1.0F) * (f + 1.0F)) {
                        if (y == 0) {
                            this.setBlock(context.level(), context.origin().offset(x, y, z), AetherBlocks.AETHER_GRASS_BLOCK.defaultBlockState());
                            if (context.random().nextInt(6) == 0) {
                                this.setBlock(context.level(), context.origin().offset(x, y+1, z), AetherBlocks.AETHER_GRASS.defaultBlockState());
                            }
                        } else if (y == -1) {
                            this.setBlock(context.level(), context.origin().offset(x, y, z), AetherBlocks.AETHER_DIRT.defaultBlockState());
                        } else if (y == -2) {
                            if (context.random().nextBoolean()) {
                                this.setBlock(context.level(), context.origin().offset(x, y, z), AetherBlocks.AETHER_DIRT.defaultBlockState());
                            } else {
                                this.setBlock(context.level(), context.origin().offset(x, y, z), AetherBlocks.HOLYSTONE.defaultBlockState());
                            }
                        } else {
                            this.setBlock(context.level(), context.origin().offset(x, y, z), AetherBlocks.HOLYSTONE.defaultBlockState());
                        }

                    }
                }
            }
            f = (float)((double)f - ((double)context.random().nextInt(2) + 0.5D));
        }
        int[] leafRadii = new int[] {0, 3, 2, 1, 2, 1, 0, 1};

        for (int y = 1; y < 9; y++) {
            this.generateTreeCircle(context.level(), context.random(), context.origin().above(y), leafRadii[y-1], AetherBlocks.CRYSTAL_LEAVES.defaultBlockState().setValue(AetherLeavesBlock.DISTANCE, 1));
            this.setBlock(context.level(), context.origin().above(y), AetherBlocks.CRYSTAL_LOG.defaultBlockState());
        }

        this.setBlock(context.level(), context.origin().above(9), AetherBlocks.CRYSTAL_LEAVES.defaultBlockState().setValue(AetherLeavesBlock.DISTANCE, 1));

        return true;
    }

    private void generateTreeCircle(WorldGenLevel structureWorldAccess, Random random, BlockPos blockPos, int radius, BlockState state) {
        if (radius < 1) { return; }
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                if (!(Math.abs(x) == radius && Math.abs(z) == radius) || (x == 0 && z == 0)) {
                    if (structureWorldAccess.getBlockState(blockPos.offset(x, 0, z)).is(Blocks.AIR)) {
                        this.setBlock(structureWorldAccess, blockPos.offset(x, 0, z), AetherBlocks.CRYSTAL_LEAVES.defaultBlockState().setValue(AetherLeavesBlock.DISTANCE, 1));
                    }
                }
            }
        }
    }
}
