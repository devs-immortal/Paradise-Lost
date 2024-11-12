package net.id.paradiselost.items.misc;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TotemOfLevitationItem extends Item {
    public TotemOfLevitationItem(Settings settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient && entity instanceof PlayerEntity player) {
            BlockPos belowPlayer = player.getBlockPos().down(6);

            if (player.getVelocity().y < -1.5 && !world.isAir(belowPlayer) && !world.isWater(belowPlayer) && selected) {
                player.getWorld().sendEntityStatus(player, (byte) 35); // mc totem animation
                player.setVelocity(player.getVelocity().x,0.6d, player.getVelocity().z);
                player.velocityModified = true;
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 15, 1));
                //player.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 7, 2));


                if (!player.getAbilities().creativeMode) {
                    stack.decrement(1);

                }
            }
        }
    }
}