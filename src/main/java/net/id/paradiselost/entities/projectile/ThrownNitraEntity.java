package net.id.paradiselost.entities.projectile;

import java.util.List;
import net.id.paradiselost.entities.ParadiseLostEntityTypes;
import net.id.paradiselost.items.ParadiseLostItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ThrownNitraEntity extends ThrownItemEntity {

    public ThrownNitraEntity(EntityType<? extends ThrownNitraEntity> entityType, World world) {
        super(entityType, world);
    }

    public ThrownNitraEntity(World world, LivingEntity owner) {
        super(ParadiseLostEntityTypes.THROWN_NITRA, owner, world);
    }

    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        doDamage();
    }

    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        doDamage();
        if (!this.getWorld().isClient) {
            this.getWorld().sendEntityStatus(this, (byte) 3);
            this.discard();
        }
    }

    public void handleStatus(byte status) {
        if (status == 3) {
            for (int i = 0; i < 4; i++) {
                this.getWorld().addParticle(ParticleTypes.EXPLOSION,
                        this.getX() + this.random.nextDouble() - 0.5, this.getY() + this.random.nextDouble() - 0.5, this.getZ() + this.random.nextDouble() - 0.5,
                        0.0, 0.0, 0.0
                );
            }
            this.getWorld().playSound(this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.NEUTRAL, 1.0F, 1.1F + this.random.nextFloat() * 0.4F, false);
        }

    }

    private void doDamage() {
        var hit = this.getWorld().getOtherEntities(this, new Box(this.getX() - 1.5, this.getY() - 1.5, this.getZ() - 1.5, this.getX() + 1.5, this.getY() + 1.5, this.getZ() + 1.5));
        for (Entity e : hit) {
            Vec3d diff = this.getPos().subtract(e.getPos()).negate().normalize();
            e.addVelocity(diff.x, diff.y, diff.z);
            e.damage(this.getWorld().getDamageSources().explosion(null, e), 2);
        }
    }

    @Override
    protected Item getDefaultItem() {
        return ParadiseLostItems.NITRA_BULB;
    }
}
