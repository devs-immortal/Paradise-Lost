package com.aether.items;

import com.aether.Aether;
import com.aether.blocks.AetherBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import static net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder.build;

public class AetherItemGroups {
    public static final ItemGroup AETHER_BLOCKS = build(
            Aether.locate("aether_blocks"),
            () -> new ItemStack(AetherBlocks.AETHER_GRASS_BLOCK));

    public static final ItemGroup AETHER_TOOLS = build(
            Aether.locate("aether_tools"),
            () -> new ItemStack(AetherItems.GRAVITITE_PICKAXE));

    public static final ItemGroup AETHER_FOOD = build(
            Aether.locate("aether_food"),
            () -> new ItemStack(AetherItems.BLUEBERRY));

    public static final ItemGroup AETHER_RESOURCES = build(
            Aether.locate("aether_resources"),
            () -> new ItemStack(AetherItems.AMBROSIUM_SHARD));

    public static final ItemGroup AETHER_MISC = build(
            Aether.locate("aether_misc"),
            () -> new ItemStack(AetherItems.BRONZE_KEY));

    public static final ItemGroup AETHER_WEARABLES = build(
            Aether.locate("aether_wearables"),
            () -> new ItemStack(AetherItems.ZANITE_CHESTPLATE));
}
