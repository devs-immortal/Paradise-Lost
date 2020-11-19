package com.aether.world.feature;

import com.aether.Aether;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;

public class AetherFeatures {
    public static ConfiguredFeature<?, ?> GOLDEN_OAK, SKYROOT;
    public static ConfiguredFeature<?, ?> COLD_AERCLOUD, BLUE_AERCLOUD, GOLDEN_AERCLOUD;
    public static ConfiguredFeature<?, ?> AETHER_GRASS, AETHER_TALL_GRASS;

    public static void registerFeatures() {
        AETHER_GRASS = register("aether_grass", Feature.RANDOM_PATCH.configure(AetherConfiguredFeatures.AETHER_GRASS_CONFIG));
        AETHER_GRASS = register("aether_tall_grass", Feature.RANDOM_PATCH.configure(AetherConfiguredFeatures.AETHER_TALL_GRASS_CONFIG));
        SKYROOT = register("skyroot_tree", Feature.TREE.configure(AetherConfiguredFeatures.SKYROOT_CONFIG));
        GOLDEN_OAK = register("golden_oak_tree", Feature.TREE.configure(AetherConfiguredFeatures.GOLDEN_OAK_CONFIG));
        COLD_AERCLOUD = register("cold_aercloud", new AercloudFeature().configure(AetherConfiguredFeatures.COLD_AERCLOUD_CONFIG));
        BLUE_AERCLOUD = register("blue_aercloud", new AercloudFeature().configure(AetherConfiguredFeatures.BLUE_AERCLOUD_CONFIG));
        GOLDEN_AERCLOUD = register("golden_aercloud", new AercloudFeature().configure(AetherConfiguredFeatures.GOLDEN_AERCLOUD_CONFIG));
    }

    private static <FC extends FeatureConfig> ConfiguredFeature<FC, ?> register(String id, ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier(Aether.MOD_ID, id), configuredFeature);
    }

    private static <C extends FeatureConfig, F extends Feature<C>> F register(String id, F feature) {
        return Registry.register(Registry.FEATURE, new Identifier(Aether.MOD_ID, id), feature);
    }
}
