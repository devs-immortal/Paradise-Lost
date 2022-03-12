package net.id.aether.world.feature.structure;

import net.id.aether.Aether;
import net.id.aether.mixin.structure.StructureFeatureAccessor;
import net.id.aether.world.feature.structure.generator.OrangeRuinGenerator;
import net.id.aether.world.feature.structure.generator.SkyrootTowerGenerator;
import net.id.aether.world.feature.structure.generator.WellGenerator;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public class AetherStructureFeatures {
    public static final StructurePieceType WELL_PIECE = register(WellGenerator.Piece::new, "well");
    public static final StructurePieceType SKYROOT_TOWER_PIECE = register(SkyrootTowerGenerator.Piece::new, "skyroot_tower");
    public static final StructurePieceType ORANGE_RUIN_PIECE = register(OrangeRuinGenerator.Piece::new, "orange_ruin");

    public static void init() {
        register("well", new WellFeature(DefaultFeatureConfig.CODEC), GenerationStep.Feature.SURFACE_STRUCTURES);
        register("skyroot_tower", new SkyrootTowerFeature(DefaultFeatureConfig.CODEC), GenerationStep.Feature.SURFACE_STRUCTURES);
        register("orange_ruin", new OrangeRuinFeature(DefaultFeatureConfig.CODEC), GenerationStep.Feature.SURFACE_STRUCTURES);
    }

    private static <T extends FeatureConfig> void register(String id, StructureFeature<T> structure, GenerationStep.Feature genStep) {
        StructureFeatureAccessor.callRegister(Aether.locate(id).toString(), structure, genStep);
    }

    private static StructurePieceType register(StructurePieceType pieceType, String id) {
        return Registry.register(Registry.STRUCTURE_PIECE, Aether.locate(id), pieceType);
    }
}
