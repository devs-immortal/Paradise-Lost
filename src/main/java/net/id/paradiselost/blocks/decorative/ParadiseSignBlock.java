package net.id.paradiselost.blocks.decorative;

import net.id.paradiselost.blocks.blockentity.ParadiseSignBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.SignBlock;
import net.minecraft.block.WoodType;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class ParadiseSignBlock extends SignBlock {

    public ParadiseSignBlock(Settings settings, WoodType woodType) {
        super(settings, woodType);
    }

    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ParadiseSignBlockEntity(pos, state);
    }
}
