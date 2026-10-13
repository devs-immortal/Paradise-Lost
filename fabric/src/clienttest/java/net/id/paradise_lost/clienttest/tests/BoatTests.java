package net.id.paradise_lost.clienttest.tests;

import net.id.paradise_lost.clienttest.ClientTest;
import net.id.paradise_lost.clienttest.Step;
import net.id.paradise_lost.registry.EntityRegistry;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static net.id.paradise_lost.clienttest.TestHelpers.*;
import static net.id.paradise_lost.registry.ItemRegistry.*;

public final class BoatTests {
    private static final List<Supplier<? extends EntityType<? extends Boat>>> BOATS = List.of(
            EntityRegistry.AUREL_BOAT, EntityRegistry.MOTHER_AUREL_BOAT, EntityRegistry.MENTH_BOAT, EntityRegistry.WISTERIA_BOAT,
            EntityRegistry.AUREL_CHEST_BOAT, EntityRegistry.MOTHER_AUREL_CHEST_BOAT, EntityRegistry.MENTH_CHEST_BOAT, EntityRegistry.WISTERIA_CHEST_BOAT
    );
    private static final List<String> WOODS = List.of("aurel", "mother_aurel", "menth", "wisteria");

    private static Entity entity;

    private BoatTests() {
    }

    private static EntityType<? extends Boat> boat(int index) {
        return BOATS.get(index).get();
    }

    private static Item boatItem(int index) {
        BoatSet set = BOAT_SETS[index % 4];
        return index < 4 ? set.boat().get() : set.chestBoat().get();
    }

    @ClientTest("boats render")
    private static List<Step> boatsRender() {
        return List.of(
                Step.run(0, () -> {
                    List<String> wrong = new ArrayList<>();
                    for (int i = 0; i < BOATS.size(); i++) {
                        Entity boat = boat(i).create(client().level);
                        String id = EntityType.getKey(boat(i)).toString();

                        EntityRenderer<? super Entity> renderer = client().getEntityRenderDispatcher().getRenderer(boat);
                        if (renderer == null) {
                            wrong.add("no renderer for " + id);
                            continue;
                        }

                        ResourceLocation expected = ResourceLocation.fromNamespaceAndPath("paradise_lost",
                                "textures/entity/" + (i < 4 ? "boat" : "chest_boat") + "/" + WOODS.get(i % 4) + ".png");
                        ResourceLocation texture = renderer.getTextureLocation(boat);
                        if (!texture.equals(expected)) {
                            wrong.add(id + " uses " + texture + " expected " + expected);
                        } else if (client().getResourceManager().getResource(texture).isEmpty()) {
                            wrong.add("missing texture " + texture);
                        }
                    }
                    checkAll(wrong);
                }),
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    ServerLevel world = player.serverLevel();
                    Direction facing = player.getDirection();
                    for (int i = 0; i < BOATS.size(); i++) {
                        Entity boat = boat(i).create(world);
                        Vec3 at = Vec3.atBottomCenterOf(ahead(player, i < 4 ? 6 : 9, (i % 4) * 2 - 3));
                        boat.moveTo(at.x, at.y, at.z, facing.toYRot() + 90, 0);
                        world.addFreshEntity(boat);
                    }
                })),

                Step.run(20, () -> screenshot("boats.png"))
        );
    }

    @ClientTest("player can ride")
    private static List<Step> boatRide() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    ServerLevel world = player.serverLevel();
                    entity = EntityRegistry.MENTH_BOAT.get().create(world);
                    Vec3 at = Vec3.atBottomCenterOf(ahead(player, 2, 0));
                    entity.moveTo(at.x, at.y, at.z, 0, 0);
                    world.addFreshEntity(entity);
                })),
                Step.run(5, () -> onServer(server -> check(player(server).startRiding(entity, true), "startRiding returned false"))),
                Step.until("client player in the boat", 40, () -> {
                    Entity vehicle = client().player.getVehicle();
                    return vehicle != null && vehicle.getType() == EntityRegistry.MENTH_BOAT.get();
                })
        );
    }

    @ClientTest("boats drop their own item and chest contents")
    private static Step boatDrops() {
        return Step.run(0, () -> onServer(server -> {
            ServerPlayer player = player(server);
            ServerLevel world = player.serverLevel();
            List<String> wrong = new ArrayList<>();
            for (int i = 0; i < BOATS.size(); i++) {
                BlockPos boatPos = ahead(player, i < 4 ? 4 : 8, (i % 4) * 3 - 4);
                Boat boat = boat(i).create(world);
                Vec3 at = Vec3.atBottomCenterOf(boatPos);
                boat.moveTo(at.x, at.y, at.z, 0, 0);
                Map<Item, Integer> expected = new LinkedHashMap<>();
                expected.put(boatItem(i), 1);
                if (boat instanceof ChestBoat chest) {
                    chest.setItem(0, new ItemStack(Items.DIAMOND));
                    expected.put(Items.DIAMOND, 1);
                }
                world.addFreshEntity(boat);
                boat.hurt(world.damageSources().playerAttack(player), 100);
                Map<Item, Integer> drops = drops(world, boatPos);
                if (!drops.equals(expected)) {
                    wrong.add(EntityType.getKey(boat(i)) + " dropped " + describe(drops) + " expected " + describe(expected));
                }
            }
            checkAll(wrong);
        }));
    }

    @ClientTest("boats keep type, item and chest contents after relog")
    private static Step boatSaveLoad() {
        return Step.run(0, () -> onServer(server -> {
            ServerLevel world = world(server);
            List<String> wrong = new ArrayList<>();
            for (int i = 0; i < BOATS.size(); i++) {
                Boat boat = boat(i).create(world);
                if (boat instanceof ChestBoat chest) chest.setItem(3, new ItemStack(Items.DIAMOND));
                CompoundTag nbt = new CompoundTag();
                boat.save(nbt);
                Entity loaded = EntityType.create(nbt, world).orElse(null);
                String id = EntityType.getKey(boat(i)).toString();
                if (loaded == null || loaded.getType() != boat(i)) {
                    wrong.add(id + " loaded as " + (loaded == null ? "nothing" : EntityType.getKey(loaded.getType())));
                } else if (!loaded.getPickResult().is(boatItem(i))) {
                    wrong.add(id + " item is " + loaded.getPickResult());
                } else if (loaded instanceof ChestBoat chest && !chest.getItem(3).is(Items.DIAMOND)) {
                    wrong.add(id + " lost its chest contents");
                }
            }
            checkAll(wrong);
        }));
    }
}
