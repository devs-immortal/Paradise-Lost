package net.id.paradise_lost.world.feature.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.id.paradise_lost.registry.BlockRegistry;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

/**
 * Noise-contour river + lakes. The river is the zero line of a warped simplex field;
 * width is measured in BLOCKS (noise value divided by its local gradient), so it does
 * not swell into blobs where the noise happens to be flat.
 * <p>
 * Lakes are not a separate feature: a second, lower-frequency noise widens and deepens
 * the river wherever it rises above {@code lakeThreshold}, so lakes always sit on the
 * river network and connect to it. Lakes only form on nearly flat ground.
 * <p>
 * The water surface is flat across the whole channel: every water column takes the
 * lowest bank found within {@code levelRadius}, so a river on a hillside becomes a
 * level pool with a cut bank rather than a tilted sheet. Where the ground is more than
 * {@code maxCut} above that level the river is simply not carved.
 *
 * @param scale          noise frequency (~0.004 = one period every ~250 blocks)
 * @param halfWidth      half the river width in blocks (6 = ~12 blocks wide)
 *                       (levelRadius 7 trades a perfectly flat surface for rivers that actually appear on hilly ground)
 * @param warpScale      domain-warp frequency (meander)
 * @param warpStrength   warp amplitude in blocks
 * @param maxDepth       water depth across the channel (6 gives rivers 5-7 deep)
 * @param levelRadius    water level = lowest river column within this many blocks (>= river width keeps the surface flat)
 * @param maxCut         refuse river columns where ground is more than this above the water level
 * @param lakeMaxCut     same limit for the widened lake zone; small so lakes only fill flat ground
 * @param bankWidth      land this close to the water is shaved into a gentle slope down to it
 * @param salt           independent river networks
 * @param frozen         ice surface instead of water top, frozen falls at island edges
 * @param bed            block under the water column
 * @param lakeScale      frequency of the lake noise (0.012 = lake spots roughly every ~80 blocks)
 * @param lakeThreshold  lake noise must exceed this (simplex is roughly -1..1; higher = rarer lakes)
 * @param lakeExtraWidth extra half-width in blocks at the centre of a lake
 * @param lakeExtraDepth extra depth in blocks at the centre of a lake (6 + 9 = 15 deep at most)
 */
public record RiverConfiguration(
        float scale,
        float halfWidth,
        float warpScale,
        float warpStrength,
        int maxDepth,
        int levelRadius,
        int maxCut,
        int lakeMaxCut,
        int bankWidth,
        int salt,
        boolean frozen,
        BlockState bed,
        float lakeScale,
        float lakeThreshold,
        float lakeExtraWidth,
        int lakeExtraDepth
) implements FeatureConfiguration {

    public static final Codec<RiverConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.floatRange(0.0005f, 0.1f).optionalFieldOf("scale", 0.004f).forGetter(RiverConfiguration::scale),
            Codec.floatRange(1f, 64f).optionalFieldOf("half_width", 6f).forGetter(RiverConfiguration::halfWidth),
            Codec.floatRange(0.0005f, 0.1f).optionalFieldOf("warp_scale", 0.01f).forGetter(RiverConfiguration::warpScale),
            Codec.floatRange(0f, 200f).optionalFieldOf("warp_strength", 40f).forGetter(RiverConfiguration::warpStrength),
            Codec.intRange(1, 16).optionalFieldOf("max_depth", 6).forGetter(RiverConfiguration::maxDepth),
            Codec.intRange(1, 15).optionalFieldOf("level_radius", 7).forGetter(RiverConfiguration::levelRadius),
            Codec.intRange(1, 12).optionalFieldOf("max_cut", 6).forGetter(RiverConfiguration::maxCut),
            Codec.intRange(0, 12).optionalFieldOf("lake_max_cut", 3).forGetter(RiverConfiguration::lakeMaxCut),
            Codec.intRange(0, 6).optionalFieldOf("bank_width", 3).forGetter(RiverConfiguration::bankWidth),
            Codec.INT.optionalFieldOf("salt", 0).forGetter(RiverConfiguration::salt),
            Codec.BOOL.optionalFieldOf("frozen", false).forGetter(RiverConfiguration::frozen),
            BlockState.CODEC.optionalFieldOf("bed", Blocks.DIRT.defaultBlockState()).forGetter(RiverConfiguration::bed),
            Codec.floatRange(0.001f, 0.1f).optionalFieldOf("lake_scale", 0.012f).forGetter(RiverConfiguration::lakeScale),
            Codec.floatRange(-1f, 1f).optionalFieldOf("lake_threshold", 0.3f).forGetter(RiverConfiguration::lakeThreshold),
            Codec.floatRange(0f, 64f).optionalFieldOf("lake_extra_width", 10f).forGetter(RiverConfiguration::lakeExtraWidth),
            Codec.intRange(0, 12).optionalFieldOf("lake_extra_depth", 9).forGetter(RiverConfiguration::lakeExtraDepth)
    ).apply(instance, RiverConfiguration::new));

    private static RiverConfiguration make(boolean frozen, BlockState bed) {
        return new RiverConfiguration(
                0.004f, 6f, 0.01f, 40f,
                6, 7, 6, 3, 3,
                0, frozen, bed,
                0.012f, 0.3f, 10f, 9);
    }

    /**
     * The river shape the placed features use. RiverBankPlacement reads this too, so trees follow
     * the same river. If you change the defaults in make(), both update together.
     * Uses a vanilla bed block so it is safe to call at any time (no registry lookups).
     */
    public static RiverConfiguration noiseDefaults() {
        return make(false, Blocks.DIRT.defaultBlockState());
    }

    public static RiverConfiguration thawed() {
        return make(false, BlockRegistry.DIRT.get().defaultBlockState());
    }

    public static RiverConfiguration iced() {
        return make(true, BlockRegistry.PERMAFROST.get().defaultBlockState());
    }
}
