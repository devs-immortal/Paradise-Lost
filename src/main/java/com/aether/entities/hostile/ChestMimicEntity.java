package com.aether.entities.hostile;

import com.aether.entities.AetherEntityTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.Level;

public class ChestMimicEntity extends Monster {

    public float mouth, legs;
    private float legsDirection = 1;

    public ChestMimicEntity(Level world) {
        super(AetherEntityTypes.CHEST_MIMIC, world);
    }

    public static AttributeSupplier.Builder initAttributes() {
        return AetherEntityTypes.getDefaultAttributes()
                .add(Attributes.FOLLOW_RANGE, 8.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.28000000417232513D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.MAX_HEALTH, 40.0D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public void tick() {
        super.tick();

        this.mouth = (float) ((Math.cos((float) this.tickCount / 10F * 3.14159265F)) + 1F) * 0.6F;
        this.legs *= 0.9F;

        if (this.xo - this.getX() != 0 || this.zo - this.getZ() != 0) {
            this.legs += legsDirection * 0.2F;

            if (this.legs > 1.0F) this.legsDirection = -1;

            if (this.legs < -1.0F) this.legsDirection = 1;
        } else {
            this.legs = 0.0F;
        }
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        if (damageSource.getEntity() instanceof Player) {
            Player player = (Player) damageSource.getEntity();

            if (player.getMainHandItem().getItem() instanceof AxeItem) damage *= 1.25F;
        }
        return super.hurt(damageSource, damage);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.WOOD_HIT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.CHEST_CLOSE;
    }
}