package net.id.paradise_lost.test.client;

import net.id.paradise_lost.test.Recorder;
import net.id.paradise_lost.test.Step;
import net.id.paradise_lost.test.Test;
import net.id.paradise_lost.test.TestFactory;
import net.id.paradise_lost.registry.BlockRegistry;
import net.id.paradise_lost.util.ParadiseLostDamageTypes;
import net.id.paradise_lost.util.RegistryUtil;
import net.id.paradise_lost.world.ParadiseLostGameRules;
import net.id.paradise_lost.world.dimension.ParadiseLostDimension;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

import static net.id.paradise_lost.test.TestHelpers.*;
import static net.id.paradise_lost.test.client.ClientTestHelpers.*;
import static net.id.paradise_lost.registry.ItemRegistry.*;

public final class DimensionTests {
    private static final ResourceKey<Level> PARADISE = ParadiseLostDimension.PARADISE_LOST_WORLD_KEY;
    private static final BlockPos ORIGIN = new BlockPos(0, 128, 0);

    private static BlockPos pos;
    private static Direction across;
    private static CompletableFuture<String> search;

    private DimensionTests() {
    }

    private static void buildFrame(ServerPlayer player) {
        ServerLevel world = player.serverLevel();
        across = player.getDirection().getClockWise();
        pos = ahead(player, 3, 0);

        for (int x = -1; x <= 2; x++) {
            for (int y = 0; y <= 4; y++) {
                boolean edge = x == -1 || x == 2 || y == 0 || y == 4;
                world.setBlockAndUpdate(pos.relative(across, x).above(y), edge
                        ? BlockRegistry.BLOOMED_CALCITE.get().defaultBlockState()
                        : Blocks.AIR.defaultBlockState());
            }
        }
    }

    private static BlockPos opening() {
        return pos.above();
    }

    private static boolean openingIs(ServerLevel world, Block block) {
        for (int x = 0; x <= 1; x++) {
            for (int y = 1; y <= 3; y++) {
                if (!world.getBlockState(pos.relative(across, x).above(y)).is(block))
                    return false;
            }
        }
        return true;
    }

