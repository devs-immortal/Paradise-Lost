package com.aether.items.food;

import com.aether.items.AetherItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class DrinkableItem extends Item {

    public DrinkableItem(Settings settings) {
        super(settings);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if(!(user instanceof PlayerEntity) || !((PlayerEntity) user).isCreative()) {
            if (this == AetherItems.AETHER_MILK) {
                return new ItemStack(AetherItems.QUICKSOIL_VIAL);
            }
        }
        return super.finishUsing(stack, world, user);
    }
}
