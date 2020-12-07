package com.aether.blocks.natural;

import net.minecraft.block.Block;
import net.minecraft.sound.BlockSoundGroup;

public class AetherDirtBlock extends Block {

    public AetherDirtBlock(Settings settings) {
        super(settings.strength(0.3f).sounds(BlockSoundGroup.GRAVEL));
    }
}