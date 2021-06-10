package com.aether.blocks.decorative;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;

public class SkyrootPlanksBlock extends Block {
    public SkyrootPlanksBlock() {
        super(FabricBlockSettings.of(Material.WOOD).strength(2.0F, 5.0F).sounds(BlockSoundGroup.WOOD));
    }

    // TODO: Stubbed. Pending 1.17 rewrite.
//    @Override
//    public boolean isIn(Tag<Block> tag) {
//        return tag == BlockTags.PLANKS || super.isIn(tag);
//    }
}