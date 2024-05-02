package net.id.paradiselost.world.dimension;

import net.id.paradiselost.entities.ParadiseLostEntityTypes;
import net.id.paradiselost.world.gen.carver.ParadiseLostCarvers;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.carver.ConfiguredCarvers;
import net.minecraft.world.gen.feature.MiscPlacedFeatures;
import net.minecraft.world.gen.feature.PlacedFeature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.id.paradiselost.ParadiseLost.locate;
import static net.id.paradiselost.world.feature.placed_features.ParadiseLostMiscPlacedFeatures.*;
import static net.id.paradiselost.world.feature.placed_features.ParadiseLostTreePlacedFeatures.*;
import static net.id.paradiselost.world.feature.placed_features.ParadiseLostVegetationPlacedFeatures.*;

/**
 * Generates the Paradise Lost biome instances and registers them with Minecraft. Things in here don't have to be particularly
 * speedy because they are only done once.
 *
 * 1.19 update notes:
 * - Biome categories appear to be gone, verify that they have not been replaced.
 *
 * @author Gudenau
 */
public final class ParadiseLostBiomes {
    public static final RegistryKey<Biome> AUTUMNAL_TUNDRA_KEY = register("autumnal_tundra");
    public static final RegistryKey<Biome> CONTINENTAL_PLATEAU_KEY = register("continental_plateau");
    public static final RegistryKey<Biome> HIGHLANDS_PLAINS_KEY = register("highlands");
    public static final RegistryKey<Biome> HIGHLANDS_FOREST_KEY = register("highlands_forest");
    public static final RegistryKey<Biome> HIGHLANDS_GRAND_GLADE_KEY = register("highlands_grand_glade");
    public static final RegistryKey<Biome> HIGHLANDS_SHIELD_KEY = register("highlands_shield");
    public static final RegistryKey<Biome> HIGHLANDS_THICKET_KEY = register("highlands_thicket");
    public static final RegistryKey<Biome> TRADEWINDS_KEY = register("tradewinds");
    public static final RegistryKey<Biome> WISTERIA_WOODS_KEY = register("wisteria_woods");

    public static final RegistryEntry<Biome> AUTUMNAL_TUNDRA;
    public static final RegistryEntry<Biome> CONTINENTAL_PLATEAU;
    public static final RegistryEntry<Biome> HIGHLANDS_PLAINS;
    public static final RegistryEntry<Biome> HIGHLANDS_FOREST;
    public static final RegistryEntry<Biome> HIGHLANDS_GRAND_GLADE;
    public static final RegistryEntry<Biome> HIGHLANDS_SHIELD;
    public static final RegistryEntry<Biome> HIGHLANDS_THICKET;
    public static final RegistryEntry<Biome> TRADEWINDS;
    public static final RegistryEntry<Biome> WISTERIA_WOODS;

    static {
        AUTUMNAL_TUNDRA = register(AUTUMNAL_TUNDRA_KEY, createAutumnalTundra());
        CONTINENTAL_PLATEAU = register(CONTINENTAL_PLATEAU_KEY, createContinentalPlateau());
        HIGHLANDS_PLAINS = register(HIGHLANDS_PLAINS_KEY, createHighlandsPlains());
        HIGHLANDS_FOREST = register(HIGHLANDS_FOREST_KEY, createHighlandsForest());
        HIGHLANDS_GRAND_GLADE = register(HIGHLANDS_GRAND_GLADE_KEY, createHighlandsGrandGlade());
        HIGHLANDS_SHIELD = register(HIGHLANDS_SHIELD_KEY, createHighlandsShield());
        HIGHLANDS_THICKET = register(HIGHLANDS_THICKET_KEY, createHighlandsThicket());
        TRADEWINDS = register(TRADEWINDS_KEY, createTradewinds());
        WISTERIA_WOODS = register(WISTERIA_WOODS_KEY, createWisteriaWoods());
    }
    
    public static void init() {
    }
    
    private static RegistryKey<Biome> register(String name) {
        return RegistryKey.of(Registry.BIOME_KEY, locate(name));
    }
    
