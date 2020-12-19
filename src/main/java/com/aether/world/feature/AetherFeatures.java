package com.aether.world.feature;

import com.aether.Aether;
import com.aether.world.feature.config.AercloudConfig;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.SingleStateFeatureConfig;

public class AetherFeatures {
    public static Feature<AercloudConfig> AERCLOUD;
    public static Feature<SingleStateFeatureConfig> LAKE;
    public static Feature<DefaultFeatureConfig> QUICKSOIL;

    public static void registerFeatures() {
        register("lake", new AetherLakeFeature(SingleStateFeatureConfig.CODEC));
        register("aercloud", new AercloudFeature());
        register("quicksoil", new QuicksoilFeature());
    }

    @SuppressWarnings("UnusedReturnValue")
    private static <C extends FeatureConfig, F extends Feature<C>> F register(String id, F feature) {
        return Registry.register(Registry.FEATURE, Aether.locate(id), feature);
    }
}