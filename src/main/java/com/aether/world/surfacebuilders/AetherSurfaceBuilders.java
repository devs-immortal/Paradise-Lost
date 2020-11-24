package com.aether.world.surfacebuilders;

import com.aether.Aether;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;

public class AetherSurfaceBuilders {
    public static final SurfaceBuilder<TernarySurfaceConfig> AETHER_SURFACE_BUILDER = new AetherSurfaceBuilder(TernarySurfaceConfig.CODEC);

    public static void registerSurfaceBuilders() {
        Registry.register(Registry.SURFACE_BUILDER, Aether.locate("aether_surface_builder"), AETHER_SURFACE_BUILDER);
    }
}