    private static RegistryEntry<Biome> register(RegistryKey<Biome> key, Biome biome) {
        return BuiltinRegistries.add(BuiltinRegistries.BIOME, key, biome);
    }
    
    /**
     * Creates a new {@link BiomeEffects} instance from the provided colors.
     *
     * @param skyColor The color of the sky
     * @param foliageColor The color of foliage (I.E. leaves)
     * @param grassColor The color of grass
     * @param fogColor The color of fog
     * @param waterColor The color of water
     * @param waterFogColor The color of fog while in water
     * @param grassModifier Modifies grass colors
     * @return The new {@link BiomeEffects} instance
     */
    private static BiomeEffects createBiomeEffects(int skyColor, int foliageColor, int grassColor, int fogColor, int waterColor, int waterFogColor, BiomeEffects.GrassColorModifier grassModifier) {
        return new BiomeEffects.Builder()
            .skyColor(skyColor)
            .foliageColor(foliageColor)
            .grassColor(grassColor)
            .fogColor(fogColor)
            .waterColor(waterColor)
            .waterFogColor(waterFogColor)
            .grassColorModifier(grassModifier)
            .build();
    }
    
    /**
     * Creates a new {@link BiomeEffects} instance from the provided colors.
     *
     * @param skyColor The color of the sky
     * @param foliageColor The color of foliage (I.E. leaves)
     * @param grassColor The color of grass
     * @param fogColor The color of fog
     * @param waterColor The color of water
     * @param waterFogColor The color of fog while in water
     * @return The new {@link BiomeEffects} instance
     */
    private static BiomeEffects createBiomeEffects(int skyColor, int foliageColor, int grassColor, int fogColor, int waterColor, int waterFogColor) {
        return createBiomeEffects(skyColor, foliageColor, grassColor, fogColor, waterColor, waterFogColor, BiomeEffects.GrassColorModifier.NONE);
    }
    
    /**
     * Creates a new {@link GenerationSettings} instance from the provided carvers and features.
     *
     * @param carvers The carvers to use
     * @param features The features to use
     * @return The new {@link GenerationSettings} instance
     */
    private static GenerationSettings createGenerationSettings(Map<GenerationStep.Carver, List<RegistryEntry<? extends ConfiguredCarver<?>>>> carvers, Map<GenerationStep.Feature, List<RegistryEntry<PlacedFeature>>> features) {
        var builder = new GenerationSettings.Builder();
        for (var step : GenerationStep.Carver.values()) {
            for (var carver : carvers.getOrDefault(step, List.of())) {
                builder.carver(step, carver);
            }
        }
        for (var step : GenerationStep.Feature.values()) {
            for (var feature : features.getOrDefault(step, List.of())) {
                builder.feature(step, feature);
            }
        }
        return builder.build();
    }
    
    /**
     * Creates entity {@link SpawnSettings} from provided maps. Intended to be used with the collection classes static
     * of methods.
     *
     * @param spawns The entities to spawn
     * @param costs The cost to spawn some entities
     * @return The created {@link SpawnSettings}
     */
    private static SpawnSettings createSpawnSettings(Map<SpawnGroup, List<SpawnSettings.SpawnEntry>> spawns, Map<EntityType<?>, SpawnCost> costs) {
        var builder = new SpawnSettings.Builder();
        for (var group : SpawnGroup.values()) {
            for (var spawn : spawns.getOrDefault(group, List.of())) {
                builder.spawn(group, spawn);
            }
        }
        for (var entry : costs.entrySet()) {
            var cost = entry.getValue();
            builder.spawnCost(entry.getKey(), cost.charge(), cost.energyBudget());
        }
        return builder.build();
    }
    
    /**
     * Merges two or more feature maps together.
     *
     * @param maps The maps to merge
     * @param <A> The type of the map keys
     * @param <B> The type of the map lists
     * @return A map that contains all features
     */
    @SafeVarargs
    public static <A, B> Map<A, List<B>> merge(Map<A, List<B>>... maps) {
        if (maps.length == 1) {
            return maps[0];
        }
        
        Map<A, List<B>> result = new HashMap<>();
        for (var map : maps) {
            for (Map.Entry<A, List<B>> entry : map.entrySet()) {
                result.computeIfAbsent(entry.getKey(), (ignored) -> new ArrayList<>()).addAll(entry.getValue());
            }
        }
        return result;
    }
    
