package net.id.paradise_lost.test.server;

import net.id.paradise_lost.registry.BlockRegistry;
import net.id.paradise_lost.test.Step;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static net.id.paradise_lost.registry.ItemRegistry.*;
import static net.id.paradise_lost.test.TestHelpers.*;
import static net.id.paradise_lost.test.server.ServerTestHelpers.*;

public final class ItemTests {
    private static BlockPos pos;
    private static int entityId;

    private ItemTests() {
    }

    @ServerTest(value = "armor and tools keep their stats, repair items and tags", group = "Items")
    private static Step armorAndTools() {
        return Step.run(0, () -> {
            List<String> wrong = new ArrayList<>();

            expect(wrong, new ItemStack(OLVITE_HELMET.get()).getMaxDamage() == ArmorItem.Type.HELMET.getDurability(15), "olvite helmet durability");
            expect(wrong, new ItemStack(SURTRUM_CHESTPLATE.get()).getMaxDamage() == ArmorItem.Type.CHESTPLATE.getDurability(27), "surtrum chestplate durability");
            expect(wrong, new ItemStack(FLOATY_BOOTS.get()).getMaxDamage() == ArmorItem.Type.BOOTS.getDurability(66), "floaty boots durability");

            expect(wrong, new ItemStack(SURTRUM_CHESTPLATE.get()).has(DataComponents.FIRE_RESISTANT), "surtrum armor is not fire resistant");

            expect(wrong, new ItemStack(OLVITE_CHESTPLATE.get()).getRarity() == Rarity.COMMON, "olvite chestplate rarity");
            expect(wrong, new ItemStack(XP_CIRCLET.get()).getRarity() == Rarity.RARE, "xp circlet rarity");
            expect(wrong, new ItemStack(FLOATY_LEGGINGS.get()).getRarity() == Rarity.UNCOMMON, "floaty leggings rarity");

            expect(wrong, repairs(OLVITE_HELMET.get(), OLVITE.get()), "olvite armor repair");
            expect(wrong, repairs(GLAZED_GOLD_BOOTS.get(), GOLDEN_AMBER.get()), "glazed gold armor repair");
            expect(wrong, repairs(SURTRUM_LEGGINGS.get(), REFINED_SURTRUM.get()), "surtrum armor repair");
            expect(wrong, repairs(XP_CIRCLET.get(), BlockRegistry.FLOESTONE.get().asItem()), "relic armor repair");
            expect(wrong, repairs(FLOATY_BOOTS.get(), FLAXWEAVE.get()), "floaty armor repair");
            expect(wrong, repairs(OLVITE_PICKAXE.get(), OLVITE.get()), "olvite tool repair");
            expect(wrong, repairs(SURTRUM_SWORD.get(), REFINED_SURTRUM.get()), "surtrum tool repair");
            expect(wrong, repairs(SOUL_BLADE.get(), BlockRegistry.BLOOMED_CALCITE.get().asItem()), "soul blade repair");

            expect(wrong, new ItemStack(OLVITE_PICKAXE.get()).isCorrectToolForDrops(Blocks.IRON_ORE.defaultBlockState()), "olvite pickaxe cannot mine iron ore");
            expect(wrong, !new ItemStack(OLVITE_PICKAXE.get()).isCorrectToolForDrops(Blocks.OBSIDIAN.defaultBlockState()), "olvite pickaxe mines obsidian");
            expect(wrong, new ItemStack(SURTRUM_PICKAXE.get()).isCorrectToolForDrops(Blocks.OBSIDIAN.defaultBlockState()), "surtrum pickaxe cannot mine obsidian");

            ArmorStand stand = new ArmorStand(level(), 0, 0, 0);
            stand.setItemSlot(EquipmentSlot.HEAD, new ItemStack(GLAZED_GOLD_HELMET.get()));
            expect(wrong, PiglinAi.isWearingGold(stand), "glazed gold is not piglin safe");

            checkAll(wrong);
        });
    }

