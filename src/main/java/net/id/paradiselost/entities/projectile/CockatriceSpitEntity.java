package net.id.paradiselost.entities.projectile;

import net.id.incubus_core.condition.api.ConditionAPI;
import net.id.incubus_core.condition.api.Persistence;
import net.id.incubus_core.condition.base.ConditionManager;
import net.id.paradiselost.effect.condition.Conditions;
import net.id.paradiselost.entities.ParadiseLostEntityTypes;
import net.id.paradiselost.entities.hostile.CockatriceEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.LlamaSpitEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class CockatriceSpitEntity extends LlamaSpitEntity {
    public CockatriceSpitEntity(EntityType<? extends CockatriceSpitEntity> entityType, World world) {
        super(entityType, world);
    }

    public CockatriceSpitEntity(World world, CockatriceEntity owner) {
        this(ParadiseLostEntityTypes.COCKATRICE_SPIT, world);
        setOwner(owner);
        setPosition(owner.getX() - (owner.getWidth() + 1.0F) * 0.5D * MathHelper.sin(owner.bodyYaw * 0.017453292F), owner.getEyeY() - 0.10000000149011612D, owner.getZ() + (owner.getWidth() + 1.0F) * 0.5D * MathHelper.cos(owner.bodyYaw * 0.017453292F));
    }

    @Override
    protected void onEntityHit(EntityHitResult hit) {
        super.onEntityHit(hit);
        Entity entity = getOwner();
        if (entity instanceof LivingEntity owner) {
            boolean successfulHit = !hit.getEntity().isInvulnerableTo(DamageSource.mobProjectile(this, owner).setProjectile());
            if (successfulHit && hit.getEntity() instanceof LivingEntity target) {
                int seconds = switch (world.getDifficulty()) {
                    default:
                        yield 0;
                    case NORMAL:
                        yield 7;
                    case HARD:
                        yield 15;
                };

                ConditionManager manager = ConditionAPI.getConditionManager(target);
                manager.add(Conditions.VENOM, Persistence.TEMPORARY, 50F);
                manager.add(Conditions.VENOM, Persistence.CHRONIC, 0.5F);
            }
        }
    }
}
