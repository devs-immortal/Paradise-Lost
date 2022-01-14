package net.id.aether.world.feature.features.placed;

import net.id.aether.Aether;
import net.id.aether.tag.AetherBlockTags;
import net.id.aether.world.feature.features.configured.AetherTreeConfiguredFeatures;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.decorator.*;
import net.minecraft.world.gen.feature.*;

import java.util.List;

import static net.id.aether.Aether.locate;
import static net.id.aether.blocks.AetherBlocks.*;

@SuppressWarnings("unused")
public class AetherPlacedFeatures {

    // todo all ".spreadHorizontally()"s have been commented out because they were shown to cause errors in 1.17.
    // if this is not the case anymore, add them back in (however that is done).

    // this does what .withInAirFilter() does, if ever needed
    public static final PlacementModifier IN_AIR = BlockFilterPlacementModifier.of(BlockPredicate.matchingBlock(Blocks.AIR, BlockPos.ORIGIN));
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

        // pre 1.18 : public static final RandomPatchFeatureConfig FLOWER_CONFIG = (new RandomPatchFeatureConfig.Builder((new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(PURPLE_FLOWER.getDefaultState(), 2).add(WHITE_FLOWER.getDefaultState(), 1)), new SimpleBlockPlacer())).tries(64).build();

        // make this into a tag instead
        // trees care about this
        public static final List<Block> AETHER_GROUD = List.of(AETHER_GRASS_BLOCK, HOLYSTONE, MOSSY_HOLYSTONE, AETHER_DIRT, COARSE_AETHER_DIRT, PERMAFROST);

        private static <C extends FeatureConfig, F extends Feature<C>> F register(String name, F feature) {
            return Registry.register(Registry.FEATURE, Aether.locate(name), feature);
        }
    }
}
