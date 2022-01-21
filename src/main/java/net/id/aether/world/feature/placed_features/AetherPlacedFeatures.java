package net.id.aether.world.feature.placed_features;

import net.id.aether.world.feature.configured_features.AetherTreeConfiguredFeatures;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.decorator.BlockFilterPlacementModifier;
import net.minecraft.world.gen.decorator.HeightRangePlacementModifier;
import net.minecraft.world.gen.decorator.PlacementModifier;
import net.minecraft.world.gen.feature.PlacedFeature;

import java.util.List;

import static net.id.aether.Aether.locate;
import static net.id.aether.blocks.AetherBlocks.*;

@SuppressWarnings("unused")
public class AetherPlacedFeatures {
    // this does what .withInAirFilter() does
    public static final BlockPredicate IN_AIR = (BlockPredicate.matchingBlock(Blocks.AIR, BlockPos.ORIGIN));
    public static final BlockPredicate IN_OR_ON_GROUND = (BlockPredicate.allOf(BlockPredicate.hasSturdyFace(Vec3i.ZERO.down(), Direction.UP), BlockPredicate.not(BlockPredicate.solid(Vec3i.ZERO.up()))));
    public static final PlacementModifier ON_SOLID_GROUND = BlockFilterPlacementModifier.of(BlockPredicate.bothOf(IN_OR_ON_GROUND, IN_AIR));
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