    @ServerTest(value = "fuel and compost values are registered", group = "Items")
    private static Step fuelAndCompost() {
        return Step.run(0, () -> {
            List<String> wrong = new ArrayList<>();
            Map<Item, Integer> fuel = AbstractFurnaceBlockEntity.getFuel();

            expect(wrong, fuel.getOrDefault(CHERINE.get(), 0) == 500, "cherine burn time " + fuel.getOrDefault(CHERINE.get(), 0));
            expect(wrong, fuel.getOrDefault(AUREL_BOATS.boat().get(), 0) == 1200, "aurel boat burn time");
            expect(wrong, fuel.getOrDefault(BlockRegistry.AUREL_WOODSTUFF.plank().get().asItem(), 0) == 300, "aurel planks burn time");

            expect(wrong, ComposterBlock.COMPOSTABLES.getFloat(SWEDROOT_PULP.get()) == 0.3F, "swedroot pulp compost chance");

            checkAll(wrong);
        });
    }

    @ServerTest(value = "can go on a mob or armor stand head", group = "XP circlet")
    private static Step xpCircletOnMobs() {
        return Step.run(0, () -> {
            ServerLevel world = level();
            List<String> wrong = new ArrayList<>();

            for (EntityType<? extends LivingEntity> type : List.of(EntityType.ARMOR_STAND, EntityType.PIG)) {
                LivingEntity entity = type.spawn(world, ahead(3, 0), MobSpawnType.COMMAND);
                entity.tick();
                try {
                    entity.setItemSlot(EquipmentSlot.HEAD, new ItemStack(XP_CIRCLET.get()));
                } catch (RuntimeException e) {
                    wrong.add(EntityType.getKey(type) + " threw " + e);
                }
                entity.discard();
            }

            checkAll(wrong);
        });
    }

    @ServerTest(value = "dispenser puts it on an armor stand", group = "XP circlet")
    private static List<Step> xpCircletDispenser() {
        return List.of(
                Step.run(0, () -> {
                    ServerLevel world = level();
                    pos = ahead(3, 0);

                    world.setBlockAndUpdate(pos, Blocks.DISPENSER.defaultBlockState().setValue(DispenserBlock.FACING, FACING));
                    ((DispenserBlockEntity) world.getBlockEntity(pos)).setItem(0, new ItemStack(XP_CIRCLET.get()));

                    entityId = EntityType.ARMOR_STAND.spawn(world, pos.relative(FACING), MobSpawnType.COMMAND).getId();
                }),
                Step.run(5, () -> level().setBlockAndUpdate(pos.relative(FACING.getOpposite()), Blocks.REDSTONE_BLOCK.defaultBlockState())),
                Step.until("armor stand to wear the circlet", 20, () ->
                        ((LivingEntity) level().getEntity(entityId)).getItemBySlot(EquipmentSlot.HEAD).is(XP_CIRCLET.get()))
        );
    }

    @ServerTest(value = "dispenser places water from an aurel water bucket", group = "Aurel bucket")
    private static List<Step> aurelBucketDispenser() {
        return dispenseAurelBucket(AUREL_WATER_BUCKET.get(), Blocks.WATER);
    }

    @ServerTest(value = "dispenser places powder snow from an aurel powder snow bucket", group = "Aurel bucket")
    private static List<Step> aurelBucketDispenserPowderSnow() {
        return dispenseAurelBucket(AUREL_POWDER_SNOW_BUCKET.get(), Blocks.POWDER_SNOW);
    }

    private static List<Step> dispenseAurelBucket(Item bucket, Block placed) {
        return List.of(
                Step.run(0, () -> {
                    ServerLevel world = level();
                    BlockPos dispenser = ahead(3, 0);
                    pos = dispenser.relative(FACING);

                    world.setBlockAndUpdate(dispenser, Blocks.DISPENSER.defaultBlockState().setValue(DispenserBlock.FACING, FACING));
                    ((DispenserBlockEntity) world.getBlockEntity(dispenser)).setItem(0, new ItemStack(bucket));
                    world.setBlockAndUpdate(dispenser.relative(FACING.getOpposite()), Blocks.REDSTONE_BLOCK.defaultBlockState());
                }),
                Step.until("block in front of the dispenser", 20, () -> level().getBlockState(pos).is(placed)),
                Step.run(0, () -> check(((DispenserBlockEntity) level().getBlockEntity(pos.relative(FACING.getOpposite()))).getItem(0).is(AUREL_BUCKET.get()),
                        "dispenser does not hold an empty aurel bucket"))
        );
    }

