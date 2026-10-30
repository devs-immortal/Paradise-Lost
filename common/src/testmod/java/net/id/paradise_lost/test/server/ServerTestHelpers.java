package net.id.paradise_lost.test.server;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;

public final class ServerTestHelpers {
    public static final Direction FACING = Direction.SOUTH;

    private ServerTestHelpers() {
    }

    public static ServerLevel level() {
        return ServerTestRunner.server().overworld();
    }

    public static BlockPos ahead(int distance, int side) {
        return ServerTestRunner.site().relative(FACING, distance).relative(FACING.getClockWise(), side);
    }
}
