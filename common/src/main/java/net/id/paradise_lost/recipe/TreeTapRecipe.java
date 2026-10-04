package net.id.paradise_lost.recipe;

import org.jetbrains.annotations.Nullable;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.PlacementInfo;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

public class TreeTapRecipe implements Recipe<TreeTapRecipeInput> {
    protected final Ingredient ingredient;
    protected final ItemStack result;
    protected final Block tappedBlock;
    protected final Block resultBlock;
    protected final int chance;
    @Nullable
    private PlacementInfo placementInfo;

    public TreeTapRecipe(Ingredient ingredient, Block tappedBlock, Block resultBlock, ItemStack result, Optional<PotionContents> contents, int chance) {
        this.ingredient = ingredient;
        this.result = result;
        contents.ifPresent(potion -> result.set(DataComponents.POTION_CONTENTS, potion));
        this.tappedBlock = tappedBlock;
        this.resultBlock = resultBlock;
        this.chance = chance;
    }

    @Override
    public boolean matches(TreeTapRecipeInput input, Level world) {
        return ingredient.test(input.stack()) && input.tappedState().is(this.tappedBlock);
    }

    @Override
    public ItemStack assemble(TreeTapRecipeInput input, HolderLookup.Provider lookup) {
        return result.copy();
    }

    public Block getOutputBlock() {
        return resultBlock;
    }

    public int getChance() {
        return chance;
    }

    @Override
    public RecipeSerializer<TreeTapRecipe> getSerializer() {
        return ParadiseLostRecipeTypes.TREE_TAP_RECIPE_SERIALIZER;
    }

    @Override
    public RecipeType<TreeTapRecipe> getType() {
        return ParadiseLostRecipeTypes.TREE_TAP_RECIPE_TYPE;
    }

    @Override
    public PlacementInfo placementInfo() {
        if (this.placementInfo == null) {
            this.placementInfo = PlacementInfo.create(this.ingredient);
        }
        return this.placementInfo;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return ParadiseLostRecipeTypes.TREE_TAP_RECIPE_BOOK_CATEGORY;
    }

    public static class Serializer implements RecipeSerializer<TreeTapRecipe> {
        private static final MapCodec<TreeTapRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.CODEC.fieldOf("ingredient").forGetter(recipe -> recipe.ingredient),
                BuiltInRegistries.BLOCK.byNameCodec().fieldOf("tapped_block").forGetter(recipe -> recipe.tappedBlock),
                BuiltInRegistries.BLOCK.byNameCodec().fieldOf("result_block").forGetter(recipe -> recipe.resultBlock),
                ItemStack.CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
                PotionContents.CODEC.optionalFieldOf("potion_content").forGetter(Serializer::potionContents),
                ExtraCodecs.POSITIVE_INT.fieldOf("chance").forGetter(recipe -> recipe.chance)
        ).apply(instance, TreeTapRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, TreeTapRecipe> PACKET_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC, recipe -> recipe.ingredient,
                ByteBufCodecs.registry(Registries.BLOCK), recipe -> recipe.tappedBlock,
                ByteBufCodecs.registry(Registries.BLOCK), recipe -> recipe.resultBlock,
                ItemStack.STREAM_CODEC, recipe -> recipe.result,
                ByteBufCodecs.optional(PotionContents.STREAM_CODEC), Serializer::potionContents,
                ByteBufCodecs.VAR_INT, recipe -> recipe.chance,
                TreeTapRecipe::new
        );

        private static Optional<PotionContents> potionContents(TreeTapRecipe recipe) {
            return Optional.ofNullable(recipe.result.get(DataComponents.POTION_CONTENTS));
        }

        @Override
        public MapCodec<TreeTapRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, TreeTapRecipe> streamCodec() {
            return PACKET_CODEC;
        }
    }
}
