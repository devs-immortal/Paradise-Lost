package com.aether.blocks.decorative;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallBlock;

public class AetherWallBlock extends WallBlock {
    public AetherWallBlock(BlockState state) {
        super(AbstractBlock.Settings.copy(state.getBlock()));
    }

    // TODO: Stubbed. Pending 1.17 rewrite.
//    @Override
//    public boolean isIn(Tag<Block> tag) {
//        return tag == BlockTags.WALLS || super.isIn(tag);
//    }
}