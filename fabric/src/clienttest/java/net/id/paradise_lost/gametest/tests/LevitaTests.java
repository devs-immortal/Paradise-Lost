package net.id.paradise_lost.gametest.tests;

import net.id.paradise_lost.ParadiseLost;
import net.id.paradise_lost.attachments.MinecartFloating;
import net.id.paradise_lost.block.mechanical.LevitaRailBlock;
import net.id.paradise_lost.gametest.ClientTest;
import net.id.paradise_lost.gametest.Step;
import net.id.paradise_lost.entity.MinecartFloatPoseAccess;
import net.id.paradise_lost.entity.ParadiseLostEntityExtensions;
import net.id.paradise_lost.entity.block.FloatingBlockEntity;
import net.id.paradise_lost.entity.projectile.LevitaArrow;
import net.id.paradise_lost.item.tool.base_tools.GravityTool;
import net.id.paradise_lost.registry.BlockRegistry;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.entity.vehicle.MinecartFurnace;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.StructureBlock;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.DoublePredicate;

import static net.id.paradise_lost.gametest.TestHelpers.*;
import static net.id.paradise_lost.registry.ItemRegistry.*;

public final class LevitaTests {
    private static final int CHARGE = 82;
    private static final int STAIRS = 6;
    private static final int GAP_STAIRS = 12;
    private static final int GAP_AT = 3;
    private static final int GAP_LEN = 5;
    private static final int CEILING_HEIGHT = 10;
    private static final int SIDE_VIEW_DIST = 8;
    private static final int SIDE_VIEW_DIST_WIDE = 18;

    private enum ShotFrame {
        CLOSE, WIDE
    }

    private static BlockPos pos;
    private static int entityId;
    private static int secondId;
    private static int thirdId;
    private static double startY;

    private static BlockPos start;
    private static Direction dir;
    private static Direction landDir;
    private static BlockPos landCenter;
    private static int ticks;
    private static double seen;
    private static double speedA;
    private static double speedB;
    private static double speedC;
    private static String state;
    private static double levitaTop;
    private static double plainTop;
    private static int activeStairs = STAIRS;
    private static int activeGapAt = -1;
    private static int activeGapLen = 0;
    private static final int CONNECT_STAIRS = 5;
    private static final int LANDING_HALF = 14;
    private static int connectGapAir = 1;
    private static final int WALK_STAIRS = 10;
    private static final int WALK_RAIL_RUN = 3;
    private static double shotResumeAlong = 0;

    private LevitaTests() {
    }

    private static boolean clientSeesFloating(Block block) {
        return !client().level.getEntitiesOfClass(FloatingBlockEntity.class, new AABB(pos).inflate(2, 30, 2),
                e -> e.getBlockState().is(block)).isEmpty();
    }

