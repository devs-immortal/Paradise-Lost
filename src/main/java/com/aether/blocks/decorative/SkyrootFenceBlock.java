package com.aether.blocks.decorative;

import com.aether.blocks.AetherBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.FenceBlock;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;

public class SkyrootFenceBlock extends FenceBlock {
    public SkyrootFenceBlock() {
        super(FabricBlockSettings.copy(AetherBlocks.SKYROOT_PLANKS));
    }

    // TODO: Stubbed. Pending 1.17 rewrite.
//    @Override
//    public boolean isIn(Tag<Block> tag) {
//        return tag == BlockTags.FENCES || tag == BlockTags.WOODEN_FENCES || super.isIn(tag);
//    }
}