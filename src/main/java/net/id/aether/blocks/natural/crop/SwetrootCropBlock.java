package net.id.aether.blocks.natural.crop;

import net.id.aether.blocks.AetherBlocks;
import net.id.aether.items.AetherItems;
import net.id.aether.tag.AetherBlockTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
import net.minecraft.item.ItemConvertible;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;

import java.util.Random;

public class SwetrootCropBlock extends CropBlock {

    public SwetrootCropBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (world.getBlockState(pos.up().up()).isOf(Blocks.WATER)) {
            super.randomTick(state, world, pos, random);
        }
    }

    @Override
    protected ItemConvertible getSeedsItem() {
        return AetherItems.SWEDROOT;
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return canPlantOnTop(Blocks.AIR.getDefaultState(), world, pos);
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return world.getBlockState(pos.up()).isIn(AetherBlockTags.SWEDROOT_PLANTABLE);
    }
}
