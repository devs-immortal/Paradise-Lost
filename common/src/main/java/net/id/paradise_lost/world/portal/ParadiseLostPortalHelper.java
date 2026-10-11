package net.id.paradise_lost.world.portal;

import net.id.paradise_lost.world.ParadiseLostGameRules;
import net.id.paradise_lost.world.dimension.ParadiseLostDimension;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.Nullable;

public final class ParadiseLostPortalHelper {
    private ParadiseLostPortalHelper() {}

    public static boolean tryLightPortal(Level level, BlockPos pos) {
        if (level.isClientSide) {
            return false;
        }
        if (!level.getGameRules().getBoolean(ParadiseLostGameRules.PARADISE_PORTAL_ENABLED)) {
            return false;
        }
        if (level.dimension() != Level.OVERWORLD && level.dimension() != ParadiseLostDimension.PARADISE_LOST_WORLD_KEY) {
            return false;
        }

        return ParadiseLostPortalShape.findEmptyPortalShape(level, pos, Direction.Axis.X)
                .map(shape -> {
                    shape.createPortalBlocks();
                    return true;
                })
                .orElse(false);
    }

    @Nullable
    public static ParadiseLostPortalShape findCompleteFrame(LevelAccessor level, BlockPos around) {
        return ParadiseLostPortalShape.findPortalShape(
                level,
                around,
                ParadiseLostPortalShape::isComplete,
                Direction.Axis.X
        ).orElse(null);
    }

    public static boolean placeForcedPortal(Level level, BlockPos footing, Direction.Axis axis) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return false;
        }
        BlockPos bottomLeft = footing.above();
        new ParadiseLostPortalForcer(serverLevel).placeStandardFrame(bottomLeft, axis);
        return true;
    }
}
