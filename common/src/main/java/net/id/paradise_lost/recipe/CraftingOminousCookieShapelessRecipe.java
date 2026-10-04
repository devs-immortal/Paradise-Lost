package net.id.paradise_lost.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;

public class CraftingOminousCookieShapelessRecipe extends ShapelessRecipe {
    public static final int OUTPUT_COUNT = 8;

    private final NonNullList<Ingredient> shapelessIngredients;

    public CraftingOminousCookieShapelessRecipe(
            String group,
            CraftingBookCategory category,
            ItemStack result,
            NonNullList<Ingredient> ingredients
    ) {
        super(group, category, result, ingredients);
        this.shapelessIngredients = ingredients;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        ItemStack result = super.assemble(input, registries);
        copyAmplifier(input, result);
        return result;
    }

    private static void copyAmplifier(CraftingInput input, ItemStack result) {
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.is(Items.OMINOUS_BOTTLE)) {
                Integer amplifier = stack.get(DataComponents.OMINOUS_BOTTLE_AMPLIFIER);
                if (amplifier != null) {
                    result.set(DataComponents.OMINOUS_BOTTLE_AMPLIFIER, amplifier);
                }
                break;
            }
        }
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ParadiseLostRecipeTypes.OMINOUS_COOKIE_SHAPELESS_RECIPE_SERIALIZER;
    }

    public static class Serializer implements RecipeSerializer<CraftingOminousCookieShapelessRecipe> {
        public static final MapCodec<CraftingOminousCookieShapelessRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.STRING.optionalFieldOf("group", "").forGetter(ShapelessRecipe::getGroup),
                CraftingBookCategory.CODEC.optionalFieldOf("category", CraftingBookCategory.MISC).forGetter(ShapelessRecipe::category),
                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(recipe -> recipe.getResultItem(RegistryAccess.EMPTY)),
                Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients").flatXmap(
                        ingredients -> {
                            Ingredient[] array = ingredients.toArray(Ingredient[]::new);
                            if (array.length == 0) {
                                return DataResult.error(() -> "No ingredients for shapeless recipe");
                            }
                            if (array.length > 9) {
                                return DataResult.error(() -> "Too many ingredients for shapeless recipe");
                            }
                            return DataResult.success(NonNullList.of(Ingredient.EMPTY, array));
                        },
                        DataResult::success
                ).forGetter(recipe -> recipe.shapelessIngredients)
        ).apply(instance, CraftingOminousCookieShapelessRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, CraftingOminousCookieShapelessRecipe> STREAM_CODEC = StreamCodec.of(
                Serializer::toNetwork,
                Serializer::fromNetwork
        );

        private static CraftingOminousCookieShapelessRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            String group = buffer.readUtf();
            CraftingBookCategory category = buffer.readEnum(CraftingBookCategory.class);
            int size = buffer.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(size, Ingredient.EMPTY);
            ingredients.replaceAll(ignored -> Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
            ItemStack result = ItemStack.STREAM_CODEC.decode(buffer);
            return new CraftingOminousCookieShapelessRecipe(group, category, result, ingredients);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, CraftingOminousCookieShapelessRecipe recipe) {
            buffer.writeUtf(recipe.getGroup());
            buffer.writeEnum(recipe.category());
            buffer.writeVarInt(recipe.shapelessIngredients.size());
            for (Ingredient ingredient : recipe.shapelessIngredients) {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
            }
            ItemStack.STREAM_CODEC.encode(buffer, recipe.getResultItem(RegistryAccess.EMPTY));
        }

        @Override
        public MapCodec<CraftingOminousCookieShapelessRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, CraftingOminousCookieShapelessRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