    /**
     * Creates a {@link Map} suitable for use with {@link #createGenerationSettings} that contains all of the default
     * Paradise Lost carvers.
     *
     * @return The default Paradise Lost carvers
     */
    private static Map<GenerationStep.Carver, List<RegistryEntry<? extends ConfiguredCarver<?>>>> getParadiseLostCarvers() {
        return Map.of(GenerationStep.Carver.AIR, List.of(
            ConfiguredCarvers.CAVE,
            ParadiseLostCarvers.LARGE_COLD_CLOUD_CARVER,
            ParadiseLostCarvers.COLD_CLOUD_CARVER,
            ParadiseLostCarvers.TINY_COLD_CLOUD_CARVER,
            ParadiseLostCarvers.LARGE_BLUE_CLOUD_CARVER,
            ParadiseLostCarvers.BLUE_CLOUD_CARVER,
            ParadiseLostCarvers.TINY_BLUE_CLOUD_CARVER,
            ParadiseLostCarvers.LARGE_GOLDEN_CLOUD_CARVER,
            ParadiseLostCarvers.GOLDEN_CLOUD_CARVER,
            ParadiseLostCarvers.TINY_GOLDEN_CLOUD_CARVER
        ));
    }
    
    /**
     * Returns a map of common placed features in all of the Paradise Lost biomes.
     * 
     * @return A map of features suitable for use with {@link #createGenerationSettings}
     */
    private static Map<GenerationStep.Feature, List<RegistryEntry<PlacedFeature>>> getStandardParadiseLostFeatures() {
        return Map.of(
            GenerationStep.Feature.RAW_GENERATION, List.of(
                SURTRUM_METEORITE
            ),
            GenerationStep.Feature.UNDERGROUND_ORES, List.of(
                HELIOLITH_BLOB,
                LEVITA_BLOB,
                ORE_CHERINE,
                ORE_OLVITE,
                ORE_LEVITA
            ),
            GenerationStep.Feature.VEGETAL_DECORATION, List.of(
                PATCH_BROWN_SPORECAP,
                PATCH_PINK_SPORECAP,
                NATURAL_SWEDROOT
            )
        );
    }
    
    private static Biome createHighlandsPlains() {
        return new Biome.Builder()
            .effects(createBiomeEffects(0xC0C0FF, 0xf1ff99, 0xa9dbc5, 0xFFFFFF, 0x58E5FF, 0x63E7FF))
            .generationSettings(createGenerationSettings(
                getParadiseLostCarvers(),
    
                merge(
                    getStandardParadiseLostFeatures(),
                    Map.of(
                        GenerationStep.Feature.LOCAL_MODIFICATIONS, List.of(
                            PLAINS_BOULDER
                        ),
                        GenerationStep.Feature.FLUID_SPRINGS, List.of(
                            WATER_SPRING
                        ),
                        GenerationStep.Feature.VEGETAL_DECORATION, List.of(
                            FLOWERS,
                            GRASS,
                            TALL_GRASS,
                            PATCH_BLACKCURRANT,
                            SPARSE_TREES
                        )
                    )
                )
            ))
            .spawnSettings(createSpawnSettings(
                merge(
                    Map.of(
                        SpawnGroup.MONSTER, List.of(
                            new SpawnSettings.SpawnEntry(ParadiseLostEntityTypes.ENVOY, 50, 1, 3)
                        ),
                        SpawnGroup.CREATURE, List.of(
                            new SpawnSettings.SpawnEntry(ParadiseLostEntityTypes.PARADISE_HARE, 12, 4, 4),
                            new SpawnSettings.SpawnEntry(ParadiseLostEntityTypes.MOA, 6, 5, 13)
                        )
                    )
                ),
                    Map.of(
                            ParadiseLostEntityTypes.ENVOY, SpawnCost.of(1.0, 0.5)
                    )
            ))
            .precipitation(Biome.Precipitation.RAIN).temperature(0.5F).downfall(1)
            // .category(Biome.Category.PLAINS)
            .build();
    }
    
