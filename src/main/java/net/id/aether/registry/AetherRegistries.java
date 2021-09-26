package net.id.aether.registry;

import com.mojang.serialization.Lifecycle;
import net.id.aether.Aether;
import net.id.aether.effect.condition.Condition;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;

public class AetherRegistries {

    public static void init() {}

    public static final RegistryKey<Registry<Condition>> CONDITION_REGISTRY_KEY = RegistryKey.ofRegistry(Aether.locate("condition"));
    public static final Registry<Condition> CONDITION_REGISTRY = (Registry<Condition>) ((MutableRegistry) Registry.REGISTRIES).add(CONDITION_REGISTRY_KEY, new SimpleRegistry<>(CONDITION_REGISTRY_KEY, Lifecycle.experimental()), Lifecycle.experimental());


}
