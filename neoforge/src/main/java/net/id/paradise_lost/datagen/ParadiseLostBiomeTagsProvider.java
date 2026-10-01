package net.id.paradise_lost.datagen;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.tag.ParadiseLostStructureTags;
import net.id.paradise_lost.world.dimension.ParadiseLostBiomes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ParadiseLostBiomeTagsProvider extends BiomeTagsProvider {
    public ParadiseLostBiomeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, ModConstants.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ParadiseLostStructureTags.AUREL_TOWER_HAS_STRUCTURE,
                ParadiseLostBiomes.HIGHLANDS_PLAINS_KEY,
                ParadiseLostBiomes.HIGHLANDS_FOREST_KEY,
                ParadiseLostBiomes.HIGHLANDS_THICKET_KEY);

        tag(ParadiseLostStructureTags.BIRDCAGE_HAS_STRUCTURE,
                ParadiseLostBiomes.HIGHLANDS_PLAINS_KEY,
                ParadiseLostBiomes.HIGHLANDS_FOREST_KEY,
                ParadiseLostBiomes.WISTERIA_WOODS_KEY);

        tag(ParadiseLostStructureTags.PALACE_HAS_STRUCTURE,
                ParadiseLostBiomes.HIGHLANDS_PLAINS_KEY,
                ParadiseLostBiomes.HIGHLANDS_FOREST_KEY);

        tag(ParadiseLostStructureTags.REMAINS_HAS_STRUCTURE,
                ParadiseLostBiomes.HIGHLANDS_PLAINS_KEY,
                ParadiseLostBiomes.HIGHLANDS_FOREST_KEY,
                ParadiseLostBiomes.HIGHLANDS_THICKET_KEY,
                ParadiseLostBiomes.HIGHLANDS_GRAND_GLADE_KEY);

        tag(ParadiseLostStructureTags.VAULT_HAS_STRUCTURE,
                ParadiseLostBiomes.HIGHLANDS_PLAINS_KEY,
                ParadiseLostBiomes.HIGHLANDS_FOREST_KEY,
                ParadiseLostBiomes.HIGHLANDS_THICKET_KEY,
                ParadiseLostBiomes.HIGHLANDS_GRAND_GLADE_KEY,
                ParadiseLostBiomes.WISTERIA_WOODS_KEY,
                ParadiseLostBiomes.AUTUMNAL_TUNDRA_KEY,
                ParadiseLostBiomes.CONTINENTAL_PLATEAU_KEY,
                ParadiseLostBiomes.HIGHLANDS_SHIELD_KEY);

        // Kept for datapack/API use; no well structure is registered yet.
        this.tag(ParadiseLostStructureTags.WELL_HAS_STRUCTURE);
    }

    @SafeVarargs
    private void tag(TagKey<Biome> tag, net.minecraft.resources.ResourceKey<Biome>... biomes) {
        var appender = this.tag(tag);
        for (var biome : biomes) {
            appender.addOptional(biome.location());
        }
    }
}
