package net.id.paradiselost.blocks.natural.tree;

import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

public class CrystalLeavesBlock extends LeavesBlock {
    public static final BooleanProperty FRUITY = BooleanProperty.of("fruity");

    public CrystalLeavesBlock(Settings settings) {
        super(settings);
        setDefaultState((stateManager.getDefaultState()).with(FRUITY, false).with(PERSISTENT, false));
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return (state.get(DISTANCE) == 7 && !state.get(PERSISTENT)) || (!state.get(FRUITY));
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!state.get(PERSISTENT) && state.get(DISTANCE) == 7) {
            dropStacks(state, world, pos);
            world.removeBlock(pos, false);
        } else if ((random.nextInt(60) == 0) && (!checkFruitiness(world, pos.up()) && !checkFruitiness(world, pos.down()) && !checkFruitiness(world, pos.north()) && !checkFruitiness(world, pos.east()) && !checkFruitiness(world, pos.south()) && !checkFruitiness(world, pos.west()))) {
            world.setBlockState(pos, state.with(FRUITY, true));
        }

    }

    private boolean checkFruitiness(ServerWorld world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() == ParadiseLostBlocks.CRYSTAL_LEAVES && world.getBlockState(pos).get(FRUITY);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FRUITY);
    }
}
