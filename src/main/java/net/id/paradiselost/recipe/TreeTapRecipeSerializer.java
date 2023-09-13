package net.id.paradiselost.recipe;

import com.google.gson.JsonObject;
import net.id.incubus_core.recipe.RecipeParser;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;

public record TreeTapRecipeSerializer(TreeTapRecipeSerializer.RecipeFactory recipeFactory) implements RecipeSerializer<TreeTapRecipe> {

	public interface RecipeFactory {
		TreeTapRecipe create(Identifier id, String group, Ingredient ingredient, ItemStack output, Block tappedBlock);
	}

	@Override
	public TreeTapRecipe read(Identifier identifier, JsonObject jsonObject) {
		String group = JsonHelper.getString(jsonObject, "group", "");
		Ingredient ingredient = Ingredient.fromJson(JsonHelper.getObject(jsonObject, "ingredient"));
		Block tappedBlock = Registry.BLOCK.get(Identifier.tryParse(JsonHelper.getString(jsonObject, "tapped_block")));
		ItemStack output = RecipeParser.getItemStackWithNbtFromJson(JsonHelper.getObject(jsonObject, "result"));

		return this.recipeFactory.create(identifier, group, ingredient, output, tappedBlock);
	}

	@Override
	public void write(PacketByteBuf packetByteBuf, TreeTapRecipe recipe) {
		packetByteBuf.writeString(recipe.group);
		recipe.ingredient.write(packetByteBuf);
		packetByteBuf.writeIdentifier(Registry.BLOCK.getId(recipe.tappedBlock));
		packetByteBuf.writeItemStack(recipe.output);
	}

	@Override
	public TreeTapRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
		String group = packetByteBuf.readString();
		Ingredient ingredient = Ingredient.fromPacket(packetByteBuf);
		Block tappedBlock = Registry.BLOCK.get(packetByteBuf.readIdentifier());
		ItemStack output = packetByteBuf.readItemStack();

		return this.recipeFactory.create(identifier, group, ingredient, output, tappedBlock);
	}

}
