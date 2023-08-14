package net.id.paradiselost.world.dimension;

import com.mojang.serialization.JsonOps;
import com.mojang.serialization.Lifecycle;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.id.paradiselost.config.ParadiseLostConfig;
import net.id.paradiselost.util.ParadiseLostSoundEvents;
import net.id.paradiselost.util.MiscUtil;
import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.kyrptonaught.customportalapi.event.CPASoundEventData;
import net.minecraft.util.dynamic.RegistryOps;
import net.minecraft.util.registry.*;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.FlatLevelGeneratorPreset;

import java.io.IOException;

import static net.id.paradiselost.ParadiseLost.MOD_ID;
import static net.id.paradiselost.ParadiseLost.locate;

public class ParadiseLostDimension {
    public static final RegistryKey<World> PARADISE_LOST_WORLD_KEY = key(Registry.WORLD_KEY, MOD_ID);
    public static final RegistryKey<DimensionType> DIMENSION_TYPE = key(Registry.DIMENSION_TYPE_KEY, MOD_ID);
    public static final RegistryKey<DimensionOptions> OPTIONS_KEY = key(Registry.DIMENSION_KEY, MOD_ID);
    public static final RegistryKey<FlatLevelGeneratorPreset> SUPERFLAT_PRESET = key(Registry.FLAT_LEVEL_GENERATOR_PRESET_KEY, MOD_ID);
    
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
				.frameBlock(ParadiseLostConfig.CONFIG.getPortalBaseBlock(Registry.BLOCK))
				.customPortalBlock(ParadiseLostBlocks.BLUE_PORTAL)
				.destDimID(locate(MOD_ID))
				.tintColor(55, 89, 195)
				.lightWithWater()
				.onlyLightInOverworld()
				.registerInPortalAmbienceSound(player -> new CPASoundEventData(ParadiseLostSoundEvents.BLOCK_PORTAL_TRIGGER, player.getRandom().nextFloat() * 0.4F + 0.8F, 0.25F))
				.registerPostTPPortalAmbience(player -> new CPASoundEventData(ParadiseLostSoundEvents.BLOCK_PORTAL_TRAVEL, player.getRandom().nextFloat() * 0.4F + 0.8F, 0.25F))
				.registerPortal();
    }
    
    public static void registerDimensionTypes(MutableRegistry<DimensionType> registry) {
        registry.add(DIMENSION_TYPE, dimensionType, Lifecycle.stable());
    }
    
    // FIXME This needs to be called again, or replaced.
    public static void registerDefaultOptions(DynamicRegistryManager registryManager, SimpleRegistry<DimensionOptions> registry, long seed, boolean useInstance) {
        /*
        Yes this is awful, no I don't care. It works. Blame Mojang.
        Rational:
         They had the super big brain idea to make the server and client datapack logic function differently. This is an
         ugly but working way to make the server behave a little more like the client for our use case.
         
         This still uses the normal JSON files, but it reads them here before the server has a chance to behave
         differently.
        */
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            return;
        }
    
        try {
            registry.add(
                    OPTIONS_KEY,
                    MiscUtil.deserializeDataJson(
                        RegistryOps.of(JsonOps.INSTANCE, registryManager),
                        DimensionOptions.CODEC,
                        locate("dimension/paradise_lost")
                    ),
                    Lifecycle.stable()
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to read dimension options", e);
        }
    }
}