    private static Biome createHighlandsForest() {
        return new Biome.Builder()
            .effects(createBiomeEffects(0xC0C0FF, 0xf1ff99, 0xa2dbc2, 0xFFFFFF, 0x58E5FF, 0x63E7FF))
            .generationSettings(createGenerationSettings(
                getParadiseLostCarvers(),
    
                merge(
                    getStandardParadiseLostFeatures(),
                    Map.of(
                        GenerationStep.Feature.LOCAL_MODIFICATIONS, List.of(
                        ),
                        GenerationStep.Feature.FLUID_SPRINGS, List.of(
                            WATER_SPRING
                        ),
                        GenerationStep.Feature.VEGETAL_DECORATION, List.of(
                            FLOWERS,
                            TALL_GRASS,
                            TUNDRA_FOLIAGE,
                            BUSH,
                            PATCH_BLACKCURRANT,
                            SCATTERED_TREES,
                            ALT_FALLEN_LEAVES
                        )
                    )
                )
            ))
            .spawnSettings(createSpawnSettings(
                merge(
                    Map.of(
                        SpawnGroup.MONSTER, List.of(
                            new SpawnSettings.SpawnEntry(ParadiseLostEntityTypes.ENVOY, 50, 1, 3)
                        ),
                        SpawnGroup.CREATURE, List.of(
                            new SpawnSettings.SpawnEntry(ParadiseLostEntityTypes.PARADISE_HARE, 12, 4, 4),
                            new SpawnSettings.SpawnEntry(ParadiseLostEntityTypes.MOA, 6, 5, 13)
                        )
                    )
                ),
                    Map.of(
                            ParadiseLostEntityTypes.ENVOY, SpawnCost.of(1.0, 0.5)
                    )
            ))
            .precipitation(Biome.Precipitation.RAIN).temperature(0.5F).downfall(1)
            // .category(Biome.Category.FOREST)
            .build();
    }

    private static Biome createTradewinds() {
        return new Biome.Builder()
                .effects(createBiomeEffects(0xC0C0FF, 0xf1ff99, 0x8dc9af, 0xFFFFFF, 0x58E5FF, 0x63E7FF))
                .generationSettings(createGenerationSettings(
                        getParadiseLostCarvers(),

                        merge(
                                getStandardParadiseLostFeatures(),
                                Map.of(
                                        GenerationStep.Feature.LOCAL_MODIFICATIONS, List.of(
                                        ),
                                        GenerationStep.Feature.FLUID_SPRINGS, List.of(
                                                WATER_SPRING
                                        ),
                                        GenerationStep.Feature.VEGETAL_DECORATION, List.of(
                                                TALL_GRASS,
                                                BUSH,
                                                PATCH_BLACKCURRANT,
                                                PLATEAU_TREES,
                                                SPARSE_TREES
                                        )
                                )
                        )
                ))
                .spawnSettings(createSpawnSettings(
                    merge(
                        Map.of(
                            SpawnGroup.MONSTER, List.of(
                                new SpawnSettings.SpawnEntry(ParadiseLostEntityTypes.ENVOY, 50, 1, 3)
                            ),
                            SpawnGroup.CREATURE, List.of(
                                new SpawnSettings.SpawnEntry(ParadiseLostEntityTypes.PARADISE_HARE, 12, 4, 4),
                                new SpawnSettings.SpawnEntry(ParadiseLostEntityTypes.MOA, 6, 5, 13)
                            )
                        )
                    ),
                        Map.of(
                                ParadiseLostEntityTypes.ENVOY, SpawnCost.of(1.0, 0.5)
                        )
                ))
                .precipitation(Biome.Precipitation.RAIN).temperature(0.5F).downfall(1)
                // .category(Biome.Category.FOREST)
                .build();
    }
    
