package com.aether.blocks.natural.ore;

import com.aether.blocks.FloatingBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;

public class GravititeOreBlock extends FloatingBlock {

    public GravititeOreBlock() {
        // Note: Any Blocks using this should also be appropriately tagged as follows:
        // - MINEABLE_WITH_PICKAXE
        // - NEEDS_IRON_TOOL
        super(false, AbstractBlock.Settings.of(Material.STONE).strength(5.0F).sounds(BlockSoundGroup.STONE));
    }

//    @Override
//    protected void onStartFloating(FloatingBlockEntity entityIn) {
//        entityIn.dropItem = false;
//    }
}