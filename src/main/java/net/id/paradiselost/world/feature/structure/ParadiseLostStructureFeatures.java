package net.id.paradiselost.world.feature.structure;

import net.id.paradiselost.world.feature.structure.generator.AurelTowerGenerator;
import net.id.paradiselost.world.feature.structure.generator.WellGenerator;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

import static net.id.paradiselost.ParadiseLost.locate;

public class ParadiseLostStructureFeatures {
    public static final TagKey<Structure> WELL_KEY = tagKey("well");
    public static final StructureType<WellFeature> WELL = () -> WellFeature.CODEC;
    public static final StructurePieceType WELL_PIECE = WellGenerator.Piece::new;
    
    public static final TagKey<Structure> AUREL_TOWER_KEY = tagKey("aurel_tower");
    public static final StructureType<AurelTowerFeature> AUREL_TOWER = () -> AurelTowerFeature.CODEC;
    public static final StructurePieceType AUREL_TOWER_PIECE = AurelTowerGenerator.Piece::new;
    
    private static TagKey<Structure> tagKey(String name) {
        return TagKey.of(Registry.STRUCTURE_KEY, locate(name));
    }
    
    public static void init() {
        register(WELL_KEY, WELL, WELL_PIECE);
        register(AUREL_TOWER_KEY, AUREL_TOWER, AUREL_TOWER_PIECE);
    }
    
    private static <T extends Structure> void register(TagKey<? extends T> name, StructureType<? extends T> type, StructurePieceType pieceType) {
        var id = name.id();
        Registry.register(Registry.STRUCTURE_TYPE, id, type);
        Registry.register(Registry.STRUCTURE_PIECE, id, pieceType);
    }
}
