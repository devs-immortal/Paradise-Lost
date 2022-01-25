package net.id.aether.world.feature;

import net.id.aether.Aether;
import net.id.aether.world.feature.configs.AercloudConfig;
import net.id.aether.world.feature.configs.DynamicConfiguration;
import net.id.aether.world.feature.configs.QuicksoilConfig;
import net.id.aether.world.feature.configured_features.AetherConfiguredFeatures;
import net.id.aether.world.feature.decorators.AetherDecorators;
import net.id.aether.world.feature.features.AercloudFeature;
import net.id.aether.world.feature.features.AetherLakeFeature;
import net.id.aether.world.feature.features.CrystalTreeIslandFeature;
import net.id.aether.world.feature.features.QuicksoilFeature;
import net.id.aether.world.feature.placed_features.AetherPlacedFeatures;
import net.id.aether.world.feature.structure.AetherStructureFeatures;
import net.id.aether.world.feature.tree.AetherTreeHell;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;

public class AetherFeatures {

    public static final Feature<DynamicConfiguration> AETHER_LAKE = register("lake", new AetherLakeFeature());
    public static final Feature<QuicksoilConfig> QUICKSOIL = register("quicksoil", new QuicksoilFeature());
    public static final Feature<AercloudConfig> AERCLOUD = register("aercloud", new AercloudFeature());
    public static final Feature<DefaultFeatureConfig> CRYSTAL_TREE_ISLAND = register("crystal_tree_island", new CrystalTreeIslandFeature(DefaultFeatureConfig.CODEC));

    private static <C extends FeatureConfig, F extends Feature<C>> F register(String id, F feature) {
        return Registry.register(Registry.FEATURE, Aether.locate(id), feature);
    }

    public static void init() {
        AetherTreeHell.init();
        AetherStructureFeatures.init();
        AetherConfiguredFeatures.init();
        AetherDecorators.init();
        AetherPlacedFeatures.init();
    }
}