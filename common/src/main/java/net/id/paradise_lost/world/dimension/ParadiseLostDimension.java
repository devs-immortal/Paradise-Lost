package net.id.paradise_lost.world.dimension;

import com.mojang.datafixers.util.Pair;
import net.id.paradise_lost.ParadiseLost;
import net.id.paradise_lost.world.gen.noise.ParadiseLostNoiseSettings;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorPreset;

import java.util.List;
import net.id.paradise_lost.world.portal.ParadiseLostPoi;

public class ParadiseLostDimension {
    public static final ResourceKey<Level> PARADISE_LOST_WORLD_KEY = key(Registries.DIMENSION, ParadiseLost.MOD_ID);
    public static final ResourceKey<DimensionType> DIMENSION_TYPE = key(Registries.DIMENSION_TYPE, ParadiseLost.MOD_ID);
    public static final ResourceKey<LevelStem> OPTIONS_KEY = key(Registries.LEVEL_STEM, ParadiseLost.MOD_ID);
    public static final ResourceKey<FlatLevelGeneratorPreset> SUPERFLAT_PRESET = key(Registries.FLAT_LEVEL_GENERATOR_PRESET, ParadiseLost.MOD_ID);
    
    private static DimensionType dimensionType;
    
    private static <T> ResourceKey<T> key(ResourceKey<? extends Registry<T>> registry, String name) {
        return ResourceKey.create(registry, ParadiseLost.locate(name));
    }
    
    public static void init() {
    }

    public static void initPortal() {
        ParadiseLostPoi.init();
    }

    @Deprecated
    public static DimensionType getDimensionType() {
        return dimensionType;
    }

