package net.id.paradise_lost.world.portal;

import net.id.paradise_lost.block.ParadiseLostPortalBlock;
import net.id.paradise_lost.registry.BlockRegistry;
import net.id.paradise_lost.tag.ParadiseLostBlockTags;
import net.id.paradise_lost.util.ParadiseLostSoundEvents;
import net.id.paradise_lost.world.ParadiseLostGameRules;
import net.id.paradise_lost.world.dimension.ParadiseLostDimension;
import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.portal.DimensionTransition;

import java.util.Comparator;
import java.util.Optional;

public class ParadiseLostPortalForcer {

    public static final int PARADISE_MIN_PORTAL_Y = 100;
    public static final int OVERWORLD_MIN_PORTAL_Y = -59;
    private static final int PARADISE_MAX_SURFACE_DROP = 32;

    public static final DimensionTransition.PostDimensionTransition PLAY_PORTAL_SOUND =
            ParadiseLostPortalForcer::playTravelSound;

    private static void playTravelSound(Entity entity) {
        entity.level().playSound(
                null,
                entity.blockPosition(),
                ParadiseLostSoundEvents.BLOCK_PORTAL_TRAVEL,
                SoundSource.PLAYERS,
                1.0F,
                1.0F
        );
    }

    private final ServerLevel level;

    public ParadiseLostPortalForcer(ServerLevel level) {
        this.level = level;
    }

    public Optional<BlockPos> findClosestPortalPosition(BlockPos exitPos, WorldBorder worldBorder) {
        PoiManager poiManager = this.level.getPoiManager();
        int radius = 128;
        poiManager.ensureLoadedAndValid(this.level, exitPos, radius);
        return poiManager.getInSquare(
                        type -> type.is(ParadiseLostPoi.BLUE_PORTAL_KEY),
                        exitPos,
                        radius,
                        PoiManager.Occupancy.ANY
                )
                .map(PoiRecord::getPos)
                .filter(worldBorder::isWithinBounds)
                .filter(this::isLinkedPortalBlock)
                .min(Comparator.<BlockPos>comparingDouble(pos -> pos.distSqr(exitPos)).thenComparingInt(Vec3i::getY));
    }

    private boolean isLinkedPortalBlock(BlockPos pos) {
        BlockState state = this.level.getBlockState(pos);
        return state.is(BlockRegistry.BLUE_PORTAL.get())
                && (state.hasProperty(ParadiseLostPortalBlock.AXIS)
                || state.hasProperty(BlockStateProperties.HORIZONTAL_AXIS));
    }

    public Optional<BlockUtil.FoundRectangle> createPortal(BlockPos pos, Direction.Axis axis) {
        if (!this.level.getGameRules().getBoolean(ParadiseLostGameRules.PARADISE_PORTAL_ENABLED)) {
            return Optional.empty();
        }

        Direction.Axis plane = plane(axis);
        Direction direction = Direction.get(Direction.AxisDirection.POSITIVE, plane);
        double bestDist = -1.0;
        BlockPos bestPos = null;
        double backupDist = -1.0;
        BlockPos backupPos = null;
        WorldBorder worldBorder = this.level.getWorldBorder();
        int maxY = Math.min(this.level.getMaxBuildHeight(), this.level.getMinBuildHeight() + this.level.getLogicalHeight()) - 1;
        BlockPos.MutableBlockPos scratch = pos.mutable();
        boolean paradise = this.level.dimension() == ParadiseLostDimension.PARADISE_LOST_WORLD_KEY;
        int minSearchY = paradise
                ? Math.max(this.level.getMinBuildHeight() + 1, PARADISE_MIN_PORTAL_Y)
                : this.level.getMinBuildHeight();

        for (BlockPos.MutableBlockPos cursor : BlockPos.spiralAround(pos, 64, Direction.EAST, Direction.SOUTH)) {
            boolean on_land = true;
            int surfaceY = paradise
                    ? this.findIslandSurfaceY(cursor.getX(), cursor.getZ(), maxY, minSearchY, scratch)
                    : Math.min(maxY, this.level.getHeight(Heightmap.Types.MOTION_BLOCKING, cursor.getX(), cursor.getZ()));
            if (surfaceY == Integer.MIN_VALUE) {
                surfaceY = minSearchY;
                on_land = false;
            }
            int searchFloor = paradise
                    ? Math.max(minSearchY, surfaceY - PARADISE_MAX_SURFACE_DROP)
                    : minSearchY;
            if (surfaceY < searchFloor) {
                continue;
            }
            if (worldBorder.isWithinBounds(cursor) && worldBorder.isWithinBounds(cursor.move(direction, 1))) {
                cursor.move(direction.getOpposite(), 1);

                var y = surfaceY;
                while (y > searchFloor && this.isPortalIgnore(this.level.getBlockState(new BlockPos(cursor.getX(), y, cursor.getZ())))) {
                    --y;
                }

                if (y + 5 <= maxY) {
                    cursor.setY(y + 1);
                    if (cursor.getY() >= minSearchY) {
                        double dist = paradise ? horizontalDistSqr(pos, cursor) : pos.distSqr(cursor);
                        if ((bestDist == -1.0 || bestDist > dist) && on_land) {
                            bestDist = dist;
                            bestPos = cursor.immutable();
                        }
                        if (bestDist == -1.0 && (backupDist == -1.0 || backupDist > dist)) {
                            backupDist = dist;
                            backupPos = cursor.immutable();
                        }
                    }
                }
            }
        }

        if (bestDist == -1.0 && backupDist != -1.0) {
            bestPos = backupPos;
            bestDist = backupDist;
        }

        if (bestDist == -1.0) {
            int minSafeY = paradise
                    ? Math.max(this.level.getMinBuildHeight() + 1, PARADISE_MIN_PORTAL_Y)
                    : OVERWORLD_MIN_PORTAL_Y;
            int maxSafeY = maxY - 9;
            if (maxSafeY < minSafeY) {
                return Optional.empty();
            }

            int fallbackY = pos.getY();
            if (paradise) {
                int islandSurface = this.findIslandSurfaceY(pos.getX(), pos.getZ(), maxY, minSafeY, scratch);
                fallbackY = islandSurface != Integer.MIN_VALUE ? islandSurface : minSafeY;
            }
            bestPos = new BlockPos(pos.getX(), Mth.clamp(fallbackY, minSafeY, maxSafeY), pos.getZ()).immutable();
            Direction clockwise = direction.getClockWise();
            if (!worldBorder.isWithinBounds(bestPos)) {
                return Optional.empty();
            }

            for (int ox = -1; ox < 2; ++ox) {
                for (int oz = 0; oz < 2; ++oz) {
                    for (int oy = -1; oy < 3; ++oy) {
                        BlockState state = oy < 0
                                ? BlockRegistry.BLOOMED_CALCITE.get().defaultBlockState()
                                : Blocks.AIR.defaultBlockState();
                        scratch.setWithOffset(
                                bestPos,
                                oz * direction.getStepX() + ox * clockwise.getStepX(),
                                oy,
                                oz * direction.getStepZ() + ox * clockwise.getStepZ()
                        );
                        this.level.setBlockAndUpdate(scratch, state);
                    }
                }
            }
        }

        if (!paradise && bestPos.getY() < OVERWORLD_MIN_PORTAL_Y) bestPos = bestPos.atY(OVERWORLD_MIN_PORTAL_Y);
        placeStandardFrame(bestPos, direction, plane);
        return Optional.of(new BlockUtil.FoundRectangle(bestPos.immutable(), 2, 3));
    }

