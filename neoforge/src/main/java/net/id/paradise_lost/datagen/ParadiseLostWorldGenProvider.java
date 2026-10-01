package net.id.paradise_lost.datagen;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.util.ParadiseLostDamageTypes;
import net.id.paradise_lost.world.dimension.ParadiseLostBiomes;
import net.id.paradise_lost.world.dimension.ParadiseLostDimension;
import net.id.paradise_lost.world.dimension.ParadiseLostLevelStem;
import net.id.paradise_lost.world.feature.configured_features.ParadiseLostConfiguredFeatures;
import net.id.paradise_lost.world.feature.placed_features.ParadiseLostPlacedFeatures;
import net.id.paradise_lost.world.feature.structure.ParadiseLostProcessorLists;
import net.id.paradise_lost.world.feature.structure.ParadiseLostStructureSets;
import net.id.paradise_lost.world.feature.structure.ParadiseLostStructures;
import net.id.paradise_lost.world.feature.structure.ParadiseLostTemplatePools;
import net.id.paradise_lost.world.gen.carver.ParadiseLostConfiguredCarvers;
import net.id.paradise_lost.world.gen.density.ParadiseLostDensityFunctions;
import net.id.paradise_lost.world.gen.noise.ParadiseLostNoise;
import net.id.paradise_lost.world.gen.noise.ParadiseLostNoiseSettings;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.dimension.DimensionType;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.OptionalLong;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class ParadiseLostWorldGenProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.DIMENSION_TYPE, ParadiseLostWorldGenProvider::bootstrapDimensionTypes)
            .add(Registries.CONFIGURED_FEATURE, ParadiseLostConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, ParadiseLostPlacedFeatures::bootstrap)
            .add(Registries.CONFIGURED_CARVER, ParadiseLostConfiguredCarvers::bootstrap)
            .add(Registries.NOISE, ParadiseLostNoise::bootstrap)
            .add(Registries.DENSITY_FUNCTION, ParadiseLostDensityFunctions::bootstrap)
            .add(Registries.NOISE_SETTINGS, ParadiseLostNoiseSettings::bootstrap)
            .add(Registries.PROCESSOR_LIST, ParadiseLostProcessorLists::bootstrap)
            .add(Registries.TEMPLATE_POOL, ParadiseLostTemplatePools::bootstrap)
            .add(Registries.STRUCTURE, ParadiseLostStructures::bootstrap)
            .add(Registries.STRUCTURE_SET, ParadiseLostStructureSets::bootstrap)
            .add(Registries.BIOME, ParadiseLostBiomes::bootstrap)
            .add(Registries.LEVEL_STEM, ParadiseLostLevelStem::bootstrap);

    public ParadiseLostWorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, BUILDER, Set.of(ModConstants.MODID));
    }

    public static void bootstrapDimensionTypes(BootstrapContext<DimensionType> context) {
        context.register(
                ParadiseLostDimension.DIMENSION_TYPE,
                new DimensionType(
                        OptionalLong.empty(),
                        true,
                        false,
                        false,
                        true,
                        1.0,
                        true,
                        false,
                        -64,
                        512,
                        384,
                        BlockTags.INFINIBURN_OVERWORLD,
                        ResourceLocation.withDefaultNamespace("overworld"),
                        0.03F,
                        new DimensionType.MonsterSettings(false, false, UniformInt.of(0, 3), 0)
                )
        );
    }

    @Override
    public String getName() {
        return "Paradise Lost WorldGen";
    }
}
