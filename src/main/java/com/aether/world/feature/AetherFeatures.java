package com.aether.world.feature;

import com.aether.Aether;
import com.aether.world.gen.decorator.CrystalTreeIslandDecorator;
import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;

public class AetherFeatures {
    // TODO: Stubbed. Pending 1.17 rewrite.
//    public static final StructurePieceType WELL_PIECE = register(WellGenerator.WellPiece::new, "well");
//    public static final StructurePieceType SKYROOT_TOWER_PIECE = register(SkyrootTowerGenerator.TowerPiece::new, "skyroot_tower");

    public static void registerFeatures() {
        register("lake", new AetherLakeFeature());
        register("aercloud", new AercloudFeature());
        register("quicksoil", new QuicksoilFeature());
        register("crystal_tree_island", new CrystalTreeIslandFeature(NoneFeatureConfiguration.CODEC));

        // Decorators
        register("crystal_tree_island", new CrystalTreeIslandDecorator(NoneDecoratorConfiguration.CODEC));

        //register("well", new WellFeature(DefaultFeatureConfig.CODEC));
        //register("skyroot_tower", new SkyrootTowerFeature(DefaultFeatureConfig.CODEC));
    }

    private static <T extends FeatureConfiguration> void register(String id, StructureFeature<T> structure) {
        FabricStructureBuilder.create(Aether.locate(id), structure)
                .step(GenerationStep.Decoration.SURFACE_STRUCTURES)
                .defaultConfig(10, 1, 0)
                .register();
    }

    static StructurePieceType register(StructurePieceType pieceType, String id) {
        return Registry.register(Registry.STRUCTURE_PIECE, Aether.locate(id), pieceType);
    }


    @SuppressWarnings("UnusedReturnValue")
    private static <C extends FeatureConfiguration, F extends Feature<C>> F register(String id, F feature) {

        return Registry.register(Registry.FEATURE, Aether.locate(id), feature);
    }

    private static <T extends DecoratorConfiguration, G extends FeatureDecorator<T>> G register(String id, G decorator) {
        return Registry.register(Registry.DECORATOR, Aether.locate(id), decorator);
    }
}