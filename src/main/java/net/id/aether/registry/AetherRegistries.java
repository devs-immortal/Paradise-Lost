package net.id.aether.registry;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.id.aether.lore.LoreEntry;
import net.minecraft.util.registry.Registry;

import static net.id.aether.Aether.locate;

@SuppressWarnings("unchecked")
public class AetherRegistries {

    public static void init() {
    }

    /**
     * The registry for {@link LoreEntry}, try not to use this directly. Use the methods in {@link net.id.aether.lore.AetherLore}.
     */
    public static final Registry<LoreEntry<?>> LORE_REGISTRY = (Registry<LoreEntry<?>>)(Object)FabricRegistryBuilder.createDefaulted(LoreEntry.class, locate("lore"), locate("root")).attribute(RegistryAttribute.SYNCED).buildAndRegister();
}
