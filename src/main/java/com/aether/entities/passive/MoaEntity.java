package com.aether.entities.passive;

import com.aether.api.AetherAPI;
import com.aether.api.moa.MoaType;
import com.aether.entities.AetherEntityTypes;
import com.aether.entities.util.SaddleMountEntity;
import com.aether.items.AetherItems;
import com.aether.items.MoaEgg;
import net.minecraft.block.BlockState;
import net.minecraft.entity.JumpingMount;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

//import com.aether.world.storage.loot.AetherLootTableList;

public class MoaEntity extends SaddleMountEntity implements JumpingMount {

    public static final TrackedData<Integer> MOA_TYPE_ID = DataTracker.registerData(MoaEntity.class, TrackedDataHandlerRegistry.INTEGER);
    public static final TrackedData<Integer> REMAINING_JUMPS = DataTracker.registerData(MoaEntity.class, TrackedDataHandlerRegistry.INTEGER);
    public static final TrackedData<Integer> AIR_TICKS = DataTracker.registerData(MoaEntity.class, TrackedDataHandlerRegistry.INTEGER);
    public static final TrackedData<Byte> AMMOUNT_FEED = DataTracker.registerData(MoaEntity.class, TrackedDataHandlerRegistry.BYTE);
    public static final TrackedData<Boolean> PLAYER_GROWN = DataTracker.registerData(MoaEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public static final TrackedData<Boolean> HUNGRY = DataTracker.registerData(MoaEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public static final TrackedData<Boolean> SITTING = DataTracker.registerData(MoaEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public float curWingRoll, curWingYaw, curLegPitch;
    protected int maxJumps, secsUntilHungry, secsUntilEgg;
    public float jumpStrength;
    public boolean isInAir;

    public MoaEntity(World world) {
        super(AetherEntityTypes.MOA, world);

        this.stepHeight = 1.0F;
        this.secsUntilEgg = this.getRandomEggTime();
    }

    public MoaEntity(World world, MoaType type) {
        this(world);

        this.setMoaType(type);
    }

    public static DefaultAttributeContainer.Builder initAttributes() {
        return AetherEntityTypes.getDefaultAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 35.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 1.0D);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 2));
        this.goalSelector.add(2, new WanderAroundFarGoal(this, 0.65F, 0.1F)); //WanderGoal
        this.goalSelector.add(2, new TemptGoal(this, 1.0D, Ingredient.ofItems(AetherItems.NATURE_STAFF), false));
        this.goalSelector.add(2, new WanderAroundGoal(this, 1.0)); //WanderGoal
        this.goalSelector.add(4, new LookAtEntityGoal(this, PlayerEntity.class, 4.5F));
        this.goalSelector.add(5, new LookAroundGoal(this)); //LookGoal
        this.goalSelector.add(6, new AnimalMateGoal(this, 0.25F));
    }

    @Override
    public void move(MovementType movement, Vec3d motion) {
        if (!this.isSitting()) super.move(movement, motion);
        else super.move(movement, new Vec3d(0, motion.y, 0));
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();

        MoaType moaType = AetherAPI.instance().getMoa();

        this.dataTracker.startTracking(MOA_TYPE_ID, AetherAPI.instance().getMoaId(moaType));
        this.dataTracker.startTracking(REMAINING_JUMPS, moaType.getMoaProperties().getMaxJumps());
        this.dataTracker.startTracking(AIR_TICKS, 0);

        this.dataTracker.startTracking(PLAYER_GROWN, false);
        this.dataTracker.startTracking(AMMOUNT_FEED, (byte) 0);
        this.dataTracker.startTracking(HUNGRY, false);
        this.dataTracker.startTracking(SITTING, false);
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
            curWingRoll = (MathHelper.sin(age / 1.75F) * 0.725F + 0.1F);
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
        return this.dataTracker.get(SITTING);
    }

    public void setSitting(boolean isSitting) {
        this.dataTracker.set(SITTING, isSitting);
    }

    public boolean isHungry() {
        return this.dataTracker.get(HUNGRY);
    }

    public void setHungry(boolean hungry) {
        this.dataTracker.set(HUNGRY, hungry);
    }

    public byte getAmountFed() {
        return this.dataTracker.get(AMMOUNT_FEED);
    }

    public void setAmountFed(int amountFed) {
        this.dataTracker.set(AMMOUNT_FEED, (byte) amountFed);
    }

    public void increaseAmountFed(int amountFed) {
        this.setAmountFed(this.getAmountFed() + amountFed);
    }

    public boolean isPlayerGrown() {
        return this.dataTracker.get(PLAYER_GROWN);
    }

    public void setPlayerGrown(boolean playerGrown) {
        this.dataTracker.set(PLAYER_GROWN, playerGrown);
    }

