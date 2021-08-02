package com.aether.items.tools;

import com.aether.items.AetherItems;
import com.aether.util.EnumExtender;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Lazy;

import static com.aether.items.utils.IngredientUtil.EMPTY;
import static com.aether.items.utils.IngredientUtil.itemIngredient;

@SuppressWarnings("unused")
public class AetherToolMaterials {
    /*public static final ToolMaterial SKYROOT = EnumExtender.add(ToolMaterials.class, "AETHER_SKYROOT",
            0, 59, 2f, 0f, 15, itemIngredient(AetherItems.SKYROOT_PLANKS));

    public static final ToolMaterial HOLYSTONE = EnumExtender.add(ToolMaterials.class, "AETHER_HOLYSTONE",
            1, 131, 4f, 1f, 5, itemIngredient(AetherItems.HOLYSTONE));*/

    public static final ToolMaterial ZANITE = EnumExtender.add(ToolMaterials.class, "AETHER_ZANITE",
            2, 250, 4.5f, 2f, 14, itemIngredient(AetherItems.ZANITE_GEM));

    public static final ToolMaterial CANDY = EnumExtender.add(ToolMaterials.class, "AETHER_CANDY",
            2, 520, 7f, 2.5f, 12, itemIngredient(AetherItems.CANDY_CANE));

    public static final ToolMaterial GRAVITITE = EnumExtender.add(ToolMaterials.class, "AETHER_GRAVITITE",
            3, 1561, 8f, 3f, 10, EMPTY);

    public static final ToolMaterial VALKYRIE = EnumExtender.add(ToolMaterials.class, "AETHER_VALKYRIE",
            4, 2164, 10f, 4f, 8, EMPTY);

    public static final ToolMaterial LEGENDARY = EnumExtender.add(ToolMaterials.class, "AETHER_LEGENDARY",
            4, 2164, 10f, 4f, 8, EMPTY);
}