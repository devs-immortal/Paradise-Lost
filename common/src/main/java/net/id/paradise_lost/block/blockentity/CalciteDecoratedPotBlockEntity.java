package net.id.paradise_lost.block.blockentity;

import net.id.paradise_lost.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.entity.PotDecorations;
import net.minecraft.world.level.block.state.BlockState;

public class CalciteDecoratedPotBlockEntity extends DecoratedPotBlockEntity {

    public CalciteDecoratedPotBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public BlockEntityType<?> getType() {
        return ParadiseLostBlockEntityTypes.CALCITE_DECORATED_POT.get();
    }

    @Override
    public boolean isValidBlockState(BlockState state) {
        return getType().isValid(state);
    }

    @Override
    public ItemStack getPotAsItem() {
        ItemStack stack = new ItemStack(BlockRegistry.CALCITE_DECORATED_POT.get());
        stack.applyComponents(collectComponents());
        return stack;
    }

    public static ItemStack getStackWith(PotDecorations sherds) {
        ItemStack stack = new ItemStack(BlockRegistry.CALCITE_DECORATED_POT.get());
        stack.set(DataComponents.POT_DECORATIONS, sherds);
        return stack;
    }
}
