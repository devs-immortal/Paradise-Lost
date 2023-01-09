package net.id.paradiselost.items;

import net.id.paradiselost.ParadiseLost;
import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import static net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder.build;

public class ParadiseLostItemGroups {
    public static final ItemGroup PARADISE_LOST_BUILDING_BLOCKS = build(
            ParadiseLost.locate("building_blocks"),
            () -> new ItemStack(ParadiseLostBlocks.HIGHLANDS_GRASS));

    public static final ItemGroup PARADISE_LOST_DECORATIONS = build(
            ParadiseLost.locate("decorations"),
            () -> new ItemStack(ParadiseLostBlocks.GIANT_LILY));

    public static final ItemGroup PARADISE_LOST_TOOLS = build(
            ParadiseLost.locate("tools"),
            () -> new ItemStack(ParadiseLostItems.OLVITE_PICKAXE));

    public static final ItemGroup PARADISE_LOST_FOOD = build(
            ParadiseLost.locate("food"),
            () -> new ItemStack(ParadiseLostItems.BLUEBERRY));

    public static final ItemGroup PARADISE_LOST_RESOURCES = build(
            ParadiseLost.locate("resources"),
            () -> new ItemStack(ParadiseLostItems.CHERINE));

    public static final ItemGroup PARADISE_LOST_MISC = build(
            ParadiseLost.locate("misc"),
            () -> new ItemStack(ParadiseLostItems.LORE_BOOK));

    public static final ItemGroup PARADISE_LOST_WEARABLES = build(
            ParadiseLost.locate("wearables"),
            () -> new ItemStack(ParadiseLostItems.OLVITE_CHESTPLATE));
}
