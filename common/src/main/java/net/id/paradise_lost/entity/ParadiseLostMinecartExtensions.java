package net.id.paradise_lost.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

public interface ParadiseLostMinecartExtensions {
    /**
     * Moves a floating cart that is midair above a rail without snapping it back onto the rail.
     *
     * @return whether the cart was moved, so the regular rail movement must be skipped
     */
    boolean paradiseLost$moveMidairAboveRail(ServerLevel level, BlockPos railPos);
}
