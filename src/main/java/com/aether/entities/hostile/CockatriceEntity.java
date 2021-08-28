package com.aether.entities.hostile;

import com.aether.entities.projectile.PoisonNeedleEntity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.ProjectileAttackGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class CockatriceEntity extends HostileEntity implements RangedAttackMob {

    public float wingRotation, destPos, prevDestPos, prevWingRotation;
    public int shootTime, ticksUntilFlap;

    public CockatriceEntity(EntityType<? extends CockatriceEntity> entityType, World world) {
        super(entityType, world);
        this.stepHeight = 1.0F;
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

        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(4, new ProjectileAttackGoal(this, 0.5D, 30, 12.0F));
        //this.goalSelector.add(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(6, new LookAroundGoal(this));
        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new FollowTargetGoal<>(this, PlayerEntity.class, true));
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.onGround && this.getVelocity().y < 0.0D)
            this.setVelocity(this.getVelocity().multiply(1.0D, 0.6D, 1.0D));

        if (!this.onGround) {
            if (this.ticksUntilFlap == 0) {
                this.world.playSound(null, new BlockPos(this.getPos()), SoundEvents.ENTITY_BAT_TAKEOFF, SoundCategory.NEUTRAL, 0.15F, MathHelper.clamp(this.random.nextFloat(), 0.7f, 1.0f) + MathHelper.clamp(this.random.nextFloat(), 0f, 0.3f));

                this.ticksUntilFlap = 8;
            } else {
                this.ticksUntilFlap--;
            }
        }

        this.prevWingRotation = this.wingRotation;
        this.prevDestPos = this.destPos;

        this.destPos += 0.2F;
        this.destPos = Math.min(1.0F, Math.max(0.01F, this.destPos));

        if (this.onGround) this.destPos = 0.0F;

        this.wingRotation += 1.233F;
    }

    @Override
    public void attack(LivingEntity target, float pullProgress) {
        PoisonNeedleEntity needle = new PoisonNeedleEntity(this, this.world);

        double dx = target.getX() - this.getX();
        double dz = target.getZ() - this.getZ();
        double dy = target.getBodyY(0.3333333333333333D) - needle.getY();
        double distance = Math.sqrt(dx * dx + dz * dz);

        needle.setVelocity(dx, dy + distance * 0.2D, dz, 1.2F, 1.0F);

        this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.2F / (this.getRandom().nextFloat() * 0.2F + 0.9F));

        this.world.spawnEntity(needle);
    }

    public boolean canSpawn(WorldAccess world, SpawnReason SpawnReason) {
        return world.getRandom().nextInt(25) == 0 && super.canSpawn(world, SpawnReason);
    }

    @Override
    public boolean handleFallDamage(float distance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    @Override
    public boolean canHaveStatusEffect(StatusEffectInstance effect) {
        return effect.getEffectType() != StatusEffects.POISON && super.canHaveStatusEffect(effect);
    }

    @Override
    public int getLimitPerChunk() {
        return 1;
    }

//    @Override
//    protected SoundEvent getAmbientSound() {
//        return AetherSounds.MOA_SAY;
//    }
//
//    @Override
//    protected SoundEvent getHurtSound(DamageSource source) {
//        return AetherSounds.MOA_SAY;
//    }
//
//    @Override
//    protected SoundEvent getDeathSound() {
//        return AetherSounds.MOA_SAY;
//    }

}