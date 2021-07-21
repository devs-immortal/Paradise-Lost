package com.aether.blocks.natural;

import com.aether.blocks.AetherBlocks;
import com.aether.entities.hostile.TransformableSwetEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AetherFlowerPotBlock extends FlowerPotBlock {
    public AetherFlowerPotBlock(Block content, Settings settings) {
        super(content, settings);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        super.onEntityCollision(state, world, pos, entity);
        if (this == AetherBlocks.POTTED_GOLDEN_OAK_SAPLING && entity instanceof TransformableSwetEntity swet) {
            swet.suggestTypeChange(world, pos, state);
        }
    }
}
