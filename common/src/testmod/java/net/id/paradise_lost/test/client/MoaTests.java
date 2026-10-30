package net.id.paradise_lost.test.client;

import net.id.paradise_lost.block.blockentity.IncubatorBlockEntity;
import net.id.paradise_lost.client.screen.MoaScreen;
import net.id.paradise_lost.test.Step;
import net.id.paradise_lost.entity.passive.moa.MoaEntity;
import net.id.paradise_lost.registry.BlockRegistry;
import net.id.paradise_lost.registry.EntityRegistry;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

import static net.id.paradise_lost.test.TestHelpers.*;
import static net.id.paradise_lost.test.client.ClientTestHelpers.*;
import static net.id.paradise_lost.test.server.MoaTests.goldenrodEgg;

public final class MoaTests {
    private static BlockPos pos;
    private static int entityId;

    private MoaTests() {
    }

    private static MoaEntity tamedMoa(ServerPlayer player) {
        MoaEntity moa = EntityRegistry.MOA.get().spawn(player.serverLevel(), ahead(player, 2, 0), MobSpawnType.COMMAND);
        moa.setNoAi(true);
        moa.getGenes().tame(player.getUUID());
        moa.syncGenes();
        entityId = moa.getId();
        return moa;
    }

    private static MoaEntity serverMoa(IntegratedServer server) {
        return (MoaEntity) world(server).getEntity(entityId);
    }

    @ClientTest("sneak and right click opens the moa menu on client")
    private static List<Step> moaMenuOpens() {
        return List.of(
                Step.run(0, () -> onServer(server -> tamedMoa(player(server)))),
                Step.run(10, () -> client().options.keyShift.setDown(true)),
                Step.run(3, () -> interact(entityId)),
                Step.until("moa screen", 20, () -> client().screen instanceof MoaScreen),
                Step.run(0, () -> {
                    client().options.keyShift.setDown(false);
                    client().player.closeContainer();
                })
        );
    }

