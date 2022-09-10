package net.id.paradiselost.items.tools;

import net.fabricmc.yarn.constants.MiningLevels;
import net.id.paradiselost.items.ParadiseLostItems;
import net.id.paradiselost.items.utils.IngredientUtil;
import net.id.paradiselost.mixin.item.ToolMaterialsAccessor;
import net.minecraft.item.ToolMaterial;

@SuppressWarnings("unused")
public class ParadiseLostToolMaterials {
    public static final ToolMaterial ZANITE = ToolMaterialsAccessor.callInit("PARADISE_LOST_ZANITE", -1,
            MiningLevels.IRON, 250, 4.5f, 2f, 14, IngredientUtil.itemIngredient(ParadiseLostItems.ZANITE_GEM));

    public static final ToolMaterial CANDY = ToolMaterialsAccessor.callInit("PARADISE_LOST_CANDY", -1,
            MiningLevels.IRON, 520, 7f, 2.5f, 12, IngredientUtil.itemIngredient(ParadiseLostItems.CANDY_CANE));

    public static final ToolMaterial GRAVITITE = ToolMaterialsAccessor.callInit("PARADISE_LOST_GRAVITITE", -1,
            MiningLevels.DIAMOND, 1561, 8f, 3f, 10, IngredientUtil.EMPTY);

    public static final ToolMaterial VALKYRIE = ToolMaterialsAccessor.callInit("PARADISE_LOST_VALKYRIE", -1,
            MiningLevels.NETHERITE, 2164, 10f, 4f, 8, IngredientUtil.EMPTY);

    public static final ToolMaterial LEGENDARY = ToolMaterialsAccessor.callInit("PARADISE_LOST_LEGENDARY", -1,
            MiningLevels.NETHERITE, 2164, 10f, 4f, 8, IngredientUtil.EMPTY);
}