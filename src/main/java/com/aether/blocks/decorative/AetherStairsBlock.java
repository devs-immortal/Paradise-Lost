package com.aether.blocks.decorative;

import com.aether.blocks.AetherBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;

public class AetherStairsBlock extends StairBlock {
    public AetherStairsBlock(BlockState state) {
        super(state, FabricBlockSettings.copy(state.getBlock()));
    }

    // TODO: Stubbed. Pending 1.17 rewrite.
//    @Override
//    public boolean isIn(Tag<Block> tag) {
//        return tag == BlockTags.STAIRS || (tag == BlockTags.WOODEN_STAIRS && this == AetherBlocks.SKYROOT_STAIRS) || super.isIn(tag);
//    }
}