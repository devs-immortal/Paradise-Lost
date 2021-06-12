package com.aether.blocks.decorative;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;

public class AetherWallBlock extends WallBlock {
    public AetherWallBlock(BlockState state) {
        super(FabricBlockSettings.copy(state.getBlock()));
    }

    // TODO: Stubbed. Pending 1.17 rewrite.
//    @Override
//    public boolean isIn(Tag<Block> tag) {
//        return tag == BlockTags.WALLS || super.isIn(tag);
//    }
}