package net.id.aether.world.dimension;

import com.mojang.serialization.JsonOps;
import com.mojang.serialization.Lifecycle;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.id.aether.blocks.AetherBlocks;
import net.id.aether.util.AetherSoundEvents;
import net.id.aether.util.MiscUtil;
import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.kyrptonaught.customportalapi.util.CPASoundEventData;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.RegistryOps;
import net.minecraft.util.registry.*;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;
import net.minecraft.world.gen.FlatLevelGeneratorPreset;

import java.io.IOException;

import static net.id.aether.Aether.MOD_ID;
import static net.id.aether.Aether.locate;

public class AetherDimension {
    public static final RegistryKey<World> AETHER_WORLD_KEY = key(Registry.WORLD_KEY, MOD_ID);
    public static final RegistryKey<DimensionType> DIMENSION_TYPE = key(Registry.DIMENSION_TYPE_KEY, MOD_ID);
    public static final RegistryKey<DimensionOptions> AETHER_OPTIONS_KEY = key(Registry.DIMENSION_KEY, MOD_ID);
    public static final RegistryKey<FlatLevelGeneratorPreset> SUPERFLAT_PRESET = key(Registry.FLAT_LEVEL_GENERATOR_PRESET_KEY, MOD_ID);
    
    private static DimensionType dimensionType;
    
    private static <T> RegistryKey<T> key(RegistryKey<? extends Registry<T>> registry, String name) {
        return RegistryKey.of(registry, locate(name));
    }
    
    public static void init() {
        try {
            dimensionType = MiscUtil.deserializeDataJson(DimensionType.CODEC, locate("dimension_type/the_aether"));
        }catch (IOException e) {
            throw new RuntimeException("Failed to read Paradise Lost dimension JSONs", e);
        }
        
        CustomPortalBuilder.beginPortal()
                .frameBlock(Blocks.GLOWSTONE)
                .customPortalBlock(AetherBlocks.BLUE_PORTAL)
                .destDimID(locate(MOD_ID))
                .tintColor(55, 89, 195)
                .lightWithWater()
                .onlyLightInOverworld()
                .registerInPortalAmbienceSound(player -> new CPASoundEventData(AetherSoundEvents.BLOCK_AETHER_PORTAL_TRIGGER, player.getRandom().nextFloat() * 0.4F + 0.8F, 0.25F))
                .registerPostTPPortalAmbience(player -> new CPASoundEventData(AetherSoundEvents.BLOCK_AETHER_PORTAL_TRAVEL, player.getRandom().nextFloat() * 0.4F + 0.8F, 0.25F))
                .registerPortal();
    }
    
    public static void registerDimensionTypes(MutableRegistry<DimensionType> registry) {
        registry.add(DIMENSION_TYPE, dimensionType, Lifecycle.stable());
    }
    
    public static void registerDefaultOptions(DynamicRegistryManager registryManager, SimpleRegistry<DimensionOptions> registry, long seed, boolean useInstance) {
        /*
        Yes this is awful, no I don't care. It works. Blame Mojang.
        Rational:
         They had the super big brain idea to make the server and client datapack logic function differently. This is an
         ugly but working way to make the server behave a little more like the client for our use case.
         
         This still uses the normal JSON files, but it reads them here before the server has a chance to behave
         differently.
        */
        if(FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            return;
        }
    
        try {
            registry.add(
                AETHER_OPTIONS_KEY,
                MiscUtil.deserializeDataJson(
                    RegistryOps.of(JsonOps.INSTANCE, registryManager),
                    DimensionOptions.CODEC,
                    locate("dimension/the_aether")
                ),
                Lifecycle.stable()
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to read dimension options", e);
        }
    }
}
