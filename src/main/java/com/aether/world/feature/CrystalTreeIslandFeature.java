package com.aether.world.feature;

import com.aether.blocks.AetherBlocks;
import com.aether.blocks.natural.AetherLeavesBlock;
import com.aether.blocks.natural.CrystalLeavesBlock;
import com.aether.world.feature.tree.CrystalSaplingGenerator;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class CrystalTreeIslandFeature extends Feature<DefaultFeatureConfig> {
    public CrystalTreeIslandFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    public boolean generate(StructureWorldAccess structureWorldAccess, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, DefaultFeatureConfig defaultFeatureConfig) {
        float f = (float)(random.nextInt(3) + 3);

        for(int y = 0; f > 0.5F; --y) {
            for(int x = MathHelper.floor(-f); x <= MathHelper.ceil(f); ++x) {
                for(int z = MathHelper.floor(-f); z <= MathHelper.ceil(f); ++z) {
                    if ((float)(x * x + z * z) <= (f + 1.0F) * (f + 1.0F)) {
                        if (y == 0) {
                            this.setBlockState(structureWorldAccess, blockPos.add(x, y, z), AetherBlocks.AETHER_GRASS_BLOCK.getDefaultState());
                            if (random.nextInt(6) == 0) {
                                this.setBlockState(structureWorldAccess, blockPos.add(x, y+1, z), AetherBlocks.AETHER_GRASS.getDefaultState());
                            }
                        } else if (y == -1) {
                            this.setBlockState(structureWorldAccess, blockPos.add(x, y, z), AetherBlocks.AETHER_DIRT.getDefaultState());
                        } else if (y == -2) {
                            if (random.nextBoolean()) {
                                this.setBlockState(structureWorldAccess, blockPos.add(x, y, z), AetherBlocks.AETHER_DIRT.getDefaultState());
                            } else {
                                this.setBlockState(structureWorldAccess, blockPos.add(x, y, z), AetherBlocks.HOLYSTONE.getDefaultState());
                            }
                        } else {
                            this.setBlockState(structureWorldAccess, blockPos.add(x, y, z), AetherBlocks.HOLYSTONE.getDefaultState());
                        }

                    }
                }
            }
            f = (float)((double)f - ((double)random.nextInt(2) + 0.5D));
        }

        BlockState[] states = new BlockState[] {
                AetherBlocks.CRYSTAL_LEAVES.getDefaultState().with(AetherLeavesBlock.DISTANCE, 1),
                AetherBlocks.CRYSTAL_LEAVES.getDefaultState().with(AetherLeavesBlock.DISTANCE, 1),
                AetherBlocks.CRYSTAL_LEAVES.getDefaultState().with(AetherLeavesBlock.DISTANCE, 1).with(CrystalLeavesBlock.FRUITY, true)

        };
        int[] leafRadii = new int[] {0, 3, 2, 1, 2, 1, 0, 1};

        for (int y = 1; y < 9; y++) {
            this.generateTreeCircle(structureWorldAccess, random, blockPos.up(y), leafRadii[y-1], states);
            this.setBlockState(structureWorldAccess, blockPos.up(y), AetherBlocks.CRYSTAL_LOG.getDefaultState());
        }
        this.setBlockState(structureWorldAccess, blockPos.up(9), AetherBlocks.CRYSTAL_LEAVES.getDefaultState().with(AetherLeavesBlock.DISTANCE, 1));

        return true;
    }

    private void generateTreeCircle(StructureWorldAccess structureWorldAccess, Random random, BlockPos blockPos, int radius, BlockState[] states) {
        if (radius < 1) { return; }
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                if (!(Math.abs(x) == radius && Math.abs(z) == radius) || (x == 0 && z == 0)) {
                    this.setBlockState(structureWorldAccess, blockPos.add(x, 0, z), randomBlock(random, states));
                }
            }
        }
    }

    private BlockState randomBlock(Random random, BlockState[] states) {
        return states[random.nextInt(states.length)];
    }
}
