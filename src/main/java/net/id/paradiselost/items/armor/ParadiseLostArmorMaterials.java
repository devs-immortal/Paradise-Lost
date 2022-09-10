package net.id.paradiselost.items.armor;

import net.id.paradiselost.items.ParadiseLostItems;
import net.id.paradiselost.items.utils.IngredientUtil;
import net.id.paradiselost.mixin.item.ArmorMaterialsAccessor;
import net.id.paradiselost.util.ParadiseLostSoundEvents;
import net.minecraft.item.ArmorMaterial;

@SuppressWarnings("unused")
public class ParadiseLostArmorMaterials {
    public static final ArmorMaterial ZANITE = ArmorMaterialsAccessor.callInit("PARADISE_LOST_ZANITE", -1,
            "paradise_lost_zanite", 15, new int[]{2, 5, 6, 2}, 9, ParadiseLostSoundEvents.ITEM_ARMOR_EQUIP_ZANITE,
            0f, 0f, IngredientUtil.itemIngredient(ParadiseLostItems.ZANITE_GEM));

    public static final ArmorMaterial GRAVITITE = ArmorMaterialsAccessor.callInit("PARADISE_LOST_GRAVITITE", -1,
            "paradise_lost_gravitite", 33, new int[]{3, 6, 8, 3}, 10, ParadiseLostSoundEvents.ITEM_ARMOR_EQUIP_GRAVITITE,
            2f, 0f, IngredientUtil.itemIngredient(ParadiseLostItems.GRAVITITE_GEM));

    public static final ArmorMaterial NEPTUNE = ArmorMaterialsAccessor.callInit("PARADISE_LOST_NEPTUNE", -1,
            "paradise_lost_neptune", 35, new int[]{4, 6, 8, 4}, 8, ParadiseLostSoundEvents.ITEM_ARMOR_EQUIP_NEPTUNE,
            1.5f, 0f, IngredientUtil.EMPTY);

    public static final ArmorMaterial PHOENIX = ArmorMaterialsAccessor.callInit("PARADISE_LOST_GRAVITITE", -1,
            "paradise_lost_phoenix", 35, new int[]{4, 6, 8, 4}, 10, ParadiseLostSoundEvents.ITEM_ARMOR_EQUIP_PHOENIX,
            1.5F, 0f, IngredientUtil.EMPTY);

    public static final ArmorMaterial OBSIDIAN = ArmorMaterialsAccessor.callInit("PARADISE_LOST_GRAVITITE", -1,
            "paradise_lost_obsidian", 38, new int[]{4, 8, 10, 4}, 6, ParadiseLostSoundEvents.ITEM_ARMOR_EQUIP_OBSIDIAN,
            2f, 0.1f, IngredientUtil.EMPTY);

    public static final ArmorMaterial VALKYRIE = ArmorMaterialsAccessor.callInit("PARADISE_LOST_GRAVITITE", -1,
            "paradise_lost_valkyrie", 35, new int[]{4, 8, 10, 4}, 10, ParadiseLostSoundEvents.ITEM_ARMOR_EQUIP_VALKYRIE,
            1.5f, 0.1f, IngredientUtil.EMPTY);

    public static final ArmorMaterial SENTRY = ArmorMaterialsAccessor.callInit("PARADISE_LOST_GRAVITITE", -1,
            "paradise_lost_sentry", 35, new int[]{4, 8, 10, 4}, 10, ParadiseLostSoundEvents.ITEM_ARMOR_EQUIP_SENTRY,
            1.5f, 0f, IngredientUtil.EMPTY);
}