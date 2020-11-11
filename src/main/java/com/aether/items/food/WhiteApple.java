package com.aether.items.food;

import com.aether.api.AetherAPI;
import com.aether.items.AetherItemGroups;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class WhiteApple extends Item {

    public WhiteApple() {
        super(new Item.Settings().group(AetherItemGroups.Food).food(AetherFood.WHITE_APPLE));
    }

    @Override
    public ItemStack finishUsing(ItemStack stackIn, World worldIn, LivingEntity entityIn) {
        if (entityIn instanceof PlayerEntity) AetherAPI.get((PlayerEntity) entityIn).inflictCure(300);
        return super.finishUsing(stackIn, worldIn, entityIn);
    }
}