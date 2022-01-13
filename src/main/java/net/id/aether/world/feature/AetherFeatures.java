package net.id.aether.world.feature;

import net.fabricmc.fabric.mixin.structure.StructureFeatureAccessor;
import net.id.aether.Aether;
import net.id.aether.world.feature.config.AercloudConfig;
import net.id.aether.world.feature.config.DynamicConfiguration;
import net.id.aether.world.feature.config.QuicksoilConfig;
import net.id.aether.world.feature.features.AercloudFeature;
import net.id.aether.world.feature.features.AetherLakeFeature;
import net.id.aether.world.feature.features.CrystalTreeIslandFeature;
import net.id.aether.world.feature.features.QuicksoilFeature;
import net.id.aether.world.feature.generator.OrangeRuinGenerator;
import net.id.aether.world.feature.generator.SkyrootTowerGenerator;
import net.id.aether.world.feature.generator.WellGenerator;
import net.id.aether.world.feature.structure.OrangeRuinFeature;
import net.id.aether.world.feature.structure.SkyrootTowerFeature;
import net.id.aether.world.feature.structure.WellFeature;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public class AetherFeatures {
    public static final StructurePieceType WELL_PIECE = register(WellGenerator.Piece::new, "well");
    public static final StructurePieceType SKYROOT_TOWER_PIECE = register(SkyrootTowerGenerator.Piece::new, "skyroot_tower");
    public static final StructurePieceType ORANGE_RUIN_PIECE = register(OrangeRuinGenerator.Piece::new, "orange_ruin");

    public static final Feature<DynamicConfiguration> AETHER_LAKE = register("lake", new AetherLakeFeature());
    public static final Feature<QuicksoilConfig> QUICKSOIL = register("quicksoil", new QuicksoilFeature());
    public static final Feature<AercloudConfig> AERCLOUD = register("aercloud", new AercloudFeature());
    public static final Feature<DefaultFeatureConfig> CRYSTAL_TREE_ISLAND = register("crystal_tree_island", new CrystalTreeIslandFeature(DefaultFeatureConfig.CODEC));

    public static void init() {
        register("well", new WellFeature(DefaultFeatureConfig.CODEC), GenerationStep.Feature.SURFACE_STRUCTURES);
        register("skyroot_tower", new SkyrootTowerFeature(DefaultFeatureConfig.CODEC), GenerationStep.Feature.SURFACE_STRUCTURES);
        register("orange_ruin", new OrangeRuinFeature(DefaultFeatureConfig.CODEC), GenerationStep.Feature.SURFACE_STRUCTURES);
    }

    private static <T extends FeatureConfig> void register(String id, StructureFeature<T> structure, GenerationStep.Feature genStep) {
        StructureFeatureAccessor.callRegister(Aether.locate(id).toString(), structure, genStep);
    }

    static StructurePieceType register(StructurePieceType pieceType, String id) {
        return Registry.register(Registry.STRUCTURE_PIECE, Aether.locate(id), pieceType);
    }


    @SuppressWarnings("UnusedReturnValue")
    private static <C extends FeatureConfig, F extends Feature<C>> F register(String id, F feature) {
        return Registry.register(Registry.FEATURE, Aether.locate(id), feature);
    }
}