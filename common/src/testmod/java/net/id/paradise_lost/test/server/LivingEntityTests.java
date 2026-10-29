package net.id.paradise_lost.test.server;

import net.id.paradise_lost.entity.passive.moa.MoaAttributes;
import net.id.paradise_lost.entity.passive.moa.MoaEntity;
import net.id.paradise_lost.entity.passive.moa.MoaGenes;
import net.id.paradise_lost.item.armor.FloatyLeggingsItem;
import net.id.paradise_lost.test.Step;
import net.id.paradise_lost.world.dimension.ParadiseLostDimension;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static net.id.paradise_lost.registry.ItemRegistry.*;
import static net.id.paradise_lost.test.TestHelpers.*;
import static net.id.paradise_lost.test.server.ServerTestHelpers.ahead;
import static net.id.paradise_lost.test.server.ServerTestHelpers.level;

public final class LivingEntityTests {
    private LivingEntityTests() {
    }

    private static Zombie zombie(ServerLevel world, BlockPos pos) {
        Zombie zombie = EntityType.ZOMBIE.create(world);
        zombie.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        zombie.setNoAi(true);
        return zombie;
    }

    private static void checkEffect(List<String> wrong, Zombie zombie, MobEffectInstance expected) {
        MobEffectInstance actual = zombie.getEffect(expected.getEffect());
        expect(wrong, actual != null && actual.getDuration() == expected.getDuration() && actual.getAmplifier() == expected.getAmplifier(),
                "expected " + expected + " but got " + actual);
    }

    @ServerTest("levitation totem saves a mob from void damage in Paradise Lost")
    private static Step voidTotem() {
        return Step.run(0, () -> {
            ServerLevel paradise = ServerTestRunner.server().getLevel(ParadiseLostDimension.PARADISE_LOST_WORLD_KEY);
            Zombie zombie = zombie(paradise, BlockPos.ZERO.atY(paradise.getMinBuildHeight() - 70));
            zombie.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(TOTEM_OF_LEVITATION.get()));

            List<String> wrong = new ArrayList<>();
            expect(wrong, !zombie.hurt(paradise.damageSources().fellOutOfWorld(), 4), "void damage went through");
            expect(wrong, zombie.getMainHandItem().isEmpty(), "totem was not used up");
            checkEffect(wrong, zombie, new MobEffectInstance(MobEffects.LEVITATION, 110, 30));
            checkEffect(wrong, zombie, new MobEffectInstance(MobEffects.SLOW_FALLING, 320, 1));
            checkAll(wrong);
        });
    }

    @ServerTest("levitation totem saves a mob from a big fall")
    private static Step fallTotem() {
        return Step.run(0, () -> {
            ServerLevel world = level();
            Zombie zombie = zombie(world, ahead(3, 0));
            zombie.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(TOTEM_OF_LEVITATION.get()));

            List<String> wrong = new ArrayList<>();
            expect(wrong, !zombie.hurt(world.damageSources().fall(), 15), "fall damage went through");
            expect(wrong, zombie.getMainHandItem().isEmpty(), "totem was not used up");
            checkEffect(wrong, zombie, new MobEffectInstance(MobEffects.SLOW_FALLING, 15, 1));
            checkAll(wrong);
        });
    }

    @ServerTest("mob killed with an igniting tool drops smelted loot")
    private static Step ignitingToolSmeltsDrops() {
        return Step.run(0, () -> {
            ServerLevel world = level();
            BlockPos pos = ahead(3, 0);
            Cow cow = EntityType.COW.create(world);
            cow.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
            cow.setNoAi(true);
            world.addFreshEntity(cow);
            Zombie attacker = zombie(world, ahead(4, 0));
            attacker.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(SURTRUM_SWORD.get()));

            cow.hurt(world.damageSources().mobAttack(attacker), 1000);

            Map<Item, Integer> drops = drops(world, pos, 3);
            check(drops.containsKey(Items.COOKED_BEEF) && !drops.containsKey(Items.BEEF), "dropped " + describe(drops));
        });
    }

    @ServerTest("floaty boots soften a fall")
    private static Step floatyBootsFall() {
        return Step.run(0, () -> {
            ServerLevel world = level();
            Zombie plain = zombie(world, ahead(3, 0));
            Zombie booted = zombie(world, ahead(3, 1));
            booted.setItemSlot(EquipmentSlot.FEET, new ItemStack(FLOATY_BOOTS.get()));

            plain.causeFallDamage(10, 1, world.damageSources().fall());
            booted.causeFallDamage(10, 1, world.damageSources().fall());

            List<String> wrong = new ArrayList<>();
            expect(wrong, plain.getHealth() < plain.getMaxHealth(), "plain zombie took no fall damage");
            expect(wrong, booted.getHealth() == booted.getMaxHealth(), "booted zombie took " + (booted.getMaxHealth() - booted.getHealth()) + " fall damage");
            checkAll(wrong);
        });
    }

    @ServerTest("broken floaty leggings give no armor")
    private static List<Step> brokenFloatyLeggings() {
        Zombie[] zombie = new Zombie[1];
        ItemStack leggings = new ItemStack(FLOATY_LEGGINGS.get());
        return List.of(
                Step.run(0, () -> {
                    ServerLevel world = level();
                    zombie[0] = zombie(world, ahead(3, 0));
                    zombie[0].setItemSlot(EquipmentSlot.LEGS, leggings);
                    world.addFreshEntity(zombie[0]);
                }),
                Step.run(2, () -> {
                    double whole = zombie[0].getAttributeValue(Attributes.ARMOR);
                    leggings.setDamageValue(leggings.getMaxDamage() - 1);
                    double broken = zombie[0].getAttributeValue(Attributes.ARMOR);
                    zombie[0].discard();
                    check(broken == whole - FloatyLeggingsItem.getDefensePoints(FLOATY_LEGGINGS.get()), "armor went from " + whole + " to " + broken + " when the leggings broke");
                })
        );
    }

    @ServerTest("moa max health comes from its genes")
    private static Step moaMaxHealth() {
        return Step.run(0, () -> {
            ServerLevel world = level();
            MoaEntity moa = MoaGenes.getMoaFromEgg(world, MoaTests.goldenrodEgg(world, false), UUID.randomUUID(), ahead(3, 0));
            check(moa.getGenes().isInitialized(), "genes are not initialized");
            float expected = moa.getGenes().getAttribute(MoaAttributes.MAX_HEALTH);
            check(moa.getMaxHealth() == expected, "max health is " + moa.getMaxHealth() + " expected " + expected);
        });
    }
}
