package com.aether.items.armor;

import com.aether.items.AetherItems;
import com.aether.util.EnumExtender;
import net.minecraft.item.*;
import net.minecraft.sound.SoundEvents;

import static com.aether.items.utils.IngredientUtil.EMPTY;
import static com.aether.items.utils.IngredientUtil.itemIngredient;

@SuppressWarnings("unused")
public class AetherArmorMaterials {
    public static final ArmorMaterial ZANITE = EnumExtender.add(ArmorMaterials.class, "AETHER_ZANITE",
            "aether_zanite", 15, new int[]{2, 5, 6, 2}, 9, SoundEvents.ITEM_ARMOR_EQUIP_IRON,
            0f, 0f, itemIngredient(AetherItems.ZANITE_GEM));

    public static final ArmorMaterial GRAVITITE = EnumExtender.add(ArmorMaterials.class, "AETHER_GRAVITITE",
            "aether_gravitite", 33, new int[]{3, 6, 8, 3}, 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND,
            2f, 0f, itemIngredient(AetherItems.GRAVITITE_GEM));

    public static final ArmorMaterial NEPTUNE = EnumExtender.add(ArmorMaterials.class, "AETHER_NEPTUNE",
            "aether_neptune", 35, new int[]{4, 6, 8, 4}, 8, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND,
            1.5f, 0f, EMPTY);

    public static final ArmorMaterial PHOENIX = EnumExtender.add(ArmorMaterials.class, "AETHER_GRAVITITE",
            "aether_phoenix", 35, new int[]{4, 6, 8, 4}, 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND,
            1.5F, 0f, EMPTY);

    public static final ArmorMaterial OBSIDIAN = EnumExtender.add(ArmorMaterials.class, "AETHER_GRAVITITE",
            "aether_obsidian", 38, new int[]{4, 8, 10, 4}, 6, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE,
            2f, 0.1f, EMPTY);

    public static final ArmorMaterial VALKYRIE = EnumExtender.add(ArmorMaterials.class, "AETHER_GRAVITITE",
            "aether_valkyrie", 35, new int[]{4, 8, 10, 4}, 10, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE,
            1.5f, 0.1f, EMPTY);

    public static final ArmorMaterial SENTRY = EnumExtender.add(ArmorMaterials.class, "AETHER_GRAVITITE",
            "aether_sentry", 35, new int[]{4, 8, 10, 4}, 10, SoundEvents.ITEM_ARMOR_EQUIP_TURTLE,
            1.5f, 0f, EMPTY);
}