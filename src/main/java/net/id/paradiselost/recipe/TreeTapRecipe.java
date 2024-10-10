package net.id.paradiselost.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.id.paradiselost.blocks.blockentity.TreeTapBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.World;

import java.util.Optional;

public class TreeTapRecipe implements Recipe<TreeTapBlockEntity> {

	protected final Ingredient ingredient;
    protected final ItemStack result;
    protected final Block tappedBlock;
    protected final Block resultBlock;
    protected final int chance;

	public TreeTapRecipe(Ingredient ingredient, Identifier tappedBlock, Identifier resultBlock, ItemStack result, Optional<PotionContentsComponent> contents, int chance) {
		this.ingredient = ingredient;
		this.result = result;
        contents.ifPresent(potionContentsComponent -> result.set(DataComponentTypes.POTION_CONTENTS, potionContentsComponent));
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
    public ItemStack craft(TreeTapBlockEntity inventory, RegistryWrapper.WrapperLookup lookup) {
        return result.copy();
    }

    @Override
	public boolean fits(int width, int height) {
		return true;
	}

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
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

        private static Optional<PotionContentsComponent> getOptionalPotionContentsComponent(TreeTapRecipe recipe) {
            return recipe.result.getComponents().contains(DataComponentTypes.POTION_CONTENTS) ? Optional.of(recipe.result.get(DataComponentTypes.POTION_CONTENTS)) : Optional.empty();
        }

        private static final MapCodec<TreeTapRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
                Ingredient.ALLOW_EMPTY_CODEC.fieldOf("ingredient").forGetter((recipe) -> recipe.ingredient),
                Identifier.CODEC.fieldOf("tapped_block").forGetter((recipe) -> Registries.BLOCK.getId(recipe.tappedBlock)),
                Identifier.CODEC.fieldOf("result_block").forGetter((recipe) -> Registries.BLOCK.getId(recipe.resultBlock)),
                ItemStack.CODEC.fieldOf("result").forGetter((recipe) -> recipe.result),
                PotionContentsComponent.CODEC.optionalFieldOf("potion_content").forGetter((recipe) -> getOptionalPotionContentsComponent(recipe)),
                Codecs.POSITIVE_INT.fieldOf("chance").forGetter((recipe) -> recipe.chance)
        ).apply(instance, TreeTapRecipe::new));
        public static final PacketCodec<RegistryByteBuf, TreeTapRecipe> PACKET_CODEC = PacketCodec.ofStatic(Serializer::write, Serializer::read);

        public Serializer() {
        }

        @Override
        public MapCodec<TreeTapRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, TreeTapRecipe> packetCodec() {
            return PACKET_CODEC;
        }

        private static TreeTapRecipe read(RegistryByteBuf buf) {
            Ingredient ingredient = Ingredient.PACKET_CODEC.decode(buf);
            Identifier tappedBlock = Identifier.PACKET_CODEC.decode(buf);
            Identifier resultBlock = Identifier.PACKET_CODEC.decode(buf);
            ItemStack result = ItemStack.PACKET_CODEC.decode(buf);
            Optional<PotionContentsComponent> contents = PacketCodecs.optional(PotionContentsComponent.PACKET_CODEC).decode(buf);
            int chance = buf.readInt();
            return new TreeTapRecipe(ingredient, tappedBlock, resultBlock, result, contents, chance);
        }

        private static void write(RegistryByteBuf buf, TreeTapRecipe recipe) {
            Ingredient.PACKET_CODEC.encode(buf, recipe.ingredient);
            Identifier.PACKET_CODEC.encode(buf, Registries.BLOCK.getId(recipe.tappedBlock));
            Identifier.PACKET_CODEC.encode(buf, Registries.BLOCK.getId(recipe.resultBlock));
            ItemStack.PACKET_CODEC.encode(buf, recipe.result);
            PacketCodecs.optional(PotionContentsComponent.PACKET_CODEC).encode(buf, getOptionalPotionContentsComponent(recipe));
            buf.writeInt(recipe.chance);
        }
    }

}
