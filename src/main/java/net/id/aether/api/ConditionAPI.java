package net.id.aether.api;

import net.id.aether.Aether;
import net.id.aether.api.condition.ConditionModifier;
import net.id.aether.api.condition.ConditionProcessor;
import net.id.aether.api.condition.VenomCondition;
import net.id.aether.registry.AetherRegistries;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class ConditionAPI {

    public static final Identifier VENOM_ID = Aether.locate("venom_processor");
    public static final ConditionProcessor VENOM = register(VENOM_ID, new VenomCondition(VENOM_ID));

    private static ConditionProcessor register(Identifier id, ConditionProcessor processor) {
        return Registry.register(AetherRegistries.CONDITION_REGISTRY, id, processor);
    }

    public static void init() {}

    public static List<Identifier> getValidProcessors(EntityType<?> type) {
        //noinspection ConstantConditions
        return AetherRegistries.CONDITION_REGISTRY.getIds()
                .stream()
                .filter(id -> AetherRegistries.CONDITION_REGISTRY.get(id).exempt.contains(type))
                .collect(Collectors.toList());
    }

    public static ConditionProcessor getOrThrow(Identifier id) {
        return AetherRegistries.CONDITION_REGISTRY.getOrEmpty(id).orElseThrow((() -> new NoSuchElementException("No ConditionManager found registered for entry: " + id.toString())));
    }
}
