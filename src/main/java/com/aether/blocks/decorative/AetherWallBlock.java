package com.aether.blocks.decorative;

import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class AetherWallBlock extends WallBlock {
    public AetherWallBlock(BlockState state) {
        super(BlockBehaviour.Properties.copy(state.getBlock()));
    }

    // TODO: Stubbed. Pending 1.17 rewrite.
//    @Override
//    public boolean isIn(Tag<Block> tag) {
//        return tag == BlockTags.WALLS || super.isIn(tag);
//    }
}