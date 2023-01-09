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

    public static final ToolMaterial GRAVITITE = ToolMaterialsAccessor.callInit("PARADISE_LOST_GRAVITITE", -1,
            MiningLevels.DIAMOND, 1561, 8f, 3f, 10, IngredientUtil.EMPTY);

}