    private static double horizontalDistSqr(BlockPos from, BlockPos to) {
        double dx = from.getX() - to.getX();
        double dz = from.getZ() - to.getZ();
        return dx * dx + dz * dz;
    }

    private int findIslandSurfaceY(int x, int z, int maxY, int minY, BlockPos.MutableBlockPos scratch) {
        this.level.getChunk(x >> 4, z >> 4, ChunkStatus.FULL, true);
        int y = Math.min(maxY, this.level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z) + 20);
        if (y < minY) {
            return Integer.MIN_VALUE;
        }
        return Math.min(maxY, y + 1);
    }

    private boolean isPortalIgnore(BlockState state) {
        return state.isAir() && state.is(ParadiseLostBlockTags.PORTAL_IGNORE);
    }

    private static Direction.Axis plane(Direction.Axis axis) {
        return axis.isHorizontal() ? axis : Direction.Axis.X;
    }

    public BlockUtil.FoundRectangle placeStandardFrame(BlockPos bottomLeft, Direction.Axis axis) {
        Direction.Axis plane = plane(axis);
        Direction direction = Direction.get(Direction.AxisDirection.POSITIVE, plane);
        placeStandardFrame(bottomLeft, direction, plane);
        return new BlockUtil.FoundRectangle(bottomLeft.immutable(), 2, 3);
    }

    private void placeStandardFrame(BlockPos bottomLeft, Direction direction, Direction.Axis axis) {
        BlockPos.MutableBlockPos mutable = bottomLeft.mutable();
        BlockState frame = BlockRegistry.BLOOMED_CALCITE.get().defaultBlockState();

        for (int w = -1; w < 3; ++w) {
            for (int h = -1; h < 4; ++h) {
                if (w == -1 || w == 2 || h == -1 || h == 3) {
                    mutable.setWithOffset(bottomLeft, w * direction.getStepX(), h, w * direction.getStepZ());
                    this.level.setBlock(mutable, frame, 1 | 2);
                }
            }
        }

        Direction depth = direction.getClockWise();
        for (int w = 0; w < 2; ++w) {
            for (int d : new int[]{-1, 1}) {
                mutable.setWithOffset(
                        bottomLeft,
                        w * direction.getStepX() + d * depth.getStepX(),
                        -1,
                        w * direction.getStepZ() + d * depth.getStepZ()
                );
                if (!this.level.getBlockState(mutable).blocksMotion()) {
                    this.level.setBlock(mutable, frame, 3);
                }
                for (int h = 1; h < 4; ++h) {
                    this.level.setBlock(mutable.above(h), Blocks.AIR.defaultBlockState(), 3);
                }
            }
        }

        BlockState portal = BlockRegistry.BLUE_PORTAL.get().defaultBlockState()
                .setValue(ParadiseLostPortalBlock.AXIS, axis);
        for (int w = 0; w < 2; ++w) {
            for (int h = 0; h < 3; ++h) {
                mutable.setWithOffset(bottomLeft, w * direction.getStepX(), h, w * direction.getStepZ());
                this.level.setBlock(mutable, portal, 2 | 16);
                this.level.getPoiManager().add(mutable.immutable(), ParadiseLostPoi.BLUE_PORTAL.asHolder());
            }
        }
    }
}
