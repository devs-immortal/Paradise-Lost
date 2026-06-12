package net.id.paradiselost.items.tools;

import net.id.paradiselost.tag.ParadiseLostItemTags;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.tag.BlockTags;

@SuppressWarnings("unused")
public class ParadiseLostToolMaterials {
    public static final ToolMaterial OLVITE = new ToolMaterial(BlockTags.INCORRECT_FOR_IRON_TOOL, 250, 4.5f, 2f, 14, ParadiseLostItemTags.OLVITE_TOOL_MATERIALS);
    public static final ToolMaterial SURTRUM = new ToolMaterial(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 827, 7.0f, 3f, 16, ParadiseLostItemTags.SURTRUM_TOOL_MATERIALS);
    public static final ToolMaterial GLAZED_GOLD = new ToolMaterial(BlockTags.INCORRECT_FOR_IRON_TOOL, 131, 12f, 2f, 22, ParadiseLostItemTags.GLAZED_GOLD_TOOL_MATERIALS);
    public static final ToolMaterial SOUL_BLADE = new ToolMaterial(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 636, 6.5f, 1.5f, 17, ParadiseLostItemTags.SOUL_BLADE_TOOL_MATERIALS);
}
