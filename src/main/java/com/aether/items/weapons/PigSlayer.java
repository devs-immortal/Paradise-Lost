package com.aether.items.weapons;

import com.aether.items.utils.AetherTiers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;

public class PigSlayer extends AetherSword {

    public PigSlayer(Settings settings) {
        super(AetherTiers.Legendary, -2.4F, 3, settings);
    }

    @Override
    public boolean postHit(ItemStack itemStack, LivingEntity entityLiving, LivingEntity entityLiving1) {
        if (entityLiving == null || entityLiving1 == null) return false;

        String s = entityLiving.getType().getTranslationKey().toLowerCase();

        if (s.toLowerCase().contains("pig") || s.toLowerCase().contains("phyg")) {
            if (entityLiving.getHealth() >= 0.0F) {
                entityLiving.hurtTime = 0;
                entityLiving.setHealth(1.0F);
                entityLiving.damage(DamageSource.mob(entityLiving1), 9999.0F);
            }

            for (int j = 0; j < 20; ++j) {
                double d = entityLiving.world.random.nextGaussian() * 0.02D;
                double d1 = entityLiving.world.random.nextGaussian() * 0.02D;
                double d2 = entityLiving.world.random.nextGaussian() * 0.02D;
                double d3 = 5.0D;
                entityLiving.world.addParticle(ParticleTypes.FLAME, (entityLiving.getPos().getX() + (double) (entityLiving.world.random.nextFloat() * entityLiving.getWidth() * 2.0F)) - (double) entityLiving.getWidth() - d * d3, (entityLiving.getPos().getY() + (double) (entityLiving.world.random.nextFloat() * entityLiving.getHeight())) - d1 * d3, (entityLiving.getPos().getZ() + (double) (entityLiving.world.random.nextFloat() * entityLiving.getWidth() * 2.0F)) - (double) entityLiving.getWidth() - d2 * d3, d, d1, d2);
            }
            entityLiving.discard();
        }
        return super.postHit(itemStack, entityLiving, entityLiving1);
    }
}