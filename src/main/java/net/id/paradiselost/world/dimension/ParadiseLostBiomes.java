package net.id.paradiselost.world.dimension;

import net.id.paradiselost.entities.ParadiseLostEntityTypes;
import net.id.paradiselost.world.gen.carver.ParadiseLostCarvers;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
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
    public static final RegistryKey<Biome> AUTUMNAL_TUNDRA_KEY = of("autumnal_tundra");
    public static final RegistryKey<Biome> CONTINENTAL_PLATEAU_KEY = of("continental_plateau");
    public static final RegistryKey<Biome> HIGHLANDS_PLAINS_KEY = of("highlands");
    public static final RegistryKey<Biome> HIGHLANDS_FOREST_KEY = of("highlands_forest");
    public static final RegistryKey<Biome> HIGHLANDS_GRAND_GLADE_KEY = of("highlands_grand_glade");
    public static final RegistryKey<Biome> HIGHLANDS_SHIELD_KEY = of("highlands_shield");
    public static final RegistryKey<Biome> HIGHLANDS_THICKET_KEY = of("highlands_thicket");
    public static final RegistryKey<Biome> TRADEWINDS_KEY = of("tradewinds");
    public static final RegistryKey<Biome> WISTERIA_WOODS_KEY = of("wisteria_woods");

    public static void init() {}

    private static RegistryKey<Biome> of(String name) {
        return RegistryKey.of(RegistryKeys.BIOME, locate(name));
    }

