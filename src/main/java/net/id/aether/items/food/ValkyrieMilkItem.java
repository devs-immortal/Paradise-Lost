package net.id.aether.items.food;

import net.id.aether.items.AetherItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class ValkyrieMilkItem extends Item {
    public ValkyrieMilkItem(Settings settings) {
        super(settings);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        return user instanceof PlayerEntity player && player.getAbilities().creativeMode ?
                super.finishUsing(stack, world, user) : new ItemStack(AetherItems.QUICKSOIL_VIAL);
    }
}
