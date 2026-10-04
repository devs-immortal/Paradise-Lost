package net.id.paradise_lost.world.portal;

import net.id.paradise_lost.block.ParadiseLostPortalBlock;
import net.id.paradise_lost.registry.BlockRegistry;
import net.id.paradise_lost.util.ParadiseLostSoundEvents;
import net.id.paradise_lost.world.ParadiseLostGameRules;
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
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.portal.TeleportTransition;

import java.util.Comparator;
import java.util.Optional;

public class ParadiseLostPortalForcer {

    public static final TeleportTransition.PostTeleportTransition PLAY_PORTAL_SOUND =
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

                .filter(pos -> {
                    BlockState state = this.level.getBlockState(pos);
                    return state.hasProperty(ParadiseLostPortalBlock.AXIS)
                            || state.hasProperty(BlockStateProperties.HORIZONTAL_AXIS);
                })
                .min(Comparator.<BlockPos>comparingDouble(pos -> pos.distSqr(exitPos)).thenComparingInt(Vec3i::getY));
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
        int maxY = Math.min(this.level.getMaxY() + 1, this.level.getMinY() + this.level.getLogicalHeight()) - 1;
        BlockPos.MutableBlockPos scratch = pos.mutable();

        for (BlockPos.MutableBlockPos cursor : BlockPos.spiralAround(pos, 64, Direction.EAST, Direction.SOUTH)) {
            int surfaceY = Math.min(maxY, this.level.getHeight(Heightmap.Types.MOTION_BLOCKING, cursor.getX(), cursor.getZ()));
            if (worldBorder.isWithinBounds(cursor) && worldBorder.isWithinBounds(cursor.move(direction, 1))) {
                cursor.move(direction.getOpposite(), 1);

                for (int y = surfaceY; y >= this.level.getMinY(); --y) {
                    cursor.setY(y);
                    if (this.level.isEmptyBlock(cursor)) {
                        int emptyBottom;
                        for (emptyBottom = y;
                             y > this.level.getMinY() && this.level.isEmptyBlock(cursor.move(Direction.DOWN));
                             --y) {
                        }

                        if (y + 4 <= maxY) {
                            int airColumn = emptyBottom - y;
                            if (airColumn <= 0 || airColumn >= 3) {
                                cursor.setY(y);
                                if (this.canHostFrame(cursor, scratch, direction, 0)) {
                                    double dist = pos.distSqr(cursor);
                                    if (this.canHostFrame(cursor, scratch, direction, -1)
                                            && this.canHostFrame(cursor, scratch, direction, 1)
                                            && (bestDist == -1.0 || bestDist > dist)) {
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
                }
            }
        }

        if (bestDist == -1.0 && backupDist != -1.0) {
            bestPos = backupPos;
            bestDist = backupDist;
        }

        if (bestDist == -1.0) {
            int minSafeY = Math.max(this.level.getMinY() + 1, 70);
            int maxSafeY = maxY - 9;
            if (maxSafeY < minSafeY) {
                return Optional.empty();
            }

            bestPos = new BlockPos(pos.getX(), Mth.clamp(pos.getY(), minSafeY, maxSafeY), pos.getZ()).immutable();
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

        placeStandardFrame(bestPos, direction, plane);
        return Optional.of(new BlockUtil.FoundRectangle(bestPos.immutable(), 2, 3));
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
            }
        }

        BlockState portal = BlockRegistry.BLUE_PORTAL.get().defaultBlockState()
                .setValue(ParadiseLostPortalBlock.AXIS, axis);
        for (int w = 0; w < 2; ++w) {
            for (int h = 0; h < 3; ++h) {
                mutable.setWithOffset(bottomLeft, w * direction.getStepX(), h, w * direction.getStepZ());
                this.level.setBlock(mutable, portal, 2 | 16);
            }
        }
    }

    private boolean canHostFrame(BlockPos originalPos, BlockPos.MutableBlockPos offsetPos, Direction direction, int offsetScale) {
        Direction clockWise = direction.getClockWise();
        for (int i = -1; i < 3; ++i) {
            for (int j = -1; j < 4; ++j) {
                offsetPos.setWithOffset(
                        originalPos,
                        direction.getStepX() * i + clockWise.getStepX() * offsetScale,
                        j,
                        direction.getStepZ() * i + clockWise.getStepZ() * offsetScale
                );
                BlockState state = this.level.getBlockState(offsetPos);
                if (j < 0 && state.isAir()) {
                    return false;
                }
                if (j >= 0 && !this.level.isEmptyBlock(offsetPos)) {
                    return false;
                }
            }
        }
        return true;
    }
}
