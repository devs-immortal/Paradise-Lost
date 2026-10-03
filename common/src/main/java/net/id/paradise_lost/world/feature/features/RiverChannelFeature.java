package net.id.paradise_lost.world.feature.features;

import com.mojang.serialization.Codec;
import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.block.natural.cloud.ParadiseLostCloudBlock;
import net.id.paradise_lost.world.feature.RiverField;
import net.id.paradise_lost.world.feature.configs.RiverConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.material.Fluids;

/**
 * River as the zero-contour of a warped 2D noise field, continuous across chunks.
 * Lakes are the same channel widened/deepened where a second noise is high.
 * Placement must be biome-only (no count / in_square): one call per chunk at chunk origin.
 * <p>
 * How a chunk is carved:
 * <ol>
 *   <li>Sample the river mask for the chunk plus a margin.</li>
 *   <li>Read the terrain height of every column in that area, ignoring trees and plants.
 *       Columns already holding water (a neighbour chunk's river) report their surface.</li>
 *   <li>Each river column takes as water level the lowest river column within
 *       {@code level_radius}. The surface is flat across the channel and only steps
 *       along it, and it never sits above the land beside it.</li>
 *   <li>Columns whose ground is more than {@code max_cut} above that level are skipped,
 *       as are stray single columns, so steep slopes get no puddles.</li>
 *   <li>Carve, fill, lay the bed, shave the banks into a slope, then let water at steps
 *       and at island edges flow (frozen rivers hang ice there instead).</li>
 * </ol>
 */
public class RiverChannelFeature extends Feature<RiverConfiguration> {
    private static final BlockState WATER = Blocks.WATER.defaultBlockState();
    private static final BlockState ICE = Blocks.ICE.defaultBlockState();
    private static final BlockState PACKED_ICE = Blocks.PACKED_ICE.defaultBlockState();
    private static final BlockState AIR = Blocks.AIR.defaultBlockState();
    private static final int NONE = Integer.MAX_VALUE;
    private static final int FROZEN_FALL_LENGTH = 6;
    /** How far below a cloud we search for the real ground before calling the column void. */
    private static final int CLOUD_LOOKDOWN = 24;
    private static final int[][] CARDINAL = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    public RiverChannelFeature(Codec<RiverConfiguration> codec) {
        super(codec);
    }

    /** Everything we need to know about one column in the chunk-plus-margin window. */
    private static final class Columns {
        final int size;
        final int wx0;
        final int wz0;
        final float[][] dist;
        final float[][] half;
        final float[][] lake;
        final boolean[][] inside;
        final boolean[][] known;
        final boolean[][] isVoid;
        final boolean[][] waterTop;
        final int[][] ground;
        final int[][] cand;

        Columns(int size, int wx0, int wz0) {
            this.size = size;
            this.wx0 = wx0;
            this.wz0 = wz0;
            this.dist = new float[size][size];
            this.half = new float[size][size];
            this.lake = new float[size][size];
            this.inside = new boolean[size][size];
            this.known = new boolean[size][size];
            this.isVoid = new boolean[size][size];
            this.waterTop = new boolean[size][size];
            this.ground = new int[size][size];
            this.cand = new int[size][size];
        }

        boolean in(int i, int j) {
            return i >= 0 && j >= 0 && i < size && j < size;
        }
    }

    /** Carved sky clouds are solid blocks but not terrain: never ground for a river, never its bed support. */
    private static boolean isCloud(BlockState state) {
        return state.getBlock() instanceof ParadiseLostCloudBlock;
    }

    /** Solid terrain that can hold a river bed (not air, not fluid, not cloud). */
    private static boolean solid(WorldGenLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return !state.isAir() && state.getFluidState().isEmpty() && !isCloud(state);
    }

    private static boolean isWaterish(BlockState state) {
        return !state.getFluidState().isEmpty() || state.is(Blocks.ICE) || state.is(Blocks.PACKED_ICE);
    }

    private static boolean isClutter(BlockState state) {
        return state.isAir() || state.canBeReplaced() || state.is(BlockTags.LOGS) || state.is(BlockTags.LEAVES);
    }

