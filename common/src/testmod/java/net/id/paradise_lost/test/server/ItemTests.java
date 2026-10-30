package net.id.paradise_lost.test.server;

import net.id.paradise_lost.registry.BlockRegistry;
import net.id.paradise_lost.test.Step;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;

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
        return List.of(
                Step.run(0, () -> {
                    ServerLevel world = level();
                    BlockPos dispenser = ahead(3, 0);
                    pos = dispenser.relative(FACING);

                    world.setBlockAndUpdate(dispenser, Blocks.DISPENSER.defaultBlockState().setValue(DispenserBlock.FACING, FACING));
                    ((DispenserBlockEntity) world.getBlockEntity(dispenser)).setItem(0, new ItemStack(AUREL_WATER_BUCKET.get()));
                    world.setBlockAndUpdate(dispenser.relative(FACING.getOpposite()), Blocks.REDSTONE_BLOCK.defaultBlockState());
                }),
                Step.until("water in front of the dispenser", 20, () -> level().getBlockState(pos).is(Blocks.WATER))
        );
    }

    private static boolean repairs(Item item, Item material) {
        return item.isValidRepairItem(new ItemStack(item), new ItemStack(material));
    }
}