    @ClientTest("levita floats up and the client sees the floating block")
    private static List<Step> levitaFloats() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    pos = ahead(player, 3, 0);
                    player.serverLevel().setBlockAndUpdate(pos, BlockRegistry.LEVITA.get().defaultBlockState());
                })),
                Step.until("levita to float on the server", 20, () -> fromServer(server -> world(server).getBlockState(pos).isAir())),
                Step.until("client to see the floating levita", 20, () -> clientSeesFloating(BlockRegistry.LEVITA.get()))
        );
    }

    @ClientTest("levitator floats only with redstone power")
    private static List<Step> levitatorNeedsPower() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    pos = ahead(player, 3, 0);
                    player.serverLevel().setBlockAndUpdate(pos, BlockRegistry.LEVITATOR.get().defaultBlockState());
                })),
                Step.run(10, () -> onServer(server -> {
                    ServerLevel world = world(server);
                    check(world.getBlockState(pos).is(BlockRegistry.LEVITATOR.get()), "unpowered levitator floated away");
                    world.setBlockAndUpdate(pos.relative(player(server).getDirection().getClockWise()), Blocks.REDSTONE_BLOCK.defaultBlockState());
                })),
                Step.until("powered levitator to float", 20, () -> fromServer(server -> world(server).getBlockState(pos).isAir())),
                Step.until("client to see the floating levitator", 20, () -> clientSeesFloating(BlockRegistry.LEVITATOR.get()))
        );
    }

    @ClientTest("wand floats a block and loses durability")
    private static List<Step> levitaWandFloatsBlock() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    pos = ahead(player, 2, 0);
                    player.serverLevel().setBlockAndUpdate(pos, Blocks.STONE.defaultBlockState());
                    hold(player, LEVITA_WAND.get(), 1);
                })),
                Step.run(10, () -> useOn(pos)),
                Step.until("client to see the floating stone", 20, () -> clientSeesFloating(Blocks.STONE)),
                Step.run(5, () -> onServer(server -> {
                    int damage = player(server).getMainHandItem().getDamageValue();
                    check(damage == 4, "wand damage is " + damage + ", expected 4");
                }))
        );
    }

    @ClientTest("gravity flipped by the wand reaches the client")
    private static List<Step> levitaWandFlipsEntity() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    entityId = spawnNoAi(player, EntityType.COW, ahead(player, 3, 0));
                })),
                Step.run(10, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    hold(player, LEVITA_WAND.get(), 1);
                    GravityTool.flipEntity(player.getMainHandItem(), player, (Cow) world(server).getEntity(entityId), InteractionHand.MAIN_HAND);
                })),
                Step.until("client cow to be flipped", 20, () -> ((ParadiseLostEntityExtensions) client().level.getEntity(entityId)).getFlipped()),
                Step.until("flip to wear off on the client", 60, () -> !((ParadiseLostEntityExtensions) client().level.getEntity(entityId)).getFlipped())
        );
    }

    @ClientTest("powered levita rail makes a minecart float")
    private static List<Step> levitaRailFloatsMinecart() {
        return List.of(
                setup(world -> {
                    track(world, start, "rLrr");
                    entityId = launch(world, EntityType.MINECART, start, 0.4);
                }),
                Step.until("cart to float on the server", 60, () -> serverFloating(entityId)),
                Step.until("cart to float on the client", 20, () -> clientFloating(entityId))
        );
    }

    @ClientTest("arrow shot from a bow rises on the client")
    private static List<Step> levitaArrowRises() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    hold(player, Items.BOW, 1);
                    player.getInventory().setItem(1, new ItemStack(LEVITA_ARROW.get(), 4));
                })),
                Step.run(10, () -> client().options.keyUse.setDown(true)),
                Step.run(25, () -> client().options.keyUse.setDown(false)),
                Step.until("client to see the arrow", 20, () -> levitaArrow() != null),
                Step.run(0, () -> startY = levitaArrow().getY()),
                Step.run(10, () -> {
                    Entity arrow = levitaArrow();
                    check(arrow != null && arrow.getY() > startY + 0.5, "arrow went from y=" + startY + " to y=" + (
                            arrow == null
                                    ? "null"
                                    : arrow.getY()));
                })
        );
    }

    @ClientTest("one powered rail gives one float charge that runs out on both sides")
    private static List<Step> railGivesOneCharge() {
        return List.of(
                setup(world -> {
                    track(world, start, "rLrrr");
                    entityId = launch(world, EntityType.MINECART, start, 0.4);
                }),
                Step.until("cart to float on the server", 60, () -> serverFloating(entityId)),
                Step.run(0, () -> onServer(server -> {
                    int time = MinecartFloating.getFloatTime(serverCart(server, entityId));
                    check(time > CHARGE - 12 && time <= CHARGE, "float time right after the rail is " + time + ", expected about " + CHARGE);
                })),
                Step.until("float to end on the server", CHARGE + 20, () -> fromServer(server -> {
                    AbstractMinecart cart = serverCart(server, entityId);
                    checkFloatRotation(cart, "server");
                    return !MinecartFloating.isFloating(cart);
                })),
                Step.until("float to end on the client", 20, () -> {
                    AbstractMinecart cart = clientCart(entityId);
                    checkFloatRotation(cart, "client");
                    return !MinecartFloating.isFloating(cart);
                })
        );
    }

    @ClientTest("float time stacks over a run of powered rails")
    private static List<Step> floatTimeStacks() {
        return List.of(
                setup(world -> {
                    track(world, start, "rLLLrr");
                    entityId = launch(world, EntityType.MINECART, start, 0.4);
                    seen = 0;
                }),
                Step.until("cart to pass the run", 40, () -> fromServer(server -> {
                    AbstractMinecart cart = serverCart(server, entityId);
                    seen = Math.max(seen, MinecartFloating.getFloatTime(cart));
                    return along(cart) > 4.5;
                })),
                Step.run(0, () -> check(seen >= 3 * CHARGE - 10, "highest float time was " + (int) seen + ", expected at least " + (3 * CHARGE - 10)))
        );
    }

    @ClientTest("a second cart right behind the first also floats")
    private static List<Step> secondCartFloats() {
        return List.of(
                setup(world -> {
                    track(world, start, "rrLrrrrrrr");
                    entityId = launch(world, EntityType.MINECART, start, 0.4);
                }),
                Step.until("first cart to float", 40, () -> serverFloating(entityId)),
                Step.run(20, () -> onServer(server -> secondId = launch(world(server), EntityType.MINECART, start, 0.4))),
                Step.until("second cart to float", 40, () -> serverFloating(secondId))
        );
    }

    @ClientTest("rail trigger shows on the client and clears again")
    private static List<Step> triggerLockClears() {
        return List.of(
                setup(world -> {
                    track(world, start, "rLrrr");
                    pos = start.relative(dir, 1);
                    entityId = launch(world, EntityType.MINECART, start, 0.4);
                }),
                Step.until("rail to be triggered on the server", 40, () -> fromServer(server -> world(server).getBlockState(pos).getValue(LevitaRailBlock.TRIGGERED))),
                Step.until("rail to be triggered on the client", 20, () -> client().level.getBlockState(pos).getValue(LevitaRailBlock.TRIGGERED)),
                Step.until("trigger to clear on the server", 100, () -> fromServer(server -> !world(server).getBlockState(pos).getValue(LevitaRailBlock.TRIGGERED))),
                Step.until("trigger to clear on the client", 20, () -> !client().level.getBlockState(pos).getValue(LevitaRailBlock.TRIGGERED))
        );
    }

    @ClientTest("a standing cart on a powered rail floats in place")
    private static List<Step> standingCartStays() {
        return List.of(
                setup(world -> {
                    track(world, start, "L");
                    entityId = launch(world, EntityType.MINECART, start, 0);
                    ticks = 0;
                }),
                Step.until("cart to float", 40, () -> serverFloating(entityId)),
                Step.until("100 ticks in place", 110, () -> fromServer(server -> {
                    AbstractMinecart cart = serverCart(server, entityId);
                    check(cart.blockPosition().equals(start), "cart drifted to " + describe(cart) + floatState(cart));
                    return ++ticks >= 100;
                }))
        );
    }

    @ClientTest("levita rails neither boost nor brake a cart")
    private static List<Step> railNeitherBoostsNorBrakes() {
        return List.of(
                setup(world -> {
                    Direction side = dir.getClockWise();
                    track(world, start, "rrrrrr");
                    track(world, start.relative(side, 3), "rrlrrr");
                    track(world, start.relative(side, 6), "rrLrrr");

                    entityId = launch(world, EntityType.MINECART, start, 0.4);
                    secondId = launch(world, EntityType.MINECART, start.relative(side, 3), 0.4);
                    thirdId = launch(world, EntityType.MINECART, start.relative(side, 6), 0.4);
                    speedA = speedB = speedC = -1;
                }),
                Step.until("all carts to pass the levita rails", 40, () -> fromServer(server -> {
                    speedA = speedPast(serverCart(server, entityId), speedA);
                    speedB = speedPast(serverCart(server, secondId), speedB);
                    speedC = speedPast(serverCart(server, thirdId), speedC);
                    check(!MinecartFloating.isFloating(serverCart(server, secondId)), "cart over the unpowered rail floats");
                    return speedA >= 0 && speedB >= 0 && speedC >= 0;
                })),
                Step.run(0, () -> {
                    List<String> wrong = new ArrayList<>();
                    expect(wrong, Math.abs(speedB - speedA) < 0.05, "unpowered levita rail: speed " + fmt(speedB) + ", plain rail: " + fmt(speedA));
                    expect(wrong, Math.abs(speedC - speedA) < 0.05, "powered levita rail: speed " + fmt(speedC) + ", plain rail: " + fmt(speedA));
                    checkAll(wrong);
                })
        );
    }

    @ClientTest("an unpowered levita rail ends the float")
    private static List<Step> unpoweredRailEndsFloat() {
        return List.of(
                setup(world -> {
                    track(world, start, "rLrrlrr");
                    entityId = launch(world, EntityType.MINECART, start, 0.4);
                }),
                Step.until("cart to float", 40, () -> serverFloating(entityId)),
                Step.until("float to end at the unpowered rail", 30, () -> !serverFloating(entityId)),
                Step.run(0, () -> onServer(server -> {
                    AbstractMinecart cart = serverCart(server, entityId);
                    check(along(cart) > 3.4, "float ended before the unpowered rail, at " + describe(cart));
                }))
        );
    }

    @ClientTest("power spreads along levita rails only")
    private static List<Step> powerSpreadsOnlyAlongLevitaRails() {
        return List.of(
                setup(world -> {
                    BlockPos other = start.relative(dir.getClockWise(), 3);
                    track(world, start, "llllllllll");
                    world.setBlockAndUpdate(start.relative(dir, 10), Blocks.POWERED_RAIL.defaultBlockState());
                    for (int i = 0; i < 3; i++) {
                        world.setBlockAndUpdate(other.relative(dir, i), Blocks.POWERED_RAIL.defaultBlockState());
                    }
                    track(world, other.relative(dir, 3), "ll");
                    world.setBlockAndUpdate(start.below(), Blocks.REDSTONE_BLOCK.defaultBlockState());
                    world.setBlockAndUpdate(other.below(), Blocks.REDSTONE_BLOCK.defaultBlockState());
                }),
                Step.run(10, () -> onServer(server -> {
                    ServerLevel world = world(server);
                    BlockPos other = start.relative(dir.getClockWise(), 3);
                    List<String> wrong = new ArrayList<>();
                    for (int i = 0; i < 10; i++) {
                        boolean powered = world.getBlockState(start.relative(dir, i)).getValue(PoweredRailBlock.POWERED);
                        expect(wrong, powered == i <= 8, "levita rail " + i + " powered=" + powered);
                    }
                    expect(wrong, !world.getBlockState(start.relative(dir, 10)).getValue(PoweredRailBlock.POWERED),
                            "vanilla powered rail after the levita run is powered");
                    expect(wrong, world.getBlockState(other.relative(dir, 2)).getValue(PoweredRailBlock.POWERED),
                            "vanilla powered rails are not powered");
                    expect(wrong, !world.getBlockState(other.relative(dir, 3)).getValue(PoweredRailBlock.POWERED),
                            "levita rail after vanilla powered run is powered");
                    checkAll(wrong);
                }))
        );
    }

    @ClientTest("floating cart crosses gap and lands on rails again")
    private static List<Step> floatsOverGapAndRejoins() {
        return List.of(
                setup(world -> {
                    track(world, start, "rLrr........rrrr");
                    trench(world, 4, 11);
                    entityId = launch(world, EntityType.MINECART, start, 0.4);
                }),
                Step.until("cart to reach the far rails", 80, () -> fromServer(server -> {
                    AbstractMinecart cart = serverCart(server, entityId);
                    double a = along(cart);
                    if (a > 3.5 && a < 12.0) {
                        check(Math.abs(cart.getY() - start.getY()) < 0.2, "cart is at y=" + fmt(cart.getY()) + " over the gap" + describe(cart) + floatState(cart));
                        checkFloatRotation(cart, "server gap");
                    }
                    return a > 12.0 && a < 16.0 && MinecartFloating.isCartOnRail(cart) && !MinecartFloating.isOffRail(cart);
                })),
                Step.run(0, () -> onServer(server -> {
                    AbstractMinecart cart = serverCart(server, entityId);
                    check(MinecartFloating.isCartOnRail(cart) && !MinecartFloating.isOffRail(cart),
                            "cart is not seated on the far rails: " + describe(cart) + floatState(cart));
                    check(along(cart) > 12.0 && along(cart) < 16.0, "cart not on the far rail run: " + describe(cart) + floatState(cart));
                    checkFloatRotation(cart, "server after flat reconnect");
                }))
        );
    }

    @ClientTest("floating cart falls when the float runs out")
    private static List<Step> fallsWhenFloatEnds() {
        return List.of(
                setup(world -> {
                    track(world, start, "rLrr");
                    trench(world, 4, 54);
                    entityId = launch(world, EntityType.MINECART, start, 0.4);
                }),
                Step.until("cart to float", 40, () -> serverFloating(entityId)),
                Step.until("float to end", CHARGE + 20, () -> fromServer(server -> {
                    AbstractMinecart cart = serverCart(server, entityId);
                    if (along(cart) > 3.5) {
                        check(Math.abs(cart.getY() - start.getY()) < 0.3, "floating cart is at y=" + fmt(cart.getY()) + describe(cart) + floatState(cart));
                        checkFloatRotation(cart, "server air");
                    }
                    return !MinecartFloating.isFloating(cart);
                })),
                Step.until("cart to fall into the trench", 40, () -> fromServer(server -> serverCart(server, entityId).getY() < start.getY() - 1.5))
        );
    }

    @ClientTest("wall stops a cart that just floated off the track")
    private static List<Step> wallStopsFloatingCart() {
        return List.of(
                setup(world -> {
                    track(world, start, "rLrr");
                    BlockPos wall = start.relative(dir, 5);
                    fill(world, wall.relative(dir.getClockWise()), wall.relative(dir.getCounterClockWise()).above(), Blocks.STONE.defaultBlockState());
                    entityId = launch(world, EntityType.MINECART, start, 0.4);
                    ticks = 0;
                }),
                Step.until("cart to float", 40, () -> serverFloating(entityId)),
                Step.until("40 ticks at the wall", 50, () -> fromServer(server -> {
                    AbstractMinecart cart = serverCart(server, entityId);
                    check(along(cart) < 4.5 - cart.getBbWidth() / 2 + 0.01, "cart went into the wall: " + describe(cart) + floatState(cart));
                    return ++ticks >= 40;
                }))
        );
    }

    @ClientTest("powered slope launches the cart upward at 45 degrees")
    private static List<Step> slopeLaunchesUpward() {
        return List.of(
                setup(world -> {
                    singleSlope(world, false);
                    entityId = launch(world, EntityType.MINECART, start.relative(dir, -2), 0.4);
                    ticks = 0;
                }),
                Step.until("cart to rise 3 blocks", 40, () -> fromServer(server -> {
                    AbstractMinecart cart = serverCart(server, entityId);
                    state = describe(cart) + floatState(cart);
                    return cart.getY() > start.getY() + 3 || ++ticks >= 39;
                })),
                Step.run(0, () -> onServer(server -> {
                    AbstractMinecart cart = serverCart(server, entityId);
                    check(cart.getY() > start.getY() + 3, "cart did not rise 3 blocks in 40 ticks:" + state);
                    check(MinecartFloating.getIncline(cart) == 1, "server incline is " + MinecartFloating.getIncline(cart) + floatState(cart));
                    check(Math.abs(side(cart)) < 0.1, "cart drifted sideways: " + describe(cart));
                    checkFloatRotation(cart, "server launch");
                })),
                Step.until("client to show the 45 degree float", 20, () -> {
                    AbstractMinecart cart = clientCart(entityId);
                    if (MinecartFloating.getIncline(cart) != 1
                            || !((MinecartFloatPoseAccess) cart).paradiseLost$hasRailRenderPose()) {
                        return false;
                    }
                    checkFloatRotation(cart, "client launch");
                    return Math.abs(Math.abs(cart.getXRot()) - 45) < 1;
                })
        );
    }

    @ClientTest("ceiling stops a rising cart")
    private static List<Step> ceilingStopsRisingCart() {
        return List.of(
                setup(world -> {
                    singleSlope(world, false);
                    Direction side = dir.getClockWise();
                    fill(world,
                            start.relative(dir, 1).relative(side).above(CEILING_HEIGHT),
                            start.relative(dir, 15).relative(side.getOpposite()).above(CEILING_HEIGHT),
                            Blocks.STONE.defaultBlockState());
                    entityId = launch(world, EntityType.MINECART, start.relative(dir, -2), 0.4);
                    ticks = 0;
                    seen = start.getY();
                }),
                Step.until("40 ticks under the ceiling", 50, () -> fromServer(server -> {
                    AbstractMinecart cart = serverCart(server, entityId);
                    seen = Math.max(seen, cart.getY());
                    check(cart.getBoundingBox().maxY <= start.getY() + CEILING_HEIGHT + 0.01,
                            "cart went into the ceiling: " + describe(cart) + floatState(cart));
                    return ++ticks >= 40;
                })),
                Step.run(0, () -> onServer(server -> {
                    AbstractMinecart cart = serverCart(server, entityId);
                    check(seen > start.getY() + 4, "cart only rose to y=" + fmt(seen) + ", so the high ceiling was never tested");
                    check(MinecartFloating.getIncline(cart) == 0, "ceiling hit should clear incline, was "
                            + MinecartFloating.getIncline(cart) + floatState(cart));
                    check(Math.abs(cart.getXRot()) < 2, "ceiling hit should flatten pitch, was "
                            + fmt(cart.getXRot()) + floatState(cart));
                    check(Math.abs(cart.getDeltaMovement().y) < 0.01, "ceiling hit should zero dy, motion="
                            + fmt(cart.getDeltaMovement().x) + "/" + fmt(cart.getDeltaMovement().y) + "/"
                            + fmt(cart.getDeltaMovement().z));
                }))
        );
    }

    @ClientTest("cart floating down a slope stays above the ground")
    private static List<Step> descendingCartStaysAboveGround() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    ServerLevel level = world(server);
                    player.setGameMode(GameType.CREATIVE);
                    server.tickRateManager().setFrozen(false);
                    dir = Direction.SOUTH;
                    teleport(player, level, player.getX(), player.getY(), player.getZ(), dir.toYRot(), 0.0F);
                    start = player.blockPosition().relative(dir, 2);
                    activeStairs = STAIRS;
                    activeGapAt = -1;
                    activeGapLen = 0;
                    shotResumeAlong = 0;
                    connectGapAir = 1;
                    singleSlope(level, true);
                    entityId = launch(level, EntityType.MINECART, start.relative(dir).above(), -0.4);
                    ticks = 0;
                    seen = 0;
                    Direction right = dir.getClockWise();
                    BlockPos side = player.blockPosition().relative(right, 5);
                    teleport(player, level, side.getX() + 0.5, player.getY(), side.getZ() + 0.5,
                            dir.getCounterClockWise().toYRot(), 0.0F);
                })),
                Step.until("40 ticks after the slope", 60, () -> fromServer(server -> {
                    AbstractMinecart cart = serverCart(server, entityId);
                    ServerLevel world = world(server);
                    seen = Math.min(seen, along(cart));
                    check(cart.getY() >= start.getY() - 0.1, "cart sank to y=" + fmt(cart.getY()) + describe(cart) + floatState(cart));
                    check(world.getBlockState(cart.blockPosition()).getCollisionShape(world, cart.blockPosition()).isEmpty(),
                            "cart is inside " + world.getBlockState(cart.blockPosition()).getBlock().getName().getString() + describe(cart) + floatState(cart));
                    return ++ticks >= 40;
                })),
                Step.run(0, () -> onServer(server -> {
                    AbstractMinecart cart = serverCart(server, entityId);
                    check(seen < -1, "setup: cart never left the bottom of the slope");
                    check(MinecartFloating.findRailPos(cart) == null, "cart should be off-rail after the slope"
                            + describe(cart) + floatState(cart));
                    check(MinecartFloating.getIncline(cart) == 0, "floor skim should clear incline"
                            + floatState(cart));
                    check(Math.abs(cart.getXRot()) < 2, "floor skim should flatten pitch"
                            + floatState(cart));
                    check(Math.abs(cart.getDeltaMovement().y) < 0.01, "floor skim should zero dy"
                            + describe(cart) + floatState(cart));
                }))
        );
    }

    @ClientTest("ridden cart stays on a long climbing run of powered slopes, also when it rolls back")
    private static List<Step> cartStaysOnClimbingRun() {
        return List.of(
                setup(world -> {
                    staircase(world, start, true);
                    entityId = launch(world, EntityType.MINECART, start, 0.4);
                    check(world.players().getFirst().startRiding(world.getEntity(entityId), true), "setup: startRiding returned false");
                    ticks = 0;
                }),
                Step.until("100 ticks on the run", 110, () -> stairsTick(along -> false))
        );
    }

    @ClientTest("ridden cart stays on a long descending run of powered slopes")
    private static List<Step> cartStaysOnDescendingRun() {
        return List.of(
                setup(world -> {
                    staircase(world, start, true);
                    entityId = launch(world, EntityType.MINECART, start.relative(dir, STAIRS).above(STAIRS), -0.4);
                    check(world.players().getFirst().startRiding(world.getEntity(entityId), true), "setup: startRiding returned false");
                    ticks = 0;
                }),
                Step.until("cart to reach the bottom", 110, () -> stairsTick(along -> along < -1.5)),
                Step.run(0, () -> onServer(server -> {
                    AbstractMinecart cart = serverCart(server, entityId);
                    check(along(cart) < -1.5, "cart did not reach the bottom: " + describe(cart) + floatState(cart));
                }))
        );
    }

    @ClientTest("empty cart climbs a long run of powered levita slopes")
    private static List<Step> emptyCartAscendsPoweredSlopes() {
        return List.of(
                setup(world -> {
                    staircase(world, start, true);
                    entityId = launch(world, EntityType.MINECART, start, 0.4);
                    ticks = 0;
                }),
                Step.until("cart to reach the top", 110, () -> stairsTick(along -> along > STAIRS - 0.5)),
                Step.run(0, () -> onServer(server -> {
                    AbstractMinecart cart = serverCart(server, entityId);
                    check(along(cart) > STAIRS - 0.5, "cart did not reach the top: " + describe(cart) + floatState(cart));
                    check(cart.getY() > start.getY() + STAIRS - 0.6, "cart is too low at the top: " + describe(cart) + floatState(cart));
                }))
        );
    }

    @ClientTest("empty cart descends a long run of powered levita slopes")
    private static List<Step> emptyCartDescendsPoweredSlopes() {
        return List.of(
                setup(world -> {
                    staircase(world, start, true);
                    entityId = launch(world, EntityType.MINECART, start.relative(dir, STAIRS).above(STAIRS), -0.4);
                    ticks = 0;
                }),
                Step.until("cart to reach the bottom", 110, () -> stairsTick(along -> along < -1.0)),
                Step.run(0, () -> onServer(server -> {
                    AbstractMinecart cart = serverCart(server, entityId);
                    check(along(cart) < -1.0, "cart did not reach the bottom: " + describe(cart) + floatState(cart));
                }))
        );
    }

    @ClientTest("floating cart reconnects across a gap while climbing powered slopes a")
    private static List<Step> reconnectsAcrossGapOnAscendA() {
        return gapReconnectSteps(true, 'a');
    }

    @ClientTest("floating cart reconnects across a gap while climbing powered slopes b")
    private static List<Step> reconnectsAcrossGapOnAscendB() {
        return gapReconnectSteps(true, 'b');
    }

    @ClientTest("floating cart reconnects across a gap while descending powered slopes a")
    private static List<Step> reconnectsAcrossGapOnDescendA() {
        return gapReconnectSteps(false, 'a');
    }

    @ClientTest("floating cart reconnects across a gap while descending powered slopes b")
    private static List<Step> reconnectsAcrossGapOnDescendB() {
        return gapReconnectSteps(false, 'b');
    }

    private static List<Step> gapReconnectSteps(boolean ascend, char rotation) {
        check(rotation == 'a' || rotation == 'b', "setup: rotation letter must be a or b, got " + rotation);
        String tag = (ascend ? "ascend" : "descend") + "_" + rotation;
        List<Step> steps = new ArrayList<>();
        steps.add(setupAscendDescend(rotation, world -> {
            activeStairs = GAP_STAIRS;
            activeGapAt = GAP_AT;
            activeGapLen = GAP_LEN;
            shotResumeAlong = ascend ? 0.45 : -0.45;
            staircaseWithGap(world, start, GAP_AT, GAP_LEN);
            if (ascend) {
                entityId = launch(world, EntityType.MINECART, start, 0.4);
            } else {
                entityId = launch(world, EntityType.MINECART, start.relative(dir, GAP_STAIRS).above(GAP_STAIRS), -0.4);
            }
            ticks = 0;
            seen = 0;
        }));

        if (ascend) {
            steps.add(untilShot(tag + " on-rail before " + GAP_LEN + "-block gap", 100,
                    server -> {
                        AbstractMinecart cart = serverCart(server, entityId);
                        checkOnStairs(cart, "server");
                        return along(cart) > GAP_AT - 1.3 && MinecartFloating.isCartOnRail(cart);
                    }));
        } else {
            steps.add(untilShot(tag + " on-rail before " + GAP_LEN + "-block gap", 100,
                    server -> {
                        AbstractMinecart cart = serverCart(server, entityId);
                        checkOnStairs(cart, "server");
                        return along(cart) < GAP_AT + GAP_LEN + 1.3 && MinecartFloating.isCartOnRail(cart);
                    }));
        }
        steps.addAll(sideShotSteps("levita_" + tag + "_onrail_before_gap", true, ShotFrame.CLOSE));

        steps.add(untilShot(tag + " rail disconnect into gap", 140, server -> {
            AbstractMinecart cart = serverCart(server, entityId);
            checkGapPath(cart, "server");
            double a = along(cart);
            boolean inGap = ascend
                    ? (a > GAP_AT + 0.2 && a < GAP_AT + 2.2)
                    : (a < GAP_AT + GAP_LEN - 0.2 && a > GAP_AT + GAP_LEN - 2.2);
            return inGap && MinecartFloating.isFloating(cart)
                    && MinecartFloating.getIncline(cart) != 0;
        }));
        steps.addAll(sideShotSteps("levita_" + tag + "_rail_disconnect", true, ShotFrame.CLOSE));

        steps.add(untilShot(tag + " mid-air across gap", 180, server -> {
            AbstractMinecart cart = serverCart(server, entityId);
            checkGapPath(cart, "server");
            double a = along(cart);
            boolean mid = ascend
                    ? (a > GAP_AT + 1.6 && a < GAP_AT + GAP_LEN - 1.2)
                    : (a < GAP_AT + GAP_LEN - 1.6 && a > GAP_AT + 1.2);
            boolean airborne = MinecartFloating.isFloating(cart)
                    && MinecartFloating.findRailPos(cart) == null
                    && !MinecartFloating.isCartOnRail(cart)
                    && MinecartFloating.getIncline(cart) != 0;
            if (mid && airborne) {
                seen = 1;
                checkFloatRotation(cart, "server " + tag + " midair");
                return true;
            }
            return false;
        }));
        steps.addAll(sideShotSteps("levita_" + tag + "_midair_float", true, ShotFrame.CLOSE));

        steps.add(untilShot(tag + " rail reconnect", 180, server -> {
            AbstractMinecart cart = serverCart(server, entityId);
            checkGapPath(cart, "server");
            double a = along(cart);
            if (ascend) {
                return a >= GAP_AT + GAP_LEN - 0.1 && MinecartFloating.isCartOnRail(cart);
            }
            return a <= GAP_AT + 0.5;
        }));
        steps.addAll(sideShotSteps("levita_" + tag + "_rail_reconnect", true, ShotFrame.WIDE));

        if (ascend) {
            steps.add(untilShot(tag + " on-rail after gap", 180, server -> {
                AbstractMinecart cart = serverCart(server, entityId);
                checkGapPath(cart, "server");
                return along(cart) > GAP_STAIRS - 0.5 && MinecartFloating.isCartOnRail(cart);
            }));
        } else {
            steps.add(untilShot(tag + " after gap toward lower stairs", 200, server -> {
                AbstractMinecart cart = serverCart(server, entityId);
                checkGapPath(cart, "server");
                return along(cart) < GAP_AT - 0.5
                        && (MinecartFloating.isCartOnRail(cart) || cart.getY() < start.getY() + GAP_AT + 0.8);
            }));
        }
        steps.addAll(sideShotSteps("levita_" + tag + "_onrail_after_gap", true, ShotFrame.WIDE));
        steps.add(shotsFinished());

        steps.add(Step.run(0, () -> onServer(server -> {
            AbstractMinecart cart = serverCart(server, entityId);
            check(seen == 1, "setup: cart never crossed the " + tag + " mid-air gap");
            if (ascend) {
                check(along(cart) > GAP_STAIRS - 0.5, "cart did not finish the climb after the gap: "
                        + describe(cart) + floatState(cart));
                check(cart.getY() > start.getY() + GAP_STAIRS - 1.0,
                        "cart is too low after the ascend gap: " + describe(cart) + floatState(cart));
            } else {
                check(along(cart) < GAP_AT, "cart did not finish the descent after the gap: "
                        + describe(cart) + floatState(cart));
                check(cart.getY() < start.getY() + GAP_AT + 1.0,
                        "cart stayed too high after the descend gap: " + describe(cart) + floatState(cart));
            }
        })));
        return steps;
    }

    @ClientTest("descending float connects to parallel rails and rides them a")
    private static List<Step> zzDescendConnectParallelA() {
        return descendConnectSteps(false, 'a');
    }

    @ClientTest("descending float connects to parallel rails and rides them b")
    private static List<Step> zzDescendConnectParallelB() {
        return descendConnectSteps(false, 'b');
    }

    @ClientTest("descending float connects to perpendicular rails and rides them a")
    private static List<Step> zzDescendConnectPerpendicularA() {
        return descendConnectSteps(true, 'a');
    }

    @ClientTest("descending float connects to perpendicular rails and rides them b")
    private static List<Step> zzDescendConnectPerpendicularB() {
        return descendConnectSteps(true, 'b');
    }

    @ClientTest("descending float connects to perpendicular rails from other tick phases")
    private static List<Step> zzDescendConnectPerpendicularPhases() {
        List<Step> steps = new ArrayList<>();
        for (double phase : new double[]{0.1, 0.2, 0.3}) {
            steps.addAll(descendConnectSteps(true, 'a', phase));
        }
        return steps;
    }

    @ClientTest("ascending float connects to perpendicular rails from other tick phases")
    private static List<Step> zzAscendConnectPerpendicularPhases() {
        List<Step> steps = new ArrayList<>();
        for (double phase : new double[]{0.1, 0.2, 0.3}) {
            steps.addAll(ascendConnectSteps(true, 'a', phase));
        }
        return steps;
    }

    private static List<Step> descendConnectSteps(boolean perpendicular, char gapVariant) {
        return descendConnectSteps(perpendicular, gapVariant, 0.0);
    }

    private static List<Step> descendConnectSteps(boolean perpendicular, char gapVariant, double phase) {
        check(gapVariant == 'a' || gapVariant == 'b', "setup: gap letter must be a or b, got " + gapVariant);
        int gapAir = gapVariant == 'b' ? 4 : 1;
        String tag = (perpendicular ? "descend_perp" : "descend_parallel") + "_" + gapVariant
                + (phase > 0 ? "_p" + Math.round(phase * 10) : "");
        List<Step> steps = new ArrayList<>();
        steps.add(setup(world -> {
            dir = Direction.SOUTH;
            landDir = perpendicular ? Direction.EAST : Direction.SOUTH;
            connectGapAir = gapAir;
            activeStairs = CONNECT_STAIRS;
            activeGapAt = -1;
            activeGapLen = 0;
            shotResumeAlong = -0.45;
            resetRenderWatch();
            buildDescendConnectLanding(world, perpendicular, gapAir);
            int lip = gapAir;
            int topAlong = lip + CONNECT_STAIRS - 1;
            launchPhase = phase;
            entityId = launch(world, EntityType.MINECART,
                    start.relative(dir, topAlong).above(topAlong), -0.4);
            launchPhase = 0.0;
            ticks = 0;
            seen = 0;
        }, true));

        double lipAlong = gapAir;
        steps.add(untilShot(tag + " to leave the stairs into the landing gap", 180, server -> {
            AbstractMinecart cart = serverCart(server, entityId);
            if (along(cart) > -0.2) {
                check(Math.abs(side(cart)) < 0.45, tag + " cart drifted off the approach"
                        + describe(cart) + floatState(cart));
            }
            boolean inGap = along(cart) < lipAlong - 0.55 && along(cart) > -0.9;
            boolean airborne = MinecartFloating.isFloating(cart)
                    && MinecartFloating.findRailPos(cart) == null
                    && !MinecartFloating.isCartOnRail(cart);
            boolean crossed = along(cart) <= -0.9;
            return (inGap && airborne) || crossed;
        }));
        steps.addAll(sideShotSteps("levita_" + tag + "_approach_gap", true, ShotFrame.CLOSE));

        steps.add(untilShot(tag + " to seat on the landing rails", 240, server -> {
            AbstractMinecart cart = serverCart(server, entityId);
            BlockPos railPos = MinecartFloating.findRailPos(cart);
            if (railPos == null || railPos.getY() != start.getY()) {
                return false;
            }
            if (!MinecartFloating.isCartOnRail(cart) || MinecartFloating.isOffRail(cart)) {
                return false;
            }
            if (!MinecartFloating.isSettledOnRail(cart, railPos)) {
                return false;
            }
            RailShape shape = railShapeAt(world(server), railPos);
            RailShape expect = perpendicular ? RailShape.EAST_WEST : RailShape.NORTH_SOUTH;
            check(shape == expect, tag + " seated on " + shape + ", expected " + expect
                    + describe(cart) + floatState(cart));
            checkLandingFacing(cart, expect, tag);
            seen = 1;
            return true;
        }));
        steps.addAll(sideShotSteps("levita_" + tag + "_seated", true, ShotFrame.WIDE));
        steps.addAll(clientRenderWatchSteps(tag, perpendicular ? RailShape.EAST_WEST : RailShape.NORTH_SOUTH));

        steps.add(untilShot(tag + " to ride along the landing", 160, server -> {
            AbstractMinecart cart = serverCart(server, entityId);
            check(MinecartFloating.isCartOnRail(cart) && !MinecartFloating.isOffRail(cart),
                    tag + " left the landing rails while riding: " + describe(cart) + floatState(cart));
            check(Math.abs(cart.getY() - start.getY()) < 0.35,
                    tag + " is not on the landing height: " + describe(cart) + floatState(cart));
            return Math.abs(alongLand(cart)) > 2.5;
        }));
        steps.addAll(sideShotSteps("levita_" + tag + "_riding", true, ShotFrame.WIDE));

        steps.add(Step.run(0, () -> checkClientRenderFacing(tag,
                perpendicular ? RailShape.EAST_WEST : RailShape.NORTH_SOUTH)));
        steps.add(Step.run(0, () -> onServer(server -> {
            AbstractMinecart cart = serverCart(server, entityId);
            check(seen == 1, "setup: cart never seated on " + tag + " landing");
            check(MinecartFloating.isCartOnRail(cart) && !MinecartFloating.isOffRail(cart),
                    tag + " finished off the rails: " + describe(cart) + floatState(cart));
            check(Math.abs(alongLand(cart)) > 2.5, tag + " did not ride the landing (alongLand="
                    + fmt(alongLand(cart)) + ")" + describe(cart) + floatState(cart));
            RailShape expect = perpendicular ? RailShape.EAST_WEST : RailShape.NORTH_SOUTH;
            checkLandingFacing(cart, expect, tag);
            Vec3 motion = cart.getDeltaMovement().multiply(1, 0, 1);
            if (motion.lengthSqr() > 0.01) {
                Vec3 landAxis = Vec3.atLowerCornerOf(landDir.getNormal());
                check(Math.abs(motion.normalize().dot(landAxis)) > 0.85,
                        tag + " motion is not along the landing rails: motion="
                                + fmt(motion.x) + "/" + fmt(motion.z) + describe(cart) + floatState(cart));
            }
        })));
        steps.add(shotsFinished());
        return steps;
    }

    private static int renderWatchTicks;
    private static final List<String> renderViolations = new ArrayList<>();
    private static Vec3 renderWatchLast;
    private static int renderWatchTick;
    private static String renderPendingViolation;
    private static int renderWatchSampleTick;

    private static float[] clientRenderPose(AbstractMinecart cart) {
        if (MinecartFloating.shouldUseFloatRenderPose(cart)) {
            return new float[]{MinecartFloating.getFloatRenderYaw(cart), MinecartFloating.getFloatRenderPitch(cart)};
        }
        float yaw = cart.getYRot();
        float pitch = cart.getXRot();
        Vec3 at = cart.getPos(cart.getX(), cart.getY(), cart.getZ());
        if (at != null) {
            Vec3 a = cart.getPosOffs(cart.getX(), cart.getY(), cart.getZ(), 0.3F);
            Vec3 b = cart.getPosOffs(cart.getX(), cart.getY(), cart.getZ(), -0.3F);
            if (a == null) {
                a = at;
            }
            if (b == null) {
                b = at;
            }
            Vec3 d = b.add(-a.x, -a.y, -a.z);
            if (d.length() != 0.0) {
                d = d.normalize();
                yaw = (float) (Math.atan2(d.z, d.x) * 180.0 / Math.PI);
                pitch = (float) (Math.atan(d.y) * 73.0);
            }
        }
        return new float[]{yaw, pitch};
    }

    private static void resetRenderWatch() {
        renderViolations.clear();
        renderWatchLast = null;
        renderWatchTick = 0;
        renderPendingViolation = null;
    }

    private static boolean watchClientRender() {
        sampleClientRender(entityId);
        return true;
    }

    private static void sampleClientRender(int id) {
        renderWatchTick++;
        if (!(client().level.getEntity(id) instanceof AbstractMinecart cart)) {
            return;
        }
        Vec3 now = cart.position();
        Vec3 last = renderWatchLast;
        renderWatchLast = now;
        boolean consecutive = renderWatchSampleTick == clientTicks - 1;
        renderWatchSampleTick = clientTicks;
        if (last == null || !consecutive) {
            return;
        }
        Vec3 move = now.subtract(last);
        double horizontal = Math.sqrt(move.x * move.x + move.z * move.z);
        if (horizontal < 0.08 || !MinecartFloating.isFloating(cart)) {
            return;
        }
        float travelYaw = (float) (Math.atan2(move.z, move.x) * 180.0 / Math.PI);
        float offAxis = Math.abs(Mth.wrapDegrees(travelYaw - Math.round(travelYaw / 90.0F) * 90.0F));
        float[] pose = clientRenderPose(cart);
        double rad = Math.toRadians(pose[0]);
        double facing = Math.cos(rad) * move.x / horizontal + Math.sin(rad) * move.z / horizontal;
        boolean steep = Math.abs(move.y) > 0.15 && Math.abs(move.y) / horizontal > 0.6;
        String why = null;
        if (Math.abs(facing) < 0.82 && offAxis < 20.0F) {
            why = "yaw " + fmt(pose[0]) + " is " + fmt(Math.toDegrees(Math.acos(Math.min(1.0, Math.abs(facing)))))
                    + "deg off the travel direction";
        } else if (steep && Math.abs(pose[1]) < 10.0F) {
            why = "pitch " + fmt(pose[1]) + " while dy=" + fmt(move.y);
        } else if (steep && Math.abs(pose[1]) >= 10.0F
                && Math.signum(pose[1]) != Math.signum(move.y)
                && Math.signum(pose[1]) != Math.signum(move.y) * Math.signum(facing)) {
            why = "pitch " + fmt(pose[1]) + " has the wrong sign for dy=" + fmt(move.y);
        } else if (Math.abs(move.y) < 0.02 && Math.abs(pose[1]) > 10.0F) {
            why = "pitch " + fmt(pose[1]) + " while flat";
        }
        if (why != null) {
            String entry = "t" + renderWatchTick + " " + why + " (move=" + fmt(move.x) + "/" + fmt(move.y) + "/"
                    + fmt(move.z) + floatState(cart) + ")";
            if (renderPendingViolation != null) {
                if (renderViolations.isEmpty()) {
                    renderViolations.add(renderPendingViolation);
                }
                renderViolations.add(entry);
            }
            renderPendingViolation = entry;
        } else {
            renderPendingViolation = null;
        }
    }

    private static List<Step> clientRenderWatchSteps(String tag, RailShape expect) {
        List<Step> out = new ArrayList<>();
        int[] flat = {0};
        int[] waited = {0};
        out.add(Step.until(tag + " client cart to ride the landing", 80, () -> {
            watchClientRender();
            AbstractMinecart cart = clientCart(entityId);
            Vec3 motion = cart.getDeltaMovement();
            boolean level = Math.abs(cart.getY() - cart.yOld) < 0.02 && motion.horizontalDistanceSqr() > 0.01;
            flat[0] = level ? flat[0] + 1 : 0;
            waited[0]++;
            check(flat[0] >= 3 || waited[0] < 70, tag + " client cart never rode level: dy=" + fmt(cart.getY() - cart.yOld) + describe(cart) + floatState(cart));
            return flat[0] >= 3;
        }));
        out.add(Step.run(0, () -> renderWatchTicks = 0));
        out.add(Step.run(0, () -> check(renderViolations.isEmpty(), tag + " client render did not follow the rails ("
                + renderViolations.size() + " samples): "
                + String.join(" | ", renderViolations.subList(0, Math.min(8, renderViolations.size()))))));
        out.add(Step.until(tag + " client render to follow the landing rails", 80, () -> {
            checkClientRenderFacing(tag, expect);
            return ++renderWatchTicks >= 8;
        }));
        return out;
    }

    private static void checkClientRenderFacing(String tag, RailShape expect) {
        check(renderViolations.isEmpty(), tag + " client render did not follow the rails ("
                + renderViolations.size() + " samples): "
                + String.join(" | ", renderViolations.subList(0, Math.min(8, renderViolations.size()))));
        AbstractMinecart cart = clientCart(entityId);
        float yaw;
        float pitch;
        String path;
        if (MinecartFloating.shouldUseFloatRenderPose(cart)) {
            yaw = MinecartFloating.getFloatRenderYaw(cart);
            pitch = MinecartFloating.getFloatRenderPitch(cart);
            path = "floatPose";
        } else {
            yaw = Mth.rotLerp(0.5F, cart.yRotO, cart.getYRot());
            pitch = Mth.lerp(0.5F, cart.xRotO, cart.getXRot());
            path = "entityPose";
        }
        yaw = Mth.wrapDegrees(yaw);
        boolean yawOk = expect == RailShape.EAST_WEST
                ? (Math.abs(yaw) <= 8.0F || Math.abs(Math.abs(yaw) - 180.0F) <= 8.0F)
                : Math.abs(Math.abs(yaw) - 90.0F) <= 8.0F;
        check(yawOk, tag + " client renders yaw=" + fmt(yaw) + " via " + path + ", not along " + expect
                + describe(cart) + floatState(cart));
        check(Math.abs(pitch) < 3.0F, tag + " client renders pitch=" + fmt(pitch) + " via " + path
                + " on a flat landing" + describe(cart) + floatState(cart));
    }

    private static void buildDescendConnectLanding(ServerLevel world, boolean perpendicular, int gapAir) {
        check(gapAir >= 1 && gapAir <= 5, "setup: connect gapAir must be 1..5, got " + gapAir);
        int lip = gapAir;
        int topAlong = lip + CONNECT_STAIRS - 1;
        for (int i = lip; i <= topAlong; i++) {
            placePoweredLevitaRail(world, start.relative(dir, i).above(i));
        }
        world.setBlockAndUpdate(start.below(), Blocks.STONE.defaultBlockState());

        BlockPos top = start.relative(dir, topAlong + 1).above(topAlong + 1);
        fill(world, top.below(), top.relative(dir).below(), Blocks.STONE.defaultBlockState());
        world.setBlockAndUpdate(top, Blocks.RAIL.defaultBlockState());

        for (int i = lip; i <= topAlong; i++) {
            slope(world, start.relative(dir, i).above(i));
        }

        for (int g = 0; g < gapAir; g++) {
            world.setBlockAndUpdate(start.relative(dir, g), Blocks.AIR.defaultBlockState());
            world.setBlockAndUpdate(start.relative(dir, g).below(), Blocks.AIR.defaultBlockState());
        }

        landCenter = start.relative(dir, -1);
        placeConnectLanding(world, perpendicular, landCenter, dir.getOpposite());
    }

    @ClientTest("ascending float connects to parallel rails and rides them a")
    private static List<Step> zzAscendConnectParallelA() {
        return ascendConnectSteps(false, 'a');
    }

    @ClientTest("ascending float connects to parallel rails and rides them b")
    private static List<Step> zzAscendConnectParallelB() {
        return ascendConnectSteps(false, 'b');
    }

    @ClientTest("ascending float connects to perpendicular rails and rides them a")
    private static List<Step> zzAscendConnectPerpendicularA() {
        return ascendConnectSteps(true, 'a');
    }

    @ClientTest("ascending float connects to perpendicular rails and rides them b")
    private static List<Step> zzAscendConnectPerpendicularB() {
        return ascendConnectSteps(true, 'b');
    }

    private static List<Step> ascendConnectSteps(boolean perpendicular, char gapVariant) {
        return ascendConnectSteps(perpendicular, gapVariant, 0.0);
    }

    private static List<Step> ascendConnectSteps(boolean perpendicular, char gapVariant, double phase) {
        check(gapVariant == 'a' || gapVariant == 'b', "setup: gap letter must be a or b, got " + gapVariant);
        int gapAir = gapVariant == 'b' ? 4 : 1;
        String tag = (perpendicular ? "ascend_perp" : "ascend_parallel") + "_" + gapVariant
                + (phase > 0 ? "_p" + Math.round(phase * 10) : "");
        List<Step> steps = new ArrayList<>();
        steps.add(setup(world -> {
            dir = Direction.SOUTH;
            landDir = perpendicular ? Direction.EAST : Direction.SOUTH;
            connectGapAir = gapAir;
            activeStairs = CONNECT_STAIRS;
            activeGapAt = -1;
            activeGapLen = 0;
            shotResumeAlong = CONNECT_STAIRS + gapAir + 2.5;
            resetRenderWatch();
            buildAscendConnectLanding(world, perpendicular, gapAir);
            launchPhase = phase;
            entityId = launch(world, EntityType.MINECART, start, 0.4);
            launchPhase = 0.0;
            ticks = 0;
            seen = 0;
        }, true));

        int landAlong = CONNECT_STAIRS + gapAir;
        int landY = landAlong;
        steps.add(untilShot(tag + " to leave the stairs into the landing gap", 180, server -> {
            AbstractMinecart cart = serverCart(server, entityId);
            if (along(cart) < landAlong + 0.5) {
                check(Math.abs(side(cart)) < 0.45, tag + " cart drifted off the approach"
                        + describe(cart) + floatState(cart));
            }
            boolean inGap = along(cart) > CONNECT_STAIRS - 0.45 && along(cart) < landAlong - 0.35;
            boolean airborne = MinecartFloating.isFloating(cart)
                    && MinecartFloating.findRailPos(cart) == null
                    && !MinecartFloating.isCartOnRail(cart);
            boolean crossed = along(cart) >= landAlong - 0.35;
            return (inGap && airborne) || crossed;
        }));
        steps.addAll(sideShotSteps("levita_" + tag + "_approach_gap", true, ShotFrame.CLOSE));

        steps.add(untilShot(tag + " to seat on the landing rails", 240, server -> {
            AbstractMinecart cart = serverCart(server, entityId);
            BlockPos railPos = MinecartFloating.findRailPos(cart);
            if (railPos == null || railPos.getY() != start.getY() + landY) {
                return false;
            }
            if (!MinecartFloating.isCartOnRail(cart) || MinecartFloating.isOffRail(cart)) {
                return false;
            }
            if (!MinecartFloating.isSettledOnRail(cart, railPos)) {
                return false;
            }
            check(cart.getY() >= railPos.getY() - 0.01,
                    tag + " seated under the landing rails: " + describe(cart) + floatState(cart));
            RailShape shape = railShapeAt(world(server), railPos);
            RailShape expect = perpendicular ? RailShape.EAST_WEST : RailShape.NORTH_SOUTH;
            check(shape == expect, tag + " seated on " + shape + ", expected " + expect
                    + describe(cart) + floatState(cart));
            checkLandingFacing(cart, expect, tag);
            seen = 1;
            return true;
        }));
        steps.addAll(sideShotSteps("levita_" + tag + "_seated", true, ShotFrame.WIDE));
        steps.addAll(clientRenderWatchSteps(tag, perpendicular ? RailShape.EAST_WEST : RailShape.NORTH_SOUTH));

        steps.add(untilShot(tag + " to ride along the landing", 160, server -> {
            AbstractMinecart cart = serverCart(server, entityId);
            check(MinecartFloating.isCartOnRail(cart) && !MinecartFloating.isOffRail(cart),
                    tag + " left the landing rails while riding: " + describe(cart) + floatState(cart));
            check(Math.abs(cart.getY() - (start.getY() + landY)) < 0.35,
                    tag + " is not on the landing height: " + describe(cart) + floatState(cart));
            return Math.abs(alongLand(cart)) > 2.5;
        }));
        steps.addAll(sideShotSteps("levita_" + tag + "_riding", true, ShotFrame.WIDE));

        steps.add(Step.run(0, () -> checkClientRenderFacing(tag,
                perpendicular ? RailShape.EAST_WEST : RailShape.NORTH_SOUTH)));
        steps.add(Step.run(0, () -> onServer(server -> {
            AbstractMinecart cart = serverCart(server, entityId);
            check(seen == 1, "setup: cart never seated on " + tag + " landing");
            check(MinecartFloating.isCartOnRail(cart) && !MinecartFloating.isOffRail(cart),
                    tag + " finished off the rails: " + describe(cart) + floatState(cart));
            check(Math.abs(alongLand(cart)) > 2.5, tag + " did not ride the landing (alongLand="
                    + fmt(alongLand(cart)) + ")" + describe(cart) + floatState(cart));
            RailShape expect = perpendicular ? RailShape.EAST_WEST : RailShape.NORTH_SOUTH;
            checkLandingFacing(cart, expect, tag);
            Vec3 motion = cart.getDeltaMovement().multiply(1, 0, 1);
            if (motion.lengthSqr() > 0.01) {
                Vec3 landAxis = Vec3.atLowerCornerOf(landDir.getNormal());
                check(Math.abs(motion.normalize().dot(landAxis)) > 0.85,
                        tag + " motion is not along the landing rails: motion="
                                + fmt(motion.x) + "/" + fmt(motion.z) + describe(cart) + floatState(cart));
            }
        })));
        steps.add(shotsFinished());
        return steps;
    }

    private static void buildAscendConnectLanding(ServerLevel world, boolean perpendicular, int gapAir) {
        check(gapAir >= 1 && gapAir <= 5, "setup: connect gapAir must be 1..5, got " + gapAir);
        int landAlong = CONNECT_STAIRS + gapAir;
        int landY = landAlong;
        for (int i = 0; i < CONNECT_STAIRS; i++) {
            placePoweredLevitaRail(world, start.relative(dir, i).above(i));
        }
        world.setBlockAndUpdate(start.relative(dir, -1).below(), Blocks.STONE.defaultBlockState());
        world.setBlockAndUpdate(start.relative(dir, -1), Blocks.RAIL.defaultBlockState());

        for (int g = 0; g < gapAir; g++) {
            BlockPos lip = start.relative(dir, CONNECT_STAIRS + g).above(CONNECT_STAIRS + g);
            world.setBlockAndUpdate(lip.below(), Blocks.STONE.defaultBlockState());
            world.setBlockAndUpdate(lip, Blocks.RAIL.defaultBlockState());
        }

        for (int i = 0; i < CONNECT_STAIRS; i++) {
            slope(world, start.relative(dir, i).above(i));
        }

        for (int g = 0; g < gapAir; g++) {
            BlockPos gap = start.relative(dir, CONNECT_STAIRS + g).above(CONNECT_STAIRS + g);
            world.setBlockAndUpdate(gap, Blocks.AIR.defaultBlockState());
            world.setBlockAndUpdate(gap.below(), Blocks.AIR.defaultBlockState());
        }
        BlockPos exitLip = start.relative(dir, CONNECT_STAIRS - 1).above(CONNECT_STAIRS - 1);
        if (world.getBlockState(exitLip).getBlock() instanceof BaseRailBlock) {
            slope(world, exitLip);
        }

        landCenter = start.relative(dir, landAlong).above(landY);
        placeConnectLanding(world, perpendicular, landCenter, dir);
    }

    private static void placeConnectLanding(ServerLevel world, boolean perpendicular, BlockPos center, Direction along) {
        if (perpendicular) {
            for (int i = -LANDING_HALF; i <= LANDING_HALF; i++) {
                BlockPos at = center.relative(Direction.EAST, i);
                world.setBlockAndUpdate(at.below(), Blocks.STONE.defaultBlockState());
                setFlatRail(world, at, RailShape.EAST_WEST);
            }
        } else {
            for (int i = 0; i < LANDING_HALF * 2; i++) {
                BlockPos at = center.relative(along, i);
                world.setBlockAndUpdate(at.below(), Blocks.STONE.defaultBlockState());
                setFlatRail(world, at, RailShape.NORTH_SOUTH);
            }
        }
    }

    private static void checkLandingFacing(AbstractMinecart cart, RailShape expect, String tag) {
        float yaw = Mth.wrapDegrees(cart.getYRot());
        boolean yawOk = expect == RailShape.EAST_WEST
                ? (Math.abs(yaw) <= 5.0F || Math.abs(Math.abs(yaw) - 180.0F) <= 5.0F)
                : Math.abs(Math.abs(yaw) - 90.0F) <= 5.0F;
        check(yawOk, tag + " yaw=" + fmt(yaw) + " is not on " + expect + " after connect"
                + describe(cart) + floatState(cart));
        Vec3 motion = cart.getDeltaMovement().multiply(1, 0, 1);
        if (motion.lengthSqr() > 0.04) {
            boolean motionOk = expect == RailShape.EAST_WEST
                    ? Math.abs(motion.x) >= Math.abs(motion.z) * 0.85
                    : Math.abs(motion.z) >= Math.abs(motion.x) * 0.85;
            check(motionOk, tag + " motion still on approach axis after connect: "
                    + fmt(motion.x) + "/" + fmt(motion.z) + describe(cart) + floatState(cart));
        }
    }

    private static void setFlatRail(ServerLevel world, BlockPos at, RailShape shape) {
        world.setBlockAndUpdate(at, Blocks.RAIL.defaultBlockState());
        BlockState state = world.getBlockState(at);
        Property<RailShape> property = ((BaseRailBlock) state.getBlock()).getShapeProperty();
        world.setBlock(at, state.setValue(property, shape), Block.UPDATE_ALL);
        BlockState now = world.getBlockState(at);
        check(now.getValue(property) == shape, "setup: flat rail at " + at.toShortString() + " is " + now);
    }

    private static RailShape railShapeAt(ServerLevel world, BlockPos railPos) {
        BlockState state = world.getBlockState(railPos);
        check(state.getBlock() instanceof BaseRailBlock, "expected rail at " + railPos + ", got " + state);
        return state.getValue(((BaseRailBlock) state.getBlock()).getShapeProperty());
    }

    private static double alongLand(Entity entity) {
        return entity.position().subtract(Vec3.atBottomCenterOf(landCenter))
                .dot(Vec3.atLowerCornerOf(landDir.getNormal()));
    }

    @ClientTest("floating cart walks a solid ascending staircase without far rails")
    private static List<Step> walksSolidAscendingStaircase() {
        return walkingStaircaseSteps(true);
    }

    @ClientTest("floating cart walks a solid descending staircase without far rails")
    private static List<Step> walksSolidDescendingStaircase() {
        return walkingStaircaseSteps(false);
    }

    private static List<Step> walkingStaircaseSteps(boolean ascend) {
        String tag = ascend ? "walk_ascend" : "walk_descend";
        List<Step> steps = new ArrayList<>();
        steps.add(setupAscendDescend('a', world -> {
            activeStairs = WALK_STAIRS;
            activeGapAt = WALK_RAIL_RUN;
            activeGapLen = WALK_STAIRS - WALK_RAIL_RUN;
            shotResumeAlong = ascend ? 0.45 : -0.45;
            buildWalkingStaircase(world, ascend);
            if (ascend) {
                entityId = launch(world, EntityType.MINECART, start, 0.4);
            } else {
                entityId = launch(world, EntityType.MINECART,
                        start.relative(dir, WALK_STAIRS).above(WALK_STAIRS), -0.4);
            }
            ticks = 0;
            seen = 0;
        }));

        int expectIncline = ascend ? 1 : -1;
        if (ascend) {
            steps.add(Step.until(tag + " onto the solid ladder", 120, () -> fromServer(server -> {
                AbstractMinecart cart = serverCart(server, entityId);
                checkOnStairs(cart, "server");
                boolean onSolid = along(cart) > WALK_RAIL_RUN + 0.2
                        && walkingSolidDiagonal(cart, expectIncline);
                if (onSolid) {
                    seen = 1;
                    checkFloatRotation(cart, "server " + tag);
                }
                return onSolid;
            })));
        } else {
            steps.add(Step.until(tag + " onto the solid ladder", 120, () -> fromServer(server -> {
                AbstractMinecart cart = serverCart(server, entityId);
                boolean onSolid = along(cart) < WALK_STAIRS - WALK_RAIL_RUN - 0.2
                        && along(cart) > 0.5
                        && walkingSolidDiagonal(cart, expectIncline);
                if (onSolid) {
                    seen = 1;
                    checkFloatRotation(cart, "server " + tag);
                }
                return onSolid;
            })));
        }
        steps.addAll(sideShotSteps("levita_" + tag + "_mid_ladder", true, ShotFrame.CLOSE));

        if (ascend) {
            steps.add(Step.until(tag + " past mid solid ladder", 160, () -> fromServer(server -> {
                AbstractMinecart cart = serverCart(server, entityId);
                check(Math.abs(side(cart)) < 0.4, tag + " drifted off ladder" + describe(cart) + floatState(cart));
                check(walkingSolidDiagonal(cart, expectIncline), tag + " lost slope flight on the solid ladder"
                        + describe(cart) + floatState(cart));
                checkFloatRotation(cart, "server " + tag);
                return along(cart) > WALK_STAIRS * 0.55;
            })));
        } else {
            steps.add(Step.until(tag + " past mid solid ladder", 160, () -> fromServer(server -> {
                AbstractMinecart cart = serverCart(server, entityId);
                check(Math.abs(side(cart)) < 0.4, tag + " drifted off ladder" + describe(cart) + floatState(cart));
                check(walkingSolidDiagonal(cart, expectIncline), tag + " lost slope flight on the solid ladder"
                        + describe(cart) + floatState(cart));
                checkFloatRotation(cart, "server " + tag);
                return along(cart) < WALK_STAIRS * 0.35;
            })));
        }
        steps.addAll(sideShotSteps("levita_" + tag + "_far_ladder", true, ShotFrame.WIDE));
        steps.add(shotsFinished());

        steps.add(Step.run(0, () -> onServer(server -> {
            AbstractMinecart cart = serverCart(server, entityId);
            check(seen == 1, "setup: cart never entered the solid " + tag + " ladder");
            check(walkingSolidDiagonal(cart, expectIncline),
                    tag + " finished without slope flight on the solid ladder (flat coast is a false pass): "
                            + describe(cart) + floatState(cart));
            if (ascend) {
                check(along(cart) > WALK_STAIRS * 0.55, tag + " did not climb the solid ladder: "
                        + describe(cart) + floatState(cart));
            } else {
                check(along(cart) < WALK_STAIRS * 0.45, tag + " did not descend the solid ladder: "
                        + describe(cart) + floatState(cart));
            }
        })));
        return steps;
    }

    private static boolean walkingSolidDiagonal(AbstractMinecart cart, int expectIncline) {
        if (!MinecartFloating.isFloating(cart) || MinecartFloating.findRailPos(cart) != null) {
            return false;
        }
        if (MinecartFloating.getIncline(cart) != expectIncline) {
            return false;
        }
        double a = along(cart);
        double rise = cart.getY() - start.getY();
        if (Math.abs(rise - a) >= 0.85) {
            return false;
        }
        Vec3 motion = cart.getDeltaMovement();
        double horiz = motion.horizontalDistance();
        if (horiz > 0.08) {
            double expectDy = expectIncline * horiz;
            if (Math.abs(motion.y - expectDy) > 0.22) {
                return false;
            }
        }
        return true;
    }

    private static void buildWalkingStaircase(ServerLevel world, boolean ascend) {
        int railFrom = ascend ? 0 : WALK_STAIRS - WALK_RAIL_RUN + 1;
        int railTo = ascend ? WALK_RAIL_RUN - 1 : WALK_STAIRS;
        for (int i = 0; i <= WALK_STAIRS; i++) {
            BlockPos pier = start.relative(dir, i).above(i).below();
            world.setBlockAndUpdate(pier, Blocks.STONE.defaultBlockState());
            world.setBlockAndUpdate(pier.above(), Blocks.AIR.defaultBlockState());
        }
        for (int i = railFrom; i <= railTo; i++) {
            placePoweredLevitaRail(world, start.relative(dir, i).above(i));
        }
        if (ascend) {
            track(world, start.relative(dir, -2), "rr");
        } else {
            BlockPos top = start.relative(dir, WALK_STAIRS + 1).above(WALK_STAIRS + 1);
            fill(world, top.below(), top.relative(dir).below(), Blocks.STONE.defaultBlockState());
            track(world, top, "rr");
        }
        for (int i = railFrom; i <= railTo; i++) {
            slope(world, start.relative(dir, i).above(i));
        }
    }

    @ClientTest("cart climbs powered levita slopes as high as plain rail slopes")
    private static List<Step> levitaSlopesClimbLikePlainSlopes() {
        return List.of(
                setup(world -> {
                    staircase(world, start, true);
                    staircase(world, start.relative(dir.getClockWise(), 3), false);
                    entityId = launch(world, EntityType.MINECART, start, 0.4);
                    secondId = launch(world, EntityType.MINECART, start.relative(dir.getClockWise(), 3), 0.4);
                    levitaTop = plainTop = start.getY();
                    ticks = 0;
                }),
                Step.until("60 ticks of climbing", 70, () -> fromServer(server -> {
                    levitaTop = Math.max(levitaTop, serverCart(server, entityId).getY());
                    plainTop = Math.max(plainTop, serverCart(server, secondId).getY());
                    return ++ticks >= 60;
                })),
                Step.run(0, () -> check(levitaTop >= plainTop - 0.5, "levita cart rose " + fmt(levitaTop - start.getY())
                        + " blocks, plain cart rose " + fmt(plainTop - start.getY())))
        );
    }

    @ClientTest("player stays in a floating cart")
    private static List<Step> riderStaysInFloatingCart() {
        return List.of(
                setup(world -> {
                    track(world, start, "rLrrr");
                    entityId = launch(world, EntityType.MINECART, start, 0.4);
                    check(world.players().getFirst().startRiding(world.getEntity(entityId), true), "setup: startRiding returned false");
                }),
                Step.until("ridden cart to float on the client", 60, () ->
                        clientFloating(entityId) && client().player.getVehicle() == clientCart(entityId)),
                Step.until("float to end", CHARGE + 20, () -> fromServer(server -> {
                    AbstractMinecart cart = serverCart(server, entityId);
                    check(player(server).getVehicle() == cart, "player left the cart at " + describe(cart) + floatState(cart));
                    return !MinecartFloating.isFloating(cart);
                })),
                Step.run(0, () -> check(client().player.getVehicle() == clientCart(entityId), "client player is not in the cart after the float"))
        );
    }

    @ClientTest("chest and furnace minecarts float too")
    private static List<Step> otherMinecartsFloat() {
        return List.of(
                setup(world -> {
                    BlockPos other = start.relative(dir.getClockWise(), 3);
                    track(world, start, "rLrrr");
                    track(world, other, "rLrrr");
                    entityId = launch(world, EntityType.CHEST_MINECART, start, 0.4);
                    secondId = launch(world, EntityType.FURNACE_MINECART, other, 0.2);
                    MinecartFurnace furnace = (MinecartFurnace) world.getEntity(secondId);
                    setField(furnace, "fuel", 1000);
                    furnace.xPush = dir.getStepX();
                    furnace.zPush = dir.getStepZ();
                }),
                Step.until("chest minecart to float", 60, () -> serverFloating(entityId)),
                Step.until("furnace minecart to float", 60, () -> serverFloating(secondId))
        );
    }

    @ClientTest("player rides palace side deco minecart loop back to the button")
    private static List<Step> zzPalaceSideDecoMinecartLoop() {
        ResourceLocation structureId = ParadiseLost.locate("palace/extras/side_3_deco_4");
        final double cartLocalX = 2.5;
        final double cartLocalY = 0.0625;
        final double cartLocalZ = 2.5;
        final BlockPos buttonLocal = new BlockPos(1, 1, 2);
        return List.of(
                Step.run(0, () -> {
                    client().options.pauseOnLostFocus = false;
                    client().options.keyShift.setDown(false);
                    client().options.keyUse.setDown(false);
                    if (client().screen instanceof net.minecraft.client.gui.screens.PauseScreen) {
                        client().setScreen(null);
                    }
                    onServer(server -> {
                        ServerPlayer player = player(server);
                        ServerLevel level = world(server);
                        server.tickRateManager().setFrozen(false);
                        player.setGameMode(GameType.CREATIVE);
                        dir = Direction.SOUTH;
                        teleport(player, level, player.getX(), player.getY(), player.getZ(), dir.toYRot(), 0.0F);

                        ItemStack structureBlock = palaceSideDecoStructureBlockItem(structureId);
                        player.getInventory().clearContent();
                        check(player.getInventory().add(structureBlock.copy()),
                                "setup: player did not receive the structure block");

                        BlockPos structureBlockAt = player.blockPosition().relative(dir, 1).below();
                        level.setBlock(structureBlockAt,
                                Blocks.STRUCTURE_BLOCK.defaultBlockState().setValue(StructureBlock.MODE, StructureMode.LOAD),
                                Block.UPDATE_ALL);
                        check(level.getBlockEntity(structureBlockAt) instanceof StructureBlockEntity,
                                "setup: no structure block entity at " + structureBlockAt.toShortString());
                        StructureBlockEntity sbe = (StructureBlockEntity) level.getBlockEntity(structureBlockAt);
                        configurePalaceStructureBlock(sbe, structureId);
                        activateStructureBlockLoad(sbe, level);

                        start = structureBlockAt.offset(0, 1, 0);
                        restampPalacePaletteBlockstates(level, structureId, start);
                        pos = start.offset(buttonLocal);
                        assertPalaceStructureBlockstates(level, start, pos);
                        Vec3 cartAt = Vec3.atLowerCornerOf(start).add(cartLocalX, cartLocalY, cartLocalZ);
                        AbstractMinecart cart = level.getEntitiesOfClass(AbstractMinecart.class,
                                        new AABB(cartAt, cartAt).inflate(3.0), e -> true).stream()
                                .findFirst()
                                .orElseGet(() -> level.getEntitiesOfClass(AbstractMinecart.class,
                                                new AABB(start).inflate(24), e -> true).stream()
                                        .findFirst()
                                        .orElse(null));
                        check(cart != null, "setup: structure did not spawn its minecart near " + cartAt
                                + " (origin " + start.toShortString() + ") — entities were not placed");
                        entityId = cart.getId();
                        startY = cart.getY();

                        player.setGameMode(GameType.SURVIVAL);
                        check(player.startRiding(cart, true), "setup: startRiding returned false");
                        ticks = 0;
                        seen = 0;
                        renderViolations.clear();
                        renderWatchLast = null;
                        renderWatchTick = 0;
                    });
                }),
                Step.run(5, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    AbstractMinecart cart = serverCart(server, entityId);
                    if (player.getVehicle() != cart) {
                        check(player.startRiding(cart, true), "setup: could not remount palace minecart before button");
                    }
                    BlockState state = world(server).getBlockState(pos);
                    Direction hitFace = Direction.UP;
                    if (state.hasProperty(net.minecraft.world.level.block.state.properties.BlockStateProperties.ATTACH_FACE)) {
                        var face = state.getValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.ATTACH_FACE);
                        if (face == net.minecraft.world.level.block.state.properties.AttachFace.WALL
                                && state.hasProperty(net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING)) {
                            hitFace = state.getValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING);
                        } else if (face == net.minecraft.world.level.block.state.properties.AttachFace.CEILING) {
                            hitFace = Direction.DOWN;
                        }
                    }
                    BlockHitResult hit = new BlockHitResult(Vec3.atCenterOf(pos), hitFace, pos, false);
                    state.useWithoutItem(world(server), player, hit);
                    check(world(server).getBlockState(pos).getValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED),
                            "setup: floestone button did not power after press");
                })),
                Step.until("ride the palace loop back to the start", 20 * 60, () -> {
                    if (client().screen instanceof net.minecraft.client.gui.screens.PauseScreen) {
                        client().setScreen(null);
                    }
                    sampleClientRender(entityId);
                    return fromServer(server -> {
                        ServerPlayer player = player(server);
                        AbstractMinecart cart = serverCart(server, entityId);
                        if (player.getVehicle() != cart && player.distanceTo(cart) < 4.0) {
                            player.startRiding(cart, true);
                        }
                        check(player.getVehicle() == cart || player.distanceTo(cart) < 4.0,
                                "player left the palace minecart" + describe(cart) + floatState(cart));
                        if (MinecartFloating.isFloating(cart) && MinecartFloating.getIncline(cart) != 0) {
                            BlockPos railPos = MinecartFloating.findRailPos(cart);
                            boolean airborne = MinecartFloating.isOffRail(cart)
                                    || railPos == null
                                    || !MinecartFloating.isSettledOnRail(cart, railPos);
                            if (airborne) {
                                MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
                                float pitch = pose.paradiseLost$hasRailRenderPose()
                                        ? pose.paradiseLost$getRailRenderPitch()
                                        : MinecartFloating.getSyncedFloatPitch(cart);
                                if (Math.abs(pitch) <= 1.0F) {
                                    pitch = MinecartFloating.getSyncedFloatPitch(cart);
                                }
                                check(Math.abs(Math.abs(pitch) - 45) < 8,
                                        "palace slope gap pitch is " + fmt(pitch)
                                                + ", expected about +-45" + describe(cart) + floatState(cart));
                                check(Math.abs(Math.abs(cart.getXRot()) - 45) < 8
                                                || Math.abs(Math.abs(pitch) - 45) < 8,
                                        "palace slope gap entity/pose pitch flat: xRot="
                                                + fmt(cart.getXRot()) + " pose=" + fmt(pitch)
                                                + describe(cart) + floatState(cart));
                            }
                        }
                        Vec3 startAt = Vec3.atLowerCornerOf(start).add(cartLocalX, cartLocalY, cartLocalZ);
                        double dist = player.position().distanceTo(startAt);
                        if (dist > 3.0) {
                            seen = 1;
                        }
                        ticks++;
                        return seen == 1 && dist < 1.75;
                    });
                }),
                Step.run(0, () -> onServer(server -> {
                    check(seen == 1, "setup: cart never left the palace button start");
                    check(renderViolations.isEmpty(), "client render did not follow the rails (" + renderViolations.size()
                            + " samples): " + String.join(" | ", renderViolations.subList(0, Math.min(10, renderViolations.size()))));
                    AbstractMinecart cart = serverCart(server, entityId);
                    Vec3 startAt = Vec3.atLowerCornerOf(start).add(cartLocalX, cartLocalY, cartLocalZ);
                    check(player(server).position().distanceTo(startAt) < 1.75,
                            "player did not return to the palace minecart start: dist="
                                    + fmt(player(server).position().distanceTo(startAt))
                                    + describe(cart) + floatState(cart));
                }))
        );
    }

    private static ItemStack palaceSideDecoStructureBlockItem(ResourceLocation structureId) {
        CompoundTag tag = new CompoundTag();
        tag.putString("id", "minecraft:structure_block");
        tag.putString("mode", "LOAD");
        tag.putString("name", structureId.toString());
        tag.putInt("posX", 0);
        tag.putInt("posY", 1);
        tag.putInt("posZ", 0);
        tag.putInt("sizeX", 0);
        tag.putInt("sizeY", 0);
        tag.putInt("sizeZ", 0);
        tag.putBoolean("ignoreEntities", false);
        tag.putFloat("integrity", 1.0F);
        tag.putLong("seed", 0L);
        tag.putString("rotation", "NONE");
        tag.putString("mirror", "NONE");
        tag.putBoolean("showboundingbox", true);
        ItemStack stack = new ItemStack(Blocks.STRUCTURE_BLOCK);
        stack.set(DataComponents.BLOCK_ENTITY_DATA, CustomData.of(tag));
        return stack;
    }

    private static void configurePalaceStructureBlock(StructureBlockEntity sbe, ResourceLocation structureId) {
        sbe.setMode(StructureMode.LOAD);
        sbe.setStructureName(structureId);
        sbe.setStructurePos(new BlockPos(0, 1, 0));
        sbe.setStructureSize(Vec3i.ZERO);
        sbe.setMirror(Mirror.NONE);
        sbe.setRotation(Rotation.NONE);
        sbe.setIgnoreEntities(false);
        sbe.setIntegrity(1.0F);
        sbe.setSeed(0L);
        sbe.setShowBoundingBox(true);
        sbe.setChanged();
    }

    private static void activateStructureBlockLoad(StructureBlockEntity sbe, ServerLevel level) {
        check(sbe.isStructureLoadable(),
                "setup: structure is not loadable: " + sbe.getStructureName());
        boolean placed = sbe.placeStructureIfSameSize(level);
        if (!placed) {
            placed = sbe.placeStructureIfSameSize(level);
        }
        check(placed, "setup: structure block LOAD_AREA did not place " + sbe.getStructureName()
                + " (size after prepare=" + sbe.getStructureSize() + ")");
    }

    private static void restampPalacePaletteBlockstates(ServerLevel level, ResourceLocation structureId, BlockPos origin) {
        Optional<StructureTemplate> templateOpt = level.getServer().getStructureManager().get(structureId);
        check(templateOpt.isPresent(), "setup: missing structure template " + structureId);
        StructureTemplate template = templateOpt.get();
        StructurePlaceSettings settings = new StructurePlaceSettings()
                .setMirror(Mirror.NONE)
                .setRotation(Rotation.NONE)
                .setIgnoreEntities(true)
                .setKnownShape(true);
        int flags = Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE;
        check(template.placeInWorld(level, origin, origin, settings, RandomSource.create(0L), flags),
                "setup: failed to restamp palette blockstates for " + structureId);

        for (Block railBlock : List.of(
                Blocks.RAIL, Blocks.POWERED_RAIL, Blocks.DETECTOR_RAIL, Blocks.ACTIVATOR_RAIL,
                BlockRegistry.LEVITA_RAIL.get())) {
            for (StructureTemplate.StructureBlockInfo info : template.filterBlocks(origin, settings, railBlock)) {
                level.setBlock(info.pos(), info.state(), flags);
            }
        }
    }

    private static void assertPalaceStructureBlockstates(ServerLevel level, BlockPos origin, BlockPos buttonPos) {
        BlockState button = level.getBlockState(buttonPos);
        check(button.is(BlockRegistry.FLOESTONE_BUTTON.get()),
                "setup: expected floestone button at " + buttonPos.toShortString() + ", got " + button);
        check(button.getValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.ATTACH_FACE)
                        == net.minecraft.world.level.block.state.properties.AttachFace.FLOOR,
                "setup: floestone button face != floor: " + button);
        check(button.getValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING)
                        == Direction.WEST,
                "setup: floestone button facing != west: " + button);

        expectRailShape(level, origin.offset(2, 0, 2), RailShape.EAST_WEST);
        expectRailShape(level, origin.offset(4, 0, 5), RailShape.ASCENDING_EAST);
        expectRailShape(level, origin.offset(6, 0, 2), RailShape.ASCENDING_SOUTH);
        expectRailShape(level, origin.offset(6, 0, 5), RailShape.ASCENDING_WEST);
        expectRailShape(level, origin.offset(7, 0, 7), RailShape.ASCENDING_EAST);
        expectRailShape(level, origin.offset(8, 0, 5), RailShape.ASCENDING_EAST);
        expectRailShape(level, origin.offset(10, 0, 2), RailShape.ASCENDING_SOUTH);
        expectRailShape(level, origin.offset(10, 0, 5), RailShape.ASCENDING_WEST);
        expectRailShape(level, origin.offset(10, 3, 7), RailShape.ASCENDING_EAST);
        expectRailShape(level, origin.offset(6, 6, 8), RailShape.ASCENDING_SOUTH);
        expectRailShape(level, origin.offset(10, 6, 8), RailShape.ASCENDING_SOUTH);

        BlockState levita = level.getBlockState(origin.offset(4, 0, 7));
        check(levita.is(BlockRegistry.LEVITA_RAIL.get()),
                "setup: missing levita rail at origin+(4,0,7): " + levita);
        check(level.getBlockState(origin.offset(4, 0, 6)).is(Blocks.REDSTONE_TORCH),
                "setup: missing redstone torch at origin+(4,0,6)");
    }

    private static void expectRailShape(ServerLevel level, BlockPos at, RailShape expect) {
        BlockState state = level.getBlockState(at);
        check(state.getBlock() instanceof BaseRailBlock,
                "setup: expected rail at " + at.toShortString() + ", got " + state);
        RailShape shape = state.getValue(((BaseRailBlock) state.getBlock()).getShapeProperty());
        check(shape == expect, "setup: rail at " + at.toShortString() + " shape=" + shape
                + ", expected " + expect + " from structure NBT (got " + state + ")");
    }

    @ClientTest("float state survives save and load, also from legacy NBT")
    private static Step floatStateSurvivesSaveLoad() {
        return Step.run(0, () -> onServer(server -> {
            ServerLevel world = world(server);
            List<String> wrong = new ArrayList<>();

            Minecart saved = EntityType.MINECART.create(world);
            MinecartFloating.setFloatTime(saved, 50);
            MinecartFloating.setIncline(saved, 1);
            MinecartFloating.setRailShape(saved, RailShape.ASCENDING_SOUTH);
            Minecart loaded = EntityType.MINECART.create(world);
            loaded.load(saved.saveWithoutId(new CompoundTag()));
            expect(wrong, MinecartFloating.getFloatTime(loaded) == 50, "float time " + MinecartFloating.getFloatTime(loaded));
            expect(wrong, MinecartFloating.getIncline(loaded) == 1, "incline " + MinecartFloating.getIncline(loaded));
            expect(wrong, MinecartFloating.getRailShape(loaded) == RailShape.ASCENDING_SOUTH, "shape " + MinecartFloating.getRailShapeName(loaded));

            CompoundTag tag = EntityType.MINECART.create(world).saveWithoutId(new CompoundTag());
            CompoundTag legacy = new CompoundTag();
            legacy.putBoolean("floating", true);
            legacy.putInt("floatTime", 30);
            legacy.putInt("floatIncline", -1);
            legacy.putString("floatShape", "ascending_north");
            tag.put("paradiseLostFloating", legacy);
            Minecart old = EntityType.MINECART.create(world);
            old.load(tag);
            expect(wrong, MinecartFloating.getFloatTime(old) == 30, "legacy float time " + MinecartFloating.getFloatTime(old));
            expect(wrong, MinecartFloating.getIncline(old) == -1, "legacy incline " + MinecartFloating.getIncline(old));
            expect(wrong, MinecartFloating.getRailShape(old) == RailShape.ASCENDING_NORTH, "legacy shape " + MinecartFloating.getRailShapeName(old));
            expect(wrong, !old.saveWithoutId(new CompoundTag()).contains("paradiseLostFloating"), "re-save still has the legacy key");

            CompoundTag ccaTag = EntityType.MINECART.create(world).saveWithoutId(new CompoundTag());
            CompoundTag ccaRoot = new CompoundTag();
            CompoundTag ccaFloat = new CompoundTag();
            ccaFloat.putBoolean("floating", true);
            ccaFloat.putInt("floatTime", 42);
            ccaRoot.put("paradise_lost:minecart_float", ccaFloat);
            ccaTag.put("cardinal_components", ccaRoot);
            Minecart fromCca = EntityType.MINECART.create(world);
            fromCca.load(ccaTag);
            expect(wrong, MinecartFloating.getFloatTime(fromCca) == 42, "cca float time " + MinecartFloating.getFloatTime(fromCca));
            expect(wrong, !fromCca.saveWithoutId(new CompoundTag()).contains("cardinal_components"), "re-save still has CCA float");
            checkAll(wrong);
        }));
    }

    private static Step setup(Consumer<ServerLevel> builder) {
        return setup(builder, false);
    }

    private static Step setup(Consumer<ServerLevel> builder, boolean watching) {
        return Step.run(0, () -> onServer(server -> {
            ServerPlayer player = player(server);
            ServerLevel level = world(server);
            player.setGameMode(GameType.CREATIVE);
            server.tickRateManager().setFrozen(false);
            dir = player.getDirection();
            start = ahead(player, 2, 0);
            activeStairs = STAIRS;
            activeGapAt = -1;
            activeGapLen = 0;
            shotResumeAlong = 0;
            connectGapAir = 1;
            builder.accept(level);
            if (watching) {
                player.setGameMode(GameType.SPECTATOR);
                aimSideOverview(player, level);
            }
        }));
    }

    private static Step setupAscendDescend(char rotation, Consumer<ServerLevel> builder) {
        return Step.run(0, () -> onServer(server -> {
            ServerPlayer player = player(server);
            ServerLevel level = world(server);
            player.setGameMode(GameType.CREATIVE);
            server.tickRateManager().setFrozen(false);
            dir = rotation == 'b' ? Direction.EAST : Direction.SOUTH;
            start = ahead(player, 2, 0);
            activeStairs = STAIRS;
            activeGapAt = -1;
            activeGapLen = 0;
            builder.accept(level);
            if (player.getVehicle() == null) {
                player.setGameMode(GameType.SPECTATOR);
                aimSideOverview(player, level);
            }
        }));
    }

    private static volatile Function<IntegratedServer, Boolean> shotWatch;
    private static volatile boolean shotWatchHit;
    private static volatile Throwable shotWatchError;
    private static volatile Vec3 shotTarget;

    static {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            Function<IntegratedServer, Boolean> watch = shotWatch;
            if (watch == null || shotWatchHit || !(server instanceof IntegratedServer integrated)) {
                return;
            }
            try {
                if (watch.apply(integrated)) {
                    shotTarget = world(integrated).getEntity(entityId).position();
                    shotWatchHit = true;
                }
            } catch (Throwable t) {
                shotWatchError = t;
                shotWatch = null;
            }
        });
    }

    private static Step untilShot(String what, int timeout, Function<IntegratedServer, Boolean> condition) {
        return Step.until(what, timeout, () -> {
            watchClientRender();
            if (shotWatch != condition) {
                shotWatchHit = false;
                shotWatchError = null;
                shotWatch = condition;
            }
            Throwable error = shotWatchError;
            if (error != null) {
                shotWatch = null;
                shotWatchError = null;
                if (error instanceof RuntimeException runtime) {
                    throw runtime;
                }
                if (error instanceof Error err) {
                    throw err;
                }
                throw new IllegalStateException(error);
            }
            if (shotWatchHit) {
                shotWatch = null;
                shotWatchHit = false;
                return true;
            }
            return false;
        });
    }

    private static List<Step> sideShotSteps(String uniqueName, boolean spectator) {
        return sideShotSteps(uniqueName, spectator, ShotFrame.CLOSE);
    }

    private record Shot(String name, boolean spectator, ShotFrame frame, int cartId, Vec3 target) {
    }

    private static final ArrayDeque<Shot> shotQueue = new ArrayDeque<>();
    private static Shot activeShot;
    private static boolean shotFrozen;
    private static int shotTicks;
    private static int clientTicks;

    static {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            clientTicks++;
            try {
                runShots();
            } catch (RuntimeException e) {
                activeShot = null;
                shotFrozen = false;
                shotQueue.clear();
            }
        });
    }

    private static List<Step> sideShotSteps(String uniqueName, boolean spectator, ShotFrame frame) {
        return List.of(Step.run(0, () -> {
            Vec3 target = shotTarget;
            shotTarget = null;
            shotQueue.add(new Shot(uniqueName, spectator, frame, entityId, target));
        }));
    }

    private static Step shotsFinished() {
        return Step.until("queued screenshots to finish", 120, () -> activeShot == null && shotQueue.isEmpty());
    }

    private static void runShots() {
        if (activeShot == null) {
            activeShot = shotQueue.poll();
            shotFrozen = false;
            shotTicks = 0;
            if (activeShot == null) {
                return;
            }
        }
        Shot shot = activeShot;
        shotTicks++;
        if (!shotFrozen) {
            if (client().level == null || !(client().level.getEntity(shot.cartId()) instanceof AbstractMinecart cart)) {
                activeShot = null;
                return;
            }
            if (shot.target() == null || cart.position().distanceTo(shot.target()) < 0.45 || shotTicks > 16) {
                onServer(server -> server.tickRateManager().setFrozen(true));
                shotFrozen = true;
                shotTicks = 0;
            }
            return;
        }
        switch (shotTicks) {
            case 1, 5 -> aimSideAtClientCart(shot);
            case 7 -> {
                aimSideAtClientCart(shot);
                screenshot(shot.name() + ".png");
            }
            case 10 -> {
                onServer(server -> server.tickRateManager().setFrozen(false));
                shotFrozen = false;
                activeShot = null;
            }
            default -> {
            }
        }
    }

    private static void aimSideAtClientCart(Shot shot) {
        if (!(client().level.getEntity(shot.cartId()) instanceof AbstractMinecart cart)) {
            return;
        }
        Vec3 focus = cart.position();
        onServer(server -> aimSideAtCart(player(server), world(server), shot.spectator(), shot.frame(), focus));
    }
    private static void aimSideOverview(ServerPlayer player, ServerLevel level) {
        Direction side = dir.getClockWise();
        double mid = Math.max(3, activeStairs / 2.0);
        double height = Math.max(2, activeStairs / 2.0 + 2);
        Vec3 eye = Vec3.atBottomCenterOf(start)
                .add(Vec3.atLowerCornerOf(dir.getNormal()).scale(mid))
                .add(Vec3.atLowerCornerOf(side.getNormal()).scale(SIDE_VIEW_DIST_WIDE))
                .add(0, height, 0);
        float yaw = side.getOpposite().toYRot();
        teleport(player, level, eye.x, eye.y, eye.z, yaw, 28);
    }

    private static void aimSideAtCart(ServerPlayer player, ServerLevel level, boolean spectator, ShotFrame frame, Vec3 focus) {
        if (spectator) {
            player.setGameMode(GameType.SPECTATOR);
        }
        Direction side = dir.getClockWise();
        double a = focus.subtract(Vec3.atBottomCenterOf(start)).dot(Vec3.atLowerCornerOf(dir.getNormal()));
        double rise = focus.y - start.getY();
        boolean wide = frame == ShotFrame.WIDE;
        double dist = wide ? SIDE_VIEW_DIST_WIDE : SIDE_VIEW_DIST;
        double stairs = Math.max(activeStairs, 6);
        double finishAlong = shotResumeAlong >= 0 ? stairs : 0;
        double alongAim = wide ? (a + finishAlong) * 0.5 : a;
        double lookY = wide
                ? Math.max(1.0, Math.max(rise, stairs * 0.5) * 0.55)
                : rise + 0.4;
        double eyeAlong = wide ? stairs * 0.5 : a;
        double heightAim = wide
                ? Math.max(stairs * 0.65, rise) + 3.5
                : rise + 1.6;
        Vec3 lookAt = Vec3.atBottomCenterOf(start)
                .add(Vec3.atLowerCornerOf(dir.getNormal()).scale(alongAim))
                .add(0, lookY, 0);
        Vec3 eye = Vec3.atBottomCenterOf(start)
                .add(Vec3.atLowerCornerOf(dir.getNormal()).scale(eyeAlong))
                .add(Vec3.atLowerCornerOf(side.getNormal()).scale(dist))
                .add(0, heightAim, 0);
        Vec3 delta = lookAt.subtract(eye);
        double horiz = Math.sqrt(delta.x * delta.x + delta.z * delta.z);
        float yaw = (float) (Mth.atan2(-delta.x, delta.z) * (180.0 / Math.PI));
        float pitch = (float) (Mth.atan2(-delta.y, horiz) * (180.0 / Math.PI));
        pitch = Mth.clamp(pitch, wide ? 8 : 8, wide ? 32 : 28);
        teleport(player, level, eye.x, eye.y, eye.z, yaw, pitch);
    }

    private static void track(ServerLevel world, BlockPos from, String layout) {
        for (int i = 0; i < layout.length(); i++) {
            BlockPos at = from.relative(dir, i);

            char c = layout.charAt(i);
            if (c == '.')
                continue;
            if (c == 'L')
                world.setBlockAndUpdate(at.below(), Blocks.REDSTONE_BLOCK.defaultBlockState());

            BlockState block = c == 'r' ? Blocks.RAIL.defaultBlockState() : BlockRegistry.LEVITA_RAIL.get().defaultBlockState();
            world.setBlockAndUpdate(at, block);
        }
    }

    private static void slope(ServerLevel world, BlockPos at) {
        RailShape shape = switch (dir) {
            case NORTH -> RailShape.ASCENDING_NORTH;
            case SOUTH -> RailShape.ASCENDING_SOUTH;
            case EAST -> RailShape.ASCENDING_EAST;
            default -> RailShape.ASCENDING_WEST;
        };
        BlockState old = world.getBlockState(at);
        Property<RailShape> property = ((BaseRailBlock) old.getBlock()).getShapeProperty();
        world.setBlock(at, old.setValue(property, shape), Block.UPDATE_ALL);

        BlockState now = world.getBlockState(at);
        check(now.is(old.getBlock()) && now.getValue(property) == shape, "setup: slope at " + at.toShortString() + " is " + now);
    }

    private static void singleSlope(ServerLevel world, boolean down) {
        world.setBlockAndUpdate(start.relative(dir), Blocks.STONE.defaultBlockState());
        track(world, start, "L");
        if (down) {
            track(world, start.relative(dir).above(), "r");
        } else {
            track(world, start.relative(dir, -2), "rr");
        }
        slope(world, start);
    }

    private static void staircase(ServerLevel world, BlockPos from, boolean levita) {
        staircase(world, from, levita, activeStairs);
    }

    private static void staircase(ServerLevel world, BlockPos from, boolean levita, int stairs) {
        for (int i = 0; i < stairs; i++) {
            BlockPos at = from.relative(dir, i).above(i);
            if (levita) {
                placePoweredLevitaRail(world, at);
            } else {
                world.setBlockAndUpdate(at.below(), Blocks.STONE.defaultBlockState());
                track(world, at, "r");
            }
        }
        BlockPos top = from.relative(dir, stairs).above(stairs);
        fill(world, top.below(), top.relative(dir).below(), Blocks.STONE.defaultBlockState());
        track(world, top, "rr");
        track(world, from.relative(dir, -3), "rrr");
        for (int i = 0; i < stairs; i++) {
            slope(world, from.relative(dir, i).above(i));
        }
    }

    private static void staircaseWithGap(ServerLevel world, BlockPos from, int gapAt, int gapLength) {
        int stairs = activeStairs;
        check(gapAt > 0 && gapLength >= 1 && gapAt + gapLength < stairs - 1,
                "setup: gap [" + gapAt + "," + (gapAt + gapLength) + ") must fit inside 0.." + stairs);
        for (int i = 0; i < stairs; i++) {
            if (i >= gapAt && i < gapAt + gapLength) {
                world.setBlockAndUpdate(from.relative(dir, i).above(i).below(), Blocks.STONE.defaultBlockState());
                continue;
            }
            placePoweredLevitaRail(world, from.relative(dir, i).above(i));
        }
        BlockPos top = from.relative(dir, stairs).above(stairs);
        fill(world, top.below(), top.relative(dir).below(), Blocks.STONE.defaultBlockState());
        track(world, top, "rr");
        track(world, from.relative(dir, -3), "rrr");
        for (int i = 0; i < stairs; i++) {
            if (i >= gapAt && i < gapAt + gapLength) {
                continue;
            }
            slope(world, from.relative(dir, i).above(i));
        }
        int mid = gapAt + gapLength / 2;
        for (int i = gapAt; i < gapAt + gapLength; i++) {
            boolean keepPier = i == gapAt || i == gapAt + gapLength - 1 || i == mid;
            BlockPos pier = from.relative(dir, i).above(i).below();
            world.setBlockAndUpdate(pier, keepPier ? Blocks.STONE.defaultBlockState() : Blocks.AIR.defaultBlockState());
            world.setBlockAndUpdate(from.relative(dir, i).above(i), Blocks.AIR.defaultBlockState());
        }
    }

    private static void placePoweredLevitaRail(ServerLevel world, BlockPos at) {
        world.setBlockAndUpdate(at.below(), Blocks.REDSTONE_BLOCK.defaultBlockState());
        world.setBlockAndUpdate(at, BlockRegistry.LEVITA_RAIL.get().defaultBlockState());
    }

    private static boolean stairsTick(DoublePredicate done) {
        int clientIncline = 0;
        if (client().level.getEntity(entityId) instanceof AbstractMinecart client) {
            checkOnStairs(client, "client");
            clientIncline = MinecartFloating.getIncline(client);
        }
        int seenIncline = clientIncline;
        return fromServer(server -> {
            AbstractMinecart cart = serverCart(server, entityId);
            checkOnStairs(cart, "server");
            int serverIncline = MinecartFloating.getIncline(cart);
            check(seenIncline == 0 || serverIncline == 0 || seenIncline == serverIncline,
                    "client incline " + seenIncline + " but server incline " + serverIncline + describe(cart));
            return done.test(along(cart)) || ++ticks >= 100;
        });
    }

    private static void checkOnStairs(AbstractMinecart cart, String where) {
        double a = along(cart);
        double rise = cart.getY() - start.getY();
        check(Math.abs(side(cart)) < 0.3, where + " cart is " + fmt(side(cart)) + " blocks beside the track" + describe(cart) + floatState(cart));
        if (inStairGap(a)) {
            checkFloatRotation(cart, where);
            return;
        }
        double expected = Math.clamp(a + 0.5, 0, activeStairs);
        check(Math.abs(rise - expected) < 0.6, where + " cart is " + fmt(rise) + " above the bottom, expected about " + fmt(expected)
                + describe(cart) + floatState(cart));
        checkFloatRotation(cart, where);
    }

    private static void checkGapPath(AbstractMinecart cart, String where) {
        check(Math.abs(side(cart)) < 0.35, where + " cart is " + fmt(side(cart)) + " blocks beside the track"
                + describe(cart) + floatState(cart));
        checkFloatRotation(cart, where);
    }

    private static boolean inStairGap(double along) {
        return activeGapLen > 0 && along > activeGapAt - 0.75 && along < activeGapAt + activeGapLen + 0.75;
    }

    private static void checkFloatRotation(AbstractMinecart cart, String where) {
        if (!MinecartFloating.isFloating(cart)) {
            return;
        }
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        int incline = MinecartFloating.getIncline(cart);
        boolean offRail = MinecartFloating.isOffRail(cart) || MinecartFloating.shouldSkipRailRenderSnap(cart);
        boolean hasPose = pose.paradiseLost$hasRailRenderPose();

        if (incline != 0) {
            if (offRail) {
                float pitch = hasPose
                        ? pose.paradiseLost$getRailRenderPitch()
                        : MinecartFloating.getSyncedFloatPitch(cart);
                float yaw = hasPose
                        ? pose.paradiseLost$getRailRenderYaw()
                        : MinecartFloating.getSyncedFloatYaw(cart);
                check(Math.abs(pitch) > 1 || hasPose, where + " floating slope has no pitch pose/sync"
                        + describe(cart) + floatState(cart));
                check(Math.abs(Math.abs(pitch) - 45) < 8, where + " floating slope pitch is " + fmt(pitch)
                        + ", expected about +-45" + describe(cart) + floatState(cart));
                check(Math.abs(Math.abs(cart.getXRot()) - 45) < 8, where + " entity pitch is " + fmt(cart.getXRot())
                        + ", expected about +-45" + describe(cart) + floatState(cart));
                check(Math.abs(cart.getXRot() - pitch) < 5, where + " entity pitch " + fmt(cart.getXRot())
                        + " != float pitch " + fmt(pitch) + describe(cart) + floatState(cart));
                if (hasPose || Math.abs(MinecartFloating.getSyncedFloatPitch(cart)) > 1) {
                    check(Math.abs(Mth.wrapDegrees(cart.getYRot() - yaw)) < 8, where + " entity yaw " + fmt(cart.getYRot())
                            + " != float yaw " + fmt(yaw) + describe(cart) + floatState(cart));
                }
                checkSlopeFacing(cart, where, yaw);
            } else if (hasPose) {
                float pitch = pose.paradiseLost$getRailRenderPitch();
                check(Math.abs(Math.abs(pitch) - 45) < 8, where + " on-rail slope pose pitch is " + fmt(pitch)
                        + ", expected about +-45" + describe(cart) + floatState(cart));
                checkSlopeFacing(cart, where, pose.paradiseLost$getRailRenderYaw());
            }
        } else if (offRail) {
            check(Math.abs(cart.getXRot()) < 2, where + " flat float entity pitch is " + fmt(cart.getXRot())
                    + describe(cart) + floatState(cart));
            if (hasPose) {
                check(Math.abs(pose.paradiseLost$getRailRenderPitch()) < 2, where + " flat float pose pitch is "
                        + fmt(pose.paradiseLost$getRailRenderPitch()) + describe(cart) + floatState(cart));
            }
            check(Math.abs(MinecartFloating.getSyncedFloatPitch(cart)) < 2, where + " flat float sync pitch is "
                    + fmt(MinecartFloating.getSyncedFloatPitch(cart)) + describe(cart) + floatState(cart));
        }
    }

    private static void checkSlopeFacing(AbstractMinecart cart, String where, float railYaw) {
        double yawRad = Math.toRadians(railYaw);
        Vec3 facing = new Vec3(Math.cos(yawRad), 0, Math.sin(yawRad));
        Vec3 alongTrack = Vec3.atLowerCornerOf(dir.getNormal());
        Vec3 sideways = Vec3.atLowerCornerOf(dir.getClockWise().getNormal());
        check(Math.abs(facing.dot(alongTrack)) > 0.85, where + " cart yaw is not along the track (diagonal float), poseYaw="
                + fmt(railYaw) + describe(cart) + floatState(cart));
        check(Math.abs(facing.dot(sideways)) < 0.15, where + " cart renders across the track, shape "
                + MinecartFloating.getRailShapeName(cart) + describe(cart) + floatState(cart));

        Vec3 motion = cart.getDeltaMovement().multiply(1, 0, 1);
        if (motion.lengthSqr() > 0.01) {
            motion = motion.normalize();
            check(Math.abs(facing.dot(motion)) > 0.85, where + " cart yaw is not on the motion axis (diagonal float pose), poseYaw="
                    + fmt(railYaw) + describe(cart) + floatState(cart));
        }
    }

    private static void trench(ServerLevel world, int from, int to) {
        Direction side = dir.getClockWise();
        fill(world, start.relative(dir, from).relative(side).below(3), start.relative(dir, to).relative(side.getOpposite()).below(), Blocks.AIR.defaultBlockState());
    }

    private static void fill(ServerLevel world, BlockPos a, BlockPos b, BlockState state) {
        for (BlockPos at : BlockPos.betweenClosed(a, b)) world.setBlockAndUpdate(at, state);
    }

    private static double launchPhase;

    private static int launch(ServerLevel world, EntityType<? extends AbstractMinecart> type, BlockPos at, double speed) {
        AbstractMinecart cart = type.create(world);
        cart.moveTo(at.getX() + 0.5 + dir.getStepX() * launchPhase, at.getY(), at.getZ() + 0.5 + dir.getStepZ() * launchPhase, 0, 0);
        cart.setDeltaMovement(Vec3.atLowerCornerOf(dir.getNormal()).scale(speed));
        world.addFreshEntity(cart);
        return cart.getId();
    }

    private static AbstractMinecart serverCart(IntegratedServer server, int id) {
        AbstractMinecart cart = (AbstractMinecart) world(server).getEntity(id);
        check(cart != null, "server cart " + id + " is gone");
        return cart;
    }

    private static AbstractMinecart clientCart(int id) {
        AbstractMinecart cart = (AbstractMinecart) client().level.getEntity(id);
        check(cart != null, "client cart " + id + " is gone");
        return cart;
    }

    private static boolean serverFloating(int id) {
        return fromServer(server -> MinecartFloating.isFloating(serverCart(server, id)));
    }

    private static boolean clientFloating(int id) {
        return client().level.getEntity(id) instanceof AbstractMinecart cart && MinecartFloating.isFloating(cart);
    }

    private static double speedPast(AbstractMinecart cart, double old) {
        return old < 0 && along(cart) > 3.5 ? cart.getDeltaMovement().horizontalDistance() : old;
    }

    private static double along(Entity entity) {
        return entity.position().subtract(Vec3.atBottomCenterOf(start)).dot(Vec3.atLowerCornerOf(dir.getNormal()));
    }

    private static double side(Entity entity) {
        return entity.position().subtract(Vec3.atBottomCenterOf(start)).dot(Vec3.atLowerCornerOf(dir.getClockWise().getNormal()));
    }

    private static String describe(Entity entity) {
        return " (along=" + fmt(along(entity)) + ", side=" + fmt(side(entity)) + ", y=" + fmt(entity.getY())
                + ", motion=" + fmt(entity.getDeltaMovement().x) + "/" + fmt(entity.getDeltaMovement().y) + "/" + fmt(entity.getDeltaMovement().z) + ")";
    }

    private static String floatState(AbstractMinecart cart) {
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        String railPose = pose.paradiseLost$hasRailRenderPose()
                ? fmt(pose.paradiseLost$getRailRenderYaw()) + "/" + fmt(pose.paradiseLost$getRailRenderPitch())
                : "none";
        return " [float=" + MinecartFloating.getFloatTime(cart) + ", incline=" + MinecartFloating.getIncline(cart)
                + ", shape=" + MinecartFloating.getRailShapeName(cart)
                + ", offRail=" + MinecartFloating.isOffRail(cart)
                + ", yaw/pitch=" + fmt(cart.getYRot()) + "/" + fmt(cart.getXRot())
                + ", railPose=" + railPose + "]";
    }

    private static String fmt(double value) {
        return String.format(Locale.ROOT, "%.2f", value);
    }

    private static Entity levitaArrow() {
        List<LevitaArrow> arrows = client().level.getEntitiesOfClass(LevitaArrow.class, client().player.getBoundingBox().inflate(40), e -> true);
        return arrows.isEmpty() ? null : arrows.getFirst();
    }
}
