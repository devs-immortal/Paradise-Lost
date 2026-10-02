package net.id.paradise_lost.datagen;

import net.id.paradise_lost.data.ParadiseLostDataEntries;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import java.util.concurrent.CompletableFuture;

public class ParadiseLostCompostablesProvider extends DataMapProvider {
    public ParadiseLostCompostablesProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    public String getName() {
        return "Paradise Lost Compostables";
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        var builder = builder(NeoForgeDataMaps.COMPOSTABLES);
        ParadiseLostDataEntries.compostables().forEach((item, chance) ->
                builder.add(item.builtInRegistryHolder(), new Compostable(chance), false));
    }
}
