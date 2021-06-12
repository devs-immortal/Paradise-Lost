package com.aether.items.weapons;

import com.aether.items.utils.AetherTiers;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class PigSlayer extends AetherSword {

    public PigSlayer(Properties settings) {
        super(AetherTiers.Legendary, -2.4F, 3, settings);
    }

    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity entityLiving, LivingEntity entityLiving1) {
        if (entityLiving == null || entityLiving1 == null) return false;

        String s = entityLiving.getType().getDescriptionId().toLowerCase();

        if (s.toLowerCase().contains("pig") || s.toLowerCase().contains("phyg")) {
            if (entityLiving.getHealth() >= 0.0F) {
                entityLiving.hurtTime = 0;
                entityLiving.setHealth(1.0F);
                entityLiving.hurt(DamageSource.mobAttack(entityLiving1), 9999.0F);
            }

            for (int j = 0; j < 20; ++j) {
                double d = entityLiving.level.random.nextGaussian() * 0.02D;
                double d1 = entityLiving.level.random.nextGaussian() * 0.02D;
                double d2 = entityLiving.level.random.nextGaussian() * 0.02D;
                double d3 = 5.0D;
                entityLiving.level.addParticle(ParticleTypes.FLAME, (entityLiving.position().x() + (double) (entityLiving.level.random.nextFloat() * entityLiving.getBbWidth() * 2.0F)) - (double) entityLiving.getBbWidth() - d * d3, (entityLiving.position().y() + (double) (entityLiving.level.random.nextFloat() * entityLiving.getBbHeight())) - d1 * d3, (entityLiving.position().z() + (double) (entityLiving.level.random.nextFloat() * entityLiving.getBbWidth() * 2.0F)) - (double) entityLiving.getBbWidth() - d2 * d3, d, d1, d2);
            }
            entityLiving.discard();
        }
        return super.hurtEnemy(itemStack, entityLiving, entityLiving1);
    }
}