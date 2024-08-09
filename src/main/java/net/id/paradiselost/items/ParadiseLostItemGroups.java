package net.id.paradiselost.items;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.id.paradiselost.ParadiseLost;
import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ParadiseLostItemGroups {

    private static final ItemGroup ITEM_GROUP = FabricItemGroup.builder(ParadiseLost.locate("building_blocks"))
            .icon(() -> new ItemStack(ParadiseLostBlocks.HIGHLANDS_GRASS))
            .entries((context, entries) -> {
                entries.add(ParadiseLostItems.LEVITA_BRICK);
            }).build();


    public static void init() {}
}
