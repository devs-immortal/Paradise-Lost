package net.id.aether.world.dimension;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Lifecycle;
import net.id.aether.blocks.AetherBlocks;
import net.id.aether.mixin.server.DensityFunctionsAccessor;
import net.id.aether.util.AetherSoundEvents;
import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.kyrptonaught.customportalapi.util.CPASoundEventData;
import net.minecraft.block.Blocks;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.Spline;
import net.minecraft.util.math.VerticalSurfaceType;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.util.registry.*;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.biome.source.util.VanillaTerrainParameters;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.*;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;
import net.minecraft.world.gen.noise.NoiseParametersKeys;
import net.minecraft.world.gen.noise.SimpleNoiseRouter;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;

import java.util.OptionalLong;
import java.util.function.BiConsumer;

import static net.id.aether.Aether.MOD_ID;
import static net.id.aether.Aether.locate;
import static net.minecraft.world.dimension.DimensionType.OVERWORLD_ID;

public class AetherDimension {
    public static final RegistryKey<World> AETHER_WORLD_KEY = RegistryKey.of(Registry.WORLD_KEY, locate(MOD_ID));
    public static final RegistryKey<DimensionType> DIMENSION_TYPE_KEY = RegistryKey.of(Registry.DIMENSION_TYPE_KEY, locate(MOD_ID));
    public static final RegistryKey<DimensionOptions> AETHER_OPTIONS_KEY = RegistryKey.of(Registry.DIMENSION_KEY, locate(MOD_ID));
    public static final RegistryKey<ChunkGeneratorSettings> AETHER_CHUNK_GENERATOR_SETTINGS_KEY = RegistryKey.of(Registry.CHUNK_GENERATOR_SETTINGS_KEY, locate("aether_noise"));
    
    public static final RegistryKey<DoublePerlinNoiseSampler.NoiseParameters> AETHER_FOREST_NOISE_KEY = RegistryKey.of(Registry.NOISE_WORLDGEN, locate("aether_forest_noise"));
    public static final RegistryKey<DoublePerlinNoiseSampler.NoiseParameters> AETHER_DIRT_LAYER_NOISE_KEY = RegistryKey.of(Registry.NOISE_WORLDGEN, locate("aether_forest_noise"));
    
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
    
    /*
    FIXME
    I could not figure out where this part of aether_noise.json goes.
    {
      "structures": {
        "structures": {
          "the_aether:well": {
            "spacing": 32,
            "separation": 8,
            "salt": 42069
          },
          "the_aether:skyroot_tower": {
            "spacing": 48,
            "separation": 10,
            "salt": 123123
          }
        }
      }
    }
     */
    
    private static DensityFunction densityFunctionShiftX() {
        return DensityFunctionsAccessor.invokeMethod_41116(DensityFunctionsAccessor.getShiftX());
    }
    
    private static DensityFunction densityFunctionShiftZ() {
        return DensityFunctionsAccessor.invokeMethod_41116(DensityFunctionsAccessor.getShiftZ());
    }
    
    private static DensityFunction densityFunctionY() {
        return DensityFunctionsAccessor.invokeMethod_41116(DensityFunctionsAccessor.getY());
    }
    
