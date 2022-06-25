package net.id.paradiselost.registry;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.id.paradiselost.lore.LoreEntry;
import net.id.paradiselost.lore.ParadiseLostLore;
import net.minecraft.util.registry.Registry;

import static net.id.paradiselost.ParadiseLost.locate;

@SuppressWarnings("unchecked")
public class ParadiseLostRegistries {

    public static void init() {}

    /**
     * The registry for {@link LoreEntry}, try not to use this directly. Use the methods in {@link ParadiseLostLore}.
     */
    public static final Registry<LoreEntry<?>> LORE_REGISTRY = (Registry<LoreEntry<?>>)(Object)FabricRegistryBuilder.createDefaulted(LoreEntry.class, locate("lore"), locate("root")).attribute(RegistryAttribute.SYNCED).buildAndRegister();
}
