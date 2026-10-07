package net.id.paradise_lost.entity.projectile;

import net.id.paradise_lost.particle.ParadiseLostParticleTypes;
import net.id.paradise_lost.registry.EntityRegistry;
import net.id.paradise_lost.registry.ItemRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class LevitaArrow extends AbstractArrow {

    public LevitaArrow(EntityType<? extends LevitaArrow> entityType, Level world) {
        super(entityType, world);
    }

    public LevitaArrow(Level world, double x, double y, double z, ItemStack pickupItemStack, ItemStack firedFromWeapon) {
        super(EntityRegistry.LEVITA_ARROW.get(), x, y, z, world, pickupItemStack, firedFromWeapon);
    }

    public LevitaArrow(Level world, LivingEntity owner, ItemStack pickupItemStack, ItemStack firedFromWeapon) {
        super(EntityRegistry.LEVITA_ARROW.get(), owner, world, pickupItemStack, firedFromWeapon);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.inGround && this.level().isClientSide) {
            this.level().addParticle(ParadiseLostParticleTypes.LEVITA_SPARKLE, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
        }
    }

    @Override
    protected double getDefaultGravity() {
        return -super.getDefaultGravity();
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ItemRegistry.LEVITA_ARROW.get());
    }
}
