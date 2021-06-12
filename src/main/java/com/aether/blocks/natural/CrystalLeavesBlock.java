package com.aether.blocks.natural;

import com.aether.blocks.AetherBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import java.util.Random;

public class CrystalLeavesBlock extends LeavesBlock {
    public static final BooleanProperty FRUITY = BooleanProperty.create("fruity");

    public CrystalLeavesBlock(Properties settings) {
        super(settings);
        this.registerDefaultState((this.stateDefinition.any()).setValue(FRUITY, false).setValue(PERSISTENT, false));
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return (state.getValue(DISTANCE) == 7 && !state.getValue(PERSISTENT)) || (!state.getValue(FRUITY));
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
        if (!state.getValue(PERSISTENT) && state.getValue(DISTANCE) == 7) {
            dropResources(state, world, pos);
            world.removeBlock(pos, false);
        } else if ((random.nextInt(60) == 0) && (!checkFruitiness(world, pos.above()) && !checkFruitiness(world, pos.below()) && !checkFruitiness(world, pos.north()) && !checkFruitiness(world, pos.east()) && !checkFruitiness(world, pos.south()) && !checkFruitiness(world, pos.west()))) {
            world.setBlockAndUpdate(pos, state.setValue(FRUITY, true));
        }

    }

    private boolean checkFruitiness(ServerLevel world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() == AetherBlocks.CRYSTAL_LEAVES && world.getBlockState(pos).getValue(FRUITY);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FRUITY);
    }
}
