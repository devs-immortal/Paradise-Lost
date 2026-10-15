package net.id.paradise_lost.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

public interface ParadiseLostMinecartExtensions {
    /**
     * Runs before vanilla rail movement. A floating cart above the rail moves on its own instead of snapping back
     * onto the rail.
     *
     * @return whether the cart was moved, so the regular rail movement must be skipped
     */
    boolean paradiseLost$skipMoveAlongTrack(ServerLevel level, BlockPos railPos);
}
