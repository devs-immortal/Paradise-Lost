package net.id.paradise_lost.recipe;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;

import java.util.List;

public class CraftingOminousCookieShapelessRecipe extends ShapelessRecipe {
    public static final int OUTPUT_COUNT = OminousCookieCrafting.OUTPUT_COUNT;

    public CraftingOminousCookieShapelessRecipe(
            String group,
            CraftingBookCategory category,
            ItemStack result,
            List<Ingredient> ingredients
    ) {
        super(group, category, result, ingredients);
    }

    public CraftingOminousCookieShapelessRecipe(ShapelessRecipe recipe) {
        this(
                recipe.group(),
                recipe.category(),
                recipe.result,
                recipe.ingredients
        );
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        ItemStack result = super.assemble(input, registries);
        OminousCookieCrafting.copyAmplifier(input, result);
        return result;
    }

    // ShapelessRecipe#getSerializer is typed RecipeSerializer<ShapelessRecipe>, which our serializer can't satisfy without a cast
    @Override
    @SuppressWarnings("unchecked")
    public RecipeSerializer<ShapelessRecipe> getSerializer() {
        return (RecipeSerializer<ShapelessRecipe>) (RecipeSerializer<?>) ParadiseLostRecipeTypes.OMINOUS_COOKIE_SHAPELESS_RECIPE_SERIALIZER;
    }

    public static class Serializer implements RecipeSerializer<CraftingOminousCookieShapelessRecipe> {
        private static final MapCodec<CraftingOminousCookieShapelessRecipe> MAP_CODEC =
                new ShapelessRecipe.Serializer().codec().xmap(CraftingOminousCookieShapelessRecipe::new, recipe -> recipe);

        private static final StreamCodec<RegistryFriendlyByteBuf, CraftingOminousCookieShapelessRecipe> STREAM_CODEC = StreamCodec.of(
                (buffer, recipe) -> ShapelessRecipe.Serializer.STREAM_CODEC.encode(buffer, recipe),
                buffer -> new CraftingOminousCookieShapelessRecipe(ShapelessRecipe.Serializer.STREAM_CODEC.decode(buffer))
        );

        @Override
        public MapCodec<CraftingOminousCookieShapelessRecipe> codec() {
            return MAP_CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, CraftingOminousCookieShapelessRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
