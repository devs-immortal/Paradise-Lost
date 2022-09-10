package net.id.paradiselost.world.feature.configured_features;

import net.id.paradiselost.world.feature.placed_features.ParadiseLostPlacedFeatures;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.predicate.BlockPredicate;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

import java.util.List;

import static net.id.paradiselost.ParadiseLost.locate;
import static net.id.paradiselost.blocks.ParadiseLostBlocks.*;

/**
 * Folks extend this class to get the ooey-gooey goodness of shared Config stuff.
 */
@SuppressWarnings("unused")
public class ParadiseLostConfiguredFeatures {
    protected static <FC extends FeatureConfig, F extends Feature<FC>> RegistryEntry<ConfiguredFeature<FC, ?>> register(String id, F feature, FC featureConfig) {
        return register(BuiltinRegistries.CONFIGURED_FEATURE, id, new ConfiguredFeature<>(feature, featureConfig));
    }

    private static <V extends T, T> RegistryEntry<V> register(Registry<T> registry, String id, V value) {
        return (RegistryEntry<V>) BuiltinRegistries.<T>add(registry, locate(id), value);
    }

    public static void init() {
        ParadiseLostTreeConfiguredFeatures.init();
        ParadiseLostVegetationConfiguredFeatures.init();
        ParadiseLostMiscConfiguredFeatures.init();
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
                    singleBlock(provider, ParadiseLostPlacedFeatures.ON_SOLID_GROUND)
            );
        }
        static SimpleBlockFeatureConfig singleBlockConfig(BlockStateProvider provider){
            return new SimpleBlockFeatureConfig(provider);
        }
        static SimpleBlockFeatureConfig singleBlockConfig(Block block){
            return singleBlockConfig(BlockStateProvider.of(block));
        }
        static RegistryEntry<PlacedFeature> singleBlock(BlockStateProvider provider){
            return PlacedFeatures.createEntry(Feature.SIMPLE_BLOCK, singleBlockConfig(provider));
        }
        static RegistryEntry<PlacedFeature> singleBlock(Block block){
            return singleBlock(BlockStateProvider.of(block));
        }

        static RegistryEntry<PlacedFeature> singleBlock(BlockStateProvider provider, PlacementModifier... modifiers){
            return PlacedFeatures.createEntry(Feature.SIMPLE_BLOCK, new SimpleBlockFeatureConfig(provider), modifiers);
        }
        static RegistryEntry<PlacedFeature> singleBlock(Block block, PlacementModifier... modifiers){
            return singleBlock(BlockStateProvider.of(block), modifiers);
        }
        // TODO This is incorrect, also convert this to tag
        final static List<BlockState> GENERIC_FLOOR_WHITELIST = List.of(GRASS_BLOCK.getDefaultState(), COARSE_DIRT.getDefaultState(), HOLYSTONE.getDefaultState(), COBBLED_HOLYSTONE.getDefaultState());

        protected static <C extends FeatureConfig, F extends Feature<C>> F register(String name, F feature) {
            return Registry.register(Registry.FEATURE, locate(name), feature);
        }
    }
}