    public static void createChunkGeneratorSettings(BiConsumer<RegistryKey<ChunkGeneratorSettings>, ChunkGeneratorSettings> register) {
        var generationShapeConfig = GenerationShapeConfig.create(
            -16,
            192,
            new NoiseSamplingConfig(3.5, 1.1, 400, 120),
            new SlideConfig(-0.154, 28, 2/*.2*/),
            new SlideConfig(-0.375, 32, 1/*.3*/),
            2/*.5*/,
            1,
            new VanillaTerrainParameters(Spline.fixedFloatFunction(0.15F), Spline.fixedFloatFunction(0.175F), Spline.fixedFloatFunction(0.075F))
        );
        
        register.accept(
            AETHER_CHUNK_GENERATOR_SETTINGS_KEY,
            new ChunkGeneratorSettings(
                generationShapeConfig,
                AetherBlocks.HOLYSTONE.getDefaultState(),
                Blocks.WATER.getDefaultState().with(Properties.LEVEL_15, 8),
                new SimpleNoiseRouter(
                    DensityFunctionTypes.noise(
                        BuiltinRegistries.NOISE_PARAMETERS.entryOf(NoiseParametersKeys.AQUIFER_BARRIER),
                        1, 0.5
                    ),
                    DensityFunctionTypes.noise(
                        BuiltinRegistries.NOISE_PARAMETERS.entryOf(NoiseParametersKeys.AQUIFER_FLUID_LEVEL_FLOODEDNESS),
                        1, 0.67
                    ),
                    DensityFunctionTypes.noise(
                        BuiltinRegistries.NOISE_PARAMETERS.entryOf(NoiseParametersKeys.AQUIFER_FLUID_LEVEL_SPREAD),
                        1, 0.7142857142857143
                    ),
                    DensityFunctionTypes.noise(
                        BuiltinRegistries.NOISE_PARAMETERS.entryOf(NoiseParametersKeys.AQUIFER_LAVA),
                        1, 1
                    ),
                    DensityFunctionTypes.shiftedNoise(
                        densityFunctionShiftX(),
                        densityFunctionShiftZ(),
                        0.25,
                        BuiltinRegistries.NOISE_PARAMETERS.entryOf(NoiseParametersKeys.TEMPERATURE)
                    ),
                    DensityFunctionTypes.shiftedNoise(
                        densityFunctionShiftX(),
                        densityFunctionShiftZ(),
                        0.25,
                        BuiltinRegistries.NOISE_PARAMETERS.entryOf(NoiseParametersKeys.VEGETATION)
                    ),
                    DensityFunctionsAccessor.invokeMethod_41116(DensityFunctionsAccessor.getCONTINENTS_OVERWORLD()),
                    DensityFunctionsAccessor.invokeMethod_41116(DensityFunctionsAccessor.getEROSION_OVERWORLD()),
                    DensityFunctionsAccessor.invokeMethod_41116(DensityFunctionsAccessor.getDEPTH_OVERWORLD()),
                    DensityFunctionsAccessor.invokeMethod_41116(DensityFunctionsAccessor.getRIDGES_OVERWORLD()),
                    DensityFunctionTypes.method_40500/*MUL*/(
                        DensityFunctionTypes.constant(4),
                        DensityFunctionTypes.method_40500/*MUL*/(
                            DensityFunctionsAccessor.invokeMethod_41116(DensityFunctionsAccessor.getDEPTH_OVERWORLD()),
                            DensityFunctionTypes.cache2d(
                                DensityFunctionsAccessor.invokeMethod_41116(DensityFunctionsAccessor.getFACTOR_OVERWORLD())
                            )
                        ).quarterNegative()
                    ),
                    DensityFunctionTypes.method_40505/*MIN*/(
                        DensityFunctionTypes.method_40500/*MUL*/(
                            DensityFunctionTypes.constant(0.64),
                            DensityFunctionTypes.interpolated(
                                DensityFunctionTypes.blendDensity(
                                    DensityFunctionTypes.slide(
                                        null,
                                        DensityFunctionTypes.rangeChoice(
                                            DensityFunctionsAccessor.invokeMethod_41116(DensityFunctionsAccessor.getSLOPED_CHEESE_OVERWORLD()),
                                            -1000000,
                                            1.5625,
                                            DensityFunctionTypes.method_40505/*MIN*/(
                                                DensityFunctionsAccessor.invokeMethod_41116(DensityFunctionsAccessor.getSLOPED_CHEESE_OVERWORLD()),
                                                DensityFunctionTypes.method_40505(
                                                    DensityFunctionTypes.constant(5),
                                                    DensityFunctionsAccessor.invokeMethod_41116(DensityFunctionsAccessor.getCAVES_ENTRANCES_OVERWORLD())
                                                )
                                            ),
                                            DensityFunctionTypes.method_40508/*MAX*/(
                                                DensityFunctionTypes.method_40505/*MIN*/(
                                                    DensityFunctionTypes.method_40505/*MIN*/(
                                                        DensityFunctionTypes.method_40486/*ADD*/(
                                                            DensityFunctionTypes.method_40500/*MUL*/(
                                                                DensityFunctionTypes.constant(4),
                                                                DensityFunctionTypes.noise(
                                                                    BuiltinRegistries.NOISE_PARAMETERS.entryOf(NoiseParametersKeys.CAVE_LAYER),
                                                                    1, 8
                                                                ).square()
                                                            ),
                                                            DensityFunctionTypes.method_40486/*ADD*/(
                                                                DensityFunctionTypes.method_40486/*ADD*/(
                                                                    DensityFunctionTypes.constant(0.27),
                                                                    DensityFunctionTypes.noise(
                                                                        BuiltinRegistries.NOISE_PARAMETERS.entryOf(NoiseParametersKeys.CAVE_CHEESE),
                                                                        1, 0.6666666666666666
                                                                    )
                                                                ).clamp(-1, 1),
                                                                DensityFunctionTypes.method_40486/*ADD*/(
                                                                    DensityFunctionTypes.constant(1.5),
                                                                    DensityFunctionTypes.method_40500/*MUL*/(
                                                                        DensityFunctionTypes.constant(-0.64),
                                                                        DensityFunctionsAccessor.invokeMethod_41116(DensityFunctionsAccessor.getSLOPED_CHEESE_OVERWORLD())
                                                                    )
                                                                ).clamp(0, 0.5)
                                                            )
                                                        ),
                                                        DensityFunctionsAccessor.invokeMethod_41116(DensityFunctionsAccessor.getCAVES_ENTRANCES_OVERWORLD())
                                                    ),
                                                    DensityFunctionTypes.method_40486/*ADD*/(
                                                        DensityFunctionsAccessor.invokeMethod_41116(DensityFunctionsAccessor.getCAVES_SPAGHETTI_2D_OVERWORLD()),
                                                        DensityFunctionsAccessor.invokeMethod_41116(DensityFunctionsAccessor.getCAVES_SPAGHETTI_ROUGHNESS_FUNCTION_OVERWORLD())
                                                    )
                                                ),
                                                DensityFunctionTypes.rangeChoice(
                                                    DensityFunctionsAccessor.invokeMethod_41116(DensityFunctionsAccessor.getCAVES_PILLARS_OVERWORLD()),
                                                    -1000000,
                                                    0.03,
                                                    DensityFunctionTypes.constant(-1000000),
                                                    DensityFunctionsAccessor.invokeMethod_41116(DensityFunctionsAccessor.getCAVES_PILLARS_OVERWORLD())
                                                )
                                            )
                                        )
                                    )
                                )
                            )
                        ).squeeze(),
                        DensityFunctionsAccessor.invokeMethod_41116(DensityFunctionsAccessor.getCAVES_NOODLE_OVERWORLD())
                    ),
                    DensityFunctionTypes.interpolated(DensityFunctionTypes.rangeChoice(
                        densityFunctionY(),
                        -60,
                        51,
                        DensityFunctionTypes.noise(
                            BuiltinRegistries.NOISE_PARAMETERS.entryOf(NoiseParametersKeys.ORE_VEININESS),
                            1.5, 1.5
                        ),
                        DensityFunctionTypes.zero()
                    )),
                    DensityFunctionTypes.method_40486/*ADD*/(
                        DensityFunctionTypes.constant(-0.07999999821186066),
                        DensityFunctionTypes.method_40508/*MAX*/(
                            DensityFunctionTypes.interpolated(
                                DensityFunctionTypes.rangeChoice(
                                    densityFunctionY(),
                                    -60,
                                    51,
                                    DensityFunctionTypes.noise(
                                        BuiltinRegistries.NOISE_PARAMETERS.entryOf(NoiseParametersKeys.ORE_VEIN_A),
                                        4, 4
                                    ),
                                    DensityFunctionTypes.zero()
                                )
                            ).abs(),
                            DensityFunctionTypes.interpolated(
                                DensityFunctionTypes.rangeChoice(
                                    densityFunctionY(),
                                    -60,
                                    51,
                                    DensityFunctionTypes.noise(
                                        BuiltinRegistries.NOISE_PARAMETERS.entryOf(NoiseParametersKeys.ORE_VEIN_B),
                                        4, 4
                                    ),
                                    DensityFunctionTypes.zero()
                                )
                            ).abs()
                        )
                    ),
                    DensityFunctionTypes.noise(
                        BuiltinRegistries.NOISE_PARAMETERS.entryOf(NoiseParametersKeys.ORE_GAP),
                        1, 1
                    )
                ),
                createAetherSurfaceRule(),
                -16,
                false,
                false,
                false,
                true
            )
        );
    }
    
