package com.aether.items.weapons;

import com.aether.items.AetherItems;
import com.aether.items.utils.AetherTiers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;

public class PigSlayer extends AetherSword {

    public PigSlayer() {
        super(AetherTiers.LEGENDARY, AetherItems.AETHER_LOOT, 3, -2.4F);
    }

    @Override
    public boolean postHit(ItemStack itemstack, LivingEntity entityliving, LivingEntity entityliving1) {
        if (entityliving == null || entityliving1 == null) return false;

        String s = entityliving.getType().getTranslationKey().toLowerCase();

        if (s.toLowerCase().contains("pig") || s.toLowerCase().contains("phyg")) {
            if (entityliving.getHealth() >= 0.0F) {
                entityliving.hurtTime = 0;
                entityliving.setHealth(1.0F);
                entityliving.damage(DamageSource.mob(entityliving1), 9999.0F);
            }

            for (int j = 0; j < 20; ++j) {
                double d = entityliving.world.random.nextGaussian() * 0.02D;
                double d1 = entityliving.world.random.nextGaussian() * 0.02D;
                double d2 = entityliving.world.random.nextGaussian() * 0.02D;
                double d3 = 5.0D;
                entityliving.world.addParticle(ParticleTypes.FLAME, (entityliving.getPos().getX() + (double) (entityliving.world.random.nextFloat() * entityliving.getWidth() * 2.0F)) - (double) entityliving.getWidth() - d * d3, (entityliving.getPos().getY() + (double) (entityliving.world.random.nextFloat() * entityliving.getHeight())) - d1 * d3, (entityliving.getPos().getZ() + (double) (entityliving.world.random.nextFloat() * entityliving.getWidth() * 2.0F)) - (double) entityliving.getWidth() - d2 * d3, d, d1, d2);
            }
            entityliving.remove();
        }
        return super.postHit(itemstack, entityliving, entityliving1);
    }
}