package net.id.paradise_lost.entity.projectile;

import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerLevel;
import net.id.paradise_lost.util.ParadiseLostEvents;
import net.id.paradise_lost.registry.EntityRegistry;
import net.id.paradise_lost.registry.ItemRegistry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class ThrownNitraEntity extends ThrowableItemProjectile {

    public ThrownNitraEntity(EntityType<? extends ThrownNitraEntity> entityType, Level world) {
        super(entityType, world);
    }

    public ThrownNitraEntity(Level world, LivingEntity owner, ItemStack stack) {
        super(EntityRegistry.THROWN_NITRA.get(), owner, world, stack);
    }

    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        doDamage();
    }

    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        doDamage();
        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, (byte) 3);
            this.discard();
        }
    }

    public void handleEntityEvent(byte status) {
        if (status == EntityEvent.DEATH) {
            this.level().levelEvent(ParadiseLostEvents.NITRA_EXPLODE, this.blockPosition(), 0);
        }

    }

    private void doDamage() {
        if (!(this.level() instanceof ServerLevel level)) {
            return;
        }
        var hit = level.getEntities(this, new AABB(this.getX() - 1.5, this.getY() - 1.5, this.getZ() - 1.5, this.getX() + 1.5, this.getY() + 1.5, this.getZ() + 1.5));
        for (Entity e : hit) {
            Vec3 diff = this.position().subtract(e.position()).reverse().normalize();
            e.push(diff.x, diff.y, diff.z);
            e.hurtServer(level, level.damageSources().explosion(null, e), 2);
        }
    }

    @Override
    public boolean isOnFire() {
        return true;
    }

    @Override
    public boolean displayFireAnimation() {
        return false;
    }

    @Override
    protected Item getDefaultItem() {
        return ItemRegistry.NITRA_BULB.get();
    }
}
