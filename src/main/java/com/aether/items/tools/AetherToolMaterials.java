package com.aether.items.tools;

import com.aether.items.AetherItems;
import com.aether.mixin.item.ToolMaterialsAccessor;
import net.minecraft.item.ToolMaterial;

import static com.aether.items.utils.IngredientUtil.EMPTY;
import static com.aether.items.utils.IngredientUtil.itemIngredient;

@SuppressWarnings("unused")
public class AetherToolMaterials {
    public static final ToolMaterial ZANITE = ToolMaterialsAccessor.callInit("AETHER_ZANITE", -1,
            2, 250, 4.5f, 2f, 14, itemIngredient(AetherItems.ZANITE_GEM));

    public static final ToolMaterial CANDY = ToolMaterialsAccessor.callInit("AETHER_CANDY", -1,
            2, 520, 7f, 2.5f, 12, itemIngredient(AetherItems.CANDY_CANE));

    public static final ToolMaterial GRAVITITE = ToolMaterialsAccessor.callInit("AETHER_GRAVITITE", -1,
            3, 1561, 8f, 3f, 10, EMPTY);

    public static final ToolMaterial VALKYRIE = ToolMaterialsAccessor.callInit("AETHER_VALKYRIE", -1,
            4, 2164, 10f, 4f, 8, EMPTY);

    public static final ToolMaterial LEGENDARY = ToolMaterialsAccessor.callInit("AETHER_LEGENDARY", -1,
            4, 2164, 10f, 4f, 8, EMPTY);
}