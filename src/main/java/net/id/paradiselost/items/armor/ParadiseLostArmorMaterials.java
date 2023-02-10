package net.id.paradiselost.items.armor;

import net.id.paradiselost.items.ParadiseLostItems;
import net.id.paradiselost.items.utils.IngredientUtil;
import net.id.paradiselost.mixin.item.ArmorMaterialsAccessor;
import net.id.paradiselost.util.ParadiseLostSoundEvents;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.sound.SoundEvents;

@SuppressWarnings("unused")
public class ParadiseLostArmorMaterials {

    public static final ArmorMaterial OLVITE = ArmorMaterialsAccessor.callInit("PARADISE_LOST_OLVITE", -1,
            "paradise_lost_olvite", 15, new int[]{2, 5, 6, 2}, 9, ParadiseLostSoundEvents.ITEM_ARMOR_EQUIP_OLVITE,
            0f, 0f, IngredientUtil.itemIngredient(ParadiseLostItems.OLVITE));
    public static final ArmorMaterial GLAZED_GOLD = ArmorMaterialsAccessor.callInit("PARADISE_LOST_GLAZED_GOLD", -1,
            "paradise_lost_glazed_gold", 14, new int[]{1, 3, 5, 2}, 25, ParadiseLostSoundEvents.ITEM_ARMOR_EQUIP_GLAZED_GOLD,
            0f, 0f, IngredientUtil.itemIngredient(ParadiseLostItems.GOLDEN_AMBER));

}
