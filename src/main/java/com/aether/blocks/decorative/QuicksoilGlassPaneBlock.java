package com.aether.blocks.decorative;

import com.aether.blocks.AetherBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.block.PaneBlock;
import net.minecraft.sound.BlockSoundGroup;

public class QuicksoilGlassPaneBlock extends PaneBlock {
    public QuicksoilGlassPaneBlock() {
        super(FabricBlockSettings.of(Material.GLASS).luminance(14).strength(0.2F, -1.0F).sounds(BlockSoundGroup.GLASS).nonOpaque().solidBlock(AetherBlocks::never));
    }
}
