package net.id.aether.world.feature.configured_features;

import net.id.aether.world.feature.placed_features.AetherPlacedFeatures;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

import java.util.List;

import static net.id.aether.Aether.locate;
import static net.id.aether.blocks.AetherBlocks.*;

/**
 * Folks extend this class to get the ooey-gooey goodness of shared Config stuff.
 */
@SuppressWarnings("unused")
public class AetherConfiguredFeatures {
    protected static <FC extends FeatureConfig> ConfiguredFeature<FC, ?> register(String id, ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, locate(id), configuredFeature);
    }

    public static void init() {
        AetherTreeConfiguredFeatures.init();
        AetherVegetationConfiguredFeatures.init();
        AetherMiscConfiguredFeatures.init();
    }

    public static class Configs {
        static RandomPatchFeatureConfig blockPatch(int tries, int hSpread, int ySpread, BlockState state){
            return blockPatch(tries, hSpread, ySpread, BlockStateProvider.of(state));
        }
        static RandomPatchFeatureConfig blockPatch(int tries, int hSpread, int ySpread, Block block){
            return blockPatch(tries, hSpread, ySpread, BlockStateProvider.of(block));
        }
        static RandomPatchFeatureConfig blockPatch(int tries, int hSpread, int ySpread, BlockStateProvider provider){
            return new RandomPatchFeatureConfig(
                    tries, hSpread, ySpread,
                    () -> Feature.SIMPLE_BLOCK.configure(new SimpleBlockFeatureConfig(provider)).withPlacement(AetherPlacedFeatures.ON_SOLID_GROUND)
            );
        }
        // TODO This is incorrect, also convert this to tag
        final static List<BlockState> GENERIC_FLOOR_WHITELIST = List.of(AETHER_GRASS_BLOCK.getDefaultState(), COARSE_AETHER_DIRT.getDefaultState(), HOLYSTONE.getDefaultState(), COBBLED_HOLYSTONE.getDefaultState());

        protected static <C extends FeatureConfig, F extends Feature<C>> F register(String name, F feature) {
            return Registry.register(Registry.FEATURE, locate(name), feature);
        }
    }
}
