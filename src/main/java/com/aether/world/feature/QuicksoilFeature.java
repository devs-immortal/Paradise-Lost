package com.aether.world.feature;

import com.aether.blocks.AetherBlocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

public class QuicksoilFeature extends Feature<DefaultFeatureConfig> {

    public QuicksoilFeature() {
        super(DefaultFeatureConfig.CODEC);
    }

    @Override
    public boolean generate(StructureWorldAccess world, ChunkGenerator generator, Random random, BlockPos pos, DefaultFeatureConfig config) {
        BlockPos origin = null;
        BlockPos.Mutable mut = new BlockPos.Mutable();

        for (int x = -16; x < 16; ++x) {
            for (int y = 20; y < 128; y++) {
                for (int z = -16; z < 16; ++z) {
                    mut.set(pos);
                    mut.move(x, y, z);

                    if (world.getBlockState(mut).isAir() && world.getBlockState(mut.up()).isOf(AetherBlocks.AETHER_GRASS_BLOCK) && world.getBlockState(mut.up(2)).isAir()) {
                        origin = new BlockPos(mut);
                    }
                }
            }
        }

        if (origin == null) return false;

        Collection<BlockPos> centers = new HashSet<>();

        Direction[] directions = new Direction[] {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};

        Collection<BlockPos> visited = new HashSet<>();
        Collection<BlockPos> nextStops = new HashSet<>();
        nextStops.add(origin);

        while (!nextStops.isEmpty()) {
            BlockPos stop = nextStops.iterator().next();

            for (int i = 1; i < 5; ++i) {
                for (Direction direction : directions) {
                    mut.set(stop);
                    mut.move(direction, i);

                    if (!visited.contains(mut) && !centers.contains(mut)) {
                        if (world.getBlockState(mut).isAir() && !world.getBlockState(mut.up()).isAir()) {
                            BlockPos p = new BlockPos(mut);
                            nextStops.add(p);
                            centers.add(p);
                        }
                    }

                    visited.add(new BlockPos(mut));
                }
            }

            nextStops.remove(stop);
            visited.add(stop);
        }

        mut.set(origin);

        int radius = 6;
        if (centers.size() > 10) {
            for (BlockPos center : centers) {
                radius = MathHelper.clamp(radius + (random.nextInt(5) - 2) * 2, 2, 7);

                for (int x = center.getX() - radius; x < center.getX() + radius; x++) {
                    for (int z = center.getZ() - radius; z < center.getZ() + radius; z++) {
                        mut.set(x, center.getY(), z);

                        if (world.getBlockState(mut).isAir() && mut.isWithinDistance(center, radius)) {
                            this.setBlockState(world, mut, AetherBlocks.QUICKSOIL.getDefaultState());
                        }
                    }
                }
            }
        }

        return false;
    }
}