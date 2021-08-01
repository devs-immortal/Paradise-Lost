package com.aether.items.food;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class WhiteApple extends Item {
    public WhiteApple(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stackIn, World worldIn, LivingEntity entityIn) {
        /*if (entityIn instanceof PlayerEntity) {
            AetherAPI.get((PlayerEntity) entityIn).inflictCure(300);
        }*/
        return super.finishUsing(stackIn, worldIn, entityIn);
    }
}