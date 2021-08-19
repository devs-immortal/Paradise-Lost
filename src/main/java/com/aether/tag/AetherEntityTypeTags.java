package com.aether.tag;

import com.aether.Aether;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class AetherEntityTypeTags {
    public static final Tag<EntityType<?>> SWET_TRANSFORMERS_BLUE = TagRegistry.entityType(new Identifier(Aether.MOD_ID, "swet_transformers/blue"));
    public static final Tag<EntityType<?>> SWET_TRANSFORMERS_GOLDEN = TagRegistry.entityType(new Identifier(Aether.MOD_ID, "swet_transformers/golden"));
    public static final Tag<EntityType<?>> SWET_TRANSFORMERS_PURPLE = TagRegistry.entityType(new Identifier(Aether.MOD_ID, "swet_transformers/purple"));
    public static final Tag<EntityType<?>> SWET_TRANSFORMERS_VERMILION = TagRegistry.entityType(new Identifier(Aether.MOD_ID, "swet_transformers/vermilion"));

}
