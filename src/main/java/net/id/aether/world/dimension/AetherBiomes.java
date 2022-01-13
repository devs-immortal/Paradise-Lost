package net.id.aether.world.dimension;

import net.id.aether.entities.AetherEntityTypes;
import net.id.aether.world.feature.AetherPlacedFeatures;
import net.id.aether.world.gen.carver.AetherCarvers;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
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

import static net.id.aether.Aether.locate;

/**
 * Generates the Aether biome instances and registers them with Minecraft. Things in here don't have to be particularly
 * speedy because they are only done once.
 * @author Gudenau
 */
public final class AetherBiomes {
    public static final RegistryKey<Biome> HIGHLANDS_PLAINS_KEY = register("aether_highlands");
    public static final RegistryKey<Biome> HIGHLANDS_FOREST_KEY = register("aether_highlands_forest");
    public static final RegistryKey<Biome> HIGHLANDS_THICKET_KEY = register("aether_highlands_thicket");
    public static final RegistryKey<Biome> WISTERIA_WOODS_KEY = register("aether_wisteria_woods");
    public static final RegistryKey<Biome> AUTUMNAL_TUNDRA_KEY = register("autumnal_tundra");
    public static final RegistryKey<Biome> CONTINENTAL_PLATEAU_KEY = register("continental_plateau");
    public static final RegistryKey<Biome> HIGHLANDS_SHIELD_KEY = register("highlands_shield");
    
    public static final Biome HIGHLANDS_PLAINS;
    public static final Biome HIGHLANDS_FOREST;
    public static final Biome HIGHLANDS_THICKET;
    public static final Biome WISTERIA_WOODS;
    public static final Biome AUTUMNAL_TUNDRA;
    public static final Biome CONTINENTAL_PLATEAU;
    public static final Biome HIGHLANDS_SHIELD;
    
    static{
        HIGHLANDS_PLAINS = register(HIGHLANDS_PLAINS_KEY, createAetherHighlands());
        HIGHLANDS_FOREST = register(HIGHLANDS_FOREST_KEY, createAetherHighlandsForest());
        HIGHLANDS_THICKET = register(HIGHLANDS_THICKET_KEY, createAetherHighlandsThicket());
        WISTERIA_WOODS = register(WISTERIA_WOODS_KEY, createAetherWisteriaWoods());
        AUTUMNAL_TUNDRA = register(AUTUMNAL_TUNDRA_KEY, createAutumnalTundra());
        CONTINENTAL_PLATEAU = register(CONTINENTAL_PLATEAU_KEY, createContinentalPlateau());
        HIGHLANDS_SHIELD = register(HIGHLANDS_SHIELD_KEY, createHighlandsShield());
    }
    
    public static void init(){}
    
    private static RegistryKey<Biome> register(String name) {
        return RegistryKey.of(Registry.BIOME_KEY, locate(name));
    }
    
