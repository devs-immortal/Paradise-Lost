package net.id.aether.entities.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.world.World;

public abstract class DartEntity extends PersistentProjectileEntity {
    protected int ticksInAir;

    public DartEntity(EntityType<? extends DartEntity> entityType, double x, double y, double z, World world) {
        super(entityType, x, y, z, world);
        this.setNoGravity(true);
    }

    public DartEntity(EntityType<? extends DartEntity> entityType, LivingEntity owner, World world) {
        super(entityType, owner, world);
        this.setNoGravity(true);
    }

    public DartEntity(EntityType<? extends DartEntity> entityType, World world) {
        super(entityType, world);
        this.setNoGravity(true);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.onGround) {
            this.ticksInAir++;
        }

        if (this.ticksInAir > 500) {
            this.discard();
        }
    }
}