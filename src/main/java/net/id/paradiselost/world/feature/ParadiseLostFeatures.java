package net.id.paradiselost.world.feature;

import net.id.paradiselost.ParadiseLost;
import net.id.paradiselost.world.feature.configs.*;
import net.id.paradiselost.world.feature.configured_features.ParadiseLostConfiguredFeatures;
import net.id.paradiselost.world.feature.features.*;
import net.id.paradiselost.world.feature.placed_features.ParadiseLostPlacedFeatures;
import net.id.paradiselost.world.feature.placement_modifiers.ParadiseLostPlacementModifiers;
import net.id.paradiselost.world.feature.structure.ParadiseLostStructureFeatures;
import net.id.paradiselost.world.feature.tree.ParadiseLostTreeHell;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.DeltaFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;

public class ParadiseLostFeatures {
    public static final ParadiseLostLakeFeature LAKE = register("lake", new ParadiseLostLakeFeature(DynamicConfiguration.CODEC));
    public static final QuicksoilFeature QUICKSOIL = register("quicksoil", new QuicksoilFeature(QuicksoilConfig.CODEC));
    public static final CrystalTreeIslandFeature CRYSTAL_TREE_ISLAND = register("crystal_tree_island", new CrystalTreeIslandFeature(DefaultFeatureConfig.CODEC));

    public static final ParadiseLostDeltaFeature DELTA_FEATURE = register("paradise_lost_delta_feature", new ParadiseLostDeltaFeature(DeltaFeatureConfig.CODEC));
    public static final GroundcoverFeature GROUNDCOVER_FEATURE = register("groundcover_feature", new GroundcoverFeature(GroundcoverFeatureConfig.CODEC));
    public static final ParadiseLostBoulderFeature BOULDER = register("boulder", new ParadiseLostBoulderFeature(BoulderFeatureConfig.CODEC));
    public static final IcestoneSpireFeature ICESTONE_SPIRE_FEATURE = register("icestone_spire_feature", new IcestoneSpireFeature(DefaultFeatureConfig.CODEC));
    public static final FreezeParadiseLostTopLayerFeature FREEZE_PARADISE_LOST_TOP_LAYER_FEATURE_FEATURE = register("freeze_paradise_lost_top_layer_feature", new FreezeParadiseLostTopLayerFeature(DefaultFeatureConfig.CODEC));

    public static final ProjectedOrganicCoverFeature ORGANIC_GROUNDCOVER_FEATURE = register("organic_groundcover_feature", new ProjectedOrganicCoverFeature(ProjectedOrganicCoverConfig.CODEC));

    public static final HoneyNettleFeature HONEY_NETTLE_FEATURE = register("honey_nettle_feature", new HoneyNettleFeature(DefaultFeatureConfig.CODEC));

    public static final PillarFeature PILLAR_FEATURE = register("pillar_feature", new PillarFeature(LongFeatureConfig.CODEC));
    public static final FallenPillarFeature FALLEN_PILLAR_FEATURE = register("fallen_pillar_feature", new FallenPillarFeature(LongFeatureConfig.CODEC));

    private static <C extends FeatureConfig, F extends Feature<C>> F register(String id, F feature) {
        return Registry.register(Registry.FEATURE, ParadiseLost.locate(id), feature);
    }

    public static void init() {
        ParadiseLostTreeHell.init();
        ParadiseLostStructureFeatures.init();
        ParadiseLostConfiguredFeatures.init();
        ParadiseLostPlacementModifiers.init();
        ParadiseLostPlacedFeatures.init();
    }
}
