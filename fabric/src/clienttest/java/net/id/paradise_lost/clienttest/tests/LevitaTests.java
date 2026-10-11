package net.id.paradise_lost.clienttest.tests;

import net.id.paradise_lost.attachments.MinecartFloating;
import net.id.paradise_lost.block.mechanical.LevitaRailBlock;
import net.id.paradise_lost.clienttest.Step;
import net.id.paradise_lost.clienttest.Test;
import net.id.paradise_lost.entity.ParadiseLostEntityExtensions;
import net.id.paradise_lost.entity.block.FloatingBlockEntity;
import net.id.paradise_lost.entity.projectile.LevitaArrow;
import net.id.paradise_lost.item.tool.base_tools.GravityTool;
import net.id.paradise_lost.registry.BlockRegistry;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import static net.id.paradise_lost.clienttest.TestHelpers.*;
import static net.id.paradise_lost.registry.ItemRegistry.*;

public final class LevitaTests {
    private static final String GROUP = "Levita";
    private static final int CHARGE = 82;
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

    private LevitaTests() {
    }

    public static List<Test> all() {
        return List.of(
                levitaFloats(),
                levitatorNeedsPower(),
                levitaWandFloatsBlock(),
                levitaWandFlipsEntity(),
                levitaRailFloatsMinecart(),
                levitaArrowRises(),
                railGivesOneCharge(),
                floatTimeStacks(),
                secondCartFloats(),
                triggerLockClears(),
                standingCartStays(),
                railNeitherBoostsNorBrakes(),
                unpoweredRailEndsFloat()
        );
    }

    private static boolean clientSeesFloating(Block block) {
        return !client().level.getEntitiesOfClass(FloatingBlockEntity.class, new AABB(pos).inflate(2, 30, 2),
                e -> e.getBlockState().is(block)).isEmpty();
    }

    private static Test levitaFloats() {
        return new Test(GROUP, "levita floats up and the client sees the floating block",
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    pos = ahead(player, 3, 0);
                    player.serverLevel().setBlockAndUpdate(pos, BlockRegistry.LEVITA.get().defaultBlockState());
                })),
                Step.until("levita to float on the server", 20, () -> fromServer(server -> world(server).getBlockState(pos).isAir())),
                Step.until("client to see the floating levita", 20, () -> clientSeesFloating(BlockRegistry.LEVITA.get()))
        );
    }

    private static Test levitatorNeedsPower() {
        return new Test(GROUP, "levitator floats only with redstone power",
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

    private static Test levitaWandFloatsBlock() {
        return new Test(GROUP, "wand floats a block and loses durability",
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

    private static Test levitaWandFlipsEntity() {
        return new Test(GROUP, "gravity flipped by the wand reaches the client",
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

    private static Test levitaRailFloatsMinecart() {
        return new Test(GROUP, "powered levita rail makes a minecart float",
                setup(world -> {
                    track(world, start, "rLrr");
                    entityId = launch(world, EntityType.MINECART, start, 0.4);
                }),
                Step.until("cart to float on the server", 60, () -> serverFloating(entityId)),
                Step.until("cart to float on the client", 20, () -> clientFloating(entityId))
        );
    }

    private static Test levitaArrowRises() {
        return new Test(GROUP, "arrow shot from a bow rises on the client",
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

    private static Test railGivesOneCharge() {
        return new Test(GROUP, "one powered rail gives one float charge that runs out on both sides",
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

    private static Test floatTimeStacks() {
        return new Test(GROUP, "float time stacks over a run of powered rails",
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

    private static Test secondCartFloats() {
        return new Test(GROUP, "a second cart right behind the first also floats",
                setup(world -> {
                    track(world, start, "rrLrrrrrrr");
                    entityId = launch(world, EntityType.MINECART, start, 0.4);
                }),
                Step.until("first cart to float", 40, () -> serverFloating(entityId)),
                Step.run(20, () -> onServer(server -> secondId = launch(world(server), EntityType.MINECART, start, 0.4))),
                Step.until("second cart to float", 40, () -> serverFloating(secondId))
        );
    }

    private static Test triggerLockClears() {
        return new Test(GROUP, "rail trigger shows on the client and clears again",
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

    private static Test standingCartStays() {
        return new Test(GROUP, "a standing cart on a powered rail floats in place",
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

    private static Test railNeitherBoostsNorBrakes() {
        return new Test(GROUP, "levita rails neither boost nor brake a cart",
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

    private static Test unpoweredRailEndsFloat() {
        return new Test(GROUP, "an unpowered levita rail ends the float",
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
