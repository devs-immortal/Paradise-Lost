package com.aether.blocks.natural;

import com.aether.blocks.AetherBlocks;
import com.aether.entities.hostile.TransformableSwetEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class AetherSaplingBlock extends SaplingBlock {

    public AetherSaplingBlock(SaplingGenerator generator, Settings settings) {
        super(generator, settings);
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        world.setBlockState(pos, Blocks.AIR.getDefaultState());
        super.grow(world, random, pos, state);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        super.onEntityCollision(state, world, pos, entity);
        if (this == AetherBlocks.GOLDEN_OAK_SAPLING && entity instanceof TransformableSwetEntity swet) {
            swet.suggestTypeChange(world, pos, state);
        }
    }
}