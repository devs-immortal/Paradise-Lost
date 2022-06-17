package net.id.aether.world.feature.structure;

import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

import static net.id.aether.Aether.locate;

public class AetherStructureFeatures {
    public static final TagKey<Structure> WELL_PIECE_KEY = tagKey("well");
    public static final StructureType<WellFeature> WELL_PIECE = ()->WellFeature.CODEC;
    
    public static final TagKey<Structure> SKYROOT_TOWER_PIECE_KEY = tagKey("skyroot_tower");
    public static final StructureType<SkyrootTowerFeature> SKYROOT_TOWER_PIECE = ()->SkyrootTowerFeature.CODEC;
    
    public static final TagKey<Structure> ORANGE_RUIN_KEY = tagKey("orange_ruin");
    public static final StructureType<OrangeRuinFeature> ORANGE_RUIN = ()->OrangeRuinFeature.CODEC;
    
    private static TagKey<Structure> tagKey(String name) {
        return TagKey.of(Registry.STRUCTURE_KEY, locate(name));
    }
    
    public static void init() {
        register("well", WELL_PIECE);
        register("skyroot_tower", SKYROOT_TOWER_PIECE);
        register("orange_ruin", ORANGE_RUIN);
    }
    
    private static void register(String name, StructureType<?> type) {
        Registry.register(Registry.STRUCTURE_TYPE, locate(name), type);
    }
}
