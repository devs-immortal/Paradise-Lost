package net.id.paradiselost.recipe;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.input.RecipeInput;

public record TreeTapRecipeInput(ItemStack stack, BlockState tappedState) implements RecipeInput {

    @Override
    public ItemStack getStackInSlot(int slot) {
        return this.stack;
    }

    @Override
    public int size() {
        return 1;
    }
}
