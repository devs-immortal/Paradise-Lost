package net.id.paradise_lost.world.feature.structure;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.registration.RegistrationProvider;
import net.id.paradise_lost.world.feature.structure.generator.AurelTowerGenerator;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

import static net.id.paradise_lost.ModConstants.id;

public class ParadiseLostStructureFeatures {
    private static final RegistrationProvider<StructureType<?>> STRUCTURE_TYPES =
            RegistrationProvider.get(Registries.STRUCTURE_TYPE, ModConstants.MODID);
    private static final RegistrationProvider<StructurePieceType> STRUCTURE_PIECES =
            RegistrationProvider.get(Registries.STRUCTURE_PIECE, ModConstants.MODID);

    public static final TagKey<Structure> AUREL_TOWER_KEY = tagKey("aurel_tower");
    public static final StructureType<AurelTowerFeature> AUREL_TOWER = () -> AurelTowerFeature.CODEC;
    public static final StructurePieceType AUREL_TOWER_PIECE = AurelTowerGenerator.Piece::new;

    private static TagKey<Structure> tagKey(String name) {
        return TagKey.create(Registries.STRUCTURE, id(name));
    }

    public static void init() {
        register(AUREL_TOWER_KEY, AUREL_TOWER, AUREL_TOWER_PIECE);
    }

    private static <T extends Structure> void register(TagKey<? extends T> name, StructureType<? extends T> type, StructurePieceType pieceType) {
        var path = name.location().getPath();
        STRUCTURE_TYPES.register(path, () -> type);
        STRUCTURE_PIECES.register(path, () -> pieceType);
    }
}
