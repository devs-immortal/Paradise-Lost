package net.id.paradiselost.blocks.natural;

import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.SaplingGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class ParadiseLostSaplingBlock extends SaplingBlock {

    public ParadiseLostSaplingBlock(SaplingGenerator generator, Settings settings) {
        super(generator, settings);
    }

    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return (super.canPlantOnTop(floor, world, pos) || floor.isOf(ParadiseLostBlocks.MOSSY_FLOESTONE));
    }
}


