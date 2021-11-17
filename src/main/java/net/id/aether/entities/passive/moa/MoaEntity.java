package net.id.aether.entities.passive.moa;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.aether.api.MoaAPI;
import net.id.aether.blocks.blockentity.FoodBowlBlockEntity;
import net.id.aether.component.MoaGenes;
import net.id.aether.entities.AetherEntityTypes;
import net.id.aether.entities.util.SaddleMountEntity;
import net.id.aether.items.AetherItems;
import net.minecraft.block.BlockState;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.*;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class MoaEntity extends SaddleMountEntity implements JumpingMount, Tameable {
    public static final TrackedData<Integer> AIR_TICKS = DataTracker.registerData(MoaEntity.class, TrackedDataHandlerRegistry.INTEGER);
    public float curWingRoll, curWingYaw, curLegPitch;
    public float jumpStrength;
    public boolean isInAir;
    protected int secsUntilEgg;
    private MoaGenes genes;

    public MoaEntity(EntityType<? extends MoaEntity> entityType, World world) {
        super(entityType, world);
        this.stepHeight = 1.0F;
        this.secsUntilEgg = this.getRandomEggTime();
    }

    public static DefaultAttributeContainer.Builder createMoaAttributes() {
        return createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 35.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 1.0D);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new MoaEscapeDangerGoal(this, 2));
        this.goalSelector.add(2, new EatFromBowlGoal(1, 24, 16));
        this.goalSelector.add(3, new AnimalMateGoal(this, 0.25F));
