package net.id.aether.registry;

import com.mojang.serialization.Lifecycle;
import net.id.aether.Aether;
import net.id.aether.api.condition.ConditionProcessor;
import net.id.incubus_core.systems.Material;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;

public class AetherRegistries {

    public static void init() {}

    public static final RegistryKey<Registry<ConditionProcessor>> CONDITION_REGISTRY_KEY = RegistryKey.ofRegistry(Aether.locate("condition"));
    public static final Registry<ConditionProcessor> CONDITION_REGISTRY = (Registry<ConditionProcessor>) ((MutableRegistry) Registry.REGISTRIES).add(CONDITION_REGISTRY_KEY, new SimpleRegistry<>(CONDITION_REGISTRY_KEY, Lifecycle.experimental()), Lifecycle.experimental());


}
