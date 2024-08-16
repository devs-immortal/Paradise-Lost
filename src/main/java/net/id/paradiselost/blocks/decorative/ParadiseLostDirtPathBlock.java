package net.id.paradiselost.blocks.decorative;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirtPathBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

import java.util.function.Supplier;

public class ParadiseLostDirtPathBlock extends DirtPathBlock {

    private Supplier<Block> returnTo;

    public ParadiseLostDirtPathBlock(Settings settings, Supplier<Block> returnTo) {
        super(settings);
        this.returnTo = returnTo;
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        world.setBlockState(pos, pushEntitiesUpBeforeBlockChange(state, this.returnTo.get().getDefaultState(), world, pos));
    }
}
