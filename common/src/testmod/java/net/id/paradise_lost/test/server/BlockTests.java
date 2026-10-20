package net.id.paradise_lost.test.server;

import net.id.paradise_lost.block.blockentity.CherineCampfireBlockEntity;
import net.id.paradise_lost.block.blockentity.TreeTapBlockEntity;
import net.id.paradise_lost.block.mechanical.TreeTapBlock;
import net.id.paradise_lost.registry.BlockRegistry;
import net.id.paradise_lost.test.Step;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static net.id.paradise_lost.test.TestHelpers.*;
import static net.id.paradise_lost.test.server.ServerTestHelpers.*;

public final class BlockTests {
    private static BlockPos pos;
    private static int entityId;

    private BlockTests() {
    }

    @ServerTest(value = "does not cook while unlit, cooks after relighting", group = "Campfire")
    private static List<Step> campfireUnlit() {
        return List.of(
                Step.run(0, () -> {
                    ServerLevel world = level();
                    pos = ahead(2, 0);

                    world.setBlockAndUpdate(pos, BlockRegistry.CHERINE_CAMPFIRE.get().defaultBlockState());
                    ((CherineCampfireBlockEntity) world.getBlockEntity(pos)).addItem(null, new ItemStack(Items.BEEF), 20);

                    world.setBlockAndUpdate(pos, world.getBlockState(pos).setValue(CampfireBlock.LIT, false));
                }),
                Step.run(40, () -> {
                    ServerLevel world = level();
                    Map<Item, Integer> dropped = drops(world, pos);
                    check(dropped.isEmpty(), "unlit campfire dropped " + describe(dropped));

                    var campfire = (CherineCampfireBlockEntity) world.getBlockEntity(pos);
                    check(campfire.getItemsBeingCooked().getFirst().is(Items.BEEF), "unlit campfire lost its beef");

                    world.setBlockAndUpdate(pos, world.getBlockState(pos).setValue(CampfireBlock.LIT, true));
                }),
                Step.until("cooked beef to drop after relighting", 80, () -> drops(level(), pos).containsKey(Items.COOKED_BEEF))
        );
    }

    @ServerTest(value = "fills bottles from logs and bee nests", group = "Tree tap")
    private static Step treeTap() {
        return Step.run(0, () -> {
            ServerLevel world = level();
            List<String> wrong = new ArrayList<>();

            BlockPos tap = ahead(3, -1);
            world.setBlockAndUpdate(tap.relative(FACING), BlockRegistry.WISTERIA_WOODSTUFF.log().get().defaultBlockState());
            TreeTapBlockEntity water = placeTap(world, tap, FACING);
            water.tryCraft();
            expect(wrong, water.getItem(0).is(Items.POTION), "wisteria log gave " + water.getItem(0));

            BlockPos nestTap = ahead(3, 1);
            BlockPos nest = nestTap.relative(FACING);
            world.setBlockAndUpdate(nest, Blocks.BEE_NEST.defaultBlockState().setValue(BeehiveBlock.HONEY_LEVEL, 5).setValue(BeehiveBlock.FACING, FACING.getOpposite()));
            TreeTapBlockEntity honey = placeTap(world, nestTap, FACING);
            for (int i = 0; i < 100 && !honey.getItem(0).is(Items.HONEY_BOTTLE); i++) {
                honey.tryCraft();
            }
            expect(wrong, honey.getItem(0).is(Items.HONEY_BOTTLE), "bee nest gave " + honey.getItem(0));
            expect(wrong, world.getBlockState(nest).getValue(BeehiveBlock.HONEY_LEVEL) == 0, "bee nest kept its honey");

            BlockPos stoneTap = ahead(6, 0);
            world.setBlockAndUpdate(stoneTap.relative(FACING), Blocks.STONE.defaultBlockState());
            TreeTapBlockEntity nothing = placeTap(world, stoneTap, FACING);
            for (int i = 0; i < 20; i++) {
                nothing.tryCraft();
            }
            expect(wrong, nothing.getItem(0).is(Items.GLASS_BOTTLE), "stone gave " + nothing.getItem(0));

            checkAll(wrong);
        });
    }

    private static TreeTapBlockEntity placeTap(ServerLevel world, BlockPos tap, Direction towardsLog) {
        world.setBlockAndUpdate(tap, BlockRegistry.TREE_TAP.get().defaultBlockState().setValue(TreeTapBlock.FACING, towardsLog.getOpposite()));
        TreeTapBlockEntity entity = (TreeTapBlockEntity) world.getBlockEntity(tap);
        entity.setItem(0, new ItemStack(Items.GLASS_BOTTLE));
        return entity;
    }

    @ServerTest(value = "splash healing potion turns calcite into bloomed calcite", group = "Calcite")
    private static List<Step> splashHealingBloomsCalcite() {
        return List.of(
                Step.run(0, () -> {
                    ServerLevel world = level();
                    pos = ahead(4, 0);

                    world.setBlockAndUpdate(pos, Blocks.CALCITE.defaultBlockState());

                    ThrownPotion potion = new ThrownPotion(world, pos.getX() + 0.5, pos.getY() + 3D, pos.getZ() + 0.5);
                    potion.setItem(PotionContents.createItemStack(Items.SPLASH_POTION, Potions.HEALING));
                    potion.setDeltaMovement(0, -0.5, 0);
                    world.addFreshEntity(potion);
                }),
                Step.until("bloomed calcite", 40, () -> level().getBlockState(pos).is(BlockRegistry.BLOOMED_CALCITE.get()))
        );
    }

    @ServerTest(value = "surtrum air disappears and burns nearby mobs when opened", group = "Blocks")
    private static List<Step> surtrumAirPoofs() {
        return List.of(
                Step.run(0, () -> {
                    ServerLevel world = level();

                    pos = ahead(4, 0);
                    world.setBlockAndUpdate(pos, BlockRegistry.SURTRUM_AIR.get().defaultBlockState());
                    world.setBlockAndUpdate(pos.above(), Blocks.STONE.defaultBlockState());

                    Cow cow = EntityType.COW.create(world);
                    cow.moveTo(pos.getX() + 2.5, pos.getY(), pos.getZ() + 0.5, 0, 0);
                    world.addFreshEntity(cow);
                    entityId = cow.getId();
                }),
                Step.run(5, () -> {
                    ServerLevel world = level();
                    check(world.getBlockState(pos).is(BlockRegistry.SURTRUM_AIR.get()), "surtrum air went away before it was opened");

                    world.setBlockAndUpdate(pos.above(), Blocks.AIR.defaultBlockState());
                    check(world.getBlockState(pos).isAir(), "surtrum air is still there " + world.getBlockState(pos));
                    check(world.getEntity(entityId).getRemainingFireTicks() > 0, "cow next to it is not burning");
                })
        );
    }
}