    private static Biome createHighlandsThicket() {
        return new Biome.Builder()
            .effects(createBiomeEffects(0xC0C0FF, 0xedff7d, 0x739988, 0xFFFFFF, 0x58E5FF, 0x63E7FF))
            .generationSettings(createGenerationSettings(
                getParadiseLostCarvers(),
    
                merge(
                    getStandardParadiseLostFeatures(),
                    Map.of(
                        GenerationStep.Feature.LOCAL_MODIFICATIONS, List.of(
                            THICKET_BOULDER,
                            GOLDEN_BOULDER
                        ),
                        GenerationStep.Feature.FLUID_SPRINGS, List.of(
                            WATER_SPRING
                        ),
                        GenerationStep.Feature.VEGETAL_DECORATION, List.of(
                            THICKET_SHAMROCK,
                            THICKET_LIVERWORT_CARPET,
                            GRASS,
                            FALLEN_LEAVES,
                            TALL_GRASS,
                            DENSE_BUSH,
                            THICKET_TREES,
                            THICKET_MOTHER_AUREL_TREES
                        )
                    )
                )
            ))
            .spawnSettings(createSpawnSettings(
                merge(
                    Map.of(
                        SpawnGroup.MONSTER, List.of(
                            new SpawnSettings.SpawnEntry(ParadiseLostEntityTypes.ENVOY, 50, 1, 3)
                        ),
                        SpawnGroup.CREATURE, List.of(
                            new SpawnSettings.SpawnEntry(ParadiseLostEntityTypes.PARADISE_HARE, 12, 4, 4),
                            new SpawnSettings.SpawnEntry(ParadiseLostEntityTypes.MOA, 6, 5, 13)
                        )
                    )
                ),
                    Map.of(
                            ParadiseLostEntityTypes.ENVOY, SpawnCost.of(1.0, 0.5)
                    )
            ))
            .precipitation(Biome.Precipitation.RAIN).temperature(0.4F).downfall(2)
            // .category(Biome.Category.FOREST)
            .build();
    }

    private static Biome createHighlandsGrandGlade() {
        return new Biome.Builder()
                .effects(createBiomeEffects(0xC0C0FF, 0xedff7d, 0x739988, 0xFFFFFF, 0x58E5FF, 0x63E7FF))
                .generationSettings(createGenerationSettings(
                        getParadiseLostCarvers(),

                        merge(
                                getStandardParadiseLostFeatures(),
                                Map.of(
                                        GenerationStep.Feature.LOCAL_MODIFICATIONS, List.of(
                                                THICKET_BOULDER
                                        ),
                                        GenerationStep.Feature.FLUID_SPRINGS, List.of(
                                                WATER_SPRING
                                        ),
                                        GenerationStep.Feature.VEGETAL_DECORATION, List.of(
                                                THICKET_FALLEN_LOG,
                                                GRASS,
                                                FALLEN_LEAVES,
                                                PATCH_BROWN_SPORECAP_COMMON,
                                                TALL_GRASS,
                                                DENSE_BUSH,
                                                SPARSE_TREES
                                        )
                                )
                        )
                ))
                .spawnSettings(createSpawnSettings(
                    merge(
                        Map.of(
                            SpawnGroup.MONSTER, List.of(
                                new SpawnSettings.SpawnEntry(ParadiseLostEntityTypes.ENVOY, 50, 1, 3)
                            ),
                            SpawnGroup.CREATURE, List.of(
                                new SpawnSettings.SpawnEntry(ParadiseLostEntityTypes.PARADISE_HARE, 12, 4, 4),
                                new SpawnSettings.SpawnEntry(ParadiseLostEntityTypes.MOA, 6, 5, 13)
                            )
                        )
                    ),
                        Map.of(
                                ParadiseLostEntityTypes.ENVOY, SpawnCost.of(1.0, 0.5)
                        )
                ))
                .precipitation(Biome.Precipitation.RAIN).temperature(0.4F).downfall(2)
                // .category(Biome.Category.FOREST)
                .build();
    }
    
