package net.id.paradise_lost.world.dimension;

import net.id.paradise_lost.registry.EntityRegistry;
import net.id.paradise_lost.util.ParadiseLostSoundEvents;
import net.id.paradise_lost.world.feature.placed_features.ParadiseLostMiscPlacedFeatures;
import net.id.paradise_lost.world.feature.placed_features.ParadiseLostTreePlacedFeatures;
import net.id.paradise_lost.world.feature.placed_features.ParadiseLostVegetationPlacedFeatures;
import net.id.paradise_lost.world.gen.carver.ParadiseLostConfiguredCarvers;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.MiscOverworldPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import static net.id.paradise_lost.ModConstants.id;

public final class ParadiseLostBiomes {
    public static final ResourceKey<Biome> AUTUMNAL_TUNDRA_KEY = of("autumnal_tundra");
    public static final ResourceKey<Biome> CALCITE_CRAGLANDS_KEY = of("calcite_craglands");
    public static final ResourceKey<Biome> CONTINENTAL_PLATEAU_KEY = of("continental_plateau");
    public static final ResourceKey<Biome> HIGHLANDS_PLAINS_KEY = of("highlands");
    public static final ResourceKey<Biome> HIGHLANDS_FOREST_KEY = of("highlands_forest");
    public static final ResourceKey<Biome> HIGHLANDS_GRAND_GLADE_KEY = of("highlands_grand_glade");
    public static final ResourceKey<Biome> HIGHLANDS_SHIELD_KEY = of("highlands_shield");
    public static final ResourceKey<Biome> HIGHLANDS_THICKET_KEY = of("highlands_thicket");
    public static final ResourceKey<Biome> TRADEWINDS_KEY = of("tradewinds");
    public static final ResourceKey<Biome> WISTERIA_WOODS_KEY = of("wisteria_woods");

    public static void init() {
    }

    private static ResourceKey<Biome> of(String name) {
        return ResourceKey.create(Registries.BIOME, id(name));
    }

    public static void bootstrap(BootstrapContext<Biome> context) {
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);

