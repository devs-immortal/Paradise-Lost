package com.aether.world.feature;

import com.aether.Aether;
import com.aether.world.feature.config.AercloudConfig;
import com.aether.world.feature.generator.OutpostGenerator;
import com.aether.world.feature.generator.WellGenerator;
import com.aether.world.feature.structure.OutpostFeature;
import com.aether.world.feature.structure.WellFeature;
import com.aether.world.gen.decorator.CrystalTreeIslandDecorator;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.DecoratorConfig;
import net.minecraft.world.gen.decorator.NopeDecoratorConfig;
import net.minecraft.world.gen.feature.*;

public class AetherFeatures {
    public static Feature<AercloudConfig> AERCLOUD;
    public static Feature<SingleStateFeatureConfig> LAKE;
    public static Feature<DefaultFeatureConfig> QUICKSOIL;

    public static StructurePieceType WELL_PIECE;
    public static StructurePieceType OUTPOST_PIECE;

    public static void registerFeatures() {
        register("lake", new AetherLakeFeature(SingleStateFeatureConfig.CODEC));
        register("aercloud", new AercloudFeature());
        register("quicksoil", new QuicksoilFeature());
        register("crystal_tree_island", new CrystalTreeIslandFeature(DefaultFeatureConfig.CODEC));

        // Decorators
        registerDecorator("crystal_tree_island", new CrystalTreeIslandDecorator(NopeDecoratorConfig.CODEC));

        WELL_PIECE = registerStructure("well", WellGenerator.WellPiece::new, new WellFeature(DefaultFeatureConfig.CODEC), 213769);
        OUTPOST_PIECE = registerStructure("outpost", OutpostGenerator.OutpostPiece::new, new OutpostFeature(DefaultFeatureConfig.CODEC), 4208012);
    }

    private static StructurePieceType registerStructure(String id, StructurePieceType piece, StructureFeature<DefaultFeatureConfig> structure, int salt) {
        final ConfiguredStructureFeature<?, ?> CONFIGURED = structure.configure(DefaultFeatureConfig.DEFAULT);

        Registry.register(Registry.STRUCTURE_PIECE, Aether.locate(id + "_piece"), piece);
        FabricStructureBuilder.create(Aether.locate(id), structure)
                .step(GenerationStep.Feature.SURFACE_STRUCTURES)
                .defaultConfig(32, 8, salt)
                .adjustsSurface()
                .register();

        RegistryKey<ConfiguredStructureFeature<?, ?>> configured = RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_WORLDGEN,
                Aether.locate(id));

        BuiltinRegistries.add(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, configured.getValue(), CONFIGURED);
        BiomeModifications.addStructure(BiomeSelectors.all(), configured);

        return WELL_PIECE;
    }

    @SuppressWarnings("UnusedReturnValue")
    private static <C extends FeatureConfig, F extends Feature<C>> F register(String id, F feature) {

        return Registry.register(Registry.FEATURE, Aether.locate(id), feature);
    }

    private static <T extends DecoratorConfig, G extends Decorator<T>> G registerDecorator(String id, G decorator) {
        return Registry.register(Registry.DECORATOR, Aether.locate(id), decorator);
    }

}