package com.aether.items;

import com.aether.Aether;
import com.aether.blocks.AetherBlocks;
import com.aether.items.tools.*;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

public class AetherItems {

    public static final ItemGroup BLOCKS_GROUP = FabricItemGroupBuilder.build(
            new Identifier(Aether.MODID, "aether_blocks"),
            () -> new ItemStack(AetherBlocks.AETHER_GRASS));

    public static final ItemGroup TOOLS_GROUP = FabricItemGroupBuilder.build(
            new Identifier(Aether.MODID, "aether_tools"),
            () -> new ItemStack(AetherItems.GRAVITITE_PICKAXE));

    private static Item.Settings defaultItem() { return new FabricItemSettings().group(TOOLS_GROUP); }

    public static final ItemGroup ARMOR_GROUP = FabricItemGroupBuilder.build(
            new Identifier(Aether.MODID, "aether_armor"),
            () -> new ItemStack(AetherItems.GRAVITITE_CHESTPLATE));

    public static final Item BLUE_PORTAL = new BlockItem(AetherBlocks.BLUE_PORTAL, new Item.Settings().group(null));

    public static final Item GRAVITITE_INGOT = register("gravitite_ingot", new Item(defaultItem()));

    public static final Item GRAVITITE_PICKAXE = register("gravitite_pickaxe", new AetherPickaxeItem(AetherToolMaterials.GRAVITITE, 1, -2.8F, defaultItem()));
    public static final Item GRAVITITE_AXE = register("gravitite_axe", new AetherAxeItem(AetherToolMaterials.GRAVITITE, 5.0F, -3.0F, defaultItem()));
    public static final Item GRAVITITE_SHOVEL = register("gravitite_shovel", new AetherShovelItem(AetherToolMaterials.GRAVITITE, 1.5F, -3.0F, defaultItem()));
    public static final Item GRAVITITE_SWORD = register("gravitite_sword", new AetherSwordItem(AetherToolMaterials.GRAVITITE, 3, -2.4F, defaultItem()));
    public static final Item GRAVITITE_HOE = register("gravitite_hoe", new AetherHoeItem(AetherToolMaterials.GRAVITITE, -1, 0.0F, defaultItem()));

    public static final Item ZANITE_GEM = register("zanite", new Item(defaultItem()));

    public static final Item ZANITE_PICKAXE = register("zanite_pickaxe", new AetherPickaxeItem(AetherToolMaterials.ZANITE, 1, -2.8F, defaultItem()));
    public static final Item ZANITE_AXE = register("zanite_axe", new AetherAxeItem(AetherToolMaterials.ZANITE, 6.0F, -3.1F, defaultItem()));
    public static final Item ZANITE_SHOVEL = register("zanite_shovel", new AetherShovelItem(AetherToolMaterials.ZANITE, 1.5F, -3.0F, defaultItem()));
    public static final Item ZANITE_SWORD = register("zanite_sword", new AetherSwordItem(AetherToolMaterials.ZANITE, 3, -2.4F, defaultItem()));
    public static final Item ZANITE_HOE = register("zanite_hoe", new AetherHoeItem(AetherToolMaterials.ZANITE, -2, -1.0F, defaultItem()));

    public static final Item HOLYSTONE_PICKAXE = register("holystone_pickaxe", new AetherPickaxeItem(ToolMaterials.DIAMOND, 1, -2.8F, defaultItem()));
    public static final Item HOLYSTONE_AXE = register("holystone_axe", new AetherAxeItem(ToolMaterials.DIAMOND, 5.0F, -3.0F, defaultItem()));
    public static final Item HOLYSTONE_SHOVEL = register("holystone_shovel", new AetherShovelItem(ToolMaterials.DIAMOND, 1.5F, -3.0F, defaultItem()));
    public static final Item HOLYSTONE_SWORD = register("holystone_sword", new AetherSwordItem(ToolMaterials.DIAMOND, 3, -2.4F, defaultItem()));
    public static final Item HOLYSTONE_HOE = register("holystone_hoe", new AetherHoeItem(ToolMaterials.DIAMOND, -1, 0.0F, defaultItem()));

    public static final Item SKYROOT_PICKAXE = register("skyroot_pickaxe", new AetherPickaxeItem(ToolMaterials.DIAMOND, 1, -2.8F, defaultItem()));
    public static final Item SKYROOT_AXE = register("skyroot_axe", new AetherAxeItem(ToolMaterials.DIAMOND, 5.0F, -3.0F, defaultItem()));
    public static final Item SKYROOT_SHOVEL = register("skyroot_shovel", new AetherShovelItem(ToolMaterials.DIAMOND, 1.5F, -3.0F, defaultItem()));
    public static final Item SKYROOT_SWORD = register("skyroot_sword", new AetherSwordItem(ToolMaterials.DIAMOND, 3, -2.4F, defaultItem()));
    public static final Item SKYROOT_HOE = register("skyroot_hoe", new AetherHoeItem(ToolMaterials.DIAMOND, -1, 0.0F, defaultItem()));

    public static final Item GRAVITITE_BOOTS = register("gravitite_boots", new ArmorItem(AetherArmorMaterials.GRAVITITE, EquipmentSlot.FEET, (new Item.Settings()).group(ARMOR_GROUP)));
    public static final Item GRAVITITE_LEGGINGS = register("gravitite_leggings", new ArmorItem(AetherArmorMaterials.GRAVITITE, EquipmentSlot.LEGS, (new Item.Settings()).group(ARMOR_GROUP)));
    public static final Item GRAVITITE_HELMET = register("gravitite_helmet", new ArmorItem(AetherArmorMaterials.GRAVITITE, EquipmentSlot.HEAD, (new Item.Settings()).group(ARMOR_GROUP)));
    public static final Item GRAVITITE_CHESTPLATE = register("gravitite_chestplate", new ArmorItem(AetherArmorMaterials.GRAVITITE, EquipmentSlot.CHEST, (new Item.Settings()).group(ARMOR_GROUP)));

    public static void initialization() {
        // NO-OP. But the game needs to be told this place exists
        Registry.register(Registry.ITEM, new Identifier(Aether.MODID, "blue_portal"), BLUE_PORTAL);
    }

    private static Item register(String id, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(Aether.MODID, id), item);
    }
}
