package net.id.paradise_lost.world.feature.structure;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

import java.util.List;

import static net.id.paradise_lost.ModConstants.id;

public final class ParadiseLostTemplatePools {
    private ParadiseLostTemplatePools() {}

    public static final ResourceKey<StructureTemplatePool> BIRDCAGE_CAPS = of("birdcage/caps");
    public static final ResourceKey<StructureTemplatePool> BIRDCAGE_CENTERS = of("birdcage/centers");
    public static final ResourceKey<StructureTemplatePool> BIRDCAGE_CONNECTORS = of("birdcage/connectors");
    public static final ResourceKey<StructureTemplatePool> BIRDCAGE_ENDS = of("birdcage/ends");
    public static final ResourceKey<StructureTemplatePool> BIRDCAGE_LEVELS_NORMAL = of("birdcage/levels_normal");
    public static final ResourceKey<StructureTemplatePool> BIRDCAGE_LEVELS_STAIRS = of("birdcage/levels_stairs");
    public static final ResourceKey<StructureTemplatePool> BIRDCAGE_ROOFS = of("birdcage/roofs");
    public static final ResourceKey<StructureTemplatePool> BIRDCAGE_ROOMS = of("birdcage/rooms");
    public static final ResourceKey<StructureTemplatePool> BIRDCAGE_STAIRWELLS = of("birdcage/stairwells");
    public static final ResourceKey<StructureTemplatePool> PALACE_CENTERS_MIDS = of("palace/centers/mids");
    public static final ResourceKey<StructureTemplatePool> PALACE_CENTERS_TOPS = of("palace/centers/tops");
    public static final ResourceKey<StructureTemplatePool> PALACE_CORNERS_CORNERS_1 = of("palace/corners/corners_1");
    public static final ResourceKey<StructureTemplatePool> PALACE_CORNERS_CORNERS_2 = of("palace/corners/corners_2");
    public static final ResourceKey<StructureTemplatePool> PALACE_CORNERS_CORNERS_3 = of("palace/corners/corners_3");
    public static final ResourceKey<StructureTemplatePool> PALACE_DECORATIONS_PAVILIONS = of("palace/decorations/pavilions");
    public static final ResourceKey<StructureTemplatePool> PALACE_EXTRAS_CORNER_1_DECORATIONS = of("palace/extras/corner_1_decorations");
    public static final ResourceKey<StructureTemplatePool> PALACE_EXTRAS_CORNER_SPIRE_1 = of("palace/extras/corner_spire_1");
    public static final ResourceKey<StructureTemplatePool> PALACE_EXTRAS_CORNER_SPIRE_2 = of("palace/extras/corner_spire_2");
    public static final ResourceKey<StructureTemplatePool> PALACE_EXTRAS_PILLAR = of("palace/extras/pillar");
    public static final ResourceKey<StructureTemplatePool> PALACE_EXTRAS_QUINT = of("palace/extras/quint");
    public static final ResourceKey<StructureTemplatePool> PALACE_EXTRAS_SIDE_3_DECORATIONS = of("palace/extras/side_3_decorations");
    public static final ResourceKey<StructureTemplatePool> PALACE_EXTRAS_SPIRE = of("palace/extras/spire");
    public static final ResourceKey<StructureTemplatePool> PALACE_EXTRAS_SPIRE_PEAK = of("palace/extras/spire_peak");
    public static final ResourceKey<StructureTemplatePool> PALACE_EXTRAS_TREES = of("palace/extras/trees");
    public static final ResourceKey<StructureTemplatePool> PALACE_EXTRAS_TROPHIES = of("palace/extras/trophies");
    public static final ResourceKey<StructureTemplatePool> PALACE_FOUNTAIN = of("palace/fountain");
    public static final ResourceKey<StructureTemplatePool> PALACE_SIDES_BACKS = of("palace/sides/backs");
    public static final ResourceKey<StructureTemplatePool> PALACE_SIDES_BLANKS = of("palace/sides/blanks");
    public static final ResourceKey<StructureTemplatePool> PALACE_SIDES_BOTTOMS = of("palace/sides/bottoms");
    public static final ResourceKey<StructureTemplatePool> PALACE_SIDES_ENTRANCES = of("palace/sides/entrances");
    public static final ResourceKey<StructureTemplatePool> PALACE_STARTS = of("palace/starts");
    public static final ResourceKey<StructureTemplatePool> REMAINS_PLATES = of("remains/plates");
    public static final ResourceKey<StructureTemplatePool> REMAINS_STARTING_PLATES = of("remains/starting_plates");
    public static final ResourceKey<StructureTemplatePool> REMAINS_STARTS = of("remains/starts");
    public static final ResourceKey<StructureTemplatePool> REMAINS_STRUCTURES = of("remains/structures");
    public static final ResourceKey<StructureTemplatePool> VAULT_ARMORED_ENVOY = of("vault/armored_envoy");
    public static final ResourceKey<StructureTemplatePool> VAULT_CATACOMB = of("vault/catacomb");
    public static final ResourceKey<StructureTemplatePool> VAULT_CATACOMB_CROSSWAY = of("vault/catacomb_crossway");
    public static final ResourceKey<StructureTemplatePool> VAULT_CATACOMB_FALLBACK = of("vault/catacomb_fallback");
    public static final ResourceKey<StructureTemplatePool> VAULT_ENLIGHTENED_ENVOY = of("vault/enlightened_envoy");
    public static final ResourceKey<StructureTemplatePool> VAULT_FIRST_FLOOR_PLANT_ROOM_CONNECTOR = of("vault/first_floor/plant_room_connector");
    public static final ResourceKey<StructureTemplatePool> VAULT_FIRST_FLOOR_PLANT_ROOM_CONNECTOR_FALLBACK = of("vault/first_floor/plant_room_connector_fallback");
    public static final ResourceKey<StructureTemplatePool> VAULT_FIRST_FLOOR_SPAN = of("vault/first_floor/span");
    public static final ResourceKey<StructureTemplatePool> VAULT_FIRST_FLOOR_SPAN_FALLBACK = of("vault/first_floor/span_fallback");
    public static final ResourceKey<StructureTemplatePool> VAULT_SECOND_FLOOR_SHORTS = of("vault/second_floor/shorts");
    public static final ResourceKey<StructureTemplatePool> VAULT_SECOND_FLOOR_SHORTS_FALLBACK = of("vault/second_floor/shorts_fallback");
    public static final ResourceKey<StructureTemplatePool> VAULT_SECOND_FLOOR_TALL_HALLS = of("vault/second_floor/tall_halls");
    public static final ResourceKey<StructureTemplatePool> VAULT_SECOND_FLOOR_TALL_HALLS_FALLBACK = of("vault/second_floor/tall_halls_fallback");
    public static final ResourceKey<StructureTemplatePool> VAULT_SECOND_FLOOR_TALL_HALLS_NON_END = of("vault/second_floor/tall_halls_non_end");
    public static final ResourceKey<StructureTemplatePool> VAULT_STARTS = of("vault/starts");

