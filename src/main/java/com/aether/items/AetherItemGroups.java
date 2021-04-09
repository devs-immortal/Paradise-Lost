package com.aether.items;

import com.aether.Aether;
import com.aether.blocks.AetherBlocks;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class AetherItemGroups {
    public static final ItemGroup Blocks = FabricItemGroupBuilder.build(
            Aether.locate("aether_blocks"),
            () -> new ItemStack(AetherBlocks.AETHER_GRASS_BLOCK));

    public static final ItemGroup Tools = FabricItemGroupBuilder.build(
            Aether.locate("aether_tools"),
            () -> new ItemStack(AetherItems.GRAVITITE_PICKAXE));

//    public static final ItemGroup Weapons = FabricItemGroupBuilder.build(
//            Aether.locate("aether_weapons"),
//            () -> new ItemStack(AetherItems.GRAVITITE_SWORD));

//    public static final ItemGroup Armor = FabricItemGroupBuilder.build(
//            Aether.locate("aether_armor"),
//            () -> new ItemStack(AetherItems.ZANITE_CHESTPLATE));

    public static final ItemGroup Food = FabricItemGroupBuilder.build(
            Aether.locate("aether_food"),
            () -> new ItemStack(AetherItems.BLUEBERRY));

//    public static final ItemGroup Accessories = FabricItemGroupBuilder.build(
//            Aether.locate("aether_accessories"),
//            () -> new ItemStack(AetherItems.ZANITE_RING));

    public static final ItemGroup Resources = FabricItemGroupBuilder.build(
            Aether.locate("aether_resources"),
            () -> new ItemStack(AetherItems.AMBROSIUM_SHARD));

    public static final ItemGroup Misc = FabricItemGroupBuilder.build(
            Aether.locate("aether_misc"),
            () -> new ItemStack(AetherItems.BRONZE_KEY));

    public static final ItemGroup Wearable = FabricItemGroupBuilder.build(
            Aether.locate("aether_wearables"),
            () -> new ItemStack(AetherItems.ZANITE_CHESTPLATE));
}
