package com.aether.blocks.decorative;

import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class AetherStairsBlock extends StairBlock {
    public AetherStairsBlock(BlockState state) {
        super(state, BlockBehaviour.Properties.copy(state.getBlock()));
    }

    // TODO: Stubbed. Pending 1.17 rewrite.
//    @Override
//    public boolean isIn(Tag<Block> tag) {
//        return tag == BlockTags.STAIRS || (tag == BlockTags.WOODEN_STAIRS && this == AetherBlocks.SKYROOT_STAIRS) || super.isIn(tag);
//    }
}