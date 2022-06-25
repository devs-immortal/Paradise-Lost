package net.id.paradiselost.entities.hostile;

import net.id.incubus_core.condition.api.ConditionAPI;
import net.id.incubus_core.condition.api.Persistence;
import net.id.incubus_core.condition.base.ConditionManager;
import net.id.paradiselost.effect.condition.Conditions;
import net.id.paradiselost.entities.projectile.CockatriceSpitEntity;
import net.id.paradiselost.util.ParadiseLostSoundEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class CockatriceEntity extends HostileEntity implements RangedAttackMob {
    public float flapProgress;
    public float maxWingDeviation;
    public float prevMaxWingDeviation;
    public float prevFlapProgress;
    public float flapSpeed = 1.0F;
    private float field_28639 = 1.0F;

    public CockatriceEntity(EntityType<? extends CockatriceEntity> entityType, World world) {
        super(entityType, world);
        stepHeight = 1.0F;
    }

    public static DefaultAttributeContainer.Builder createCockatriceAttributes() {
        return createHostileAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 35.0);
    }

    @Override
    protected void initGoals() {
        super.initGoals();

        goalSelector.add(1, new SwimGoal(this));
        goalSelector.add(3, new CockatriceAttackGoal(this, 1.0D, 30, 60, 12.0F));
        goalSelector.add(4, new GoToWalkTargetGoal(this, 1.0D));
        goalSelector.add(5, new WanderAroundFarGoal(this, 1.0D, 0.01F));
        goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        goalSelector.add(6, new LookAroundGoal(this));
        targetSelector.add(1, new RevengeGoal(this, CockatriceEntity.class));
        targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        targetSelector.add(3, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
    }

    // copied from ChickenEntity
    @Override
    public void tickMovement() {
        super.tickMovement();
        prevFlapProgress = flapProgress;
        prevMaxWingDeviation = maxWingDeviation;
        maxWingDeviation = maxWingDeviation + (onGround && !isAttacking() ? -1 : 4) * 0.3F;
        maxWingDeviation = MathHelper.clamp(maxWingDeviation, 0.0F, 1.0F);
        if ((!onGround || isAttacking()) && flapSpeed < 1.0F) {
            flapSpeed = 1.0F;
        }

        flapSpeed = flapSpeed * 0.9F;
        Vec3d velocity = getVelocity();
        if (!onGround && velocity.y < 0.0D) {
            setVelocity(velocity.multiply(1.0D, 0.6D, 1.0D));
        }

        flapProgress += flapSpeed * 2.0F;
    }

    @Override
    protected boolean hasWings() {
        return speed > field_28639;
    }

    @Override
    protected void addFlapEffects() {
        field_28639 = speed + maxWingDeviation / 2.0F;
    }

    @Override
    public boolean handleFallDamage(float distance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    @Override
    public void attack(LivingEntity target, float arg1) {
        CockatriceSpitEntity needle = new CockatriceSpitEntity(world, this);

        double dx = target.getX() - getX();
        double dy = target.getBodyY(0.3333333333333333D) - needle.getY();
        double dz = target.getZ() - getZ();
        double distance = Math.sqrt(dx * dx + dz * dz);
        needle.setVelocity(dx, dy + distance * 0.2D, dz, 1.5F, 14.0F - world.getDifficulty().getId() * 4);

        if (!isSilent()) {
            playSound(ParadiseLostSoundEvents.ENTITY_COCKATRICE_SPIT, 1.0F, 1.0F + (random.nextFloat() - random.nextFloat()) * 0.2F);
        }

        world.spawnEntity(needle);
    }

    @Override
    public boolean tryAttack(Entity target) {
        if (super.tryAttack(target)) {
            if (target instanceof LivingEntity victim) {
                int seconds = 0;
                if (world.getDifficulty() == Difficulty.NORMAL) {
                    seconds = 7;
                } else if (world.getDifficulty() == Difficulty.HARD) {
                    seconds = 15;
                }

                if (seconds > 0) {
                    ConditionManager manager = ConditionAPI.getConditionManager(victim);
                    manager.add(Conditions.VENOM, Persistence.TEMPORARY, 40F);
                }
            }

            return true;
        }
        return false;
    }

    @Override
    public boolean canSpawn(WorldAccess world, SpawnReason SpawnReason) {
        return world.getRandom().nextInt(25) == 0 && super.canSpawn(world, SpawnReason);
    }

    @Override
    public boolean canHaveStatusEffect(StatusEffectInstance effect) {
        return effect.getEffectType() != StatusEffects.POISON && super.canHaveStatusEffect(effect);
    }

    @Override
    public int getLimitPerChunk() {
        return 1;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ParadiseLostSoundEvents.ENTITY_COCKATRICE_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return ParadiseLostSoundEvents.ENTITY_COCKATRICE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ParadiseLostSoundEvents.ENTITY_COCKATRICE_DEATH;
    }

    public static class CockatriceAttackGoal extends ProjectileAttackGoal {
        private final CockatriceEntity cockatrice;
        private int ticksPlunging = 0;
        private int plungingCooldown = 100;

        public CockatriceAttackGoal(CockatriceEntity mob, double mobSpeed, int minIntervalTicks, int maxIntervalTicks, float maxShootRange) {
            super(mob, mobSpeed, minIntervalTicks, maxIntervalTicks, maxShootRange);
            cockatrice = mob;
        }

        @Override
        public void tick() {
            LivingEntity target = cockatrice.getTarget();
            if (target == null) {
                return;
            }

            if (ticksPlunging == 0 && plungingCooldown > 0) {
                plungingCooldown--;
            }
            if (ticksPlunging > 0) {
                if (!cockatrice.canSee(target)) {
                    ticksPlunging = 0;
                } else {
                    ticksPlunging--;
                }
                if (ticksPlunging == 0) {
                    cockatrice.setAttacking(false);
                    plungingCooldown = 50 + cockatrice.getRandom().nextInt(25);
                }
            }
            double distSq = cockatrice.squaredDistanceTo(target);

            if (ticksPlunging > 0 || plungingCooldown == 0) {
                boolean shouldPlunge = ticksPlunging > 0 // continue plunging if already started
                        || cockatrice.getRandom().nextInt(20) == 0 // rarely start from any distance
                        || distSq < 10 * 10 && cockatrice.getRandom().nextInt(5) != 0; // normally start if we are close
                if (shouldPlunge) {
                    if (ticksPlunging == 0) {
                        ticksPlunging = 50 + cockatrice.getRandom().nextInt(25);
                        // activates flapping wings
                        cockatrice.setAttacking(true);
                    }
                    // move to target very quickly
                    cockatrice.getMoveControl().moveTo(target.getX(), target.getY(), target.getZ(), 1.5D);

                    if (distSq < 4.0D) {
                        cockatrice.tryAttack(target);
                    }
                    return;
                } else {
                    ticksPlunging = 0;
                    cockatrice.setAttacking(false);
                    plungingCooldown = 50 + cockatrice.getRandom().nextInt(25);
                }
            }
            // normally ProjectileAttackGoal stays at attack range of its ranged attack
            // add some chance to start moving closer, thus allowing to enter range for plunge attack
            if (distSq <= 12 * 12 && (cockatrice.getRandom().nextInt(3) == 0 || plungingCooldown < 10)) {
                cockatrice.getMoveControl().moveTo(target.getX(), target.getY(), target.getZ(), 1D);
            } else {
                super.tick();
            }
        }
    }
}
