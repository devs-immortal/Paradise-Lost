package net.id.aether.items;

import net.id.aether.Aether;
import net.id.aether.blocks.AetherBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

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

    // TODO: find a new icon for this item group, or change it to the bronze key when implemented
    public static final ItemGroup AETHER_MISC = build(
            Aether.locate("aether_misc"),
            () -> new ItemStack(Items.BARRIER));

    public static final ItemGroup AETHER_WEARABLES = build(
            Aether.locate("aether_wearables"),
            () -> new ItemStack(AetherItems.ZANITE_CHESTPLATE));
}
