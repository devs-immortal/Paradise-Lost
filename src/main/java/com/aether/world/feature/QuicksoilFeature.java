package com.aether.world.feature;

import com.aether.blocks.AetherBlockProperties;
import com.aether.blocks.AetherBlocks;
import com.aether.blocks.aercloud.BaseAercloudBlock;
import com.aether.world.feature.config.DynamicConfiguration;
import com.aether.world.feature.config.QuicksoilConfig;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class QuicksoilFeature extends Feature<QuicksoilConfig> {

    private static final Codec<QuicksoilConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockState.CODEC.optionalFieldOf("state").forGetter(QuicksoilConfig::getOptionalState),
            Codec.STRING.optionalFieldOf("genType").forGetter(QuicksoilConfig::getGenString)
    ).apply(instance, QuicksoilConfig::new));

    public QuicksoilFeature() {
        super(CODEC);
    }

    @Override
    public boolean generate(FeatureContext<QuicksoilConfig> context) {
        if (context.getConfig().getGenType() == DynamicConfiguration.GeneratorType.LEGACY) {
            return createLegacyBlob(context.getWorld(), context.getConfig().state, context.getOrigin());
        } else {
            return createBlob(context);
        }
    }

    private boolean createLegacyBlob(StructureWorldAccess reader, BlockState state, BlockPos pos) {
        boolean doesProtrude = (
                (reader.getBlockState(pos.west(3)).isAir() ||
                        reader.getBlockState(pos.north(3)).isAir() ||
                        reader.getBlockState(pos.south(3)).isAir() ||
                        reader.getBlockState(pos.east(3)).isAir()) &&
                        (reader.getBlockState(pos).isOf(AetherBlocks.HOLYSTONE) ||
                                reader.getBlockState(pos).isOf(AetherBlocks.AETHER_DIRT))
        );
        if (doesProtrude) {
            for(int x = pos.getX() - 4; x < pos.getX() + 5; x++) {
                for(int z = pos.getZ() - 4; z < pos.getZ() + 5; z++) {
                    BlockPos newPos = new BlockPos(x, pos.getY(), z);

                    if((x - pos.getX()) * (x - pos.getX()) + (z - pos.getZ()) * (z - pos.getZ()) < 12) {
                        reader.setBlockState(newPos, state.with(AetherBlockProperties.DOUBLE_DROPS, true), 0);
                    }
                }

            }
        }

        return true;
    }

    private boolean createBlob(FeatureContext<QuicksoilConfig> context) {
        BlockPos origin = null;
        BlockPos.Mutable mut = new BlockPos.Mutable();

        BlockPos startPos = context.getOrigin();

        for (int x = -16; x < 16; ++x) {
            for (int y = 20; y < 128; y++) {
                for (int z = -16; z < 16; ++z) {
                    mut.set(startPos);
                    mut.move(x, y, z);

                    if (context.getWorld().getBlockState(mut).isAir() && context.getWorld().getBlockState(mut.up()).isOf(AetherBlocks.AETHER_GRASS_BLOCK) && context.getWorld().getBlockState(mut.up(2)).isAir()) {
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
                        if (context.getWorld().getBlockState(mut).isAir() && !(up = context.getWorld().getBlockState(mut.up())).isAir() && !(up.getBlock() instanceof BaseAercloudBlock) && mut.isWithinDistance(startPos, 24)) {
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

        ChunkRegion region = (ChunkRegion) context.getWorld();

        List<int[]> positions = new ArrayList<>();

        int radius;
        if (centers.size() > 10) {
            for (BlockPos center : centers) {
                radius = context.getRandom().nextInt(2)+4;

                for (int x = center.getX() - radius; x < center.getX() + radius; x++) {
                    for (int z = center.getZ() - radius; z < center.getZ() + radius; z++) {
                        mut.set(x, center.getY(), z);

                        if (region.isChunkLoaded(mut.getX() >> 4, mut.getZ() >> 4)) {
                            if (context.getWorld().getBlockState(mut).isAir() && mut.isWithinDistance(center, radius)) {
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
            this.setBlockState(context.getWorld(), mut, context.getConfig().state);
        }

        return true;
    }
}