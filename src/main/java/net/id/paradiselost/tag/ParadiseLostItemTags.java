package net.id.paradiselost.tag;

import net.id.paradiselost.ParadiseLost;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;

public class ParadiseLostItemTags {
    public static final TagKey<Item> PARACHUTES = register("parachutes");
    public static final TagKey<Item> MOA_TEMPTABLES = register("entity/moa_temptables");
    public static final TagKey<Item> RIGHTEOUS_WEAPONS = register("tool/righteous_weapons");
    public static final TagKey<Item> SACRED_WEAPONS = register("tool/sacred_weapons");
    public static final TagKey<Item> IGNITING_TOOLS = register("tool/igniting_tools");

    private static TagKey<Item> register(String id) {
        return TagKey.of(Registry.ITEM_KEY, ParadiseLost.locate(id));
    }
}
