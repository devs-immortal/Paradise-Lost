package net.id.aether.effect.condition;

import net.id.aether.Aether;
import net.id.aether.registry.AetherRegistries;
import net.minecraft.util.registry.Registry;

public class Conditions {
    public static final Condition VENOM = register("venom", new VenomCondition());

    private static Condition register(String id, Condition condition) {
        return Registry.register(AetherRegistries.CONDITION_REGISTRY, Aether.locate(id), condition);
    }

    public static void init() {}

}
