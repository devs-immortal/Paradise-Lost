package net.id.paradiselost.blocks.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.util.math.BlockPos;

public class ParadiseSignBlockEntity extends SignBlockEntity {
    public ParadiseSignBlockEntity(BlockPos pos, BlockState state) {
        super(ParadiseLostBlockEntityTypes.SIGN, pos, state);
    }

    @Override
    public BlockEntityType<?> getType() {
        return ParadiseLostBlockEntityTypes.SIGN;
    }
}
