package com.aether.items;

import com.aether.Aether;
import com.aether.blocks.AetherBlocks;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class AetherItemGroups {
    public static final CreativeModeTab Blocks = FabricItemGroupBuilder.build(
            Aether.locate("aether_blocks"),
            () -> new ItemStack(AetherBlocks.AETHER_GRASS_BLOCK));

    public static final CreativeModeTab Tools = FabricItemGroupBuilder.build(
            Aether.locate("aether_tools"),
            () -> new ItemStack(AetherItems.GRAVITITE_PICKAXE));

    public static final CreativeModeTab Food = FabricItemGroupBuilder.build(
            Aether.locate("aether_food"),
            () -> new ItemStack(AetherItems.BLUEBERRY));

    public static final CreativeModeTab Resources = FabricItemGroupBuilder.build(
            Aether.locate("aether_resources"),
            () -> new ItemStack(AetherItems.AMBROSIUM_SHARD));

    public static final CreativeModeTab Misc = FabricItemGroupBuilder.build(
            Aether.locate("aether_misc"),
            () -> new ItemStack(AetherItems.BRONZE_KEY));

    public static final CreativeModeTab Wearable = FabricItemGroupBuilder.build(
            Aether.locate("aether_wearables"),
            () -> new ItemStack(AetherItems.ZANITE_CHESTPLATE));
}
