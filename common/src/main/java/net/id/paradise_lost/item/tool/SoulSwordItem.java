package net.id.paradise_lost.item.tool;

import com.google.common.collect.ImmutableList;
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
            List<String> currentSouls = stack.getComponents().has(ParadiseLostDataComponentTypes.COLLECTED_SOULS) ? stack.get(ParadiseLostDataComponentTypes.COLLECTED_SOULS).soulIds() : new LinkedList<>();
            var entityName = target.getType().getDescriptionId();
            if (!currentSouls.contains(entityName)) {
                var newSouls = new LinkedList<>(currentSouls);
                newSouls.add(entityName);
                stack.remove(ParadiseLostDataComponentTypes.COLLECTED_SOULS);
                stack.set(ParadiseLostDataComponentTypes.COLLECTED_SOULS, new ParadiseLostDataComponentTypes.CollectedSoulsComponent(newSouls));
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
            tooltip.addAll(ImmutableList.of(Component.translatable("info.paradise_lost.soul_blade.soul_count_n", getSoulCount(stack)).withStyle(ChatFormatting.LIGHT_PURPLE)));
        }
        super.appendHoverText(stack, context, tooltip, type);
    }

    private int getSoulCount(ItemStack itemStack) {
        return (itemStack != null && itemStack.getComponents().has(ParadiseLostDataComponentTypes.COLLECTED_SOULS)) ? itemStack.get(ParadiseLostDataComponentTypes.COLLECTED_SOULS).soulCount() : 0;
    }

    private void playCollectEffects(Level world, BlockPos pos) {
        world.playSound(null, pos, ParadiseLostSoundEvents.SOUL_BLADE_HARVEST, SoundSource.PLAYERS, 1.5F, 0.0F);
    }

}
