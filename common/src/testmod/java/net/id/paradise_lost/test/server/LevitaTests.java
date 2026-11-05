package net.id.paradise_lost.test.server;

import net.id.paradise_lost.block.mechanical.LevitaRailBlock;
import net.id.paradise_lost.entity.MinecartFloatPoseAccess;
import net.id.paradise_lost.registry.BlockRegistry;
import net.id.paradise_lost.test.Step;
import net.id.paradise_lost.util.MinecartFloatingHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.entity.vehicle.MinecartFurnace;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static net.id.paradise_lost.test.LevitaTrack.*;
import static net.id.paradise_lost.test.TestHelpers.*;
import static net.id.paradise_lost.test.server.ServerTestHelpers.*;

public final class LevitaTests {
    private static final int CEILING_HEIGHT = 10;

    private static BlockPos pos;
    private static int entityId;
    private static int secondId;
    private static int thirdId;
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

    @ServerTest("float time stacks over a run of powered rails")
    private static List<Step> floatTimeStacks() {
        return List.of(
                setup(world -> {
                    track(world, start, "rLLLrr");
                    entityId = launch(world, EntityType.MINECART, start, 0.4);
                    seen = 0;
                }),
                Step.until("cart to pass the run", 40, () -> {
                    AbstractMinecart cart = serverCart(entityId);
                    seen = Math.max(seen, MinecartFloatingHelper.getFloatTime(cart));
                    return along(cart) > 4.5;
                }),
                Step.run(0, () -> check(seen >= 3 * CHARGE - 10, "highest float time was " + (int) seen + ", expected at least " + (3 * CHARGE - 10)))
        );
    }

    @ServerTest("a second cart also floats once the rail has reset")
    private static List<Step> secondCartFloats() {
        return List.of(
                setup(world -> {
                    track(world, start, "rrLrrrrrrr");
                    entityId = launch(world, EntityType.MINECART, start, 0.4);
                }),
                Step.until("first cart to float", 40, () -> serverFloating(entityId)),
                // the rail is locked for 80 ticks after the first cart, see #954
                Step.run(90, () -> secondId = launch(level(), EntityType.MINECART, start, 0.4)),
                Step.until("second cart to float", 40, () -> serverFloating(secondId))
        );
    }

    @ServerTest("a standing cart on a powered rail floats in place")
    private static List<Step> standingCartStays() {
        return List.of(
                setup(world -> {
                    track(world, start, "L");
                    entityId = launch(world, EntityType.MINECART, start, 0);
                    ticks = 0;
                }),
                Step.until("cart to float", 40, () -> serverFloating(entityId)),
                Step.until("100 ticks in place", 110, () -> {
                    AbstractMinecart cart = serverCart(entityId);
                    check(cart.blockPosition().equals(start), "cart drifted to " + describe(cart) + floatState(cart));
                    return ++ticks >= 100;
                })
        );
    }

