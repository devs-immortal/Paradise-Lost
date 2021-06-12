package com.aether.items.food;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class WhiteApple extends Item {

    public WhiteApple(Properties settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stackIn, Level worldIn, LivingEntity entityIn) {
        if (entityIn instanceof Player); //AetherAPI.get((PlayerEntity) entityIn).inflictCure(300);
        return super.finishUsingItem(stackIn, worldIn, entityIn);
    }
}