    @Override
    public boolean place(FeaturePlaceContext<RiverConfiguration> context) {
        WorldGenLevel level = context.level();
        RiverConfiguration cfg = context.config();

        int x0 = context.origin().getX() & ~15;
        int z0 = context.origin().getZ() & ~15;
        int minY = level.getMinBuildHeight();
        RiverField.Noise noise = RiverField.noise(level.getSeed(), cfg.salt());

        float maxHalf = RiverField.maxHalfWidth(cfg);
        float reach = maxHalf + cfg.bankWidth();

        // Cheap reject: nothing in this chunk is near the river
        boolean any = false;
        for (int lx = 0; lx < 16 && !any; lx++) {
            for (int lz = 0; lz < 16; lz++) {
                if (RiverField.centreDistance(noise, cfg, x0 + lx, z0 + lz) < reach) {
                    any = true;
                    break;
                }
            }
        }
        if (!any) {
            return false;
        }

        // Margin must stay within the neighbouring chunks (radius 1), which already have terrain
        int margin = windowMargin(cfg);
        Columns c = new Columns(16 + 2 * margin, x0 - margin, z0 - margin);
        sampleMask(c, noise, cfg, reach);
        sampleGround(c, level, minY);

        int[][] lvl = new int[16][16];
        boolean[][] sel = new boolean[16][16];
        computeLevels(c, cfg, margin, reach, lvl);
        int inMask = 0;
        for (int lx = 0; lx < 16; lx++) {
            for (int lz = 0; lz < 16; lz++) {
                if (c.inside[lx + margin][lz + margin]) {
                    inMask++;
                }
            }
        }
        int selected = select(c, cfg, margin, lvl, sel);
        settle(c, cfg, margin, lvl, sel);

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        boolean placed = carve(c, cfg, level, margin, lvl, sel, pos);
        ModConstants.LOGGER.debug("river chunk [{}, {}] frozen={}: mask={} selected={} placed={}",
                x0 >> 4, z0 >> 4, cfg.frozen(), inMask, selected, placed);
        if (placed) {
            shaveBanks(c, cfg, level, margin, lvl, sel, pos);
            flow(c, cfg, level, margin, lvl, sel, pos);
        }
        return placed;
    }

    private static void sampleMask(Columns c, RiverField.Noise noise, RiverConfiguration cfg, float reach) {
        for (int i = 0; i < c.size; i++) {
            for (int j = 0; j < c.size; j++) {
                int x = c.wx0 + i;
                int z = c.wz0 + j;
                double d = RiverField.centreDistance(noise, cfg, x, z);
                if (d >= reach) {
                    c.dist[i][j] = Float.MAX_VALUE;
                    continue;
                }
                float mask = RiverField.lakeMask(noise, cfg, x, z);
                float half = RiverField.halfWidth(cfg, mask);
                c.dist[i][j] = (float) d;
                c.half[i][j] = half;
                c.lake[i][j] = mask;
                c.inside[i][j] = d < half;
            }
        }
    }

