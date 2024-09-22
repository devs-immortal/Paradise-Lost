package net.id.paradiselost.world.dimension;

import com.mojang.serialization.JsonOps;
import com.mojang.serialization.Lifecycle;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.id.paradiselost.util.ParadiseLostSoundEvents;
import net.id.paradiselost.util.MiscUtil;
import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.kyrptonaught.customportalapi.event.CPASoundEventData;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.MutableRegistry;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.FlatLevelGeneratorPreset;

import java.io.IOException;

import static net.id.paradiselost.ParadiseLost.MOD_ID;
import static net.id.paradiselost.ParadiseLost.locate;

public class ParadiseLostDimension {
    public static final RegistryKey<World> PARADISE_LOST_WORLD_KEY = key(RegistryKeys.WORLD, MOD_ID);
    public static final RegistryKey<DimensionType> DIMENSION_TYPE = key(RegistryKeys.DIMENSION_TYPE, MOD_ID);
    public static final RegistryKey<DimensionOptions> OPTIONS_KEY = key(RegistryKeys.DIMENSION, MOD_ID);
    public static final RegistryKey<FlatLevelGeneratorPreset> SUPERFLAT_PRESET = key(RegistryKeys.FLAT_LEVEL_GENERATOR_PRESET, MOD_ID);
    
    private static DimensionType dimensionType;
    
    private static <T> RegistryKey<T> key(RegistryKey<? extends Registry<T>> registry, String name) {
        return RegistryKey.of(registry, locate(name));
    }
    
    public static void init() {
        try {
            dimensionType = MiscUtil.deserializeDataJson(DimensionType.CODEC, locate("dimension_type/paradise_lost"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read Paradise Lost dimension JSONs", e);
        }
    }

    public static void initPortal() {
		CustomPortalBuilder.beginPortal()
				.frameBlock(ParadiseLostBlocks.BLOOMED_CALCITE)
				.customPortalBlock(ParadiseLostBlocks.BLUE_PORTAL)
				.destDimID(locate(MOD_ID))
				.tintColor(55, 89, 195)
				.lightWithWater()
				.onlyLightInOverworld()
				.registerInPortalAmbienceSound(player -> new CPASoundEventData(ParadiseLostSoundEvents.BLOCK_PORTAL_TRIGGER, player.getRandom().nextFloat() * 0.4F + 0.8F, 0.25F))
				.registerPostTPPortalAmbience(player -> new CPASoundEventData(ParadiseLostSoundEvents.BLOCK_PORTAL_TRAVEL, player.getRandom().nextFloat() * 0.4F + 0.8F, 0.25F))
				.registerPortal();
    }
}
