package net.id.paradise_lost.test.client;

import net.id.paradise_lost.block.blockentity.FoodBowlBlockEntity;
import net.id.paradise_lost.block.mechanical.FoodBowlBlock;
import net.id.paradise_lost.test.Step;
import net.id.paradise_lost.entity.passive.moa.MoaEntity;
import net.id.paradise_lost.registry.BlockRegistry;
import net.id.paradise_lost.registry.EntityRegistry;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static net.id.paradise_lost.test.TestHelpers.*;
import static net.id.paradise_lost.test.client.ClientTestHelpers.*;

public final class FoodBowlTests {
    private static BlockPos pos;

    private FoodBowlTests() {
    }

    private static boolean clientBowlFull(boolean full) {
        BlockState state = client().level.getBlockState(pos);
        return state.is(BlockRegistry.FOOD_BOWL.get()) && state.getValue(FoodBowlBlock.FULL) == full;
    }

    private static FoodBowlBlockEntity serverBowl(IntegratedServer server) {
        return (FoodBowlBlockEntity) world(server).getBlockEntity(pos);
    }

    private static void placeEmptyBowl(ServerPlayer player) {
        pos = ahead(player, 2, 0);
        player.serverLevel().setBlockAndUpdate(pos, BlockRegistry.FOOD_BOWL.get().defaultBlockState());
    }

    private static FoodBowlBlockEntity placeBowl(ServerPlayer player, Item food, int count) {
        placeEmptyBowl(player);
        var bowl = (FoodBowlBlockEntity) player.serverLevel().getBlockEntity(pos);
        hold(player, food, count);
        bowl.handleUse(player, InteractionHand.MAIN_HAND, player.getMainHandItem(), true);
        return bowl;
    }

