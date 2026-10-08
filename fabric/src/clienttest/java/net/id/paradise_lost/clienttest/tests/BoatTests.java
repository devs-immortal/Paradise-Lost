package net.id.paradise_lost.clienttest.tests;

import net.id.paradise_lost.clienttest.Step;
import net.id.paradise_lost.clienttest.Test;
import net.id.paradise_lost.registry.EntityRegistry;
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
    private static final String GROUP = "Boat";
    private static final List<Supplier<? extends EntityType<? extends Boat>>> BOATS = List.of(
            EntityRegistry.AUREL_BOAT, EntityRegistry.MOTHER_AUREL_BOAT, EntityRegistry.MENTH_BOAT, EntityRegistry.WISTERIA_BOAT,
            EntityRegistry.AUREL_CHEST_BOAT, EntityRegistry.MOTHER_AUREL_CHEST_BOAT, EntityRegistry.MENTH_CHEST_BOAT, EntityRegistry.WISTERIA_CHEST_BOAT
    );

    private static Entity entity;

    private BoatTests() {
    }

    public static List<Test> all() {
        return List.of(
                boatsRender(),
                boatRide(),
                boatDrops(),
                boatSaveLoad()
        );
    }

    private static EntityType<? extends Boat> boat(int index) {
        return BOATS.get(index).get();
    }

    private static Item boatItem(int index) {
        BoatSet set = BOAT_SETS[index % 4];
        return index < 4 ? set.boat().get() : set.chestBoat().get();
    }

    private static Test boatsRender() {
        return new Test(GROUP, "boats render",
                Step.run(0, () -> {
                    List<String> missing = new ArrayList<>();
                    for (int i = 0; i < BOATS.size(); i++) {
                        Entity boat = boat(i).create(client().level);
                        if (client().getEntityRenderDispatcher().getRenderer(boat) == null) {
                            missing.add(EntityType.getKey(boat(i)).toString());
                        }
                    }
                    check(missing.isEmpty(), "no renderer for " + missing);
                }),

                Step.run(0, () -> {
                    List<ResourceLocation> missing = new ArrayList<>();
                    for (String folder : List.of("boat", "chest_boat")) {
                        for (String wood : List.of("aurel", "mother_aurel", "menth", "wisteria")) {
                            ResourceLocation texture = ResourceLocation.fromNamespaceAndPath("paradise_lost", "textures/entity/" + folder + "/" + wood + ".png");
                            if (client().getResourceManager().getResource(texture).isEmpty()) missing.add(texture);
                        }
                    }
                    check(missing.isEmpty(), "missing textures " + missing);
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

    private static Test boatRide() {
        return new Test(GROUP, "player can ride",
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

    private static Test boatDrops() {
        return new Test(GROUP, "boats drop their own item and chest contents", Step.run(0, () -> onServer(server -> {
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
        })));
    }

    private static Test boatSaveLoad() {
        return new Test(GROUP, "boats keep type, item and chest contents after relog", Step.run(0, () -> onServer(server -> {
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
        })));
    }
}
