package net.id.paradiselost.entities.projectile;

import net.id.paradiselost.client.rendering.util.ParadiseLostEvents;
import net.id.paradiselost.entities.ParadiseLostEntityTypes;
import net.id.paradiselost.items.ParadiseLostItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ThrownNitraEntity extends ThrownItemEntity {

    public ThrownNitraEntity(EntityType<? extends ThrownNitraEntity> entityType, World world) {
        super(entityType, world);
    }

    public ThrownNitraEntity(World world, LivingEntity owner, ItemStack stack) {
        super(ParadiseLostEntityTypes.THROWN_NITRA, owner, world, stack);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        doDamage();
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        doDamage();
        if (!this.getWorld().isClient) {
            this.getWorld().sendEntityStatus(this, (byte) 3);
            this.discard();
        }
    }

    @Override
    public void handleStatus(byte status) {
        if (status == EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES) {
            this.getWorld().syncWorldEvent(ParadiseLostEvents.NITRA_EXPLODE, this.getBlockPos(), 0);
        }
    }

    private void doDamage() {
        var world = (ServerWorld) this.getWorld();
        var hit = world.getOtherEntities(this, new Box(this.getX() - 1.5, this.getY() - 1.5, this.getZ() - 1.5, this.getX() + 1.5, this.getY() + 1.5, this.getZ() + 1.5));
        for (Entity entity : hit) {
            Vec3d diff = this.getPos().subtract(entity.getPos()).negate().normalize();
            entity.addVelocity(diff.x, diff.y, diff.z);
            entity.damage(world, world.getDamageSources().explosion(null, entity), 2);
        }
    }

    @Override
    public boolean isOnFire() {
        return true;
    }

    @Override
    public boolean doesRenderOnFire() {
        return false;
    }

    @Override
    protected Item getDefaultItem() {
        return ParadiseLostItems.NITRA_BULB;
    }
}
