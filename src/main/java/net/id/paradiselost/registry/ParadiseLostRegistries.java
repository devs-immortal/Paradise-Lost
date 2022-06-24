package net.id.paradiselost.registry;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.id.paradiselost.effect.condition.Condition;
import net.id.paradiselost.lore.LoreEntry;
import net.id.paradiselost.lore.ParadiseLostLore;
import net.minecraft.util.registry.Registry;

import static net.id.paradiselost.ParadiseLost.locate;

@SuppressWarnings("unchecked")
public class ParadiseLostRegistries {

    public static void init() {}

    // TODO VERIFY THIS IS A PROPER REPLACEMENT 1.18.2
    //public static final RegistryKey<Registry<Condition>> CONDITION_REGISTRY_KEY = RegistryKey.ofRegistry(locate("condition"));
    public static final Registry<Condition> CONDITION_REGISTRY =  FabricRegistryBuilder.createSimple(Condition.class, locate("condition")).buildAndRegister();
    //(Registry<Condition>) ((MutableRegistry) Registry.REGISTRIES).add(CONDITION_REGISTRY_KEY, new SimpleRegistry<>(CONDITION_REGISTRY_KEY, Lifecycle.experimental(), null), Lifecycle.experimental());
    
    /**
     * The registry for {@link LoreEntry}, try not to use this directly. Use the methods in {@link ParadiseLostLore}.
     */
    public static final Registry<LoreEntry<?>> LORE_REGISTRY = (Registry<LoreEntry<?>>)(Object)FabricRegistryBuilder.createDefaulted(LoreEntry.class, locate("lore"), locate("root")).attribute(RegistryAttribute.SYNCED).buildAndRegister();
}
