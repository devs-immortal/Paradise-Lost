package net.id.paradise_lost.data;

import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;

/**
 * Applies {@link ParadiseLostDataEntries} to Fabric runtime registries.
 * NeoForge uses the same entries via DataMapProvider datagen instead.
 */
public final class FabricDataEntries {
    private FabricDataEntries() {
    }

    public static void apply() {
        ParadiseLostDataEntries.fuels().forEach(FuelRegistry.INSTANCE::add);
        ParadiseLostDataEntries.compostables().forEach(CompostingChanceRegistry.INSTANCE::add);
        ParadiseLostDataEntries.strippables().forEach(StrippableBlockRegistry::register);
    }
}
