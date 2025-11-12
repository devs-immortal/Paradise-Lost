package net.id.paradiselost.items.tools;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.paradiselost.items.utils.ParadiseLostDataComponentTypes;
import net.id.paradiselost.util.ParadiseLostSoundEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.LinkedList;
import java.util.List;

public class SoulSwordItem extends SwordItem {
    public SoulSwordItem(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, settings);
    }

    @Override
    public float getBonusAttackDamage(Entity target, float baseAttackDamage, DamageSource damageSource) {
        var itemStack = damageSource.getWeaponStack();
        var bonusSoulDamage = getSoulCount(itemStack) * 0.25F;
        return super.getBonusAttackDamage(target, baseAttackDamage, damageSource) + bonusSoulDamage;
    }

    @Override
    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (target.isDead()) {
            List<String> currentSouls = stack.getComponents().contains(ParadiseLostDataComponentTypes.COLLECTED_SOULS) ? stack.get(ParadiseLostDataComponentTypes.COLLECTED_SOULS).soulIds() : new LinkedList<>();
            var entityName = target.getType().getTranslationKey();
            if (!currentSouls.contains(entityName)) {
                var newSouls = new LinkedList<>(currentSouls);
                newSouls.add(entityName);
                stack.remove(ParadiseLostDataComponentTypes.COLLECTED_SOULS);
                stack.set(ParadiseLostDataComponentTypes.COLLECTED_SOULS, new ParadiseLostDataComponentTypes.CollectedSoulsComponent(newSouls));
                playCollectEffects(attacker.getWorld(), attacker.getBlockPos());
            }
        }
        stack.damage(1, attacker, EquipmentSlot.MAINHAND);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        var soulCount = getSoulCount(stack);
        if (soulCount == 1) {
            tooltip.addAll(ImmutableList.of(Text.translatable("info.paradise_lost.soul_blade.soul_count_1").formatted(Formatting.LIGHT_PURPLE)));
        } else {
            tooltip.addAll(ImmutableList.of(Text.translatable("info.paradise_lost.soul_blade.soul_count_n", getSoulCount(stack)).formatted(Formatting.LIGHT_PURPLE)));
        }
        super.appendTooltip(stack, context, tooltip, type);
    }

    private int getSoulCount(ItemStack itemStack) {
        return (itemStack != null && itemStack.getComponents().contains(ParadiseLostDataComponentTypes.COLLECTED_SOULS)) ? itemStack.get(ParadiseLostDataComponentTypes.COLLECTED_SOULS).soulCount() : 0;
    }

    private void playCollectEffects(World world, BlockPos pos) {
        world.playSound(null, pos, ParadiseLostSoundEvents.ITEM_BLOODSTONE_PRICK, SoundCategory.PLAYERS, 0.5F, 0.5F); // TODO
    }

}
