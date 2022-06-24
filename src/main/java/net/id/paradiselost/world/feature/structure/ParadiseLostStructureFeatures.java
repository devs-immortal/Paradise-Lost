package net.id.paradiselost.world.feature.structure;

import net.id.paradiselost.world.feature.structure.generator.OrangeRuinGenerator;
import net.id.paradiselost.world.feature.structure.generator.SkyrootTowerGenerator;
import net.id.paradiselost.world.feature.structure.generator.WellGenerator;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

import static net.id.paradiselost.ParadiseLost.locate;

public class ParadiseLostStructureFeatures {
    public static final TagKey<Structure> WELL_KEY = tagKey("well");
    public static final StructureType<WellFeature> WELL = ()->WellFeature.CODEC;
    public static final StructurePieceType WELL_PIECE = WellGenerator.Piece::new;
    
    public static final TagKey<Structure> SKYROOT_TOWER_KEY = tagKey("skyroot_tower");
    public static final StructureType<SkyrootTowerFeature> SKYROOT_TOWER = ()->SkyrootTowerFeature.CODEC;
    public static final StructurePieceType SKYROOT_TOWER_PIECE = SkyrootTowerGenerator.Piece::new;
    
    public static final TagKey<Structure> ORANGE_RUIN_KEY = tagKey("orange_ruin");
    public static final StructureType<OrangeRuinFeature> ORANGE_RUIN = ()->OrangeRuinFeature.CODEC;
    public static final StructurePieceType ORANGE_RUIN_PIECE = OrangeRuinGenerator.Piece::new;
    
    private static TagKey<Structure> tagKey(String name) {
        return TagKey.of(Registry.STRUCTURE_KEY, locate(name));
    }
    
    public static void init() {
        register(WELL_KEY, WELL, WELL_PIECE);
        register(SKYROOT_TOWER_KEY, SKYROOT_TOWER, SKYROOT_TOWER_PIECE);
        register(ORANGE_RUIN_KEY, ORANGE_RUIN, ORANGE_RUIN_PIECE);
    }
    
    private static <T extends Structure> void register(TagKey<? extends T> name, StructureType<? extends T> type, StructurePieceType pieceType) {
        var id = name.id();
        Registry.register(Registry.STRUCTURE_TYPE, id, type);
        Registry.register(Registry.STRUCTURE_PIECE, id, pieceType);
    }
}
