package net.id.paradiselost.world.feature;

import net.id.paradiselost.ParadiseLost;
import net.id.paradiselost.world.feature.configs.*;
import net.id.paradiselost.world.feature.configured_features.ParadiseLostConfiguredFeatures;
import net.id.paradiselost.world.feature.features.*;
import net.id.paradiselost.world.feature.placed_features.ParadiseLostPlacedFeatures;
import net.id.paradiselost.world.feature.structure.ParadiseLostStructureFeatures;
import net.id.paradiselost.world.feature.tree.ParadiseLostTreeHell;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.DeltaFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;

public class ParadiseLostFeatures {
    public static final ParadiseLostLakeFeature LAKE = register("lake", new ParadiseLostLakeFeature(DynamicConfiguration.CODEC));

    public static final ParadiseLostDeltaFeature DELTA_FEATURE = register("delta_feature", new ParadiseLostDeltaFeature(DeltaFeatureConfig.CODEC));
    public static final GroundcoverFeature GROUNDCOVER_FEATURE = register("groundcover_feature", new GroundcoverFeature(GroundcoverFeatureConfig.CODEC));
    public static final ParadiseLostBoulderFeature BOULDER = register("boulder", new ParadiseLostBoulderFeature(BoulderFeatureConfig.CODEC));
    public static final VitrouliteSpireFeature VITROULITE_SPIRE_FEATURE = register("vitroulite_spire", new VitrouliteSpireFeature(DefaultFeatureConfig.CODEC));

    public static final HoneyNettleFeature HONEY_NETTLE_FEATURE = register("honey_nettle", new HoneyNettleFeature(DefaultFeatureConfig.CODEC));

    public static final PillarFeature PILLAR_FEATURE = register("pillar_feature", new PillarFeature(LongFeatureConfig.CODEC));
    public static final FallenPillarFeature FALLEN_PILLAR_FEATURE = register("fallen_pillar_feature", new FallenPillarFeature(LongFeatureConfig.CODEC));

    public static final JaggedOreFeature JAGGED_ORE = register("jagged_ore", new JaggedOreFeature(JaggedOreConfig.CODEC));
    public static final SurtrumMeteoriteFeature SURTRUM_METEORITE_FEATURE = register("surtrum_meteorite", new SurtrumMeteoriteFeature(DefaultFeatureConfig.CODEC));

    private static <C extends FeatureConfig, F extends Feature<C>> F register(String id, F feature) {
        return Registry.register(Registries.FEATURE, ParadiseLost.locate(id), feature);
    }

    public static void init() {
        ParadiseLostTreeHell.init();
        ParadiseLostStructureFeatures.init();
        ParadiseLostConfiguredFeatures.init();
        ParadiseLostPlacedFeatures.init();
    }
}
