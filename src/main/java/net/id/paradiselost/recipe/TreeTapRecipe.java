package net.id.paradiselost.recipe;

import net.id.paradiselost.blocks.blockentity.TreeTapBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class TreeTapRecipe implements Recipe<TreeTapBlockEntity> {

	protected final Identifier id;
	protected final String group;

	protected final Ingredient ingredient;
	protected final ItemStack output;
	protected final Block tappedBlock;

	public TreeTapRecipe(Identifier id, String group, Ingredient ingredient, ItemStack output, Block tappedBlock) {
		this.id = id;
		this.group = group;
		this.ingredient = ingredient;
		this.output = output;
		this.tappedBlock = tappedBlock;
	}

	@Override
	public boolean matches(TreeTapBlockEntity inventory, World world) {
		if(!ingredient.test(inventory.getStack(0))) {
			return false;
		}

		return inventory.getTappedState().isOf(this.tappedBlock);
	}

	@Override
	public ItemStack craft(TreeTapBlockEntity inventory) {
		return output.copy();
	}

	@Override
	public boolean fits(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getOutput() {
		return output;
	}

	@Override
	public String getGroup() {
		return group;
	}

	@Override
	public Identifier getId() {
		return id;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ParadiseLostRecipeTypes.TREE_TAP_RECIPE_SERIALIZER;
	}

	@Override
	public RecipeType<?> getType() {
		return ParadiseLostRecipeTypes.TREE_TAP_RECIPE_TYPE;
	}

}
