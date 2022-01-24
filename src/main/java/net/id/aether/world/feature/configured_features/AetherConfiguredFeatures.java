package net.id.aether.world.feature.configured_features;

import com.google.common.collect.ImmutableSet;
import net.id.aether.world.feature.features.IcestoneSpireFeature;
import net.id.aether.world.feature.configs.BoulderFeatureConfig;
import net.id.aether.world.feature.configs.GroundcoverFeatureConfig;
import net.id.aether.world.feature.configs.ProjectedOrganicCoverConfig;
import net.id.aether.world.feature.configs.QuicksoilConfig;
import net.id.aether.world.feature.features.*;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.*;

import java.util.List;

import static net.id.aether.Aether.locate;
import static net.id.aether.blocks.AetherBlocks.*;

/**
 * Folks extend this class to get the ooey-gooey goodness of shared Config stuff.
 */
@SuppressWarnings("unused")
public class AetherConfiguredFeatures {
    public static <FC extends FeatureConfig> ConfiguredFeature<FC, ?> register(String id, ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, locate(id), configuredFeature);
    }
    
    public static void init() {
        AetherTreeConfiguredFeatures.init();
        AetherVegetationConfiguredFeatures.init();
        AetherMiscConfiguredFeatures.init();
    }

    public static class Configs {
        // This is incorrect, also convert this and AetherPlacedFeature's AETHER_GROUD to tag
        final static List<BlockState> GENERIC_FLOOR_WHITELIST = List.of(AETHER_GRASS_BLOCK.getDefaultState(), COARSE_AETHER_DIRT.getDefaultState(), HOLYSTONE.getDefaultState(), COBBLED_HOLYSTONE.getDefaultState());

        protected static <C extends FeatureConfig, F extends Feature<C>> F register(String name, F feature) {
            return Registry.register(Registry.FEATURE, locate(name), feature);
        }

        public static final ProjectedOrganicCoverFeature ORGANIC_GROUNDCOVER_FEATURE = register("organic_groundcover_feature", new ProjectedOrganicCoverFeature(ProjectedOrganicCoverConfig.CODEC));

        // pre 1.18 : public static final RandomPatchFeatureConfig FLOWER_CONFIG = (new RandomPatchFeatureConfig.Builder((new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(PURPLE_FLOWER.getDefaultState(), 2).add(WHITE_FLOWER.getDefaultState(), 1)), new SimpleBlockPlacer())).tries(64).build();

        //Misc
        public static final Feature<DeltaFeatureConfig> AETHER_DELTA_FEATURE = register("aether_delta_feature", new AetherDeltaFeature(DeltaFeatureConfig.CODEC));

        public static final Feature<GroundcoverFeatureConfig> GROUNDCOVER_FEATURE = register("groundcover_feature", new GroundcoverFeature(GroundcoverFeature.CODEC));

        public static final Feature<BoulderFeatureConfig> BOULDER = register("boulder", new AetherBoulderFeature(BoulderFeatureConfig.CODEC));

        public static final Feature<DefaultFeatureConfig> ICESTONE_SPIRE_FEATURE = register("icestone_spire_feature", new IcestoneSpireFeature(DefaultFeatureConfig.CODEC));

        public static final Feature<DefaultFeatureConfig> FREEZE_AETHER_TOP_LAYER_FEATURE_FEATURE = register("freeze_aether_top_layer_feature", new FreezeAetherTopLayerFeature(DefaultFeatureConfig.CODEC));

        // Other Special Things
        public static final QuicksoilConfig QUICKSOIL_CONFIG = new QuicksoilConfig();
        public static final DefaultFeatureConfig DEFAULT_CONFIG = new DefaultFeatureConfig();

        public static final SpringFeatureConfig WATER_SPRING_CONFIG = new SpringFeatureConfig(Fluids.WATER.getDefaultState(), true, 4, 1, ImmutableSet.of(HOLYSTONE));

    }
}
