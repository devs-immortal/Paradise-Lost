package net.id.aether.tag;

import net.fabricmc.fabric.api.tag.TagFactory;
import net.id.aether.Aether;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class AetherItemTags {
    public static final Tag<Item> GROWS_SWETS = TagFactory.ITEM.create(new Identifier(Aether.MOD_ID, "grows_swets"));
    public static final Tag<Item> SWET_TRANSFORMERS_BLUE = TagFactory.ITEM.create(new Identifier(Aether.MOD_ID, "swet_transformers/blue"));
    public static final Tag<Item> SWET_TRANSFORMERS_GOLDEN = TagFactory.ITEM.create(new Identifier(Aether.MOD_ID, "swet_transformers/golden"));
    public static final Tag<Item> SWET_TRANSFORMERS_PURPLE = TagFactory.ITEM.create(new Identifier(Aether.MOD_ID, "swet_transformers/purple"));
    public static final Tag<Item> SWET_TRANSFORMERS_VERMILION = TagFactory.ITEM.create(new Identifier(Aether.MOD_ID, "swet_transformers/vermilion"));
    public static final Tag<Item> PARACHUTES = TagFactory.ITEM.create(new Identifier(Aether.MOD_ID, "parachutes"));
}
