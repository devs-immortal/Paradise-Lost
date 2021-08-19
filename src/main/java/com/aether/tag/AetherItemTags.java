package com.aether.tag;

import com.aether.Aether;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class AetherItemTags {
    public static final Tag<Item> GROWS_SWETS = TagRegistry.item(new Identifier(Aether.MOD_ID, "grows_swets"));
    public static final Tag<Item> SWET_TRANSFORMERS_BLUE = TagRegistry.item(new Identifier(Aether.MOD_ID, "swet_transformers/blue"));
    public static final Tag<Item> SWET_TRANSFORMERS_GOLDEN = TagRegistry.item(new Identifier(Aether.MOD_ID, "swet_transformers/golden"));
    public static final Tag<Item> SWET_TRANSFORMERS_PURPLE = TagRegistry.item(new Identifier(Aether.MOD_ID, "swet_transformers/purple"));
    public static final Tag<Item> SWET_TRANSFORMERS_VERMILION = TagRegistry.item(new Identifier(Aether.MOD_ID, "swet_transformers/vermilion"));
}
