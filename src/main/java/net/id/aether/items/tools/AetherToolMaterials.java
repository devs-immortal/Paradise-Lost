package net.id.aether.items.tools;

import net.fabricmc.yarn.constants.MiningLevels;
import net.id.aether.items.AetherItems;
import net.id.aether.items.utils.IngredientUtil;
import net.id.aether.mixin.item.ToolMaterialsAccessor;
import net.minecraft.item.ToolMaterial;

@SuppressWarnings("unused")
public class AetherToolMaterials {
    public static final ToolMaterial ZANITE = ToolMaterialsAccessor.callInit("AETHER_ZANITE", -1,
            MiningLevels.IRON, 250, 4.5f, 2f, 14, IngredientUtil.itemIngredient(AetherItems.ZANITE_GEM));

    public static final ToolMaterial CANDY = ToolMaterialsAccessor.callInit("AETHER_CANDY", -1,
            MiningLevels.IRON, 520, 7f, 2.5f, 12, IngredientUtil.itemIngredient(AetherItems.CANDY_CANE));

    public static final ToolMaterial GRAVITITE = ToolMaterialsAccessor.callInit("AETHER_GRAVITITE", -1,
            MiningLevels.DIAMOND, 1561, 8f, 3f, 10, IngredientUtil.EMPTY);

    public static final ToolMaterial VALKYRIE = ToolMaterialsAccessor.callInit("AETHER_VALKYRIE", -1,
            MiningLevels.NETHERITE, 2164, 10f, 4f, 8, IngredientUtil.EMPTY);

    public static final ToolMaterial LEGENDARY = ToolMaterialsAccessor.callInit("AETHER_LEGENDARY", -1,
            MiningLevels.NETHERITE, 2164, 10f, 4f, 8, IngredientUtil.EMPTY);
}