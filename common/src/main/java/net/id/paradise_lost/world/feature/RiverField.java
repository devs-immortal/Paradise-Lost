package net.id.paradise_lost.world.feature;

import net.id.paradise_lost.world.feature.configs.RiverConfiguration;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
import net.minecraft.world.level.levelgen.synth.SimplexNoise;

/**
 * The river as a pure function of (seed, x, z). Shared by RiverChannelFeature (carving)
 * and RiverBankPlacement (trees), so both always agree on where the river is.
 */
public final class RiverField {
    private RiverField() {
    }

    public record Noise(long seed, int salt, SimplexNoise course, SimplexNoise warpX, SimplexNoise warpZ, SimplexNoise lake) {
    }

    private static volatile Noise cache;

    public static Noise noise(long seed, int salt) {
        Noise current = cache;
        if (current == null || current.seed() != seed || current.salt() != salt) {
            long s = seed ^ (0x9E3779B97F4A7C15L * (salt + 1L));
            current = new Noise(
                    seed,
                    salt,
                    new SimplexNoise(new XoroshiroRandomSource(s)),
                    new SimplexNoise(new XoroshiroRandomSource(s + 1L)),
                    new SimplexNoise(new XoroshiroRandomSource(s + 2L)),
                    new SimplexNoise(new XoroshiroRandomSource(s + 3L))
            );
            cache = current;
        }
        return current;
    }

    /** Warped course noise. Its zero line is the river centre. */
    private static double course(Noise n, RiverConfiguration cfg, double x, double z) {
        double ws = cfg.warpScale();
        double wx = x + n.warpX().getValue(x * ws, z * ws) * cfg.warpStrength();
        double wz = z + n.warpZ().getValue(x * ws, z * ws) * cfg.warpStrength();
        return n.course().getValue(wx * cfg.scale(), wz * cfg.scale());
    }

    /**
     * Approximate distance in BLOCKS from (x, z) to the river centre line: |noise| / |gradient|.
     * The gradient is clamped so flat spots in the noise thin the river instead of making blobs.
     */
    public static double centreDistance(Noise n, RiverConfiguration cfg, int x, int z) {
        double c = course(n, cfg, x, z);
        double gx = course(n, cfg, x + 1.0, z) - c;
        double gz = course(n, cfg, x, z + 1.0) - c;
        double grad = Math.max(Math.sqrt(gx * gx + gz * gz), cfg.scale() * 0.3);
        return Math.abs(c) / grad;
    }

    /** 0 = no lake influence, 1 = lake centre. */
    public static float lakeMask(Noise n, RiverConfiguration cfg, int x, int z) {
        double v = n.lake().getValue(x * (double) cfg.lakeScale(), z * (double) cfg.lakeScale());
        double u = Math.min(1.0, Math.max(0.0, (v - cfg.lakeThreshold()) / 0.3));
        return (float) (u * u * (3.0 - 2.0 * u));
    }

    /** Half-width in blocks of the water at a point, given its lake mask. */
    public static float halfWidth(RiverConfiguration cfg, float lakeMask) {
        return cfg.halfWidth() + lakeMask * cfg.lakeExtraWidth();
    }

    /** Widest the water can ever be (river + full lake). */
    public static float maxHalfWidth(RiverConfiguration cfg) {
        return cfg.halfWidth() + cfg.lakeExtraWidth();
    }

    /**
     * Blocks from (x, z) to the nearest water edge: negative inside the river, positive on land.
     * Returns +infinity when farther than {@code band} blocks outside the widest possible water.
     */
    public static double edgeDistance(Noise n, RiverConfiguration cfg, int x, int z, double band) {
        double dist = centreDistance(n, cfg, x, z);
        if (dist >= maxHalfWidth(cfg) + band) {
            return Double.POSITIVE_INFINITY;
        }
        return dist - halfWidth(cfg, lakeMask(n, cfg, x, z));
    }

    /** True when (x, z) lies in the water or within {@code margin} blocks of its edge. */
    public static boolean covers(Noise n, RiverConfiguration cfg, int x, int z, double margin) {
        return edgeDistance(n, cfg, x, z, margin) < margin;
    }
}
