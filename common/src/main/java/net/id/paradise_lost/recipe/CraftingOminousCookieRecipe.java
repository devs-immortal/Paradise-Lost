package net.id.paradise_lost.recipe;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;

public class CraftingOminousCookieRecipe extends ShapedRecipe {
    public static final int OUTPUT_COUNT = OminousCookieCrafting.OUTPUT_COUNT;

    public CraftingOminousCookieRecipe(
            String group,
            CraftingBookCategory category,
            ShapedRecipePattern pattern,
            ItemStack result,
            boolean showNotification
    ) {
        super(group, category, pattern, result, showNotification);
    }

    public CraftingOminousCookieRecipe(ShapedRecipe recipe) {
        this(
                recipe.getGroup(),
                recipe.category(),
                recipe.pattern,
                recipe.getResultItem(RegistryAccess.EMPTY),
                recipe.showNotification()
        );
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        ItemStack result = super.assemble(input, registries);
        OminousCookieCrafting.copyAmplifier(input, result);
        return result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ParadiseLostRecipeTypes.OMINOUS_COOKIE_RECIPE_SERIALIZER;
    }

    public static class Serializer implements RecipeSerializer<CraftingOminousCookieRecipe> {
        private static final MapCodec<CraftingOminousCookieRecipe> MAP_CODEC =
                ShapedRecipe.Serializer.CODEC.xmap(CraftingOminousCookieRecipe::new, recipe -> recipe);

        private static final StreamCodec<RegistryFriendlyByteBuf, CraftingOminousCookieRecipe> STREAM_CODEC = StreamCodec.of(
                (buffer, recipe) -> ShapedRecipe.Serializer.STREAM_CODEC.encode(buffer, recipe),
                buffer -> new CraftingOminousCookieRecipe(ShapedRecipe.Serializer.STREAM_CODEC.decode(buffer))
        );

        @Override
        public MapCodec<CraftingOminousCookieRecipe> codec() {
            return MAP_CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, CraftingOminousCookieRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
