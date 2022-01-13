package net.id.aether.world.dimension;

import net.id.aether.Aether;
import net.id.aether.blocks.AetherBlocks;
import net.id.aether.util.AetherSoundEvents;
import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.kyrptonaught.customportalapi.util.CPASoundEventData;
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

    public static void init() {
        CustomPortalBuilder.beginPortal()
                .frameBlock(Blocks.GLOWSTONE)
                .customPortalBlock(AetherBlocks.BLUE_PORTAL)
                .destDimID(Aether.locate(Aether.MOD_ID))
                .tintColor(55, 89, 195)
                .lightWithWater()
                .onlyLightInOverworld()
                .registerInPortalAmbienceSound(player -> new CPASoundEventData(AetherSoundEvents.BLOCK_AETHER_PORTAL_TRIGGER, player.getRandom().nextFloat() * 0.4F + 0.8F, 0.25F))
                .registerPostTPPortalAmbience(player -> new CPASoundEventData(AetherSoundEvents.BLOCK_AETHER_PORTAL_TRAVEL, player.getRandom().nextFloat() * 0.4F + 0.8F, 0.25F))
                .registerPortal();
    }
}