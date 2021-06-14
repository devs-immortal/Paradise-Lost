package com.aether.blocks.decorative;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;

public class SkyrootPlanksBlock extends Block {
    public SkyrootPlanksBlock() {
        super(AbstractBlock.Settings.of(Material.WOOD).strength(2.0F, 5.0F).sounds(BlockSoundGroup.WOOD));
    }

    // TODO: Stubbed. Pending 1.17 rewrite.
//    @Override
//    public boolean isIn(Tag<Block> tag) {
//        return tag == BlockTags.PLANKS || super.isIn(tag);
//    }
}