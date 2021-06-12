package com.aether.blocks.natural;

import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;

public class MultisizeSaplingBlock extends AetherSaplingBlock {

    public MultisizeSaplingBlock(AbstractTreeGrower generator, Properties settings) {
        super(generator, settings);
    }

    @Override
    public void performBonemeal(ServerLevel world, Random random, BlockPos pos, BlockState state) {
        super.performBonemeal(world, random, pos, state);
    }
}
