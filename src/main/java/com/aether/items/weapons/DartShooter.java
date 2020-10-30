package com.aether.items.weapons;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import com.aether.items.AetherItemGroups;

public class DartShooter extends Item {

    private final Dart ammo;

    public DartShooter(Dart ammo, Rarity rarity) {
        super(new Settings().maxCount(1).rarity(rarity).group(AetherItemGroups.WEAPONS));
        this.ammo = ammo;
    }

    protected ItemStack findDartStack(PlayerEntity playerIn) {
        if (playerIn.getStackInHand(Hand.OFF_HAND).getItem() == this.ammo) {
            return playerIn.getStackInHand(Hand.OFF_HAND);
        } else if (playerIn.getStackInHand(Hand.MAIN_HAND).getItem() == this.ammo) {
            return playerIn.getStackInHand(Hand.MAIN_HAND);
        } else {
            for (int index = 0; index < playerIn.inventory.size(); ++index) {
                ItemStack stack = playerIn.inventory.getStack(index);
                if (stack.getItem() == this.ammo) return stack;
            }
            return ItemStack.EMPTY;
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack heldItem = playerIn.getStackInHand(handIn);
        boolean bypassDartCheck = playerIn.abilities.creativeMode || EnchantmentHelper.getLevel(Enchantments.INFINITY, heldItem) > 0;

        ItemStack stack = this.findDartStack(playerIn);

        if (!stack.isEmpty() || bypassDartCheck) {
            if (stack.isEmpty()) {
                stack = new ItemStack(this.ammo);
            }

            ProjectileEntity projectile = this.ammo.createDart(worldIn, heldItem, playerIn);

            if (!worldIn.isClient) {
                projectile.setProperties(playerIn, playerIn.pitch, playerIn.yaw, 0.0F, 1.0F, 1.0F);
                worldIn.spawnEntity(projectile);
            }

//            worldIn.playSound(playerIn, playerIn.getBlockPos(), SoundsAether.DART_SHOOTER_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (playerIn.getRandom().nextFloat() * 0.4F + 0.8F));
        }

        return super.use(worldIn, playerIn, handIn);
    }
}