    private static MaterialRules.MaterialRule createAetherSurfaceRule() {
        return MaterialRules.sequence(
            MaterialRules.condition(
                MaterialRules.stoneDepth(
                    0,
                    false,
                    0,
                    VerticalSurfaceType.FLOOR
                ),
                MaterialRules.sequence(
                    MaterialRules.condition(
                        MaterialRules.biome(AetherBiomes.HIGHLANDS_FOREST_KEY),
                        MaterialRules.condition(
                            MaterialRules.noiseThreshold(
                                AETHER_FOREST_NOISE_KEY,
                                0,
                                0.5
                            ),
                            MaterialRules.block(AetherBlocks.COARSE_AETHER_DIRT.getDefaultState())
                        )
                    )
                )
            ),
            MaterialRules.condition(
                MaterialRules.water(-1, 0),
                MaterialRules.sequence(
                    MaterialRules.condition(
                        MaterialRules.stoneDepth(
                            0,
                            false,
                            0,
                            VerticalSurfaceType.FLOOR
                        ),
                        MaterialRules.sequence(
                            MaterialRules.condition(
                                MaterialRules.biome(AetherBiomes.AUTUMNAL_TUNDRA_KEY),
                                MaterialRules.block(AetherBlocks.AETHER_FROZEN_GRASS.getDefaultState())
                            ),
                            MaterialRules.block(AetherBlocks.AETHER_GRASS.getDefaultState())
                        )
                    ),
                    MaterialRules.condition(
                        MaterialRules.stoneDepth(
                            2,
                            false,
                            0,
                            VerticalSurfaceType.FLOOR
                        ),
                        MaterialRules.sequence(
                            MaterialRules.condition(
                                MaterialRules.biome(AetherBiomes.AUTUMNAL_TUNDRA_KEY),
                                MaterialRules.block(AetherBlocks.PERMAFROST.getDefaultState())
                            ),
                            MaterialRules.block(AetherBlocks.AETHER_DIRT.getDefaultState())
                        )
                    ),
                    MaterialRules.condition(
                        MaterialRules.noiseThreshold(
                            AETHER_DIRT_LAYER_NOISE_KEY,
                            0,
                            1
                        ),
                        MaterialRules.condition(
                            MaterialRules.stoneDepth(
                                3,
                                false,
                                0,
                                VerticalSurfaceType.FLOOR
                            ),
                            MaterialRules.sequence(
                                MaterialRules.condition(
                                    MaterialRules.biome(AetherBiomes.AUTUMNAL_TUNDRA_KEY),
                                    MaterialRules.block(AetherBlocks.PERMAFROST.getDefaultState())
                                ),
                                MaterialRules.block(AetherBlocks.AETHER_DIRT.getDefaultState())
                            )
                        )
                    )
                )
            ),
            MaterialRules.condition(
                MaterialRules.not(MaterialRules.water(-1, 0)),
                MaterialRules.sequence(
                    MaterialRules.condition(
                        MaterialRules.stoneDepth(0, false, 0, VerticalSurfaceType.FLOOR),
                        MaterialRules.sequence(
                            MaterialRules.condition(
                                MaterialRules.biome(AetherBiomes.AUTUMNAL_TUNDRA_KEY),
                                MaterialRules.block(AetherBlocks.COBBLED_HOLYSTONE.getDefaultState())
                            ),
                            MaterialRules.block(AetherBlocks.AETHER_DIRT.getDefaultState())
                        )
                    )
                )
            )
        );
    }
}
