package net.id.paradise_lost.clienttest.tests;

import net.id.paradise_lost.block.blockentity.FoodBowlBlockEntity;
import net.id.paradise_lost.block.mechanical.FoodBowlBlock;
import net.id.paradise_lost.clienttest.Step;
import net.id.paradise_lost.clienttest.Test;
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

import static net.id.paradise_lost.clienttest.TestHelpers.*;

public final class FoodBowlTests {
    private static final String GROUP = "Food bowl";
    private static BlockPos pos;

    private FoodBowlTests() {
    }

    public static List<Test> all() {
        return List.of(
                foodBowlFills(),
                foodBowlTopsUp(),
                foodBowlOverflow(),
                foodBowlOtherMeat(),
                foodBowlMarksChunk(),
                foodBowlClientHand(),
                foodBowlBreakDrops(),
                foodBowlExplodes(),
                foodBowlMoaEats(),
                foodBowlMoaEatsSome(),
                foodBowlOffHand(),
                foodBowlSaveLoad()
        );
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
        bowl.handleUse(player, InteractionHand.MAIN_HAND, player.getMainHandItem());
        return bowl;
    }

    private static Test foodBowlFills() {
        return new Test(GROUP, "right click with meat fills it",
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    placeEmptyBowl(player);
                    hold(player, Items.BEEF, 3);
                })),
                Step.run(10, () -> useOn(pos)),
                Step.until("client bowl to look full", 20, () -> clientBowlFull(true)),
                Step.run(5, () -> onServer(server -> {
                    ItemStack stored = ((FoodBowlBlockEntity) world(server).getBlockEntity(pos)).getContainedItem();
                    check(stored.is(Items.BEEF) && stored.getCount() == 3, "bowl holds " + stored);
                    check(player(server).getMainHandItem().isEmpty(), "hand still holds " + player(server).getMainHandItem());
                }))
        );
    }

    private static Test foodBowlTopsUp() {
        return new Test(GROUP, "more of the same meat adds to the stack",
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
                    check(stored.is(Items.BEEF) && stored.getCount() == 8, "bowl holds " + stored + " after adding 5 beef to 3, expected 8");
                }))
        );
    }

    private static Test foodBowlOverflow() {
        return new Test(GROUP, "topping up past 64 leaves the rest in the hand",
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    placeEmptyBowl(player);
                    hold(player, Items.BEEF, 60);
                })),
                Step.run(10, () -> useOn(pos)),
                Step.run(10, () -> onServer(server -> hold(player(server), Items.BEEF, 10))),
                Step.until("client to hold 10 beef", 20, () -> client().player.getMainHandItem().getCount() == 10),
                Step.run(0, () -> useOn(pos)),
                Step.run(10, () -> {
                    int clientHand = client().player.getMainHandItem().getCount();
                    onServer(server -> {
                        ItemStack stored = serverBowl(server).getContainedItem();
                        int hand = player(server).getMainHandItem().getCount();
                        check(stored.getCount() == 64 && hand == 6, "bowl holds " + stored.getCount() + " and hand " + hand + " after adding 10 beef to 60, expected 64 and 6");
                    });
                    check(clientHand == 6, "client hand holds " + clientHand + " beef, expected 6");
                })
        );
    }

    private static Test foodBowlOtherMeat() {
        return new Test(GROUP, "other meat takes the stored food out",
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
                    check(beef == 3, "player got " + beef + " beef back, expected 3");
                    check(player.getMainHandItem().is(Items.PORKCHOP), "hand holds " + player.getMainHandItem() + " expected the porkchop");
                }))
        );
    }

    private static Test foodBowlMarksChunk() {
        return new Test(GROUP, "topping up marks the chunk for saving", Step.run(0, () -> onServer(server -> {
            ServerPlayer player = player(server);
            FoodBowlBlockEntity bowl = placeBowl(player, Items.BEEF, 3);
            var chunk = player.serverLevel().getChunkAt(pos);
            chunk.setUnsaved(false);

            hold(player, Items.BEEF, 2);
            bowl.handleUse(player, InteractionHand.MAIN_HAND, player.getMainHandItem());
            check(bowl.getContainedItem().getCount() == 5, "bowl holds " + bowl.getContainedItem());
            check(chunk.isUnsaved(), "chunk is not marked for saving after the bowl changed");
        })));
    }

    private static Test foodBowlClientHand() {
        return new Test(GROUP, "client hand matches the server after topping up a bowl someone else filled",
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

    private static Test foodBowlBreakDrops() {
        return new Test(GROUP, "breaking a full bowl drops its food", Step.run(0, () -> onServer(server -> {
            ServerPlayer player = player(server);
            placeBowl(player, Items.BEEF, 4);
            player.gameMode.destroyBlock(pos);

            Map<Item, Integer> dropped = drops(player.serverLevel(), pos);
            check(dropped.getOrDefault(Items.BEEF, 0) == 4, "full bowl dropped " + describe(dropped) + " expected 4 beef with it");
        })));
    }

    private static Test foodBowlExplodes() {
        return new Test(GROUP, "blowing up a full bowl drops all its food", Step.run(0, () -> onServer(server -> {
            ServerPlayer player = player(server);
            ServerLevel world = player.serverLevel();
            BlockPos bowlPos = ahead(player, 6, 0);
            world.setBlockAndUpdate(bowlPos, BlockRegistry.FOOD_BOWL.get().defaultBlockState());
            hold(player, Items.BEEF, 64);
            ((FoodBowlBlockEntity) world.getBlockEntity(bowlPos)).handleUse(player, InteractionHand.MAIN_HAND, player.getMainHandItem());

            Vec3 center = Vec3.atCenterOf(bowlPos);
            world.explode(null, center.x, center.y, center.z, 4, Level.ExplosionInteraction.TNT);
            check(!world.getBlockState(bowlPos).is(BlockRegistry.FOOD_BOWL.get()), "the explosion did not break the bowl");

            Map<Item, Integer> dropped = drops(world, bowlPos);
            check(dropped.getOrDefault(Items.BEEF, 0) == 64, "blown up bowl dropped " + describe(dropped) + " expected 64 beef");
        })));
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

    private static Test foodBowlMoaEats() {
        return new Test(GROUP, "a moa eating the last food empties the bowl",
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

    private static Test foodBowlMoaEatsSome() {
        return new Test(GROUP, "a moa eating some of the food marks the chunk for saving", Step.run(0, () -> onServer(server -> {
            ServerPlayer player = player(server);
            FoodBowlBlockEntity bowl = placeBowl(player, Items.BEEF, 10);
            var chunk = player.serverLevel().getChunkAt(pos);
            chunk.setUnsaved(false);

            moaEatsFromBowl(player, 40);
            check(bowl.getContainedItem().getCount() == 5, "bowl holds " + bowl.getContainedItem() + " after the moa ate, expected 5 beef");
            check(chunk.isUnsaved(), "chunk is not marked for saving after the moa ate from the bowl");
        })));
    }

    private static Test foodBowlOffHand() {
        return new Test(GROUP, "meat in the off hand fills it when the main hand is empty",
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
                    check(stored.is(Items.BEEF) && stored.getCount() == 3, "bowl holds " + stored);
                    check(player(server).getOffhandItem().isEmpty(), "off hand still holds " + player(server).getOffhandItem());
                }))
        );
    }

    private static Test foodBowlSaveLoad() {
        return new Test(GROUP, "keeps its food after save and load", Step.run(0, () -> onServer(server -> {
            ServerPlayer player = player(server);
            ServerLevel world = player.serverLevel();
            FoodBowlBlockEntity bowl = placeBowl(player, Items.BEEF, 4);
            check(bowl.getContainedItem().getCount() == 4, "bowl did not take the beef");

            BlockEntity loaded = BlockEntity.loadStatic(pos, world.getBlockState(pos), bowl.saveWithFullMetadata(world.registryAccess()), world.registryAccess());
            ItemStack stored = ((FoodBowlBlockEntity) loaded).getContainedItem();
            check(stored.is(Items.BEEF) && stored.getCount() == 4, "loaded bowl holds " + stored);
        })));
    }
}