    private static void sampleGround(Columns c, WorldGenLevel level, int minY) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int i = 0; i < c.size; i++) {
            for (int j = 0; j < c.size; j++) {
                c.cand[i][j] = NONE;
                if (c.dist[i][j] == Float.MAX_VALUE) {
                    continue; // never consulted
                }
                int x = c.wx0 + i;
                int z = c.wz0 + j;
                if (!level.hasChunk(x >> 4, z >> 4)) {
                    continue;
                }
                c.known[i][j] = true;
                int top = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z) - 1;
                int y = top;
                boolean water = false;
                boolean cloud = false;
                while (y > minY) {
                    BlockState state = level.getBlockState(pos.set(x, y, z));
                    if (isWaterish(state)) {
                        water = true;
                        break;
                    }
                    if (isCloud(state)) {
                        cloud = true; // look through it for the terrain underneath
                    } else if (!isClutter(state)) {
                        break;
                    }
                    if (cloud && top - y > CLOUD_LOOKDOWN) {
                        y = minY; // a cloud with no island under it: treat the column as void
                        break;
                    }
                    y--;
                }
                if (y <= minY) {
                    c.isVoid[i][j] = true;
                    c.ground[i][j] = minY;
                    continue;
                }
                c.ground[i][j] = y;
                c.waterTop[i][j] = water;
                if (c.inside[i][j]) {
                    c.cand[i][j] = water ? y : y - 1;
                }
            }
        }
    }

    /** Extra level-search radius inside a lake, so a 30-block lake still gets one flat surface. */
    private static final int LAKE_LEVEL_BONUS = 8;

    /** Window margin: must cover the largest level search radius, and stay inside the neighbour chunks. */
    private static int windowMargin(RiverConfiguration cfg) {
        return Math.min(15, Math.max(cfg.levelRadius() + (cfg.lakeExtraWidth() > 0f ? LAKE_LEVEL_BONUS : 0), cfg.bankWidth() + 3));
    }

    /** Water level for every chunk column near the river: lowest river column within level_radius (more in lakes). */
    private static void computeLevels(Columns c, RiverConfiguration cfg, int margin, float reach, int[][] lvl) {
        for (int lx = 0; lx < 16; lx++) {
            for (int lz = 0; lz < 16; lz++) {
                int i = lx + margin;
                int j = lz + margin;
                if (c.dist[i][j] >= reach) {
                    lvl[lx][lz] = NONE;
                    continue;
                }
                int r = Math.min(margin, cfg.levelRadius() + Math.round(c.lake[i][j] * LAKE_LEVEL_BONUS));
                int m = NONE;
                for (int di = -r; di <= r; di++) {
                    int ii = i + di;
                    if (ii < 0 || ii >= c.size) {
                        continue;
                    }
                    for (int dj = -r; dj <= r; dj++) {
                        int jj = j + dj;
                        if (jj < 0 || jj >= c.size) {
                            continue;
                        }
                        int v = c.cand[ii][jj];
                        if (v < m) {
                            m = v;
                        }
                    }
                }
                lvl[lx][lz] = m;
            }
        }
    }

    private static int cutLimit(Columns c, RiverConfiguration cfg, int i, int j) {
        return c.dist[i][j] >= cfg.halfWidth() ? cfg.lakeMaxCut() : cfg.maxCut();
    }

    private static int select(Columns c, RiverConfiguration cfg, int margin, int[][] lvl, boolean[][] sel) {
        int count = 0;
        for (int lx = 0; lx < 16; lx++) {
            for (int lz = 0; lz < 16; lz++) {
                int i = lx + margin;
                int j = lz + margin;
                if (!c.inside[i][j] || !c.known[i][j] || c.isVoid[i][j] || lvl[lx][lz] == NONE) {
                    continue;
                }
                int cut = c.ground[i][j] - lvl[lx][lz];
                sel[lx][lz] = cut >= 0 && cut <= cutLimit(c, cfg, i, j);
                if (sel[lx][lz]) {
                    count++;
                }
            }
        }
        return count;
    }

    /** Neighbour outside the chunk counts as water when it is in the mask and is land. */
    private static boolean neighbourIsWater(Columns c, int margin, boolean[][] sel, int lx, int lz) {
        if (lx >= 0 && lz >= 0 && lx < 16 && lz < 16) {
            return sel[lx][lz];
        }
        int i = lx + margin;
        int j = lz + margin;
        return c.in(i, j) && c.known[i][j] && c.inside[i][j] && !c.isVoid[i][j];
    }

    /**
     * Keep water from running onto lower dry land. A column that would spill has its
     * surface lowered to that land instead of being thrown away (the neighbouring higher
     * water then steps down to it, which reads as a small fall). Only when lowering would
     * cut deeper than the limit, or the column is completely isolated, is it dropped.
     * Lowering one column can expose another, so iterate.
     */
    private static void settle(Columns c, RiverConfiguration cfg, int margin, int[][] lvl, boolean[][] sel) {
        boolean changed = true;
        for (int pass = 0; pass < 10 && changed; pass++) {
            changed = false;
            for (int lx = 0; lx < 16; lx++) {
                for (int lz = 0; lz < 16; lz++) {
                    if (!sel[lx][lz]) {
                        continue;
                    }
                    int water = 0;
                    int lowest = lvl[lx][lz];
                    for (int[] d : CARDINAL) {
                        int nx = lx + d[0];
                        int nz = lz + d[1];
                        if (neighbourIsWater(c, margin, sel, nx, nz)) {
                            water++;
                            continue;
                        }
                        int i = nx + margin;
                        int j = nz + margin;
                        if (!c.in(i, j) || !c.known[i][j] || c.isVoid[i][j]) {
                            continue; // unknown or void: void is where falls go
                        }
                        lowest = Math.min(lowest, c.ground[i][j]);
                    }
                    int oi = lx + margin;
                    int oj = lz + margin;
                    if (water == 0) {
                        sel[lx][lz] = false;
                        changed = true;
                    } else if (lowest < lvl[lx][lz]) {
                        if (c.ground[oi][oj] - lowest > cutLimit(c, cfg, oi, oj)) {
                            sel[lx][lz] = false;
                        } else {
                            lvl[lx][lz] = lowest;
                        }
                        changed = true;
                    }
                }
            }
        }
    }

    private static boolean carve(
            Columns c, RiverConfiguration cfg, WorldGenLevel level, int margin,
            int[][] lvl, boolean[][] sel, BlockPos.MutableBlockPos pos
    ) {
        boolean placed = false;
        for (int lx = 0; lx < 16; lx++) {
            for (int lz = 0; lz < 16; lz++) {
                if (!sel[lx][lz]) {
                    continue;
                }
                int i = lx + margin;
                int j = lz + margin;
                int x = c.wx0 + i;
                int z = c.wz0 + j;
                int waterY = lvl[lx][lz];
                int ground = c.ground[i][j];

                // Flat-bottomed U: full depth across most of the width, sloping up only near the banks
                float t = Math.min(1f, (1f - c.dist[i][j] / c.half[i][j]) * 2.2f);
                float s = t * t * (3f - 2f * t);
                float fullDepth = cfg.maxDepth() + c.lake[i][j] * cfg.lakeExtraDepth();
                int depth = Math.max(2, Math.round(fullDepth * s));
                if (nearVoid(c, i, j, 2)) {
                    depth = Math.min(depth, 2); // shallow lip at island edges: a stream, not a draining curtain
                }
                int bedY = waterY - depth + 1;

                // Need solid under the bed so water never hangs over the void
                while (bedY <= waterY
                        && !(solid(level, pos.set(x, bedY - 1, z)) && solid(level, pos.set(x, bedY - 2, z)))) {
                    bedY++;
                }
                if (bedY > waterY) {
                    sel[lx][lz] = false;
                    continue;
                }

                for (int y = ground; y > waterY; y--) {
                    level.setBlock(pos.set(x, y, z), AIR, Block.UPDATE_CLIENTS);
                }
                for (int y = waterY; y >= bedY; y--) {
                    BlockState fill = WATER;
                    if (cfg.frozen() && y == waterY) {
                        fill = (lx + lz) % 5 == 0 ? PACKED_ICE : ICE;
                    }
                    level.setBlock(pos.set(x, y, z), fill, Block.UPDATE_CLIENTS);
                }
                level.setBlock(pos.set(x, bedY - 1, z), cfg.bed(), Block.UPDATE_CLIENTS);
                placed = true;
            }
        }
        return placed;
    }

    private static boolean nearVoid(Columns c, int i, int j, int radius) {
        for (int di = -radius; di <= radius; di++) {
            for (int dj = -radius; dj <= radius; dj++) {
                int ii = i + di;
                int jj = j + dj;
                if (c.in(ii, jj) && c.known[ii][jj] && c.isVoid[ii][jj]) {
                    return true;
                }
            }
        }
        return false;
    }

    /** Land within bank_width of the water slopes down to it instead of standing as a wall. */
    private static void shaveBanks(
            Columns c, RiverConfiguration cfg, WorldGenLevel level, int margin,
            int[][] lvl, boolean[][] sel, BlockPos.MutableBlockPos pos
    ) {
        int bw = cfg.bankWidth();
        if (bw <= 0) {
            return;
        }
        for (int lx = 0; lx < 16; lx++) {
            for (int lz = 0; lz < 16; lz++) {
                if (sel[lx][lz] || lvl[lx][lz] == NONE) {
                    continue;
                }
                int i = lx + margin;
                int j = lz + margin;
                if (!c.known[i][j] || c.isVoid[i][j] || c.waterTop[i][j]) {
                    continue;
                }
                float edge = c.dist[i][j] - c.half[i][j];
                if (edge < 0f || edge >= bw) {
                    continue;
                }
                if (!nearWater(c, margin, sel, lx, lz, bw)) {
                    continue;
                }
                int ground = c.ground[i][j];
                int target = lvl[lx][lz] + 1 + (int) Math.floor(edge);
                if (ground <= target || ground - target > cfg.maxCut()) {
                    continue;
                }
                int x = c.wx0 + i;
                int z = c.wz0 + j;
                BlockState top = level.getBlockState(pos.set(x, ground, z));
                for (int y = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z) - 1; y > target; y--) {
                    level.setBlock(pos.set(x, y, z), AIR, Block.UPDATE_CLIENTS);
                }
                level.setBlock(pos.set(x, target, z), top, Block.UPDATE_CLIENTS);
            }
        }
    }

    private static boolean nearWater(Columns c, int margin, boolean[][] sel, int lx, int lz, int radius) {
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                if (neighbourIsWater(c, margin, sel, lx + dx, lz + dz)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Water at a step down to lower water, or at the island edge, is told to flow so it
     * becomes a fall. Frozen rivers hang a short ice column over the edge instead.
     */
    private static void flow(
            Columns c, RiverConfiguration cfg, WorldGenLevel level, int margin,
            int[][] lvl, boolean[][] sel, BlockPos.MutableBlockPos pos
    ) {
        for (int lx = 0; lx < 16; lx++) {
            for (int lz = 0; lz < 16; lz++) {
                if (!sel[lx][lz]) {
                    continue;
                }
                int i = lx + margin;
                int j = lz + margin;
                int x = c.wx0 + i;
                int z = c.wz0 + j;
                int waterY = lvl[lx][lz];
                for (int[] d : CARDINAL) {
                    int nx = lx + d[0];
                    int nz = lz + d[1];
                    int ni = nx + margin;
                    int nj = nz + margin;
                    if (!c.in(ni, nj) || !c.known[ni][nj]) {
                        continue;
                    }
                    boolean inChunk = nx >= 0 && nz >= 0 && nx < 16 && nz < 16;
                    if (c.isVoid[ni][nj]) {
                        if (cfg.frozen()) {
                            hangIce(level, pos, x + d[0], waterY, z + d[1], lx + lz);
                        } else {
                            level.scheduleTick(pos.set(x, waterY, z), Fluids.WATER, 0);
                        }
                        continue;
                    }
                    if (cfg.frozen()) {
                        continue; // ice ledges need no flow
                    }
                    if (inChunk) {
                        if (sel[nx][nz] && lvl[nx][nz] < waterY) {
                            level.scheduleTick(pos.set(x, waterY, z), Fluids.WATER, 0);
                        }
                    } else if (c.waterTop[ni][nj]) {
                        int theirs = c.ground[ni][nj];
                        if (theirs < waterY) {
                            level.scheduleTick(pos.set(x, waterY, z), Fluids.WATER, 0);
                        } else if (theirs > waterY) {
                            // Their source sits above our carved air: let it fall into our channel
                            pos.set(c.wx0 + ni, theirs, c.wz0 + nj);
                            if (level.getFluidState(pos).is(Fluids.WATER)) {
                                level.scheduleTick(pos, Fluids.WATER, 0);
                            }
                        }
                    }
                }
            }
        }
    }

    private static void hangIce(WorldGenLevel level, BlockPos.MutableBlockPos pos, int x, int topY, int z, int salt) {
        for (int k = 0; k < FROZEN_FALL_LENGTH; k++) {
            pos.set(x, topY - k, z);
            if (pos.getY() <= level.getMinBuildHeight() || !level.isEmptyBlock(pos)) {
                return;
            }
            level.setBlock(pos, (salt + k) % 3 == 0 ? PACKED_ICE : ICE, Block.UPDATE_CLIENTS);
        }
    }
}
