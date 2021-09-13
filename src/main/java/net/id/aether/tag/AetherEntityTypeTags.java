package net.id.aether.tag;

import net.fabricmc.fabric.api.tag.TagFactory;
import net.id.aether.Aether;
import net.minecraft.entity.EntityType;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class AetherEntityTypeTags {
    public static final Tag<EntityType<?>> SWET_TRANSFORMERS_BLUE = TagFactory.ENTITY_TYPE.create(new Identifier(Aether.MOD_ID, "swet_transformers/blue"));
    public static final Tag<EntityType<?>> SWET_TRANSFORMERS_GOLDEN = TagFactory.ENTITY_TYPE.create(new Identifier(Aether.MOD_ID, "swet_transformers/golden"));
    public static final Tag<EntityType<?>> SWET_TRANSFORMERS_PURPLE = TagFactory.ENTITY_TYPE.create(new Identifier(Aether.MOD_ID, "swet_transformers/purple"));
    public static final Tag<EntityType<?>> SWET_TRANSFORMERS_VERMILION = TagFactory.ENTITY_TYPE.create(new Identifier(Aether.MOD_ID, "swet_transformers/vermilion"));

}
