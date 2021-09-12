package net.id.aether.tag;

import net.fabricmc.fabric.api.tag.TagFactory;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.id.aether.Aether;
import net.minecraft.block.Block;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class AetherBlockTags {
    public static final Tag<Block> FAST_FLOATERS = TagFactory.BLOCK.create(new Identifier(Aether.MOD_ID, "fast_floaters"));
    public static final Tag<Block> NON_FLOATERS = TagFactory.BLOCK.create(new Identifier(Aether.MOD_ID, "non_floaters"));
    public static final Tag<Block> LICHEN_SPREADABLES = TagFactory.BLOCK.create(Aether.locate("world/lichen_spreadable"));
    public static final Tag<Block> SWET_TRANSFORMERS_BLUE = TagFactory.BLOCK.create(new Identifier(Aether.MOD_ID, "swet_transformers/blue"));
    public static final Tag<Block> SWET_TRANSFORMERS_GOLDEN = TagFactory.BLOCK.create(new Identifier(Aether.MOD_ID, "swet_transformers/golden"));
    public static final Tag<Block> SWET_TRANSFORMERS_PURPLE = TagFactory.BLOCK.create(new Identifier(Aether.MOD_ID, "swet_transformers/purple"));
    public static final Tag<Block> SWET_TRANSFORMERS_VERMILION = TagFactory.BLOCK.create(new Identifier(Aether.MOD_ID, "swet_transformers/vermilion"));
}
