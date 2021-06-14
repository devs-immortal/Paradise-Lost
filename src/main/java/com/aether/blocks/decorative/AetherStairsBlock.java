package com.aether.blocks.decorative;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;

public class AetherStairsBlock extends StairsBlock {
    public AetherStairsBlock(BlockState state) {
        super(state, AbstractBlock.Settings.copy(state.getBlock()));
    }

    // TODO: Stubbed. Pending 1.17 rewrite.
//    @Override
//    public boolean isIn(Tag<Block> tag) {
//        return tag == BlockTags.STAIRS || (tag == BlockTags.WOODEN_STAIRS && this == AetherBlocks.SKYROOT_STAIRS) || super.isIn(tag);
//    }
}