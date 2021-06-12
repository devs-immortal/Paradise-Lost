package com.aether.blocks.decorative;

import com.aether.blocks.AetherBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;

public class AetherSlabBlock extends SlabBlock {
    public AetherSlabBlock(BlockState state) {
        super(FabricBlockSettings.copy(state.getBlock()));
    }

    // TODO: Stubbed. Pending 1.17 rewrite.
//    @Override
//    public boolean isIn(Tag<Block> tag) {
//        return tag == BlockTags.SLABS || (tag == BlockTags.WOODEN_SLABS && this == AetherBlocks.SKYROOT_SLAB) || super.isIn(tag);
//    }
}