package net.id.paradiselost.items.tools;

import net.fabricmc.yarn.constants.MiningLevels;
import net.id.paradiselost.items.ParadiseLostItems;
import net.id.paradiselost.items.utils.IngredientUtil;
import net.id.paradiselost.mixin.item.ToolMaterialsAccessor;
import net.minecraft.item.ToolMaterial;

@SuppressWarnings("unused")
public class ParadiseLostToolMaterials {
    public static final ToolMaterial OLVITE = ToolMaterialsAccessor.callInit("PARADISE_LOST_OLVITE", -1,
            MiningLevels.IRON, 250, 4.5f, 2f, 14, IngredientUtil.itemIngredient(ParadiseLostItems.OLVITE));
    public static final ToolMaterial GLAZED_GOLD = ToolMaterialsAccessor.callInit("PARADISE_LOST_GLAZED_GOLD", -1,
            MiningLevels.IRON, 128, 12f, 2f, 22, IngredientUtil.itemIngredient(ParadiseLostItems.GOLDEN_AMBER));

}
