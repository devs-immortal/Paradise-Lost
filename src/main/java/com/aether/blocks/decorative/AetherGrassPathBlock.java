package com.aether.blocks.decorative;

import com.aether.blocks.AetherBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.GrassPathBlock;
import net.minecraft.block.Material;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class AetherGrassPathBlock extends GrassPathBlock {
    public AetherGrassPathBlock() {
        super(FabricBlockSettings.of(Material.SOIL).strength(0.65F, 0.65F).sounds(BlockSoundGroup.GRASS));
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        world.setBlockState(pos, pushEntitiesUpBeforeBlockChange(state, AetherBlocks.AETHER_DIRT.getDefaultState(), world, pos));
    }
}