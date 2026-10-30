package net.id.paradise_lost.test.client;

import net.id.paradise_lost.test.Step;
import net.id.paradise_lost.entity.hostile.EnvoyEntity;
import net.id.paradise_lost.entity.hostile.SentinelEntity;
import net.id.paradise_lost.entity.passive.PopomEntity;
import net.id.paradise_lost.entity.passive.QuintEntity;
import net.id.paradise_lost.registry.EntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

import static net.id.paradise_lost.test.TestHelpers.*;
import static net.id.paradise_lost.test.client.ClientTestHelpers.*;
import static net.id.paradise_lost.registry.ItemRegistry.*;

public final class EntityTests {
    private static int entityId;

    private EntityTests() {
    }

    @ClientTest(value = "empty hand takes jelly and shrinks its fur on the client", group = "Popom")
    private static List<Step> popomShears() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    PopomEntity popom = EntityRegistry.POPOM.get().spawn(player.serverLevel(), ahead(player, 2, 0), MobSpawnType.COMMAND);
                    popom.setNoAi(true);
                    popom.setFurSize(3);
                    entityId = popom.getId();
                })),
                Step.until("client to see fur size 3", 20, () -> client().level.getEntity(entityId) instanceof PopomEntity popom && popom.getFurSize() == 3),
                Step.run(0, () -> interact(entityId)),
                Step.until("client to see smaller fur", 20, () -> ((PopomEntity) client().level.getEntity(entityId)).getFurSize() < 3),
                Step.run(5, () -> onServer(server -> {
                    var jelly = drops(world(server), world(server).getEntity(entityId).blockPosition());
                    check(jelly.containsKey(POPOM_JELLY.get()), "popom dropped " + describe(jelly));
                }))
        );
    }

    @ClientTest(value = "enlightened state reaches the client", group = "Envoy")
    private static List<Step> envoyEnlightenSyncs() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    // After dimension tests the overworld chunk can still be loading.
                    server.setDifficulty(Difficulty.EASY, true);
                    ServerPlayer player = player(server);
                    BlockPos at = ahead(player, 4, 0);
                    player.serverLevel().getChunk(at);
                    EnvoyEntity envoy = EntityRegistry.ENVOY.get().spawn(player.serverLevel(), at, MobSpawnType.COMMAND);
                    check(envoy != null, "envoy failed to spawn");
                    envoy.setNoAi(true);
                    envoy.setInvulnerable(true);
                    envoy.clearFire();
                    envoy.setEnlightened(true);
                    entityId = envoy.getId();
                })),
                // Wait for the entity first so a slow spawn doesn't look like a sync failure.
                Step.until("client envoy to spawn", 60, () -> client().level.getEntity(entityId) instanceof EnvoyEntity),
                Step.until("client envoy to be enlightened", 40, () ->
                        ((EnvoyEntity) client().level.getEntity(entityId)).getEnlightened())
        );
    }

    @ClientTest(value = "immune until enlightened then drops quint when killed", group = "Sentinel")
    private static Step sentinelImmuneUntilEnlightened() {
        return Step.run(0, () -> onServer(server -> {
            server.setDifficulty(Difficulty.EASY, true);
            ServerPlayer player = player(server);
            ServerLevel world = player.serverLevel();

            BlockPos at = ahead(player, 3, 0);
            SentinelEntity sentinel = EntityRegistry.SENTINEL.get().spawn(world, at, MobSpawnType.COMMAND);
            float health = sentinel.getHealth();
            sentinel.hurt(world.damageSources().playerAttack(player), 10);
            check(sentinel.getHealth() == health, "dormant sentinel took damage");
            sentinel.setEnlightened(true);
            sentinel.hurt(world.damageSources().playerAttack(player), 1000);
            check(!sentinel.isAlive(), "enlightened sentinel survived");

            List<QuintEntity> quints = world.getEntitiesOfClass(QuintEntity.class, new AABB(at).inflate(3), e -> true);
            check(quints.size() == 1, quints.size() + " quints near the dead sentinel");
        }));
    }

    @ClientTest(value = "captures allay and releases it", group = "Warded jar")
    private static List<Step> wardedJarCapturesAllay() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    entityId = spawnNoAi(player, EntityType.ALLAY, ahead(player, 2, 0));
                    hold(player, WARDED_JAR.get(), 1);
                })),
                Step.run(10, () -> interact(entityId)),
                Step.until("allay gone on the server", 20, () -> fromServer(server -> world(server).getEntity(entityId) == null)),
                Step.run(10, () -> {
                    check(client().level.getEntity(entityId) == null, "client still has the allay");
                    int server = fromServer(s -> player(s).getInventory().countItem(WARDED_JAR_ALLAY.get()));
                    int client = client().player.getInventory().countItem(WARDED_JAR_ALLAY.get());
                    check(server == 1 && client == 1, "allay jars: " + server + " on the server " + client + " on the client");
                    onServer(s -> {
                        ServerPlayer player = player(s);
                        player.getInventory().clearContent();
                        hold(player, WARDED_JAR_ALLAY.get(), 1);
                    });
                }),
                Step.run(10, () -> useOn(fromServer(server -> ahead(player(server), 2, 0).below()))),
                Step.until("allay released", 20, () -> fromServer(server ->
                        !world(server).getEntitiesOfClass(Allay.class, new AABB(ahead(player(server), 2, 0)).inflate(3), e -> true).isEmpty()))
        );
    }

    private static void expectCount(List<String> wrong, Item item, int expected) {
        int server = fromServer(s -> player(s).getInventory().countItem(item));
        int client = client().player.getInventory().countItem(item);
        expect(wrong, server == expected && client == expected,
                BuiltInRegistries.ITEM.getKey(item).getPath() + ": " + server + " on the server " + client + " on the client, expected " + expected);
    }

    @ClientTest(value = "one jar from stack of 3 captures an allay", group = "Warded jar")
    private static List<Step> wardedJarFromStack() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    entityId = spawnNoAi(player, EntityType.ALLAY, ahead(player, 2, 0));
                    hold(player, WARDED_JAR.get(), 3);
                })),
                Step.run(10, () -> interact(entityId)),
                Step.until("allay gone on the server", 20, () -> fromServer(server -> world(server).getEntity(entityId) == null)),
                Step.run(10, () -> {
                    List<String> wrong = new ArrayList<>();
                    expectCount(wrong, WARDED_JAR.get(), 2);
                    expectCount(wrong, WARDED_JAR_ALLAY.get(), 1);
                    checkAll(wrong);
                })
        );
    }

    @ClientTest(value = "the last jar captures a quint", group = "Warded jar")
    private static List<Step> wardedJarCapturesQuint() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    entityId = spawnNoAi(player, EntityRegistry.QUINT.get(), ahead(player, 2, 0));
                    hold(player, WARDED_JAR.get(), 1);
                })),
                Step.run(10, () -> interact(entityId)),
                Step.until("quint gone on the server", 20, () -> fromServer(server -> world(server).getEntity(entityId) == null)),
                Step.run(10, () -> {
                    List<String> wrong = new ArrayList<>();
                    expectCount(wrong, WARDED_JAR.get(), 0);
                    expectCount(wrong, WARDED_JAR_QUINT.get(), 1);
                    checkAll(wrong);
                })
        );
    }

    @ClientTest(value = "creative capture keeps the empty jar", group = "Warded jar")
    private static List<Step> wardedJarCreative() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    player.setGameMode(GameType.CREATIVE);
                    entityId = spawnNoAi(player, EntityType.ALLAY, ahead(player, 2, 0));
                    hold(player, WARDED_JAR.get(), 1);
                })),
                Step.until("client in creative", 20, () -> client().player.isCreative()),
                Step.run(5, () -> interact(entityId)),
                Step.until("allay gone on the server", 20, () -> fromServer(server -> world(server).getEntity(entityId) == null)),
                Step.run(10, () -> {
                    List<String> wrong = new ArrayList<>();
                    expectCount(wrong, WARDED_JAR.get(), 1);
                    expectCount(wrong, WARDED_JAR_ALLAY.get(), 1);
                    checkAll(wrong);
                })
        );
    }

    @ClientTest(value = "the last jar in the off hand captures an allay", group = "Warded jar")
    private static List<Step> wardedJarOffHand() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    entityId = spawnNoAi(player, EntityType.ALLAY, ahead(player, 2, 0));
                    player.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(WARDED_JAR.get()));
                })),
                Step.run(10, () -> client().gameMode.interact(client().player, client().level.getEntity(entityId), InteractionHand.OFF_HAND)),
                Step.until("allay gone on the server", 20, () -> fromServer(server -> world(server).getEntity(entityId) == null)),
                Step.run(10, () -> {
                    ItemStack server = fromServer(s -> player(s).getOffhandItem().copy());
                    ItemStack client = client().player.getOffhandItem();
                    check(server.is(WARDED_JAR_ALLAY.get()) && client.is(WARDED_JAR_ALLAY.get()),
                            "off hand holds " + server + " on the server, " + client + " on the client");
                })
        );
    }

    @ClientTest(value = "with a full inventory the filled jar is dropped", group = "Warded jar")
    private static List<Step> wardedJarFullInventory() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    entityId = spawnNoAi(player, EntityType.ALLAY, ahead(player, 2, 0));
                    Inventory inventory = player.getInventory();
                    for (int i = 1; i < Inventory.INVENTORY_SIZE; i++)
                        inventory.setItem(i, new ItemStack(Items.DIRT, 64));
                    hold(player, WARDED_JAR.get(), 2);
                })),
                Step.run(10, () -> interact(entityId)),
                Step.until("allay gone on the server", 20, () -> fromServer(server -> world(server).getEntity(entityId) == null)),
                Step.run(5, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    check(player.getMainHandItem().is(WARDED_JAR.get()) && player.getMainHandItem().getCount() == 1, "hand holds " + player.getMainHandItem());
                    List<ItemEntity> jars = player.serverLevel().getEntitiesOfClass(ItemEntity.class, player.getBoundingBox().inflate(4),
                            e -> e.getItem().is(WARDED_JAR_ALLAY.get()));
                    check(jars.size() == 1, jars.size() + " allay jars dropped near the player, expected 1");
                    jars.forEach(Entity::discard);
                }))
        );
    }

    @ClientTest(value = "releasing the last filled jar gives back one empty jar", group = "Warded jar")
    private static List<Step> wardedJarReleaseReturnsJar() {
        return List.of(
                Step.run(0, () -> onServer(server -> hold(player(server), WARDED_JAR_ALLAY.get(), 1))),
                Step.run(10, () -> useOn(fromServer(server -> ahead(player(server), 2, 0).below()))),
                Step.until("allay released", 20, () -> fromServer(server ->
                        !world(server).getEntitiesOfClass(Allay.class, new AABB(ahead(player(server), 2, 0)).inflate(3), e -> true).isEmpty())),
                Step.run(10, () -> {
                    List<String> wrong = new ArrayList<>();
                    expectCount(wrong, WARDED_JAR.get(), 1);
                    expectCount(wrong, WARDED_JAR_ALLAY.get(), 0);
                    checkAll(wrong);
                })
        );
    }

    @ClientTest(value = "every Paradise Lost entity renders", group = "Entities")
    private static List<Step> entityShowroom() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    server.setDifficulty(Difficulty.EASY, true);
                    ServerPlayer player = player(server);
                    ServerLevel world = player.serverLevel();
                    List<String> failed = new ArrayList<>();
                    int i = 0;
                    for (EntityType<?> type : BuiltInRegistries.ENTITY_TYPE) {
                        if (!EntityType.getKey(type).getNamespace().equals("paradise_lost"))
                            continue;

                        Entity entity = type.create(world);
                        if (entity == null) {
                            failed.add(EntityType.getKey(type).toString());
                            continue;
                        }

                        Vec3 at = Vec3.atBottomCenterOf(ahead(player, 6 + (i / 6) * 3, (i % 6) * 3 - 8));
                        entity.moveTo(at.x, at.y, at.z, player.getYRot() + 180, 0);
                        entity.setNoGravity(true);

                        if (entity instanceof Mob mob)
                            mob.setNoAi(true);

                        world.addFreshEntity(entity);
                        i++;
                    }
                    check(failed.isEmpty(), "could not create " + failed);
                })),
                Step.run(20, () -> screenshot("entities.png"))
        );
    }
}
