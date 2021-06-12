package com.aether.entities.passive;

import com.aether.entities.AetherEntityTypes;
import com.aether.entities.util.SaddleMountEntity;
import com.aether.items.AetherItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

//import com.aether.world.storage.loot.AetherLootTableList;

public class PhygEntity extends SaddleMountEntity {

    public float wingFold;

    public float wingAngle;
    public int maxJumps;
    public int jumpsRemaining;
    public int ticks;
    private float aimingForFold;

    public PhygEntity(Level world) {
        super(AetherEntityTypes.PHYG, world);

        this.jumpsRemaining = 0;
        this.maxJumps = 1;
        this.maxUpStep = 1.0F;

        this.noCulling = true;
        this.canJumpMidAir = true;
    }

    public static AttributeSupplier.Builder initAttributes() {
        return AetherEntityTypes.getDefaultAttributes()
                .add(Attributes.MAX_HEALTH, /*this.getSaddled() ? 20.0D : */10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25D, Ingredient.of(AetherItems.BLUEBERRY), false));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
    }

    @Override
    public void tick() {
        super.tick();

        if (this.onGround) {
            this.wingAngle *= 0.8F;
            this.aimingForFold = 0.1F;
            this.jumpsRemaining = this.maxJumps;
        } else {
            this.aimingForFold = 1.0F;
        }

        this.ticks++;

        this.wingAngle = this.wingFold * (float) Math.sin(this.ticks / 31.83098862F);
        this.wingFold += (this.aimingForFold - this.wingFold) / 5F;
        this.fallDistance = 0;
        this.fall();
    }

//    @Override
//    protected SoundEvent getDeathSound() {
//        return AetherSounds.PHYG_DEATH;
//    }
//
//    @Override
//    protected SoundEvent getHurtSound(DamageSource source) {
//        return AetherSounds.PHYG_HURT;
//    }
//
//    @Override
//    protected SoundEvent getAmbientSound() {
//        return AetherSounds.PHYG_SAY;
//    }

    @Override
    public double getPassengersRidingOffset() {
        return 0.65D;
    }

    @Override
    public float getMountedMoveSpeed() {
        return 0.3F;
    }

    @Override
    public void setJumping(boolean jump) {
        super.setJumping(jump);
    }

    private void fall() {
        if (this.getDeltaMovement().y < 0.0D && !this.isShiftKeyDown())
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.6D, 1.0D));

        if (!this.onGround && !this.firstTick) {
            if (this.onGround && !this.level.isClientSide) this.jumpsRemaining = this.maxJumps;
        }
    }

    @Override
    protected double getMountJumpStrength() {
        return 5.0D;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState par4) {
        this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.PIG_STEP, SoundSource.NEUTRAL, 0.15F, 1.0F);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);

        this.maxJumps = compound.getInt("maxJumps");
        this.jumpsRemaining = compound.getInt("remainingJumps");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);

        compound.putInt("maxJumps", this.maxJumps);
        compound.putInt("remainingJumps", this.jumpsRemaining);
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entityageable) {
        return new PhygEntity(this.level);
    }

    @Override
    public ResourceLocation getDefaultLootTable() {
        return null;//AetherLootTableList.ENTITIES_PHYG;
    }
}