package com.aether.world.feature;

import com.aether.blocks.AetherBlocks;
import com.aether.blocks.aercloud.BaseAercloudBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

import java.util.*;

public class QuicksoilFeature extends Feature<DefaultFeatureConfig> {

    public QuicksoilFeature() {
        super(DefaultFeatureConfig.CODEC);
    }

    @Override
    public boolean generate(StructureWorldAccess world, ChunkGenerator generator, Random random, BlockPos startPos, DefaultFeatureConfig config) {
        BlockPos origin = null;
        BlockPos.Mutable mut = new BlockPos.Mutable();

        for (int x = -16; x < 16; ++x) {
            for (int y = 20; y < 128; y++) {
                for (int z = -16; z < 16; ++z) {
                    mut.set(startPos);
                    mut.move(x, y, z);

                    if (world.getBlockState(mut).isAir() && world.getBlockState(mut.up()).isOf(AetherBlocks.AETHER_GRASS_BLOCK) && world.getBlockState(mut.up(2)).isAir()) {
                        origin = new BlockPos(mut);
                    }
                }
            }
        }

        if (origin == null) return false;

        startPos = new BlockPos(startPos.getX(), origin.getY(), startPos.getZ());

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
                        BlockState up;
                        if (world.getBlockState(mut).isAir() && !(up = world.getBlockState(mut.up())).isAir() && !(up.getBlock() instanceof BaseAercloudBlock) && mut.isWithinDistance(startPos, 24)) {
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

        ChunkRegion region = (ChunkRegion) world;

        List<int[]> positions = new ArrayList<>();

        int radius;
        if (centers.size() > 10) {
            for (BlockPos center : centers) {
                radius = random.nextInt(2)+4;

                for (int x = center.getX() - radius; x < center.getX() + radius; x++) {
                    for (int z = center.getZ() - radius; z < center.getZ() + radius; z++) {
                        mut.set(x, center.getY(), z);

                        if (region.isChunkLoaded(mut.getX() >> 4, mut.getZ() >> 4)) {
                            if (world.getBlockState(mut).isAir() && mut.isWithinDistance(center, radius)) {
                                positions.add(new int[] {mut.getX(), mut.getY(), mut.getZ()});
                            }
                        } else {
                            return false;
                        }
                    }
                }
            }
        }

        for (int[] pos : positions) {
            mut.set(pos[0], pos[1], pos[2]);
            this.setBlockState(world, mut, AetherBlocks.QUICKSOIL.getDefaultState());
        }

        return true;
    }
}