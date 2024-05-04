package net.id.paradiselost.blocks.natural.crop;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

public class CropGrowthBlock extends Block {

    final int boostChance;

    public CropGrowthBlock(Settings settings, int boostChance) {
        super(settings);
        this.boostChance = boostChance;
    }

    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        BlockState upBlock = world.getBlockState(pos.up(2));
        if (upBlock.getBlock() instanceof CropBlock && random.nextInt(this.boostChance) == 0) {
            upBlock.randomTick(world, pos.up(2), random);
        }
    }
}
