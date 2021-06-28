package com.aether.blocks.decorative;

import com.aether.blocks.AetherBlocks;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirtPathBlock;
import net.minecraft.block.Material;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class AetherDirtPathBlock extends DirtPathBlock {
    public AetherDirtPathBlock() {
        super(AbstractBlock.Settings.of(Material.SOIL).strength(0.65F, 0.65F).sounds(BlockSoundGroup.GRASS));
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        world.setBlockState(pos, pushEntitiesUpBeforeBlockChange(state, AetherBlocks.AETHER_DIRT.getDefaultState(), world, pos));
    }
}