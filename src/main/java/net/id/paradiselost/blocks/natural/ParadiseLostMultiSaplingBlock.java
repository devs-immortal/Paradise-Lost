package net.id.paradiselost.blocks.natural;

import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.SaplingGenerator;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;

import java.util.List;

public class ParadiseLostMultiSaplingBlock extends SaplingBlock {


    private List<Pair<Block, SaplingGenerator>> altGenerators;

    public ParadiseLostMultiSaplingBlock(SaplingGenerator generator, Settings settings, List<Pair<Block, SaplingGenerator>> alts) {
        super(generator, settings);
        this.altGenerators = alts;

    }

    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return (super.canPlantOnTop(floor, world, pos) || floor.isOf(ParadiseLostBlocks.MOSSY_FLOESTONE) || floor.isOf(ParadiseLostBlocks.LIVERWORT) );
    }

    public void generate(ServerWorld world, BlockPos pos, BlockState state, Random random) {
        if (state.get(STAGE) == 0) {
            world.setBlockState(pos, state.cycle(STAGE), Block.NO_REDRAW);
        } else {
            boolean genned = false;
            for (Pair<Block, SaplingGenerator> p : altGenerators) {
                if (world.getBlockState(pos.down()).isOf(p.getLeft())) {
                    genned = true;
                    p.getRight().generate(world, world.getChunkManager().getChunkGenerator(), pos, state, random);
                }
            }
            if (!genned) {
                this.generator.generate(world, world.getChunkManager().getChunkGenerator(), pos, state, random);
            }
        }
    }
}
