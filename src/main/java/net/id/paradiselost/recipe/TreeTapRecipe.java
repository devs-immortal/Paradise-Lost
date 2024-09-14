package net.id.paradiselost.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.id.paradiselost.blocks.blockentity.TreeTapBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeCodecs;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmithingRecipe;
import net.minecraft.recipe.SmithingTransformRecipe;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.World;

public class TreeTapRecipe implements Recipe<TreeTapBlockEntity> {

	protected final Ingredient ingredient;
	protected final ItemStack result;
    protected final Block tappedBlock;
    protected final Block resultBlock;
    protected final int chance;

	public TreeTapRecipe(Ingredient ingredient, Identifier tappedBlock, Identifier resultBlock, ItemStack result, int chance) {
		this.ingredient = ingredient;
		this.result = result;
        this.tappedBlock = Registries.BLOCK.get(tappedBlock);
        this.resultBlock = Registries.BLOCK.get(resultBlock);
        this.chance = chance;
	}

	@Override
	public boolean matches(TreeTapBlockEntity inventory, World world) {
		if (!ingredient.test(inventory.getStack(0))) {
			return false;
		}

		return inventory.getTappedState().isOf(this.tappedBlock);
	}

    @Override
    public ItemStack craft(TreeTapBlockEntity inventory, DynamicRegistryManager registryManager) {
        return result.copy();
    }

	@Override
	public boolean fits(int width, int height) {
		return true;
	}

    @Override
    public ItemStack getResult(DynamicRegistryManager registryManager) {
        return result;
    }

    public Block getOutputBlock() {
        return resultBlock;
    }

    public int getChance() {
        return chance;
    }

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ParadiseLostRecipeTypes.TREE_TAP_RECIPE_SERIALIZER;
	}

	@Override
	public RecipeType<?> getType() {
		return ParadiseLostRecipeTypes.TREE_TAP_RECIPE_TYPE;
	}

    public static class Serializer implements RecipeSerializer<TreeTapRecipe> {

        public Serializer() {
        }

        private static final Codec<TreeTapRecipe> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                Ingredient.ALLOW_EMPTY_CODEC.fieldOf("ingredient").forGetter((recipe) -> recipe.ingredient),
                Identifier.CODEC.fieldOf("tapped_block").forGetter((recipe) -> Registries.BLOCK.getId(recipe.tappedBlock)),
                Identifier.CODEC.fieldOf("result_block").forGetter((recipe) -> Registries.BLOCK.getId(recipe.resultBlock)),
                RecipeCodecs.CRAFTING_RESULT.fieldOf("result").forGetter((recipe) -> recipe.result),
                Codecs.POSITIVE_INT.fieldOf("chance").forGetter((recipe) -> recipe.chance)
        ).apply(instance, TreeTapRecipe::new));

        @Override
        public Codec<TreeTapRecipe> codec() {
            return CODEC;
        }

        @Override
        public TreeTapRecipe read(PacketByteBuf buf) {
            Ingredient ingredient = Ingredient.fromPacket(buf);
            Identifier tappedBlock = buf.readIdentifier();
            Identifier resultBlock = buf.readIdentifier();
            ItemStack result = buf.readItemStack();
            int chance = buf.readInt();
            return new TreeTapRecipe(ingredient, tappedBlock, resultBlock, result, chance);
        }

        @Override
        public void write(PacketByteBuf buf, TreeTapRecipe recipe) {
            recipe.ingredient.write(buf);
            buf.writeIdentifier(Registries.BLOCK.getId(recipe.tappedBlock));
            buf.writeIdentifier(Registries.BLOCK.getId(recipe.resultBlock));
            buf.writeItemStack(recipe.result);
            buf.writeInt(recipe.chance);
        }
    }

}
