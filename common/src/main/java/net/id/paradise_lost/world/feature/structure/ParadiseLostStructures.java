package net.id.paradise_lost.world.feature.structure;

import net.id.paradise_lost.registry.EntityRegistry;
import net.id.paradise_lost.tag.ParadiseLostStructureTags;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pools.DimensionPadding;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import net.minecraft.world.level.levelgen.VerticalAnchor;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static net.id.paradise_lost.ModConstants.id;

public final class ParadiseLostStructures {
    private ParadiseLostStructures() {}

    public static final ResourceKey<Structure> AUREL_TOWER = of("aurel_tower");
    public static final ResourceKey<Structure> BIRDCAGE = of("birdcage");
    public static final ResourceKey<Structure> PALACE = of("palace");
    public static final ResourceKey<Structure> REMAINS = of("remains");
    public static final ResourceKey<Structure> VAULT = of("vault");

    public static ResourceKey<Structure> of(String name) {
        return ResourceKey.create(Registries.STRUCTURE, id(name));
    }

    public static void bootstrap(BootstrapContext<Structure> context) {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<StructureTemplatePool> pools = context.lookup(Registries.TEMPLATE_POOL);

        context.register(AUREL_TOWER, new AurelTowerFeature(new Structure.StructureSettings(biomes.getOrThrow(ParadiseLostStructureTags.AUREL_TOWER_HAS_STRUCTURE), Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.BEARD_THIN)));
        context.register(BIRDCAGE, new JigsawStructure(
                new Structure.StructureSettings(biomes.getOrThrow(ParadiseLostStructureTags.BIRDCAGE_HAS_STRUCTURE), Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE),
                pools.getOrThrow(ResourceKey.create(Registries.TEMPLATE_POOL, id("birdcage/roofs"))),
                Optional.empty(),
                10,
                ConstantHeight.of(VerticalAnchor.absolute(100)),
                true,
                Optional.empty(),
                128,
                List.of(),
                new DimensionPadding(50),
                JigsawStructure.DEFAULT_LIQUID_SETTINGS));
        context.register(PALACE, new JigsawStructure(
                new Structure.StructureSettings(biomes.getOrThrow(ParadiseLostStructureTags.PALACE_HAS_STRUCTURE), Map.ofEntries(Map.entry(MobCategory.MONSTER, new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.PIECE, WeightedRandomList.create(new MobSpawnSettings.SpawnerData(EntityRegistry.ENVOY.get(), 10, 2, 4))))), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE),
                pools.getOrThrow(ResourceKey.create(Registries.TEMPLATE_POOL, id("palace/starts"))),
                Optional.empty(),
                6,
                ConstantHeight.of(VerticalAnchor.absolute(155)),
                true,
                Optional.empty(),
                128,
                List.of(),
                new DimensionPadding(50),
                JigsawStructure.DEFAULT_LIQUID_SETTINGS));
        context.register(REMAINS, new JigsawStructure(
                new Structure.StructureSettings(biomes.getOrThrow(ParadiseLostStructureTags.REMAINS_HAS_STRUCTURE), Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.BEARD_THIN),
                pools.getOrThrow(ResourceKey.create(Registries.TEMPLATE_POOL, id("remains/starting_plates"))),
                Optional.empty(),
                4,
                ConstantHeight.of(VerticalAnchor.absolute(0)),
                true,
                Optional.of(Heightmap.Types.WORLD_SURFACE_WG),
                80,
                List.of(),
                new DimensionPadding(50),
                JigsawStructure.DEFAULT_LIQUID_SETTINGS));
        context.register(VAULT, new JigsawStructure(
                new Structure.StructureSettings(biomes.getOrThrow(ParadiseLostStructureTags.VAULT_HAS_STRUCTURE), Map.of(), GenerationStep.Decoration.UNDERGROUND_STRUCTURES, TerrainAdjustment.ENCAPSULATE),
                pools.getOrThrow(ResourceKey.create(Registries.TEMPLATE_POOL, id("vault/starts"))),
                Optional.empty(),
                8,
                ConstantHeight.of(VerticalAnchor.absolute(105)),
                false,
                Optional.empty(),
                116,
                List.of(),
                new DimensionPadding(50),
                JigsawStructure.DEFAULT_LIQUID_SETTINGS));
    }
}
