package com.aether.blocks.natural;

import com.aether.blocks.AetherBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FernBlock;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class AetherBrushBlock extends FernBlock {

    public AetherBrushBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        if(this == AetherBlocks.AETHER_GRASS) {
            TallPlantBlock tallPlantBlock = (TallPlantBlock) AetherBlocks.AETHER_TALL_GRASS;
            if (tallPlantBlock.getDefaultState().canPlaceAt(world, pos) && world.isAir(pos.up())) {
                tallPlantBlock.placeAt(world, pos, 2);
            }
        }
        Iterable<BlockPos> growPos = BlockPos.iterate(pos.add(-5, 3, -5), pos.add(5, -3, 5));
        growPos.forEach(target -> {
            if(world.isAir(target) && canPlaceAt(state, world, target) && random.nextInt(target.getManhattanDistance(pos) + 1) == 0)
                world.setBlockState(target, state);
        });
    }

    @Override
    public OffsetType getOffsetType() {
        return OffsetType.XZ;
    }
}
