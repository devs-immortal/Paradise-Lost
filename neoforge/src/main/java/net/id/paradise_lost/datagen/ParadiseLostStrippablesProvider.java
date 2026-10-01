package net.id.paradise_lost.datagen;

import net.id.paradise_lost.services.NeoForgeRegistrationHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.neoforged.neoforge.registries.datamaps.builtin.Strippable;

import java.util.concurrent.CompletableFuture;

public class ParadiseLostStrippablesProvider extends DataMapProvider {
    public ParadiseLostStrippablesProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        var builder = builder(NeoForgeDataMaps.STRIPPABLES);
        NeoForgeRegistrationHelper.getStrippables().forEach((log, stripped) ->
                builder.add(log.builtInRegistryHolder(), new Strippable(stripped), false));
    }

    @Override
    public String getName() {
        return "Paradise Lost Strippables";
    }
}
