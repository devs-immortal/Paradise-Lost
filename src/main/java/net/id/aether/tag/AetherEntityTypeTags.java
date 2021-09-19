package net.id.aether.tag;

import net.fabricmc.fabric.api.tag.TagFactory;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.id.aether.Aether;
import net.minecraft.entity.EntityType;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class AetherEntityTypeTags {
    public static final Tag<EntityType<?>> SWET_TRANSFORMERS_BLUE = TagFactory.ENTITY_TYPE.create(new Identifier(Aether.MOD_ID, "swet_transformers/blue"));
    public static final Tag<EntityType<?>> SWET_TRANSFORMERS_GOLDEN = TagFactory.ENTITY_TYPE.create(new Identifier(Aether.MOD_ID, "swet_transformers/golden"));
    public static final Tag<EntityType<?>> SWET_TRANSFORMERS_PURPLE = TagFactory.ENTITY_TYPE.create(new Identifier(Aether.MOD_ID, "swet_transformers/purple"));
    public static final Tag<EntityType<?>> SWET_TRANSFORMERS_VERMILION = TagFactory.ENTITY_TYPE.create(new Identifier(Aether.MOD_ID, "swet_transformers/vermilion"));

    public static final Tag<EntityType<?>> VENOM_IMMUNITY = TagFactory.ENTITY_TYPE.create(Aether.locate("condition_immunities/venom"));
    public static final Tag<EntityType<?>> ABSTENTINE_TOXICITY_IMMUNITY = TagFactory.ENTITY_TYPE.create(Aether.locate("condition_immunities/abstentine_toxicity"));
    public static final Tag<EntityType<?>> BLOODTINGE_IMMUNITY = TagFactory.ENTITY_TYPE.create(Aether.locate("condition_immunities/bloodtinge"));
    public static final Tag<EntityType<?>> ENTRANCEMENT_IMMUNITY = TagFactory.ENTITY_TYPE.create(Aether.locate("condition_immunities/entrancement"));
    public static final Tag<EntityType<?>> FROSTBITE_IMMUNITY = TagFactory.ENTITY_TYPE.create(Aether.locate("condition_immunities/frostbite"));
}
