package net.id.aether.world.feature.features.placed;

import net.id.aether.Aether;
import net.id.aether.world.feature.features.configured.AetherTreeConfiguredFeatures;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.decorator.BlockFilterPlacementModifier;
import net.minecraft.world.gen.decorator.HeightRangePlacementModifier;
import net.minecraft.world.gen.decorator.PlacementModifier;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.PlacedFeature;

import java.util.List;

import static net.id.aether.Aether.locate;
import static net.id.aether.blocks.AetherBlocks.*;

@SuppressWarnings("unused")
public class AetherPlacedFeatures {
    // for ease of familiarity with how 1.17 did it.
    public static final PlacementModifier SPREAD_32_ABOVE = HeightRangePlacementModifier.uniform(YOffset.aboveBottom(32), YOffset.getTop());

    static PlacedFeature register(String id, PlacedFeature feature) {
        return Registry.register(BuiltinRegistries.PLACED_FEATURE, locate(id), feature);
    }
    
    public static void init() {
        AetherTreeConfiguredFeatures.init();
        AetherVegetationPlacedFeatures.init();
        AetherMiscPlacedFeatures.init();
    }

    public static class Configs {
        // make this into a tag instead
        // trees care about this
        public static final List<Block> AETHER_GROUD = List.of(AETHER_GRASS_BLOCK, HOLYSTONE, MOSSY_HOLYSTONE, AETHER_DIRT, COARSE_AETHER_DIRT, PERMAFROST);
    }
}
