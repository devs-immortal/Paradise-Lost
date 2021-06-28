package com.aether.blocks.natural;

import com.aether.blocks.AetherBlocks;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FernBlock;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import java.util.Random;
import java.util.Set;

public class AetherBrushBlock extends FernBlock {

    private final Set<Block> validFloors;
    private final boolean override;

    public AetherBrushBlock(Settings settings) {
        this(settings, ImmutableSet.of(), false);
    }

    public AetherBrushBlock(Settings settings, Set<Block> validFloors, boolean override) {
        super(settings);
        this.validFloors = validFloors;
        this.override = override;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        if(this == AetherBlocks.AETHER_GRASS) {
            TallPlantBlock tallPlantBlock = (TallPlantBlock) AetherBlocks.AETHER_TALL_GRASS;
            BlockState blockState = tallPlantBlock.getDefaultState();
            if (blockState.canPlaceAt(world, pos) && world.isAir(pos.up())) {
                tallPlantBlock.placeAt(world, blockState, pos, 2);
            }
        }
        Iterable<BlockPos> growPos = BlockPos.iterate(pos.add(-5, 3, -5), pos.add(5, -3, 5));
        growPos.forEach(target -> {
            if(world.isAir(target) && canPlaceAt(state, world, target) && random.nextInt(target.getManhattanDistance(pos) + 1) == 0)
                world.setBlockState(target, state);
        });
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        if(override)
            return validFloors.contains(floor.getBlock());
        return super.canPlantOnTop(floor, world, pos) || validFloors.contains(floor.getBlock());
    }

    @Override
    public OffsetType getOffsetType() {
        return OffsetType.XZ;
    }
}
