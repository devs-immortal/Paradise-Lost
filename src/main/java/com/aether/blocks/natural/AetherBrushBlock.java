package com.aether.blocks.natural;

import com.aether.blocks.AetherBlocks;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.*;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.TallGrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Random;
import java.util.Set;

public class AetherBrushBlock extends TallGrassBlock {

    private final Set<Block> validFloors;
    private final boolean override;

    public AetherBrushBlock(Properties settings) {
        this(settings, ImmutableSet.of(), false);
    }

    public AetherBrushBlock(Properties settings, Set<Block> validFloors, boolean override) {
        super(settings);
        this.validFloors = validFloors;
        this.override = override;
    }

    @Override
    public void performBonemeal(ServerLevel world, Random random, BlockPos pos, BlockState state) {
        if(this == AetherBlocks.AETHER_GRASS) {
            DoublePlantBlock tallPlantBlock = (DoublePlantBlock) AetherBlocks.AETHER_TALL_GRASS;
            BlockState blockState = tallPlantBlock.defaultBlockState();
            if (blockState.canSurvive(world, pos) && world.isEmptyBlock(pos.above())) {
                tallPlantBlock.placeAt(world, blockState, pos, 2);
            }
        }
        Iterable<BlockPos> growPos = BlockPos.betweenClosed(pos.offset(-5, 3, -5), pos.offset(5, -3, 5));
        growPos.forEach(target -> {
            if(world.isEmptyBlock(target) && canSurvive(state, world, target) && random.nextInt(target.distManhattan(pos) + 1) == 0)
                world.setBlockAndUpdate(target, state);
        });
    }

    @Override
    protected boolean mayPlaceOn(BlockState floor, BlockGetter world, BlockPos pos) {
        if(override)
            return validFloors.contains(floor.getBlock());
        return super.mayPlaceOn(floor, world, pos) || validFloors.contains(floor.getBlock());
    }

    @Override
    public OffsetType getOffsetType() {
        return OffsetType.XZ;
    }
}
