package net.id.paradiselost.world.feature.configured_features;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.gen.feature.*;

import static net.id.paradiselost.ParadiseLost.locate;

/**
 * Folks extend this class to get the ooey-gooey goodness of shared Config stuff.
 */
@SuppressWarnings("unused")
public class ParadiseLostConfiguredFeatures {

    public static RegistryKey<ConfiguredFeature<?, ?>> of(String id) {
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, locate(id));
    }

    public static void init() {
        ParadiseLostTreeConfiguredFeatures.init();
        ParadiseLostVegetationConfiguredFeatures.init();
        ParadiseLostMiscConfiguredFeatures.init();
    }
}