    @ServerTest(value = "a mob in boots lands on farmland without trampling it", group = "Floaty")
    private static List<Step> floatyBootsSpareFarmland() {
        return landOnFarmland(Blocks.FARMLAND, true);
    }

    @ServerTest(value = "a mob in boots lands on Paradise Lost farmland without trampling it", group = "Floaty")
    private static List<Step> floatyBootsSpareParadiseLostFarmland() {
        return landOnFarmland(BlockRegistry.FARMLAND.get(), true);
    }

    @ServerTest(value = "a mob without boots tramples farmland", group = "Floaty")
    private static List<Step> farmlandTrampledWithoutBoots() {
        return landOnFarmland(Blocks.FARMLAND, false);
    }

    @ServerTest(value = "a mob without boots tramples Paradise Lost farmland", group = "Floaty")
    private static List<Step> paradiseLostFarmlandTrampledWithoutBoots() {
        return landOnFarmland(BlockRegistry.FARMLAND.get(), false);
    }

    @ServerTest(value = "Paradise Lost farmland dries out into Paradise Lost dirt", group = "Farmland")
    private static Step farmlandDriesOut() {
        return Step.run(0, () -> {
            ServerLevel world = level();
            pos = ahead(3, 2).below();
            // nothing wet within reach, and no moisture left
            world.setBlockAndUpdate(pos, BlockRegistry.FARMLAND.get().defaultBlockState().setValue(FarmBlock.MOISTURE, 0));
            BlockState farmland = world.getBlockState(pos);
            farmland.randomTick(world, pos, world.random);

            BlockState state = world.getBlockState(pos);
            check(state.is(BlockRegistry.DIRT.get()), "dried out into " + state);
        });
    }

    @ServerTest(value = "Paradise Lost farmland under a solid block turns into Paradise Lost dirt", group = "Farmland")
    private static Step farmlandBuiltOver() {
        return Step.run(0, () -> {
            ServerLevel world = level();
            pos = ahead(3, 4).below();
            world.setBlockAndUpdate(pos, BlockRegistry.FARMLAND.get().defaultBlockState().setValue(FarmBlock.MOISTURE, 7));
            world.setBlockAndUpdate(pos.above(), Blocks.STONE.defaultBlockState());
            BlockState farmland = world.getBlockState(pos);
            farmland.tick(world, pos, world.random);

            BlockState state = world.getBlockState(pos);
            check(state.is(BlockRegistry.DIRT.get()), "built over into " + state);
        });
    }

    private static List<Step> landOnFarmland(Block farmland, boolean boots) {
        return List.of(
                Step.run(0, () -> {
                    ServerLevel world = level();
                    pos = ahead(3, 0).below();
                    world.setBlockAndUpdate(pos, farmland.defaultBlockState().setValue(FarmBlock.MOISTURE, 7));
                    Pig pig = EntityType.PIG.spawn(world, pos.above(6), MobSpawnType.COMMAND);
                    if (boots) {
                        pig.setItemSlot(EquipmentSlot.FEET, new ItemStack(FLOATY_BOOTS.get()));
                    }
                    entityId = pig.getId();
                }),
                Step.until("pig to land", 100, () -> {
                    Entity pig = level().getEntity(entityId);
                    check(pig != null, "pig is gone");
                    // the farmland of an unprotected pig is gone the tick it lands
                    return pig.onGround() || !level().getBlockState(pos).is(farmland);
                }),
                Step.run(5, () -> {
                    BlockState state = level().getBlockState(pos);
                    if (boots) {
                        check(state.is(farmland), "farmland was trampled into " + state);
                    } else {
                        Block dirt = farmland == Blocks.FARMLAND ? Blocks.DIRT : BlockRegistry.DIRT.get();
                        check(state.is(dirt), "trampled into " + state + ", expected " + dirt);
                    }
                })
        );
    }

    private static boolean repairs(Item item, Item material) {
        return item.isValidRepairItem(new ItemStack(item), new ItemStack(material));
    }
}
