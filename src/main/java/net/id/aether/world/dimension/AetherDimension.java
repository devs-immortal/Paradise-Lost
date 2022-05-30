package net.id.aether.world.dimension;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Lifecycle;
import net.id.aether.blocks.AetherBlocks;
import net.id.aether.util.AetherSoundEvents;
import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.kyrptonaught.customportalapi.util.CPASoundEventData;
import net.minecraft.block.Blocks;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.*;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;

import java.util.OptionalLong;

import static net.id.aether.Aether.MOD_ID;
import static net.id.aether.Aether.locate;
import static net.minecraft.world.dimension.DimensionType.OVERWORLD_ID;

public class AetherDimension {
    public static final RegistryKey<World> AETHER_WORLD_KEY = RegistryKey.of(Registry.WORLD_KEY, locate(MOD_ID));
    public static final RegistryKey<DimensionType> DIMENSION_TYPE_KEY = RegistryKey.of(Registry.DIMENSION_TYPE_KEY, locate(MOD_ID));
    public static final RegistryKey<DimensionOptions> AETHER_OPTIONS_KEY = RegistryKey.of(Registry.DIMENSION_KEY, locate(MOD_ID));
    public static final RegistryKey<ChunkGeneratorSettings> AETHER_CHUNK_GENERATOR_SETTINGS_KEY = RegistryKey.of(Registry.CHUNK_GENERATOR_SETTINGS_KEY, locate("aether_noise"));
    
    private static final DimensionType DIMENSION_TYPE = DimensionType.create(OptionalLong.empty(), true, false, false, true, 10, false, false, true, false, false, -64, 384, 384, BlockTags.INFINIBURN_OVERWORLD, OVERWORLD_ID, 0.06F);
    
    private static final MultiNoiseBiomeSource.Preset AETHER_PRESET = new MultiNoiseBiomeSource.Preset(
        locate(MOD_ID),
        (registry)->new MultiNoiseUtil.Entries<>(ImmutableList.of(
            Pair.of(
                createNoiseHyperCube(0, 0, 0, 1, 0.2F, 1, 0, 0, 0),
                registry.getOrCreateEntry(AetherBiomes.HIGHLANDS_PLAINS_KEY)
            ),
            Pair.of(
                createNoiseHyperCube(-0.15F, 0.1F, -0.8F, 0.5F, -1, 1, 0, 0, 0),
                registry.getOrCreateEntry(AetherBiomes.HIGHLANDS_SHIELD_KEY)
            ),
            Pair.of(
                createNoiseHyperCube(-0.25F, 0.02F, 0.8F, 1, -0.2F, 0.96F, 0, 0, 0),
                registry.getOrCreateEntry(AetherBiomes.CONTINENTAL_PLATEAU_KEY)
            ),
            // Autumnal Tundra is special, adding a special case overload would be a little silly
            Pair.of(
                MultiNoiseUtil.createNoiseHypercube(
                    // Temperature
                    MultiNoiseUtil.ParameterRange.of(-0.35F),
                    // Humidity
                    MultiNoiseUtil.ParameterRange.of(0.075F),
                    // Continetalness
                    MultiNoiseUtil.ParameterRange.of(0.84F),
                    // Erosion
                    MultiNoiseUtil.ParameterRange.of(-0.23F, 0.86F),
                    // Depth
                    MultiNoiseUtil.ParameterRange.of(0),
                    // Weirdness
                    MultiNoiseUtil.ParameterRange.of(0),
                    // Offset
                    0
                ),
                registry.getOrCreateEntry(AetherBiomes.AUTUMNAL_TUNDRA_KEY)
            ),
            Pair.of(
                createNoiseHyperCube(0.35F, -0.05F, -1, 1, -1, 1, 0, 0, 0.05F),
                registry.getOrCreateEntry(AetherBiomes.HIGHLANDS_THICKET_KEY)
            ),
            Pair.of(
                createNoiseHyperCube(0.225F, -0.015F, -1, 0.23F, -1, 1, 0, 0, 0),
                registry.getOrCreateEntry(AetherBiomes.HIGHLANDS_FOREST_KEY)
            ),
            Pair.of(
                createNoiseHyperCube(0.1F, 0, -0.4F, 0.82F, -1, 1, 0, 0, 0.125F),
                registry.getOrCreateEntry(AetherBiomes.WISTERIA_WOODS_KEY)
            )
        ))
    );
    
    public static void init() {
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
        registry.add(DIMENSION_TYPE_KEY, DIMENSION_TYPE, Lifecycle.stable());
    }
    
    private static MultiNoiseUtil.NoiseHypercube createNoiseHyperCube(float temperature, float humidity, float continentalnessMin, float continentalnessMax, float erosionMin, float erosionMax, float depth, float weirdness, float offset) {
        return MultiNoiseUtil.createNoiseHypercube(
            MultiNoiseUtil.ParameterRange.of(temperature),
            MultiNoiseUtil.ParameterRange.of(humidity),
            MultiNoiseUtil.ParameterRange.of(continentalnessMin, continentalnessMax),
            MultiNoiseUtil.ParameterRange.of(erosionMin, erosionMax),
            MultiNoiseUtil.ParameterRange.of(depth),
            MultiNoiseUtil.ParameterRange.of(weirdness),
            offset
        );
    }
    
    @SuppressWarnings("UnnecessaryParentheses")
    public static void registerDefaultOptions(DynamicRegistryManager registryManager, SimpleRegistry<DimensionOptions> dimRegistry, long seed, boolean useInstance) {
        var dimensionTypeRegistry = registryManager.get(Registry.DIMENSION_TYPE_KEY);
        var biomeRegistry = registryManager.get(Registry.BIOME_KEY);
        var structureSetRegistry = registryManager.get(Registry.STRUCTURE_SET_KEY);
        var chunkGeneratorSettingsRegistry = registryManager.get(Registry.CHUNK_GENERATOR_SETTINGS_KEY);
        var noiseWorldgenRegistry = registryManager.get(Registry.NOISE_WORLDGEN);
        
        dimRegistry.add(
            AETHER_OPTIONS_KEY,
            new DimensionOptions(
                dimensionTypeRegistry.getOrCreateEntry(DIMENSION_TYPE_KEY),
                new NoiseChunkGenerator(
                    structureSetRegistry,
                    noiseWorldgenRegistry,
                    AETHER_PRESET.getBiomeSource(biomeRegistry, useInstance),
                    seed,
                    chunkGeneratorSettingsRegistry.getOrCreateEntry(AETHER_CHUNK_GENERATOR_SETTINGS_KEY)
                )
            ),
            Lifecycle.stable()
        );
    }
}
