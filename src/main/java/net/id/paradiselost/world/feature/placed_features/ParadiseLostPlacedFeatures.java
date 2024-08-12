package net.id.paradiselost.world.feature.placed_features;

import net.id.paradiselost.world.feature.configured_features.ParadiseLostTreeConfiguredFeatures;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.placementmodifier.BlockFilterPlacementModifier;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;

import java.util.List;

import static net.id.paradiselost.ParadiseLost.locate;

@SuppressWarnings("unused")
public class ParadiseLostPlacedFeatures {
    // this does what .withInAirFilter() does
    static final BlockPredicate IN_AIR = BlockPredicate.matchingBlocks(BlockPos.ORIGIN, Blocks.AIR);
    public static final BlockPredicate IN_OR_ON_GROUND = BlockPredicate.allOf(
            BlockPredicate.hasSturdyFace(Vec3i.ZERO.down(), Direction.UP),
            BlockPredicate.solid(Vec3i.ZERO.down()),
            BlockPredicate.matchingFluids(Vec3i.ZERO.down(), Fluids.EMPTY),
            BlockPredicate.matchingFluids(Fluids.EMPTY),
            BlockPredicate.matchingBlocks(Vec3i.ZERO.up(), Blocks.AIR)
    );
    // This also makes it so that there must be a block of air above where the feature begins.
    public static final PlacementModifier ON_SOLID_GROUND = BlockFilterPlacementModifier.of(BlockPredicate.bothOf(IN_OR_ON_GROUND, IN_AIR));
    // for ease of familiarity with how 1.17 did it.
    static final PlacementModifier SPREAD_32_ABOVE = HeightRangePlacementModifier.uniform(YOffset.aboveBottom(32), YOffset.getTop());

    public static RegistryKey<PlacedFeature> of(String id) {
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, locate(id));
    }
    
    public static void init() {
        ParadiseLostTreeConfiguredFeatures.init();
        ParadiseLostVegetationPlacedFeatures.init();
        ParadiseLostMiscPlacedFeatures.init();
    }
}
