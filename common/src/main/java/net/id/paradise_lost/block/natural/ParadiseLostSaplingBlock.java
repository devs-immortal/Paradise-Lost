package net.id.paradise_lost.block.natural;

import net.id.paradise_lost.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockState;

public class ParadiseLostSaplingBlock extends SaplingBlock {

    public ParadiseLostSaplingBlock(TreeGrower generator, Properties settings) {
        super(generator, settings);
    }

    protected boolean mayPlaceOn(BlockState floor, BlockGetter world, BlockPos pos) {
        return (super.mayPlaceOn(floor, world, pos) || floor.is(BlockRegistry.MOSSY_FLOESTONE.get()));
    }
}
