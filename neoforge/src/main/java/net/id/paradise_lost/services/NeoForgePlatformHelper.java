package net.id.paradise_lost.services;

import net.id.paradise_lost.platform.services.IPlatformHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.FarmlandWaterManager;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.bus.api.IEventBus;

public class NeoForgePlatformHelper implements IPlatformHelper {
    private static IEventBus modEventBus;

    public static void bindModBus(IEventBus bus) {
        modEventBus = bus;
    }

    public static IEventBus modBus() {
        if (modEventBus == null) {
            throw new IllegalStateException("NeoForge mod bus not bound; call NeoForgePlatformHelper.bindModBus from NeoForgeMain first");
        }
        return modEventBus;
    }

    @Override
    public String getPlatformName() {
        return "NeoForge";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }

    @Override
    public boolean isPhysicalClient() {
        return FMLEnvironment.dist == Dist.CLIENT;
    }

    @Override
    public boolean isFarmlandNearWater(LevelReader level, BlockPos pos) {
        // FarmBlock#isNearWater as NeoForge patches it.
        BlockState state = level.getBlockState(pos);
        for (BlockPos near : BlockPos.betweenClosed(pos.offset(-4, 0, -4), pos.offset(4, 1, 4))) {
            if (state.canBeHydrated(level, pos, level.getFluidState(near), near)) {
                return true;
            }
        }
        return FarmlandWaterManager.hasBlockWaterTicket(level, pos);
    }

    @Override
    public boolean shouldTrampleFarmland(Level level, BlockPos pos, float fallDistance, Entity entity) {
        // Entity#canTrample (the vanilla rules) and the trample event, where floaty boots say no (see NeoForgeMain).
        return CommonHooks.onFarmlandTrample(level, pos, Blocks.DIRT.defaultBlockState(), fallDistance, entity);
    }
}
