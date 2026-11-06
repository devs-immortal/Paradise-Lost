package net.id.paradise_lost.platform.services;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;

public interface IPlatformHelper {
    String getPlatformName();

    boolean isModLoaded(String modId);

    boolean isDevelopmentEnvironment();

    boolean isPhysicalClient();

    boolean isFarmlandNearWater(LevelReader level, BlockPos pos);

    boolean shouldTrampleFarmland(Level level, BlockPos pos, float fallDistance, Entity entity);

    default String getEnvironmentName() {
        return isDevelopmentEnvironment() ? "development" : "production";
    }
}
