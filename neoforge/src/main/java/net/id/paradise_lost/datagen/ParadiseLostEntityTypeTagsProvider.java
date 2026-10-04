package net.id.paradise_lost.datagen;

import net.id.paradise_lost.registry.EntityRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;

import java.util.concurrent.CompletableFuture;

public class ParadiseLostEntityTypeTagsProvider extends EntityTypeTagsProvider {
    public ParadiseLostEntityTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(EntityTypeTags.ARROWS).add(EntityRegistry.LEVITA_ARROW.get());
    }
}
