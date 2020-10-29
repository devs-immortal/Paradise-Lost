package com.aether.items;

import com.aether.Aether;
import com.aether.blocks.AetherBlocks;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class AetherItemGroups {
    public static final ItemGroup BLOCKS = FabricItemGroupBuilder.build(
            new Identifier(Aether.MODID, "aether_blocks"),
            () -> new ItemStack(AetherBlocks.AETHER_GRASS));

    public static final ItemGroup TOOLS = FabricItemGroupBuilder.build(
            new Identifier(Aether.MODID, "aether_tools"),
            () -> new ItemStack(AetherItems.GRAVITITE_PICKAXE));

    public static final ItemGroup ARMOR = FabricItemGroupBuilder.build(
            new Identifier(Aether.MODID, "aether_armor"),
            () -> new ItemStack(AetherItems.GRAVITITE_CHESTPLATE));
}
