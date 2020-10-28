package com.aether.items;

import com.aether.Aether;
import com.aether.blocks.AetherBlocks;
import com.aether.items.tools.AetherAxeItem;
import com.aether.items.tools.AetherPickaxeItem;
import com.aether.items.tools.AetherShovelItem;
import com.aether.items.tools.AetherSwordItem;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
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

    public static final ItemGroup ARMOR_GROUP = FabricItemGroupBuilder.build(
            new Identifier(Aether.MODID, "aether_armor"),
            () -> new ItemStack(AetherItems.GRAVITITE_CHESTPLATE));

    public static final Item BLUE_PORTAL = new BlockItem(AetherBlocks.BLUE_PORTAL, new Item.Settings().group(null));
    public static final Item AETHER_DIRT = new BlockItem(AetherBlocks.AETHER_DIRT, new Item.Settings().group(BLOCKS_GROUP));
    public static final Item AETHER_GRASS = new BlockItem(AetherBlocks.AETHER_GRASS, new Item.Settings().group(BLOCKS_GROUP));
    public static final Item ENCHANTED_AETHER_GRASS = new BlockItem(AetherBlocks.AETHER_ENCHANTED_GRASS, new Item.Settings().group(BLOCKS_GROUP).rarity(Rarity.UNCOMMON));

    public static final Item SKYROOT_LOG = new BlockItem(AetherBlocks.SKYROOT_LOG, new Item.Settings().group(BLOCKS_GROUP));
    public static final Item SKYROOT_PLANK = new BlockItem(AetherBlocks.SKYROOT_PLANK, new Item.Settings().group(BLOCKS_GROUP));

    public static final Item HOLYSTONE = new BlockItem(AetherBlocks.HOLYSTONE, new Item.Settings().group(BLOCKS_GROUP));
    public static final Item HOLYSTONE_BRICK = new BlockItem(AetherBlocks.HOLYSTONE_BRICK, new Item.Settings().group(BLOCKS_GROUP));
    public static final Item MOSSY_HOLYSTONE = new BlockItem(AetherBlocks.MOSSY_HOLYSTONE, new Item.Settings().group(BLOCKS_GROUP));

    // Don't worry, It's a placeholder. Ok?
    public static final Item GRAVITITE_INGOT =  new Item((new Item.Settings()).group(null));

    public static final Item GRAVITITE_PICKAXE = new AetherPickaxeItem(ToolMaterials.DIAMOND, 1, -2.8F, (new Item.Settings()).group(TOOLS_GROUP));
    public static final Item GRAVITITE_AXE = new AetherAxeItem(ToolMaterials.DIAMOND, 5.0F, -3.0F, (new Item.Settings()).group(TOOLS_GROUP));
    public static final Item GRAVITITE_SHOVEL = new AetherShovelItem(ToolMaterials.DIAMOND, 1.5F, -3.0F, (new Item.Settings()).group(TOOLS_GROUP));
    public static final Item GRAVITITE_SWORD = new AetherSwordItem(ToolMaterials.DIAMOND, 3, -2.4F, (new Item.Settings()).group(TOOLS_GROUP));

    public static final Item GRAVITITE_BOOTS = new ArmorItem(AetherArmorMaterials.GRAVITITE, EquipmentSlot.FEET, (new Item.Settings()).group(ARMOR_GROUP));
    public static final Item GRAVITITE_LEGGINGS = new ArmorItem(AetherArmorMaterials.GRAVITITE, EquipmentSlot.LEGS, (new Item.Settings()).group(ARMOR_GROUP));
    public static final Item GRAVITITE_HELMET = new ArmorItem(AetherArmorMaterials.GRAVITITE, EquipmentSlot.HEAD, (new Item.Settings()).group(ARMOR_GROUP));
    public static final Item GRAVITITE_CHESTPLATE = new ArmorItem(AetherArmorMaterials.GRAVITITE, EquipmentSlot.CHEST, (new Item.Settings()).group(ARMOR_GROUP));

    public static void initialization() {
        // Items (Blocks)
        Registry.register(Registry.ITEM, new Identifier(Aether.MODID, "blue_portal"), BLUE_PORTAL);
        Registry.register(Registry.ITEM, new Identifier(Aether.MODID, "aether_dirt"), AETHER_DIRT);
        Registry.register(Registry.ITEM, new Identifier(Aether.MODID, "aether_grass"), AETHER_GRASS);
        Registry.register(Registry.ITEM, new Identifier(Aether.MODID, "enchanted_aether_grass"), ENCHANTED_AETHER_GRASS);

        Registry.register(Registry.ITEM, new Identifier(Aether.MODID, "skyroot_log"), SKYROOT_LOG);
        Registry.register(Registry.ITEM, new Identifier(Aether.MODID, "skyroot_plank"), SKYROOT_PLANK);

        Registry.register(Registry.ITEM, new Identifier(Aether.MODID, "holystone"), HOLYSTONE);
        Registry.register(Registry.ITEM, new Identifier(Aether.MODID, "holystone_brick"), HOLYSTONE_BRICK);
        Registry.register(Registry.ITEM, new Identifier(Aether.MODID, "mossy_holystone"), MOSSY_HOLYSTONE);

        // Items (Tools)
        Registry.register(Registry.ITEM, new Identifier(Aether.MODID, "gravitite_pickaxe"), GRAVITITE_PICKAXE);
        Registry.register(Registry.ITEM, new Identifier(Aether.MODID, "gravitite_axe"), GRAVITITE_AXE);
        Registry.register(Registry.ITEM, new Identifier(Aether.MODID, "gravitite_shovel"), GRAVITITE_SHOVEL);
        Registry.register(Registry.ITEM, new Identifier(Aether.MODID, "gravitite_sword"), GRAVITITE_SWORD);

        Registry.register(Registry.ITEM, new Identifier(Aether.MODID, "gravitite_ingot"), GRAVITITE_INGOT);

        // Items (Armor)
        Registry.register(Registry.ITEM, new Identifier(Aether.MODID, "gravitite_boots"), GRAVITITE_BOOTS);
        Registry.register(Registry.ITEM, new Identifier(Aether.MODID, "gravitite_leggings"), GRAVITITE_LEGGINGS);
        Registry.register(Registry.ITEM, new Identifier(Aether.MODID, "gravitite_helmet"), GRAVITITE_HELMET);
        Registry.register(Registry.ITEM, new Identifier(Aether.MODID, "gravitite_chestplate"), GRAVITITE_CHESTPLATE);
    }
}
