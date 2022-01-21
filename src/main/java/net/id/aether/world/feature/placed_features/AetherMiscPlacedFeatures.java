package net.id.aether.world.feature.placed_features;

import net.id.aether.world.feature.configured_features.AetherMiscConfiguredFeatures;
import net.id.aether.world.feature.decorators.ChancePlacementModifier;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.decorator.*;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.feature.SimpleBlockFeatureConfig;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;

import static net.id.aether.blocks.AetherBlocks.*;

public class AetherMiscPlacedFeatures extends AetherPlacedFeatures{
    /*
    Highlands
     */
    // Default
    public static final PlacedFeature GENERIC_BOULDER = register("generic_boulder", AetherMiscConfiguredFeatures.GENERIC_BOULDER.withPlacement(new ChancePlacementModifier(ConstantIntProvider.create(15)), CountMultilayerPlacementModifier.of(1), CountPlacementModifier.of(UniformIntProvider.create(0, 1))));
    public static final PlacedFeature PLAINS_BOULDER = register("plains_boulder", AetherMiscConfiguredFeatures.PLAINS_BOULDER.withPlacement(new ChancePlacementModifier(ConstantIntProvider.create(8)), CountMultilayerPlacementModifier.of(1))); // TODO REPLACED WITH JSON //
    public static final PlacedFeature THICKET_BOULDER = register("thicket_boulder", AetherMiscConfiguredFeatures.THICKET_BOULDER.withPlacement(new ChancePlacementModifier(ConstantIntProvider.create(2)), CountMultilayerPlacementModifier.of(1), CountPlacementModifier.of(UniformIntProvider.create(0, 2))));
    public static final PlacedFeature GOLDEN_BOULDER = register("golden_boulder", AetherMiscConfiguredFeatures.GOLDEN_BOULDER.withPlacement(new ChancePlacementModifier(ConstantIntProvider.create(30)), CountMultilayerPlacementModifier.of(1)));

    public static final PlacedFeature QUICKSOIL = register("quicksoil", AetherMiscConfiguredFeatures.QUICKSOIL.withPlacement());
    public static final PlacedFeature WATER_SPRING = register("water_spring", AetherMiscConfiguredFeatures.WATER_SPRING.withPlacement());

    public static final PlacedFeature ORE_AMBROSIUM = register("ore_ambrosium", AetherMiscConfiguredFeatures.ORE_AMBROSIUM.withPlacement(CountPlacementModifier.of(20), SquarePlacementModifier.of(), HeightRangePlacementModifier.trapezoid(YOffset.aboveBottom(0), YOffset.aboveBottom(127)), BiomePlacementModifier.of()));
    public static final PlacedFeature ORE_GRAVITITE = register("ore_gravitite", AetherMiscConfiguredFeatures.ORE_GRAVITITE.withPlacement(CountPlacementModifier.of(1), SquarePlacementModifier.of(), HeightRangePlacementModifier.trapezoid(YOffset.aboveBottom(32), YOffset.aboveBottom(79)), BiomePlacementModifier.of()));
    public static final PlacedFeature ORE_ZANITE = register("ore_zanite", AetherMiscConfiguredFeatures.ORE_ZANITE.withPlacement(CountPlacementModifier.of(16), SquarePlacementModifier.of(), HeightRangePlacementModifier.trapezoid(YOffset.aboveBottom(0), YOffset.aboveBottom(128)), BiomePlacementModifier.of()));
    // Plato
    // Shield
    public static final PlacedFeature SHIELD_ROCKS = register("shield_rocks", AetherMiscConfiguredFeatures.SHIELD_ROCKS.withPlacement(PlacedFeatures.BOTTOM_TO_TOP_RANGE, CountMultilayerPlacementModifier.of(3), CountPlacementModifier.of(UniformIntProvider.create(0, 3)))/*.spreadHorizontally()*/);

    public static final PlacedFeature SHIELD_PONDS = register("shield_pond", AetherMiscConfiguredFeatures.SHIELD_PONDS.withPlacement(CountMultilayerPlacementModifier.of(30)));

    public static final PlacedFeature SHIELD_STONE = register("shield_stone", AetherMiscConfiguredFeatures.SHIELD_STONE.withPlacement(CountMultilayerPlacementModifier.of(2), CountPlacementModifier.of(UniformIntProvider.create(0, 2))))/*.spreadHorizontally()*/;
    public static final PlacedFeature SHIELD_PODZOL = register("shield_podzol", AetherMiscConfiguredFeatures.SHIELD_PODZOL.withPlacement(CountMultilayerPlacementModifier.of(2), new ChancePlacementModifier(ConstantIntProvider.create(15)), CountPlacementModifier.of(UniformIntProvider.create(0, 2))))/*.spreadHorizontally()*/;
    // Tundra
    public static final PlacedFeature TUNDRA_SPIRES = register("tundra_spires", AetherMiscConfiguredFeatures.TUNDRA_SPIRES.withPlacement(new ChancePlacementModifier(ConstantIntProvider.create(9)), CountMultilayerPlacementModifier.of(1), CountPlacementModifier.of(UniformIntProvider.create(0, 10))/*.spreadHorizontally()*/));

    public static final PlacedFeature TUNDRA_PONDS = register("tundra_pond", AetherMiscConfiguredFeatures.TUNDRA_PONDS.withPlacement(CountMultilayerPlacementModifier.of(1), CountPlacementModifier.of(UniformIntProvider.create(0, 1))));
    public static final PlacedFeature TUNDRA_SNOW = register("tundra_snow", AetherMiscConfiguredFeatures.TUNDRA_SNOW.withPlacement(CountMultilayerPlacementModifier.of(2), CountPlacementModifier.of(UniformIntProvider.create(0, 2))));

    public static void init(){}

    public static class Configs extends AetherPlacedFeatures.Configs {

        public static final PlacedFeature SHIELD_ROCKS_SINGLE_BLOCK = Feature.SIMPLE_BLOCK.configure(new SimpleBlockFeatureConfig(new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(COBBLED_HOLYSTONE_SLAB.getDefaultState().with(SlabBlock.TYPE, SlabType.BOTTOM), 10).add(COBBLED_HOLYSTONE.getDefaultState(), 4).build()))).withPlacement(BlockFilterPlacementModifier.of(IN_OR_ON_GROUND));
    }
}
