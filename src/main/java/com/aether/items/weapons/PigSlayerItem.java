package com.aether.items.weapons;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;

import java.util.Random;

public class PigSlayerItem extends SwordItem {
    public PigSlayerItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean postHit(ItemStack itemStack, LivingEntity target, LivingEntity attacker) {
        if (target instanceof PigEntity && target.getHealth() >= 0f) {
            target.hurtTime = 0;
            target.damage(DamageSource.mob(attacker), 16777216f);

            Random random = target.world.getRandom();
            for (int i = 0; i < random.nextInt(12) + 24; i++) {
                double x = random.nextGaussian() * 0.02;
                double y = random.nextGaussian() * 0.03;
                double z = random.nextGaussian() * 0.02;
                target.world.addParticle(ParticleTypes.FLAME,
                        target.getPos().getX() + (random.nextFloat() * target.getWidth() * 2) - target.getWidth() - x * 5,
                        target.getPos().getY() + (random.nextFloat() * target.getHeight()) - y * 5,
                        target.getPos().getZ() + (random.nextFloat() * target.getWidth() * 2) - target.getWidth() - z * 5,
                        x * 0.75,
                        y * 1.5,
                        z * 0.75);
            }
        }

        return super.postHit(itemStack, target, attacker);
    }
}