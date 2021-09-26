package net.id.aether.effect.condition;

import net.id.aether.Aether;
import net.id.aether.registry.AetherRegistries;
import net.minecraft.util.registry.Registry;

public class Conditions {
    public static final ConditionProcessor VENOM = register("venom", new VenomCondition());

    private static ConditionProcessor register(String id, ConditionProcessor processor) {
        return Registry.register(AetherRegistries.CONDITION_REGISTRY, Aether.locate(id), processor);
    }

    public static void init() {}

}