    @ClientTest(value = "does not light when paradisePortalEnabled is false", group = "Portal")
    private static List<Step> portalDisabled() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    server.getGameRules().getRule(ParadiseLostGameRules.PARADISE_PORTAL_ENABLED).set(false, server);
                    buildFrame(player(server));
                    world(server).setBlockAndUpdate(opening(), Blocks.WATER.defaultBlockState());
                })),
                Step.run(10, () -> onServer(server -> {
                    boolean lit = world(server).getBlockState(opening()).is(BlockRegistry.BLUE_PORTAL.get());
                    server.getGameRules().getRule(ParadiseLostGameRules.PARADISE_PORTAL_ENABLED).set(true, server);
                    check(!lit, "portal lit while disabled");
                }))
        );
    }

    @ClientTest(value = "water lights it, it leads to Paradise Lost and back", group = "Portal")
    private static List<Step> portalTravel() {
        return portalTravelSteps();
    }

    static List<Step> portalTravelSteps() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    buildFrame(player(server));
                    world(server).setBlockAndUpdate(opening(), Blocks.WATER.defaultBlockState());
                })),
                Step.until("portal to light", 20, () -> fromServer(server -> openingIs(world(server), BlockRegistry.BLUE_PORTAL.get()))),
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    player.teleportTo(opening().getX() + 0.5, opening().getY(), opening().getZ() + 0.5);
                })),
                Step.until("client in Paradise Lost", 300, () -> client().level.dimension() == PARADISE && client().screen == null),
                Step.run(20, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    BlockPos exit = BlockPos.betweenClosedStream(player.blockPosition().offset(-4, -4, -4), player.blockPosition().offset(4, 4, 4))
                            .filter(p -> player.serverLevel().getBlockState(p).is(BlockRegistry.BLUE_PORTAL.get()))
                            .findFirst().map(BlockPos::immutable).orElse(null);
                    check(exit != null, "no exit portal near the player at " + player.blockPosition());

                    player.setPortalCooldown(0);
                    player.teleportTo(exit.getX() + 0.5, exit.getY(), exit.getZ() + 0.5);
                })),
                Step.until("client back in the Overworld", 300, () -> client().level.dimension() == Level.OVERWORLD && client().screen == null)
        );
    }

    @TestFactory
    private static List<Test> biomes() {
        return client().level.registryAccess().lookupOrThrow(Registries.BIOME).listElementIds()
                .filter(key -> key.location().getNamespace().equals("paradise_lost"))
                .map(DimensionTests::biomeGenerates)
                .toList();
    }

    @TestFactory
    private static List<Test> structures() {
        return fromServer(server -> server.registryAccess().lookupOrThrow(Registries.STRUCTURE).listElementIds()
                .filter(key -> key.location().getNamespace().equals("paradise_lost"))
                .toList())
                .stream()
                .map(DimensionTests::structureGenerates)
                .toList();
    }

    private static Test biomeGenerates(ResourceKey<Biome> biome) {
        return new Test("Worldgen", "biome " + biome.location().getPath() + " generates", Step.run(0, () -> onServer(server -> {
            ServerLevel paradise = server.getLevel(PARADISE);
            var found = paradise.findClosestBiome3d(holder -> holder.is(biome), ORIGIN, 6400, 32, 64);
            check(found != null, "not found within 6400 blocks of " + ORIGIN.toShortString());
        })));
    }

    private static Test structureGenerates(ResourceKey<Structure> structure) {
        return new Test("Worldgen", "structure " + structure.location().getPath() + " generates",
                Step.run(0, () -> {
                    var server = client().getSingleplayerServer();
                    search = server.submit(() -> {
                        ServerLevel paradise = server.getLevel(PARADISE);
                        var holder = paradise.registryAccess().lookupOrThrow(Registries.STRUCTURE).getOrThrow(structure);
                        var found = paradise.getChunkSource().getGenerator().findNearestMapStructure(paradise, HolderSet.direct(holder), ORIGIN, 48, false);
                        if (found == null)
                            return "not found within 48 chunks of " + ORIGIN.toShortString() + ", " + placementSpots(paradise, holder);

                        BlockPos at = found.getFirst();
                        paradise.getChunk(at.getX() >> 4, at.getZ() >> 4, ChunkStatus.FULL, true);
                        return null;
                    });
                }),
                Step.until("structure search", 20 * 180, () -> search.isDone()),
                Step.run(0, () -> {
                    String problem = search.join();
                    check(problem == null, problem);
                })
        );
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

    @ClientTest(value = "every Paradise Lost block renders in Paradise Lost", group = "Skylands")
    private static List<Step> blockShowroom() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    player.setGameMode(GameType.SPECTATOR);
                    teleport(player, PARADISE, 0.5, 230, -6.5, 30);
                })),
                Step.until("client in Paradise Lost", 200, () -> client().level.dimension() == PARADISE && client().screen == null),
                Step.run(20, () -> onServer(server -> {
                    ServerLevel world = server.getLevel(PARADISE);
                    BlockPos corner = new BlockPos(-12, 220, 0);

                    List<Block> blocks = new ArrayList<>();
                    BuiltInRegistries.BLOCK.forEach(block -> {
                        if (BuiltInRegistries.BLOCK.getKey(block).getNamespace().equals("paradise_lost"))
                            blocks.add(block);
                    });

                    List<String> failed = new ArrayList<>();
                    for (int i = 0; i < blocks.size(); i++) {
                        BlockPos at = corner.offset(i % 25, 0, (i / 25) * 2);
                        world.setBlock(at.below(), Blocks.STONE.defaultBlockState(), Block.UPDATE_CLIENTS);
                        try {
                            world.setBlock(at, blocks.get(i).defaultBlockState(), Block.UPDATE_CLIENTS);
                        } catch (RuntimeException e) {
                            failed.add(BuiltInRegistries.BLOCK.getKey(blocks.get(i)) + ": " + e.getMessage());
                            world.setBlock(at, Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);
                        }
                    }

                    check(failed.isEmpty(), "could not place " + failed);
                })),
                Step.run(60, () -> screenshot("blocks.png"))
        );
    }

    @ClientTest(value = "RegistryUtil handles a missing dimension type", group = "Skylands")
    private static Step registryUtilMissingKey() {
        return Step.run(0, () -> {
            ResourceKey<DimensionType> missing = ResourceKey.create(Registries.DIMENSION_TYPE, ResourceLocation.fromNamespaceAndPath("paradise_lost", "missing"));

            boolean matches;
            try {
                matches = RegistryUtil.dimensionMatches(client().level, missing);
            } catch (RuntimeException e) {
                throw new AssertionError(e.getMessage());
            }

            check(!matches, "returned true for a missing key");
        });
    }

    @ClientTest(value = "clouds render 3 layers", group = "Skylands")
    private static List<Step> skylandsClouds() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    player.setGameMode(GameType.SPECTATOR);
                    teleport(player, ParadiseLostDimension.PARADISE_LOST_WORLD_KEY, player.getX(), 220, player.getZ(), -5);
                })),
                Step.until("client in Paradise Lost", 200, () -> client().level.dimension() == ParadiseLostDimension.PARADISE_LOST_WORLD_KEY && client().screen == null),
                Step.run(60, () -> {
                    Recorder.cloudCalls.set(0);
                    Recorder.frames.set(0);
                }),
                Step.run(20, () -> screenshot("skylands_sky.png")),
                Step.run(0, () -> {
                    int frames = Recorder.frames.get();
                    int clouds = Recorder.cloudCalls.get();
                    check(frames > 0 && clouds >= 3 * frames, clouds + " cloud draws in " + frames + " frames, expected 3 layers per frame");
                })
        );
    }

    private static void intoVoid(ServerPlayer player) {
        teleport(player, ParadiseLostDimension.PARADISE_LOST_WORLD_KEY, player.getX(), -150, player.getZ(), 0);
    }

    @ClientTest(value = "kills when paradiseVoidKills is true", group = "Void")
    private static List<Step> voidKills() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    server.getGameRules().getRule(ParadiseLostGameRules.PARADISE_VOID_KILLS).set(true, server);
                    intoVoid(player(server));
                })),
                Step.until("death by the void", 100, () -> Recorder.playerDiedFrom(DamageTypes.FELL_OUT_OF_WORLD))
        );
    }

    @ClientTest(value = "teleports to the overworld when paradiseVoidKills is false", group = "Void")
    private static List<Step> voidTeleport() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    teleport(player, ParadiseLostDimension.PARADISE_LOST_WORLD_KEY, player.getX(), -120, player.getZ(), 0);
                })),
                Step.until("teleport to the overworld", 100, () -> fromServer(server -> player(server).level().dimension() == Level.OVERWORLD)),
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    check(player.getY() > 300, "arrived at y=" + player.getY());
                    check(player.hasEffect(MobEffects.CONFUSION), "no nausea");
                    check(!Recorder.playerHurtBy(DamageTypes.FELL_OUT_OF_WORLD), "took void damage before the teleport");
                })),
                Step.until("fall damage from paradise", 600, () -> Recorder.playerHurtBy(ParadiseLostDamageTypes.FALL_FROM_PARADISE))
        );
    }

    @ClientTest(value = "levitation totem saves from the void", group = "Void")
    private static List<Step> voidTotem() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    player.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(TOTEM_OF_LEVITATION.get()));
                    intoVoid(player);
                })),
                Step.until("levitation", 60, () -> fromServer(server -> player(server).hasEffect(MobEffects.LEVITATION))),
                Step.run(0, () -> onServer(server -> check(player(server).getOffhandItem().isEmpty(), "totem was not used up"))),
                Step.until("rise above the void line", 200, () -> fromServer(server -> {
                    ServerPlayer player = player(server);
                    return player.level().dimension() != ParadiseLostDimension.PARADISE_LOST_WORLD_KEY
                            || player.getY() > player.level().getMinBuildHeight() - 64;
                })),
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    check(player.level().dimension() == ParadiseLostDimension.PARADISE_LOST_WORLD_KEY,
                            "teleported to " + player.level().dimension().location() + " after the totem was used");
                    check(!Recorder.playerHurtBy(DamageTypes.FELL_OUT_OF_WORLD), "took void damage after the totem was used");
                    check(Recorder.playerDeaths.isEmpty(), "player died");
                }))
        );
    }

    @ClientTest(value = "levitation totem in the main hand saves from the void when paradiseVoidKills is true", group = "Void")
    private static List<Step> voidTotemWhenVoidKills() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    server.getGameRules().getRule(ParadiseLostGameRules.PARADISE_VOID_KILLS).set(true, server);

                    ServerPlayer player = player(server);
                    hold(player, TOTEM_OF_LEVITATION.get());
                    intoVoid(player);
                })),
                Step.until("levitation", 60, () -> fromServer(server -> player(server).hasEffect(MobEffects.LEVITATION))),
                Step.run(0, () -> onServer(server -> check(player(server).getMainHandItem().isEmpty(), "totem was not used up"))),
                Step.until("rise above the void line", 200, () -> fromServer(server -> {
                    ServerPlayer player = player(server);
                    return player.isDeadOrDying() || player.getY() > player.level().getMinBuildHeight() - 64;
                })),
                Step.run(0, () -> {
                    check(Recorder.playerDeaths.isEmpty(), "player died");
                    check(!Recorder.playerHurtBy(DamageTypes.FELL_OUT_OF_WORLD), "took void damage after the totem was used");
                })
        );
    }

    @ClientTest(value = "levitating player is still sent to the overworld when levitation ends", group = "Void")
    private static List<Step> voidAfterLevitation() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    player.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 20, 0));
                    intoVoid(player);
                })),
                Step.until("teleport to the overworld", 200, () -> fromServer(server -> player(server).level().dimension() == Level.OVERWORLD)),
                Step.run(0, () -> {
                    check(Recorder.playerDeaths.isEmpty(), "player died");
                    check(!Recorder.playerHurtBy(DamageTypes.FELL_OUT_OF_WORLD), "took void damage");
                })
        );
    }
}
