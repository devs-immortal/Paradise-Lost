package com.aether.blocks.decorative;

import com.aether.blocks.AetherBlocks;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class SkyrootFenceBlock extends FenceBlock {
    public SkyrootFenceBlock() {
        super(BlockBehaviour.Properties.copy(AetherBlocks.SKYROOT_PLANKS));
    }

    // TODO: Stubbed. Pending 1.17 rewrite.
//    @Override
//    public boolean isIn(Tag<Block> tag) {
//        return tag == BlockTags.FENCES || tag == BlockTags.WOODEN_FENCES || super.isIn(tag);
//    }
}