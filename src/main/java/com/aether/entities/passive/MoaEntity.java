package com.aether.entities.passive;

import com.aether.api.AetherAPI;
import com.aether.api.moa.MoaType;
import com.aether.entities.AetherEntityTypes;
import com.aether.entities.util.SaddleMountEntity;
import com.aether.items.AetherItems;
import com.aether.items.MoaEgg;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

//import com.aether.world.storage.loot.AetherLootTableList;

public class MoaEntity extends SaddleMountEntity implements PlayerRideableJumping {

    public static final EntityDataAccessor<Integer> MOA_TYPE_ID = SynchedEntityData.defineId(MoaEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> REMAINING_JUMPS = SynchedEntityData.defineId(MoaEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> AIR_TICKS = SynchedEntityData.defineId(MoaEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Byte> AMMOUNT_FEED = SynchedEntityData.defineId(MoaEntity.class, EntityDataSerializers.BYTE);
    public static final EntityDataAccessor<Boolean> PLAYER_GROWN = SynchedEntityData.defineId(MoaEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> HUNGRY = SynchedEntityData.defineId(MoaEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(MoaEntity.class, EntityDataSerializers.BOOLEAN);
    public float curWingRoll, curWingYaw, curLegPitch;
    protected int maxJumps, secsUntilHungry, secsUntilEgg;
    public float jumpStrength;
    public boolean isInAir;

    public MoaEntity(Level world) {
        super(AetherEntityTypes.MOA, world);

        this.maxUpStep = 1.0F;
        this.secsUntilEgg = this.getRandomEggTime();
    }

    public MoaEntity(Level world, MoaType type) {
        this(world);

        this.setMoaType(type);
    }

    public static AttributeSupplier.Builder initAttributes() {
        return AetherEntityTypes.getDefaultAttributes()
                .add(Attributes.MAX_HEALTH, 35.0D)
                .add(Attributes.MOVEMENT_SPEED, 1.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 2));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.65F, 0.1F)); //WanderGoal
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.0D, Ingredient.of(AetherItems.NATURE_STAFF), false));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1.0)); //WanderGoal
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 4.5F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this)); //LookGoal
        this.goalSelector.addGoal(6, new BreedGoal(this, 0.25F));
    }

    @Override
    public void move(MoverType movement, Vec3 motion) {
        if (!this.isSitting()) super.move(movement, motion);
        else super.move(movement, new Vec3(0, motion.y, 0));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();

        MoaType moaType = AetherAPI.instance().getMoa();

        this.entityData.define(MOA_TYPE_ID, AetherAPI.instance().getMoaId(moaType));
        this.entityData.define(REMAINING_JUMPS, moaType.getMoaProperties().getMaxJumps());
        this.entityData.define(AIR_TICKS, 0);

        this.entityData.define(PLAYER_GROWN, false);
        this.entityData.define(AMMOUNT_FEED, (byte) 0);
        this.entityData.define(HUNGRY, false);
        this.entityData.define(SITTING, false);
    }

    public float getWingRoll() {
        if(!isGliding()) {
            float baseWingRoll = 1.39626F;

            float lDif = -baseWingRoll - curWingRoll;
            if(Math.abs(lDif) > 0.005F) {
                curWingRoll += lDif / 6;
            }
        }
        else {
            curWingRoll = (Mth.sin(tickCount / 1.75F) * 0.725F + 0.1F);
        }
        return curWingRoll;
    }

    public float getWingYaw() {
        float baseWingYaw = isGliding() ? 0.95626F : 0.174533F;

        float lDif = -baseWingYaw - curWingYaw;
        if(Math.abs(lDif) > 0.005F) {
            curWingYaw += lDif / 12.75;
        }
        return curWingYaw;
    }

    public float getLegPitch() {
        float baseLegPitch = isGliding() ? -1.5708F : 0.0174533F;

        float lDif = -baseLegPitch - curLegPitch;
        if(Math.abs(lDif) > 0.005F) {
            curLegPitch += lDif / 6;
        }
        return curLegPitch;
    }

    public int getRandomEggTime() {
        return 775 + this.random.nextInt(50);
    }

    public boolean isSitting() {
        return this.entityData.get(SITTING);
    }

    public void setSitting(boolean isSitting) {
        this.entityData.set(SITTING, isSitting);
    }

    public boolean isHungry() {
        return this.entityData.get(HUNGRY);
    }

    public void setHungry(boolean hungry) {
        this.entityData.set(HUNGRY, hungry);
    }

    public byte getAmountFed() {
        return this.entityData.get(AMMOUNT_FEED);
    }

    public void setAmountFed(int amountFed) {
        this.entityData.set(AMMOUNT_FEED, (byte) amountFed);
    }

    public void increaseAmountFed(int amountFed) {
        this.setAmountFed(this.getAmountFed() + amountFed);
    }

    public boolean isPlayerGrown() {
        return this.entityData.get(PLAYER_GROWN);
    }

    public void setPlayerGrown(boolean playerGrown) {
        this.entityData.set(PLAYER_GROWN, playerGrown);
    }

    public int getMaxJumps() {
        return this.maxJumps;
    }

    public void setMaxJumps(int maxJumps) {
        this.maxJumps = maxJumps;
    }

    public int getRemainingJumps() {
        return this.entityData.get(REMAINING_JUMPS);
    }

    public void setRemainingJumps(int jumps) {
        this.entityData.set(REMAINING_JUMPS, jumps);
    }

    public MoaType getMoaType() {
        return AetherAPI.instance().getMoa(this.entityData.get(MOA_TYPE_ID));
    }

    public void setMoaType(MoaType moa) {
        this.entityData.set(MOA_TYPE_ID, AetherAPI.instance().getMoaId(moa));
    }

    @Override
    protected void playHurtSound(DamageSource source) {
        this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.BAT_DEATH, SoundSource.NEUTRAL, 0.225F, Mth.clamp(this.random.nextFloat(), 0.5f, 0.7f) + Mth.clamp(this.random.nextFloat(), 0f, 0.15f));
    }

    @Override
    public void tick() {
        super.tick();

        isInAir = !onGround;

        if(isInAir)
            entityData.set(AIR_TICKS, entityData.get(AIR_TICKS) + 1);
        else
            entityData.set(AIR_TICKS, 0);

        if (tickCount % 15 == 0) {
            if(isGliding()) {
                this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.PHANTOM_FLAP, SoundSource.NEUTRAL, 4.5F, Mth.clamp(this.random.nextFloat(), 0.85f, 1.2f) + Mth.clamp(this.random.nextFloat(), 0f, 0.35f));
            }
            else if(random.nextFloat() < 0.057334F) {
                this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.PARROT_AMBIENT, SoundSource.NEUTRAL, 1.5F + random.nextFloat() * 2, Mth.clamp(this.random.nextFloat(), 0.55f, 0.7f) + Mth.clamp(this.random.nextFloat(), 0f, 0.25f));
            }
        }

        this.setMaxJumps(this.getMoaType().getMoaProperties().getMaxJumps());

        if (this.jumping) this.setDeltaMovement(this.getDeltaMovement().add(0.0D, 0.05D, 0.0D));

        this.fall();

        if (this.secsUntilHungry > 0) {
            if (this.tickCount % 20 == 0) this.secsUntilHungry--;
        } else if (!this.isHungry()) {
            this.setHungry(true);
        }

        if (this.level.isClientSide && this.isHungry() && this.isBaby()) {
            if (this.random.nextInt(10) == 0)
                this.level.addParticle(ParticleTypes.ANGRY_VILLAGER, this.getX() + (this.random.nextDouble() - 0.5D) * (double) this.getBbWidth(), this.getY() + 1, this.getZ() + (this.random.nextDouble() - 0.5D) * (double) this.getBbWidth(), 0.0D, 0.0D, 0.0D);
        }

        if (!this.level.isClientSide && !this.isBaby() && this.getPassengers().isEmpty()) {
            if (this.secsUntilEgg > 0) {
                if (this.tickCount % 20 == 0) this.secsUntilEgg--;
            } else {
                this.playSound(SoundEvents.CHICKEN_EGG, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                this.spawnAtLocation(MoaEgg.getStack(this.getMoaType()), 0);

                this.secsUntilEgg = this.getRandomEggTime();
            }
        }
    }

    @Override
    protected int calculateFallDamage(float fallDistance, float damageMultiplier) {
        return isGliding() ? 0 : (int) (super.calculateFallDamage(fallDistance, damageMultiplier) * 0.25);
    }

    public boolean isGliding() {
        return !isInWater() && entityData.get(AIR_TICKS) > 20;
    }

    public boolean isFood(ItemStack stack) {
        return false;
    }

    public void resetHunger() {
        if (!this.level.isClientSide) this.setHungry(false);

        this.secsUntilHungry = 40 + this.random.nextInt(40);
    }

    public void travel(Vec3 movementInput) {
        if (this.isAlive()) {
            if (this.isVehicle() && this.canBeControlledByRider() && this.isSaddled()) {
                LivingEntity livingEntity = (LivingEntity)this.getControllingPassenger();
                this.yRotO = this.getYRot();
                this.setYRot(livingEntity.getYRot());
                this.setXRot(livingEntity.getXRot() * 0.5F);
                this.setRot(this.getYRot(), this.getXRot());
                this.yBodyRot = this.getYRot();
                this.yHeadRot = this.yBodyRot;
                float f = livingEntity.xxa * 0.5F;
                float g = livingEntity.zza;
                if (g <= 0.0F) {
                    g *= 0.25F;
                }

                if(isControlledByLocalInstance()) {
                    if (this.jumpStrength > 0.0F && !this.isInAir) {
                        double d = 0.1F * (double)this.jumpStrength * (double)this.getBlockJumpFactor();
                        double h;
                        if (this.hasEffect(MobEffects.JUMP)) {
                            h = d + (double)((float)(this.getEffect(MobEffects.JUMP).getAmplifier() + 1) * 0.1F);
                        } else {
                            h = d;
                        }

                        Vec3 vec3d = this.getDeltaMovement();
                        this.setDeltaMovement(vec3d.x, h, vec3d.z);
                        this.hasImpulse = true;
                        if (g > 0.0F) {
                            float i = Mth.sin(this.getYRot() * 0.017453292F);
                            float j = Mth.cos(this.getYRot() * 0.017453292F);
                            this.setDeltaMovement(this.getDeltaMovement().add(-0.4F * i * this.jumpStrength, 0.0D, 0.4F * j * this.jumpStrength));
                        }

                        this.jumpStrength = 0.0F;
                    }
                }

                if(jumpStrength <= 0.01F && onGround)
                    isInAir = false;

                this.flyingSpeed = this.getSpeed() * (isGliding() ? 0.5F : 0.1F);
                if (this.isControlledByLocalInstance()) {
                    this.setSpeed((float)this.getAttributeValue(Attributes.MOVEMENT_SPEED));
                    super.travel(new Vec3(f, movementInput.y, g));
                } else if (livingEntity instanceof Player) {
                    this.setDeltaMovement(Vec3.ZERO);
                }

                this.calculateEntityAnimation(this, false);
            } else {
                this.flyingSpeed = getSpeed() * (isGliding() ? 0.3F : 0.1F);
                super.travel(movementInput);
            }
        }
    }

    @Override
    public float getMountedMoveSpeed() {
        return this.getMoaType().getMoaProperties().getMoaSpeed();
    }

    public void setToAdult() {
        this.setAge(0);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);


        return super.mobInteract(player, hand);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);

        compound.putBoolean("playerGrown", this.isPlayerGrown());
        compound.putInt("remainingJumps", this.getRemainingJumps());
        compound.putByte("amountFed", this.getAmountFed());
        compound.putBoolean("isHungry", this.isHungry());
        compound.putBoolean("isSitting", this.isSitting());
        compound.putInt("typeId", AetherAPI.instance().getMoaId(this.getMoaType()));
        compound.putInt("airTicks", entityData.get(AIR_TICKS));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);

        this.setPlayerGrown(compound.getBoolean("playerGrown"));
        this.setRemainingJumps(compound.getInt("remainingJumps"));
        this.setMoaType(AetherAPI.instance().getMoa(compound.getInt("typeId")));
        this.setAmountFed(compound.getByte("amountFed"));
        this.setHungry(compound.getBoolean("isHungry"));
        this.setSitting(compound.getBoolean("isSitting"));
        entityData.set(AIR_TICKS, compound.getInt("airTicks"));
    }

    @Override
    public boolean canSpawnSprintParticle() {
        return Math.abs(getDeltaMovement().multiply(1, 0, 1).length()) > 0 && !isInWater() && !isGliding();
    }

    @Override
    protected void playStepSound(BlockPos posIn, BlockState stateIn) {
        this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.PIG_STEP, SoundSource.NEUTRAL, 0.15F, 1F);
    }

    public void fall() {
        boolean blockBeneath = !this.level.isEmptyBlock(new BlockPos(this.position()).below(1));

        if (this.getDeltaMovement().y < 0.0D && !this.isShiftKeyDown())
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D,  isGliding() ? 0.6D : 1D, 1.0D));

        if (blockBeneath) this.setRemainingJumps(this.maxJumps);
    }

    @Override
    public void setJumping(boolean jump) {
        super.setJumping(jump);
    }

    @Override
    public double getPassengersRidingOffset() {
        return 1.03;
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob matingAnimal) {
        return new MoaEntity(this.level, this.getMoaType());
    }

    @Override
    public ResourceLocation getDefaultLootTable() {
        return null;//AetherLootTableList.ENTITIES_MOA;
    }

    @Override
    public void onPlayerJump(int strength) {
        jumpStrength = strength / 5F;
    }

    @Override
    public boolean canJump() {
        return true;
    }

    @Override
    public void handleStartJump(int height) {
        this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.PHANTOM_FLAP, SoundSource.NEUTRAL, 7.5F, Mth.clamp(this.random.nextFloat(), 0.55f, 0.8f));
    }

    @Override
    public void handleStopJump() {

    }
}