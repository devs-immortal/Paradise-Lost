package net.id.paradiselost.items.armor;

import net.id.paradiselost.items.ParadiseLostItems;
import net.id.paradiselost.items.utils.IngredientUtil;
import net.id.paradiselost.mixin.item.ArmorMaterialsAccessor;
import net.id.paradiselost.util.ParadiseLostSoundEvents;
import net.minecraft.item.ArmorMaterial;

@SuppressWarnings("unused")
public class ParadiseLostArmorMaterials {

    public static final ArmorMaterial OLVITE = ArmorMaterialsAccessor.callInit("PARADISE_LOST_OLVITE", -1,
            "paradise_lost_olvite", 15, new int[]{2, 5, 6, 2}, 9, ParadiseLostSoundEvents.ITEM_ARMOR_EQUIP_OLVITE,
            0f, 0f, IngredientUtil.itemIngredient(ParadiseLostItems.OLVITE));

}