//        this.goalSelector.add(4, new TemptGoal(this, 1.0D, Ingredient.ofItems(AetherItems.NATURE_STAFF), false));
        this.goalSelector.add(5, new FollowParentGoal(this, 1.1D));
        this.goalSelector.add(6, new WanderAroundFarGoal(this, 0.65F, 0.1F)); //WanderGoal
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 4.5F));
        this.goalSelector.add(8, new LookAroundGoal(this)); //LookGoal
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        if (!genes.isInitialized()) {
            genes.initMoa(this);
            setHealth(genes.getAttribute(MoaAttributes.MAX_HEALTH));
        }
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Override
    public void move(MovementType movement, Vec3d motion) {
        super.move(movement, motion);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(AIR_TICKS, 0);
    }

    public float getWingRoll() {
        if (!isGliding()) {
            float baseWingRoll = 1.39626F;

            float lDif = -baseWingRoll - curWingRoll;
            if (Math.abs(lDif) > 0.005F) {
                curWingRoll += lDif / 6;
            }
        } else {
            curWingRoll = (MathHelper.sin(age / 1.75F) * 0.725F + 0.1F);
        }
        return curWingRoll;
    }

    public float getWingYaw() {
        float baseWingYaw = isGliding() ? 0.95626F : 0.174533F;

        float lDif = -baseWingYaw - curWingYaw;
        if (Math.abs(lDif) > 0.005F) {
            curWingYaw += lDif / 12.75;
        }
        return curWingYaw;
    }

    public float getLegPitch() {
        float baseLegPitch = isGliding() ? -1.5708F : 0.0174533F;

        float lDif = -baseLegPitch - curLegPitch;
        if (Math.abs(lDif) > 0.005F) {
            curLegPitch += lDif / 6;
        }
        return curLegPitch;
    }

    public int getRandomEggTime() {
        return 775 + this.random.nextInt(50);
    }

    @Override
    protected void playHurtSound(DamageSource source) {
        this.world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_BAT_DEATH, SoundCategory.NEUTRAL, 0.225F, MathHelper.clamp(this.random.nextFloat(), 0.5f, 0.7f) + MathHelper.clamp(this.random.nextFloat(), 0f, 0.15f));
    }

    @Override
    public void tick() {
        super.tick();

        isInAir = !onGround;

        if (isInAir)
            dataTracker.set(AIR_TICKS, dataTracker.get(AIR_TICKS) + 1);
        else
            dataTracker.set(AIR_TICKS, 0);

        if (age % 15 == 0) {
            if (isGliding()) {
                this.world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_PHANTOM_FLAP, SoundCategory.NEUTRAL, 4.5F, MathHelper.clamp(this.random.nextFloat(), 0.85f, 1.2f) + MathHelper.clamp(this.random.nextFloat(), 0f, 0.35f));
            } else if (random.nextFloat() < 0.057334F) {
                this.world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_PARROT_AMBIENT, SoundCategory.NEUTRAL, 1.5F + random.nextFloat() * 2, MathHelper.clamp(this.random.nextFloat(), 0.55f, 0.7f) + MathHelper.clamp(this.random.nextFloat(), 0f, 0.25f));
            }
        }

        if (this.jumping) this.setVelocity(this.getVelocity().add(0.0D, 0.05D, 0.0D));

        this.fall();

        if (hasPassengers()) {
            streamPassengersAndSelf().forEach(entity -> entity.fallDistance = 0);
        }

        MoaGenes genes = getGenes();
        float hunger = genes.getHunger();
        if (genes.isTamed()) {
            if (random.nextBoolean()) {
                genes.setHunger(hunger - (1F / 12000F));
            }
        }
        if (getHealth() < getMaxHealth() && hunger > 65F && world.getTime() % 20 == 0 && random.nextBoolean()) {
            heal(1);
            genes.setHunger(hunger - 0.5F);
        }
        if (hunger < 20F && world.getTime() % 10 + random.nextInt(4) == 0) {
            produceParticles(ParticleTypes.ANGRY_VILLAGER);
            if (hunger < 10F) {
                removeAllPassengers();
            }
        }
        if (getGenes().getRace().legendary() && getVelocity().lengthSquared() <= 0.02 && random.nextFloat() < 0.1F && random.nextBoolean()) {
            produceParticles((ParticleEffect) getGenes().getRace().particles(), 5, 0.25F);
        }

    }


    @Override
    @Environment(EnvType.CLIENT)
    protected Text getDefaultName() {
        return new TranslatableText(getGenes().getRace().getTranslationKey(), "Moa");
    }

    @Override
    protected int computeFallDamage(float fallDistance, float damageMultiplier) {
        return 0;
    }

    public boolean isGliding() {
        return !isTouchingWater() && dataTracker.get(AIR_TICKS) > 20;
    }

    public boolean isBreedingItem(ItemStack stack) {
        return stack.getItem() == AetherItems.ORANGE;
    }

    @Override
    public boolean canBeSaddled() {
        return getGenes().isTamed() && super.canBeSaddled();
    }

    public void travel(Vec3d movementInput) {
        if (this.isAlive()) {
            if (this.hasPassengers() && this.canBeControlledByRider() && this.isSaddled()) {
                LivingEntity livingEntity = (LivingEntity) this.getPrimaryPassenger();
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

                if (isLogicalSideForUpdatingMovement()) {
                    if (this.jumpStrength > 0.0F && !this.isInAir) {
                        double d = 0.1F * (double) this.jumpStrength * (double) this.getJumpVelocityMultiplier();
                        double h;
                        if (this.hasStatusEffect(StatusEffects.JUMP_BOOST)) {
                            h = d + (double) ((float) (this.getStatusEffect(StatusEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F);
                        } else {
                            h = d;
                        }

                        Vec3d vec3d = this.getVelocity();
                        this.setVelocity(vec3d.x, h, vec3d.z);
                        this.velocityDirty = true;
                        if (g > 0.0F) {
                            float adjVel = jumpStrength / 2F;
                            float i = MathHelper.sin(this.getYaw() * 0.017453292F);
                            float j = MathHelper.cos(this.getYaw() * 0.017453292F);
                            this.setVelocity(this.getVelocity().add(-0.4F * i * adjVel, 0.0D, 0.4F * j * adjVel));
                        }

                        this.jumpStrength = 0.0F;
                    }
                }

                if (jumpStrength <= 0.01F && onGround)
                    isInAir = false;

                this.flyingSpeed = getFlyingSpeed();
                if (this.isLogicalSideForUpdatingMovement()) {
                    this.setMovementSpeed(getMountedMoveSpeed());
                    super.travel(new Vec3d(f, movementInput.y, g));
                } else if (livingEntity instanceof PlayerEntity) {
                    this.setVelocity(Vec3d.ZERO);
                }

                this.updateLimbs(this, false);
            } else {
                this.flyingSpeed = getFlyingSpeed();
                super.travel(movementInput);
            }
            if (getGenes().getRace().legendary() && getVelocity().lengthSquared() > 0.02 && random.nextFloat() < 0.55F) {
                produceParticles((ParticleEffect) getGenes().getRace().particles(), 3, 0.25F);
            }
        }
    }

    @Override
    public float getMountedMoveSpeed() {
        return getMovementSpeed() * 0.75F;
    }

    @Override
    public float getMovementSpeed() {
        return getGenes().getAttribute(MoaAttributes.GROUND_SPEED) * 0.65F;
    }

    public float getFlyingSpeed() {
        return isGliding() ? getGenes().getAttribute(MoaAttributes.GLIDING_SPEED) * 0.8F : getMovementSpeed() * 0.1F;
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (!world.isClient()) {
            ItemStack heldStack = player.getStackInHand(hand);
            Item heldItem = heldStack.getItem();
            if (heldItem.isFood() && heldItem.getFoodComponent().isMeat()) {
                if (!getGenes().isTamed()) {
                    if (random.nextFloat() < 0.15F) {
                        getGenes().tame(player.getUuid());
                        produceParticles(ParticleTypes.HEART);
                        playSound(SoundEvents.ENTITY_PARROT_AMBIENT, 2F, 2F);
                    }
                    heldStack.decrement(1);
                    playSound(SoundEvents.ENTITY_PARROT_EAT, 1F, 0.8F);
                    produceParticles(ParticleTypes.ELECTRIC_SPARK);
                } else {
                    float hungerRestored = heldItem.getFoodComponent().getHunger() * 4;
                    float satiation = getGenes().getHunger();
                    float hunger = 100 - satiation;
                    if (hunger > 1) {
                        int consumption = Math.min((int) Math.ceil(hunger / hungerRestored), heldStack.getCount());
                        spawnConsumptionEffects(heldStack, 10 + random.nextInt(consumption * 2 + 1));
                        heldStack.decrement(consumption);
                        getGenes().setHunger(satiation + (consumption * hungerRestored));
                        playSound(SoundEvents.ENTITY_PARROT_EAT, 1.5F, 0.8F);
                        produceParticles(ParticleTypes.HAPPY_VILLAGER);
                    }
                }
                return ActionResult.success(world.isClient());
            }
        }
        return super.interactMob(player, hand);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound compound) {
        super.writeCustomDataToNbt(compound);
        compound.putInt("airTicks", dataTracker.get(AIR_TICKS));
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compound) {
        super.readCustomDataFromNbt(compound);
        dataTracker.set(AIR_TICKS, compound.getInt("airTicks"));
    }

    @Override
    public boolean shouldSpawnSprintingParticles() {
        return Math.abs(getVelocity().multiply(1, 0, 1).length()) > 0 && !isTouchingWater() && !isGliding();
    }

    // todo: this doesn't actually summon the child.
    @Override
    public void breed(ServerWorld world, AnimalEntity other) {
        MoaGenes genes = getGenes();
        if (genes.getHunger() > 80F && genes.isTamed() && other instanceof MoaEntity moa && moa.getGenes().isTamed()) {
            ItemStack egg = genes.getEggForBreeding(((MoaEntity) other).genes, world, getBlockPos());
            playSound(SoundEvents.ENTITY_TURTLE_LAY_EGG, 0.8F, 1.5F);

            ItemScatterer.spawn(world, getX(), getY(), getZ(), egg);
            this.setBreedingAge(6000);
            other.setBreedingAge(6000);
            this.resetLoveTicks();
            other.resetLoveTicks();
            world.sendEntityStatus(this, (byte) 18);
            if (world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
                world.spawnEntity(new ExperienceOrbEntity(world, this.getX(), this.getY(), this.getZ(), this.getRandom().nextInt(16) + 4));
            }
        }
    }

    @Override
    protected void playStepSound(BlockPos posIn, BlockState stateIn) {
        this.world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_PIG_STEP, SoundCategory.NEUTRAL, 0.15F, 1F);
    }

    public void fall() {
        if (this.getVelocity().y < 0.0D && !this.isSneaking())
            this.setVelocity(this.getVelocity().multiply(1.0D, isGliding() ? getGenes().getAttribute(MoaAttributes.GLIDING_DECAY) * 1.4 : 1D, 1.0D));
    }

    @Override
    public void setJumping(boolean jump) {
        super.setJumping(jump);
    }

    @Override
    public double getMountedHeightOffset() {
        return 1.03;
    }

    // todo: right now this only returns a blue moa.
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity matingAnimal) {
        if(!(matingAnimal instanceof MoaEntity matingMoa)){
            return null;
        }
        
        var genesA = getGenes();
        var genesB = matingMoa.getGenes();
        
        var eggStack = genesA.getEggForBreeding(genesB, world, getBlockPos());
        var baby = AetherEntityTypes.MOA.create(world);
        if(baby == null){
            return null;
        }
        var babyGenes = baby.getGenes();
        babyGenes.readFromNbt(eggStack.getOrCreateSubNbt("genes"));
        return baby;
    }

    @Override
    public Identifier getLootTableId() {
        return null;
    }

    @Override
    protected void dropLoot(DamageSource source, boolean causedByPlayer) {
        dropStack(new ItemStack(AetherItems.MOA_MEAT, (int) Math.round(0.337 + random.nextFloat() * getGenes().getAttribute(MoaAttributes.DROP_MULTIPLIER))));
        if (random.nextBoolean()) {
            dropStack(new ItemStack(Items.FEATHER, (int) (Math.round(0.337 + random.nextFloat() * getGenes().getAttribute(MoaAttributes.DROP_MULTIPLIER)) / 2)));
        }
    }

    @Override
    public void setJumpStrength(int strength) {
        jumpStrength = strength * getGenes().getAttribute(MoaAttributes.JUMPING_STRENGTH) * 0.95F;
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

    public MoaGenes getGenes() {
        if (genes == null) {
            genes = MoaGenes.get(this);
        }
        return genes;
    }

    @Nullable
    @Override
    public UUID getOwnerUuid() {
        return getGenes().getOwner();
    }

    @Nullable
    @Override
    public Entity getOwner() {
        return Optional.ofNullable(getOwnerUuid()).map(world::getPlayerByUuid).orElse(null);
    }

    private class MoaEscapeDangerGoal extends EscapeDangerGoal {

        public MoaEscapeDangerGoal(PathAwareEntity mob, double speed) {
            super(mob, speed);
        }

        @Override
        public boolean canStart() {
            boolean ownerNear = MoaEntity.this.getAttacker() != MoaEntity.this.getOwner();
            return ownerNear && super.canStart();
        }
    }

    public class EatFromBowlGoal extends MoveToTargetPosGoal {
        protected int timer;

        public EatFromBowlGoal(double speed, int range, int maxYDifference) {
            super(MoaEntity.this, speed, range, maxYDifference);
        }

        public double getDesiredSquaredDistanceToTarget() {
            return 2.0D;
        }

        public boolean shouldResetPath() {
            return this.tryingTime % 100 == 0;
        }

        protected boolean isTargetPos(WorldView world, BlockPos pos) {
            if (world.getBlockEntity(pos) instanceof FoodBowlBlockEntity foodBowl) {
                ItemStack foodStack = foodBowl.getStack(0);
                Item foodItem = foodStack.getItem();
                return foodItem.isFood() && foodItem.getFoodComponent().isMeat();
            }
            return false;
        }

        public void tick() {
            if (this.hasReached()) {
                if (this.timer >= 20) {
                    this.tryEat();
                } else {
                    ++this.timer;
                }
            } else if (!this.hasReached() && MoaEntity.this.random.nextFloat() < 0.025F) {
                MoaEntity.this.playSound(SoundEvents.ENTITY_PARROT_DEATH, 0.5F, 2.0F);
            }
            super.tick();
        }

        protected void tryEat() {
            if (world.getBlockEntity(targetPos) instanceof FoodBowlBlockEntity foodBowl) {
                ItemStack foodStack = foodBowl.getStack(0);
                Item foodItem = foodStack.getItem();
                if (foodItem.isFood() && foodItem.getFoodComponent().isMeat()) {
                    float hungerRestored = foodItem.getFoodComponent().getHunger() * 4;
                    float satiation = getGenes().getHunger();
                    float hunger = 100 - satiation;
                    if (hunger > 1) {
                        int consumption = Math.min((int) Math.ceil(hunger / hungerRestored), foodStack.getCount());
                        spawnConsumptionEffects(foodStack, 10 + random.nextInt(consumption * 2 + 1));
                        foodStack.decrement(consumption);
                        getGenes().setHunger(satiation + (consumption * hungerRestored));
                        playSound(SoundEvents.ENTITY_PARROT_EAT, 1.5F, 0.8F);
                        produceParticles(ParticleTypes.HAPPY_VILLAGER);
                    }
                }
            }
        }

        @Override
        public boolean canStart() {
            return getGenes().getHunger() < 80F && super.canStart();
        }

        @Override
        public boolean shouldContinue() {
            return MoaEntity.this.getGenes().getHunger() < 98F && super.shouldContinue();
        }

        public void start() {
            this.timer = 0;
            super.start();
        }
    }
}