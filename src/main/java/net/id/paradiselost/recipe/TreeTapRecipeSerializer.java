package net.id.paradiselost.recipe;

import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public record TreeTapRecipeSerializer(TreeTapRecipeSerializer.RecipeFactory recipeFactory) implements RecipeSerializer<TreeTapRecipe> {

	public interface RecipeFactory {
		TreeTapRecipe create(Identifier id, String group, Ingredient ingredient, ItemStack output, Block tappedBlock, Block resultBlock, int chance);
	}

	@Override
	public TreeTapRecipe read(Identifier identifier, JsonObject jsonObject) {
		String group = JsonHelper.getString(jsonObject, "group", "");
		Ingredient ingredient = Ingredient.fromJson(JsonHelper.getObject(jsonObject, "ingredient"));
        Block tappedBlock = Registries.BLOCK.get(Identifier.tryParse(JsonHelper.getString(jsonObject, "tapped_block")));
        Block resultBlock = Registries.BLOCK.get(Identifier.tryParse(JsonHelper.getString(jsonObject, "result_block")));
        ItemStack output = RecipeHelper.getItemStackWithNbtFromJson(JsonHelper.getObject(jsonObject, "result"));
        int chance = JsonHelper.getInt(jsonObject, "chance");

		return this.recipeFactory.create(identifier, group, ingredient, output, tappedBlock, resultBlock, chance);
	}

	@Override
	public void write(PacketByteBuf packetByteBuf, TreeTapRecipe recipe) {
		packetByteBuf.writeString(recipe.group);
		recipe.ingredient.write(packetByteBuf);
        packetByteBuf.writeIdentifier(Registries.BLOCK.getId(recipe.tappedBlock));
        packetByteBuf.writeIdentifier(Registries.BLOCK.getId(recipe.resultBlock));
		packetByteBuf.writeItemStack(recipe.output);
        packetByteBuf.writeInt(recipe.chance);
	}

	@Override
	public TreeTapRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
		String group = packetByteBuf.readString();
		Ingredient ingredient = Ingredient.fromPacket(packetByteBuf);
        Block tappedBlock = Registries.BLOCK.get(packetByteBuf.readIdentifier());
        Block resultBlock = Registries.BLOCK.get(packetByteBuf.readIdentifier());
		ItemStack output = packetByteBuf.readItemStack();
        int chance = packetByteBuf.readInt();

		return this.recipeFactory.create(identifier, group, ingredient, output, tappedBlock, resultBlock, chance);
	}

}
