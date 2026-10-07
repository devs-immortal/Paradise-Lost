package net.id.paradise_lost.block.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ParadiseHangingSignBlockEntity extends HangingSignBlockEntity {

    public ParadiseHangingSignBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(blockPos, blockState);
    }
    @Override
    public BlockEntityType<?> getType() {
        return ParadiseLostBlockEntityTypes.HANGING_SIGN.get();
    }

    @Override
    public boolean isValidBlockState(BlockState state) {
        return getType().isValid(state);
    }
}
