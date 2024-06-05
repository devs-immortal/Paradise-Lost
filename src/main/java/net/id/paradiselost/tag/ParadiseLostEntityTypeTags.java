package net.id.paradiselost.tag;

import net.id.paradiselost.ParadiseLost;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class ParadiseLostEntityTypeTags {

    private static TagKey<EntityType<?>> register(String id) {
        return TagKey.of(RegistryKeys.ENTITY_TYPE, ParadiseLost.locate(id));
    }
}
