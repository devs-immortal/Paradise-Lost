package net.id.paradise_lost.item.armor;

import net.id.paradise_lost.tag.ParadiseLostItemTags;
import net.id.paradise_lost.util.ParadiseLostSoundEvents;
import net.minecraft.Util;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;

import java.util.EnumMap;

import static net.id.paradise_lost.ModConstants.id;

@SuppressWarnings("unused")
public class ParadiseLostArmorMaterials {
    public static final ArmorMaterial OLVITE = new ArmorMaterial(15, Util.make(new EnumMap<>(ArmorType.class), (map) -> {
        map.put(ArmorType.BOOTS, 2);
        map.put(ArmorType.LEGGINGS, 4);
        map.put(ArmorType.CHESTPLATE, 6);
        map.put(ArmorType.HELMET, 2);
        map.put(ArmorType.BODY, 4);
    }), 9, ParadiseLostSoundEvents.ITEM_ARMOR_EQUIP_OLVITE, 0.0F, 0.0F, ParadiseLostItemTags.REPAIRS_OLVITE_ARMOR, id("olvite"));

    public static final ArmorMaterial ORNATE_OLVITE = new ArmorMaterial(15, Util.make(new EnumMap<>(ArmorType.class), (map) -> {
        map.put(ArmorType.BOOTS, 2);
        map.put(ArmorType.LEGGINGS, 4);
        map.put(ArmorType.CHESTPLATE, 6);
        map.put(ArmorType.HELMET, 2);
        map.put(ArmorType.BODY, 4);
    }), 9, ParadiseLostSoundEvents.ITEM_ARMOR_EQUIP_OLVITE, 0.0F, 0.0F, ParadiseLostItemTags.REPAIRS_OLVITE_ARMOR, id("ornate_olvite"));

    public static final ArmorMaterial GLAZED_GOLD = new ArmorMaterial(21, Util.make(new EnumMap<>(ArmorType.class), (map) -> {
        map.put(ArmorType.BOOTS, 1);
        map.put(ArmorType.LEGGINGS, 3);
        map.put(ArmorType.CHESTPLATE, 5);
        map.put(ArmorType.HELMET, 2);
        map.put(ArmorType.BODY, 3);
    }), 25, ParadiseLostSoundEvents.ITEM_ARMOR_EQUIP_GLAZED_GOLD, 0.0F, 0.0F, ParadiseLostItemTags.REPAIRS_GLAZED_GOLD_ARMOR, id("glazed_gold"));

    public static final ArmorMaterial SURTRUM = new ArmorMaterial(27, Util.make(new EnumMap<>(ArmorType.class), (map) -> {
        map.put(ArmorType.BOOTS, 2);
        map.put(ArmorType.LEGGINGS, 5);
        map.put(ArmorType.CHESTPLATE, 6);
        map.put(ArmorType.HELMET, 3);
        map.put(ArmorType.BODY, 5);
    }), 15, ParadiseLostSoundEvents.ITEM_ARMOR_EQUIP_SURTRUM, 0.0F, 0.0F, ParadiseLostItemTags.REPAIRS_SURTRUM_ARMOR, id("surtrum"));

    public static final ArmorMaterial RELIC = new ArmorMaterial(15, Util.make(new EnumMap<>(ArmorType.class), (map) -> {
        map.put(ArmorType.BOOTS, 1);
        map.put(ArmorType.LEGGINGS, 4);
        map.put(ArmorType.CHESTPLATE, 5);
        map.put(ArmorType.HELMET, 2);
        map.put(ArmorType.BODY, 4);
    }), 12, ParadiseLostSoundEvents.ITEM_ARMOR_EQUIP_RELIC, 0.5F, 0.0F, ParadiseLostItemTags.REPAIRS_RELIC_ARMOR, id("relic"));

    public static final ArmorMaterial FLOATY = new ArmorMaterial(66, Util.make(new EnumMap<>(ArmorType.class), (map) -> {
        map.put(ArmorType.BOOTS, 1);
        map.put(ArmorType.LEGGINGS, 2);
        map.put(ArmorType.CHESTPLATE, 3);
        map.put(ArmorType.HELMET, 1);
        map.put(ArmorType.BODY, 2);
    }), 10, ParadiseLostSoundEvents.ITEM_ARMOR_EQUIP_FLAXWEAVE, 0.0F, 0.0F, ParadiseLostItemTags.REPAIRS_FLOATY_ARMOR, id("floaty"));
}
