package net.id.paradise_lost.world.feature.placement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.id.paradise_lost.world.feature.RiverField;
import net.id.paradise_lost.world.feature.configs.RiverConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

/**
 * Passes only x/z positions that lie on the bank of the river, between
 * {@code min_distance} and {@code max_distance} blocks outside the water edge.
 * The chance of passing fades linearly from 100% at the water to 0% at max_distance,
 * so trees are densest right at the water and thin out inland.
 * <p>
 * Only looks at x/z, so put it after in_square and before the heightmap modifier.
 */
public class RiverBankPlacement extends PlacementFilter {
    public static final MapCodec<RiverBankPlacement> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.floatRange(0f, 32f).optionalFieldOf("min_distance", 1f).forGetter(p -> p.minDistance),
            Codec.floatRange(1f, 64f).optionalFieldOf("max_distance", 12f).forGetter(p -> p.maxDistance)
    ).apply(instance, RiverBankPlacement::new));

    private static final RiverConfiguration RIVER = RiverConfiguration.noiseDefaults();

    private final float minDistance;
    private final float maxDistance;

    public RiverBankPlacement(float minDistance, float maxDistance) {
        this.minDistance = minDistance;
        this.maxDistance = Math.max(maxDistance, minDistance + 1f);
    }

    public static RiverBankPlacement between(float minDistance, float maxDistance) {
        return new RiverBankPlacement(minDistance, maxDistance);
    }

    @Override
    protected boolean shouldPlace(PlacementContext context, RandomSource random, BlockPos pos) {
        RiverField.Noise noise = RiverField.noise(context.getLevel().getSeed(), RIVER.salt());
        double d = RiverField.edgeDistance(noise, RIVER, pos.getX(), pos.getZ(), maxDistance);
        if (d < minDistance || d > maxDistance) {
            return false;
        }
        double fade = (d - minDistance) / (maxDistance - minDistance);
        return random.nextDouble() >= fade;
    }

    @Override
    public PlacementModifierType<?> type() {
        return ParadiseLostPlacementModifiers.RIVER_BANK;
    }
}
