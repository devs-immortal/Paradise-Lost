package com.aether.blocks.decorative;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallBlock;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;

public class AetherWallBlock extends WallBlock {
    public AetherWallBlock(BlockState state) {
        super(FabricBlockSettings.copy(state.getBlock()));
    }

    @Override
    public boolean isIn(Tag<Block> tag) {
        return tag == BlockTags.WALLS || super.isIn(tag);
    }
}