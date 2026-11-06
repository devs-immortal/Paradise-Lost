package net.id.paradise_lost.services;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.id.paradise_lost.item.armor.FloatyBootsItem;
import net.id.paradise_lost.platform.services.IPlatformHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;

public class FabricPlatformHelper implements IPlatformHelper {
    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public boolean isPhysicalClient() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }

    @Override
    public boolean isFarmlandNearWater(LevelReader level, BlockPos pos) {
        for (BlockPos near : BlockPos.betweenClosed(pos.offset(-4, 0, -4), pos.offset(4, 1, 4))) {
            if (level.getFluidState(near).is(FluidTags.WATER)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean shouldTrampleFarmland(Level level, BlockPos pos, float fallDistance, Entity entity) {
        // Vanilla FarmBlock#fallOn.
        return level.random.nextFloat() < fallDistance - 0.5F
                && entity instanceof LivingEntity living
                && (entity instanceof Player || level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING))
                && entity.getBbWidth() * entity.getBbWidth() * entity.getBbHeight() > 0.512F
                && !FloatyBootsItem.isWearing(living);
    }
}
