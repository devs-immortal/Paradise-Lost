package net.id.paradiselost.items.food;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class WhiteAppleItem extends Item {
    public WhiteAppleItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stackIn, World worldIn, LivingEntity entityIn) {
        /*if (entityIn instanceof PlayerEntity) {
            ParadiseLostAPI.get((PlayerEntity) entityIn).inflictCure(300);
        }*/
        return super.finishUsing(stackIn, worldIn, entityIn);
    }
}
