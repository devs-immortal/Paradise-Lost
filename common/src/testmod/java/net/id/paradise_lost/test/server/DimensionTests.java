package net.id.paradise_lost.test.server;

import net.id.paradise_lost.registry.BlockRegistry;
import net.id.paradise_lost.test.Step;
import net.id.paradise_lost.test.Test;
import net.id.paradise_lost.test.TestFactory;
import net.id.paradise_lost.util.RegistryUtil;
import net.id.paradise_lost.world.ParadiseLostGameRules;
import net.id.paradise_lost.world.dimension.ParadiseLostDimension;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;

import java.util.List;
import java.util.function.Predicate;

import static net.id.paradise_lost.test.TestHelpers.check;
import static net.id.paradise_lost.test.server.ServerTestHelpers.*;

public final class DimensionTests {
    private static final ResourceKey<Level> PARADISE = ParadiseLostDimension.PARADISE_LOST_WORLD_KEY;
    private static final BlockPos ORIGIN = new BlockPos(0, 128, 0);

    private DimensionTests() {
    }

    @ServerTest("the Paradise Lost level is loaded")
    private static Step levelLoaded() {
        return Step.run(0, () -> check(ServerTestRunner.server().getLevel(PARADISE) != null,
                "level " + PARADISE.location() + " is missing"));
    }

    public static void buildPortalFrame(ServerLevel world, BlockPos pos, Direction across) {
        for (int x = -1; x <= 2; x++) {
            for (int y = 0; y <= 4; y++) {
                boolean edge = x == -1 || x == 2 || y == 0 || y == 4;
                world.setBlockAndUpdate(pos.relative(across, x).above(y), edge
                        ? BlockRegistry.BLOOMED_CALCITE.get().defaultBlockState()
                        : Blocks.AIR.defaultBlockState());
            }
        }
    }

    @ServerTest(value = "does not light when paradisePortalEnabled is false", group = "Portal")
    private static List<Step> portalDisabled() {
        return List.of(
                Step.run(0, () -> {
                    MinecraftServer server = ServerTestRunner.server();
                    server.getGameRules().getRule(ParadiseLostGameRules.PARADISE_PORTAL_ENABLED).set(false, server);
                    buildPortalFrame(level(), ahead(3, 0), FACING.getClockWise());
                    level().setBlockAndUpdate(ahead(3, 0).above(), Blocks.WATER.defaultBlockState());
                }),
                Step.run(10, () -> {
                    MinecraftServer server = ServerTestRunner.server();
                    boolean lit = level().getBlockState(ahead(3, 0).above()).is(BlockRegistry.BLUE_PORTAL.get());
                    server.getGameRules().getRule(ParadiseLostGameRules.PARADISE_PORTAL_ENABLED).set(true, server);
                    check(!lit, "portal lit while disabled");
                })
        );
    }

    @TestFactory
    private static List<Test> biomes() {
        return ServerTestRunner.server().registryAccess().lookupOrThrow(Registries.BIOME).listElementIds()
                .filter(key -> key.location().getNamespace().equals("paradise_lost"))
                .map(DimensionTests::biomeGenerates)
                .toList();
    }

    @TestFactory
    private static List<Test> structures() {
        return ServerTestRunner.server().registryAccess().lookupOrThrow(Registries.STRUCTURE).listElementIds()
                .filter(key -> key.location().getNamespace().equals("paradise_lost"))
                .map(DimensionTests::structureGenerates)
                .toList();
    }

    private static Test biomeGenerates(ResourceKey<Biome> biome) {
        return new Test("Worldgen", "biome " + biome.location().getPath() + " generates", Step.run(0, () -> {
            ServerLevel paradise = ServerTestRunner.server().getLevel(PARADISE);
            var found = paradise.findClosestBiome3d(holder -> holder.is(biome), ORIGIN, 6400, 32, 64);
            check(found != null, "not found within 6400 blocks of " + ORIGIN.toShortString());
        }));
    }