    public int getMaxJumps() {
        return this.maxJumps;
    }

    public void setMaxJumps(int maxJumps) {
        this.maxJumps = maxJumps;
    }

    public int getRemainingJumps() {
        return this.dataTracker.get(REMAINING_JUMPS);
    }

    public void setRemainingJumps(int jumps) {
        this.dataTracker.set(REMAINING_JUMPS, jumps);
    }

    public MoaType getMoaType() {
        return AetherAPI.instance().getMoa(this.dataTracker.get(MOA_TYPE_ID));
    }

    public void setMoaType(MoaType moa) {
        this.dataTracker.set(MOA_TYPE_ID, AetherAPI.instance().getMoaId(moa));
    }

    @Override
    protected void playHurtSound(DamageSource source) {
        this.world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_BAT_DEATH, SoundCategory.NEUTRAL, 0.225F, MathHelper.clamp(this.random.nextFloat(), 0.5f, 0.7f) + MathHelper.clamp(this.random.nextFloat(), 0f, 0.15f));
    }

    @Override
    public void tick() {
        super.tick();

        isInAir = !onGround;

        if(isInAir)
            dataTracker.set(AIR_TICKS, dataTracker.get(AIR_TICKS) + 1);
        else
            dataTracker.set(AIR_TICKS, 0);

        if (age % 15 == 0) {
            if(isGliding()) {
                this.world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_PHANTOM_FLAP, SoundCategory.NEUTRAL, 4.5F, MathHelper.clamp(this.random.nextFloat(), 0.85f, 1.2f) + MathHelper.clamp(this.random.nextFloat(), 0f, 0.35f));
            }
            else if(random.nextFloat() < 0.057334F) {
                this.world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_PARROT_AMBIENT, SoundCategory.NEUTRAL, 1.5F + random.nextFloat() * 2, MathHelper.clamp(this.random.nextFloat(), 0.55f, 0.7f) + MathHelper.clamp(this.random.nextFloat(), 0f, 0.25f));
            }
        }

        this.setMaxJumps(this.getMoaType().getMoaProperties().getMaxJumps());

        if (this.jumping) this.setVelocity(this.getVelocity().add(0.0D, 0.05D, 0.0D));

        this.fall();

        if (this.secsUntilHungry > 0) {
            if (this.age % 20 == 0) this.secsUntilHungry--;
        } else if (!this.isHungry()) {
            this.setHungry(true);
        }

        if (this.world.isClient && this.isHungry() && this.isBaby()) {
            if (this.random.nextInt(10) == 0)
                this.world.addParticle(ParticleTypes.ANGRY_VILLAGER, this.getX() + (this.random.nextDouble() - 0.5D) * (double) this.getWidth(), this.getY() + 1, this.getZ() + (this.random.nextDouble() - 0.5D) * (double) this.getWidth(), 0.0D, 0.0D, 0.0D);
        }

        if (!this.world.isClient && !this.isBaby() && this.getPassengerList().isEmpty()) {
            if (this.secsUntilEgg > 0) {
                if (this.age % 20 == 0) this.secsUntilEgg--;
            } else {
                this.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                this.dropStack(MoaEgg.getStack(this.getMoaType()), 0);

                this.secsUntilEgg = this.getRandomEggTime();
            }
        }
    }

    @Override
    protected int computeFallDamage(float fallDistance, float damageMultiplier) {
        return isGliding() ? 0 : (int) (super.computeFallDamage(fallDistance, damageMultiplier) * 0.25);
    }

    public boolean isGliding() {
        return !isTouchingWater() && dataTracker.get(AIR_TICKS) > 20;
    }

    public boolean isBreedingItem(ItemStack stack) {
        return false;
    }

    public void resetHunger() {
        if (!this.world.isClient) this.setHungry(false);

        this.secsUntilHungry = 40 + this.random.nextInt(40);
    }

    public void travel(Vec3d movementInput) {
        if (this.isAlive()) {
            if (this.hasPassengers() && this.canBeControlledByRider() && this.isSaddled()) {
                LivingEntity livingEntity = (LivingEntity)this.getPrimaryPassenger();
                this.prevYaw = this.getYaw();
                this.setYaw(livingEntity.getYaw());
                this.setPitch(livingEntity.getPitch() * 0.5F);
                this.setRotation(this.getYaw(), this.getPitch());
                this.bodyYaw = this.getYaw();
                this.headYaw = this.bodyYaw;
                float f = livingEntity.sidewaysSpeed * 0.5F;
                float g = livingEntity.forwardSpeed;
                if (g <= 0.0F) {
                    g *= 0.25F;
                }

                if(isLogicalSideForUpdatingMovement()) {
                    if (this.jumpStrength > 0.0F && !this.isInAir) {
                        double d = 0.1F * (double)this.jumpStrength * (double)this.getJumpVelocityMultiplier();
                        double h;
                        if (this.hasStatusEffect(StatusEffects.JUMP_BOOST)) {
                            h = d + (double)((float)(this.getStatusEffect(StatusEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F);
                        } else {
                            h = d;
                        }

                        Vec3d vec3d = this.getVelocity();
                        this.setVelocity(vec3d.x, h, vec3d.z);
                        this.velocityDirty = true;
                        if (g > 0.0F) {
                            float i = MathHelper.sin(this.getYaw() * 0.017453292F);
                            float j = MathHelper.cos(this.getYaw() * 0.017453292F);
                            this.setVelocity(this.getVelocity().add(-0.4F * i * this.jumpStrength, 0.0D, 0.4F * j * this.jumpStrength));
                        }

                        this.jumpStrength = 0.0F;
                    }
                }

                if(jumpStrength <= 0.01F && onGround)
                    isInAir = false;

                this.flyingSpeed = this.getMovementSpeed() * (isGliding() ? 0.5F : 0.1F);
                if (this.isLogicalSideForUpdatingMovement()) {
                    this.setMovementSpeed((float)this.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED));
                    super.travel(new Vec3d(f, movementInput.y, g));
                } else if (livingEntity instanceof PlayerEntity) {
                    this.setVelocity(Vec3d.ZERO);
                }

                this.updateLimbs(this, false);
            } else {
                this.flyingSpeed = getMovementSpeed() * (isGliding() ? 0.3F : 0.1F);
                super.travel(movementInput);
            }
        }
    }

    @Override
    public float getMountedMoveSpeed() {
        return this.getMoaType().getMoaProperties().getMoaSpeed();
    }

    public void setToAdult() {
        this.setBreedingAge(0);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);


        return super.interactMob(player, hand);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound compound) {
        super.writeCustomDataToNbt(compound);

        compound.putBoolean("playerGrown", this.isPlayerGrown());
        compound.putInt("remainingJumps", this.getRemainingJumps());
        compound.putByte("amountFed", this.getAmountFed());
        compound.putBoolean("isHungry", this.isHungry());
        compound.putBoolean("isSitting", this.isSitting());
        compound.putInt("typeId", AetherAPI.instance().getMoaId(this.getMoaType()));
        compound.putInt("airTicks", dataTracker.get(AIR_TICKS));
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compound) {
        super.readCustomDataFromNbt(compound);

        this.setPlayerGrown(compound.getBoolean("playerGrown"));
        this.setRemainingJumps(compound.getInt("remainingJumps"));
        this.setMoaType(AetherAPI.instance().getMoa(compound.getInt("typeId")));
        this.setAmountFed(compound.getByte("amountFed"));
        this.setHungry(compound.getBoolean("isHungry"));
        this.setSitting(compound.getBoolean("isSitting"));
        dataTracker.set(AIR_TICKS, compound.getInt("airTicks"));
    }

    @Override
    public boolean shouldSpawnSprintingParticles() {
        return Math.abs(getVelocity().multiply(1, 0, 1).length()) > 0 && !isTouchingWater() && !isGliding();
    }

    @Override
    protected void playStepSound(BlockPos posIn, BlockState stateIn) {
        this.world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_PIG_STEP, SoundCategory.NEUTRAL, 0.15F, 1F);
    }

    public void fall() {
        boolean blockBeneath = !this.world.isAir(new BlockPos(this.getPos()).down(1));

        if (this.getVelocity().y < 0.0D && !this.isSneaking())
            this.setVelocity(this.getVelocity().multiply(1.0D,  isGliding() ? 0.6D : 1D, 1.0D));

        if (blockBeneath) this.setRemainingJumps(this.maxJumps);
    }

    @Override
    public void setJumping(boolean jump) {
        super.setJumping(jump);
    }

    @Override
    public double getMountedHeightOffset() {
        return 1.03;
    }

    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity matingAnimal) {
        return new MoaEntity(this.world, this.getMoaType());
    }

    @Override
    public Identifier getLootTableId() {
        return null;//AetherLootTableList.ENTITIES_MOA;
    }

    @Override
    public void setJumpStrength(int strength) {
        jumpStrength = strength / 5F;
    }

    @Override
    public boolean canJump() {
        return true;
    }

    @Override
    public void startJumping(int height) {
        this.world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_PHANTOM_FLAP, SoundCategory.NEUTRAL, 7.5F, MathHelper.clamp(this.random.nextFloat(), 0.55f, 0.8f));
    }

    @Override
    public void stopJumping() {

    }
}