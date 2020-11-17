package com.aether.items;

import com.aether.Aether;
import com.aether.blocks.AetherBlocks;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class AetherItemGroups {
    public static final ItemGroup Blocks = FabricItemGroupBuilder.build(
            Aether.locate("aether_blocks"),
            () -> new ItemStack(AetherBlocks.AETHER_GRASS));

    public static final ItemGroup Tools = FabricItemGroupBuilder.build(
            Aether.locate("aether_tools"),
            () -> new ItemStack(AetherItems.GRAVITITE_PICKAXE));

    public static final ItemGroup Weapons = FabricItemGroupBuilder.build(
            Aether.locate("aether_weapons"),
            () -> new ItemStack(AetherItems.GRAVITITE_SWORD));

    public static final ItemGroup Armor = FabricItemGroupBuilder.build(
            Aether.locate("aether_armor"),
            () -> new ItemStack(AetherItems.GRAVITITE_HELMET));

    public static final ItemGroup Food = FabricItemGroupBuilder.build(
            Aether.locate("aether_food"),
            () -> new ItemStack(AetherItems.BLUE_BERRY));

    public static final ItemGroup Accessories = FabricItemGroupBuilder.build(
            Aether.locate("aether_accessories"),
            () -> new ItemStack(AetherItems.GRAVITITE_GLOVES));

    public static final ItemGroup Materials = FabricItemGroupBuilder.build(
            Aether.locate("aether_materials"),
            () -> new ItemStack(AetherItems.AMBROSIUM_SHARD));

    public static final ItemGroup Misc = FabricItemGroupBuilder.build(
            Aether.locate("aether_misc"),
            () -> new ItemStack(AetherItems.BRONZE_KEY));
}
