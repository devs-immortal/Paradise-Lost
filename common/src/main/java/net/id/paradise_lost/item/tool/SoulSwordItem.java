package net.id.paradise_lost.item.tool;

import com.google.common.collect.ImmutableList;
import net.id.paradise_lost.enchantment.ParadiseLostEnchantmentHelper;
import net.id.paradise_lost.item.ParadiseLostDataComponentTypes;
import net.id.paradise_lost.util.ParadiseLostCriteria;
import net.id.paradise_lost.util.ParadiseLostSoundEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.LinkedList;
import java.util.List;

public class SoulSwordItem extends SwordItem {
    private static final String RENDING_BONUS_SOUL_PREFIX = "paradise_lost:rending_soul/";

    public SoulSwordItem(Tier toolMaterial, Properties settings) {
        super(toolMaterial, settings);
    }

    @Override
    public float getAttackDamageBonus(Entity target, float baseAttackDamage, DamageSource damageSource) {
        var itemStack = damageSource.getWeaponItem();
        var bonusSoulDamage = getSoulCount(itemStack) * 0.25F;
        return super.getAttackDamageBonus(target, baseAttackDamage, damageSource) + bonusSoulDamage;
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (target.isDeadOrDying()) {
            var current = stack.getOrDefault(
                    ParadiseLostDataComponentTypes.COLLECTED_SOULS,
                    new ParadiseLostDataComponentTypes.CollectedSoulsComponent(List.of())
            );
            var newSouls = new LinkedList<>(current.soulIds());
            boolean gainedSoul = false;

            var entityName = target.getType().getDescriptionId();
            if (!newSouls.contains(entityName)) {
                newSouls.add(entityName);
                gainedSoul = true;
            }

            if (!attacker.level().isClientSide()
                    && ParadiseLostEnchantmentHelper.rollExtraSoul(stack, attacker.level(), attacker.getRandom())) {
                newSouls.add(RENDING_BONUS_SOUL_PREFIX + newSouls.size());
                gainedSoul = true;
            }

            if (gainedSoul) {
                stack.set(
                        ParadiseLostDataComponentTypes.COLLECTED_SOULS,
                        new ParadiseLostDataComponentTypes.CollectedSoulsComponent(newSouls)
                );
                playCollectEffects(attacker.level(), attacker.blockPosition());
                if (attacker instanceof ServerPlayer serverPlayer && newSouls.size() >= 50) {
                    ParadiseLostCriteria.BLOOMED_BLADE_GOAL.trigger(serverPlayer, attacker.blockPosition(), stack);
                }
            }
        }
        stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
        var soulCount = getSoulCount(stack);
        if (soulCount == 1) {
            tooltip.addAll(ImmutableList.of(Component.translatable("info.paradise_lost.soul_blade.soul_count_1").withStyle(ChatFormatting.LIGHT_PURPLE)));
        } else {
            tooltip.addAll(ImmutableList.of(Component.translatable("info.paradise_lost.soul_blade.soul_count_n", soulCount).withStyle(ChatFormatting.LIGHT_PURPLE)));
        }
        super.appendHoverText(stack, context, tooltip, type);
    }

    private int getSoulCount(ItemStack itemStack) {
        if (itemStack == null) {
            return 0;
        }
        return itemStack.getOrDefault(
                ParadiseLostDataComponentTypes.COLLECTED_SOULS,
                new ParadiseLostDataComponentTypes.CollectedSoulsComponent(List.of())
        ).soulCount();
    }

    private void playCollectEffects(Level world, BlockPos pos) {
        world.playSound(null, pos, ParadiseLostSoundEvents.SOUL_BLADE_HARVEST, SoundSource.PLAYERS, 1.5F, 0.0F);
    }
}
