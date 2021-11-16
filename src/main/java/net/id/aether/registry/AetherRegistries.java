package net.id.aether.registry;

import com.mojang.serialization.Lifecycle;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.id.aether.Aether;
import net.id.aether.effect.condition.Condition;
import net.id.aether.lore.LoreEntry;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;

import static net.id.aether.Aether.locate;

@SuppressWarnings("unchecked")
public class AetherRegistries {

    public static void init() {}

    public static final RegistryKey<Registry<Condition>> CONDITION_REGISTRY_KEY = RegistryKey.ofRegistry(locate("condition"));
    public static final Registry<Condition> CONDITION_REGISTRY = (Registry<Condition>) ((MutableRegistry) Registry.REGISTRIES).add(CONDITION_REGISTRY_KEY, new SimpleRegistry<>(CONDITION_REGISTRY_KEY, Lifecycle.experimental()), Lifecycle.experimental());
    
    /**
     * The registry for {@link LoreEntry}, try not to use this directly. Use the methods in {@link net.id.aether.lore.AetherLore}.
     */
    public static final Registry<LoreEntry<?>> LORE_REGISTRY = (Registry<LoreEntry<?>>)(Object)FabricRegistryBuilder.createDefaulted(LoreEntry.class, locate("lore"), locate("root")).attribute(RegistryAttribute.SYNCED).buildAndRegister();
}
