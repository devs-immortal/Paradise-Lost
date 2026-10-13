package net.id.paradise_lost.clienttest.tests;

import net.id.paradise_lost.attachments.MinecartFloating;
import net.id.paradise_lost.block.mechanical.LevitaRailBlock;
import net.id.paradise_lost.clienttest.ClientTest;
import net.id.paradise_lost.clienttest.Step;
import net.id.paradise_lost.entity.ParadiseLostEntityExtensions;
import net.id.paradise_lost.entity.block.FloatingBlockEntity;
import net.id.paradise_lost.entity.projectile.LevitaArrow;
import net.id.paradise_lost.item.tool.base_tools.GravityTool;
import net.id.paradise_lost.registry.BlockRegistry;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.entity.vehicle.MinecartFurnace;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.DoublePredicate;

import static net.id.paradise_lost.clienttest.TestHelpers.*;
import static net.id.paradise_lost.registry.ItemRegistry.*;

public final class LevitaTests {
    private static final int CHARGE = 82;
    private static final int STAIRS = 6;
    private static BlockPos pos;
    private static int entityId;
    private static int secondId;
    private static int thirdId;
    private static double startY;

    private static BlockPos start;
    private static Direction dir;
    private static int ticks;
    private static double seen;
    private static double speedA;
    private static double speedB;
    private static double speedC;
    private static String state;
    private static double levitaTop;
    private static double plainTop;

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
                    check(cart.getXRot() == 0, "server pitch is " + cart.getXRot() + " on a flat float" + floatState(cart));
                    return !MinecartFloating.isFloating(cart);
                })),
                Step.until("float to end on the client", 20, () -> {
                    AbstractMinecart cart = clientCart(entityId);
                    check(cart.getXRot() == 0, "client pitch is " + cart.getXRot() + " on a flat float" + floatState(cart));
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
                    track(world, start, "rLrr......rrrr");
                    trench(world, 4, 9);
                    entityId = launch(world, EntityType.MINECART, start, 0.4);
                }),
                Step.until("cart to reach the far rails", 60, () -> fromServer(server -> {
                    AbstractMinecart cart = serverCart(server, entityId);
                    double a = along(cart);
                    if (a > 3.5 && a < 9.5) {
                        check(Math.abs(cart.getY() - start.getY()) < 0.2, "cart is at y=" + fmt(cart.getY()) + " over the gap" + describe(cart) + floatState(cart));
                    }
                    return a > 11.5;
                })),
                Step.run(0, () -> onServer(server -> {
                    AbstractMinecart cart = serverCart(server, entityId);
                    check(world(server).getBlockState(cart.blockPosition()).is(Blocks.RAIL), "cart is not on the far rails: " + describe(cart) + floatState(cart));
                    check(cart.getDeltaMovement().horizontalDistance() > 0.1, "cart stopped on the far rails: " + describe(cart));
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
                })),
                Step.until("client to show the 45 degree float", 20, () -> {
                    AbstractMinecart cart = clientCart(entityId);
                    return MinecartFloating.getIncline(cart) == 1
                            && Math.abs(Math.abs(cart.getXRot()) - 45) < 1
                            && MinecartFloating.getFloatingRenderYaw(cart) != null;
                })
        );
    }

    @ClientTest("ceiling stops a rising cart")
    private static List<Step> ceilingStopsRisingCart() {
        return List.of(
                setup(world -> {
                    singleSlope(world, false);
                    Direction side = dir.getClockWise();
                    fill(world, start.relative(dir, 1).relative(side).above(4), start.relative(dir, 15).relative(side.getOpposite()).above(4), Blocks.STONE.defaultBlockState());
                    entityId = launch(world, EntityType.MINECART, start.relative(dir, -2), 0.4);
                    ticks = 0;
                    seen = start.getY();
                }),
                Step.until("40 ticks under the ceiling", 50, () -> fromServer(server -> {
                    AbstractMinecart cart = serverCart(server, entityId);
                    seen = Math.max(seen, cart.getY());
                    check(cart.getBoundingBox().maxY <= start.getY() + 4.01, "cart went into the ceiling: " + describe(cart) + floatState(cart));
                    return ++ticks >= 40;
                })),
                Step.run(0, () -> check(seen > start.getY() + 2, "cart only rose to y=" + fmt(seen) + ", so the ceiling was never tested"))
        );
    }

    @ClientTest("cart floating down a slope stays above the ground")
    private static List<Step> descendingCartStaysAboveGround() {
        return List.of(
                setup(world -> {
                    singleSlope(world, true);
                    entityId = launch(world, EntityType.MINECART, start.relative(dir).above(), -0.4);
                    ticks = 0;
                    seen = 0;
                }),
                Step.until("40 ticks after the slope", 60, () -> fromServer(server -> {
                    AbstractMinecart cart = serverCart(server, entityId);
                    ServerLevel world = world(server);
                    seen = Math.min(seen, along(cart));
                    check(cart.getY() >= start.getY() - 0.1, "cart sank to y=" + fmt(cart.getY()) + describe(cart) + floatState(cart));
                    check(world.getBlockState(cart.blockPosition()).getCollisionShape(world, cart.blockPosition()).isEmpty(),
                            "cart is inside " + world.getBlockState(cart.blockPosition()).getBlock().getName().getString() + describe(cart) + floatState(cart));
                    return ++ticks >= 40;
                })),
                Step.run(0, () -> check(seen < -1, "setup: cart never left the bottom of the slope"))
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
            checkAll(wrong);
        }));
    }

    private static Step setup(Consumer<ServerLevel> builder) {
        return Step.run(0, () -> onServer(server -> {
            ServerPlayer player = player(server);
            dir = player.getDirection();
            start = ahead(player, 2, 0);
            builder.accept(player.serverLevel());
        }));
    }

    // r = rail, L = levita rail on redstone block, l = unpowered levita rail, . = nothing
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
        for (int i = 0; i < STAIRS; i++) {
            BlockPos at = from.relative(dir, i).above(i);
            if (!levita)
                world.setBlockAndUpdate(at.below(), Blocks.STONE.defaultBlockState());
            track(world, at, levita ? "L" : "r");
        }
        BlockPos top = from.relative(dir, STAIRS).above(STAIRS);
        fill(world, top.below(), top.relative(dir).below(), Blocks.STONE.defaultBlockState());
        track(world, top, "rr");
        track(world, from.relative(dir, -3), "rrr");
        for (int i = 0; i < STAIRS; i++) {
            slope(world, from.relative(dir, i).above(i));
        }
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
        double expected = Math.clamp(a + 0.5, 0, STAIRS);
        check(Math.abs(side(cart)) < 0.3, where + " cart is " + fmt(side(cart)) + " blocks beside the track" + describe(cart) + floatState(cart));
        check(Math.abs(rise - expected) < 0.6, where + " cart is " + fmt(rise) + " above the bottom, expected about " + fmt(expected)
                + describe(cart) + floatState(cart));

        Vec3 tangent = MinecartFloating.getMotionAlignedTangent(cart);
        if (MinecartFloating.shouldUseFloatingRenderRotation(cart) && tangent != null) {
            Vec3 sideways = Vec3.atLowerCornerOf(dir.getClockWise().getNormal());
            check(Math.abs(tangent.dot(sideways)) < 0.01, where + " cart renders across the track, shape " + MinecartFloating.getRailShapeName(cart) + describe(cart));

            boolean facesUp = tangent.dot(Vec3.atLowerCornerOf(dir.getNormal())) > 0;
            check(facesUp == MinecartFloating.getIncline(cart) > 0, where + " cart is drawn tilted the wrong way, it faces "
                    + (facesUp ? "up" : "down") + " the slope" + describe(cart) + floatState(cart));
        }
    }

    private static void trench(ServerLevel world, int from, int to) {
        Direction side = dir.getClockWise();
        fill(world, start.relative(dir, from).relative(side).below(3), start.relative(dir, to).relative(side.getOpposite()).below(), Blocks.AIR.defaultBlockState());
    }

    private static void fill(ServerLevel world, BlockPos a, BlockPos b, BlockState state) {
        for (BlockPos at : BlockPos.betweenClosed(a, b)) world.setBlockAndUpdate(at, state);
    }

    private static int launch(ServerLevel world, EntityType<? extends AbstractMinecart> type, BlockPos at, double speed) {
        AbstractMinecart cart = type.create(world);
        cart.moveTo(at.getX() + 0.5, at.getY(), at.getZ() + 0.5, 0, 0);
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
        return " [float=" + MinecartFloating.getFloatTime(cart) + ", incline=" + MinecartFloating.getIncline(cart)
                + ", shape=" + MinecartFloating.getRailShapeName(cart) + ", pitch=" + fmt(cart.getXRot()) + "]";
    }

    private static String fmt(double value) {
        return String.format(Locale.ROOT, "%.2f", value);
    }

    private static Entity levitaArrow() {
        List<LevitaArrow> arrows = client().level.getEntitiesOfClass(LevitaArrow.class, client().player.getBoundingBox().inflate(40), e -> true);
        return arrows.isEmpty() ? null : arrows.getFirst();
    }
}
