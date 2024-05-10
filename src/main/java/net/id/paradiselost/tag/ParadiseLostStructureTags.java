package net.id.paradiselost.tag;

import net.id.paradiselost.ParadiseLost;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public class ParadiseLostStructureTags {

    public static final TagKey<Biome> AUREL_TOWER_HAS_STRUCTURE = register("has_structure/aurel_tower");
    public static final TagKey<Biome> WELL_HAS_STRUCTURE = register("has_structure/well");

    // dungeon
    public static final TagKey<Biome> VAULT_HAS_STRUCTURE = register("has_structure/vault");

    private static TagKey<Biome> register(String id) {
        return TagKey.of(Registry.BIOME_KEY, ParadiseLost.locate(id));
    }
}
