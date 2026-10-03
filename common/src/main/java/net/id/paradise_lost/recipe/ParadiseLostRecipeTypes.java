package net.id.paradise_lost.recipe;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.ParadiseLost;
import net.id.paradise_lost.registration.RegistrationProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

public class ParadiseLostRecipeTypes {
    private static final RegistrationProvider<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            RegistrationProvider.get(Registries.RECIPE_SERIALIZER, ModConstants.MODID);
    private static final RegistrationProvider<RecipeType<?>> RECIPE_TYPES =
            RegistrationProvider.get(Registries.RECIPE_TYPE, ModConstants.MODID);

    public static final String TREE_TAP_RECIPE_ID = "tree_tap";
    public static RecipeSerializer<TreeTapRecipe> TREE_TAP_RECIPE_SERIALIZER;
    public static RecipeType<TreeTapRecipe> TREE_TAP_RECIPE_TYPE;

    public static final String CALCITE_DECORATED_POT_RECIPE_ID = "crafting_calcite_decorated_pot";
    public static RecipeSerializer<CraftingCalciteDecoratedPotRecipe> CALCITE_DECORATED_POT_RECIPE_SERIALIZER;
    public static RecipeType<CraftingCalciteDecoratedPotRecipe> CALCITE_DECORATED_POT_RECIPE_TYPE;

    public static final String OMINOUS_COOKIE_RECIPE_ID = "crafting_ominous_cookie";
    public static RecipeSerializer<CraftingOminousCookieRecipe> OMINOUS_COOKIE_RECIPE_SERIALIZER;

    @SuppressWarnings("unchecked")
    static <S extends RecipeSerializer<T>, T extends Recipe<?>> S registerSerializer(String id, S serializer) {
        RECIPE_SERIALIZERS.register(id, () -> serializer);
        return serializer;
    }

    @SuppressWarnings("unchecked")
    static <T extends Recipe<?>> RecipeType<T> registerRecipeType(String id) {
        RecipeType<T> type = new RecipeType<>() {
            @Override
            public String toString() {
                return ParadiseLost.MOD_ID + ":" + id;
            }
        };
        RECIPE_TYPES.register(id, () -> type);
        return type;
    }

    public static void init() {
        TREE_TAP_RECIPE_SERIALIZER = registerSerializer(TREE_TAP_RECIPE_ID, new TreeTapRecipe.Serializer());
        TREE_TAP_RECIPE_TYPE = registerRecipeType(TREE_TAP_RECIPE_ID);
        CALCITE_DECORATED_POT_RECIPE_SERIALIZER = registerSerializer(CALCITE_DECORATED_POT_RECIPE_ID, new SimpleCraftingRecipeSerializer<>(CraftingCalciteDecoratedPotRecipe::new));
        CALCITE_DECORATED_POT_RECIPE_TYPE = registerRecipeType(CALCITE_DECORATED_POT_RECIPE_ID);
        OMINOUS_COOKIE_RECIPE_SERIALIZER = registerSerializer(OMINOUS_COOKIE_RECIPE_ID, new CraftingOminousCookieRecipe.Serializer());
    }
}
