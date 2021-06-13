package com.aether.world.dimension;

import com.aether.Aether;
import com.aether.blocks.AetherBlocks;
import com.aether.world.gen.AetherSurfaceBuilder;
import com.aether.world.gen.AetherSurfaceBuilderConfig;
import net.kyrptonaught.customportalapi.CustomPortalApiRegistry;
import net.kyrptonaught.customportalapi.CustomPortalBlock;
import net.kyrptonaught.customportalapi.portal.PortalIgnitionSource;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder;

public class AetherDimension {
    public static final ResourceKey<Level> AETHER_WORLD_KEY = ResourceKey.create(Registry.DIMENSION_REGISTRY, Aether.locate(Aether.MOD_ID));
    public static final ResourceKey<DimensionType> TYPE = ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, Aether.locate("the_aether"));

    public static final SurfaceBuilder<AetherSurfaceBuilderConfig> AETHER_SURFACE_BUILDER =
            Registry.register(Registry.SURFACE_BUILDER, Aether.locate("surface_builder"), new AetherSurfaceBuilder());

    public static void setupDimension() {
        CustomPortalApiRegistry.addPortal(Blocks.GLOWSTONE, PortalIgnitionSource.WATER, (CustomPortalBlock) AetherBlocks.BLUE_PORTAL, Aether.locate(Aether.MOD_ID), 2, 3, 55, 89, 195);
    }
}