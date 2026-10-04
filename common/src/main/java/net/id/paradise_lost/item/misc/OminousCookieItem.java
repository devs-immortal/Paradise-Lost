package net.id.paradise_lost.item.misc;

import net.id.paradise_lost.item.ParadiseLostDataComponentTypes;
import java.util.List;

import net.id.paradise_lost.registry.ItemRegistry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;

public class OminousCookieItem extends Item {
    public static final int BAD_OMEN_DURATION = 15000;

    public OminousCookieItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
        Integer amplifier = stack.get(ParadiseLostDataComponentTypes.OMINOUS_COOKIE_AMPLIFIER);
        ItemStack result = super.finishUsingItem(stack, world, user);
        if (!world.isClientSide && amplifier != null) {
            user.addEffect(new MobEffectInstance(MobEffects.BAD_OMEN, BAD_OMEN_DURATION, amplifier, false, false, true));
        }
        return result;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
        super.appendHoverText(stack, context, tooltip, type);
        Integer amplifier = stack.getOrDefault(ParadiseLostDataComponentTypes.OMINOUS_COOKIE_AMPLIFIER, 0);
        List<MobEffectInstance> effects = List.of(
                new MobEffectInstance(MobEffects.BAD_OMEN, BAD_OMEN_DURATION, amplifier, false, false, true)
        );
        PotionContents.addPotionTooltip(effects, tooltip::add, 1.0F, context.tickRate());
    }
}
