package com.aether.world.dimension;

import com.aether.Aether;
import com.aether.world.biome.AetherBiomeProvider;
import com.aether.world.surfacebuilders.AetherSurfaceBuilders;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public class AetherDimension {
    public static final RegistryKey<World> AETHER_WORLD_KEY = RegistryKey.of(Registry.DIMENSION, Aether.MOD_DIMENSION_ID);

    public static void setupDimension() {
        AetherChunkGenerator.registerChunkGenerator();
        AetherBiomeProvider.registerBiomeProvider();

        AetherSurfaceBuilders.registerSurfaceBuilders();
    }
}
