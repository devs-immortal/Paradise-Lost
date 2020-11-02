package com.aether.blocks.natural;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class EnchantedAetherGrassBlock extends AetherGrassBlock {
    public EnchantedAetherGrassBlock(Settings of) {
        super(of);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        super.randomDisplayTick(state, world, pos, random);
    }
}
