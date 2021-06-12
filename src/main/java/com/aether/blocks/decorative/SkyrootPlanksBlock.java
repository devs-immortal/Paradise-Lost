package com.aether.blocks.decorative;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;

public class SkyrootPlanksBlock extends Block {
    public SkyrootPlanksBlock() {
        super(FabricBlockSettings.of(Material.WOOD).strength(2.0F, 5.0F).sound(SoundType.WOOD));
    }

    // TODO: Stubbed. Pending 1.17 rewrite.
//    @Override
//    public boolean isIn(Tag<Block> tag) {
//        return tag == BlockTags.PLANKS || super.isIn(tag);
//    }
}