        context.register(AUTUMNAL_TUNDRA_KEY, new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .temperature(0.1f)
                .downfall(0.8f)
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .fogColor(16777215)
                        .waterColor(9234175)
                        .waterFogColor(10611455)
                        .skyColor(12632319)
                        .foliageColorOverride(16769162)
                        .grassColorOverride(13562594)
                        .backgroundMusic(PARADISE_MUSIC)
                        .build())
                .mobSpawnSettings(new MobSpawnSettings.Builder()
                        .addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityRegistry.MOA.get(), 6, 2, 4))
                        .addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityRegistry.ENVOY.get(), 50, 2, 3))
                        .addMobCharge(EntityRegistry.ENVOY.get(), 0.7d, 0.2d)
                        .build())
                .generationSettings(baseGen(placedFeatures, carvers, true, true, RiverStyle.ICE)
                        .addFeature(GenerationStep.Decoration.LAKES, ParadiseLostMiscPlacedFeatures.TUNDRA_PONDS)
                        .addFeature(GenerationStep.Decoration.LAKES, ParadiseLostMiscPlacedFeatures.TUNDRA_SNOW)
                        .addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, ParadiseLostMiscPlacedFeatures.GENERIC_BOULDER)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostVegetationPlacedFeatures.NATURAL_SWEDROOT)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostMiscPlacedFeatures.TUNDRA_SPIRES)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostVegetationPlacedFeatures.TUNDRA_FOLIAGE)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostTreePlacedFeatures.MIXED_TREES)
                        .build())
                .build());

        context.register(CALCITE_CRAGLANDS_KEY, new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .temperature(0.4f)
                .downfall(0.8f)
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .fogColor(16777215)
                        .waterColor(5826047)
                        .waterFogColor(6547455)
                        .skyColor(12632319)
                        .foliageColorOverride(15859609)
                        .grassColorOverride(11066843)
                        .backgroundMusic(PARADISE_MUSIC)
                        .build())
                .mobSpawnSettings(new MobSpawnSettings.Builder()
                        .addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityRegistry.MOA.get(), 6, 1, 3))
                        .addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityRegistry.POPOM.get(), 4, 2, 3))
                        .addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityRegistry.ENVOY.get(), 50, 2, 3))
                        .addMobCharge(EntityRegistry.ENVOY.get(), 0.7d, 0.2d)
                        .build())
                .generationSettings(baseGen(placedFeatures, carvers)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostVegetationPlacedFeatures.NATURAL_SWEDROOT)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostVegetationPlacedFeatures.GRASS)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostVegetationPlacedFeatures.PATCH_BLACKCURRANT)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostTreePlacedFeatures.CRAGLANDS_TREES)
                        .build())
                .build());

        context.register(CONTINENTAL_PLATEAU_KEY, new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .temperature(0.25f)
                .downfall(0.8f)
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .fogColor(16777215)
                        .waterColor(9234175)
                        .waterFogColor(10611455)
                        .skyColor(12632319)
                        .foliageColorOverride(16769162)
                        .grassColorOverride(13562594)
                        .backgroundMusic(PARADISE_MUSIC)
                        .build())
                .mobSpawnSettings(new MobSpawnSettings.Builder()
                        .addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityRegistry.MOA.get(), 6, 2, 4))
                        .addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityRegistry.ENVOY.get(), 50, 2, 3))
                        .addMobCharge(EntityRegistry.ENVOY.get(), 0.7d, 0.2d)
                        .build())
                .generationSettings(baseGen(placedFeatures, carvers)
                        .addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, ParadiseLostMiscPlacedFeatures.PLAINS_BOULDER)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostVegetationPlacedFeatures.NATURAL_SWEDROOT)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostVegetationPlacedFeatures.FLOWERS)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostVegetationPlacedFeatures.GRASS)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostVegetationPlacedFeatures.TALL_GRASS)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostVegetationPlacedFeatures.PLATEAU_FOLIAGE)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostVegetationPlacedFeatures.PLATEAU_SHAMROCK)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostVegetationPlacedFeatures.PLATEAU_FLOWERING_GRASS)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostTreePlacedFeatures.PLATEAU_TREES)
                        .build())
                .build());

        context.register(HIGHLANDS_PLAINS_KEY, new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .temperature(0.5f)
                .downfall(0.8f)
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .fogColor(16777215)
                        .waterColor(5826047)
                        .waterFogColor(6547455)
                        .skyColor(12632319)
                        .foliageColorOverride(15859609)
                        .grassColorOverride(11066822)
                        .backgroundMusic(PARADISE_MUSIC)
                        .build())
                .mobSpawnSettings(new MobSpawnSettings.Builder()
                        .addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityRegistry.MOA.get(), 6, 2, 4))
                        .addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityRegistry.POPOM.get(), 4, 2, 3))
                        .addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityRegistry.ENVOY.get(), 50, 2, 3))
                        .addMobCharge(EntityRegistry.ENVOY.get(), 0.7d, 0.2d)
                        .build())
                .generationSettings(baseGen(placedFeatures, carvers)
                        .addFeature(GenerationStep.Decoration.LAKES, ParadiseLostMiscPlacedFeatures.PLAINS_BOULDER)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostVegetationPlacedFeatures.NATURAL_SWEDROOT)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostVegetationPlacedFeatures.FLOWERS)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostVegetationPlacedFeatures.GRASS)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostVegetationPlacedFeatures.TALL_GRASS)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostVegetationPlacedFeatures.PATCH_BLACKCURRANT)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostTreePlacedFeatures.SPARSE_TREES)
                        .build())
                .build());

        context.register(HIGHLANDS_FOREST_KEY, new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .temperature(0.5f)
                .downfall(0.8f)
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .fogColor(15134207)
                        .waterColor(5826047)
                        .waterFogColor(6547455)
                        .skyColor(12632319)
                        .foliageColorOverride(14017403)
                        .grassColorOverride(9229492)
                        .backgroundMusic(PARADISE_MUSIC)
                        .build())
                .mobSpawnSettings(new MobSpawnSettings.Builder()
                        .addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityRegistry.MOA.get(), 4, 2, 4))
                        .addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityRegistry.POPOM.get(), 6, 1, 3))
                        .addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityRegistry.ENVOY.get(), 50, 2, 3))
                        .addMobCharge(EntityRegistry.ENVOY.get(), 0.7d, 0.2d)
                        .build())
                .generationSettings(baseGen(placedFeatures, carvers)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostVegetationPlacedFeatures.NATURAL_SWEDROOT)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostVegetationPlacedFeatures.FLOWERS_FOREST)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostVegetationPlacedFeatures.TALL_GRASS)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostVegetationPlacedFeatures.TUNDRA_FOLIAGE)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostVegetationPlacedFeatures.BUSH)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostVegetationPlacedFeatures.PATCH_BLACKCURRANT)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostTreePlacedFeatures.SCATTERED_TREES)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostTreePlacedFeatures.FALLEN_LEAVES)
                        .build())
                .build());

        context.register(HIGHLANDS_GRAND_GLADE_KEY, new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .temperature(0.4f)
                .downfall(0.8f)
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .fogColor(16777215)
                        .waterColor(5826047)
                        .waterFogColor(6547455)
                        .skyColor(12632319)
                        .foliageColorOverride(15597437)
                        .grassColorOverride(7575944)
                        .backgroundMusic(PARADISE_MUSIC)
                        .build())
                .mobSpawnSettings(new MobSpawnSettings.Builder()
                        .addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityRegistry.MOA.get(), 6, 2, 4))
                        .addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityRegistry.ENVOY.get(), 50, 1, 3))
                        .addMobCharge(EntityRegistry.ENVOY.get(), 0.7d, 0.2d)
                        .build())
                .generationSettings(baseGen(placedFeatures, carvers)
                        .addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, ParadiseLostMiscPlacedFeatures.THICKET_BOULDER)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostVegetationPlacedFeatures.NATURAL_SWEDROOT)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostTreePlacedFeatures.THICKET_FALLEN_LOG)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostVegetationPlacedFeatures.GRASS)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostVegetationPlacedFeatures.PATCH_BROWN_SPORECAP_COMMON)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostVegetationPlacedFeatures.TALL_GRASS)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostVegetationPlacedFeatures.DENSE_BUSH)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostTreePlacedFeatures.SPARSE_TREES)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostTreePlacedFeatures.FALLEN_LEAVES)
                        .build())
                .build());

        context.register(HIGHLANDS_SHIELD_KEY, new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .temperature(0.6f)
                .downfall(0.8f)
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .fogColor(16777215)
                        .waterColor(4908287)
                        .waterFogColor(6547455)
                        .skyColor(12635902)
                        .foliageColorOverride(16441439)
                        .grassColorOverride(9826514)
                        .backgroundMusic(PARADISE_MUSIC)
                        .build())
                .mobSpawnSettings(new MobSpawnSettings.Builder()
                        .addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityRegistry.MOA.get(), 6, 1, 3))
                        .addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityRegistry.ENVOY.get(), 50, 1, 3))
                        .addMobCharge(EntityRegistry.ENVOY.get(), 0.7d, 0.2d)
                        .build())
                .generationSettings(baseGen(placedFeatures, carvers)
                        .addFeature(GenerationStep.Decoration.LAKES, ParadiseLostMiscPlacedFeatures.SHIELD_PONDS)
                        .addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, ParadiseLostMiscPlacedFeatures.GENERIC_BOULDER)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostVegetationPlacedFeatures.NATURAL_SWEDROOT)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostTreePlacedFeatures.SHIELD_STUMPS)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostTreePlacedFeatures.SHIELD_HOLLOW_STUMPS)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostTreePlacedFeatures.MOTTLED_HOLLOW_FALLEN_LOG)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostVegetationPlacedFeatures.FLOWERS)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostVegetationPlacedFeatures.SHIELD_FLAX)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostVegetationPlacedFeatures.SHIELD_NETTLES)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostVegetationPlacedFeatures.SHIELD_FOLIAGE)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostVegetationPlacedFeatures.GRASS)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostVegetationPlacedFeatures.TALL_GRASS)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostTreePlacedFeatures.SHIELD_TREES)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostTreePlacedFeatures.DENSE_SHIELD_TREES)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostMiscPlacedFeatures.SHIELD_ROCKS)
                        .build())
                .build());

        context.register(HIGHLANDS_THICKET_KEY, new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .temperature(0.4f)
                .downfall(0.8f)
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .fogColor(16777215)
                        .waterColor(5826047)
                        .waterFogColor(6547455)
                        .skyColor(12632319)
                        .foliageColorOverride(15597437)
                        .grassColorOverride(7575944)
                        .backgroundMusic(PARADISE_MUSIC)
                        .build())
                .mobSpawnSettings(new MobSpawnSettings.Builder()
                        .addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityRegistry.MOA.get(), 6, 1, 3))
                        .addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityRegistry.ENVOY.get(), 50, 2, 4))
                        .addMobCharge(EntityRegistry.ENVOY.get(), 0.7d, 0.2d)
                        .build())
                .generationSettings(baseGen(placedFeatures, carvers)
                        .addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, ParadiseLostMiscPlacedFeatures.THICKET_BOULDER)
                        .addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, ParadiseLostMiscPlacedFeatures.GOLDEN_BOULDER)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostVegetationPlacedFeatures.NATURAL_SWEDROOT)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostVegetationPlacedFeatures.FLOWERS_THICKET)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostVegetationPlacedFeatures.GRASS)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostVegetationPlacedFeatures.THICKET_SHAMROCK)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostVegetationPlacedFeatures.THICKET_LIVERWORT_CARPET)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostVegetationPlacedFeatures.TALL_GRASS)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostVegetationPlacedFeatures.DENSE_BUSH)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostTreePlacedFeatures.THICKET_TREES)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostTreePlacedFeatures.FALLEN_LEAVES)
                        .build())
                .build());

        context.register(TRADEWINDS_KEY, new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .temperature(0.5f)
                .downfall(0.8f)
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .fogColor(16777215)
                        .waterColor(5826047)
                        .waterFogColor(6547455)
                        .skyColor(12632319)
                        .foliageColorOverride(15859609)
                        .grassColorOverride(9292207)
                        .backgroundMusic(PARADISE_MUSIC)
                        .build())
                .mobSpawnSettings(new MobSpawnSettings.Builder()
                        .addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityRegistry.MOA.get(), 6, 2, 4))
                        .addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityRegistry.ENVOY.get(), 50, 1, 3))
                        .addMobCharge(EntityRegistry.ENVOY.get(), 0.7d, 0.2d)
                        .build())
                .generationSettings(baseGen(placedFeatures, carvers, false, true, RiverStyle.NONE)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostVegetationPlacedFeatures.NATURAL_SWEDROOT)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostVegetationPlacedFeatures.TALL_GRASS)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostVegetationPlacedFeatures.BUSH)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostTreePlacedFeatures.PLATEAU_TREES)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostTreePlacedFeatures.SPARSE_TREES)
                        .build())
                .build());

        context.register(WISTERIA_WOODS_KEY, new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .temperature(0.5f)
                .downfall(0.8f)
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .fogColor(16777215)
                        .waterColor(5826047)
                        .waterFogColor(6547455)
                        .skyColor(12632319)
                        .foliageColorOverride(15859609)
                        .grassColorOverride(10868417)
                        .backgroundMusic(PARADISE_MUSIC)
                        .build())
                .mobSpawnSettings(new MobSpawnSettings.Builder()
                        .addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityRegistry.MOA.get(), 6, 2, 4))
                        .addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityRegistry.POPOM.get(), 3, 2, 3))
                        .addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityRegistry.ENVOY.get(), 50, 1, 3))
                        .addMobCharge(EntityRegistry.ENVOY.get(), 0.7d, 0.2d)
                        .build())
                .generationSettings(baseGen(placedFeatures, carvers)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostVegetationPlacedFeatures.NATURAL_SWEDROOT)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostVegetationPlacedFeatures.FLOWERS_FOREST)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostVegetationPlacedFeatures.GRASS)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostTreePlacedFeatures.FALLEN_ROSE_LEAVES)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostTreePlacedFeatures.FALLEN_LAVENDER_LEAVES)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostVegetationPlacedFeatures.BUSH)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostVegetationPlacedFeatures.PATCH_BLACKCURRANT)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostTreePlacedFeatures.RAINBOW_FOREST_TREES)
                        .build())
                .build());
    }

    private static final Music PARADISE_MUSIC = new Music(Holder.direct(ParadiseLostSoundEvents.MUSIC_PARADISE_LOST), 12000, 24000, false);
    private static final ResourceKey<ConfiguredWorldCarver<?>> CAVE = ResourceKey.create(Registries.CONFIGURED_CARVER, ResourceLocation.withDefaultNamespace("cave"));

    /** Noise rivers cut land like vanilla; NONE for void/tradewinds. */
    private enum RiverStyle { NONE, WATER, ICE }

    private static BiomeGenerationSettings.Builder baseGen(HolderGetter<PlacedFeature> features, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        return baseGen(features, carvers, true, true, RiverStyle.WATER);
    }

    private static BiomeGenerationSettings.Builder baseGen(
            HolderGetter<PlacedFeature> features,
            HolderGetter<ConfiguredWorldCarver<?>> carvers,
            boolean calciteBlob,
            boolean fluidSprings,
            RiverStyle riverStyle
    ) {
        var builder = new BiomeGenerationSettings.Builder(features, carvers)
                .addCarver(GenerationStep.Carving.AIR, CAVE)
                .addCarver(GenerationStep.Carving.AIR, ParadiseLostConfiguredCarvers.LARGE_COLD_CLOUD)
                .addCarver(GenerationStep.Carving.AIR, ParadiseLostConfiguredCarvers.COLD_CLOUD)
                .addCarver(GenerationStep.Carving.AIR, ParadiseLostConfiguredCarvers.TINY_COLD_CLOUD)
                .addCarver(GenerationStep.Carving.AIR, ParadiseLostConfiguredCarvers.LARGE_BLUE_CLOUD)
                .addCarver(GenerationStep.Carving.AIR, ParadiseLostConfiguredCarvers.BLUE_CLOUD)
                .addCarver(GenerationStep.Carving.AIR, ParadiseLostConfiguredCarvers.TINY_BLUE_CLOUD)
                .addCarver(GenerationStep.Carving.AIR, ParadiseLostConfiguredCarvers.LARGE_GOLDEN_CLOUD)
                .addCarver(GenerationStep.Carving.AIR, ParadiseLostConfiguredCarvers.GOLDEN_CLOUD)
                .addCarver(GenerationStep.Carving.AIR, ParadiseLostConfiguredCarvers.TINY_GOLDEN_CLOUD)
                .addFeature(GenerationStep.Decoration.RAW_GENERATION, ParadiseLostMiscPlacedFeatures.SURTRUM_METEORITE);
        if (calciteBlob) {
            builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ParadiseLostMiscPlacedFeatures.CALCITE_BLOB);
        }
        // Noise-contour rivers: once per chunk, before vegetation (zip LOCAL_MODIFICATIONS)
        if (riverStyle == RiverStyle.WATER) {
            builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, ParadiseLostMiscPlacedFeatures.RIVER_CHANNEL);
            builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostTreePlacedFeatures.RIVERBANK_TREES);
        } else if (riverStyle == RiverStyle.ICE) {
            builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, ParadiseLostMiscPlacedFeatures.FROZEN_RIVER_CHANNEL);
            builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostTreePlacedFeatures.FROZEN_RIVERBANK_TREES);
        }
        builder
                .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ParadiseLostMiscPlacedFeatures.HELIOLITH_BLOB)
                .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ParadiseLostMiscPlacedFeatures.LEVITA_BLOB)
                .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ParadiseLostMiscPlacedFeatures.ORE_CHERINE)
                .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ParadiseLostMiscPlacedFeatures.ORE_LEVITA)
                .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ParadiseLostMiscPlacedFeatures.ORE_OLVITE)
                .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ParadiseLostMiscPlacedFeatures.ORE_FLOESTONE_REDSTONE);
        if (fluidSprings) {
            builder.addFeature(GenerationStep.Decoration.FLUID_SPRINGS, ParadiseLostMiscPlacedFeatures.WATER_SPRING);
        }
        return builder
                .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostVegetationPlacedFeatures.PATCH_BROWN_SPORECAP)
                .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ParadiseLostVegetationPlacedFeatures.PATCH_PINK_SPORECAP)
                .addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, MiscOverworldPlacements.FREEZE_TOP_LAYER);
    }

}
