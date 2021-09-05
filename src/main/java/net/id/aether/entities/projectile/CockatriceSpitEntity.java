package net.id.aether.entities.projectile;

import net.id.aether.entities.AetherEntityTypes;
import net.id.aether.entities.hostile.CockatriceEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.LlamaSpitEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class CockatriceSpitEntity extends LlamaSpitEntity {
    public CockatriceSpitEntity(EntityType<? extends CockatriceSpitEntity> entityType, World world) {
        super(entityType, world);
    }

    public CockatriceSpitEntity(World world, CockatriceEntity owner) {
        this(AetherEntityTypes.COCKATRICE_SPIT, world);
        this.setOwner(owner);
        this.setPosition(owner.getX() - (owner.getWidth() + 1.0F) * 0.5D * MathHelper.sin(owner.bodyYaw * 0.017453292F), owner.getEyeY() - 0.10000000149011612D, owner.getZ() + (owner.getWidth() + 1.0F) * 0.5D * MathHelper.cos(owner.bodyYaw * 0.017453292F));
    }

    @Override
    protected void onEntityHit(EntityHitResult hit) {
        super.onEntityHit(hit);
        Entity entity = this.getOwner();
        if (entity instanceof LivingEntity owner) {
            boolean successfulHit = !hit.getEntity().isInvulnerableTo(DamageSource.mobProjectile(this, owner).setProjectile());
            if (successfulHit && hit.getEntity() instanceof LivingEntity target) {
                int seconds = switch (this.world.getDifficulty()) {
                    default:
                        yield 0;
                    case NORMAL:
                        yield 7;
                    case HARD:
                        yield 15;
                };

                target.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, seconds * 20), owner);
            }
        }
    }
}
