package com.aether.util;

import com.aether.Aether;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.RequiredTagList;
import net.minecraft.tag.RequiredTagListRegistry;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AetherBlockTags {
    public static final Tag<Block> FAST_FLOATERS = TagRegistry.block(new Identifier(Aether.MOD_ID, "fast_floaters"));
}
