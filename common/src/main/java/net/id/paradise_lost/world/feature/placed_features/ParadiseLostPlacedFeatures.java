package net.id.paradise_lost.world.feature.placed_features;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.material.Fluids;

import java.util.List;

import static net.id.paradise_lost.ModConstants.id;

@SuppressWarnings("unused")
public class ParadiseLostPlacedFeatures {

    static final BlockPredicate IN_AIR = BlockPredicate.matchesBlocks(BlockPos.ZERO, Blocks.AIR);
    public static final BlockPredicate IN_OR_ON_GROUND = BlockPredicate.allOf(
            BlockPredicate.hasSturdyFace(Vec3i.ZERO.below(), Direction.UP),
            BlockPredicate.solid(Vec3i.ZERO.below()),
            BlockPredicate.matchesFluids(Vec3i.ZERO.below(), Fluids.EMPTY),
            BlockPredicate.matchesFluids(Fluids.EMPTY),
            BlockPredicate.matchesBlocks(Vec3i.ZERO.above(), Blocks.AIR)
    );

    public static final PlacementModifier ON_SOLID_GROUND = BlockPredicateFilter.forPredicate(BlockPredicate.allOf(IN_OR_ON_GROUND, IN_AIR));

    static final PlacementModifier SPREAD_32_ABOVE = HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(32), VerticalAnchor.top());

    public static ResourceKey<PlacedFeature> of(String id) {
        return ResourceKey.create(Registries.PLACED_FEATURE, id(id));
    }

    public static void init() {
        ParadiseLostTreePlacedFeatures.init();
        ParadiseLostVegetationPlacedFeatures.init();
        ParadiseLostMiscPlacedFeatures.init();
    }

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        ParadiseLostMiscPlacedFeatures.bootstrap(context);
        ParadiseLostVegetationPlacedFeatures.bootstrap(context);
        ParadiseLostTreePlacedFeatures.bootstrap(context);
    }

    protected static void register(
            BootstrapContext<PlacedFeature> context,
            ResourceKey<PlacedFeature> key,
            Holder<ConfiguredFeature<?, ?>> configuration,
            List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }

    protected static void register(
            BootstrapContext<PlacedFeature> context,
            ResourceKey<PlacedFeature> key,
            Holder<ConfiguredFeature<?, ?>> configuration,
            PlacementModifier... modifiers) {
        register(context, key, configuration, List.of(modifiers));
    }
}
