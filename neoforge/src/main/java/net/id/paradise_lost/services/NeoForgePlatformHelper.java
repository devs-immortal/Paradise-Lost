package net.id.paradise_lost.services;

import net.id.paradise_lost.platform.services.IPlatformHelper;
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
            throw new IllegalStateException("NeoForge mod bus not bound; call NeoForgePlatformHelper.bindModBus from ModMain first");
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
}
