package net.id.aether.entities.projectile;

import net.id.aether.entities.AetherEntityTypes;
import net.id.aether.entities.hostile.CockatriceEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;

public class CockatriceSpitEntity extends ProjectileEntity {
    public CockatriceSpitEntity(EntityType<? extends CockatriceSpitEntity> entityType, World world) {
        super(entityType, world);
    }

    public CockatriceSpitEntity(World world, CockatriceEntity owner) {
        this(AetherEntityTypes.COCKATRICE_SPIT, world);
        this.setOwner(owner);
        this.setPosition(owner.getX() - (owner.getWidth() + 1.0F) * 0.5D * MathHelper.sin(owner.bodyYaw * 0.017453292F), owner.getEyeY() - 0.10000000149011612D, owner.getZ() + (owner.getWidth() + 1.0F) * 0.5D * MathHelper.cos(owner.bodyYaw * 0.017453292F));
    }

    @Override
    public void tick() {
        super.tick();
        Vec3d velocity = this.getVelocity();
        HitResult hitResult = ProjectileUtil.getCollision(this, this::canHit);
        this.onCollision(hitResult);
        double x = this.getX() + velocity.x;
        double y = this.getY() + velocity.y;
        double z = this.getZ() + velocity.z;
        this.updateRotation();
        if (this.world.getStatesInBox(this.getBoundingBox()).noneMatch(AbstractBlock.AbstractBlockState::isAir)) {
            this.discard();
        } else if (this.isInsideWaterOrBubbleColumn()) {
            this.discard();
        } else {
            this.setVelocity(velocity.multiply(0.99D));
            if (!this.hasNoGravity()) {
                this.setVelocity(this.getVelocity().add(0.0D, -0.06D, 0.0D));
            }

            this.setPosition(x, y, z);
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult hit) {
        super.onEntityHit(hit);
        Entity entity = this.getOwner();
        if (entity instanceof LivingEntity owner) {
            DamageSource damageSource = DamageSource.mobProjectile(this, owner).setProjectile();
            boolean successfulHit = hit.getEntity().damage(damageSource, 1.0F);
            if (successfulHit && hit.getEntity() instanceof LivingEntity target) {
                int seconds = 0;
                if (this.world.getDifficulty() == Difficulty.NORMAL) {
                    seconds = 7;
                } else if (this.world.getDifficulty() == Difficulty.HARD) {
                    seconds = 15;
                }

                target.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, seconds * 20), owner);
            }
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        if (!this.world.isClient) {
            this.discard();
        }

    }

    @Override
    protected void initDataTracker() {
    }

    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);

        double vX = packet.getVelocityX();
        double vY = packet.getVelocityY();
        double vZ = packet.getVelocityZ();

        for (int i = 0; i < 7; ++i) {
            double offset = 0.4D + 0.1D * i;
            this.world.addParticle(ParticleTypes.SPIT, this.getX(), this.getY(), this.getZ(), vX * offset, vY, vZ * offset);
        }

        this.setVelocity(vX, vY, vZ);
    }
}
