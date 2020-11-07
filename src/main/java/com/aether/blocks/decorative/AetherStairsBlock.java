package com.aether.blocks.decorative;

import com.aether.blocks.AetherBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public class AetherStairsBlock extends StairsBlock {
    public AetherStairsBlock(BlockState state) {
        super(state, FabricBlockSettings.copy(state.getBlock()));
    }

    @Override
    public boolean isIn(Tag<Block> tag) {
        return tag == BlockTags.STAIRS || (tag == BlockTags.WOODEN_STAIRS && this == AetherBlocks.skyroot_stairs) || super.isIn(tag);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public boolean isSideInvisible(BlockState blockState_1, BlockState blockState_2, Direction direction_1) {
        return this.isAerogel() && blockState_2.getBlock() == this && blockState_2.get(FACING) == blockState_1.get(FACING) || super.isSideInvisible(blockState_1, blockState_2, direction_1);
    }

    @Override
    public boolean isTranslucent(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1) {
        return this.isAerogel();
    }

    private boolean isAerogel() {
        return this == AetherBlocks.aerogel_stairs;
    }
}
