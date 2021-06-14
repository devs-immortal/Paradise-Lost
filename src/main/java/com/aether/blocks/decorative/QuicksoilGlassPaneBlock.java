package com.aether.blocks.decorative;

import com.aether.blocks.AetherBlocks;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Material;
import net.minecraft.block.PaneBlock;
import net.minecraft.sound.BlockSoundGroup;

public class QuicksoilGlassPaneBlock extends PaneBlock {
    public QuicksoilGlassPaneBlock() {
        super(AbstractBlock.Settings.of(Material.GLASS).luminance(state -> 14).strength(0.2F, -1.0F).sounds(BlockSoundGroup.GLASS).nonOpaque().solidBlock(AetherBlocks::never));
    }
}