    private static Biome register(RegistryKey<Biome> key, Biome biome) {
        return BuiltinRegistries.set(BuiltinRegistries.BIOME, key, biome);
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
    private static BiomeEffects createBiomeEffects(int skyColor, int foliageColor, int grassColor, int fogColor, int waterColor, int waterFogColor, BiomeEffects.GrassColorModifier grassModifier){
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
    private static BiomeEffects createBiomeEffects(int skyColor, int foliageColor, int grassColor, int fogColor, int waterColor, int waterFogColor){
        return createBiomeEffects(skyColor, foliageColor, grassColor, fogColor, waterColor, waterFogColor, BiomeEffects.GrassColorModifier.NONE);
    }
    
    /**
     * Creates a new {@link GenerationSettings} instance from the provided carvers and features.
     *
     * @param carvers The carvers to use
     * @param features The features to use
     * @return The new {@link GenerationSettings} instance
     */
    private static GenerationSettings createGenerationSettings(Map<GenerationStep.Carver, List<ConfiguredCarver<?>>> carvers, Map<GenerationStep.Feature, List<PlacedFeature>> features) {
        var builder = new GenerationSettings.Builder();
        for (var step : GenerationStep.Carver.values()) {
            for (var carver : carvers.getOrDefault(step, List.of())) {
                builder.carver(step, carver);
            }
        }
        for (var step : GenerationStep.Feature.values()){
            for (var feature : features.getOrDefault(step, List.of())){
                builder.feature(step, feature);
            }
        }
        return builder.build();
    }
    
    private record SpawnCost(
        double charge,
        double energyBudget
    ){
        static SpawnCost of(double charge, double energyBudget){
            return new SpawnCost(charge, energyBudget);
        }
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
        for(var group : SpawnGroup.values()){
            for(var spawn : spawns.getOrDefault(group, List.of())){
                builder.spawn(group, spawn);
            }
        }
        for(var entry : costs.entrySet()){
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
    public static <A, B> Map<A, List<B>> mergeFeatures(Map<A, List<B>>... maps){
        if(maps.length == 1){
            return maps[0];
        }
        
        Map<A, List<B>> result = new HashMap<>();
        for (var map : maps) {
            for (Map.Entry<A, List<B>> entry : map.entrySet()) {
                result.computeIfAbsent(entry.getKey(), (ignored)->new ArrayList<>()).addAll(entry.getValue());
            }
        }
        return result;
    }
    
    /**
     * Creates a {@link Map} suitable for use with {@link #createGenerationSettings} that contains all of the default
     * Aether carvers.
     *
     * @return The default Aether carvers
     */
    private static Map<GenerationStep.Carver, List<ConfiguredCarver<?>>> getAetherCarvers() {
        return Map.of(GenerationStep.Carver.AIR, List.of(
            ConfiguredCarvers.CAVE,
            AetherCarvers.LARGE_COLD_AERCLOUD_CARVER,
            AetherCarvers.COLD_AERCLOUD_CARVER,
            AetherCarvers.TINY_COLD_AERCLOUD_CARVER,
            AetherCarvers.LARGE_BLUE_AERCLOUD_CARVER,
            AetherCarvers.BLUE_AERCLOUD_CARVER,
            AetherCarvers.TINY_BLUE_AERCLOUD_CARVER,
            AetherCarvers.LARGE_GOLDEN_AERCLOUD_CARVER,
            AetherCarvers.GOLDEN_AERCLOUD_CARVER,
            AetherCarvers.TINY_GOLDEN_AERCLOUD_CARVER
        ));
    }
    
    /**
     * Returns a map of common placed features in all of the Aether biomes.
     * 
     * @return A map of features suitable for use with {@link #createGenerationSettings}
     */
    private static Map<GenerationStep.Feature, List<PlacedFeature>> getStandardAetherFeatures() {
        return Map.of(
            GenerationStep.Feature.UNDERGROUND_ORES, List.of(
                AetherPlacedFeatures.ORE_AMBROSIUM,
                AetherPlacedFeatures.ORE_GRAVITITE,
                AetherPlacedFeatures.ORE_ZANITE
            )
        );
    }
    
    private static Biome createAetherHighlands(){
        /*
        TODO
        {
          "starts": [
            "the_aether:well",
            "the_aether:skyroot_tower",
            "the_aether:orange_ruin"
          ],
          "player_spawn_friendly": false,
          "depth": 0.0,
          "scale": 0.1
        }
         */
        return new Biome.Builder()
            .effects(createBiomeEffects(0xC0C0FF, 0xDCFF81, 0xB1FFCB, 0xFFFFFF, 0x58E5FF, 0x58E5FF))
            .generationSettings(createGenerationSettings(
                getAetherCarvers(),
    
                mergeFeatures(
                    getStandardAetherFeatures(),
                    Map.of(
                        GenerationStep.Feature.LOCAL_MODIFICATIONS, List.of(
                            AetherPlacedFeatures.QUICKSOIL,
                            AetherPlacedFeatures.PLAINS_BOULDER
                        ),
                        GenerationStep.Feature.FLUID_SPRINGS, List.of(
                            AetherPlacedFeatures.WATER_SPRING
                        ),
                        GenerationStep.Feature.VEGETAL_DECORATION, List.of(
                            AetherPlacedFeatures.AETHER_FLOWERS,
                            AetherPlacedFeatures.AETHER_GRASS,
                            AetherPlacedFeatures.AETHER_TALL_GRASS,
                            AetherPlacedFeatures.PATCH_BLUEBERRY,
                            AetherPlacedFeatures.SPARSE_TREES,
                            AetherPlacedFeatures.FLUTEGRASS
                        )
                    )
                )
            ))
            .spawnSettings(createSpawnSettings(
                Map.of(
                    SpawnGroup.MONSTER, List.of(
                        new SpawnSettings.SpawnEntry(AetherEntityTypes.BLUE_SWET, 100, 2, 6)
                    ),
                    SpawnGroup.CREATURE, List.of(
                        new SpawnSettings.SpawnEntry(AetherEntityTypes.AERBUNNY, 12, 4, 4),
                        new SpawnSettings.SpawnEntry(AetherEntityTypes.MOA, 6, 5, 13),
                        new SpawnSettings.SpawnEntry(AetherEntityTypes.AERWHALE, 6, 1, 3)
                    )
                ),
                Map.of(
                    AetherEntityTypes.BLUE_SWET, SpawnCost.of(1, 0.12)
                )
            ))
            .precipitation(Biome.Precipitation.RAIN).temperature(0.5F).downfall(1)
            .category(Biome.Category.PLAINS)
            .build();
    }
    
    private static Biome createAetherHighlandsForest(){
        /*
        TODO
        {
          "starts": [
            "the_aether:well",
            "the_aether:skyroot_tower",
            "the_aether:orange_ruin"
          ],
          "player_spawn_friendly": false,
          "depth": 0.1,
          "scale": 0.1
        }
         */
        return new Biome.Builder()
            .effects(createBiomeEffects(0xC0C0FF, 0xD6FF6B, 0x88EBA1, 0xFFFFFF, 0x58E5FF, 0x58E5FF))
            .generationSettings(createGenerationSettings(
                getAetherCarvers(),
    
                mergeFeatures(
                    getStandardAetherFeatures(),
                    Map.of(
                        GenerationStep.Feature.LOCAL_MODIFICATIONS, List.of(
                            AetherPlacedFeatures.QUICKSOIL
                        ),
                        GenerationStep.Feature.FLUID_SPRINGS, List.of(
                            AetherPlacedFeatures.WATER_SPRING
                        ),
                        GenerationStep.Feature.VEGETAL_DECORATION, List.of(
                            AetherPlacedFeatures.AETHER_FLOWERS,
                            AetherPlacedFeatures.AETHER_TALL_GRASS,
                            AetherPlacedFeatures.TUNDRA_FOLIAGE,
                            AetherPlacedFeatures.AETHER_BUSH,
                            AetherPlacedFeatures.PATCH_BLUEBERRY,
                            AetherPlacedFeatures.SCATTERED_TREES,
                            AetherPlacedFeatures.ALT_FALLEN_LEAVES,
                            AetherPlacedFeatures.FLUTEGRASS
                        )
                    )
                )
            ))
            .spawnSettings(createSpawnSettings(
                Map.of(
                    SpawnGroup.MONSTER, List.of(
                        new SpawnSettings.SpawnEntry(AetherEntityTypes.BLUE_SWET, 100, 2, 6)
                    ),
                    SpawnGroup.CREATURE, List.of(
                        new SpawnSettings.SpawnEntry(AetherEntityTypes.AERBUNNY, 12, 4, 4),
                        new SpawnSettings.SpawnEntry(AetherEntityTypes.MOA, 6, 3, 7),
                        new SpawnSettings.SpawnEntry(AetherEntityTypes.AERWHALE, 6, 1, 3)
                    )
                ),
                
                Map.of(
                    AetherEntityTypes.BLUE_SWET, SpawnCost.of(1, 0.12F)
                )
            ))
            .precipitation(Biome.Precipitation.RAIN).temperature(0.5F).downfall(1)
            .category(Biome.Category.FOREST)
            .build();
    }
    
    private static Biome createAetherHighlandsThicket(){
        /*
        TODO
         {
          "starts": [
            "the_aether:well",
            "the_aether:skyroot_tower",
            "the_aether:orange_ruin"
          ],
          "player_spawn_friendly": false,
          "depth": 0.125,
          "scale": 0.05
        }
         */
        return new Biome.Builder()
            .effects(createBiomeEffects(0xC0C0FF, 0xC8E67A, 0x85DDAF, 0xFFFFFF, 0x58E5FF, 0x58E5FF, BiomeEffects.GrassColorModifier.DARK_FOREST))
            .generationSettings(createGenerationSettings(
                getAetherCarvers(),
    
                mergeFeatures(
                    getStandardAetherFeatures(),
                    Map.of(
                        GenerationStep.Feature.LOCAL_MODIFICATIONS, List.of(
                            AetherPlacedFeatures.THICKET_BOULDER,
                            AetherPlacedFeatures.GOLDEN_BOULDER
                        ),
                        GenerationStep.Feature.STRONGHOLDS, List.of(
                            AetherPlacedFeatures.THICKET_LIVERWORT
                        ),
                        GenerationStep.Feature.FLUID_SPRINGS, List.of(
                            AetherPlacedFeatures.WATER_SPRING
                        ),
                        GenerationStep.Feature.VEGETAL_DECORATION, List.of(
                            AetherPlacedFeatures.THICKET_SHAMROCK,
                            AetherPlacedFeatures.THICKET_LIVERWORT_CARPET,
                            AetherPlacedFeatures.THICKET_FALLEN_LOG,
                            AetherPlacedFeatures.AETHER_FLOWERS,
                            AetherPlacedFeatures.AETHER_GRASS,
                            AetherPlacedFeatures.FALLEN_LEAVES,
                            AetherPlacedFeatures.AETHER_TALL_GRASS,
                            AetherPlacedFeatures.AETHER_DENSE_BUSH,
                            AetherPlacedFeatures.THICKET_TREES,
                            AetherPlacedFeatures.SPARSE_TREES
                        )
                    )
                )
            ))
            .spawnSettings(createSpawnSettings(
                Map.of(
                    SpawnGroup.MONSTER, List.of(
                        new SpawnSettings.SpawnEntry(AetherEntityTypes.BLUE_SWET, 100, 2, 6),
                        new SpawnSettings.SpawnEntry(AetherEntityTypes.AECHOR_PLANT, 50, 1, 2)
                    ),
                    SpawnGroup.CREATURE, List.of(
                        new SpawnSettings.SpawnEntry(AetherEntityTypes.AERBUNNY, 12, 4, 4),
                        new SpawnSettings.SpawnEntry(AetherEntityTypes.MOA, 10, 1, 5),
                        new SpawnSettings.SpawnEntry(AetherEntityTypes.AERWHALE, 6, 1, 3)
                    )
                ),
                
                Map.of(
                    AetherEntityTypes.BLUE_SWET, SpawnCost.of(1, 0.12),
                    AetherEntityTypes.AECHOR_PLANT, SpawnCost.of(0.75, 0.07)
                )
            ))
            .precipitation(Biome.Precipitation.RAIN).temperature(0.4F).downfall(2)
            .category(Biome.Category.FOREST)
            .build();
    }
    
    private static Biome createAetherWisteriaWoods(){
        /*
        TODO
        {
          "starts": [
            "the_aether:well",
            "the_aether:skyroot_tower",
            "the_aether:orange_ruin"
          ],
          "parent": "the_aether:aether_highlands_forest",
          "player_spawn_friendly": false,
          "depth": 0.155,
          "scale": 0.15
        }
         */
        return new Biome.Builder()
            .effects(createBiomeEffects(0xC0C0FF, 0xF3FF81, 0x88EF9D, 0xFFFFFF, 0xA9F7FF, 0x58E5FF))
            .generationSettings(createGenerationSettings(
                getAetherCarvers(),
    
                mergeFeatures(
                    getStandardAetherFeatures(),
                    Map.of(
                        GenerationStep.Feature.LOCAL_MODIFICATIONS, List.of(
                            AetherPlacedFeatures.QUICKSOIL
                        ),
                        GenerationStep.Feature.VEGETAL_DECORATION, List.of(
                            AetherPlacedFeatures.RAINBOW_MALT_SPRIGS,
                            AetherPlacedFeatures.AETHER_FLOWERS,
                            AetherPlacedFeatures.AETHER_GRASS,
                            AetherPlacedFeatures.FALLEN_ROSE_LEAVES,
                            AetherPlacedFeatures.FALLEN_LAVENDER_LEAVES,
                            AetherPlacedFeatures.AETHER_BUSH,
                            AetherPlacedFeatures.PATCH_BLUEBERRY,
                            AetherPlacedFeatures.RAINBOW_FOREST_TREES
                        )
                    )
                )
                ))
            .spawnSettings(createSpawnSettings(
                Map.of(
                    SpawnGroup.MONSTER, List.of(
                        new SpawnSettings.SpawnEntry(AetherEntityTypes.BLUE_SWET, 30, 1, 3),
                        new SpawnSettings.SpawnEntry(AetherEntityTypes.AECHOR_PLANT, 100, 3, 7)
                    ),
                    
                    SpawnGroup.CREATURE, List.of(
                        new SpawnSettings.SpawnEntry(AetherEntityTypes.AERBUNNY, 12, 4, 4),
                        new SpawnSettings.SpawnEntry(AetherEntityTypes.MOA, 10, 1, 5),
                        new SpawnSettings.SpawnEntry(AetherEntityTypes.AERWHALE, 6, 1, 3)
                    )
                ),
                
                Map.of(
                    AetherEntityTypes.BLUE_SWET, SpawnCost.of(1, 0.12),
                    AetherEntityTypes.AECHOR_PLANT, SpawnCost.of(0.7, 0.1)
                )
            ))
            .precipitation(Biome.Precipitation.RAIN).temperature(0.5F).downfall(1)
            .category(Biome.Category.FOREST)
            .build();
    }
    
    private static Biome createAutumnalTundra(){
        /*
        TODO
        {
          "starts": [
            "the_aether:well",
            "the_aether:skyroot_tower",
            "the_aether:orange_ruin"
          ],
          "player_spawn_friendly": false,
          "depth": 0.1,
          "scale": 0.025
        }
         */
        return new Biome.Builder()
            .effects(createBiomeEffects(0xC0C0FF, 0xFFB485, 0xFFFFFF, 0xFFFFFF, 0x8CE6FF, 0x8CE6FF))
            .generationSettings(createGenerationSettings(
                getAetherCarvers(),
    
                mergeFeatures(
                    getStandardAetherFeatures(),
                    Map.of(
                        GenerationStep.Feature.LAKES, List.of(
                            AetherPlacedFeatures.TUNDRA_PONDS,
                            AetherPlacedFeatures.TUNDRA_SNOW
                        ),
                        GenerationStep.Feature.LOCAL_MODIFICATIONS, List.of(
                            AetherPlacedFeatures.QUICKSOIL,
                            AetherPlacedFeatures.GENERIC_BOULDER
                        ),
                        GenerationStep.Feature.FLUID_SPRINGS, List.of(
                            AetherPlacedFeatures.WATER_SPRING
                        ),
                        GenerationStep.Feature.VEGETAL_DECORATION, List.of(
                            AetherPlacedFeatures.TUNDRA_SPIRES,
                            AetherPlacedFeatures.TUNDRA_FOLIAGE,
                            AetherPlacedFeatures.MIXED_TREES,
                            AetherPlacedFeatures.FLUTEGRASS
                        ),
                        GenerationStep.Feature.TOP_LAYER_MODIFICATION, List.of(
                            MiscPlacedFeatures.FREEZE_TOP_LAYER
                        )
                    )
                )
            ))
            .spawnSettings(createSpawnSettings(
                Map.of(
                    SpawnGroup.MONSTER, List.of(
                        new SpawnSettings.SpawnEntry(AetherEntityTypes.BLUE_SWET, 100, 2, 6)
                    ),
                    SpawnGroup.CREATURE, List.of(
                        new SpawnSettings.SpawnEntry(AetherEntityTypes.AERBUNNY, 12, 4, 4),
                        new SpawnSettings.SpawnEntry(AetherEntityTypes.MOA, 6, 5, 13),
                        new SpawnSettings.SpawnEntry(AetherEntityTypes.AERBUNNY, 6, 1, 3)
                    )
                ),
                
                Map.of(
                    AetherEntityTypes.BLUE_SWET, SpawnCost.of(1, 0.12)
                )
            ))
            .precipitation(Biome.Precipitation.RAIN).temperature(0.1F).downfall(2)
            .category(Biome.Category.TAIGA)
            .build();
    }
    
    private static Biome createContinentalPlateau(){
        /*
        TODO
        {
          "starts": [
            "the_aether:well",
            "the_aether:skyroot_tower",
            "the_aether:orange_ruin"
          ],
          "player_spawn_friendly": false,
          "depth": 0.0,
          "scale": 0.05
        }
         */
        return new Biome.Builder()
            .effects(createBiomeEffects(0xC0C0FF, 0xDCFFCC, 0x91E2C8, 0xFFFFFF, 0x58E5FF, 0x58E5FF))
            .generationSettings(createGenerationSettings(
                getAetherCarvers(),
    
                mergeFeatures(
                    getStandardAetherFeatures(),
                    Map.of(
                        GenerationStep.Feature.LOCAL_MODIFICATIONS, List.of(
                            AetherPlacedFeatures.QUICKSOIL,
                            AetherPlacedFeatures.PLAINS_BOULDER
                        ),
                        GenerationStep.Feature.FLUID_SPRINGS, List.of(
                            AetherPlacedFeatures.WATER_SPRING
                        ),
                        GenerationStep.Feature.VEGETAL_DECORATION, List.of(
                            AetherPlacedFeatures.AETHER_FLOWERS,
                            AetherPlacedFeatures.AETHER_GRASS,
                            AetherPlacedFeatures.AETHER_TALL_GRASS,
                            AetherPlacedFeatures.PATCH_BLUEBERRY,
                            AetherPlacedFeatures.PLATEAU_FOLIAGE,
                            AetherPlacedFeatures.PLATEAU_SHAMROCK,
                            AetherPlacedFeatures.PLATEAU_FLOWERING_GRASS,
                            AetherPlacedFeatures.PLATEAU_TREES,
                            AetherPlacedFeatures.FLUTEGRASS
                        )
                    )
                )
            ))
            .spawnSettings(createSpawnSettings(
                Map.of(
                    SpawnGroup.MONSTER, List.of(
                        new SpawnSettings.SpawnEntry(AetherEntityTypes.BLUE_SWET, 100, 2, 6)
                    ),
                    SpawnGroup.CREATURE, List.of(
                        new SpawnSettings.SpawnEntry(AetherEntityTypes.AERBUNNY, 12, 4, 4),
                        new SpawnSettings.SpawnEntry(AetherEntityTypes.MOA, 6, 5, 13),
                        new SpawnSettings.SpawnEntry(AetherEntityTypes.AERBUNNY, 6, 1, 3)
                    )
                ),
            
                Map.of(
                    AetherEntityTypes.BLUE_SWET, SpawnCost.of(1, 0.12)
                )
            ))
            .precipitation(Biome.Precipitation.RAIN).temperature(0.25F).downfall(0)
            .category(Biome.Category.PLAINS)
            .build();
    }
    
    private static Biome createHighlandsShield(){
        /*
        TODO
        {
          "starts": [
            "the_aether:well",
            "the_aether:skyroot_tower",
            "the_aether:orange_ruin"
          ],
          "player_spawn_friendly": false,
          "depth": 0.0,
          "scale": 0.035
        }
         */
        return new Biome.Builder()
            .effects(createBiomeEffects(0xC0C0FF, 0xFFF781, 0xDCFFB0, 0xFFFFFF, 0x58E5FF, 0x58E5FF))
            .generationSettings(createGenerationSettings(
                getAetherCarvers(),
    
                mergeFeatures(
                    getStandardAetherFeatures(),
                    Map.of(
                        GenerationStep.Feature.LAKES, List.of(
                            AetherPlacedFeatures.SHIELD_PONDS
                        ),
                        GenerationStep.Feature.LOCAL_MODIFICATIONS, List.of(
                            AetherPlacedFeatures.QUICKSOIL,
                            AetherPlacedFeatures.GENERIC_BOULDER
                        ),
                        GenerationStep.Feature.STRONGHOLDS, List.of(
                            AetherPlacedFeatures.SHIELD_STONE
                        ),
                        GenerationStep.Feature.FLUID_SPRINGS, List.of(
                            AetherPlacedFeatures.WATER_SPRING
                        ),
                        GenerationStep.Feature.VEGETAL_DECORATION, List.of(
                            AetherPlacedFeatures.SHIELD_STUMPS,
                            AetherPlacedFeatures.SHIELD_HOLLOW_STUMPS,
                            AetherPlacedFeatures.MOTTLED_FALLEN_LOG,
                            AetherPlacedFeatures.MOTTLED_HOLLOW_FALLEN_LOG,
                            AetherPlacedFeatures.AETHER_FLOWERS,
                            AetherPlacedFeatures.SHIELD_FLAX,
                            AetherPlacedFeatures.SHIELD_NETTLES,
                            AetherPlacedFeatures.SHIELD_FOLIAGE,
                            AetherPlacedFeatures.AETHER_GRASS,
                            AetherPlacedFeatures.AETHER_TALL_GRASS,
                            AetherPlacedFeatures.WATER_SPRING,
                            AetherPlacedFeatures.SHIELD_TREES,
                            AetherPlacedFeatures.DENSE_SHIELD_TREES,
                            AetherPlacedFeatures.SHIELD_ROCKS,
                            AetherPlacedFeatures.SHIELD_FALLEN_LEAVES,
                            AetherPlacedFeatures.FLUTEGRASS
                        )
                    )
                )
            ))
            .spawnSettings(createSpawnSettings(
                Map.of(
                    SpawnGroup.MONSTER, List.of(
                        new SpawnSettings.SpawnEntry(AetherEntityTypes.BLUE_SWET, 100, 2, 6),
                        new SpawnSettings.SpawnEntry(AetherEntityTypes.AECHOR_PLANT, 50, 1, 3)
                    ),
                    SpawnGroup.CREATURE, List.of(
                        new SpawnSettings.SpawnEntry(AetherEntityTypes.AERBUNNY, 12, 4, 4),
                        new SpawnSettings.SpawnEntry(AetherEntityTypes.MOA, 6, 5, 13),
                        new SpawnSettings.SpawnEntry(AetherEntityTypes.AERBUNNY, 6, 1, 3)
                    )
                ),
                Map.of(
                    AetherEntityTypes.BLUE_SWET, SpawnCost.of(1, 0.12),
                    AetherEntityTypes.AECHOR_PLANT, SpawnCost.of(1, 0.1)
                )
            ))
            .precipitation(Biome.Precipitation.RAIN).temperature(0.35F).downfall(2)
            .category(Biome.Category.PLAINS)
            .build();
    }
}