    public static ResourceKey<StructureTemplatePool> of(String name) {
        return ResourceKey.create(Registries.TEMPLATE_POOL, id(name));
    }

    public static void bootstrap(BootstrapContext<StructureTemplatePool> context) {
        HolderGetter<StructureTemplatePool> pools = context.lookup(Registries.TEMPLATE_POOL);
        HolderGetter<StructureProcessorList> processors = context.lookup(Registries.PROCESSOR_LIST);
        Holder<StructureTemplatePool> empty = pools.getOrThrow(Pools.EMPTY);
        Holder<StructureProcessorList> emptyProcessors = processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, ResourceLocation.withDefaultNamespace("empty")));
        context.register(BIRDCAGE_CAPS, new StructureTemplatePool(
                empty,
                List.of(
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/caps/cap_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 1)
                ),
                StructureTemplatePool.Projection.RIGID));
        context.register(BIRDCAGE_CENTERS, new StructureTemplatePool(
                empty,
                List.of(
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/centers/center_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 5),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/centers/center_2", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 5),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/centers/center_3", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 4),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/centers/center_4", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/centers/center_5", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/centers/center_6", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 2)
                ),
                StructureTemplatePool.Projection.RIGID));
        context.register(BIRDCAGE_CONNECTORS, new StructureTemplatePool(
                pools.getOrThrow(ResourceKey.create(Registries.TEMPLATE_POOL, id("birdcage/ends"))),
                List.of(
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/connectors/straight_short_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 5),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/connectors/straight_medium_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 5),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/connectors/straight_long_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 7),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/connectors/straight_short_2", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 5),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/connectors/straight_medium_2", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 5),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/connectors/straight_long_2", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 7),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/connectors/straight_short_3", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 2),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/connectors/straight_medium_3", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/connectors/straight_long_3", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 4),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/connectors/straight_short_4", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 2),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/connectors/straight_medium_4", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 2),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/connectors/straight_long_4", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 2),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/connectors/straight_long_5", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 2),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/ends/end_tunnel_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 2),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/ends/end_tunnel_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 1)
                ),
                StructureTemplatePool.Projection.RIGID));
        context.register(BIRDCAGE_ENDS, new StructureTemplatePool(
                pools.getOrThrow(ResourceKey.create(Registries.TEMPLATE_POOL, id("birdcage/caps"))),
                List.of(
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/ends/end_closet_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 2),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/ends/end_closet_2", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 2),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/ends/end_closet_3", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/ends/end_closet_4", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 4),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/ends/end_room_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/ends/end_room_2", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/ends/end_room_3", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/ends/end_tunnel_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/ends/end_tunnel_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 1)
                ),
                StructureTemplatePool.Projection.RIGID));
        context.register(BIRDCAGE_LEVELS_NORMAL, new StructureTemplatePool(
                empty,
                List.of(
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/levels/normal/normal_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 8),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/levels/normal/normal_2", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 11),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/levels/normal/normal_3", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 6),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/levels/normal/normal_4", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 8),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/levels/normal/normal_5", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 10),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/levels/normal/normal_6", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 6),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/levels/normal/normal_1_spawner", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 6),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/levels/normal/normal_2_spawner", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 6)
                ),
                StructureTemplatePool.Projection.RIGID));
        context.register(BIRDCAGE_LEVELS_STAIRS, new StructureTemplatePool(
                empty,
                List.of(
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/levels/stairs/stairs_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 2),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/levels/stairs/stairs_2", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 5),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/levels/stairs/stairs_3", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 5),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/levels/stairs/stairs_4", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 4),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/levels/stairs/stairs_2_spawner", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 2)
                ),
                StructureTemplatePool.Projection.RIGID));
        context.register(BIRDCAGE_ROOFS, new StructureTemplatePool(
                empty,
                List.of(
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/roofs/roof_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/roofs/roof_2", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/roofs/roof_3", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/roofs/roof_4", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 1)
                ),
                StructureTemplatePool.Projection.RIGID));
        context.register(BIRDCAGE_ROOMS, new StructureTemplatePool(
                pools.getOrThrow(ResourceKey.create(Registries.TEMPLATE_POOL, id("birdcage/ends"))),
                List.of(
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/rooms/big_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 2),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/rooms/medium_end_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/rooms/medium_pass_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 2),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/rooms/medium_turn_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 2),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/rooms/big_2", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 2),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/rooms/medium_end_2", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/rooms/medium_pass_2", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 2),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/rooms/medium_turn_2", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 2),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/rooms/big_3", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/rooms/medium_end_3", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/rooms/medium_pass_3", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 4),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/rooms/medium_turn_3", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 4),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/rooms/big_4", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/rooms/medium_end_4", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/rooms/medium_pass_4", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 4),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/rooms/medium_turn_4", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 4),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/rooms/small_turn_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 2),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/rooms/small_turn_2", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 1)
                ),
                StructureTemplatePool.Projection.RIGID));
        context.register(BIRDCAGE_STAIRWELLS, new StructureTemplatePool(
                empty,
                List.of(
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/stairwells/stairwell_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/stairwells/stairwell_2", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/stairwells/stairwell_3", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/stairwells/stairwell_4", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/stairwells/stairwell_1_broken", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/stairwells/stairwell_2_broken", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/stairwells/stairwell_3_broken", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/stairwells/stairwell_4_broken", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/stairwells/stairwell_external_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/stairwells/stairwell_external_2", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/stairwells/stairwell_external_3", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/stairwells/stairwell_external_4", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/stairwells/stairwell_external_1_broken", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/stairwells/stairwell_external_2_broken", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/stairwells/stairwell_external_3_broken", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:birdcage/stairwells/stairwell_external_4_broken", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("birdcage_proc")))), 1)
                ),
                StructureTemplatePool.Projection.RIGID));
        context.register(PALACE_CENTERS_MIDS, new StructureTemplatePool(
                empty,
                List.of(
                Pair.of(StructurePoolElement.single("paradise_lost:palace/centers/center_mid_1", emptyProcessors), 1)
                ),
                StructureTemplatePool.Projection.RIGID));
        context.register(PALACE_CENTERS_TOPS, new StructureTemplatePool(
                empty,
                List.of(
                Pair.of(StructurePoolElement.single("paradise_lost:palace/centers/center_top_1", emptyProcessors), 1)
                ),
                StructureTemplatePool.Projection.RIGID));
        context.register(PALACE_CORNERS_CORNERS_1, new StructureTemplatePool(
                empty,
                List.of(
                Pair.of(StructurePoolElement.single("paradise_lost:palace/corners/corner_1", emptyProcessors), 1)
                ),
                StructureTemplatePool.Projection.RIGID));
        context.register(PALACE_CORNERS_CORNERS_2, new StructureTemplatePool(
                empty,
                List.of(
                Pair.of(StructurePoolElement.single("paradise_lost:palace/corners/corner_2", emptyProcessors), 1)
                ),
                StructureTemplatePool.Projection.RIGID));
        context.register(PALACE_CORNERS_CORNERS_3, new StructureTemplatePool(
                empty,
                List.of(
                Pair.of(StructurePoolElement.single("paradise_lost:palace/corners/corner_3_1", emptyProcessors), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:palace/corners/corner_3_2", emptyProcessors), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:palace/corners/corner_3_3", emptyProcessors), 1)
                ),
                StructureTemplatePool.Projection.RIGID));
        context.register(PALACE_DECORATIONS_PAVILIONS, new StructureTemplatePool(
                empty,
                List.of(
                Pair.of(StructurePoolElement.single("paradise_lost:palace/sides/side_1", emptyProcessors), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:palace/sides/side_2", emptyProcessors), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:palace/sides/side_3", emptyProcessors), 1)
                ),
                StructureTemplatePool.Projection.RIGID));
        context.register(PALACE_EXTRAS_CORNER_1_DECORATIONS, new StructureTemplatePool(
                empty,
                List.of(
                Pair.of(StructurePoolElement.single("paradise_lost:palace/extras/side_3_deco_2", emptyProcessors), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:palace/extras/corner_1_deco_1", emptyProcessors), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:palace/extras/corner_1_deco_2", emptyProcessors), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:palace/extras/corner_1_deco_3", emptyProcessors), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:palace/extras/corner_1_deco_empty", emptyProcessors), 1)
                ),
                StructureTemplatePool.Projection.RIGID));
        context.register(PALACE_EXTRAS_CORNER_SPIRE_1, new StructureTemplatePool(
                empty,
                List.of(
                Pair.of(StructurePoolElement.single("paradise_lost:palace/extras/corner_spire_1", emptyProcessors), 1)
                ),
                StructureTemplatePool.Projection.RIGID));
        context.register(PALACE_EXTRAS_CORNER_SPIRE_2, new StructureTemplatePool(
                empty,
                List.of(
                Pair.of(StructurePoolElement.single("paradise_lost:palace/extras/corner_spire_2", emptyProcessors), 1)
                ),
                StructureTemplatePool.Projection.RIGID));
        context.register(PALACE_EXTRAS_PILLAR, new StructureTemplatePool(
                empty,
                List.of(
                Pair.of(StructurePoolElement.single("paradise_lost:palace/extras/pillar", emptyProcessors), 1)
                ),
                StructureTemplatePool.Projection.RIGID));
        context.register(PALACE_EXTRAS_QUINT, new StructureTemplatePool(
                empty,
                List.of(
                Pair.of(StructurePoolElement.single("paradise_lost:palace/extras/quint", emptyProcessors), 3)
                ),
                StructureTemplatePool.Projection.RIGID));
        context.register(PALACE_EXTRAS_SIDE_3_DECORATIONS, new StructureTemplatePool(
                empty,
                List.of(
                Pair.of(StructurePoolElement.single("paradise_lost:palace/extras/side_3_deco_1", emptyProcessors), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:palace/extras/side_3_deco_2", emptyProcessors), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:palace/extras/side_3_deco_3", emptyProcessors), 2)
                ),
                StructureTemplatePool.Projection.RIGID));
        context.register(PALACE_EXTRAS_SPIRE, new StructureTemplatePool(
                empty,
                List.of(
                Pair.of(StructurePoolElement.single("paradise_lost:palace/extras/spire", emptyProcessors), 1)
                ),
                StructureTemplatePool.Projection.RIGID));
        context.register(PALACE_EXTRAS_SPIRE_PEAK, new StructureTemplatePool(
                empty,
                List.of(
                Pair.of(StructurePoolElement.single("paradise_lost:palace/extras/spire_peak", emptyProcessors), 1)
                ),
                StructureTemplatePool.Projection.RIGID));
        context.register(PALACE_EXTRAS_TREES, new StructureTemplatePool(
                empty,
                List.of(
                Pair.of(StructurePoolElement.single("paradise_lost:palace/extras/small_tree", emptyProcessors), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:palace/extras/large_tree", emptyProcessors), 1)
                ),
                StructureTemplatePool.Projection.RIGID));
        context.register(PALACE_EXTRAS_TROPHIES, new StructureTemplatePool(
                empty,
                List.of(
                Pair.of(StructurePoolElement.single("paradise_lost:palace/extras/trophy_gold", emptyProcessors), 2),
                Pair.of(StructurePoolElement.single("paradise_lost:palace/extras/trophy_calcite", emptyProcessors), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:palace/extras/trophy_popom", emptyProcessors), 1)
                ),
                StructureTemplatePool.Projection.RIGID));
        context.register(PALACE_FOUNTAIN, new StructureTemplatePool(
                empty,
                List.of(
                Pair.of(StructurePoolElement.single("paradise_lost:palace/fountain", emptyProcessors), 1)
                ),
                StructureTemplatePool.Projection.RIGID));
        context.register(PALACE_SIDES_BACKS, new StructureTemplatePool(
                empty,
                List.of(
                Pair.of(StructurePoolElement.single("paradise_lost:palace/sides/back_1_1", emptyProcessors), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:palace/sides/back_1_2", emptyProcessors), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:palace/sides/back_1_3", emptyProcessors), 1)
                ),
                StructureTemplatePool.Projection.RIGID));
        context.register(PALACE_SIDES_BLANKS, new StructureTemplatePool(
                empty,
                List.of(
                Pair.of(StructurePoolElement.single("paradise_lost:palace/sides/side_1", emptyProcessors), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:palace/sides/side_2", emptyProcessors), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:palace/sides/side_3", emptyProcessors), 1)
                ),
                StructureTemplatePool.Projection.RIGID));
        context.register(PALACE_SIDES_BOTTOMS, new StructureTemplatePool(
                empty,
                List.of(
                Pair.of(StructurePoolElement.single("paradise_lost:palace/corners/corner_1_bottom", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("palace_dissolve_proc")))), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:palace/corners/corner_2_bottom", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("palace_dissolve_proc")))), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:palace/corners/corner_3_bottom", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("palace_dissolve_proc")))), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:palace/sides/back_1_bottom", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("palace_dissolve_proc")))), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:palace/sides/entrance_1_bottom", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("palace_dissolve_proc")))), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:palace/sides/side_1_bottom", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("palace_dissolve_proc")))), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:palace/sides/side_2_bottom", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("palace_dissolve_proc")))), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:palace/sides/side_3_bottom", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("palace_dissolve_proc")))), 1)
                ),
                StructureTemplatePool.Projection.RIGID));
        context.register(PALACE_SIDES_ENTRANCES, new StructureTemplatePool(
                empty,
                List.of(
                Pair.of(StructurePoolElement.single("paradise_lost:palace/sides/entrance_1_1", emptyProcessors), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:palace/sides/entrance_1_2", emptyProcessors), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:palace/sides/entrance_1_3", emptyProcessors), 1)
                ),
                StructureTemplatePool.Projection.RIGID));
        context.register(PALACE_STARTS, new StructureTemplatePool(
                empty,
                List.of(
                Pair.of(StructurePoolElement.single("paradise_lost:palace/centers/center_bottom_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("palace_dissolve_proc")))), 1)
                ),
                StructureTemplatePool.Projection.RIGID));
        context.register(REMAINS_PLATES, new StructureTemplatePool(
                empty,
                List.of(
                Pair.of(StructurePoolElement.single("paradise_lost:remains/plates/big_1", emptyProcessors), 21),
                Pair.of(StructurePoolElement.single("paradise_lost:remains/plates/medium_1", emptyProcessors), 14),
                Pair.of(StructurePoolElement.single("paradise_lost:remains/plates/medium_2", emptyProcessors), 14),
                Pair.of(StructurePoolElement.single("paradise_lost:remains/plates/big_1_sus", emptyProcessors), 2),
                Pair.of(StructurePoolElement.single("paradise_lost:remains/plates/medium_1_sus", emptyProcessors), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:remains/plates/medium_2_sus", emptyProcessors), 1)
                ),
                StructureTemplatePool.Projection.TERRAIN_MATCHING));
        context.register(REMAINS_STARTING_PLATES, new StructureTemplatePool(
                empty,
                List.of(
                Pair.of(StructurePoolElement.single("paradise_lost:remains/plates/start", emptyProcessors), 2),
                Pair.of(StructurePoolElement.single("paradise_lost:remains/plates/start_sus", emptyProcessors), 1)
                ),
                StructureTemplatePool.Projection.TERRAIN_MATCHING));
        context.register(REMAINS_STARTS, new StructureTemplatePool(
                empty,
                List.of(
                Pair.of(StructurePoolElement.single("paradise_lost:remains/structures/center_1_blank", emptyProcessors), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:remains/structures/center_1_moa", emptyProcessors), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:remains/structures/center_1_sun", emptyProcessors), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:remains/structures/center_2", emptyProcessors), 2),
                Pair.of(StructurePoolElement.single("paradise_lost:remains/structures/center_3_blank", emptyProcessors), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:remains/structures/center_3_moa", emptyProcessors), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:remains/structures/center_3_sun", emptyProcessors), 1)
                ),
                StructureTemplatePool.Projection.RIGID));
        context.register(REMAINS_STRUCTURES, new StructureTemplatePool(
                empty,
                List.of(
                Pair.of(StructurePoolElement.single("paradise_lost:remains/structures/building_1", emptyProcessors), 18),
                Pair.of(StructurePoolElement.single("paradise_lost:remains/structures/building_2", emptyProcessors), 18),
                Pair.of(StructurePoolElement.single("paradise_lost:remains/structures/building_3", emptyProcessors), 18),
                Pair.of(StructurePoolElement.single("paradise_lost:remains/structures/building_4", emptyProcessors), 18),
                Pair.of(StructurePoolElement.single("paradise_lost:remains/structures/building_5", emptyProcessors), 18),
                Pair.of(StructurePoolElement.single("paradise_lost:remains/structures/building_6", emptyProcessors), 18),
                Pair.of(StructurePoolElement.single("paradise_lost:remains/structures/building_7", emptyProcessors), 18),
                Pair.of(StructurePoolElement.single("paradise_lost:remains/structures/building_8", emptyProcessors), 18),
                Pair.of(StructurePoolElement.single("paradise_lost:remains/structures/building_9", emptyProcessors), 18),
                Pair.of(StructurePoolElement.single("paradise_lost:remains/structures/building_10", emptyProcessors), 18),
                Pair.of(StructurePoolElement.single("paradise_lost:remains/structures/building_11", emptyProcessors), 18),
                Pair.of(StructurePoolElement.single("paradise_lost:remains/structures/building_12", emptyProcessors), 18),
                Pair.of(StructurePoolElement.single("paradise_lost:remains/structures/building_13", emptyProcessors), 18),
                Pair.of(StructurePoolElement.single("paradise_lost:remains/structures/building_14", emptyProcessors), 18),
                Pair.of(StructurePoolElement.single("paradise_lost:remains/structures/building_15", emptyProcessors), 18),
                Pair.of(StructurePoolElement.single("paradise_lost:remains/structures/building_rare_1", emptyProcessors), 9),
                Pair.of(StructurePoolElement.single("paradise_lost:remains/structures/building_rare_2", emptyProcessors), 9),
                Pair.of(StructurePoolElement.single("paradise_lost:remains/structures/building_rare_3", emptyProcessors), 9),
                Pair.of(StructurePoolElement.single("paradise_lost:remains/structures/building_rare_4", emptyProcessors), 9),
                Pair.of(StructurePoolElement.single("paradise_lost:remains/structures/ruin_1", emptyProcessors), 20),
                Pair.of(StructurePoolElement.single("paradise_lost:remains/structures/ruin_2", emptyProcessors), 20),
                Pair.of(StructurePoolElement.single("paradise_lost:remains/structures/ruin_3", emptyProcessors), 20),
                Pair.of(StructurePoolElement.single("paradise_lost:remains/structures/ruin_4", emptyProcessors), 20),
                Pair.of(StructurePoolElement.single("paradise_lost:remains/structures/ruin_5", emptyProcessors), 20),
                Pair.of(StructurePoolElement.single("paradise_lost:remains/structures/ruin_6", emptyProcessors), 20),
                Pair.of(StructurePoolElement.single("paradise_lost:remains/structures/ruin_7", emptyProcessors), 20),
                Pair.of(StructurePoolElement.single("paradise_lost:remains/structures/ruin_rare_1", emptyProcessors), 5),
                Pair.of(StructurePoolElement.single("paradise_lost:remains/structures/ruin_rare_2", emptyProcessors), 5),
                Pair.of(StructurePoolElement.single("paradise_lost:remains/structures/farm_1", emptyProcessors), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:remains/structures/farm_2", emptyProcessors), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:remains/structures/farm_3", emptyProcessors), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:remains/structures/obelisk_1", emptyProcessors), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:remains/structures/obelisk_2", emptyProcessors), 1)
                ),
                StructureTemplatePool.Projection.RIGID));
        context.register(VAULT_ARMORED_ENVOY, new StructureTemplatePool(
                empty,
                List.of(
                Pair.of(StructurePoolElement.single("paradise_lost:vault/armored_envoy", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 1)
                ),
                StructureTemplatePool.Projection.RIGID));
        context.register(VAULT_CATACOMB, new StructureTemplatePool(
                pools.getOrThrow(ResourceKey.create(Registries.TEMPLATE_POOL, id("vault/catacomb_fallback"))),
                List.of(
                Pair.of(StructurePoolElement.single("paradise_lost:vault/catacomb/passage_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/catacomb/passage_2", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/catacomb/passage_chest_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/catacomb/crossway_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 5),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/catacomb/crossway_2", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 5),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/catacomb/crossway_rare_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 2),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/catacomb/crossway_rare_2", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 2),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/catacomb/crossway_rare_3", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 2),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/catacomb/room_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/catacomb/room_2", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/catacomb/room_3", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/catacomb/room_chest_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/catacomb/room_chest_2", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/catacomb/end_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 4),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/catacomb/end_2", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 3)
                ),
                StructureTemplatePool.Projection.RIGID));
        context.register(VAULT_CATACOMB_CROSSWAY, new StructureTemplatePool(
                pools.getOrThrow(ResourceKey.create(Registries.TEMPLATE_POOL, id("vault/catacomb_fallback"))),
                List.of(
                Pair.of(StructurePoolElement.single("paradise_lost:vault/catacomb/crossway_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/catacomb/crossway_2", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/catacomb/crossway_rare_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/catacomb/crossway_rare_2", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/catacomb/crossway_rare_3", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 1)
                ),
                StructureTemplatePool.Projection.RIGID));
        context.register(VAULT_CATACOMB_FALLBACK, new StructureTemplatePool(
                empty,
                List.of(
                Pair.of(StructurePoolElement.single("paradise_lost:vault/catacomb/room_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/catacomb/room_2", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/catacomb/room_3", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/catacomb/room_chest_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 2),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/catacomb/room_chest_2", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 2),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/catacomb/end_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 5),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/catacomb/end_2", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 3)
                ),
                StructureTemplatePool.Projection.RIGID));
        context.register(VAULT_ENLIGHTENED_ENVOY, new StructureTemplatePool(
                empty,
                List.of(
                Pair.of(StructurePoolElement.single("paradise_lost:vault/enlightened_envoy", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 1)
                ),
                StructureTemplatePool.Projection.RIGID));
        context.register(VAULT_FIRST_FLOOR_PLANT_ROOM_CONNECTOR, new StructureTemplatePool(
                pools.getOrThrow(ResourceKey.create(Registries.TEMPLATE_POOL, id("vault/first_floor/plant_room_connector_fallback"))),
                List.of(
                Pair.of(StructurePoolElement.single("paradise_lost:vault/first_floor/plant/room_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 2),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/first_floor/plant/room_2", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/first_floor/plant/room_3", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/first_floor/plant/room_4", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 2),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/first_floor/plant/connector_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 2),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/first_floor/plant/endcap_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 6),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/first_floor/plant/endcap_2", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 5)
                ),
                StructureTemplatePool.Projection.RIGID));
        context.register(VAULT_FIRST_FLOOR_PLANT_ROOM_CONNECTOR_FALLBACK, new StructureTemplatePool(
                empty,
                List.of(
                Pair.of(StructurePoolElement.single("paradise_lost:vault/first_floor/plant/endcap_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/first_floor/plant/endcap_2", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 3)
                ),
                StructureTemplatePool.Projection.RIGID));
        context.register(VAULT_FIRST_FLOOR_SPAN, new StructureTemplatePool(
                pools.getOrThrow(ResourceKey.create(Registries.TEMPLATE_POOL, id("vault/first_floor/span_fallback"))),
                List.of(
                Pair.of(StructurePoolElement.single("paradise_lost:vault/first_floor/plant/room_2", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/first_floor/plant/room_3", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 4),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/first_floor/plant/room_4", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 4),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/first_floor/span/big_room_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/first_floor/catacomb_staircase", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 2),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/first_floor/span/room_chest_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 2),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/first_floor/span/room_chest_2", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 2),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/first_floor/span/room_chest_3", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 2),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/first_floor/span/room_chest_4", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/first_floor/span/room_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/first_floor/span/room_2", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 2),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/first_floor/span/room_3", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 2),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/first_floor/span/room_4", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 2),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/first_floor/span/room_5", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 2),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/first_floor/span/passthru_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 2),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/first_floor/span/passthru_2", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 2),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/first_floor/span/passthru_3", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/first_floor/span/passthru_4", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/first_floor/span/passthru_5", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/first_floor/span/passthru_chest_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 1)
                ),
                StructureTemplatePool.Projection.RIGID));
        context.register(VAULT_FIRST_FLOOR_SPAN_FALLBACK, new StructureTemplatePool(
                empty,
                List.of(
                Pair.of(StructurePoolElement.single("paradise_lost:vault/first_floor/span/room_chest_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 2),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/first_floor/span/room_chest_2", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 2),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/first_floor/span/room_chest_3", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 2)
                ),
                StructureTemplatePool.Projection.RIGID));
        context.register(VAULT_SECOND_FLOOR_SHORTS, new StructureTemplatePool(
                pools.getOrThrow(ResourceKey.create(Registries.TEMPLATE_POOL, id("vault/second_floor/shorts_fallback"))),
                List.of(
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/shorts/short_hall_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 4),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/shorts/short_hall_2", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/shorts/short_hall_3", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/shorts/passthru_big_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 2),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/shorts/passthru_big_2", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 2),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/shorts/passthru_big_3", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/shorts/passthru_chest_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/shorts/passthru_chest_2", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/shorts/passthru_chest_3", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/shorts/passthru_chest_4", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/shorts/passthru_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 6),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/shorts/passthru_2", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 6),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/shorts/passthru_3", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 6),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/shorts/room_2", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/shorts/room_3", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/shorts/room_4", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/shorts/room_5", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/shorts/room_chest_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 2),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/shorts/room_chest_2", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 2),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/shorts/room_chest_3", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 2),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/shorts/room_chest_4", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/shorts/room_chest_5", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 1)
                ),
                StructureTemplatePool.Projection.RIGID));
        context.register(VAULT_SECOND_FLOOR_SHORTS_FALLBACK, new StructureTemplatePool(
                empty,
                List.of(
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/shorts/room_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/shorts/room_chest_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 2),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/shorts/room_chest_2", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 2),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/shorts/room_chest_3", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 2),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/shorts/room_chest_4", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 2)
                ),
                StructureTemplatePool.Projection.RIGID));
        context.register(VAULT_SECOND_FLOOR_TALL_HALLS, new StructureTemplatePool(
                pools.getOrThrow(ResourceKey.create(Registries.TEMPLATE_POOL, id("vault/second_floor/tall_halls_fallback"))),
                List.of(
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/tall_halls/hall_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/tall_halls/hall_2", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 2),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/tall_halls/hall_3", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 4),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/tall_halls/hall_4", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 4),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/tall_halls/hall_5", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/tall_halls/endcap_chest_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/tall_halls/endcap_chest_2", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/tall_halls/endcap_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/tall_halls/endcap_2", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 2),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/tall_halls/endcap_3", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 2),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/tall_halls/endcap_pillar_chest_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/tall_halls/endcap_pillar_chest_2", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/tall_halls/endcap_pillar_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/tall_halls/endcap_pillar_2", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/tall_halls/endcap_pillar_3", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 1)
                ),
                StructureTemplatePool.Projection.RIGID));
        context.register(VAULT_SECOND_FLOOR_TALL_HALLS_FALLBACK, new StructureTemplatePool(
                empty,
                List.of(
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/tall_halls/endcap_chest_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/tall_halls/endcap_chest_2", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/tall_halls/endcap_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/tall_halls/endcap_2", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/tall_halls/endcap_3", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/tall_halls/endcap_pillar_chest_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/tall_halls/endcap_pillar_chest_2", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/tall_halls/endcap_pillar_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/tall_halls/endcap_pillar_2", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/tall_halls/endcap_pillar_3", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 1)
                ),
                StructureTemplatePool.Projection.RIGID));
        context.register(VAULT_SECOND_FLOOR_TALL_HALLS_NON_END, new StructureTemplatePool(
                pools.getOrThrow(ResourceKey.create(Registries.TEMPLATE_POOL, id("vault/second_floor/tall_halls_fallback"))),
                List.of(
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/tall_halls/hall_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 2),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/tall_halls/hall_2", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 1),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/tall_halls/hall_3", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/tall_halls/hall_4", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 3),
                Pair.of(StructurePoolElement.single("paradise_lost:vault/second_floor/tall_halls/hall_5", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 1)
                ),
                StructureTemplatePool.Projection.RIGID));
        context.register(VAULT_STARTS, new StructureTemplatePool(
                empty,
                List.of(
                Pair.of(StructurePoolElement.single("paradise_lost:vault/starts/start_1", processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id("vault_proc")))), 1)
                ),
                StructureTemplatePool.Projection.RIGID));
    }
}
