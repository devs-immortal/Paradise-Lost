package net.id.paradise_lost.test.client;

import net.id.paradise_lost.block.blockentity.CherineCampfireBlockEntity;
import net.id.paradise_lost.block.blockentity.ParadiseLostBlockEntityTypes;
import net.id.paradise_lost.block.blockentity.TreeTapBlockEntity;
import net.id.paradise_lost.block.decorative.CalciteFlowerPotBlock;
import net.id.paradise_lost.block.mechanical.PalaceDoorBlock;
import net.id.paradise_lost.block.mechanical.TreeTapBlock;
import net.id.paradise_lost.block.natural.crop.TallCropBlock;
import net.id.paradise_lost.test.Recorder;
import net.id.paradise_lost.test.Step;
import net.id.paradise_lost.registry.BlockRegistry;
import net.minecraft.client.gui.screens.inventory.AbstractSignEditScreen;
import net.minecraft.client.gui.screens.inventory.HangingSignEditScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static net.id.paradise_lost.test.TestHelpers.*;
import static net.id.paradise_lost.test.client.ClientTestHelpers.*;
import static net.id.paradise_lost.registry.ItemRegistry.*;

public final class BlockTests {
    private static BlockPos pos;
    private static int cooked;

    private BlockTests() {
    }

    @ClientTest(value = "cooks food", group = "Campfire")
    private static List<Step> campfireCooks() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    pos = ahead(player, 2, 0);
                    player.serverLevel().setBlockAndUpdate(pos, BlockRegistry.CHERINE_CAMPFIRE.get().defaultBlockState());
                    hold(player, Items.BEEF, 1);
                })),
                Step.run(10, () -> useOn(pos)),
                Step.until("client campfire to show the beef", 20, () ->
                        client().level.getBlockEntity(pos) instanceof CherineCampfireBlockEntity campfire && campfire.getItemsBeingCooked().getFirst().is(Items.BEEF)),
                Step.run(0, () -> onServer(server -> {
                    int[] totals = getField(world(server).getBlockEntity(pos), "cookingTotalTimes");
                    totals[0] = 20;
                })),
                Step.until("cooked beef to drop", 80, () -> fromServer(server -> drops(world(server), pos).containsKey(Items.COOKED_BEEF)))
        );
    }

    @ClientTest(value = "cooks all four items", group = "Campfire")
    private static List<Step> campfireCooksFour() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    pos = ahead(player, 2, 0);

                    player.serverLevel().setBlockAndUpdate(pos, BlockRegistry.CHERINE_CAMPFIRE.get().defaultBlockState());
                    hold(player, Items.BEEF, 4);
                    cooked = 0;
                })),
                Step.run(10, () -> useOn(pos)),
                Step.run(5, () -> useOn(pos)),
                Step.run(5, () -> useOn(pos)),
                Step.run(5, () -> useOn(pos)),
                Step.until("client campfire to show 4 beef", 20, () -> client().level.getBlockEntity(pos) instanceof CherineCampfireBlockEntity campfire
                        && campfire.getItemsBeingCooked().stream().filter(stack -> stack.is(Items.BEEF)).count() == 4),
                Step.run(0, () -> onServer(server -> {
                    int[] totals = getField(world(server).getBlockEntity(pos), "cookingTotalTimes");
                    Arrays.fill(totals, 20);
                })),
                Step.until("4 cooked beef to drop", 80, () -> {
                    cooked += fromServer(server -> drops(world(server), pos).getOrDefault(Items.COOKED_BEEF, 0));
                    return cooked >= 4;
                }),
                Step.run(0, () -> check(cooked == 4, cooked + " cooked beef from 4 raw beef"))
        );
    }

    @ClientTest(value = "drops cherine", group = "Campfire")
    private static Step campfireDropsCherine() {
        return Step.run(0, () -> onServer(server -> {
            ServerPlayer player = player(server);
            BlockPos campfire = ahead(player, 3, 0);
            player.serverLevel().setBlockAndUpdate(campfire, BlockRegistry.CHERINE_CAMPFIRE.get().defaultBlockState());

            player.gameMode.destroyBlock(campfire);
            Map<Item, Integer> dropped = drops(player.serverLevel(), campfire);
            check(dropped.equals(Map.of(CHERINE.get(), 1)), "dropped " + describe(dropped) + " expected 1 cherine");
        }));
    }

    @ClientTest(value = "bottle put in by someone else shows on the client", group = "Tree tap")
    private static List<Step> treeTapSyncsToOtherPlayers() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    ServerLevel world = player.serverLevel();
                    Direction facing = player.getDirection();
                    pos = ahead(player, 3, 0);

                    world.setBlockAndUpdate(pos.relative(facing), BlockRegistry.AUREL_WOODSTUFF.log().get().defaultBlockState());
                    world.setBlockAndUpdate(pos, BlockRegistry.TREE_TAP.get().defaultBlockState().setValue(TreeTapBlock.FACING, facing.getOpposite()));
                })),
                Step.run(5, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    hold(player, Items.GLASS_BOTTLE, 1);
                    ((TreeTapBlockEntity) world(server).getBlockEntity(pos)).handleUse(player, InteractionHand.MAIN_HAND, player.getMainHandItem());
                })),
                Step.until("client tree tap to show the bottle", 20, () ->
                        client().level.getBlockEntity(pos) instanceof TreeTapBlockEntity tap && tap.getItem(0).is(Items.GLASS_BOTTLE))
        );
    }

    @ClientTest(value = "bottle taken out by someone else disappears on the client", group = "Tree tap")
    private static List<Step> treeTapTakeOutSyncs() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    ServerLevel world = player.serverLevel();
                    Direction facing = player.getDirection();
                    pos = ahead(player, 3, 0);

                    world.setBlockAndUpdate(pos.relative(facing), BlockRegistry.AUREL_WOODSTUFF.log().get().defaultBlockState());
                    world.setBlockAndUpdate(pos, BlockRegistry.TREE_TAP.get().defaultBlockState().setValue(TreeTapBlock.FACING, facing.getOpposite()));

                    hold(player, Items.GLASS_BOTTLE, 1);
                    ((TreeTapBlockEntity) world.getBlockEntity(pos)).handleUse(player, InteractionHand.MAIN_HAND, player.getMainHandItem());
                })),
                Step.until("client tree tap to show the bottle", 20, () ->
                        client().level.getBlockEntity(pos) instanceof TreeTapBlockEntity tap && !tap.getItem(0).isEmpty()),
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    ((TreeTapBlockEntity) world(server).getBlockEntity(pos)).handleUse(player, InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                })),
                Step.until("client tree tap to be empty", 20, () ->
                        client().level.getBlockEntity(pos) instanceof TreeTapBlockEntity tap && tap.getItem(0).isEmpty())
        );
    }

    @ClientTest(value = "hanging sign can be placed by a player", group = "Sign")
    private static List<Step> hangingSignPlaces() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    pos = ahead(player, 2, 0);
                    player.serverLevel().setBlockAndUpdate(pos.above(2), Blocks.STONE.defaultBlockState());
                    hold(player, AUREL_HANGING_SIGN.get(), 1);
                })),
                Step.run(10, () -> client().gameMode.useItemOn(client().player, InteractionHand.MAIN_HAND,
                        new BlockHitResult(Vec3.atCenterOf(pos.above(2)).add(0, -0.5, 0), Direction.DOWN, pos.above(2), false))),
                Step.until("sign placed", 20, () -> fromServer(server -> world(server).getBlockState(pos.above()).is(BlockRegistry.AUREL_SIGNS.hangingSign().get()))),
                Step.run(0, () -> {
                    if (client().screen != null)
                        client().player.closeContainer();
                })
        );
    }

    @ClientTest(value = "wall hanging sign of every wood places on a wall and keeps its text", group = "Sign")
    private static List<Step> wallHangingSigns() {
        List<BlockRegistry.SignSet> signs = List.of(BlockRegistry.AUREL_SIGNS, BlockRegistry.MOTHER_AUREL_SIGNS, BlockRegistry.MENTH_SIGNS, BlockRegistry.WISTERIA_SIGNS);
        List<Item> items = List.of(AUREL_HANGING_SIGN.get(), MOTHER_AUREL_HANGING_SIGN.get(), MENTH_HANGING_SIGN.get(), WISTERIA_HANGING_SIGN.get());
        List<Step> steps = new ArrayList<>();
        for (int i = 0; i < signs.size(); i++) {
            int side = i * 2 - 3;
            BlockRegistry.SignSet sign = signs.get(i);
            Item item = items.get(i);
            String text = "wall sign " + i;

            steps.add(Step.run(0, () -> onServer(server -> {
                ServerPlayer player = player(server);
                pos = ahead(player, 3, side).above();
                player.serverLevel().setBlockAndUpdate(pos, Blocks.STONE.defaultBlockState());
                hold(player, item, 1);
            })));
            steps.add(Step.run(10, () -> {
                Direction toPlayer = client().player.getDirection().getOpposite();
                Vec3 hit = Vec3.atCenterOf(pos).add(toPlayer.getStepX() * 0.5, 0, toPlayer.getStepZ() * 0.5);
                client().gameMode.useItemOn(client().player, InteractionHand.MAIN_HAND, new BlockHitResult(hit, toPlayer, pos, false));
            }));

            steps.add(Step.until(text + " placed", 20, () -> fromServer(server ->
                    world(server).getBlockState(pos.relative(player(server).getDirection().getOpposite())).is(sign.wallHangingSign().get()))));
            steps.add(Step.until("edit screen for " + text, 20, () -> client().screen instanceof HangingSignEditScreen));

            steps.add(Step.run(0, () -> {
                String[] lines = getField(client().screen, AbstractSignEditScreen.class, "messages");
                lines[0] = text;
                client().setScreen(null);
            }));
            steps.add(Step.until("server to get the text of " + text, 20, () -> fromServer(server ->
                    world(server).getBlockEntity(pos.relative(player(server).getDirection().getOpposite())) instanceof SignBlockEntity be
                            && be.getFrontText().getMessage(0, false).getString().equals(text))));

            steps.add(Step.run(0, () -> onServer(server -> {
                ServerLevel world = world(server);
                BlockPos signPos = pos.relative(player(server).getDirection().getOpposite());
                BlockEntity be = world.getBlockEntity(signPos);
                BlockEntity loaded = BlockEntity.loadStatic(signPos, world.getBlockState(signPos), be.saveWithFullMetadata(world.registryAccess()), world.registryAccess());
                check(loaded != null && loaded.getType() == ParadiseLostBlockEntityTypes.HANGING_SIGN.get(), text + " loaded as " + (
                        loaded == null
                                ? "nothing"
                                : loaded.getType()));

                String line = ((SignBlockEntity) loaded).getFrontText().getMessage(0, false).getString();
                check(line.equals(text), text + " loaded with text: " + line);
            })));
        }

        return steps;
    }

    @ClientTest(value = "wall torch and wall sign drop their standing item", group = "Sign")
    private static Step wallVariantDrops() {
        return Step.run(0, () -> onServer(server -> {
            ServerPlayer player = player(server);
            ServerLevel world = player.serverLevel();
            List<String> wrong = new ArrayList<>();
            Direction facing = player.getDirection();

            BlockPos torch = ahead(player, 3, -2);
            world.setBlockAndUpdate(torch.relative(facing), Blocks.STONE.defaultBlockState());
            world.setBlockAndUpdate(torch, BlockRegistry.CHERINE_TORCH_WALL.get().defaultBlockState().setValue(WallTorchBlock.FACING, facing.getOpposite()));
            player.gameMode.destroyBlock(torch);
            Map<Item, Integer> torchDrops = drops(world, torch);
            expect(wrong, torchDrops.equals(Map.of(CHERINE_TORCH.get(), 1)), "cherine wall torch dropped " + describe(torchDrops));

            BlockPos sign = ahead(player, 3, 2);
            world.setBlockAndUpdate(sign.relative(facing), Blocks.STONE.defaultBlockState());
            world.setBlockAndUpdate(sign, BlockRegistry.AUREL_SIGNS.wallSign().get().defaultBlockState());
            player.gameMode.destroyBlock(sign);
            Map<Item, Integer> signDrops = drops(world, sign);
            expect(wrong, signDrops.equals(Map.of(AUREL_SIGN.get(), 1)), "aurel wall sign dropped " + describe(signDrops));

            checkAll(wrong);
        }));
    }

    @ClientTest(value = "placer builds it and the key opens it", group = "Palace door")
    private static List<Step> palaceDoorOpens() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    pos = ahead(player, 3, 0);
                    hold(player, PALACE_DOOR_PLACER.get(), 1);
                })),
                Step.run(10, () -> useOn(pos.below())),
                Step.until("door to be placed", 20, () -> fromServer(server -> world(server).getBlockState(pos.above(4)).is(BlockRegistry.PALACE_DOOR.get()))),
                Step.run(0, () -> onServer(server -> hold(player(server), PALACE_KEY.get(), 1))),
                Step.run(10, () -> useOn(pos)),
                Step.until("client door to open", 20, () -> client().level.getBlockState(pos.above(4)).getValue(PalaceDoorBlock.OPEN)),
                Step.run(5, () -> {
                    BlockPos door = pos.above(4);
                    check(client().level.getBlockState(door).getCollisionShape(client().level, door).isEmpty(), "open door still has collision");
                    check(client().level.getBlockState(pos).isAir(), "extension blocks are still there");
                    onServer(server -> check(player(server).getMainHandItem().isEmpty(), "key was not used up"));

                    client().player.setXRot(-30);
                }),
                Step.run(20, () -> screenshot("palace_door_open.png"))
        );
    }

    @ClientTest(value = "breaking it removes the extension blocks", group = "Palace door")
    private static List<Step> palaceDoorBreaks() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    pos = ahead(player, 3, 0);
                    hold(player, PALACE_DOOR_PLACER.get(), 1);
                })),
                Step.run(10, () -> useOn(pos.below())),
                Step.until("door to be placed", 20, () -> fromServer(server -> world(server).getBlockState(pos.above(4)).is(BlockRegistry.PALACE_DOOR.get()))),
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    player.setGameMode(GameType.CREATIVE);
                    player.gameMode.destroyBlock(pos.above(4));

                    List<String> wrong = new ArrayList<>();
                    for (int y = 0; y < 7; y++) {
                        BlockState state = player.serverLevel().getBlockState(pos.above(y));
                        expect(wrong, state.isAir(), "y+" + y + " is " + state);
                    }

                    checkAll(wrong);
                }))
        );
    }

    private static void overCloud(Block cloud, int cloudHeight, int fall) {
        onServer(server -> {
            ServerPlayer player = player(server);
            pos = player.blockPosition().above(cloudHeight);
            player.serverLevel().setBlockAndUpdate(pos, cloud.defaultBlockState());
            player.teleportTo(pos.getX() + 0.5, pos.getY() + fall, pos.getZ() + 0.5);
        });
    }

    @ClientTest(value = "blue cloud bounces the player up", group = "Cloud")
    private static List<Step> blueCloudBounces() {
        return List.of(
                Step.run(0, () -> overCloud(BlockRegistry.BLUE_CLOUD.get(), 0, 6)),
                Step.until("client player to bounce", 60, () -> client().player.getDeltaMovement().y > 1.5),
                Step.run(0, () -> check(!Recorder.playerHurtBy(DamageTypes.FALL), "took fall damage"))
        );
    }

    @ClientTest(value = "golden cloud drops the player through", group = "Cloud")
    private static List<Step> goldenCloudDropsThrough() {
        return List.of(
                Step.run(0, () -> overCloud(BlockRegistry.GOLDEN_CLOUD.get(), 5, 6)),
                Step.until("player below the cloud", 60, () -> fromServer(server -> player(server).getY() < pos.getY())),
                Step.until("fall damage", 40, () -> Recorder.playerHurtBy(DamageTypes.FALL))
        );
    }

    @ClientTest(value = "cold cloud stops fall damage", group = "Cloud")
    private static List<Step> coldCloudSoftensFall() {
        return List.of(
                Step.run(0, () -> overCloud(BlockRegistry.COLD_CLOUD.get(), 0, 20)),
                Step.run(5, () -> {}),
                Step.until("landing", 100, () -> fromServer(server -> player(server).onGround() || player(server).getY() < pos.getY() + 1)),
                Step.run(10, () -> check(!Recorder.playerHurtBy(DamageTypes.FALL), "took fall damage"))
        );
    }

    @ClientTest(value = "healing potion turns calcite into bloomed calcite", group = "Calcite")
    private static List<Step> healingPotionBloomsCalcite() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    pos = ahead(player, 2, 0);
                    player.serverLevel().setBlockAndUpdate(pos, Blocks.CALCITE.defaultBlockState());
                    player.setItemInHand(InteractionHand.MAIN_HAND, PotionContents.createItemStack(Items.POTION, Potions.HEALING));
                })),
                Step.run(10, () -> useOn(pos)),
                Step.until("bloomed calcite", 20, () -> fromServer(server -> world(server).getBlockState(pos).is(BlockRegistry.BLOOMED_CALCITE.get()))),
                Step.run(5, () -> onServer(server -> check(player(server).getMainHandItem().is(Items.GLASS_BOTTLE), "hand holds " + player(server).getMainHandItem())))
        );
    }

    @ClientTest(value = "calcite pot can pot a flower, give it back and break", group = "Flower pot")
    private static List<Step> calciteFlowerPot() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    pos = ahead(player, 2, 0);
                    hold(player, BlockRegistry.CALCITE_FLOWER_POT.get().asItem(), 1);
                })),
                Step.run(10, () -> useOn(pos.below())),
                Step.until("calcite pot placed", 20, () -> fromServer(server -> world(server).getBlockState(pos).is(BlockRegistry.CALCITE_FLOWER_POT.get()))),
                Step.run(0, () -> onServer(server -> hold(player(server), BlockRegistry.ANCIENT_FLOWER.get().asItem(), 1))),
                Step.run(10, () -> useOn(pos)),
                Step.until("client and server to show the potted flower in calcite", 20, () -> {
                    BlockState clientState = client().level.getBlockState(pos);
                    BlockState serverState = fromServer(server -> world(server).getBlockState(pos));
                    return clientState.equals(serverState) && clientState.is(BlockRegistry.POTTED_ANCIENT_FLOWER.get()) && clientState.getValue(CalciteFlowerPotBlock.IS_CALCITE);
                }),
                Step.run(0, () -> onServer(server -> player(server).setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY))),
                Step.run(10, () -> useOn(pos)),
                Step.until("flower taken out", 20, () -> fromServer(server -> world(server).getBlockState(pos).is(BlockRegistry.CALCITE_FLOWER_POT.get()))),
                Step.run(5, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    check(player.getInventory().countItem(BlockRegistry.ANCIENT_FLOWER.get().asItem()) == 1, "flower did not come back");

                    player.gameMode.destroyBlock(pos);
                    Map<Item, Integer> drops = drops(player.serverLevel(), pos);
                    check(drops.equals(Map.of(BlockRegistry.CALCITE_FLOWER_POT.get().asItem(), 1)), "empty calcite pot dropped " + describe(drops));
                }))
        );
    }

    @ClientTest(value = "vanilla pot still works with a Paradise Lost flower", group = "Flower pot")
    private static List<Step> vanillaFlowerPot() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    pos = ahead(player, 2, 0);
                    player.serverLevel().setBlockAndUpdate(pos, Blocks.FLOWER_POT.defaultBlockState());
                    hold(player, BlockRegistry.ANCIENT_FLOWER.get().asItem(), 1);
                })),
                Step.run(10, () -> useOn(pos)),
                Step.until("flower potted", 20, () -> fromServer(server -> world(server).getBlockState(pos).is(BlockRegistry.POTTED_ANCIENT_FLOWER.get()))),
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    check(!player.serverLevel().getBlockState(pos).getValue(CalciteFlowerPotBlock.IS_CALCITE), "vanilla pot became calcite");

                    player.gameMode.destroyBlock(pos);
                    Map<Item, Integer> drops = drops(player.serverLevel(), pos);
                    check(drops.equals(Map.of(Items.FLOWER_POT, 1, BlockRegistry.ANCIENT_FLOWER.get().asItem(), 1)), "potted flower dropped " + describe(drops));
                }))
        );
    }

    @ClientTest(value = "picking a ripe blackcurrant bush drops berries", group = "Crops")
    private static List<Step> blackcurrantPicking() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    pos = ahead(player, 2, 0);
                    player.serverLevel().setBlockAndUpdate(pos, BlockRegistry.BLACKCURRANT_BUSH.get().defaultBlockState().setValue(SweetBerryBushBlock.AGE, 3));
                })),
                Step.run(10, () -> useOn(pos)),
                Step.until("berries", 20, () -> fromServer(server -> drops(world(server), pos).containsKey(BLACKCURRANT.get()))),
                Step.run(0, () -> onServer(server -> {
                    int age = world(server).getBlockState(pos).getValue(SweetBerryBushBlock.AGE);
                    check(age == 1, "bush age is " + age + " after picking, expected 1");
                }))
        );
    }

    @ClientTest(value = "Paradise Lost farmland is trampled into Paradise Lost dirt", group = "Crops")
    private static List<Step> farmlandTramples() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    pos = player.blockPosition().below();
                    player.serverLevel().setBlockAndUpdate(pos, BlockRegistry.FARMLAND.get().defaultBlockState());
                    player.teleportTo(pos.getX() + 0.5, pos.getY() + 5D, pos.getZ() + 0.5);
                })),
                Step.until("farmland trampled", 60, () -> fromServer(server -> !world(server).getBlockState(pos).is(BlockRegistry.FARMLAND.get()))),
                Step.run(0, () -> onServer(server -> {
                    BlockState state = world(server).getBlockState(pos);
                    check(state.is(BlockRegistry.DIRT.get()), "trampled into " + state);
                }))
        );
    }

    @ClientTest(value = "grow with bone meal and drop their harvest", group = "Crops")
    private static Step cropsGrowAndDrop() {
        return Step.run(0, () -> onServer(server -> {
            ServerPlayer player = player(server);
            ServerLevel world = player.serverLevel();
            List<String> wrong = new ArrayList<>();
            List<Object[]> crops = List.of(
                    new Object[]{BlockRegistry.AMADRYS.get(), AMADRYS_BUSHEL.get()},
                    new Object[]{BlockRegistry.FLAX.get(), FLAX_THREAD.get()},
                    new Object[]{BlockRegistry.NITRA.get(), NITRA_BULB.get()},
                    new Object[]{BlockRegistry.SWEDROOT.get(), SWEDROOT.get()}
            );

            for (int i = 0; i < crops.size(); i++) {
                CropBlock crop = (CropBlock) crops.get(i)[0];
                Item harvest = (Item) crops.get(i)[1];
                String name = crop.getName().getString();
                BlockPos cropPos = ahead(player, 3, i * 3 - 4);

                if (crop == BlockRegistry.SWEDROOT.get()) {
                    cropPos = cropPos.above(2);
                    world.setBlockAndUpdate(cropPos.above(), BlockRegistry.DIRT.get().defaultBlockState());
                } else {
                    world.setBlockAndUpdate(cropPos.below(), BlockRegistry.FARMLAND.get().defaultBlockState());
                }
                world.setBlockAndUpdate(cropPos, crop.getStateForAge(0));

                for (int j = 0; j < 30 && world.getBlockState(cropPos).getBlock() == crop && !crop.isMaxAge(world.getBlockState(cropPos)); j++) {
                    BoneMealItem.growCrop(new ItemStack(Items.BONE_MEAL), world, cropPos);
                }

                BlockState grown = world.getBlockState(cropPos);
                if (grown.getBlock() != crop) {
                    wrong.add(name + " became " + grown);
                    continue;
                }
                expect(wrong, crop.isMaxAge(grown), name + " stopped at " + grown);

                if (crop instanceof TallCropBlock) {
                    BlockState top = world.getBlockState(cropPos.above());
                    expect(wrong, top.is(crop) && top.getValue(TallCropBlock.HALF) == DoubleBlockHalf.UPPER, name + " has no top half, found " + top);
                }

                player.gameMode.destroyBlock(cropPos);
                Map<Item, Integer> drops = drops(world, cropPos);
                expect(wrong, drops.containsKey(harvest), "ripe " + name + " dropped " + describe(drops));
            }

            checkAll(wrong);
        }));
    }

    @ClientTest(value = "axe strips Paradise Lost logs", group = "Blocks")
    private static List<Step> strippingMotherAurel() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    pos = ahead(player, 2, 0);
                    player.serverLevel().setBlockAndUpdate(pos, BlockRegistry.MOTHER_AUREL_WOODSTUFF.log().get().defaultBlockState());
                    hold(player, Items.IRON_AXE);
                })),
                Step.run(10, () -> useOn(pos)),
                Step.until("log to be stripped", 20, () -> fromServer(server ->
                        world(server).getBlockState(pos).is(BlockRegistry.MOTHER_AUREL_WOODSTUFF.strippedLog().get())))
        );
    }
}
