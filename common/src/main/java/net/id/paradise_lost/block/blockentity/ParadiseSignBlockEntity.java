package net.id.paradise_lost.block.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ParadiseSignBlockEntity extends SignBlockEntity {
    public ParadiseSignBlockEntity(BlockPos pos, BlockState state) {
        super(ParadiseLostBlockEntityTypes.SIGN.get(), pos, state);
    }

    @Override
    public BlockEntityType<?> getType() {
        return ParadiseLostBlockEntityTypes.SIGN.get();
    }
}