    @ClientTest("right click with meat fills it")
    private static List<Step> foodBowlFills() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    placeEmptyBowl(player);
                    hold(player, Items.BEEF, 3);
                })),
                Step.run(10, () -> useOn(pos)),
                Step.until("client bowl to look full", 20, () -> clientBowlFull(true)),
                Step.run(5, () -> onServer(server -> {
                    ItemStack stored = ((FoodBowlBlockEntity) world(server).getBlockEntity(pos)).getContainedItem();
                    check(stored.is(Items.BEEF) && stored.getCount() == 1, "bowl holds " + stored);

                    int hand = player(server).getMainHandItem().getCount();
                    check(hand == 2, "hand holds " + hand + " beef, expected 2");
                }))
        );
    }

    @ClientTest("more of the same meat adds to the stack")
    private static List<Step> foodBowlTopsUp() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    placeEmptyBowl(player);
                    hold(player, Items.BEEF, 3);
                })),
                Step.run(10, () -> useOn(pos)),
                Step.run(10, () -> onServer(server -> hold(player(server), Items.BEEF, 5))),
                Step.run(10, () -> useOn(pos)),
                Step.run(10, () -> onServer(server -> {
                    ItemStack stored = ((FoodBowlBlockEntity) world(server).getBlockEntity(pos)).getContainedItem();
                    check(stored.is(Items.BEEF) && stored.getCount() == 2, "bowl holds " + stored + " after 2 clicks, expected 2");
                }))
        );
    }

    @ClientTest("topping up to 64 keeps the rest in the hand")
    private static List<Step> foodBowlOverflow() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    placeBowl(player, Items.BEEF, 63);
                    hold(player, Items.BEEF, 10);
                })),
                Step.until("client to hold 10 beef", 20, () -> client().player.getMainHandItem().getCount() == 10),
                Step.run(0, () -> useOn(pos)),
                Step.run(10, () -> {
                    int clientHand = client().player.getMainHandItem().getCount();
                    onServer(server -> {
                        ItemStack stored = serverBowl(server).getContainedItem();
                        int hand = player(server).getMainHandItem().getCount();
                        check(stored.getCount() == 64 && hand == 9, "bowl holds " + stored.getCount() + " and hand " + hand + " after adding 1 beef to 63, expected 64 and 9");
                    });
                    check(clientHand == 9, "client hand holds " + clientHand + " beef, expected 9");
                })
        );
    }

    @ClientTest("other meat takes the stored food out")
    private static List<Step> foodBowlOtherMeat() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    placeEmptyBowl(player);
                    hold(player, Items.BEEF, 3);
                })),
                Step.run(10, () -> useOn(pos)),
                Step.run(10, () -> onServer(server -> hold(player(server), Items.PORKCHOP, 1))),
                Step.run(10, () -> useOn(pos)),
                Step.until("client bowl to look empty", 20, () -> clientBowlFull(false)),
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    check(serverBowl(server).getContainedItem().isEmpty(), "bowl still holds " + serverBowl(server).getContainedItem());

                    int beef = player.getInventory().countItem(Items.BEEF);
                    check(beef == 1, "player got " + beef + " beef back, expected 1");
                    check(player.getMainHandItem().is(Items.PORKCHOP), "hand holds " + player.getMainHandItem() + " expected the porkchop");
                }))
        );
    }

    @ClientTest("topping up marks the chunk for saving")
    private static Step foodBowlMarksChunk() {
        return Step.run(0, () -> onServer(server -> {
            ServerPlayer player = player(server);
            FoodBowlBlockEntity bowl = placeBowl(player, Items.BEEF, 3);
            var chunk = player.serverLevel().getChunkAt(pos);
            chunk.setUnsaved(false);

            hold(player, Items.BEEF, 2);
            bowl.handleUse(player, InteractionHand.MAIN_HAND, player.getMainHandItem(), true);
            check(bowl.getContainedItem().getCount() == 5, "bowl holds " + bowl.getContainedItem());
            check(chunk.isUnsaved(), "chunk is not marked for saving after the bowl changed");
        }));
    }

    @ClientTest("client hand matches the server after topping up a bowl someone else filled")
    private static List<Step> foodBowlClientHand() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    placeBowl(player, Items.BEEF, 64);
                    hold(player, Items.BEEF, 10);
                })),
                Step.until("client to see a full bowl and 10 beef in hand", 20, () ->
                        clientBowlFull(true) && client().player.getMainHandItem().getCount() == 10),
                Step.run(0, () -> useOn(pos)),
                Step.run(20, () -> {
                    int server = fromServer(s -> player(s).getMainHandItem().getCount());
                    int client = client().player.getMainHandItem().getCount();
                    check(server == client, "hand holds " + server + " beef on the server but " + client + " on the client");
                })
        );
    }

    @ClientTest("breaking a full bowl drops its food")
    private static Step foodBowlBreakDrops() {
        return Step.run(0, () -> onServer(server -> {
            ServerPlayer player = player(server);
            placeBowl(player, Items.BEEF, 4);
            player.gameMode.destroyBlock(pos);

            Map<Item, Integer> dropped = drops(player.serverLevel(), pos);
            check(dropped.getOrDefault(Items.BEEF, 0) == 4, "full bowl dropped " + describe(dropped) + " expected 4 beef with it");
        }));
    }

    @ClientTest("blowing up a full bowl drops all its food")
    private static Step foodBowlExplodes() {
        return Step.run(0, () -> onServer(server -> {
            ServerPlayer player = player(server);
            ServerLevel world = player.serverLevel();
            BlockPos bowlPos = ahead(player, 6, 0);
            world.setBlockAndUpdate(bowlPos, BlockRegistry.FOOD_BOWL.get().defaultBlockState());
            hold(player, Items.BEEF, 64);
            ((FoodBowlBlockEntity) world.getBlockEntity(bowlPos)).handleUse(player, InteractionHand.MAIN_HAND, player.getMainHandItem(), true);

            Vec3 center = Vec3.atCenterOf(bowlPos);
            world.explode(null, center.x, center.y, center.z, 4, Level.ExplosionInteraction.TNT);
            check(!world.getBlockState(bowlPos).is(BlockRegistry.FOOD_BOWL.get()), "the explosion did not break the bowl");

            Map<Item, Integer> dropped = drops(world, bowlPos);
            check(dropped.getOrDefault(Items.BEEF, 0) == 64, "blown up bowl dropped " + describe(dropped) + " expected 64 beef");
        }));
    }

    private static void moaEatsFromBowl(ServerPlayer player, float hunger) {
        MoaEntity moa = EntityRegistry.MOA.get().spawn(player.serverLevel(), ahead(player, 3, 1), MobSpawnType.COMMAND);
        moa.setNoAi(true);
        moa.getGenes().setHunger(hunger);

        try {
            MoaEntity.EatFromBowlGoal goal = moa.new EatFromBowlGoal(1.0, 8, 2);
            Field target = MoveToBlockGoal.class.getDeclaredField("blockPos");
            target.setAccessible(true);
            target.set(goal, pos);

            Method tryEat = MoaEntity.EatFromBowlGoal.class.getDeclaredMethod("tryEat");
            tryEat.setAccessible(true);
            tryEat.invoke(goal);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @ClientTest("a moa eating the last food empties the bowl")
    private static List<Step> foodBowlMoaEats() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    FoodBowlBlockEntity bowl = placeBowl(player, Items.BEEF, 1);
                    moaEatsFromBowl(player, 40);

                    check(bowl.getContainedItem().isEmpty(), "moa did not eat, bowl holds " + bowl.getContainedItem());
                    check(!world(server).getBlockState(pos).getValue(FoodBowlBlock.FULL), "bowl is empty on the server but its block state is still full");
                })),
                Step.until("client bowl to look empty", 20, () -> clientBowlFull(false))
        );
    }

    @ClientTest("a moa eating some of the food marks the chunk for saving")
    private static Step foodBowlMoaEatsSome() {
        return Step.run(0, () -> onServer(server -> {
            ServerPlayer player = player(server);
            FoodBowlBlockEntity bowl = placeBowl(player, Items.BEEF, 10);
            var chunk = player.serverLevel().getChunkAt(pos);
            chunk.setUnsaved(false);

            moaEatsFromBowl(player, 40);
            check(bowl.getContainedItem().getCount() == 5, "bowl holds " + bowl.getContainedItem() + " after the moa ate, expected 5 beef");
            check(chunk.isUnsaved(), "chunk is not marked for saving after the moa ate from the bowl");
        }));
    }

    @ClientTest("meat in the off hand fills it when the main hand is empty")
    private static List<Step> foodBowlOffHand() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    placeEmptyBowl(player);
                    player.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(Items.BEEF, 3));
                })),
                Step.run(10, () -> {
                    for (InteractionHand hand : InteractionHand.values()) {
                        InteractionResult result = client().gameMode.useItemOn(client().player, hand, topOf(pos));
                        if (result.consumesAction() || result == InteractionResult.FAIL) break;
                    }
                }),
                Step.until("client bowl to look full", 20, () -> clientBowlFull(true)),
                Step.run(5, () -> onServer(server -> {
                    ItemStack stored = serverBowl(server).getContainedItem();
                    check(stored.is(Items.BEEF) && stored.getCount() == 1, "bowl holds " + stored);
                    
                    int offHand = player(server).getOffhandItem().getCount();
                    check(offHand == 2, "off hand holds " + offHand + " beef, expected 2");
                }))
        );
    }

    @ClientTest("keeps its food after save and load")
    private static Step foodBowlSaveLoad() {
        return Step.run(0, () -> onServer(server -> {
            ServerPlayer player = player(server);
            ServerLevel world = player.serverLevel();
            FoodBowlBlockEntity bowl = placeBowl(player, Items.BEEF, 4);
            check(bowl.getContainedItem().getCount() == 4, "bowl did not take the beef");

            BlockEntity loaded = BlockEntity.loadStatic(pos, world.getBlockState(pos), bowl.saveWithFullMetadata(world.registryAccess()), world.registryAccess());
            ItemStack stored = ((FoodBowlBlockEntity) loaded).getContainedItem();
            check(stored.is(Items.BEEF) && stored.getCount() == 4, "loaded bowl holds " + stored);
        }));
    }
}
