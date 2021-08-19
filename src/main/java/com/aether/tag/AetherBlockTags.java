package com.aether.tag;

import com.aether.Aether;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class AetherBlockTags {
    public static final Tag<Block> FAST_FLOATERS = TagRegistry.block(new Identifier(Aether.MOD_ID, "fast_floaters"));
    public static final Tag<Block> SWET_TRANSFORMERS_BLUE = TagRegistry.block(new Identifier(Aether.MOD_ID, "swet_transformers/blue"));
    public static final Tag<Block> SWET_TRANSFORMERS_GOLDEN = TagRegistry.block(new Identifier(Aether.MOD_ID, "swet_transformers/golden"));
    public static final Tag<Block> SWET_TRANSFORMERS_PURPLE = TagRegistry.block(new Identifier(Aether.MOD_ID, "swet_transformers/purple"));
    public static final Tag<Block> SWET_TRANSFORMERS_VERMILION = TagRegistry.block(new Identifier(Aether.MOD_ID, "swet_transformers/vermilion"));
}