    private static Test structureGenerates(ResourceKey<Structure> structure) {
        return new Test("Worldgen", "structure " + structure.location().getPath() + " generates", Step.run(0, () -> {
            ServerLevel paradise = ServerTestRunner.server().getLevel(PARADISE);
            var holder = paradise.registryAccess().lookupOrThrow(Registries.STRUCTURE).getOrThrow(structure);
            var found = paradise.getChunkSource().getGenerator().findNearestMapStructure(paradise, HolderSet.direct(holder), ORIGIN, 48, false);
            check(found != null, "not found within 48 chunks of " + ORIGIN.toShortString() + ", " + placementSpots(paradise, holder));

            BlockPos at = found.getFirst();
            paradise.getChunk(at.getX() >> 4, at.getZ() >> 4, ChunkStatus.FULL, true);
        }));
    }

    private static String placementSpots(ServerLevel paradise, Holder<Structure> structure) {
        var chunks = paradise.getChunkSource();
        int spots = 0, land = 0, biome = 0, start = 0, startInBiome = 0, stored = 0;

        for (var placement : chunks.getGeneratorState().getPlacementsForStructure(structure)) {
            if (!(placement instanceof RandomSpreadStructurePlacement spread)) continue;
            int regions = 48 / spread.spacing();
            for (int x = -regions; x <= regions; x++) {
                for (int z = -regions; z <= regions; z++) {
                    ChunkPos spot = spread.getPotentialStructureChunk(chunks.getGeneratorState().getLevelSeed(), x * spread.spacing(), z * spread.spacing());
                    int height = chunks.getGenerator().getBaseHeight(spot.getMiddleBlockX(), spot.getMiddleBlockZ(), Heightmap.Types.WORLD_SURFACE_WG, paradise, chunks.randomState());
                    spots++;

                    if (height <= paradise.getMinBuildHeight())
                        continue;
                    land++;

                    if (!structure.value().biomes().contains(paradise.getBiome(new BlockPos(spot.getMiddleBlockX(), height, spot.getMiddleBlockZ()))))
                        continue;
                    biome++;

                    if (startAt(paradise, structure, spot, b -> true))
                        start++;

                    if (!startAt(paradise, structure, spot, structure.value().biomes()::contains))
                        continue;
                    startInBiome++;

                    var generated = paradise.getChunk(spot.x, spot.z, ChunkStatus.STRUCTURE_STARTS).getStartForStructure(structure.value());
                    if (generated != null && generated.isValid())
                        stored++;
                }
            }
        }

        return spots + " placement spots, " + land + " with land " + biome + " with land in allowed biome "
                + start + " where the start piece fits " + startInBiome + " of those pass the biome check, "
                + stored + " of those have a start after the chunk generates";
    }

    private static boolean startAt(ServerLevel paradise, Holder<Structure> structure, ChunkPos spot, Predicate<Holder<Biome>> validBiome) {
        var chunks = paradise.getChunkSource();
        var context = new Structure.GenerationContext(paradise.registryAccess(), chunks.getGenerator(), chunks.getGenerator().getBiomeSource(),
                chunks.randomState(), paradise.getStructureManager(), paradise.getSeed(), spot, paradise, validBiome);
        return structure.value().findValidGenerationPoint(context).isPresent();
    }

    @ServerTest(value = "RegistryUtil handles a missing dimension type", group = "Skylands")
    private static Step registryUtilMissingKey() {
        return Step.run(0, () -> {
            ResourceKey<DimensionType> missing = ResourceKey.create(Registries.DIMENSION_TYPE, ResourceLocation.fromNamespaceAndPath("paradise_lost", "missing"));

            boolean matches;
            try {
                matches = RegistryUtil.dimensionMatches(level(), missing);
            } catch (RuntimeException e) {
                throw new AssertionError(e.getMessage());
            }

            check(!matches, "returned true for a missing key");
        });
    }
}
