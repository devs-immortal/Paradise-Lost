package com.aether.items.food;

import com.aether.items.AetherItemGroups;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class WhiteApple extends Item {

    public WhiteApple() {
        super(new Item.Settings().group(AetherItemGroups.FOOD).food(AetherFood.WHITE_APPLE));
    }

    @Override
    public ItemStack finishUsing(ItemStack stackIn, World worldIn, LivingEntity entityIn) {

        //TODO: Cure player

        return super.finishUsing(stackIn, worldIn, entityIn);
    }
}