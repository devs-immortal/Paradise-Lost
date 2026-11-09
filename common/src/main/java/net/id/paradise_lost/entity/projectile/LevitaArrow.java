package net.id.paradise_lost.entity.projectile;

import net.id.paradise_lost.particle.ParadiseLostParticleTypes;
import net.id.paradise_lost.registry.EntityRegistry;
import net.id.paradise_lost.registry.ItemRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class LevitaArrow extends AbstractArrow {
    private static final double CRIT_BURST_RADIUS = 1.5;
    private static final int CRIT_BURST_PARTICLES = 16;

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
    protected void onHit(HitResult result) {
        boolean crit = this.isCritArrow();
        double speed = this.getDeltaMovement().length();
        super.onHit(result);
        if (!crit || !(this.level() instanceof ServerLevel server)) {
            return;
        }
        Vec3 pos = result.getLocation();
        server.sendParticles(ParticleTypes.EXPLOSION, pos.x, pos.y, pos.z, 1, 0.0, 0.0, 0.0, 0.0);
        RandomSource random = this.random;
        for (int i = 0; i < CRIT_BURST_PARTICLES; i++) {
            Vec3 point = pos.add(randomInsideSphere(random, CRIT_BURST_RADIUS));
            server.sendParticles(ParticleTypes.POOF, point.x, point.y, point.z, 1, 0.0, 0.0, 0.0, 0.0);
        }
        float regularHit = Mth.ceil(Mth.clamp(speed * this.getBaseDamage(), 0.0, Integer.MAX_VALUE));
        float splash = regularHit * 0.5F;
        if (splash <= 0.0F) {
            return;
        }
        Entity directHit = result.getType() == HitResult.Type.ENTITY ? ((EntityHitResult) result).getEntity() : null;
        Entity owner = this.getOwner();
        AABB area = new AABB(pos, pos).inflate(CRIT_BURST_RADIUS);
        var source = this.damageSources().arrow(this, owner == null ? this : owner);
        for (Entity entity : server.getEntities(this, area, candidate -> candidate != directHit && candidate != owner && candidate.isAlive())) {
            if (closestDistanceSqr(entity, pos) > CRIT_BURST_RADIUS * CRIT_BURST_RADIUS) {
                continue;
            }
            entity.hurt(source, splash);
        }
    }

    private static double closestDistanceSqr(Entity entity, Vec3 pos) {
        AABB box = entity.getBoundingBox();
        double x = Mth.clamp(pos.x, box.minX, box.maxX);
        double y = Mth.clamp(pos.y, box.minY, box.maxY);
        double z = Mth.clamp(pos.z, box.minZ, box.maxZ);
        return pos.distanceToSqr(x, y, z);
    }

    private static Vec3 randomInsideSphere(RandomSource random, double radius) {
        double x;
        double y;
        double z;
        do {
            x = random.nextDouble() * 2.0 - 1.0;
            y = random.nextDouble() * 2.0 - 1.0;
            z = random.nextDouble() * 2.0 - 1.0;
        } while (x * x + y * y + z * z > 1.0);
        return new Vec3(x * radius, y * radius, z * radius);
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