    @ServerTest("levita rails neither boost nor brake a cart")
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
                Step.until("all carts to pass the levita rails", 40, () -> {
                    speedA = speedPast(serverCart(entityId), speedA);
                    speedB = speedPast(serverCart(secondId), speedB);
                    speedC = speedPast(serverCart(thirdId), speedC);
                    check(!MinecartFloatingHelper.isFloating(serverCart(secondId)), "cart over the unpowered rail floats");
                    return speedA >= 0 && speedB >= 0 && speedC >= 0;
                }),
                Step.run(0, () -> {
                    List<String> wrong = new ArrayList<>();
                    expect(wrong, Math.abs(speedB - speedA) < 0.05, "unpowered levita rail: speed " + fmt(speedB) + ", plain rail: " + fmt(speedA));
                    expect(wrong, Math.abs(speedC - speedA) < 0.05, "powered levita rail: speed " + fmt(speedC) + ", plain rail: " + fmt(speedA));
                    checkAll(wrong);
                })
        );
    }

    @ServerTest("an unpowered levita rail ends the float")
    private static List<Step> unpoweredRailEndsFloat() {
        return List.of(
                setup(world -> {
                    track(world, start, "rLrrlrr");
                    entityId = launch(world, EntityType.MINECART, start, 0.4);
                }),
                Step.until("cart to float", 40, () -> serverFloating(entityId)),
                Step.until("float to end at the unpowered rail", 30, () -> !serverFloating(entityId)),
                Step.run(0, () -> {
                    AbstractMinecart cart = serverCart(entityId);
                    check(along(cart) > 3.4, "float ended before the unpowered rail, at " + describe(cart));
                })
        );
    }

    @ServerTest("power spreads along levita rails only")
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
                Step.run(10, () -> {
                    ServerLevel world = level();
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
                })
        );
    }

    @ServerTest("floating cart crosses gap and lands on rails again")
    private static List<Step> floatsOverGapAndRejoins() {
        return List.of(
                setup(world -> {
                    track(world, start, "rLrr........rrrr");
                    trench(world, 4, 11);
                    entityId = launch(world, EntityType.MINECART, start, 0.4);
                }),
                Step.until("cart to reach the far rails", 80, () -> {
                    AbstractMinecart cart = serverCart(entityId);
                    double a = along(cart);
                    if (a > 3.5 && a < 12.0) {
                        check(Math.abs(cart.getY() - start.getY()) < 0.2, "cart is at y=" + fmt(cart.getY()) + " over the gap" + describe(cart) + floatState(cart));
                        checkFloatRotation(cart, "server gap");
                    }
                    return a > 12.0 && a < 16.0 && MinecartFloatingHelper.isCartOnRail(cart) && !MinecartFloatingHelper.isOffRail(cart);
                }),
                Step.run(0, () -> {
                    AbstractMinecart cart = serverCart(entityId);
                    check(MinecartFloatingHelper.isCartOnRail(cart) && !MinecartFloatingHelper.isOffRail(cart),
                            "cart is not seated on the far rails: " + describe(cart) + floatState(cart));
                    check(along(cart) > 12.0 && along(cart) < 16.0, "cart not on the far rail run: " + describe(cart) + floatState(cart));
                    checkFloatRotation(cart, "server after flat reconnect");
                })
        );
    }

    @ServerTest("floating cart falls when the float runs out")
    private static List<Step> fallsWhenFloatEnds() {
        return List.of(
                setup(world -> {
                    track(world, start, "rLrr");
                    trench(world, 4, 54);
                    entityId = launch(world, EntityType.MINECART, start, 0.4);
                }),
                Step.until("cart to float", 40, () -> serverFloating(entityId)),
                Step.until("float to end", CHARGE + 20, () -> {
                    AbstractMinecart cart = serverCart(entityId);
                    if (along(cart) > 3.5) {
                        check(Math.abs(cart.getY() - start.getY()) < 0.3, "floating cart is at y=" + fmt(cart.getY()) + describe(cart) + floatState(cart));
                        checkFloatRotation(cart, "server air");
                    }
                    return !MinecartFloatingHelper.isFloating(cart);
                }),
                Step.until("cart to fall into the trench", 40, () -> serverCart(entityId).getY() < start.getY() - 1.5)
        );
    }

    @ServerTest("wall stops a cart that just floated off the track")
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
                Step.until("40 ticks at the wall", 50, () -> {
                    AbstractMinecart cart = serverCart(entityId);
                    check(along(cart) < 4.5 - cart.getBbWidth() / 2 + 0.01, "cart went into the wall: " + describe(cart) + floatState(cart));
                    return ++ticks >= 40;
                })
        );
    }

    @ServerTest("flat floating cart does not climb onto a flat rail one block above")
    private static List<Step> flatCartDoesNotClimbOntoRailAbove() {
        return List.of(
                setup(world -> {
                    track(world, start, "rLrr");
                    // two blocks of gap, then a flat rail on a block that is one higher than the cart's rails
                    BlockPos upper = start.relative(dir, 6).above();
                    fill(world, upper.below(), upper.relative(dir, 3).below(), Blocks.STONE.defaultBlockState());
                    track(world, upper, "rrrr");
                    entityId = launch(world, EntityType.MINECART, start, 0.4);
                    ticks = 0;
                    seen = start.getY();
                }),
                Step.until("cart to float", 40, () -> serverFloating(entityId)),
                Step.until("80 ticks at the step", 90, () -> {
                    AbstractMinecart cart = serverCart(entityId);
                    seen = Math.max(seen, cart.getY());
                    check(seen < start.getY() + 0.5, "cart climbed to y=" + fmt(seen) + " onto the rail above" + describe(cart) + floatState(cart));
                    check(MinecartFloatingHelper.getIncline(cart) == 0, "flat cart got incline " + MinecartFloatingHelper.getIncline(cart) + describe(cart) + floatState(cart));
                    return ++ticks >= 80;
                })
        );
    }

    private static final double[] PERP_SPEEDS = {0.2, 0.25, 0.3};
    private static final int[] PERP_GAPS = {0, 1, 2, 3};
    private static final List<String> perpWrong = new ArrayList<>();

    @ServerTest("flat floating cart reaches a perpendicular staircase after a gap at any speed")
    private static List<Step> floatingCartReachesPerpendicularStaircase() {
        List<Step> steps = new ArrayList<>();
        steps.add(setup(world -> perpWrong.clear()));
        for (double speed : PERP_SPEEDS) {
            for (int gap : PERP_GAPS) {
                steps.add(Step.run(0, () -> {
                    Direction up = dir.getCounterClockWise();
                    fill(level(), start.relative(dir, -3).relative(up, -2), start.relative(dir, 20).relative(up, 14).above(9), Blocks.AIR.defaultBlockState());
                    track(level(), start, "rLrr");
                    // The staircase runs across the track and the cart meets its second step, with the first one beside and below it.
                    BlockPos first = start.relative(dir, 4 + gap);
                    for (int i = -1; i < 5; i++) {
                        BlockPos at = first.relative(up, i).above(i);
                        level().setBlockAndUpdate(at.below(), Blocks.REDSTONE_BLOCK.defaultBlockState());
                    }
                    for (int i = -1; i < 4; i++) {
                        level().setBlockAndUpdate(first.relative(up, i).above(i), BlockRegistry.LEVITA_RAIL.get().defaultBlockState());
                    }
                    for (int i = -1; i < 4; i++) {
                        forceShape(level(), first.relative(up, i).above(i), ascending(up));
                    }
                    BlockPos top = first.relative(up, 4).above(4);
                    for (int i = 0; i < 6; i++) {
                        level().setBlockAndUpdate(top.relative(up, i).below(), Blocks.REDSTONE_BLOCK.defaultBlockState());
                        level().setBlockAndUpdate(top.relative(up, i), BlockRegistry.LEVITA_RAIL.get().defaultBlockState());
                    }
                    entityId = launch(level(), EntityType.MINECART, start, speed);
                    ticks = 0;
                    seen = start.getY();
                }));
                steps.add(Step.until("cart to climb the staircase", 110, () -> {
                    AbstractMinecart cart = (AbstractMinecart) level().getEntity(entityId);
                    if (cart != null) {
                        seen = Math.max(seen, cart.getY());
                    }
                    return seen > start.getY() + 3.5 || ++ticks >= 100;
                }));
                steps.add(Step.run(0, () -> {
                    AbstractMinecart cart = (AbstractMinecart) level().getEntity(entityId);
                    if (seen <= start.getY() + 3.5) {
                        perpWrong.add("speed " + speed + ", gap " + gap + ": cart stopped at the staircase"
                                + (cart == null ? "" : describe(cart) + floatState(cart)));
                    }
                    if (cart != null) {
                        cart.discard();
                    }
                }));
            }
        }
        steps.add(Step.run(0, () -> checkAll(perpWrong)));
        return steps;
    }

    @ServerTest("climbing cart levels out against a ledge that only clips the top of it")
    private static List<Step> climbingCartLevelsOutAgainstOverhang() {
        return List.of(
                setup(world -> {
                    int ledgeY = start.getY() + 8;
                    BlockPos ledge = new BlockPos(start.getX(), ledgeY, start.getZ()).relative(dir, 5);
                    fill(world, ledge.relative(dir.getClockWise(), 2), ledge.relative(dir.getCounterClockWise(), 2), Blocks.STONE.defaultBlockState());
                    entityId = launch(world, EntityType.MINECART, start, 0);
                    AbstractMinecart cart = serverCart(entityId);
                    cart.setPos(start.getX() + 0.5, ledgeY - 4.9, start.getZ() + 0.5);
                    cart.setDeltaMovement(dir.getStepX() * 0.4, 0.4, dir.getStepZ() * 0.4);
                    MinecartFloatingHelper.setFloatTime(cart, 120);
                    MinecartFloatingHelper.setRailShape(cart, ascending(dir));
                    MinecartFloatingHelper.setIncline(cart, 1);
                    ((MinecartFloatPoseAccess) cart).paradiseLost$setOpenAirClimbGrace(120);
                    ticks = 0;
                }),
                Step.until("cart to reach the ledge", 40, () -> along(serverCart(entityId)) > 3.9),
                Step.until("10 ticks against the ledge", 20, () -> ++ticks >= 10),
                Step.run(0, () -> {
                    AbstractMinecart cart = serverCart(entityId);
                    check(MinecartFloatingHelper.getIncline(cart) == 0, "cart still hangs at incline " + MinecartFloatingHelper.getIncline(cart)
                            + " against the ledge" + describe(cart) + floatState(cart));
                })
        );
    }

    @ServerTest("ceiling stops a rising cart")
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
                Step.until("40 ticks under the ceiling", 50, () -> {
                    AbstractMinecart cart = serverCart(entityId);
                    seen = Math.max(seen, cart.getY());
                    check(cart.getBoundingBox().maxY <= start.getY() + CEILING_HEIGHT + 0.01,
                            "cart went into the ceiling: " + describe(cart) + floatState(cart));
                    return ++ticks >= 40;
                }),
                Step.run(0, () -> {
                    AbstractMinecart cart = serverCart(entityId);
                    check(seen > start.getY() + 4, "cart only rose to y=" + fmt(seen) + ", so the high ceiling was never tested");
                    check(MinecartFloatingHelper.getIncline(cart) == 0, "ceiling hit should clear incline, was "
                            + MinecartFloatingHelper.getIncline(cart) + floatState(cart));
                    check(Math.abs(cart.getXRot()) < 2, "ceiling hit should flatten pitch, was "
                            + fmt(cart.getXRot()) + floatState(cart));
                    check(Math.abs(cart.getDeltaMovement().y) < 0.01, "ceiling hit should zero dy, motion="
                            + fmt(cart.getDeltaMovement().x) + "/" + fmt(cart.getDeltaMovement().y) + "/"
                            + fmt(cart.getDeltaMovement().z));
                })
        );
    }

    @ServerTest("wall flattens a floating cart that climbs into it")
    private static List<Step> wallFlattensClimbingCart() {
        return List.of(
                setup(world -> {
                    singleSlope(world, false);
                    wall(world, 4);
                    entityId = launch(world, EntityType.MINECART, start.relative(dir, -2), 0.4);
                    ticks = 0;
                    seen = 0;
                    speedA = 0;
                }),
                Step.until("cart to hit the wall", 60, () -> hitWall(entityId)),
                Step.until("2 ticks at the wall", 10, () -> ++ticks >= 2),
                Step.run(0, () -> checkFlatAtWall(serverCart(entityId), 4, "climbing"))
        );
    }

    @ServerTest("a wall one block high flattens a floating cart that climbs into it")
    private static List<Step> lowWallFlattensClimbingCart() {
        return List.of(
                setup(world -> {
                    singleSlope(world, false);
                    // hangs in the air, 3 blocks up, nothing above or below it
                    Direction side = dir.getClockWise();
                    BlockPos at = start.relative(dir, 4).above(3);
                    fill(world, at.relative(side, 2), at.relative(side.getOpposite(), 2), Blocks.STONE.defaultBlockState());
                    entityId = launch(world, EntityType.MINECART, start.relative(dir, -2), 0.4);
                    ticks = 0;
                    seen = 0;
                    speedA = 0;
                }),
                Step.until("cart to hit the wall", 60, () -> hitWall(entityId)),
                Step.until("2 ticks at the wall", 10, () -> ++ticks >= 2),
                Step.run(0, () -> checkFlatAtWall(serverCart(entityId), 4, "climbing"))
        );
    }

    @ServerTest("a solid stair path is not a wall for a floating cart")
    private static List<Step> stairPathIsNoWall() {
        return List.of(
                setup(world -> {
                    singleSlope(world, false);
                    Direction side = dir.getClockWise();
                    // treads one block high, the cart bumps into the front of each one
                    for (int k = 2; k <= 9; k++) {
                        fill(world, start.relative(dir, k).relative(side, 2).below(), start.relative(dir, k).relative(side.getOpposite(), 2).above(k - 1),
                                Blocks.STONE.defaultBlockState());
                    }
                    entityId = launch(world, EntityType.MINECART, start.relative(dir, -2), 0.4);
                    ticks = 0;
                    seen = start.getY();
                    speedA = 0;
                    speedB = 0;
                }),
                Step.until("30 ticks on the stairs", 40, () -> {
                    AbstractMinecart cart = serverCart(entityId);
                    seen = Math.max(seen, cart.getY());
                    // the climb must never be levelled out on the way up, even if the cart recovers afterwards
                    if (MinecartFloatingHelper.getIncline(cart) > 0) {
                        speedA = 1;
                    } else if (speedA == 1 && cart.getY() < start.getY() + 5) {
                        check(false, "climb was levelled out on a tread" + describe(cart) + floatState(cart));
                    }
                    if (cart.horizontalCollision) {
                        speedB++;
                    }
                    return ++ticks >= 30;
                }),
                Step.run(0, () -> {
                    AbstractMinecart cart = serverCart(entityId);
                    check(speedB > 0, "setup: the cart never bumped into a tread" + describe(cart) + floatState(cart));
                    check(seen > start.getY() + 5, "cart only climbed to y=" + fmt(seen - start.getY())
                            + " blocks up the stairs" + describe(cart) + floatState(cart));
                })
        );
    }

    @ServerTest("wall flattens a floating cart that dives into it")
    private static List<Step> wallFlattensDivingCart() {
        return List.of(
                setup(world -> {
                    staircase(world, start, true, 6);
                    // a stretch of the stairs is missing: the cart dives through the gap and hits the wall in it
                    for (int i = 1; i <= 3; i++) {
                        BlockPos step = start.relative(dir, i).above(i);
                        world.setBlockAndUpdate(step, Blocks.AIR.defaultBlockState());
                        world.setBlockAndUpdate(step.below(), Blocks.AIR.defaultBlockState());
                    }
                    wall(world, 1);
                    BlockPos top = start.relative(dir, 6).above(6);
                    entityId = launch(world, EntityType.MINECART, top, -0.4);
                    ticks = 0;
                    seen = 0;
                    speedA = 0;
                }),
                Step.until("cart to hit the wall", 60, () -> hitWall(entityId)),
                Step.until("2 ticks at the wall", 10, () -> ++ticks >= 2),
                Step.run(0, () -> checkFlatAtWall(serverCart(entityId), 1, "diving"))
        );
    }

    @ServerTest("a cart floating level against a wall keeps its height")
    private static List<Step> wallLeavesLevelCartAlone() {
        return List.of(
                setup(world -> {
                    track(world, start, "rLrr");
                    wall(world, 5);
                    entityId = launch(world, EntityType.MINECART, start, 0.4);
                    ticks = 0;
                }),
                Step.until("cart to float", 40, () -> serverFloating(entityId)),
                Step.until("40 ticks at the wall", 50, () -> {
                    AbstractMinecart cart = serverCart(entityId);
                    check(Math.abs(cart.getY() - start.getY()) < 0.1, "cart left its height: " + describe(cart) + floatState(cart));
                    return ++ticks >= 40;
                })
        );
    }

    private static boolean hitWall(int id) {
        AbstractMinecart cart = serverCart(id);
        if (cart.horizontalCollision) {
            check(speedA != 0, "setup: the cart reached the wall level: " + describe(cart) + floatState(cart));
            return true;
        }
        speedA = MinecartFloatingHelper.getIncline(cart);
        return false;
    }

    private static void checkFlatAtWall(AbstractMinecart cart, int wallAt, String what) {
        List<String> wrong = new ArrayList<>();
        expect(wrong, MinecartFloatingHelper.isFloating(cart), "the " + what + " cart stopped floating at the wall");
        expect(wrong, MinecartFloatingHelper.getIncline(cart) == 0, "wall hit should clear the incline, was "
                + MinecartFloatingHelper.getIncline(cart));
        expect(wrong, Math.abs(cart.getXRot()) < 2, "wall hit should flatten the pitch, was " + fmt(cart.getXRot()));
        expect(wrong, Math.abs(cart.getDeltaMovement().y) < 0.01, "wall hit should zero dy, was " + fmt(cart.getDeltaMovement().y));
        double face = wallAt > 0 ? wallAt - 0.5 : wallAt + 0.5;
        expect(wrong, Math.abs(along(cart) - face) >= cart.getBbWidth() / 2 - 0.01, "cart is inside the wall at " + fmt(along(cart)));
        if (!wrong.isEmpty()) {
            wrong.add(describe(cart) + floatState(cart));
        }
        checkAll(wrong);
    }

    private static void wall(ServerLevel world, int at) {
        Direction side = dir.getClockWise();
        BlockPos base = start.relative(dir, at);
        fill(world, base.relative(side, 2).below(8), base.relative(side.getOpposite(), 2).above(CEILING_HEIGHT), Blocks.STONE.defaultBlockState());
    }

    @ServerTest("cart launched off a lone slope falls once the climb ends")
    private static List<Step> fallsAfterLoneSlopeClimb() {
        return List.of(
                setup(world -> {
                    BlockPos log = start.relative(dir, -2);
                    world.setBlockAndUpdate(log, Blocks.OAK_LOG.defaultBlockState());
                    world.setBlockAndUpdate(log.above(), Blocks.LEVER.defaultBlockState()
                            .setValue(LeverBlock.FACE, AttachFace.FLOOR)
                            .setValue(LeverBlock.POWERED, true));
                    BlockPos powered = start.relative(dir, -1);
                    RailShape flat = dir.getAxis() == Direction.Axis.X ? RailShape.EAST_WEST : RailShape.NORTH_SOUTH;
                    world.setBlockAndUpdate(powered, Blocks.POWERED_RAIL.defaultBlockState()
                            .setValue(PoweredRailBlock.SHAPE, flat));
                    world.setBlockAndUpdate(start.relative(dir), Blocks.REDSTONE_BLOCK.defaultBlockState());
                    track(world, start, "L");
                    slope(world, start);
                    entityId = launch(world, EntityType.MINECART, powered, 0);
                    ticks = 0;
                    seen = start.getY();
                    speedA = 0;
                }),
                Step.until("cart to climb and then fall", 300, () -> {
                    AbstractMinecart cart = serverCart(entityId);
                    double y = cart.getY();
                    if (y > seen) {
                        seen = y;
                        speedA = 0;
                    } else if (seen > start.getY() + 3 && MinecartFloatingHelper.isFloating(cart)
                            && Math.abs(cart.getDeltaMovement().y) < 0.02) {
                        speedA++;
                    }
                    check(speedA < 6, "cart coasted level for " + (int) speedA + " ticks after the climb ended"
                            + describe(cart) + floatState(cart));
                    return seen > start.getY() + 3 && y < seen - 4;
                }),
                Step.run(0, () -> {
                    AbstractMinecart cart = serverCart(entityId);
                    check(!MinecartFloatingHelper.isFloating(cart) || cart.getDeltaMovement().y < 0,
                            "cart is not falling" + describe(cart) + floatState(cart));
                })
        );
    }

    @ServerTest("cart floating down a slope stays above the ground")
    private static List<Step> descendingCartStaysAboveGround() {
        return List.of(
                setup(world -> {
                    singleSlope(world, true);
                    entityId = launch(world, EntityType.MINECART, start.relative(dir).above(), -0.4);
                    ticks = 0;
                    seen = 0;
                }),
                Step.until("40 ticks after the slope", 60, () -> {
                    AbstractMinecart cart = serverCart(entityId);
                    ServerLevel world = level();
                    seen = Math.min(seen, along(cart));
                    check(cart.getY() >= start.getY() - 0.1, "cart sank to y=" + fmt(cart.getY()) + describe(cart) + floatState(cart));
                    check(world.getBlockState(cart.blockPosition()).getCollisionShape(world, cart.blockPosition()).isEmpty(),
                            "cart is inside " + world.getBlockState(cart.blockPosition()).getBlock().getName().getString() + describe(cart) + floatState(cart));
                    return ++ticks >= 40;
                }),
                Step.run(0, () -> {
                    AbstractMinecart cart = serverCart(entityId);
                    check(seen < -1, "setup: cart never left the bottom of the slope");
                    check(MinecartFloatingHelper.findRailPos(cart) == null, "cart should be off-rail after the slope"
                            + describe(cart) + floatState(cart));
                    check(MinecartFloatingHelper.getIncline(cart) == 0, "floor skim should clear incline"
                            + floatState(cart));
                    check(Math.abs(cart.getXRot()) < 2, "floor skim should flatten pitch"
                            + floatState(cart));
                    check(Math.abs(cart.getDeltaMovement().y) < 0.01, "floor skim should zero dy"
                            + describe(cart) + floatState(cart));
                })
        );
    }

    @ServerTest("cart climbs powered levita slopes as high as plain rail slopes")
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
                Step.until("60 ticks of climbing", 70, () -> {
                    levitaTop = Math.max(levitaTop, serverCart(entityId).getY());
                    plainTop = Math.max(plainTop, serverCart(secondId).getY());
                    return ++ticks >= 60;
                }),
                Step.run(0, () -> check(levitaTop >= plainTop - 0.5, "levita cart rose " + fmt(levitaTop - start.getY())
                        + " blocks, plain cart rose " + fmt(plainTop - start.getY())))
        );
    }

    @ServerTest("chest and furnace minecarts float too")
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

    @ServerTest("float state survives save and load, also from legacy NBT")
    private static Step floatStateSurvivesSaveLoad() {
        return Step.run(0, () -> {
            ServerLevel world = level();
            List<String> wrong = new ArrayList<>();

            Minecart saved = EntityType.MINECART.create(world);
            MinecartFloatingHelper.setFloatTime(saved, 50);
            MinecartFloatingHelper.setIncline(saved, 1);
            MinecartFloatingHelper.setRailShape(saved, RailShape.ASCENDING_SOUTH);
            Minecart loaded = EntityType.MINECART.create(world);
            loaded.load(saved.saveWithoutId(new CompoundTag()));
            expect(wrong, MinecartFloatingHelper.getFloatTime(loaded) == 50, "float time " + MinecartFloatingHelper.getFloatTime(loaded));
            expect(wrong, MinecartFloatingHelper.getIncline(loaded) == 1, "incline " + MinecartFloatingHelper.getIncline(loaded));
            expect(wrong, MinecartFloatingHelper.getRailShape(loaded) == RailShape.ASCENDING_SOUTH, "shape " + MinecartFloatingHelper.getRailShapeName(loaded));

            CompoundTag tag = EntityType.MINECART.create(world).saveWithoutId(new CompoundTag());
            CompoundTag legacy = new CompoundTag();
            legacy.putBoolean("floating", true);
            legacy.putInt("floatTime", 30);
            legacy.putInt("floatIncline", -1);
            legacy.putString("floatShape", "ascending_north");
            tag.put("paradiseLostFloating", legacy);
            Minecart old = EntityType.MINECART.create(world);
            old.load(tag);
            expect(wrong, MinecartFloatingHelper.getFloatTime(old) == 30, "legacy float time " + MinecartFloatingHelper.getFloatTime(old));
            expect(wrong, MinecartFloatingHelper.getIncline(old) == -1, "legacy incline " + MinecartFloatingHelper.getIncline(old));
            expect(wrong, MinecartFloatingHelper.getRailShape(old) == RailShape.ASCENDING_NORTH, "legacy shape " + MinecartFloatingHelper.getRailShapeName(old));
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
            expect(wrong, MinecartFloatingHelper.getFloatTime(fromCca) == 42, "cca float time " + MinecartFloatingHelper.getFloatTime(fromCca));
            expect(wrong, !fromCca.saveWithoutId(new CompoundTag()).contains("cardinal_components"), "re-save still has CCA float");
            checkAll(wrong);
        });
    }

    // ---- #951: the distance a cart climbs must grow with the number of rails ----

    private static final int LONG_RUN = 40;
    private static final int SHORT_RUN = 12;

    @ServerTest("cart climbs to the top of a long run of powered levita slopes (#951)")
    private static List<Step> climbsLongRunToTheTop() {
        return List.of(
                setup(world -> {
                    staircase(world, start, true, LONG_RUN);
                    entityId = launch(world, EntityType.MINECART, start, 0.4);
                    seen = 0;
                }),
                Step.until("cart to reach the top of the run", 400, () -> {
                    AbstractMinecart cart = serverCart(entityId);
                    seen = Math.max(seen, along(cart));
                    if (seen < 1) {
                        return false;
                    }
                    check(Math.abs(side(cart)) < 0.4, "cart drifted off the run" + describe(cart) + floatState(cart));
                    check(MinecartFloatingHelper.isFloating(cart) || along(cart) > LONG_RUN - 1,
                            "cart stopped floating after " + fmt(seen) + " of " + LONG_RUN + " rails" + describe(cart) + floatState(cart));
                    check(cart.getY() > start.getY() + along(cart) - 2.5,
                            "cart left the run after " + fmt(seen) + " of " + LONG_RUN + " rails" + describe(cart) + floatState(cart));
                    return along(cart) > LONG_RUN - 0.5;
                }),
                Step.run(0, () -> {
                    AbstractMinecart cart = serverCart(entityId);
                    check(cart.getY() > start.getY() + LONG_RUN - 1.5, "cart is too low at the top: " + describe(cart) + floatState(cart));
                })
        );
    }

    private static final int[] launchIds = new int[2];
    private static final int[] launchRails = {0, 4};

    /**
     * Every powered rail a cart passes adds a float charge, so a cart that rolled over four rails before it reaches a
     * launch slope has about five times the float of a cart that only touches the slope. The climb after the slope has
     * to use that charge instead of ending after a fixed distance.
     */
    @ServerTest("distance flown up a launch slope grows with the number of rails passed before it (#951)")
    private static List<Step> launchDistanceGrowsWithRailCount() {
        return List.of(
                setup(world -> {
                    for (int lane = 0; lane < launchIds.length; lane++) {
                        BlockPos lane0 = start.relative(dir.getClockWise(), 3 * lane);
                        int rails = launchRails[lane];
                        fill(world, lane0.relative(dir, -2).below(), lane0.relative(dir, rails + 1).below(), Blocks.STONE.defaultBlockState());
                        track(world, lane0.relative(dir, -2), "rr" + "L".repeat(rails) + "L");
                        world.setBlockAndUpdate(lane0.relative(dir, rails + 1), Blocks.STONE.defaultBlockState());
                        slope(world, lane0.relative(dir, rails));
                        launchIds[lane] = launch(world, EntityType.MINECART, lane0.relative(dir, -2), 0.4);
                    }
                    levitaTop = plainTop = start.getY();
                    ticks = 0;
                }),
                Step.until("150 ticks of flight", 160, () -> {
                    plainTop = Math.max(plainTop, serverCart(launchIds[0]).getY());
                    levitaTop = Math.max(levitaTop, serverCart(launchIds[1]).getY());
                    return ++ticks >= 150;
                }),
                Step.run(0, () -> {
                    double few = plainTop - start.getY();
                    double many = levitaTop - start.getY();
                    check(few > 3, "setup: the cart that only touched the launch slope rose just " + fmt(few));
                    check(many > few + 10, "cart that passed " + launchRails[1] + " rails before the slope rose " + fmt(many)
                            + " blocks, cart that passed " + launchRails[0] + " rose " + fmt(few) + ", the extra rails made no difference");
                })
        );
    }

    @ServerTest("ridden cart climbs to the top of a long run of powered levita slopes (#951)")
    private static List<Step> riddenCartClimbsLongRun() {
        return longRunSteps(0.4, 1, true);
    }

    @ServerTest("slow cart climbs to the top of a long run of powered levita slopes (#951)")
    private static List<Step> slowCartClimbsLongRun() {
        return longRunSteps(0.15, 1, false);
    }

    @ServerTest("cart climbs to the top of a long run of slopes powered by one redstone block per 8 rails (#951)")
    private static List<Step> climbsLongRunWithSharedPower() {
        return longRunSteps(0.4, 8, false);
    }

    @ServerTest("cart floating in on flat levita rails climbs to the top of a long run of powered levita slopes (#951)")
    private static List<Step> floatingCartClimbsLongRun() {
        return longRunSteps(0.4, 1, false, true);
    }

    private static List<Step> longRunSteps(double speed, int powerEvery, boolean rider) {
        return longRunSteps(speed, powerEvery, rider, false);
    }

    private static List<Step> longRunSteps(double speed, int powerEvery, boolean rider, boolean floatingApproach) {
        return List.of(
                setup(world -> {
                    for (int i = 0; i < LONG_RUN; i++) {
                        BlockPos at = start.relative(dir, i).above(i);
                        world.setBlockAndUpdate(at.below(), i % powerEvery == 0
                                ? Blocks.REDSTONE_BLOCK.defaultBlockState()
                                : Blocks.STONE.defaultBlockState());
                        world.setBlockAndUpdate(at, BlockRegistry.LEVITA_RAIL.get().defaultBlockState());
                    }
                    BlockPos top = start.relative(dir, LONG_RUN).above(LONG_RUN);
                    fill(world, top.below(), top.relative(dir).below(), Blocks.STONE.defaultBlockState());
                    track(world, top, "rr");
                    track(world, start.relative(dir, -6), floatingApproach ? "rLLrrr" : "rrrrrr");
                    for (int i = 0; i < LONG_RUN; i++) {
                        slope(world, start.relative(dir, i).above(i));
                    }
                    entityId = launch(world, EntityType.MINECART, floatingApproach
                            ? start.relative(dir, -6)
                            : start, speed);
                    if (rider) {
                        Entity passenger = EntityType.VILLAGER.create(world);
                        passenger.moveTo(world.getEntity(entityId).position());
                        world.addFreshEntity(passenger);
                        check(passenger.startRiding(world.getEntity(entityId), true), "setup: startRiding returned false");
                    }
                    seen = 0;
                }),
                Step.until("cart to reach the top of the run", 600, () -> {
                    AbstractMinecart cart = serverCart(entityId);
                    seen = Math.max(seen, along(cart));
                    if (seen < 1) {
                        return false;
                    }
                    check(Math.abs(side(cart)) < 0.4, "cart drifted off the run" + describe(cart) + floatState(cart));
                    check(MinecartFloatingHelper.isFloating(cart) || along(cart) > LONG_RUN - 1,
                            "cart stopped floating after " + fmt(seen) + " of " + LONG_RUN + " rails" + describe(cart) + floatState(cart));
                    check(cart.getY() > start.getY() + along(cart) - 2.5,
                            "cart left the run after " + fmt(seen) + " of " + LONG_RUN + " rails" + describe(cart) + floatState(cart));
                    return along(cart) > LONG_RUN - 0.5;
                })
        );
    }

    @ServerTest("a longer run of powered levita slopes takes the cart proportionally higher (#951)")
    private static List<Step> climbDistanceGrowsWithRailCount() {
        return List.of(
                setup(world -> {
                    staircase(world, start, true, SHORT_RUN);
                    staircase(world, start.relative(dir.getClockWise(), 3), true, LONG_RUN);
                    entityId = launch(world, EntityType.MINECART, start, 0.4);
                    secondId = launch(world, EntityType.MINECART, start.relative(dir.getClockWise(), 3), 0.4);
                    levitaTop = plainTop = start.getY();
                    ticks = 0;
                }),
                Step.until("200 ticks of climbing", 210, () -> {
                    levitaTop = Math.max(levitaTop, serverCart(secondId).getY());
                    plainTop = Math.max(plainTop, serverCart(entityId).getY());
                    return ++ticks >= 200;
                }),
                Step.run(0, () -> {
                    double short_ = plainTop - start.getY();
                    double long_ = levitaTop - start.getY();
                    check(short_ > SHORT_RUN - 1.5, "cart on the " + SHORT_RUN + " rail run only rose " + fmt(short_));
                    check(long_ > short_ + (LONG_RUN - SHORT_RUN) - 3, "cart on the " + LONG_RUN + " rail run rose " + fmt(long_)
                            + " blocks, cart on the " + SHORT_RUN + " rail run rose " + fmt(short_)
                            + ", the extra rails made no difference");
                })
        );
    }

    // ---- #952: carts follow a downward levita slope even when no rail is waiting at the bottom ----

    private static final int DESCENT_HEIGHT = 8;

    @ServerTest("cart keeps diving after a single downward levita slope with no rail beyond it (#952)")
    private static List<Step> descendsSingleSlopeWithoutRailAhead() {
        return descentSteps(1, false);
    }

    @ServerTest("cart keeps diving after a run of downward levita slopes with no rail beyond it (#952)")
    private static List<Step> descendsSlopeRunWithoutRailAhead() {
        return descentSteps(4, false);
    }

    @ServerTest("cart follows a run of downward levita slopes onto a rail beyond it (#952)")
    private static List<Step> descendsSlopeRunWithRailAhead() {
        return descentSteps(4, true);
    }

    /**
     * A cart that leaves the top of a run of downward slopes follows them. Once the last slope is behind it, a climbing
     * cart keeps climbing in open air, so a descending one has to keep descending, with or without a rail ahead to land
     * on. With a rail ahead it just has to land there.
     */
    private static List<Step> descentSteps(int slopes, boolean railAhead) {
        List<Step> steps = new ArrayList<>();
        steps.addAll(List.of(
                setup(world -> {
                    BlockPos base = start.above(DESCENT_HEIGHT);
                    for (int i = 0; i < slopes; i++) {
                        placePoweredLevitaRail(world, base.relative(dir, i).above(i));
                    }
                    BlockPos top = base.relative(dir, slopes).above(slopes);
                    fill(world, top.below(), top.relative(dir, 3).below(), Blocks.STONE.defaultBlockState());
                    track(world, top, "LLL");
                    for (int i = 0; i < slopes; i++) {
                        slope(world, base.relative(dir, i).above(i));
                    }
                    if (railAhead) {
                        fill(world, base.relative(dir, -3).below(), base.relative(dir, -1).below(), Blocks.STONE.defaultBlockState());
                        track(world, base.relative(dir, -3), "rrr");
                    }
                    entityId = launch(world, EntityType.MINECART, top.relative(dir, 2), -0.4);
                    seen = Double.NaN;
                    ticks = 0;
                }),
                Step.until("cart to pass the lowest slope", 150, () -> {
                    AbstractMinecart cart = serverCart(entityId);
                    double a = along(cart);
                    if (a > -0.5 && a < slopes - 0.5) {
                        double railY = start.getY() + DESCENT_HEIGHT + a + 0.5;
                        check(Math.abs(cart.getY() - railY) < 1.2, "cart left the slope at along=" + fmt(a)
                                + ", the slope is at y=" + fmt(railY) + describe(cart) + floatState(cart));
                    }
                    if (a < -0.6) {
                        seen = cart.getY();
                    }
                    return a < -0.6;
                }),
                Step.run(0, () -> {
                    AbstractMinecart cart = serverCart(entityId);
                    check(seen < start.getY() + DESCENT_HEIGHT + 0.7, "cart was still at y=" + fmt(seen) + " after the last slope, it should be at about y="
                            + (start.getY() + DESCENT_HEIGHT) + describe(cart) + floatState(cart));
                    ticks = 0;
                })
        ));
        if (!railAhead) {
            steps.add(Step.until("cart to keep diving below the last slope", 40, () -> {
                AbstractMinecart cart = serverCart(entityId);
                check(cart.getY() > start.getY() - 0.5, "cart hit the ground" + describe(cart) + floatState(cart));
                return cart.getY() < start.getY() + DESCENT_HEIGHT - 4;
            }));
        }
        return steps;
    }

    // ---- #953: a floating cart must not snap to rails that merely lie below its path ----

    private static final String[] GHOST_LANES = {"parallel flat rail", "crossing flat rail", "rail rising ahead", "rail falling ahead", "curve"};
    private static final int[] ghostIds = new int[GHOST_LANES.length];
    private static final boolean[] ghostFailed = new boolean[GHOST_LANES.length];
    private static final List<String> ghostWrong = new ArrayList<>();

    private static RailShape ghostShape(int lane) {
        boolean x = dir.getAxis() == Direction.Axis.X;
        return switch (lane) {
            case 0 -> x ? RailShape.EAST_WEST : RailShape.NORTH_SOUTH;
            case 1 -> x ? RailShape.NORTH_SOUTH : RailShape.EAST_WEST;
            case 2 -> ascending(dir);
            case 3 -> ascending(dir.getOpposite());
            default -> RailShape.SOUTH_EAST;
        };
    }

    @ServerTest("floating cart flies straight and level over rails one block below it (#953)")
    private static List<Step> floatingCartIgnoresRailsBelow() {
        return List.of(
                setup(world -> {
                    ghostWrong.clear();
                    Arrays.fill(ghostFailed, false);
                    for (int lane = 0; lane < GHOST_LANES.length; lane++) {
                        BlockPos lane0 = start.relative(dir.getClockWise(), 3 * lane);
                        fill(world, lane0, lane0.relative(dir, 3), Blocks.STONE.defaultBlockState());
                        track(world, lane0.above(), "rLrr");
                        for (int i = 6; i <= 9; i++) {
                            world.setBlockAndUpdate(lane0.relative(dir, i), Blocks.RAIL.defaultBlockState());
                        }
                        for (int i = 6; i <= 9; i++) {
                            forceShape(world, lane0.relative(dir, i), ghostShape(lane));
                        }
                        ghostIds[lane] = launch(world, EntityType.MINECART, lane0.above(), 0.4);
                    }
                }),
                Step.until("carts to fly over the rails", 150, () -> {
                    boolean done = true;
                    for (int lane = 0; lane < GHOST_LANES.length; lane++) {
                        AbstractMinecart cart = serverCart(ghostIds[lane]);
                        double a = along(cart);
                        done &= a > 12.5;
                        if (a < 5 || a > 12.5 || ghostFailed[lane]) {
                            continue;
                        }
                        String where = "over a " + GHOST_LANES[lane] + " below the path";
                        Vec3 motion = cart.getDeltaMovement().multiply(1, 0, 1);
                        String problem = null;
                        if (!MinecartFloatingHelper.isFloating(cart)) {
                            problem = "cart stopped floating";
                        } else if (Math.abs(cart.getY() - (start.getY() + 1)) > 0.15) {
                            problem = "cart left its flight level";
                        } else if (MinecartFloatingHelper.getIncline(cart) != 0) {
                            problem = "cart tilted";
                        } else if (Math.abs(side(cart) - 3 * lane) > 0.25) {
                            problem = "cart swerved";
                        } else if (motion.lengthSqr() > 1.0E-4 && motion.normalize().dot(Vec3.atLowerCornerOf(dir.getNormal())) < 0.98) {
                            problem = "cart turned";
                        } else {
                            try {
                                checkFloatRotation(cart, "server " + where);
                            } catch (AssertionError e) {
                                problem = e.getMessage();
                            }
                        }
                        if (problem != null) {
                            ghostFailed[lane] = true;
                            ghostWrong.add(GHOST_LANES[lane] + ": " + problem + " " + where + describe(cart) + floatState(cart));
                        }
                    }
                    return done;
                }),
                Step.run(0, () -> checkAll(ghostWrong))
        );
    }

    // ---- #954: a triggered levita rail is off until its delay is over ----

    @ServerTest("a triggered levita rail gives no new charge until its delay is over (#954)")
    private static List<Step> triggeredRailGivesNoCharge() {
        return List.of(
                setup(world -> {
                    track(world, start, "rrLrrr");
                    pos = start.relative(dir, 2);
                    entityId = launch(world, EntityType.MINECART, start, 0.4);
                    ticks = 0;
                }),
                Step.until("rail to be triggered", 40, () -> level().getBlockState(pos).getValue(LevitaRailBlock.TRIGGERED)),
                // let the first cart fly off, it would push the second one away
                Step.until("first cart to fly on", 40, () -> along(serverCart(entityId)) > 5),
                Step.run(0, () -> secondId = launch(level(), EntityType.MINECART, pos, 0)),
                Step.until("20 ticks on the triggered rail", 30, () -> {
                    check(level().getBlockState(pos).getValue(LevitaRailBlock.TRIGGERED), "setup: the rail was reset too early");
                    AbstractMinecart cart = serverCart(secondId);
                    check(!MinecartFloatingHelper.isFloating(cart), "a triggered rail charged a cart that stood on it" + describe(cart) + floatState(cart));
                    return ++ticks >= 20;
                }),
                // the rail resets after 80 ticks and charges the cart that is still standing on it
                Step.until("cart to float again once the rail has reset", 100, () -> serverFloating(secondId))
        );
    }

    @ServerTest("a cart whose float ran out is not recharged by a triggered rail (#954)")
    private static List<Step> triggeredRailDoesNotRechargeSpentCart() {
        return List.of(
                setup(world -> {
                    track(world, start, "rrLrrr");
                    pos = start.relative(dir, 2);
                    entityId = launch(world, EntityType.MINECART, start, 0.4);
                    ticks = 0;
                }),
                Step.until("rail to be triggered", 40, () -> level().getBlockState(pos).getValue(LevitaRailBlock.TRIGGERED)),
                Step.run(0, () -> {
                    // the cart has used its charge, it now rolls back over the rail that is still off
                    AbstractMinecart cart = serverCart(entityId);
                    MinecartFloatingHelper.stopFloating(cart);
                    cart.setPos(Vec3.atBottomCenterOf(pos));
                    cart.setDeltaMovement(Vec3.ZERO);
                }),
                Step.until("20 ticks on the triggered rail", 30, () -> {
                    check(level().getBlockState(pos).getValue(LevitaRailBlock.TRIGGERED), "setup: the rail was reset too early");
                    AbstractMinecart cart = serverCart(entityId);
                    check(!MinecartFloatingHelper.isFloating(cart), "a triggered rail recharged a spent cart" + describe(cart) + floatState(cart));
                    return ++ticks >= 20;
                })
        );
    }

    private static Step setup(Consumer<ServerLevel> builder) {
        return Step.run(0, () -> {
            ServerTestRunner.server().tickRateManager().setFrozen(false);
            dir = FACING;
            start = ahead(2, 0);
            activeStairs = STAIRS;
            activeGapAt = -1;
            activeGapLen = 0;
            builder.accept(level());
        });
    }

    private static AbstractMinecart serverCart(int id) {
        AbstractMinecart cart = (AbstractMinecart) level().getEntity(id);
        check(cart != null, "server cart " + id + " is gone");
        return cart;
    }

    private static boolean serverFloating(int id) {
        return MinecartFloatingHelper.isFloating(serverCart(id));
    }

    private static double speedPast(AbstractMinecart cart, double old) {
        return old < 0 && along(cart) > 3.5 ? cart.getDeltaMovement().horizontalDistance() : old;
    }
}
