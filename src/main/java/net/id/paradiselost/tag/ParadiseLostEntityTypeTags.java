package net.id.paradiselost.tag;

import net.id.paradiselost.ParadiseLost;
import net.minecraft.entity.EntityType;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;

public class ParadiseLostEntityTypeTags {
    public static final TagKey<EntityType<?>> SWET_TRANSFORMERS_BLUE = register("swet_transformers/blue");
    public static final TagKey<EntityType<?>> SWET_TRANSFORMERS_GOLDEN = register("swet_transformers/golden");
    public static final TagKey<EntityType<?>> SWET_TRANSFORMERS_PURPLE = register("swet_transformers/purple");
    public static final TagKey<EntityType<?>> SWET_TRANSFORMERS_VERMILION = register("swet_transformers/vermilion");

    public static final TagKey<EntityType<?>> VENOM_IMMUNITY = register("condition_immunities/venom");
    public static final TagKey<EntityType<?>> ABSTENTINE_TOXICITY_IMMUNITY = register("condition_immunities/abstentine_toxicity");
    public static final TagKey<EntityType<?>> BLOODTINGE_IMMUNITY = register("condition_immunities/bloodtinge");
    public static final TagKey<EntityType<?>> ENTRANCEMENT_IMMUNITY = register("condition_immunities/entrancement");
    public static final TagKey<EntityType<?>> FROSTBITE_IMMUNITY = register("condition_immunities/frostbite");

    private static TagKey<EntityType<?>> register(String id) {
        return TagKey.of(Registry.ENTITY_TYPE_KEY, ParadiseLost.locate(id));
    }
}
