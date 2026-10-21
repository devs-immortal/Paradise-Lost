package net.id.paradise_lost.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.id.paradise_lost.ModConstants;

public class ParadiseLostEntityTypeTags {

    public static final TagKey<EntityType<?>> CANNOT_LEVITATE = register("cannot_levitate");

    private static TagKey<EntityType<?>> register(String id) {
        return TagKey.create(Registries.ENTITY_TYPE, ModConstants.locate(id));
    }
}
