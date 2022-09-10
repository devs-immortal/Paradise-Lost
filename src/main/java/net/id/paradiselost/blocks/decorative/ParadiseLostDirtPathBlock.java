package net.id.paradiselost.blocks.decorative;

import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirtPathBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

public class ParadiseLostDirtPathBlock extends DirtPathBlock {
    public ParadiseLostDirtPathBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        world.setBlockState(pos, pushEntitiesUpBeforeBlockChange(state, ParadiseLostBlocks.DIRT.getDefaultState(), world, pos));
    }
}
