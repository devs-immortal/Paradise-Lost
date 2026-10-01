package net.id.paradise_lost.datagen;

import net.id.paradise_lost.services.NeoForgeRegistrationHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import java.util.concurrent.CompletableFuture;

public class ParadiseLostFuelsProvider extends DataMapProvider {
    public ParadiseLostFuelsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        var builder = builder(NeoForgeDataMaps.FURNACE_FUELS);
        NeoForgeRegistrationHelper.getFuels().forEach((item, burnTime) ->
                builder.add(item.builtInRegistryHolder(), new FurnaceFuel(burnTime), false));
    }

    @Override
    public String getName() {
        return "Paradise Lost Fuels";
    }
}
