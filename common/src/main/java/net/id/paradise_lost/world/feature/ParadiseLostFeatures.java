package net.id.paradise_lost.world.feature;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.registration.RegistrationProvider;
import net.id.paradise_lost.world.feature.configs.BoulderFeatureConfig;
import net.id.paradise_lost.world.feature.configs.RiverConfiguration;
import net.id.paradise_lost.world.feature.configs.FrozenAwareFeatureConfig;
import net.id.paradise_lost.world.feature.configs.JaggedOreConfig;
import net.id.paradise_lost.world.feature.configs.LongFeatureConfig;
import net.id.paradise_lost.world.feature.configured_features.ParadiseLostConfiguredFeatures;
import net.id.paradise_lost.world.feature.features.FallenPillarFeature;
import net.id.paradise_lost.world.feature.features.HoneyNettleFeature;
import net.id.paradise_lost.world.feature.features.HugeBrownSporecapFeature;
import net.id.paradise_lost.world.feature.features.JaggedOreFeature;
import net.id.paradise_lost.world.feature.features.ParadiseLostBoulderFeature;
import net.id.paradise_lost.world.feature.features.ParadiseLostDeltaFeature;
import net.id.paradise_lost.world.feature.features.ParadiseLostLakeFeature;
import net.id.paradise_lost.world.feature.features.PillarFeature;
import net.id.paradise_lost.world.feature.features.RiverChannelFeature;
import net.id.paradise_lost.world.feature.features.SurtrumMeteoriteFeature;
import net.id.paradise_lost.world.feature.features.VitrouliteSpireFeature;
import net.id.paradise_lost.world.feature.features.VoidSpillFeature;
import net.id.paradise_lost.world.feature.placement.ParadiseLostPlacementModifiers;
import net.id.paradise_lost.world.feature.placed_features.ParadiseLostPlacedFeatures;
import net.id.paradise_lost.world.feature.structure.ParadiseLostStructureFeatures;
import net.id.paradise_lost.world.feature.tree.ParadiseLostTreeHell;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.DeltaFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class ParadiseLostFeatures {
    private static final RegistrationProvider<Feature<?>> FEATURES =
            RegistrationProvider.get(Registries.FEATURE, ModConstants.MODID);

    public static final ParadiseLostLakeFeature LAKE = register("lake", new ParadiseLostLakeFeature(BlockStateConfiguration.CODEC));
    public static final RiverChannelFeature RIVER_CHANNEL = register("river_channel", new RiverChannelFeature(RiverConfiguration.CODEC));
    public static final VoidSpillFeature VOID_SPILL = register("void_spill", new VoidSpillFeature(FrozenAwareFeatureConfig.CODEC));

    public static final ParadiseLostDeltaFeature DELTA_FEATURE = register("delta_feature", new ParadiseLostDeltaFeature(DeltaFeatureConfiguration.CODEC));
    public static final ParadiseLostBoulderFeature BOULDER = register("boulder", new ParadiseLostBoulderFeature(BoulderFeatureConfig.CODEC));
    public static final VitrouliteSpireFeature VITROULITE_SPIRE_FEATURE = register("vitroulite_spire", new VitrouliteSpireFeature(NoneFeatureConfiguration.CODEC));

    public static final HoneyNettleFeature HONEY_NETTLE_FEATURE = register("honey_nettle", new HoneyNettleFeature(NoneFeatureConfiguration.CODEC));

    public static final PillarFeature PILLAR_FEATURE = register("pillar_feature", new PillarFeature(LongFeatureConfig.CODEC));
    public static final FallenPillarFeature FALLEN_PILLAR_FEATURE = register("fallen_pillar_feature", new FallenPillarFeature(LongFeatureConfig.CODEC));

    public static final JaggedOreFeature JAGGED_ORE = register("jagged_ore", new JaggedOreFeature(JaggedOreConfig.CODEC));
    public static final SurtrumMeteoriteFeature SURTRUM_METEORITE_FEATURE = register("surtrum_meteorite", new SurtrumMeteoriteFeature(NoneFeatureConfiguration.CODEC));

    public static final HugeBrownSporecapFeature BROWN_SPORECAP_FEATURE = register("huge_brown_sporecap", new HugeBrownSporecapFeature(HugeMushroomFeatureConfiguration.CODEC));

    private static <C extends FeatureConfiguration, F extends Feature<C>> F register(String id, F feature) {
        FEATURES.register(id, () -> feature);
        return feature;
    }

    public static void init() {
        ParadiseLostPlacementModifiers.init();
        ParadiseLostTreeHell.init();
        ParadiseLostStructureFeatures.init();
        ParadiseLostConfiguredFeatures.init();
        ParadiseLostPlacedFeatures.init();
    }
}
