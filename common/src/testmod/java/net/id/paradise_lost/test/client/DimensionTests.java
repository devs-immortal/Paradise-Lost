package net.id.paradise_lost.test.client;

import net.id.paradise_lost.test.Recorder;
import net.id.paradise_lost.test.Step;
import net.id.paradise_lost.registry.BlockRegistry;
import net.id.paradise_lost.util.ParadiseLostDamageTypes;
import net.id.paradise_lost.world.ParadiseLostGameRules;
import net.id.paradise_lost.world.dimension.ParadiseLostDimension;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;

import static net.id.paradise_lost.test.TestHelpers.*;
import static net.id.paradise_lost.test.client.ClientTestHelpers.*;
import static net.id.paradise_lost.test.server.DimensionTests.buildPortalFrame;
import static net.id.paradise_lost.registry.ItemRegistry.*;

public final class DimensionTests {
    private static final ResourceKey<Level> PARADISE = ParadiseLostDimension.PARADISE_LOST_WORLD_KEY;

    private static BlockPos pos;
    private static Direction across;

    private DimensionTests() {
    }

    private static void buildFrame(ServerPlayer player) {
        across = player.getDirection().getClockWise();
        pos = ahead(player, 3, 0);
        buildPortalFrame(player.serverLevel(), pos, across);
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
