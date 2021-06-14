package com.aether.items.weapons;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

public class DartShooter extends Item {

    private final Dart ammo;

    public DartShooter(Dart ammo, Properties settings) {
        super(settings);
        this.ammo = ammo;
    }

    protected ItemStack findDartStack(Player playerIn) {
        if (playerIn.getItemInHand(InteractionHand.OFF_HAND).getItem() == this.ammo) {
            return playerIn.getItemInHand(InteractionHand.OFF_HAND);
        } else if (playerIn.getItemInHand(InteractionHand.MAIN_HAND).getItem() == this.ammo) {
            return playerIn.getItemInHand(InteractionHand.MAIN_HAND);
        } else {
            for (int index = 0; index < playerIn.getInventory().getContainerSize(); ++index) {
                ItemStack stack = playerIn.getInventory().getItem(index);
                if (stack.getItem() == this.ammo) return stack;
            }
            return ItemStack.EMPTY;
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack heldItem = playerIn.getItemInHand(handIn);
        boolean bypassDartCheck = playerIn.isCreative() || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, heldItem) > 0;

        ItemStack stack = this.findDartStack(playerIn);
        if (!stack.isEmpty() || bypassDartCheck) {
            if (stack.isEmpty()) stack = new ItemStack(this.ammo);

            AbstractArrow projectile = this.ammo.createDart(worldIn, heldItem, playerIn);

            if (!worldIn.isClientSide) {
                projectile.shootFromRotation(playerIn, playerIn.getXRot(), playerIn.getYRot(), 0.0F, 1.0F, 1.0F);
                worldIn.addFreshEntity(projectile);

                if (!playerIn.isCreative()) {
                    projectile.pickup = AbstractArrow.Pickup.ALLOWED;
                    stack.shrink(1);
                } else if (playerIn.isCreative())
                    projectile.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
            }
            worldIn.playSound(playerIn, playerIn.blockPosition(), SoundEvents.CROSSBOW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (playerIn.getRandom().nextFloat() * 0.4F + 0.8F));
        }
        return super.use(worldIn, playerIn, handIn);
    }
}