package net.id.paradiselost.recipe;

import net.id.paradiselost.ParadiseLost;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ParadiseLostRecipeTypes {

	public static final String TREE_TAP_RECIPE_ID = "tree_tap";
	public static RecipeSerializer<TreeTapRecipe> TREE_TAP_RECIPE_SERIALIZER;
	public static RecipeType<TreeTapRecipe> TREE_TAP_RECIPE_TYPE;

	static <S extends RecipeSerializer<T>, T extends Recipe<?>> S registerSerializer(String id, S serializer) {
		return Registry.register(Registries.RECIPE_SERIALIZER, ParadiseLost.locate(id), serializer);
	}

	static <T extends Recipe<?>> RecipeType<T> registerRecipeType(String id) {
		return Registry.register(Registries.RECIPE_TYPE, ParadiseLost.locate(id), new RecipeType<T>() {
			@Override
			public String toString() {
				return ParadiseLost.MOD_ID + ":" + id;
			}
		});
	}

	public static void init() {
		TREE_TAP_RECIPE_SERIALIZER = registerSerializer(TREE_TAP_RECIPE_ID, new TreeTapRecipe.Serializer());
		TREE_TAP_RECIPE_TYPE = registerRecipeType(TREE_TAP_RECIPE_ID);
	}

}
