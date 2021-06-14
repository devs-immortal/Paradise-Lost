package com.aether.blocks.decorative;

import com.aether.blocks.AetherBlocks;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.FenceBlock;

public class SkyrootFenceBlock extends FenceBlock {
    public SkyrootFenceBlock() {
        super(AbstractBlock.Settings.copy(AetherBlocks.SKYROOT_PLANKS));
    }

    // TODO: Stubbed. Pending 1.17 rewrite.
//    @Override
//    public boolean isIn(Tag<Block> tag) {
//        return tag == BlockTags.FENCES || tag == BlockTags.WOODEN_FENCES || super.isIn(tag);
//    }
}