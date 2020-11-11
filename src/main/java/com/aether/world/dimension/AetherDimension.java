package com.aether.world.dimension;

import com.aether.Aether;
import com.aether.blocks.AetherBlocks;
import com.aether.world.biome.AetherBiomeProvider;
import com.aether.world.surfacebuilders.AetherSurfaceBuilders;
import net.kyrptonaught.customportalapi.CustomPortalApiRegistry;
import net.kyrptonaught.customportalapi.CustomPortalBlock;
import net.minecraft.block.Blocks;
import net.minecraft.util.DyeColor;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public class AetherDimension {
    public static final RegistryKey<World> AETHER_WORLD_KEY = RegistryKey.of(Registry.DIMENSION, Aether.locate(Aether.modId));

    public static void setupDimension() {
        AetherChunkGenerator.registerChunkGenerator();
        AetherBiomeProvider.registerBiomeProvider();

        AetherSurfaceBuilders.registerSurfaceBuilders();

        CustomPortalApiRegistry.addPortal(Blocks.GLOWSTONE, Blocks.WATER, (CustomPortalBlock)AetherBlocks.BLUE_PORTAL, Aether.MOD_DIMENSION_ID, DyeColor.LIGHT_BLUE.getMaterialColor().color);
    }
}