//    private static BiomeEffects createBiomeEffects(int skyColor, int foliageColor, int grassColor, int fogColor, int waterColor, int waterFogColor, BiomeEffects.GrassColorModifier grassModifier) {
//        return new BiomeEffects.Builder()
//            .skyColor(skyColor)
//            .foliageColor(foliageColor)
//            .grassColor(grassColor)
//            .fogColor(fogColor)
//            .waterColor(waterColor)
//            .waterFogColor(waterFogColor)
//            .grassColorModifier(grassModifier)
//            .build();
//    }
//
//    private static BiomeEffects createBiomeEffects(int skyColor, int foliageColor, int grassColor, int fogColor, int waterColor, int waterFogColor) {
//        return createBiomeEffects(skyColor, foliageColor, grassColor, fogColor, waterColor, waterFogColor, BiomeEffects.GrassColorModifier.NONE);
//    }
//
//
//    /**
//     * Returns a map of common placed features in all of the Paradise Lost biomes.
//     *
//     * @return A map of features suitable for use with {@link #createGenerationSettings}
//     */
//    private static Map<GenerationStep.Feature, List<RegistryEntry<PlacedFeature>>> getStandardParadiseLostFeatures() {
//        return Map.of(
//            GenerationStep.Feature.RAW_GENERATION, List.of(
//                SURTRUM_METEORITE
//            ),
//            GenerationStep.Feature.UNDERGROUND_ORES, List.of(
//                HELIOLITH_BLOB,
//                LEVITA_BLOB,
//                ORE_CHERINE,
//                ORE_OLVITE,
//                ORE_LEVITA
//            ),
//            GenerationStep.Feature.VEGETAL_DECORATION, List.of(
//                PATCH_BROWN_SPORECAP,
//                PATCH_PINK_SPORECAP,
//                NATURAL_SWEDROOT
//            )
//        );
//    }
//
//    private static Biome createHighlandsPlains() {
//        return new Biome.Builder()
//            .effects(createBiomeEffects(0xC0C0FF, 0xf1ff99, 0xa9dbc5, 0xFFFFFF, 0x58E5FF, 0x63E7FF))
//            .generationSettings(createGenerationSettings(
//                getParadiseLostCarvers(),
//
//                merge(
//                    getStandardParadiseLostFeatures(),
//                    Map.of(
//                        GenerationStep.Feature.LOCAL_MODIFICATIONS, List.of(
//                            PLAINS_BOULDER
//                        ),
//                        GenerationStep.Feature.FLUID_SPRINGS, List.of(
//                            WATER_SPRING
//                        ),
//                        GenerationStep.Feature.VEGETAL_DECORATION, List.of(
//                            FLOWERS,
//                            GRASS,
//                            TALL_GRASS,
//                            PATCH_BLACKCURRANT,
//                            SPARSE_TREES
//                        )
//                    )
//                )
//            ))
//            .spawnSettings(createSpawnSettings(
//                merge(
//                    Map.of(
//                        SpawnGroup.MONSTER, List.of(
//                            new SpawnSettings.SpawnEntry(ParadiseLostEntityTypes.ENVOY, 50, 1, 3)
//                        ),
//                        SpawnGroup.CREATURE, List.of(
//                            new SpawnSettings.SpawnEntry(ParadiseLostEntityTypes.MOA, 6, 5, 13)
//                        )
//                    )
//                ),
//                    Map.of(
//                            ParadiseLostEntityTypes.ENVOY, SpawnCost.of(1.0, 0.5)
//                    )
//            ))
//            .precipitation(Biome.Precipitation.RAIN).temperature(0.5F).downfall(1)
//            // .category(Biome.Category.PLAINS)
//            .build();
//    }
//
//    private static Biome createHighlandsForest() {
//        return new Biome.Builder()
//            .effects(createBiomeEffects(0xC0C0FF, 0xf1ff99, 0xa2dbc2, 0xFFFFFF, 0x58E5FF, 0x63E7FF))
//            .generationSettings(createGenerationSettings(
//                getParadiseLostCarvers(),
//
//                merge(
//                    getStandardParadiseLostFeatures(),
//                    Map.of(
//                        GenerationStep.Feature.LOCAL_MODIFICATIONS, List.of(
//                        ),
//                        GenerationStep.Feature.FLUID_SPRINGS, List.of(
//                            WATER_SPRING
//                        ),
//                        GenerationStep.Feature.VEGETAL_DECORATION, List.of(
//                            FLOWERS,
//                            TALL_GRASS,
//                            TUNDRA_FOLIAGE,
//                            BUSH,
//                            PATCH_BLACKCURRANT,
//                            SCATTERED_TREES,
//                            ALT_FALLEN_LEAVES
//                        )
//                    )
//                )
//            ))
//            .spawnSettings(createSpawnSettings(
//                merge(
//                    Map.of(
//                        SpawnGroup.MONSTER, List.of(
//                            new SpawnSettings.SpawnEntry(ParadiseLostEntityTypes.ENVOY, 50, 1, 3)
//                        ),
//                        SpawnGroup.CREATURE, List.of(
//                            new SpawnSettings.SpawnEntry(ParadiseLostEntityTypes.MOA, 6, 5, 13)
//                        )
//                    )
//                ),
//                    Map.of(
//                            ParadiseLostEntityTypes.ENVOY, SpawnCost.of(1.0, 0.5)
//                    )
//            ))
//            .precipitation(true).temperature(0.5F).downfall(1)
//            // .category(Biome.Category.FOREST)
//            .build();
//    }
//
//    private static Biome createTradewinds() {
//        return new Biome.Builder()
//                .effects(createBiomeEffects(0xC0C0FF, 0xf1ff99, 0x8dc9af, 0xFFFFFF, 0x58E5FF, 0x63E7FF))
//                .generationSettings(createGenerationSettings(
//                        getParadiseLostCarvers(),
//
//                        merge(
//                                getStandardParadiseLostFeatures(),
//                                Map.of(
//                                        GenerationStep.Feature.LOCAL_MODIFICATIONS, List.of(
//                                        ),
//                                        GenerationStep.Feature.FLUID_SPRINGS, List.of(
//                                                WATER_SPRING
//                                        ),
//                                        GenerationStep.Feature.VEGETAL_DECORATION, List.of(
//                                                TALL_GRASS,
//                                                BUSH,
//                                                PATCH_BLACKCURRANT,
//                                                PLATEAU_TREES,
//                                                SPARSE_TREES
//                                        )
//                                )
//                        )
//                ))
//                .spawnSettings(createSpawnSettings(
//                    merge(
//                        Map.of(
//                            SpawnGroup.MONSTER, List.of(
//                                new SpawnSettings.SpawnEntry(ParadiseLostEntityTypes.ENVOY, 50, 1, 3)
//                            ),
//                            SpawnGroup.CREATURE, List.of(
//                                new SpawnSettings.SpawnEntry(ParadiseLostEntityTypes.MOA, 6, 5, 13)
//                            )
//                        )
//                    ),
//                        Map.of(
//                                ParadiseLostEntityTypes.ENVOY, SpawnCost.of(1.0, 0.5)
//                        )
//                ))
//                .precipitation(true).temperature(0.5F).downfall(1)
//                // .category(Biome.Category.FOREST)
//                .build();
//    }
//
//    private static Biome createHighlandsThicket() {
//        return new Biome.Builder()
//            .effects(createBiomeEffects(0xC0C0FF, 0xedff7d, 0x739988, 0xFFFFFF, 0x58E5FF, 0x63E7FF))
//            .generationSettings(createGenerationSettings(
//                getParadiseLostCarvers(),
//
//                merge(
//                    getStandardParadiseLostFeatures(),
//                    Map.of(
//                        GenerationStep.Feature.LOCAL_MODIFICATIONS, List.of(
//                            THICKET_BOULDER,
//                            GOLDEN_BOULDER
//                        ),
//                        GenerationStep.Feature.FLUID_SPRINGS, List.of(
//                            WATER_SPRING
//                        ),
//                        GenerationStep.Feature.VEGETAL_DECORATION, List.of(
//                            THICKET_SHAMROCK,
//                            THICKET_LIVERWORT_CARPET,
//                            GRASS,
//                            FALLEN_LEAVES,
//                            TALL_GRASS,
//                            DENSE_BUSH,
//                            THICKET_TREES,
//                            THICKET_MOTHER_AUREL_TREES
//                        )
//                    )
//                )
//            ))
//            .spawnSettings(createSpawnSettings(
//                merge(
//                    Map.of(
//                        SpawnGroup.MONSTER, List.of(
//                            new SpawnSettings.SpawnEntry(ParadiseLostEntityTypes.ENVOY, 50, 1, 3)
//                        ),
//                        SpawnGroup.CREATURE, List.of(
//                            new SpawnSettings.SpawnEntry(ParadiseLostEntityTypes.MOA, 6, 5, 13)
//                        )
//                    )
//                ),
//                    Map.of(
//                            ParadiseLostEntityTypes.ENVOY, SpawnCost.of(1.0, 0.5)
//                    )
//            ))
//            .precipitation(Biome.Precipitation.RAIN).temperature(0.4F).downfall(2)
//            // .category(Biome.Category.FOREST)
//            .build();
//    }
//
//    private static Biome createHighlandsGrandGlade() {
//        return new Biome.Builder()
//                .effects(createBiomeEffects(0xC0C0FF, 0xedff7d, 0x739988, 0xFFFFFF, 0x58E5FF, 0x63E7FF))
//                .generationSettings(createGenerationSettings(
//                        getParadiseLostCarvers(),
//
//                        merge(
//                                getStandardParadiseLostFeatures(),
//                                Map.of(
//                                        GenerationStep.Feature.LOCAL_MODIFICATIONS, List.of(
//                                                THICKET_BOULDER
//                                        ),
//                                        GenerationStep.Feature.FLUID_SPRINGS, List.of(
//                                                WATER_SPRING
//                                        ),
//                                        GenerationStep.Feature.VEGETAL_DECORATION, List.of(
//                                                THICKET_FALLEN_LOG,
//                                                GRASS,
//                                                FALLEN_LEAVES,
//                                                PATCH_BROWN_SPORECAP_COMMON,
//                                                TALL_GRASS,
//                                                DENSE_BUSH,
//                                                SPARSE_TREES
//                                        )
//                                )
//                        )
//                ))
//                .spawnSettings(createSpawnSettings(
//                    merge(
//                        Map.of(
//                            SpawnGroup.MONSTER, List.of(
//                                new SpawnSettings.SpawnEntry(ParadiseLostEntityTypes.ENVOY, 50, 1, 3)
//                            ),
//                            SpawnGroup.CREATURE, List.of(
//                                new SpawnSettings.SpawnEntry(ParadiseLostEntityTypes.MOA, 6, 5, 13)
//                            )
//                        )
//                    ),
//                        Map.of(
//                                ParadiseLostEntityTypes.ENVOY, SpawnCost.of(1.0, 0.5)
//                        )
//                ))
//                .precipitation(Biome.Precipitation.RAIN).temperature(0.4F).downfall(2)
//                // .category(Biome.Category.FOREST)
//                .build();
//    }
//
//    private static Biome createWisteriaWoods() {
//        return new Biome.Builder()
//            .effects(createBiomeEffects(0xC0C0FF, 0xf1ff99, 0xa5d6c1, 0xFFFFFF, 0x58E5FF, 0x63E7FF))
//            .generationSettings(createGenerationSettings(
//                getParadiseLostCarvers(),
//
//                merge(
//                    getStandardParadiseLostFeatures(),
//                    Map.of(
//                        GenerationStep.Feature.LOCAL_MODIFICATIONS, List.of(
//                        ),
//                        GenerationStep.Feature.VEGETAL_DECORATION, List.of(
//                            FLOWERS,
//                            GRASS,
//                            FALLEN_ROSE_LEAVES,
//                            FALLEN_LAVENDER_LEAVES,
//                            BUSH,
//                            PATCH_BLACKCURRANT,
//                            RAINBOW_FOREST_TREES
//                        )
//                    )
//                )
//                ))
//            .spawnSettings(createSpawnSettings(
//                merge(
//                    Map.of(
//                        SpawnGroup.MONSTER, List.of(
//                            new SpawnSettings.SpawnEntry(ParadiseLostEntityTypes.ENVOY, 50, 1, 3)
//                        ),
//                        SpawnGroup.CREATURE, List.of(
//                            new SpawnSettings.SpawnEntry(ParadiseLostEntityTypes.MOA, 6, 5, 13)
//                        )
//                    )
//                ),
//                    Map.of(
//                            ParadiseLostEntityTypes.ENVOY, SpawnCost.of(1.0, 0.5)
//                    )
//            ))
//            .precipitation(true).temperature(0.5F).downfall(1)
//            // .category(Biome.Category.FOREST)
//            .build();
//    }
//
//    private static Biome createAutumnalTundra() {
//        return new Biome.Builder()
//            .effects(createBiomeEffects(0xC0C0FF, 0xffe08a, 0xcef2e2, 0xFFFFFF, 0x8CE6FF, 0xA1EAFF))
//            .generationSettings(createGenerationSettings(
//                getParadiseLostCarvers(),
//
//                merge(
//                    getStandardParadiseLostFeatures(),
//                    Map.of(
//                        GenerationStep.Feature.LAKES, List.of(
//                            TUNDRA_PONDS,
//                            TUNDRA_SNOW
//                        ),
//                        GenerationStep.Feature.LOCAL_MODIFICATIONS, List.of(
//                            GENERIC_BOULDER
//                        ),
//                        GenerationStep.Feature.FLUID_SPRINGS, List.of(
//                            WATER_SPRING
//                        ),
//                        GenerationStep.Feature.VEGETAL_DECORATION, List.of(
//                            TUNDRA_SPIRES,
//                            TUNDRA_FOLIAGE,
//                            MIXED_TREES
//                        ),
//                        GenerationStep.Feature.TOP_LAYER_MODIFICATION, List.of(
//                            MiscPlacedFeatures.FREEZE_TOP_LAYER
//                        )
//                    )
//                )
//            ))
//            .spawnSettings(createSpawnSettings(
//                merge(
//                    Map.of(
//                        SpawnGroup.MONSTER, List.of(
//                            new SpawnSettings.SpawnEntry(ParadiseLostEntityTypes.ENVOY, 50, 1, 3)
//                        ),
//                        SpawnGroup.CREATURE, List.of(
//                            new SpawnSettings.SpawnEntry(ParadiseLostEntityTypes.MOA, 6, 5, 13)
//                        )
//                    )
//                ),
//                    Map.of(
//                            ParadiseLostEntityTypes.ENVOY, SpawnCost.of(1.0, 0.5)
//                    )
//            ))
//            .precipitation(Biome.Precipitation.RAIN).temperature(0.1F).downfall(2)
//            // .category(Biome.Category.TAIGA)
//            .build();
//    }
//
//    private static Biome createContinentalPlateau() {
//        return new Biome.Builder()
//            .effects(createBiomeEffects(0xC0C0FF, 0xffe08a, 0xcef2e2, 0xFFFFFF, 0x8CE6FF, 0xA1EAFF))
//            .generationSettings(createGenerationSettings(
//                getParadiseLostCarvers(),
//
//                merge(
//                    getStandardParadiseLostFeatures(),
//                    Map.of(
//                        GenerationStep.Feature.LOCAL_MODIFICATIONS, List.of(
//                            PLAINS_BOULDER
//                        ),
//                        GenerationStep.Feature.FLUID_SPRINGS, List.of(
//                            WATER_SPRING
//                        ),
//                        GenerationStep.Feature.VEGETAL_DECORATION, List.of(
//                            FLOWERS,
//                            GRASS,
//                            TALL_GRASS,
//                            PATCH_BLACKCURRANT,
//                            PLATEAU_FOLIAGE,
//                            PLATEAU_SHAMROCK,
//                            PLATEAU_FLOWERING_GRASS,
//                            PLATEAU_TREES
//                        )
//                    )
//                )
//            ))
//            .spawnSettings(createSpawnSettings(
//                merge(
//                    Map.of(
//                        SpawnGroup.MONSTER, List.of(
//                            new SpawnSettings.SpawnEntry(ParadiseLostEntityTypes.ENVOY, 50, 1, 3)
//                        ),
//                        SpawnGroup.CREATURE, List.of(
//                            new SpawnSettings.SpawnEntry(ParadiseLostEntityTypes.MOA, 6, 5, 13)
//                        )
//                    )
//                ),
//                    Map.of(
//                            ParadiseLostEntityTypes.ENVOY, SpawnCost.of(1.0, 0.5)
//                    )
//            ))
//            .precipitation(Biome.Precipitation.RAIN).temperature(0.25F).downfall(0)
//            .build();
//    }
//
//    private static Biome createHighlandsShield() {
//        return new Biome.Builder()
//            .effects(createBiomeEffects(0xC0C0FF, 0xfae05f, 0xaef5d6, 0xFFFFFF, 0x58E5FF, 0x63E7FF))
//            .generationSettings(createGenerationSettings(
//                getParadiseLostCarvers(),
//
//                merge(
//                    getStandardParadiseLostFeatures(),
//                    Map.of(
//                        GenerationStep.Feature.LAKES, List.of(
//                            SHIELD_PONDS
//                        ),
//                        GenerationStep.Feature.LOCAL_MODIFICATIONS, List.of(
//                            GENERIC_BOULDER
//                        ),
//                        GenerationStep.Feature.FLUID_SPRINGS, List.of(
//                            WATER_SPRING
//                        ),
//                        GenerationStep.Feature.VEGETAL_DECORATION, List.of(
//                            SHIELD_STUMPS,
//                            SHIELD_HOLLOW_STUMPS,
//                            MOTTLED_FALLEN_LOG,
//                            MOTTLED_HOLLOW_FALLEN_LOG,
//                            FLOWERS,
//                            SHIELD_FLAX,
//                            SHIELD_NETTLES,
//                            SHIELD_FOLIAGE,
//                            GRASS,
//                            TALL_GRASS,
//                            WATER_SPRING,
//                            SHIELD_TREES,
//                            DENSE_SHIELD_TREES,
//                            SHIELD_ROCKS
//                        )
//                    )
//                )
//            ))
//            .spawnSettings(createSpawnSettings(
//                merge(
//                    Map.of(
//                        SpawnGroup.MONSTER, List.of(
//                            new SpawnSettings.SpawnEntry(ParadiseLostEntityTypes.ENVOY, 50, 1, 3)
//                        ),
//                        SpawnGroup.CREATURE, List.of(
//                            new SpawnSettings.SpawnEntry(ParadiseLostEntityTypes.MOA, 6, 5, 13)
//                        )
//                    )
//                ),
//                Map.of(
//                        ParadiseLostEntityTypes.ENVOY, SpawnCost.of(1.0, 0.5)
//                )
//            ))
//            .precipitation(Biome.Precipitation.RAIN).temperature(0.35F).downfall(2)
//            .build();
//    }
//

}
