package com.aether.world.feature;

import com.aether.Aether;

import com.aether.world.feature.config.AercloudConfig;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.decorator.ChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.*;

public class AetherFeatures {
    public static Feature<AercloudConfig> DEFAULT_AERCLOUD;
    public static Feature<SingleStateFeatureConfig> DEFAULT_LAKE;
    public static Feature<DefaultFeatureConfig> DEFAULT_QUICKSOIL;

    public static void registerFeatures() {
        DEFAULT_LAKE = register("default_lake", new AetherLakeFeature(SingleStateFeatureConfig.CODEC));
        DEFAULT_AERCLOUD = register("default_aercloud", new AercloudFeature());
        DEFAULT_QUICKSOIL = register("default_quicksoil", new QuicksoilFeature());
    }

    private static <C extends FeatureConfig, F extends Feature<C>> F register(String id, F feature) {
        return Registry.register(Registry.FEATURE, new Identifier(Aether.MOD_ID, id), feature);
    }
}
