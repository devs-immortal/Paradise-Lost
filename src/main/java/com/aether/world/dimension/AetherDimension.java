package com.aether.world.dimension;

import com.aether.Aether;
import com.aether.blocks.AetherBlocks;
import com.aether.world.gen.AetherSurfaceBuilder;
import com.aether.world.gen.AetherSurfaceBuilderConfig;
import net.kyrptonaught.customportalapi.CustomPortalApiRegistry;
import net.kyrptonaught.customportalapi.CustomPortalBlock;
import net.minecraft.block.Blocks;
import net.minecraft.util.DyeColor;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;

public class AetherDimension {
    public static final RegistryKey<World> AETHER_WORLD_KEY = RegistryKey.of(Registry.DIMENSION, Aether.locate(Aether.MOD_ID));

    public static final SurfaceBuilder<AetherSurfaceBuilderConfig> AETHER_SURFACE_BUILDER =
            Registry.register(Registry.SURFACE_BUILDER, Aether.locate("surface_builder"), new AetherSurfaceBuilder());

    public static void setupDimension() {
        CustomPortalApiRegistry.addPortal(Blocks.GLOWSTONE, Blocks.WATER, (CustomPortalBlock) AetherBlocks.BLUE_PORTAL, Aether.locate(Aether.MOD_ID), DyeColor.LIGHT_BLUE.getMaterialColor().color);
    }
}