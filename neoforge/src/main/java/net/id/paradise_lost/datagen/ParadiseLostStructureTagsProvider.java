package net.id.paradise_lost.datagen;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.tag.ParadiseLostStructureTags;
import net.id.paradise_lost.world.feature.structure.ParadiseLostStructures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ParadiseLostStructureTagsProvider extends TagsProvider<Structure> {
    public ParadiseLostStructureTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, Registries.STRUCTURE, lookupProvider, ModConstants.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(ParadiseLostStructureTags.VAULT).addOptional(ParadiseLostStructures.VAULT.location());
    }
}
