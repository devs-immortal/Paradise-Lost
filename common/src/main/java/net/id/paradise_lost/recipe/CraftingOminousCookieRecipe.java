package net.id.paradise_lost.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.OminousBottleAmplifier;
import net.id.paradise_lost.item.ParadiseLostDataComponentTypes;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;

public class CraftingOminousCookieRecipe extends ShapedRecipe {
    public static final int OUTPUT_COUNT = 8;

    private final ShapedRecipePattern shapedPattern;
    private final ItemStack result;

    public CraftingOminousCookieRecipe(
            String group,
            CraftingBookCategory category,
            ShapedRecipePattern pattern,
            ItemStack result,
            boolean showNotification
    ) {
        super(group, category, pattern, result, showNotification);
        this.shapedPattern = pattern;
        this.result = result;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        ItemStack result = super.assemble(input, registries);
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.is(Items.OMINOUS_BOTTLE)) {
                OminousBottleAmplifier amplifier = stack.get(DataComponents.OMINOUS_BOTTLE_AMPLIFIER);
                if (amplifier != null) {
                    result.set(ParadiseLostDataComponentTypes.OMINOUS_COOKIE_AMPLIFIER, amplifier.value());
                }
                break;
            }
        }
        return result;
    }

    @Override
    public RecipeSerializer<CraftingOminousCookieRecipe> getSerializer() {
        return ParadiseLostRecipeTypes.OMINOUS_COOKIE_RECIPE_SERIALIZER;
    }

    public static class Serializer implements RecipeSerializer<CraftingOminousCookieRecipe> {
        public static final MapCodec<CraftingOminousCookieRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.STRING.optionalFieldOf("group", "").forGetter(ShapedRecipe::group),
                CraftingBookCategory.CODEC.optionalFieldOf("category", CraftingBookCategory.MISC).forGetter(ShapedRecipe::category),
                ShapedRecipePattern.MAP_CODEC.forGetter(recipe -> recipe.shapedPattern),
                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
                Codec.BOOL.optionalFieldOf("show_notification", true).forGetter(ShapedRecipe::showNotification)
        ).apply(instance, CraftingOminousCookieRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, CraftingOminousCookieRecipe> STREAM_CODEC = StreamCodec.of(
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
            buffer.writeUtf(recipe.group());
            buffer.writeEnum(recipe.category());
            ShapedRecipePattern.STREAM_CODEC.encode(buffer, recipe.shapedPattern);
            ItemStack.STREAM_CODEC.encode(buffer, recipe.result);
            buffer.writeBoolean(recipe.showNotification());
        }

        @Override
        public MapCodec<CraftingOminousCookieRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, CraftingOminousCookieRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
