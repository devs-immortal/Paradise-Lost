package com.aether.entities.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.world.World;

public abstract class DartEntity extends PersistentProjectileEntity {

    private int ticksInAir;

    public DartEntity(EntityType<? extends PersistentProjectileEntity> entityType, double x, double y, double z, World world) {
        super(entityType, x, y, z, world);
        this.setNoGravity(true);
    }

    public DartEntity(EntityType<? extends PersistentProjectileEntity> entityType, LivingEntity owner, World world) {
        super(entityType, owner, world);
        this.setNoGravity(true);
    }

    public DartEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
        this.setNoGravity(true);
    }

    public DartEntity(World world) {
        super(EntityType.ARROW, world);
        this.setNoGravity(true);
    }

    @Override
    public void tick() {
        if (!this.world.isClient) {
            if (!this.onGround) ++this.ticksInAir;

            if (this.ticksInAir == 500) {
                this.discard();
                return;
            }
        }

        super.tick();
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this, 1 + (this.getOwner() == null ? this.getId() : this.getOwner().getId()));
    }
}