    private static Biome createWisteriaWoods() {
        return new Biome.Builder()
            .effects(createBiomeEffects(0xC0C0FF, 0xf1ff99, 0xa5d6c1, 0xFFFFFF, 0x58E5FF, 0x63E7FF))
            .generationSettings(createGenerationSettings(
                getParadiseLostCarvers(),
    
                merge(
                    getStandardParadiseLostFeatures(),
                    Map.of(
                        GenerationStep.Feature.LOCAL_MODIFICATIONS, List.of(
                        ),
                        GenerationStep.Feature.VEGETAL_DECORATION, List.of(
                            RAINBOW_MALT_SPRIGS,
                            FLOWERS,
                            GRASS,
                            FALLEN_ROSE_LEAVES,
                            FALLEN_LAVENDER_LEAVES,
                            BUSH,
                            PATCH_BLACKCURRANT,
                            RAINBOW_FOREST_TREES
                        )
                    )
                )
                ))
            .spawnSettings(createSpawnSettings(
                merge(
                    Map.of(
                        SpawnGroup.MONSTER, List.of(
                            new SpawnSettings.SpawnEntry(ParadiseLostEntityTypes.ENVOY, 50, 1, 3)
                        ),
                        SpawnGroup.CREATURE, List.of(
                            new SpawnSettings.SpawnEntry(ParadiseLostEntityTypes.PARADISE_HARE, 12, 4, 4),
                            new SpawnSettings.SpawnEntry(ParadiseLostEntityTypes.MOA, 6, 5, 13)
                        )
                    )
                ),
                    Map.of(
                            ParadiseLostEntityTypes.ENVOY, SpawnCost.of(1.0, 0.5)
                    )
            ))
            .precipitation(Biome.Precipitation.RAIN).temperature(0.5F).downfall(1)
            // .category(Biome.Category.FOREST)
            .build();
    }
    
    private static Biome createAutumnalTundra() {
        return new Biome.Builder()
            .effects(createBiomeEffects(0xC0C0FF, 0xffe08a, 0xcef2e2, 0xFFFFFF, 0x8CE6FF, 0xA1EAFF))
            .generationSettings(createGenerationSettings(
                getParadiseLostCarvers(),
    
                merge(
                    getStandardParadiseLostFeatures(),
                    Map.of(
                        GenerationStep.Feature.LAKES, List.of(
                            TUNDRA_PONDS,
                            TUNDRA_SNOW
                        ),
                        GenerationStep.Feature.LOCAL_MODIFICATIONS, List.of(
                            GENERIC_BOULDER
                        ),
                        GenerationStep.Feature.FLUID_SPRINGS, List.of(
                            WATER_SPRING
                        ),
                        GenerationStep.Feature.VEGETAL_DECORATION, List.of(
                            TUNDRA_SPIRES,
                            TUNDRA_FOLIAGE,
                            MIXED_TREES
                        ),
                        GenerationStep.Feature.TOP_LAYER_MODIFICATION, List.of(
                            MiscPlacedFeatures.FREEZE_TOP_LAYER
                        )
                    )
                )
            ))
            .spawnSettings(createSpawnSettings(
                merge(
                    Map.of(
                        SpawnGroup.MONSTER, List.of(
                            new SpawnSettings.SpawnEntry(ParadiseLostEntityTypes.ENVOY, 50, 1, 3)
                        ),
                        SpawnGroup.CREATURE, List.of(
                            new SpawnSettings.SpawnEntry(ParadiseLostEntityTypes.PARADISE_HARE, 12, 4, 4),
                            new SpawnSettings.SpawnEntry(ParadiseLostEntityTypes.MOA, 6, 5, 13),
                            new SpawnSettings.SpawnEntry(ParadiseLostEntityTypes.PARADISE_HARE, 6, 1, 3)
                        )
                    )
                ),
                    Map.of(
                            ParadiseLostEntityTypes.ENVOY, SpawnCost.of(1.0, 0.5)
                    )
            ))
            .precipitation(Biome.Precipitation.RAIN).temperature(0.1F).downfall(2)
            // .category(Biome.Category.TAIGA)
            .build();
    }
    
