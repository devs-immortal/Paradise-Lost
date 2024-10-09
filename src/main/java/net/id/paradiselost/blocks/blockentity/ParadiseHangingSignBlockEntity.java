package net.id.paradiselost.blocks.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.HangingSignBlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.util.math.BlockPos;

public class ParadiseHangingSignBlockEntity extends HangingSignBlockEntity {

    public ParadiseHangingSignBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(blockPos, blockState);
    }
    @Override
    public BlockEntityType<?> getType() {
        return ParadiseLostBlockEntityTypes.HANGING_SIGN;
    }
}
