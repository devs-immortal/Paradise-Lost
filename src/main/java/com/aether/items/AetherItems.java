package com.aether.items;

import com.aether.Aether;
import com.aether.blocks.AetherBlocks;
import com.aether.items.tools.AetherAxeItem;
import com.aether.items.tools.AetherPickaxeItem;
import com.aether.items.tools.AetherShovelItem;
import com.aether.items.tools.AetherSwordItem;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
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

    public static final Item BLUE_PORTAL = new BlockItem(AetherBlocks.BLUE_PORTAL, new Item.Settings().group(null));
    public static final Item AETHER_DIRT = new BlockItem(AetherBlocks.AETHER_DIRT, new Item.Settings().group(BLOCKS_GROUP));
    public static final Item AETHER_GRASS = new BlockItem(AetherBlocks.AETHER_GRASS, new Item.Settings().group(BLOCKS_GROUP));
    public static final Item ENCHANTED_AETHER_GRASS = new BlockItem(AetherBlocks.AETHER_ENCHANTED_GRASS, new Item.Settings().group(BLOCKS_GROUP).rarity(Rarity.UNCOMMON));

    public static final Item GRAVITITE_PICKAXE = new AetherPickaxeItem(ToolMaterials.DIAMOND, 1, -2.8F, (new Item.Settings()).group(TOOLS_GROUP));
    public static final Item GRAVITITE_AXE = new AetherAxeItem(ToolMaterials.DIAMOND, 5.0F, -3.0F, (new Item.Settings()).group(TOOLS_GROUP));
    public static final Item GRAVITITE_SHOVEL = new AetherShovelItem(ToolMaterials.DIAMOND, 1.5F, -3.0F, (new Item.Settings()).group(TOOLS_GROUP));
    public static final Item GRAVITITE_SWORD = new AetherSwordItem(ToolMaterials.DIAMOND, 3, -2.4F, (new Item.Settings()).group(TOOLS_GROUP));

    public static void initialization() {
        // Items (Blocks)
        Registry.register(Registry.ITEM, new Identifier(Aether.MODID, "blue_portal"), BLUE_PORTAL);
        Registry.register(Registry.ITEM, new Identifier(Aether.MODID, "aether_dirt"), AETHER_DIRT);
        Registry.register(Registry.ITEM, new Identifier(Aether.MODID, "aether_grass"), AETHER_GRASS);
        Registry.register(Registry.ITEM, new Identifier(Aether.MODID, "enchanted_aether_grass"), ENCHANTED_AETHER_GRASS);

        // Items (Tools)
        Registry.register(Registry.ITEM, new Identifier(Aether.MODID, "gravitite_pickaxe"), GRAVITITE_PICKAXE);
        Registry.register(Registry.ITEM, new Identifier(Aether.MODID, "gravitite_axe"), GRAVITITE_AXE);
        Registry.register(Registry.ITEM, new Identifier(Aether.MODID, "gravitite_shovel"), GRAVITITE_SHOVEL);
        Registry.register(Registry.ITEM, new Identifier(Aether.MODID, "gravitite_sword"), GRAVITITE_SWORD);
    }
}
