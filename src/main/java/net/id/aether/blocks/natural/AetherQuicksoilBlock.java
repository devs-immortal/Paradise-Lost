package net.id.aether.blocks.natural;

import net.id.aether.blocks.AetherBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Random;

public class AetherQuicksoilBlock extends Block implements Fertilizable {
    public AetherQuicksoilBlock(Settings settings) {
        super(settings);
    }

    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        return world.getBlockState(pos.up()).isAir();
    }

    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        BlockPos blockPos = pos.up();
        BlockState blockState = AetherBlocks.FLUTEGRASS.getDefaultState();

        label48:
        for (int i = 0; i < 128; ++i) {
            BlockPos blockPos2 = blockPos;

            for (int j = 0; j < i / 16; ++j) {
                blockPos2 = blockPos2.add(random.nextInt(3) - 1, (random.nextInt(3) - 1) * random.nextInt(3) / 2, random.nextInt(3) - 1);
                if (!world.getBlockState(blockPos2.down()).isOf(this) || world.getBlockState(blockPos2).isFullCube(world, blockPos2))
                    continue label48;
            }

            BlockState blockState2 = world.getBlockState(blockPos2);
            if (blockState2.isOf(blockState.getBlock()) && random.nextInt(10) == 0)
                ((Fertilizable) blockState.getBlock()).grow(world, random, blockPos2, blockState2);

            if (blockState2.isAir() && blockState.canPlaceAt(world, blockPos2)) {
                world.setBlockState(blockPos2, blockState, Block.NOTIFY_ALL);
            }
        }
    }
}