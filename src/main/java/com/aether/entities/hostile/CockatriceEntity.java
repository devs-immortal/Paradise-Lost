package com.aether.entities.hostile;

import com.aether.entities.AetherEntityTypes;
import com.aether.entities.projectile.PoisonNeedleEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

public class CockatriceEntity extends Monster implements RangedAttackMob {

    public float wingRotation, destPos, prevDestPos, prevWingRotation;
    public int shootTime, ticksUntilFlap;

    public CockatriceEntity(Level world) {
        super(AetherEntityTypes.COCKATRICE, world);
        this.maxUpStep = 1.0F;
    }

    public static AttributeSupplier.Builder initAttributes() {
        return AetherEntityTypes.getDefaultAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.FOLLOW_RANGE, 35.0D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(4, new RangedAttackGoal(this, 0.5D, 30, 12.0F));
        //this.goalSelector.add(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.onGround && this.getDeltaMovement().y < 0.0D)
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.6D, 1.0D));

        if (!this.onGround) {
            if (this.ticksUntilFlap == 0) {
                this.level.playSound(null, new BlockPos(this.position()), SoundEvents.BAT_TAKEOFF, SoundSource.NEUTRAL, 0.15F, Mth.clamp(this.random.nextFloat(), 0.7f, 1.0f) + Mth.clamp(this.random.nextFloat(), 0f, 0.3f));

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
    public void performRangedAttack(LivingEntity targetIn, float arg1) {
        PoisonNeedleEntity needle = new PoisonNeedleEntity(this, this.level);

        double x = targetIn.getX() - this.getX();
        double z = targetIn.getZ() - this.getX();
        double y = targetIn.getBoundingBox().minY + (double) (targetIn.getBbHeight() / 3.0F) - needle.getY();
        double double_4 = Mth.sqrt((float) (x * x + z * z));

        needle.shoot(x, y + double_4 * 0.20000000298023224D, z, 1.2F, 1.0F);

        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.2F / (this.getRandom().nextFloat() * 0.2F + 0.9F));

        this.level.addFreshEntity(needle);
    }

    public boolean checkSpawnRules(LevelAccessor world, MobSpawnType SpawnReason) {
        return world.getRandom().nextInt(25) == 0 && super.checkSpawnRules(world, SpawnReason);
    }

    // TODO: Stubbed. Pending 1.17 rewrite.
//    @Override
//    public boolean handleFallDamage(float distance, float damageMultiplier) {
//        return false;
//    }

    @Override
    public boolean canBeAffected(MobEffectInstance effect) {
        return effect.getEffect() != MobEffects.POISON && super.canBeAffected(effect);
    }

    @Override
    public int getMaxSpawnClusterSize() {
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