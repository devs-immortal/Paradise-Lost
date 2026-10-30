package net.id.paradise_lost.test.server;

import net.id.paradise_lost.test.Step;
import net.id.paradise_lost.util.MinecartFloatingHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.entity.vehicle.MinecartFurnace;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.RailShape;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static net.id.paradise_lost.test.LevitaTrack.*;
import static net.id.paradise_lost.test.TestHelpers.*;
import static net.id.paradise_lost.test.server.ServerTestHelpers.*;

public final class LevitaTests {
    private static final int CEILING_HEIGHT = 10;

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

    @ServerTest("a second cart right behind the first also floats")
    private static List<Step> secondCartFloats() {
        return List.of(
                setup(world -> {
                    track(world, start, "rrLrrrrrrr");
                    entityId = launch(world, EntityType.MINECART, start, 0.4);
                }),
                Step.until("first cart to float", 40, () -> serverFloating(entityId)),
                Step.run(20, () -> secondId = launch(level(), EntityType.MINECART, start, 0.4)),
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
