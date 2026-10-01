package net.id.paradise_lost.datagen;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.registry.MoaBreedingRegistry;
import net.id.paradise_lost.registry.MoaRaceRegistry;
import net.id.paradise_lost.registry.MoaSpawnRegistry;
import net.id.paradise_lost.registry.MoaSpawnStatWeightingRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class ParadiseLostMoaDatapackProvider extends DatapackBuiltinEntriesProvider {
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder();

    public ParadiseLostMoaDatapackProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, BUILDER, Set.of(ModConstants.MODID));
        MoaSpawnStatWeightingRegistry.REGISTRY.addToSet(BUILDER);
        MoaRaceRegistry.REGISTRY.addToSet(BUILDER);
        MoaSpawnRegistry.REGISTRY.addToSet(BUILDER);
        MoaBreedingRegistry.REGISTRY.addToSet(BUILDER);
    }

    @Override
    public String getName() {
        return "Paradise Lost Moa Registries";
    }
}
