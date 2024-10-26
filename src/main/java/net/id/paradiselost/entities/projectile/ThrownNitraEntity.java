package net.id.paradiselost.entities.projectile;

import net.id.paradiselost.client.rendering.util.ParadiseLostWorldEvents;
import net.id.paradiselost.entities.ParadiseLostEntityTypes;
import net.id.paradiselost.items.ParadiseLostItems;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.potion.Potions;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;

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
        if (status == EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES) {
            this.getWorld().syncWorldEvent(ParadiseLostWorldEvents.NITRA_EXPLODE, this.getBlockPos(), 0);
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
