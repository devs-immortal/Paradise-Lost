package com.aether.blocks.decorative;

import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class AetherFarmlandBlock extends FarmBlock {
    public AetherFarmlandBlock(BlockBehaviour.Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(MOISTURE, 0));
    }
}