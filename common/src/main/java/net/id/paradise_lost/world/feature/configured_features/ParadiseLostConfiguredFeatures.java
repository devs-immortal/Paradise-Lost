package net.id.paradise_lost.world.feature.configured_features;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import static net.id.paradise_lost.ModConstants.id;

@SuppressWarnings("unused")
public class ParadiseLostConfiguredFeatures {

    public static ResourceKey<ConfiguredFeature<?, ?>> of(String id) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, id(id));
    }

    public static void init() {
        ParadiseLostTreeConfiguredFeatures.init();
        ParadiseLostVegetationConfiguredFeatures.init();
        ParadiseLostMiscConfiguredFeatures.init();
    }

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        ParadiseLostMiscConfiguredFeatures.bootstrap(context);
        ParadiseLostVegetationConfiguredFeatures.bootstrap(context);
        ParadiseLostTreeConfiguredFeatures.bootstrap(context);
    }

    protected static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(
            BootstrapContext<ConfiguredFeature<?, ?>> context,
            ResourceKey<ConfiguredFeature<?, ?>> key,
            F feature,
            FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
