package net.id.paradise_lost.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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

    private final ShapedRecipePattern shapedPattern;

    public CraftingOminousCookieRecipe(
            String group,
            CraftingBookCategory category,
            ShapedRecipePattern pattern,
            ItemStack result,
            boolean showNotification
    ) {
        super(group, category, pattern, result, showNotification);
        this.shapedPattern = pattern;
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
        private static final MapCodec<CraftingOminousCookieRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.STRING.optionalFieldOf("group", "").forGetter(ShapedRecipe::getGroup),
                CraftingBookCategory.CODEC.optionalFieldOf("category", CraftingBookCategory.MISC).forGetter(ShapedRecipe::category),
                ShapedRecipePattern.MAP_CODEC.forGetter(recipe -> recipe.shapedPattern),
                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(recipe -> recipe.getResultItem(RegistryAccess.EMPTY)),
                Codec.BOOL.optionalFieldOf("show_notification", true).forGetter(ShapedRecipe::showNotification)
        ).apply(instance, CraftingOminousCookieRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, CraftingOminousCookieRecipe> STREAM_CODEC = StreamCodec.of(
                Serializer::toNetwork,
                Serializer::fromNetwork
        );

        private static CraftingOminousCookieRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            String group = buffer.readUtf();
            CraftingBookCategory category = buffer.readEnum(CraftingBookCategory.class);
            ShapedRecipePattern pattern = ShapedRecipePattern.STREAM_CODEC.decode(buffer);
            ItemStack result = ItemStack.STREAM_CODEC.decode(buffer);
            boolean showNotification = buffer.readBoolean();
            return new CraftingOminousCookieRecipe(group, category, pattern, result, showNotification);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, CraftingOminousCookieRecipe recipe) {
            buffer.writeUtf(recipe.getGroup());
            buffer.writeEnum(recipe.category());
            ShapedRecipePattern.STREAM_CODEC.encode(buffer, recipe.shapedPattern);
            ItemStack.STREAM_CODEC.encode(buffer, recipe.getResultItem(RegistryAccess.EMPTY));
            buffer.writeBoolean(recipe.showNotification());
        }

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
