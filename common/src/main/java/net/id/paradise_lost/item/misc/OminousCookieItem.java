package net.id.paradise_lost.item.misc;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class OminousCookieItem extends Item {
    /** One eighth of {@link net.minecraft.world.item.OminousBottleItem#EFFECT_DURATION} (120000 / 8). */
    public static final int BAD_OMEN_DURATION = 15000;

    public OminousCookieItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
        Integer amplifier = stack.get(DataComponents.OMINOUS_BOTTLE_AMPLIFIER);
        ItemStack result = super.finishUsingItem(stack, world, user);
        if (!world.isClientSide && amplifier != null) {
            user.addEffect(new MobEffectInstance(MobEffects.BAD_OMEN, BAD_OMEN_DURATION, amplifier));
        }
        return result;
    }
}
