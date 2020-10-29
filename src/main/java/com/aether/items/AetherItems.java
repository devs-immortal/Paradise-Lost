package com.aether.items;

import com.aether.Aether;
import com.aether.blocks.AetherBlocks;
import com.aether.items.tools.*;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AetherItems {

    private static Item.Settings defaultItem() {
        return new FabricItemSettings().group(AetherItemGroups.TOOLS);
    }

    public static final Item GRAVITITE_INGOT;

    public static final Item GRAVITITE_PICKAXE;
    public static final Item GRAVITITE_AXE;
    public static final Item GRAVITITE_SHOVEL;
    public static final Item GRAVITITE_SWORD;
    public static final Item GRAVITITE_HOE;

    public static final Item ZANITE_GEM;

    public static final Item ZANITE_PICKAXE;
    public static final Item ZANITE_AXE;
    public static final Item ZANITE_SHOVEL;
    public static final Item ZANITE_SWORD;
    public static final Item ZANITE_HOE;

    public static final Item HOLYSTONE_PICKAXE;
    public static final Item HOLYSTONE_AXE;
    public static final Item HOLYSTONE_SHOVEL;
    public static final Item HOLYSTONE_SWORD;
    public static final Item HOLYSTONE_HOE;

    public static final Item SKYROOT_PICKAXE;
    public static final Item SKYROOT_AXE;
    public static final Item SKYROOT_SHOVEL;
    public static final Item SKYROOT_SWORD;
    public static final Item SKYROOT_HOE;

    public static final Item GRAVITITE_BOOTS;
    public static final Item GRAVITITE_LEGGINGS;
    public static final Item GRAVITITE_HELMET;
    public static final Item GRAVITITE_CHESTPLATE;

    static {
        // There is no gravitite ingot in aether. I think...
        GRAVITITE_INGOT = register("gravitite_ingot", new Item(defaultItem()));

        GRAVITITE_PICKAXE = register("gravitite_pickaxe", new AetherPickaxeItem(AetherToolMaterials.GRAVITITE, 1, -2.8F, defaultItem()));
        GRAVITITE_AXE = register("gravitite_axe", new AetherAxeItem(AetherToolMaterials.GRAVITITE, 5.0F, -3.0F, defaultItem()));
        GRAVITITE_SHOVEL = register("gravitite_shovel", new AetherShovelItem(AetherToolMaterials.GRAVITITE, 1.5F, -3.0F, defaultItem()));
        GRAVITITE_SWORD = register("gravitite_sword", new AetherSwordItem(AetherToolMaterials.GRAVITITE, 3, -2.4F, defaultItem()));
        GRAVITITE_HOE = register("gravitite_hoe", new AetherHoeItem(AetherToolMaterials.GRAVITITE, -1, 0.0F, defaultItem()));

        ZANITE_GEM = register("zanite_gemstone", new Item(defaultItem()));

        ZANITE_PICKAXE = register("zanite_pickaxe", new AetherPickaxeItem(AetherToolMaterials.ZANITE, 1, -2.8F, defaultItem()));
        ZANITE_AXE = register("zanite_axe", new AetherAxeItem(AetherToolMaterials.ZANITE, 6.0F, -3.1F, defaultItem()));
        ZANITE_SHOVEL = register("zanite_shovel", new AetherShovelItem(AetherToolMaterials.ZANITE, 1.5F, -3.0F, defaultItem()));
        ZANITE_SWORD = register("zanite_sword", new AetherSwordItem(AetherToolMaterials.ZANITE, 3, -2.4F, defaultItem()));
        ZANITE_HOE = register("zanite_hoe", new AetherHoeItem(AetherToolMaterials.ZANITE, -2, -1.0F, defaultItem()));

        HOLYSTONE_PICKAXE = register("holystone_pickaxe", new AetherPickaxeItem(ToolMaterials.DIAMOND, 1, -2.8F, defaultItem()));
        HOLYSTONE_AXE = register("holystone_axe", new AetherAxeItem(ToolMaterials.DIAMOND, 5.0F, -3.0F, defaultItem()));
        HOLYSTONE_SHOVEL = register("holystone_shovel", new AetherShovelItem(ToolMaterials.DIAMOND, 1.5F, -3.0F, defaultItem()));
        HOLYSTONE_SWORD = register("holystone_sword", new AetherSwordItem(ToolMaterials.DIAMOND, 3, -2.4F, defaultItem()));
        HOLYSTONE_HOE = register("holystone_hoe", new AetherHoeItem(ToolMaterials.DIAMOND, -1, 0.0F, defaultItem()));

        SKYROOT_PICKAXE = register("skyroot_pickaxe", new AetherPickaxeItem(ToolMaterials.DIAMOND, 1, -2.8F, defaultItem()));
        SKYROOT_AXE = register("skyroot_axe", new AetherAxeItem(ToolMaterials.DIAMOND, 5.0F, -3.0F, defaultItem()));
        SKYROOT_SHOVEL = register("skyroot_shovel", new AetherShovelItem(ToolMaterials.DIAMOND, 1.5F, -3.0F, defaultItem()));
        SKYROOT_SWORD = register("skyroot_sword", new AetherSwordItem(ToolMaterials.DIAMOND, 3, -2.4F, defaultItem()));
        SKYROOT_HOE = register("skyroot_hoe", new AetherHoeItem(ToolMaterials.DIAMOND, -1, 0.0F, defaultItem()));

        GRAVITITE_BOOTS = register("gravitite_boots", new ArmorItem(AetherArmorMaterials.GRAVITITE, EquipmentSlot.FEET, (new Item.Settings()).group(AetherItemGroups.ARMOR)));
        GRAVITITE_LEGGINGS = register("gravitite_leggings", new ArmorItem(AetherArmorMaterials.GRAVITITE, EquipmentSlot.LEGS, (new Item.Settings()).group(AetherItemGroups.ARMOR)));
        GRAVITITE_HELMET = register("gravitite_helmet", new ArmorItem(AetherArmorMaterials.GRAVITITE, EquipmentSlot.HEAD, (new Item.Settings()).group(AetherItemGroups.ARMOR)));
        GRAVITITE_CHESTPLATE = register("gravitite_chestplate", new ArmorItem(AetherArmorMaterials.GRAVITITE, EquipmentSlot.CHEST, (new Item.Settings()).group(AetherItemGroups.ARMOR)));
    }

    private static Item register(String id, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(Aether.MODID, id), item);
    }
}