    public static void bootstrap(BootstrapContext<LevelStem> context) {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<DimensionType> dimensionTypes = context.lookup(Registries.DIMENSION_TYPE);
        HolderGetter<NoiseGeneratorSettings> noiseSettings = context.lookup(Registries.NOISE_SETTINGS);

        Climate.ParameterList<Holder<Biome>> parameterList = new Climate.ParameterList<>(List.of(
                Pair.of(Climate.parameters(Climate.Parameter.span(-1.0f, 1.0f), Climate.Parameter.span(-1.0f, 1.0f), Climate.Parameter.span(-1.2f, -1.05f), Climate.Parameter.span(-1.0f, 1.0f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.TRADEWINDS_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(-1.0f, -0.45f), Climate.Parameter.span(-1.0f, -0.1f), Climate.Parameter.span(-1.05f, 1.0f), Climate.Parameter.span(-1.0f, 1.0f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.CONTINENTAL_PLATEAU_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(-1.0f, -0.45f), Climate.Parameter.span(-0.1f, 0.3f), Climate.Parameter.span(-1.05f, -0.19f), Climate.Parameter.span(-1.0f, 0.05f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_PLAINS_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(-1.0f, -0.45f), Climate.Parameter.span(0.3f, 1.0f), Climate.Parameter.span(-1.05f, 1.0f), Climate.Parameter.span(-1.0f, 1.0f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.AUTUMNAL_TUNDRA_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(-0.45f, 0.55f), Climate.Parameter.span(-1.0f, -0.1f), Climate.Parameter.span(-1.05f, 0.03f), Climate.Parameter.span(-1.0f, 1.0f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_PLAINS_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(-0.45f, 0.2f), Climate.Parameter.span(-0.1f, 0.7f), Climate.Parameter.span(-1.05f, -0.19f), Climate.Parameter.span(-1.0f, 0.05f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.CALCITE_CRAGLANDS_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(-0.45f, 0.2f), Climate.Parameter.span(0.7f, 1.0f), Climate.Parameter.span(-1.05f, -0.19f), Climate.Parameter.span(-1.0f, 0.05f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_FOREST_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(0.2f, 0.55f), Climate.Parameter.span(-0.1f, 0.1f), Climate.Parameter.span(-1.05f, -0.19f), Climate.Parameter.span(-1.0f, 0.05f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.CALCITE_CRAGLANDS_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(0.2f, 0.55f), Climate.Parameter.span(0.1f, 0.3f), Climate.Parameter.span(-1.05f, 1.0f), Climate.Parameter.span(-1.0f, 1.0f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_FOREST_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(0.2f, 0.55f), Climate.Parameter.span(0.3f, 0.7f), Climate.Parameter.span(-1.05f, 0.03f), Climate.Parameter.span(-1.0f, 1.0f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_FOREST_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(0.2f, 1.0f), Climate.Parameter.span(0.7f, 1.0f), Climate.Parameter.span(-1.05f, -0.19f), Climate.Parameter.span(-1.0f, 0.05f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_THICKET_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(0.55f, 1.0f), Climate.Parameter.span(-1.0f, -0.1f), Climate.Parameter.span(-1.05f, -0.19f), Climate.Parameter.span(-1.0f, 0.05f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_FOREST_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(0.55f, 1.0f), Climate.Parameter.span(-0.1f, 0.3f), Climate.Parameter.span(-1.05f, 0.03f), Climate.Parameter.span(-1.0f, 1.0f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_THICKET_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(0.55f, 1.0f), Climate.Parameter.span(0.3f, 0.7f), Climate.Parameter.span(-1.05f, 0.03f), Climate.Parameter.span(-1.0f, 1.0f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_GRAND_GLADE_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(-1.0f, -0.45f), Climate.Parameter.span(-0.1f, 0.3f), Climate.Parameter.span(-1.05f, 0.03f), Climate.Parameter.span(0.05f, 1.0f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.CONTINENTAL_PLATEAU_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(-0.45f, -0.15f), Climate.Parameter.span(-0.1f, 1.0f), Climate.Parameter.span(-1.05f, 0.03f), Climate.Parameter.span(0.05f, 1.0f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_SHIELD_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(-0.15f, 0.55f), Climate.Parameter.span(-0.1f, 0.1f), Climate.Parameter.span(-1.05f, 0.03f), Climate.Parameter.span(0.05f, 1.0f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_PLAINS_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(-0.15f, 0.2f), Climate.Parameter.span(0.1f, 1.0f), Climate.Parameter.span(-1.05f, 0.03f), Climate.Parameter.span(0.05f, 1.0f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_SHIELD_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(0.2f, 0.55f), Climate.Parameter.span(0.7f, 1.0f), Climate.Parameter.span(-1.05f, 0.03f), Climate.Parameter.span(0.05f, 1.0f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_FOREST_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(0.55f, 1.0f), Climate.Parameter.span(-1.0f, -0.1f), Climate.Parameter.span(-1.05f, 1.0f), Climate.Parameter.span(0.05f, 1.0f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_GRAND_GLADE_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(0.55f, 1.0f), Climate.Parameter.span(0.7f, 1.0f), Climate.Parameter.span(-1.05f, 1.0f), Climate.Parameter.span(0.05f, 1.0f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_GRAND_GLADE_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(-1.0f, -0.45f), Climate.Parameter.span(-0.1f, 0.3f), Climate.Parameter.span(-0.19f, 0.03f), Climate.Parameter.span(-1.0f, -0.2225f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_PLAINS_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(-0.45f, 0.2f), Climate.Parameter.span(-0.1f, 0.7f), Climate.Parameter.span(-0.19f, 0.03f), Climate.Parameter.span(-1.0f, -0.2225f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.CALCITE_CRAGLANDS_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(-0.45f, 0.2f), Climate.Parameter.span(0.7f, 1.0f), Climate.Parameter.span(-0.19f, 1.0f), Climate.Parameter.span(-1.0f, -0.2225f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_FOREST_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(0.2f, 0.55f), Climate.Parameter.span(-0.1f, 0.1f), Climate.Parameter.span(-0.19f, 0.03f), Climate.Parameter.span(-1.0f, -0.2225f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.CALCITE_CRAGLANDS_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(0.2f, 0.55f), Climate.Parameter.span(0.7f, 1.0f), Climate.Parameter.span(-0.19f, 1.0f), Climate.Parameter.span(-1.0f, -0.2225f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_THICKET_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(0.55f, 1.0f), Climate.Parameter.span(-1.0f, -0.1f), Climate.Parameter.span(-0.19f, 0.03f), Climate.Parameter.span(-1.0f, -0.2225f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_FOREST_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(0.55f, 1.0f), Climate.Parameter.span(0.7f, 1.0f), Climate.Parameter.span(-0.19f, 0.03f), Climate.Parameter.span(-1.0f, -0.2225f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_THICKET_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(-1.0f, -0.45f), Climate.Parameter.span(-0.1f, 0.3f), Climate.Parameter.span(-0.19f, 0.03f), Climate.Parameter.span(-0.2225f, 1.0f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.CONTINENTAL_PLATEAU_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(-0.45f, -0.15f), Climate.Parameter.span(-0.1f, 1.0f), Climate.Parameter.span(-0.19f, 0.03f), Climate.Parameter.span(-0.2225f, 1.0f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_SHIELD_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(-0.15f, 0.55f), Climate.Parameter.span(-0.1f, 0.1f), Climate.Parameter.span(-0.19f, 0.03f), Climate.Parameter.span(-0.2225f, 1.0f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_PLAINS_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(-0.15f, 0.2f), Climate.Parameter.span(0.1f, 1.0f), Climate.Parameter.span(-0.19f, 0.03f), Climate.Parameter.span(-0.2225f, 1.0f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_SHIELD_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(0.2f, 0.55f), Climate.Parameter.span(0.7f, 1.0f), Climate.Parameter.span(-0.19f, 0.03f), Climate.Parameter.span(-0.2225f, 1.0f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_FOREST_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(0.55f, 1.0f), Climate.Parameter.span(-1.0f, -0.1f), Climate.Parameter.span(-0.19f, 1.0f), Climate.Parameter.span(-0.2225f, 1.0f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_GRAND_GLADE_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(0.55f, 1.0f), Climate.Parameter.span(0.7f, 1.0f), Climate.Parameter.span(-0.19f, 1.0f), Climate.Parameter.span(-0.2225f, 1.0f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_GRAND_GLADE_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(-1.0f, -0.45f), Climate.Parameter.span(-0.1f, 1.0f), Climate.Parameter.span(0.03f, 1.0f), Climate.Parameter.span(-1.0f, 0.45f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.AUTUMNAL_TUNDRA_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(-0.45f, 0.2f), Climate.Parameter.span(-1.0f, -0.35f), Climate.Parameter.span(0.03f, 1.0f), Climate.Parameter.span(-1.0f, 0.45f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.WISTERIA_WOODS_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(-0.45f, 0.2f), Climate.Parameter.span(-0.35f, -0.1f), Climate.Parameter.span(0.03f, 1.0f), Climate.Parameter.span(-1.0f, 1.0f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_PLAINS_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(-0.45f, 0.2f), Climate.Parameter.span(-0.1f, 1.0f), Climate.Parameter.span(0.03f, 1.0f), Climate.Parameter.span(-1.0f, 0.45f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_FOREST_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(0.2f, 0.55f), Climate.Parameter.span(-1.0f, -0.35f), Climate.Parameter.span(0.03f, 1.0f), Climate.Parameter.span(-1.0f, 1.0f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_PLAINS_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(0.2f, 0.55f), Climate.Parameter.span(-0.35f, 0.3f), Climate.Parameter.span(0.03f, 1.0f), Climate.Parameter.span(-1.0f, 0.45f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_FOREST_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(0.2f, 0.55f), Climate.Parameter.span(0.3f, 1.0f), Climate.Parameter.span(0.03f, 1.0f), Climate.Parameter.span(-1.0f, 0.45f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_THICKET_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(0.55f, 1.0f), Climate.Parameter.span(-1.0f, -0.1f), Climate.Parameter.span(0.03f, 1.0f), Climate.Parameter.span(-1.0f, 1.0f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_GRAND_GLADE_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(0.55f, 1.0f), Climate.Parameter.span(-0.1f, 0.1f), Climate.Parameter.span(0.03f, 1.0f), Climate.Parameter.span(-1.0f, 0.45f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_GRAND_GLADE_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(0.55f, 1.0f), Climate.Parameter.span(0.1f, 0.3f), Climate.Parameter.span(0.03f, 1.0f), Climate.Parameter.span(-1.0f, 1.0f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_THICKET_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(0.55f, 1.0f), Climate.Parameter.span(0.3f, 0.7f), Climate.Parameter.span(0.03f, 1.0f), Climate.Parameter.span(-1.0f, 0.45f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_THICKET_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(0.55f, 1.0f), Climate.Parameter.span(0.7f, 1.0f), Climate.Parameter.span(0.03f, 1.0f), Climate.Parameter.span(-1.0f, 1.0f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_GRAND_GLADE_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(-1.0f, -0.45f), Climate.Parameter.span(-0.1f, 0.3f), Climate.Parameter.span(0.03f, 0.8f), Climate.Parameter.span(0.45f, 1.0f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.CONTINENTAL_PLATEAU_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(-0.45f, 0.55f), Climate.Parameter.span(-1.0f, -0.1f), Climate.Parameter.span(0.03f, 0.8f), Climate.Parameter.span(0.45f, 1.0f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_PLAINS_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(-0.45f, -0.15f), Climate.Parameter.span(-0.1f, 1.0f), Climate.Parameter.span(0.03f, 0.8f), Climate.Parameter.span(0.45f, 1.0f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_SHIELD_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(-0.15f, 0.55f), Climate.Parameter.span(-0.1f, 0.1f), Climate.Parameter.span(0.03f, 0.8f), Climate.Parameter.span(0.45f, 1.0f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_PLAINS_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(-0.15f, 0.2f), Climate.Parameter.span(0.1f, 1.0f), Climate.Parameter.span(0.03f, 0.8f), Climate.Parameter.span(0.45f, 1.0f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_SHIELD_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(0.2f, 0.55f), Climate.Parameter.span(0.3f, 1.0f), Climate.Parameter.span(0.03f, 0.8f), Climate.Parameter.span(0.45f, 1.0f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_FOREST_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(0.55f, 1.0f), Climate.Parameter.span(-0.1f, 0.3f), Climate.Parameter.span(0.03f, 0.8f), Climate.Parameter.span(0.45f, 1.0f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_THICKET_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(0.55f, 1.0f), Climate.Parameter.span(0.3f, 1.0f), Climate.Parameter.span(0.03f, 0.8f), Climate.Parameter.span(0.45f, 1.0f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_GRAND_GLADE_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(-1.0f, -0.45f), Climate.Parameter.span(-0.1f, 1.0f), Climate.Parameter.span(0.8f, 1.0f), Climate.Parameter.span(0.45f, 0.55f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.AUTUMNAL_TUNDRA_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(-0.45f, 0.2f), Climate.Parameter.span(-1.0f, -0.35f), Climate.Parameter.span(0.8f, 1.0f), Climate.Parameter.span(0.45f, 0.55f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.WISTERIA_WOODS_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(-0.45f, 0.2f), Climate.Parameter.span(-0.1f, 1.0f), Climate.Parameter.span(0.8f, 1.0f), Climate.Parameter.span(0.45f, 0.55f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_FOREST_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(0.2f, 0.55f), Climate.Parameter.span(-0.35f, 0.3f), Climate.Parameter.span(0.8f, 1.0f), Climate.Parameter.span(0.45f, 0.55f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_FOREST_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(0.2f, 0.55f), Climate.Parameter.span(0.3f, 1.0f), Climate.Parameter.span(0.8f, 1.0f), Climate.Parameter.span(0.45f, 0.55f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_THICKET_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(0.55f, 1.0f), Climate.Parameter.span(-0.1f, 0.1f), Climate.Parameter.span(0.8f, 1.0f), Climate.Parameter.span(0.45f, 0.55f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_GRAND_GLADE_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(0.55f, 1.0f), Climate.Parameter.span(0.3f, 0.7f), Climate.Parameter.span(0.8f, 1.0f), Climate.Parameter.span(0.45f, 0.55f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_THICKET_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(-1.0f, -0.45f), Climate.Parameter.span(-0.1f, 0.3f), Climate.Parameter.span(0.8f, 1.0f), Climate.Parameter.span(0.55f, 1.0f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.CONTINENTAL_PLATEAU_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(-0.45f, 0.55f), Climate.Parameter.span(-1.0f, -0.1f), Climate.Parameter.span(0.8f, 1.0f), Climate.Parameter.span(0.55f, 1.0f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_PLAINS_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(-0.45f, -0.15f), Climate.Parameter.span(-0.1f, 1.0f), Climate.Parameter.span(0.8f, 1.0f), Climate.Parameter.span(0.55f, 1.0f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_SHIELD_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(-0.15f, 0.55f), Climate.Parameter.span(-0.1f, 0.1f), Climate.Parameter.span(0.8f, 1.0f), Climate.Parameter.span(0.55f, 1.0f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_PLAINS_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(-0.15f, 0.2f), Climate.Parameter.span(0.1f, 1.0f), Climate.Parameter.span(0.8f, 1.0f), Climate.Parameter.span(0.55f, 1.0f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_SHIELD_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(0.2f, 0.55f), Climate.Parameter.span(0.3f, 1.0f), Climate.Parameter.span(0.8f, 1.0f), Climate.Parameter.span(0.55f, 1.0f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_FOREST_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(0.55f, 1.0f), Climate.Parameter.span(-0.1f, 0.3f), Climate.Parameter.span(0.8f, 1.0f), Climate.Parameter.span(0.55f, 1.0f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_THICKET_KEY)),
                Pair.of(Climate.parameters(Climate.Parameter.span(0.55f, 1.0f), Climate.Parameter.span(0.3f, 1.0f), Climate.Parameter.span(0.8f, 1.0f), Climate.Parameter.span(0.55f, 1.0f), Climate.Parameter.span(0.0f, 0.0f), Climate.Parameter.span(-1.0f, 1.0f), 0.0f), biomes.getOrThrow(ParadiseLostBiomes.HIGHLANDS_GRAND_GLADE_KEY))
        ));

        MultiNoiseBiomeSource biomeSource = MultiNoiseBiomeSource.createFromList(parameterList);
        NoiseBasedChunkGenerator generator = new NoiseBasedChunkGenerator(
                biomeSource,
                noiseSettings.getOrThrow(ParadiseLostNoiseSettings.NOISE)
        );
        context.register(OPTIONS_KEY, new LevelStem(dimensionTypes.getOrThrow(DIMENSION_TYPE), generator));
    }
}
