package net.id.paradiselost.items.armor;

import net.id.paradiselost.ParadiseLost;
import net.id.paradiselost.tag.ParadiseLostItemTags;
import net.id.paradiselost.util.ParadiseLostSoundEvents;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.util.Util;

import java.util.EnumMap;

@SuppressWarnings("unused")
public class ParadiseLostArmorMaterials {

    public static final ArmorMaterial OLVITE = new ArmorMaterial(15, Util.make(new EnumMap<>(EquipmentType.class), (map) -> {
        map.put(EquipmentType.BOOTS, 2);
        map.put(EquipmentType.LEGGINGS, 4);
        map.put(EquipmentType.CHESTPLATE, 6);
        map.put(EquipmentType.HELMET, 2);
        map.put(EquipmentType.BODY, 4);
    }), 9, ParadiseLostSoundEvents.ITEM_ARMOR_EQUIP_OLVITE, 0.0F, 0.0F, ParadiseLostItemTags.REPAIRS_OLVITE_ARMOR, ParadiseLost.locate("olvite"));

    public static final ArmorMaterial GLAZED_GOLD = new ArmorMaterial(21, Util.make(new EnumMap<>(EquipmentType.class), (map) -> {
        map.put(EquipmentType.BOOTS, 1);
        map.put(EquipmentType.LEGGINGS, 3);
        map.put(EquipmentType.CHESTPLATE, 5);
        map.put(EquipmentType.HELMET, 2);
        map.put(EquipmentType.BODY, 3);
    }), 25, ParadiseLostSoundEvents.ITEM_ARMOR_EQUIP_GLAZED_GOLD, 0.0F, 0.0F, ParadiseLostItemTags.REPAIRS_GLAZED_GOLD_ARMOR, ParadiseLost.locate("glazed_gold"));

    public static final ArmorMaterial SURTRUM = new ArmorMaterial(27, Util.make(new EnumMap<>(EquipmentType.class), (map) -> {
        map.put(EquipmentType.BOOTS, 2);
        map.put(EquipmentType.LEGGINGS, 5);
        map.put(EquipmentType.CHESTPLATE, 6);
        map.put(EquipmentType.HELMET, 3);
        map.put(EquipmentType.BODY, 5);
    }), 15, ParadiseLostSoundEvents.ITEM_ARMOR_EQUIP_SURTRUM, 0.0F, 0.0F, ParadiseLostItemTags.REPAIRS_SURTRUM_ARMOR, ParadiseLost.locate("surtrum"));

    public static final ArmorMaterial RELIC = new ArmorMaterial(15, Util.make(new EnumMap<>(EquipmentType.class), (map) -> {
        map.put(EquipmentType.BOOTS, 1);
        map.put(EquipmentType.LEGGINGS, 4);
        map.put(EquipmentType.CHESTPLATE, 5);
        map.put(EquipmentType.HELMET, 2);
        map.put(EquipmentType.BODY, 4);
    }), 12, ParadiseLostSoundEvents.ITEM_ARMOR_EQUIP_RELIC, 0.5F, 0.0F, ParadiseLostItemTags.REPAIRS_RELIC_ARMOR, ParadiseLost.locate("relic"));

}