    @ClientTest("single chest from survival goes onto the moa")
    private static List<Step> moaTakesChest() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    tamedMoa(player);
                    hold(player, Items.CHEST, 1);
                })),
                Step.run(10, () -> interact(entityId)),
                Step.run(10, () -> onServer(server -> {
                    boolean hasChest = serverMoa(server).hasChest();
                    int left = player(server).getInventory().countItem(Items.CHEST);
                    check(hasChest && left == 0, "moa has chest: " + hasChest + " chests left in inventory: " + left);
                }))
        );
    }

    @ClientTest("chest from stack of 2 leaves 1 and the moa keeps its copy")
    private static List<Step> moaTakesOneOfTwoChests() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    tamedMoa(player);
                    hold(player, Items.CHEST, 2);
                })),
                Step.run(10, () -> interact(entityId)),
                Step.run(10, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    MoaEntity moa = serverMoa(server);
                    check(moa.hasChest() && moa.getChest().getCount() == 1, "moa chest is " + moa.getChest());
                    check(player.getMainHandItem().is(Items.CHEST) && player.getMainHandItem().getCount() == 1, "hand holds " + player.getMainHandItem());
                    player.getMainHandItem().setCount(5);
                    check(moa.getChest().getCount() == 1, "moa chest changed to " + moa.getChest() + " with the hand stack");
                }))
        );
    }

    @ClientTest("creative player keeps the chest")
    private static List<Step> moaCreativeKeepsChest() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    player.setGameMode(GameType.CREATIVE);
                    tamedMoa(player);
                    hold(player, Items.CHEST, 1);
                })),
                Step.until("client in creative", 20, () -> client().player.isCreative()),
                Step.run(5, () -> interact(entityId)),
                Step.run(10, () -> onServer(server -> {
                    MoaEntity moa = serverMoa(server);
                    ItemStack hand = player(server).getMainHandItem();
                    check(moa.hasChest() && moa.getChest().getCount() == 1, "moa chest is " + moa.getChest());
                    check(hand.is(Items.CHEST) && hand.getCount() == 1, "creative hand holds " + hand);
                }))
        );
    }

    private static void openMoaMenu(ServerPlayer player, int moaSlot, ItemStack moaItem) {
        MoaEntity moa = tamedMoa(player);
        moa.setChest(new ItemStack(Items.CHEST));
        moa.getInventory().setItem(moaSlot, moaItem);
        moa.openCustomInventoryScreen(player);
        check(player.containerMenu != player.inventoryMenu, "moa menu did not open");
    }

    @ClientTest("menu shift click from the last moa chest slot moves to the player")
    private static Step moaMenuShiftClickFromChest() {
        return Step.run(0, () -> onServer(server -> {
            ServerPlayer player = player(server);
            openMoaMenu(player, 19, new ItemStack(Items.DIAMOND));
            player.containerMenu.quickMoveStack(player, 21);
            int inPlayer = player.getInventory().countItem(Items.DIAMOND);
            player.closeContainer();
            check(inPlayer == 1, "diamond from menu slot 21 is not in the player inventory");
        }));
    }

    @ClientTest("menu shift click from the moa chest can use the last hotbar slot")
    private static Step moaMenuShiftClickToLastHotbarSlot() {
        return Step.run(0, () -> onServer(server -> {
            ServerPlayer player = player(server);
            Inventory inventory = player.getInventory();
            for (int i = 0; i < Inventory.INVENTORY_SIZE; i++) {
                if (i != 8) inventory.setItem(i, new ItemStack(Items.DIRT, 64));
            }
            openMoaMenu(player, 0, new ItemStack(Items.DIAMOND));
            player.containerMenu.quickMoveStack(player, 2);

            ItemStack lastSlot = inventory.getItem(8);
            player.closeContainer();
            check(lastSlot.is(Items.DIAMOND), "only free slot is hotbar slot 8, it holds " + lastSlot);
        }));
    }

    @ClientTest("menu shift clicks from the client match the server")
    private static List<Step> moaMenuClientShiftClick() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    MoaEntity moa = tamedMoa(player);
                    moa.setChest(new ItemStack(Items.CHEST));
                    moa.getInventory().setItem(19, new ItemStack(Items.DIAMOND));
                    player.getInventory().setItem(8, new ItemStack(Items.EMERALD));
                })),
                Step.run(10, () -> client().options.keyShift.setDown(true)),
                Step.run(3, () -> interact(entityId)),
                Step.until("moa screen", 20, () -> client().screen instanceof MoaScreen),
                Step.run(0, () -> client().options.keyShift.setDown(false)),
                Step.run(5, () -> client().gameMode.handleInventoryMouseClick(client().player.containerMenu.containerId, 21, 0, ClickType.QUICK_MOVE, client().player)),
                Step.run(5, () -> client().gameMode.handleInventoryMouseClick(client().player.containerMenu.containerId, 57, 0, ClickType.QUICK_MOVE, client().player)),
                Step.run(10, () -> {
                    List<String> wrong = new ArrayList<>();
                    Inventory clientInventory = client().player.getInventory();
                    onServer(server -> {
                        Inventory inventory = player(server).getInventory();
                        Container moaChest = serverMoa(server).getInventory();
                        expect(wrong, inventory.countItem(Items.DIAMOND) == 1, "server: diamond did not move to the player");
                        expect(wrong, inventory.countItem(Items.EMERALD) == 0, "server: emerald is still in the player inventory");
                        expect(wrong, moaChest.countItem(Items.EMERALD) == 1, "server: emerald is not in the moa chest");
                        expect(wrong, moaChest.countItem(Items.DIAMOND) == 0, "server: diamond is still in the moa chest");
                    });
                    expect(wrong, clientInventory.countItem(Items.DIAMOND) == 1, "client: diamond is not in the player inventory");
                    expect(wrong, clientInventory.countItem(Items.EMERALD) == 0, "client: emerald is still in the player inventory");
                    client().player.closeContainer();
                    checkAll(wrong);
                })
        );
    }

    @ClientTest("menu shiftclick from the player fills the last moa chest slot")
    private static Step moaMenuShiftClickFillsLastChestSlot() {
        return Step.run(0, () -> onServer(server -> {
            ServerPlayer player = player(server);
            openMoaMenu(player, 0, new ItemStack(Items.DIRT, 64));

            Container moaChest = serverMoa(server).getInventory();
            for (int i = 1; i < 19; i++)
                moaChest.setItem(i, new ItemStack(Items.DIRT, 64));
            player.getInventory().setItem(9, new ItemStack(Items.DIAMOND));
            player.containerMenu.quickMoveStack(player, 22);

            ItemStack last = moaChest.getItem(19);
            player.closeContainer();
            check(last.is(Items.DIAMOND), "last moa chest slot holds " + last + " expected the diamond");
        }));
    }

    @ClientTest("saddle and ride moa")
    private static List<Step> moaSaddleAndRide() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    tamedMoa(player);
                    hold(player, Items.SADDLE, 1);
                })),
                Step.run(10, () -> interact(entityId)),
                Step.until("moa saddled", 20, () -> fromServer(server -> serverMoa(server).isSaddled())),
                Step.run(10, () -> interact(entityId)),
                Step.until("client player on the moa", 20, () -> client().player.getVehicle() instanceof MoaEntity)
        );
    }

    @ClientTest("hunger after feeding reaches the client")
    private static List<Step> moaHungerSyncs() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    MoaEntity moa = tamedMoa(player);
                    moa.getGenes().setHunger(40);
                    moa.syncGenes();
                    hold(player, Items.BEEF, 5);
                })),
                Step.until("client to see hunger 40", 20, () -> client().level.getEntity(entityId) instanceof MoaEntity moa && Math.abs(moa.getGenes().getHunger() - 40) < 1),
                Step.run(0, () -> interact(entityId)),
                Step.run(10, () -> {
                    float server = fromServer(s -> serverMoa(s).getGenes().getHunger());
                    float client = ((MoaEntity) client().level.getEntity(entityId)).getGenes().getHunger();

                    check(server > 41, "feeding did not change hunger on the server");
                    check(Math.abs(server - client) < 1, "hunger is " + server + " on the server but " + client + " on the client");
                })
        );
    }

    @ClientTest("egg disappears from incubator after hatching")
    private static List<Step> moaEggHatch() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    ServerLevel world = player.serverLevel();
                    pos = ahead(player, 2, 0);
                    world.setBlockAndUpdate(pos, BlockRegistry.INCUBATOR.get().defaultBlockState());
                    player.setItemInHand(InteractionHand.MAIN_HAND, goldenrodEgg(world, false));
                })),
                Step.run(10, () -> useOn(pos)),
                Step.run(10, () -> onServer(server -> {
                    var incubator = (IncubatorBlockEntity) world(server).getBlockEntity(pos);
                    check(incubator.hasItem(), "server incubator did not take the egg");
                    setField(incubator, "hatchTicks", 1);
                })),
                Step.run(40, () -> {
                    onServer(server -> check(
                            !world(server).getEntitiesOfClass(MoaEntity.class, new AABB(pos).inflate(3), e -> true).isEmpty(),
                            "no moa hatched on the server"));
                    var incubator = (IncubatorBlockEntity) client().level.getBlockEntity(pos);
                    check(!incubator.hasItem(), "client incubator still shows the egg after the moa hatched");
                })
        );
    }

    @ClientTest("moa spawned from an egg on the side of a wall is not inside a block")
    private static List<Step> moaEggBesideWall() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    ServerLevel world = player.serverLevel();
                    player.setGameMode(GameType.CREATIVE);
                    pos = ahead(player, 2, 0);
                    world.setBlockAndUpdate(pos, Blocks.STONE.defaultBlockState());
                    world.setBlockAndUpdate(pos.above(), Blocks.STONE.defaultBlockState());
                    player.setItemInHand(InteractionHand.MAIN_HAND, goldenrodEgg(world, true));
                })),
                Step.run(10, () -> {
                    Direction face = client().player.getDirection().getOpposite();
                    Vec3 hit = Vec3.atCenterOf(pos).relative(face, 0.5).add(0, 0.3, 0);
                    client().gameMode.useItemOn(client().player, InteractionHand.MAIN_HAND, new BlockHitResult(hit, face, pos, false));
                }),
                Step.run(10, () -> onServer(server -> {
                    ServerLevel world = world(server);
                    List<MoaEntity> moas = world.getEntitiesOfClass(MoaEntity.class, new AABB(pos).inflate(3), e -> true);
                    check(moas.size() == 1, moas.size() + " moas spawned, expected 1");

                    MoaEntity moa = moas.getFirst();
                    check(world.noCollision(moa), "moa spawned inside a block at " + moa.position() + " wall is at " + pos + " and " + pos.above());
                }))
        );
    }
}
