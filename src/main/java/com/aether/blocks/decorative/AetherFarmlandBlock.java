package com.aether.blocks.decorative;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.FarmlandBlock;

public class AetherFarmlandBlock extends FarmlandBlock {
    public AetherFarmlandBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(MOISTURE, 0));
    }
}