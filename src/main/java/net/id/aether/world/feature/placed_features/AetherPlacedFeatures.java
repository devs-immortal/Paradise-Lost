package net.id.aether.world.feature.placed_features;

import net.id.aether.world.feature.configured_features.AetherTreeConfiguredFeatures;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.placementmodifier.BlockFilterPlacementModifier;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;

import java.util.List;

import static net.id.aether.Aether.locate;

@SuppressWarnings("unused")
public class AetherPlacedFeatures {
    // this does what .withInAirFilter() does
    static final BlockPredicate IN_AIR = BlockPredicate.matchingBlock(Blocks.AIR, BlockPos.ORIGIN);
    public static final BlockPredicate IN_OR_ON_GROUND = BlockPredicate.allOf(
            BlockPredicate.hasSturdyFace(Vec3i.ZERO.down(), Direction.UP),
            BlockPredicate.solid(Vec3i.ZERO.down()),
            BlockPredicate.matchingFluid(Fluids.EMPTY, Vec3i.ZERO.down()),
            BlockPredicate.matchingFluid(Fluids.EMPTY, Vec3i.ZERO),
            BlockPredicate.matchingBlock(Blocks.AIR, Vec3i.ZERO.up())
    );
    // This also makes it so that there must be a block of air above where the feature begins.
    public static final PlacementModifier ON_SOLID_GROUND = BlockFilterPlacementModifier.of(BlockPredicate.bothOf(IN_OR_ON_GROUND, IN_AIR));
    // for ease of familiarity with how 1.17 did it.
    static final PlacementModifier SPREAD_32_ABOVE = HeightRangePlacementModifier.uniform(YOffset.aboveBottom(32), YOffset.getTop());

    static RegistryEntry<PlacedFeature> register(String id, RegistryEntry<? extends ConfiguredFeature<?, ?>> feature, PlacementModifier... modifiers) {
        return BuiltinRegistries.add(BuiltinRegistries.PLACED_FEATURE, locate(id), new PlacedFeature(RegistryEntry.upcast(feature), List.of(modifiers)));
    }
    
    public static void init() {
        AetherTreeConfiguredFeatures.init();
        AetherVegetationPlacedFeatures.init();
        AetherMiscPlacedFeatures.init();
    }
}
