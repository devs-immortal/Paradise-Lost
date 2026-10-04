package net.id.paradise_lost.tag;

import net.id.paradise_lost.ModConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;

public class ParadiseLostStructureTags {

    public static final TagKey<Biome> AUREL_TOWER_HAS_STRUCTURE = biome("has_structure/aurel_tower");
    public static final TagKey<Biome> WELL_HAS_STRUCTURE = biome("has_structure/well");
    public static final TagKey<Biome> VAULT_HAS_STRUCTURE = biome("has_structure/vault");
    public static final TagKey<Biome> REMAINS_HAS_STRUCTURE = biome("has_structure/remains");
    public static final TagKey<Biome> PALACE_HAS_STRUCTURE = biome("has_structure/palace");
    public static final TagKey<Biome> BIRDCAGE_HAS_STRUCTURE = biome("has_structure/birdcage");

    public static final TagKey<Structure> VAULT = structure("vault");

    private static TagKey<Biome> biome(String id) {
        return TagKey.create(Registries.BIOME, ModConstants.id(id));
    }

    private static TagKey<Structure> structure(String id) {
        return TagKey.create(Registries.STRUCTURE, ModConstants.id(id));
    }
}

