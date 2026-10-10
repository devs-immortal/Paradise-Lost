package net.id.paradise_lost.clienttest.tests;

import net.id.paradise_lost.attachments.MinecartFloating;
import net.id.paradise_lost.clienttest.Step;
import net.id.paradise_lost.clienttest.Test;
import net.id.paradise_lost.entity.ParadiseLostEntityExtensions;
import net.id.paradise_lost.entity.block.FloatingBlockEntity;
import net.id.paradise_lost.entity.projectile.LevitaArrow;
import net.id.paradise_lost.item.tool.base_tools.GravityTool;
import net.id.paradise_lost.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static net.id.paradise_lost.clienttest.TestHelpers.*;
import static net.id.paradise_lost.registry.ItemRegistry.*;

public final class LevitaTests {
    private static final String GROUP = "Levita";
    private static BlockPos pos;
    private static int entityId;
    private static double startY;

    private LevitaTests() {
    }

    public static List<Test> all() {
        return List.of(
                levitaFloats(),
                levitatorNeedsPower(),
                levitaWandFloatsBlock(),
                levitaWandFlipsEntity(),
                levitaRailFloatsMinecart(),
                levitaArrowRises()
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
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    ServerLevel world = player.serverLevel();
                    Direction facing = player.getDirection();
                    pos = ahead(player, 2, 0);
                    for (int i = 0; i < 4; i++) {
                        BlockPos rail = pos.relative(facing, i);
                        world.setBlockAndUpdate(rail.below(), i == 1
                                ? Blocks.REDSTONE_BLOCK.defaultBlockState()
                                : Blocks.STONE.defaultBlockState());
                        BlockState state = i == 1
                                ? BlockRegistry.LEVITA_RAIL.get().defaultBlockState()
                                : Blocks.RAIL.defaultBlockState();
                        world.setBlockAndUpdate(rail, state);
                    }
                    Minecart cart = EntityType.MINECART.create(world);
                    cart.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0);
                    Vec3 push = Vec3.atLowerCornerOf(facing.getNormal()).scale(0.4);
                    cart.setDeltaMovement(push);
                    world.addFreshEntity(cart);
                    entityId = cart.getId();
                })),
                Step.until("cart to float on the server", 60, () -> fromServer(server ->
                        world(server).getEntity(entityId) instanceof AbstractMinecart cart && MinecartFloating.isFloating(cart))),
                Step.until("cart to float on the client", 20, () ->
                        client().level.getEntity(entityId) instanceof AbstractMinecart cart && MinecartFloating.isFloating(cart))
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
                    check(
                            arrow != null && arrow.getY() > startY + 0.5,
                            "arrow went from y=" + startY + " to y=" + (arrow == null ? "null" : arrow.getY())
                    );
                })
        );
    }

    private static Entity levitaArrow() {
        List<LevitaArrow> arrows = client().level.getEntitiesOfClass(LevitaArrow.class, client().player.getBoundingBox().inflate(40), e -> true);
        return arrows.isEmpty() ? null : arrows.getFirst();
    }
}
