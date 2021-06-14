package com.aether.blocks.decorative;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;

public class AetherSlabBlock extends SlabBlock {
    public AetherSlabBlock(BlockState state) {
        super(AbstractBlock.Settings.copy(state.getBlock()));
    }

    // TODO: Stubbed. Pending 1.17 rewrite.
//    @Override
//    public boolean isIn(Tag<Block> tag) {
//        return tag == BlockTags.SLABS || (tag == BlockTags.WOODEN_SLABS && this == AetherBlocks.SKYROOT_SLAB) || super.isIn(tag);
//    }
}