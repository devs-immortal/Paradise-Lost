package net.id.paradiselost.blocks.decorative;

import net.id.paradiselost.blocks.blockentity.ParadiseSignBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallSignBlock;
import net.minecraft.block.WoodType;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class ParadiseWallSignBlock extends WallSignBlock {
    public ParadiseWallSignBlock(Settings settings, WoodType woodType) {
        super(settings, woodType);
    }

    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ParadiseSignBlockEntity(pos, state);
    }
}
