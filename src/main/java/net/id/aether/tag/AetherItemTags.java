package net.id.aether.tag;

import net.fabricmc.fabric.api.tag.TagFactory;
import net.id.aether.Aether;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;

public class AetherItemTags {
    public static final Tag<Item> GROWS_SWETS = TagFactory.ITEM.create(Aether.locate("grows_swets"));
    public static final Tag<Item> SWET_TRANSFORMERS_BLUE = TagFactory.ITEM.create(Aether.locate("swet_transformers/blue"));
    public static final Tag<Item> SWET_TRANSFORMERS_GOLDEN = TagFactory.ITEM.create(Aether.locate("swet_transformers/golden"));
    public static final Tag<Item> SWET_TRANSFORMERS_PURPLE = TagFactory.ITEM.create(Aether.locate("swet_transformers/purple"));
    public static final Tag<Item> SWET_TRANSFORMERS_VERMILION = TagFactory.ITEM.create(Aether.locate("swet_transformers/vermilion"));
    public static final Tag<Item> PARACHUTES = TagFactory.ITEM.create(Aether.locate("parachutes"));
    public static final Tag<Item> MOA_TEMPTABLES = TagFactory.ITEM.create(Aether.locate("entity/moa_temptables"));
    public static final Tag<Item> RIGHTEOUS_WEAPONS = TagFactory.ITEM.create(Aether.locate("tool/righteous_weapons"));
    public static final Tag<Item> SACRED_WEAPONS = TagFactory.ITEM.create(Aether.locate("tool/sacred_weapons"));
}
