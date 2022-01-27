package net.id.aether.world.feature;

import net.id.aether.Aether;
import net.id.aether.world.feature.configs.*;
import net.id.aether.world.feature.configured_features.AetherConfiguredFeatures;
import net.id.aether.world.feature.placement_modifiers.AetherPlacementModifiers;
import net.id.aether.world.feature.features.*;
import net.id.aether.world.feature.placed_features.AetherPlacedFeatures;
import net.id.aether.world.feature.structure.AetherStructureFeatures;
import net.id.aether.world.feature.tree.AetherTreeHell;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.DeltaFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;

public class AetherFeatures {

    public static final Feature<DynamicConfiguration> AETHER_LAKE = register("lake", new AetherLakeFeature());
    public static final Feature<QuicksoilConfig> QUICKSOIL = register("quicksoil", new QuicksoilFeature());
    public static final Feature<AercloudConfig> AERCLOUD = register("aercloud", new AercloudFeature());
    public static final Feature<DefaultFeatureConfig> CRYSTAL_TREE_ISLAND = register("crystal_tree_island", new CrystalTreeIslandFeature(DefaultFeatureConfig.CODEC));

    public static final Feature<DeltaFeatureConfig> AETHER_DELTA_FEATURE = register("aether_delta_feature", new AetherDeltaFeature(DeltaFeatureConfig.CODEC));
    public static final Feature<GroundcoverFeatureConfig> GROUNDCOVER_FEATURE = register("groundcover_feature", new GroundcoverFeature(GroundcoverFeature.CODEC));
    public static final Feature<BoulderFeatureConfig> BOULDER = register("boulder", new AetherBoulderFeature(BoulderFeatureConfig.CODEC));
    public static final Feature<DefaultFeatureConfig> ICESTONE_SPIRE_FEATURE = register("icestone_spire_feature", new IcestoneSpireFeature(DefaultFeatureConfig.CODEC));
    public static final Feature<DefaultFeatureConfig> FREEZE_AETHER_TOP_LAYER_FEATURE_FEATURE = register("freeze_aether_top_layer_feature", new FreezeAetherTopLayerFeature(DefaultFeatureConfig.CODEC));

    public static final ProjectedOrganicCoverFeature ORGANIC_GROUNDCOVER_FEATURE = register("organic_groundcover_feature", new ProjectedOrganicCoverFeature(ProjectedOrganicCoverConfig.CODEC));

    public static final Feature<DefaultFeatureConfig> HONEY_NETTLE_FEATURE = register("honey_nettle_feature", new HoneyNettleFeature(DefaultFeatureConfig.CODEC));


    public static final Feature<LongFeatureConfig> PILLAR_FEATURE = register("pillar_feature", new PillarFeature(LongFeatureConfig.CODEC));
    public static final Feature<LongFeatureConfig> FALLEN_PILLAR_FEATURE = register("fallen_pillar_feature", new FallenPillarFeature(LongFeatureConfig.CODEC));


    private static <C extends FeatureConfig, F extends Feature<C>> F register(String id, F feature) {
        return Registry.register(Registry.FEATURE, Aether.locate(id), feature);
    }

    public static void init() {
        AetherTreeHell.init();
        AetherStructureFeatures.init();
        AetherConfiguredFeatures.init();
        AetherPlacementModifiers.init();
        AetherPlacedFeatures.init();
    }
}