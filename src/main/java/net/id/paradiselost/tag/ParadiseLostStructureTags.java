package net.id.paradiselost.tag;

import net.id.paradiselost.ParadiseLost;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public class ParadiseLostStructureTags {

    public static final TagKey<Biome> ORANGE_RUIN_HAS_STRUCTURE = register("has_structure/orange_ruin");
    public static final TagKey<Biome> SKYROOT_TOWER_HAS_STRUCTURE = register("has_structure/skyroot_tower");
    public static final TagKey<Biome> WELL_HAS_STRUCTURE = register("has_structure/well");

    private static TagKey<Biome> register(String id) {
        return TagKey.of(Registry.BIOME_KEY, ParadiseLost.locate(id));
    }
}
