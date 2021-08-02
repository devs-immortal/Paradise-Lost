package com.aether.items.utils;

import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;

import java.util.function.Supplier;

/**
 * Because passing lambdas to EnumExtender requires a lengthy cast
 */
public class IngredientUtil {
    public static final Supplier<Ingredient> EMPTY = Ingredient::empty;

    public static Supplier<Ingredient> itemIngredient(ItemConvertible item) {
        return () -> Ingredient.ofItems(item);
    }
}