    private static Biome createContinentalPlateau() {
        return new Biome.Builder()
            .effects(createBiomeEffects(0xC0C0FF, 0xffe08a, 0xcef2e2, 0xFFFFFF, 0x8CE6FF, 0xA1EAFF))
            .generationSettings(createGenerationSettings(
                getParadiseLostCarvers(),
    
                merge(
                    getStandardParadiseLostFeatures(),
                    Map.of(
                        GenerationStep.Feature.LOCAL_MODIFICATIONS, List.of(
                            PLAINS_BOULDER
                        ),
                        GenerationStep.Feature.FLUID_SPRINGS, List.of(
                            WATER_SPRING
                        ),
                        GenerationStep.Feature.VEGETAL_DECORATION, List.of(
                            FLOWERS,
                            GRASS,
                            TALL_GRASS,
                            PATCH_BLACKCURRANT,
                            PLATEAU_FOLIAGE,
                            PLATEAU_SHAMROCK,
                            PLATEAU_FLOWERING_GRASS,
                            PLATEAU_TREES
                        )
                    )
                )
            ))
            .spawnSettings(createSpawnSettings(
                merge(
                    Map.of(
                        SpawnGroup.MONSTER, List.of(
                            new SpawnSettings.SpawnEntry(ParadiseLostEntityTypes.ENVOY, 50, 1, 3)
                        ),
                        SpawnGroup.CREATURE, List.of(
                            new SpawnSettings.SpawnEntry(ParadiseLostEntityTypes.PARADISE_HARE, 12, 4, 4),
                            new SpawnSettings.SpawnEntry(ParadiseLostEntityTypes.MOA, 6, 5, 13),
                            new SpawnSettings.SpawnEntry(ParadiseLostEntityTypes.PARADISE_HARE, 6, 1, 3)
                        )
                    )
                ),
                    Map.of(
                            ParadiseLostEntityTypes.ENVOY, SpawnCost.of(1.0, 0.5)
                    )
            ))
            .precipitation(Biome.Precipitation.RAIN).temperature(0.25F).downfall(0)
            .build();
    }
    
    private static Biome createHighlandsShield() {
        return new Biome.Builder()
            .effects(createBiomeEffects(0xC0C0FF, 0xfae05f, 0xaef5d6, 0xFFFFFF, 0x58E5FF, 0x63E7FF))
            .generationSettings(createGenerationSettings(
                getParadiseLostCarvers(),
    
                merge(
                    getStandardParadiseLostFeatures(),
                    Map.of(
                        GenerationStep.Feature.LAKES, List.of(
                            SHIELD_PONDS
                        ),
                        GenerationStep.Feature.LOCAL_MODIFICATIONS, List.of(
                            GENERIC_BOULDER
                        ),
                        GenerationStep.Feature.FLUID_SPRINGS, List.of(
                            WATER_SPRING
                        ),
                        GenerationStep.Feature.VEGETAL_DECORATION, List.of(
                            SHIELD_STUMPS,
                            SHIELD_HOLLOW_STUMPS,
                            MOTTLED_FALLEN_LOG,
                            MOTTLED_HOLLOW_FALLEN_LOG,
                            FLOWERS,
                            SHIELD_FLAX,
                            SHIELD_NETTLES,
                            SHIELD_FOLIAGE,
                            GRASS,
                            TALL_GRASS,
                            WATER_SPRING,
                            SHIELD_TREES,
                            DENSE_SHIELD_TREES,
                            SHIELD_ROCKS,
                            SHIELD_FALLEN_LEAVES
                        )
                    )
                )
            ))
            .spawnSettings(createSpawnSettings(
                merge(
                    Map.of(
                        SpawnGroup.MONSTER, List.of(
                            new SpawnSettings.SpawnEntry(ParadiseLostEntityTypes.ENVOY, 50, 1, 3)
                        ),
                        SpawnGroup.CREATURE, List.of(
                            new SpawnSettings.SpawnEntry(ParadiseLostEntityTypes.PARADISE_HARE, 12, 4, 4),
                            new SpawnSettings.SpawnEntry(ParadiseLostEntityTypes.MOA, 6, 5, 13),
                            new SpawnSettings.SpawnEntry(ParadiseLostEntityTypes.PARADISE_HARE, 6, 1, 3)
                        )
                    )
                ),
                Map.of(
                        ParadiseLostEntityTypes.ENVOY, SpawnCost.of(1.0, 0.5)
                )
            ))
            .precipitation(Biome.Precipitation.RAIN).temperature(0.35F).downfall(2)
            .build();
    }

    private record SpawnCost(
            double charge,
            double energyBudget
    ) {
        static SpawnCost of(double charge, double energyBudget) {
            return new SpawnCost(charge, energyBudget);
        }
    }
}
