package com.aether.entities.projectile;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;

public abstract class DartEntity extends AbstractArrow {

    private int ticksInAir;

    public DartEntity(EntityType<? extends AbstractArrow> entityType, double x, double y, double z, Level world) {
        super(entityType, x, y, z, world);
        this.setNoGravity(true);
    }

    public DartEntity(EntityType<? extends AbstractArrow> entityType, LivingEntity owner, Level world) {
        super(entityType, owner, world);
        this.setNoGravity(true);
    }

    public DartEntity(EntityType<? extends AbstractArrow> entityType, Level world) {
        super(entityType, world);
        this.setNoGravity(true);
    }

    public DartEntity(Level world) {
        super(EntityType.ARROW, world);
        this.setNoGravity(true);
    }

    @Override
    public void tick() {
        if (!this.level.isClientSide) {
            if (!this.onGround) ++this.ticksInAir;

            if (this.ticksInAir == 500) {
                this.discard();
                return;
            }
        }

        super.tick();
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this, 1 + (this.getOwner() == null ? this.getId() : this.getOwner().getId()));
    }
}