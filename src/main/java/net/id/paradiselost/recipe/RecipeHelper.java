package net.id.paradiselost.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.JsonHelper;

public class RecipeHelper {
    // extracted from Incubus Core
    public static ItemStack getItemStackWithNbtFromJson(JsonObject json) {
        Item item = ShapedRecipe.getItem(json);

        int count = JsonHelper.getInt(json, "count", 1);
        if (count < 1) {
            throw new JsonSyntaxException("Invalid output count: " + count);
        }

        ItemStack stack = new ItemStack(item, count);
        String nbt = JsonHelper.getString(json, "nbt", "");
        if(nbt.isEmpty()) {
            return stack;
        }

        try {
            NbtCompound compound = NbtHelper.fromNbtProviderString(nbt);
            compound.remove("palette");
            stack.setNbt(compound);
        } catch (CommandSyntaxException e) {
            throw new JsonSyntaxException("Invalid output nbt: " + nbt);
        }

        return stack;
    }
}
