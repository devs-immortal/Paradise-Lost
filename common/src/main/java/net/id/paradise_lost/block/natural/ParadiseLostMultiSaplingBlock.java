package net.id.paradise_lost.block.natural;

import com.mojang.datafixers.util.Pair;
import net.id.paradise_lost.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class ParadiseLostMultiSaplingBlock extends SaplingBlock {
    private final List<Pair<Block, TreeGrower>> altGenerators;

    public ParadiseLostMultiSaplingBlock(TreeGrower generator, Properties settings, List<Pair<Block, TreeGrower>> alts) {
        super(generator, settings);
        this.altGenerators = alts;
    }

    @Override
    protected boolean mayPlaceOn(BlockState floor, BlockGetter world, BlockPos pos) {
        return super.mayPlaceOn(floor, world, pos) || floor.is(BlockRegistry.MOSSY_FLOESTONE.get()) || floor.is(BlockRegistry.LIVERWORT.get());
    }

    @Override
    public void advanceTree(ServerLevel world, BlockPos pos, BlockState state, RandomSource random) {
        if (state.getValue(STAGE) == 0) {
            world.setBlock(pos, state.cycle(STAGE), Block.UPDATE_INVISIBLE);
        } else {
            boolean genned = false;
            for (Pair<Block, TreeGrower> pair : altGenerators) {
                if (world.getBlockState(pos.below()).is(pair.getFirst())) {
                    genned = true;
                    pair.getSecond().growTree(world, world.getChunkSource().getGenerator(), pos, state, random);
                }
            }
            if (!genned) {
                this.treeGrower.growTree(world, world.getChunkSource().getGenerator(), pos, state, random);
            }
        }
    }
}
