package net.id.paradise_lost.block.natural.crop;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;

public class CropGrowthBlock extends Block {

    final int boostChance;

    public CropGrowthBlock(Properties settings, int boostChance) {
        super(settings);
        this.boostChance = boostChance;
    }

    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        BlockState upBlock = world.getBlockState(pos.above(2));
        if (upBlock.getBlock() instanceof CropBlock && random.nextInt(this.boostChance) == 0) {
            upBlock.randomTick(world, pos.above(2), random);
        }
    }
}
