package net.id.paradise_lost.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.block.state.BlockState;

public record TreeTapRecipeInput(ItemStack stack, BlockState tappedState) implements RecipeInput {
    @Override
    public ItemStack getItem(int slot) {
        return this.stack;
    }

    @Override
    public int size() {
        return 1;
    }
}
