package net.id.paradiselost.tag;

import net.id.paradiselost.ParadiseLost;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;

public class ParadiseLostItemTags {
    public static final TagKey<Item> GROWS_SWETS = register("grows_swets");
    public static final TagKey<Item> SWET_TRANSFORMERS_BLUE = register("swet_transformers/blue");
    public static final TagKey<Item> SWET_TRANSFORMERS_GOLDEN = register("swet_transformers/golden");
    public static final TagKey<Item> SWET_TRANSFORMERS_PURPLE = register("swet_transformers/purple");
    public static final TagKey<Item> SWET_TRANSFORMERS_VERMILION = register("swet_transformers/vermilion");
    public static final TagKey<Item> PARACHUTES = register("parachutes");
    public static final TagKey<Item> MOA_TEMPTABLES = register("entity/moa_temptables");
    public static final TagKey<Item> RIGHTEOUS_WEAPONS = register("tool/righteous_weapons");
    public static final TagKey<Item> SACRED_WEAPONS = register("tool/sacred_weapons");

    private static TagKey<Item> register(String id) {
        return TagKey.of(Registry.ITEM_KEY, ParadiseLost.locate(id));
    }
}
