package net.id.aether.world.dimension;

import net.id.aether.Aether;
import net.id.aether.blocks.AetherBlocks;
import net.id.aether.world.gen.AetherSurfaceBuilder;
import net.id.aether.world.gen.AetherSurfaceBuilderConfig;
import net.kyrptonaught.customportalapi.CustomPortalBlock;
import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;

public class AetherDimension {
    public static final RegistryKey<World> AETHER_WORLD_KEY = RegistryKey.of(Registry.WORLD_KEY, Aether.locate(Aether.MOD_ID));
    public static final RegistryKey<DimensionType> TYPE = RegistryKey.of(Registry.DIMENSION_TYPE_KEY, Aether.locate("the_aether"));

    public static final RegistryKey<Biome> HIGHLANDS_PLAINS = RegistryKey.of(Registry.BIOME_KEY, Aether.locate("aether_highlands"));
    public static final RegistryKey<Biome> HIGHLANDS_FOREST = RegistryKey.of(Registry.BIOME_KEY, Aether.locate("aether_highlands_forest"));
    public static final RegistryKey<Biome> HIGHLANDS_THICKET = RegistryKey.of(Registry.BIOME_KEY, Aether.locate("aether_highlands_thicket"));
    public static final RegistryKey<Biome> WISTERIA_WOODS = RegistryKey.of(Registry.BIOME_KEY, Aether.locate("aether_wisteria_woods"));

    public static final SurfaceBuilder<AetherSurfaceBuilderConfig> AETHER_SURFACE_BUILDER =
            Registry.register(Registry.SURFACE_BUILDER, Aether.locate("surface_builder"), new AetherSurfaceBuilder());

    public static void setupDimension() {
        CustomPortalBuilder.beginPortal()
                .frameBlock(Blocks.GLOWSTONE)
                .customPortalBlock((CustomPortalBlock) AetherBlocks.BLUE_PORTAL)
                .destDimID(Aether.locate(Aether.MOD_ID))
                .tintColor(55, 89, 195)
                .lightWithWater()
                .onlyLightInOverworld()
                .registerPortal();
    }
}