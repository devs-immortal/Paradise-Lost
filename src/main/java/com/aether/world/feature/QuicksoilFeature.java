package com.aether.world.feature;

import com.aether.blocks.AetherBlocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class QuicksoilFeature extends Feature<DefaultFeatureConfig> {

    public QuicksoilFeature() {
        super(DefaultFeatureConfig.CODEC);
    }

    @Override
    public boolean generate(StructureWorldAccess world, ChunkGenerator generator, Random random, BlockPos pos, DefaultFeatureConfig config) {
        BlockPos spawnPos = BlockPos.ORIGIN;

        for (int x = -3; x < 12; x++) {
            for (int z = -3; z < 12; z++) {
                for (int n = 20; n < 48; n++) {
                    BlockPos newPos = pos.add(x, n, z);

                    if (world.getBlockState(newPos).isAir() && world.getBlockState(newPos.up()).getBlock() == AetherBlocks.AETHER_GRASS && world.getBlockState(newPos.up(2)).isAir()) {
                        n += 128;

                        spawnPos = new BlockPos(newPos.getX(), n, newPos.getZ());
                    }
                }
            }
        }

        if (spawnPos.getY() < 128) {
            return false;
        }

        spawnPos = spawnPos.down(128);

        if (world.getBlockState(spawnPos.up()).isAir()) {
            return false;
        }

        for (int x = spawnPos.getX() - 3; x < spawnPos.getX() + 4; x++) {
            for (int z = spawnPos.getZ() - 3; z < spawnPos.getZ() + 4; z++) {
                BlockPos newPos = new BlockPos(x, spawnPos.getY(), z);

                if (world.getBlockState(newPos).isAir() && ((x - spawnPos.getX()) * (x - spawnPos.getX()) + (z - spawnPos.getZ()) * (z - spawnPos.getZ())) < 12) {
                    this.setBlockState(world, newPos, AetherBlocks.QUICKSOIL.getDefaultState());
                }
            }
        }

        return false;
    }
}
