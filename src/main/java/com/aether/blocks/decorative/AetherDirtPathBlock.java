package com.aether.blocks.decorative;

import com.aether.blocks.AetherBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirtPathBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class AetherDirtPathBlock extends DirtPathBlock {
    public AetherDirtPathBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        world.setBlockState(pos, pushEntitiesUpBeforeBlockChange(state, AetherBlocks.AETHER_DIRT.getDefaultState(), world, pos));
    }
}