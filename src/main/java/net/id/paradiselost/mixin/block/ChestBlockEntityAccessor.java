package net.id.paradiselost.mixin.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@SuppressWarnings({"ConstantConditions", "unused"})
@Mixin(ChestBlockEntity.class)
public interface ChestBlockEntityAccessor{
    @Invoker("<init>") static ChestBlockEntity init(BlockEntityType<? extends ChestBlockEntity> blockEntityType, BlockPos pos, BlockState state){ return (ChestBlockEntity)new Object(); }
}
