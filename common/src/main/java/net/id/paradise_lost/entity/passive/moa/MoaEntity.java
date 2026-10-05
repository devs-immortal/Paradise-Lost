package net.id.paradise_lost.entity.passive.moa;

import net.id.paradise_lost.platform.Services;
import net.minecraft.tags.ItemTags;
import net.id.paradise_lost.block.blockentity.FoodBowlBlockEntity;
import net.id.paradise_lost.component.MoaGenes;
import net.id.paradise_lost.registry.EntityRegistry;
import net.id.paradise_lost.entity.util.SaddleMountEntity;
import net.id.paradise_lost.registry.ItemRegistry;
import net.id.paradise_lost.item.tool.bloodstone.BloodstoneItem;
import net.id.paradise_lost.item.ParadiseLostDataComponentTypes;
import net.id.paradise_lost.tag.ParadiseLostItemTags;
import net.id.paradise_lost.util.DummyInventory;
import net.id.paradise_lost.util.ParadiseLostSoundEvents;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.HasCustomInventoryScreen;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.InteractGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.AbstractChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class MoaEntity extends SaddleMountEntity implements PlayerRideableJumping, OwnableEntity, ContainerListener, HasCustomInventoryScreen {
    private static final SimpleContainer DUMMY = new DummyInventory();

    public static final EntityDataAccessor<Integer> AIR_TICKS = SynchedEntityData.defineId(MoaEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<ItemStack> CHEST = SynchedEntityData.defineId(MoaEntity.class, EntityDataSerializers.ITEM_STACK);

    private static final EntityDataAccessor<CompoundTag> GENES = SynchedEntityData.defineId(MoaEntity.class, EntityDataSerializers.COMPOUND_TAG);
    public float curWingRoll, curWingYaw, curLegPitch;
    public float jumpStrength;
    public boolean isInAir;
    protected int secsUntilEgg;
    private MoaGenes genes;

    @NotNull private SimpleContainer inventory = DUMMY;

    public MoaEntity(EntityType<? extends MoaEntity> entityType, Level world) {
        super(entityType, world);
        this.secsUntilEgg = this.getRandomEggTime();
        refreshChest(false);
    }

    public static AttributeSupplier.Builder createMoaAttributes() {
        return createMobAttributes()
                .add(Attributes.MAX_HEALTH, 35.0D)
                .add(Attributes.MOVEMENT_SPEED, 1.0D)
                .add(Attributes.STEP_HEIGHT, 1.0);

    }

    @Override
    protected void registerGoals() {

        this.goalSelector.addGoal(0, new MoaEscapeDangerGoal(this, 0.9f));

        this.goalSelector.addGoal(1, new EatFromBowlGoal(0.4, 24, 16));
        this.goalSelector.addGoal(1, new BreedGoal(this, 0.25F));
        this.goalSelector.addGoal(2, new TemptGoal(this, 0.7D, Ingredient.of(ParadiseLostItemTags.MOA_TEMPTABLES), false));

        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Parrot.class, 18F, 100f));
        this.goalSelector.addGoal(7, new InteractGoal(this, Parrot.class, 25, 120f));

        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, LivingEntity.class, 10F, 150f));
        this.goalSelector.addGoal(8, new InteractGoal(this, LivingEntity.class, 4, 180f));

        this.goalSelector.addGoal(9, new WaterAvoidingRandomStrollGoal(this, 0.32F, 0.01f));
        this.goalSelector.addGoal(9, new MoaWanderAroundGoal(this, 0.320D, 230));

        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(12, new FollowParentGoal(this, 0.33D));

        super.registerGoals();
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType spawnReason, @Nullable SpawnGroupData entityData) {

        MoaGenes genes = getGenes();
        if (!genes.isInitialized()) {
            genes.initMoa(this);
            setHealth(genes.getAttribute(MoaAttributes.MAX_HEALTH));
            syncGenes();
        }
        return super.finalizeSpawn(world, difficulty, spawnReason, entityData);
    }

    @Override
    public void move(MoverType movement, Vec3 motion) {
        super.move(movement, motion);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(AIR_TICKS, 0);
        builder.define(CHEST, ItemStack.EMPTY);
        builder.define(GENES, new CompoundTag());
    }

    public ItemStack getChest() {
        return entityData.get(CHEST);
    }

    public boolean hasChest() {
        return !getChest().isEmpty();
    }

    public void setChest(ItemStack stack) {
        if (!stack.isEmpty() && (!(stack.getItem() instanceof BlockItem blockItem) || !(blockItem.getBlock() instanceof AbstractChestBlock<?>))) {
            throw new IllegalArgumentException("Can not set a Moa chest to be a non-chest or empty item stack!");
        }
        entityData.set(CHEST, stack);
        refreshChest(true);
    }

    public void refreshChest(boolean scatterItems) {
        if (hasChest()) {
            if (inventory.getContainerSize() != 20) {
                inventory = new SimpleContainer(20);
                inventory.addListener(this);
            }
        } else {
            if (!inventory.isEmpty()) {
                inventory.removeListener(this);
                if (scatterItems && !level().isClientSide) {
                    Containers.dropContents(level(), this, inventory);
                }
                inventory.clearContent();
                inventory = DUMMY;
            }
        }
    }

    @Override
    protected void dropEquipment() {
        super.dropEquipment();
        if (hasChest()) {
            if (!level().isClientSide) {
                spawnAtLocation(getChest());
            }
            setChest(ItemStack.EMPTY);
        }
    }

    float wingFlapSpeed = 2.5f;
    float idleFlapSpeed = 12;
    float randFlapSpeed, randFlapTimer = 200f, prevWingRoll, prevWingYaw;
    private int flapCount = 0;
    private int prevFlapCount = 0;
    private boolean atWingBottom;
    private boolean atWingYawBottom;

    void pickRollOrYawFlapping(boolean theFinalPickForRollingOrYawingTheWingsOfTheMoa) {
        prevFlapCount = flapCount;
        if (theFinalPickForRollingOrYawingTheWingsOfTheMoa) {
            if (flapCount > 0 && !isSaddled()) {

                float baseWingRoll = Mth.sin(randFlapTimer / (wingFlapSpeed - randFlapSpeed)) * (0.67F + (randFlapSpeed / 8));
                float lDif = -baseWingRoll - curWingRoll;
                if (Math.abs(lDif) > 0.005F) {
                    curWingRoll += lDif / 4;
                }

                if (curWingRoll < prevWingRoll && !atWingBottom) {
                    atWingBottom = true;
                    flapCount--;
                } else if (curWingRoll > prevWingRoll) {

                    atWingBottom = false;
                }
                prevWingRoll = curWingRoll;

            } else {

                float baseWingRoll = 1.39626F;
                float lDif = -baseWingRoll - curWingRoll;
                if (Math.abs(lDif) > 0.005F) {
                    curWingRoll += lDif / 6;
                }
            }
        } else {
            if (flapCount > 0 && !isSaddled()) {

                float baseWingYaw = Mth.sin(randFlapTimer / 1) * (0.42F + (randFlapSpeed / 12));
                float lDif = -baseWingYaw - curWingYaw;
                if (Math.abs(lDif) > 0.005F) {
                    curWingYaw += lDif / 3;
                }

                if (curWingYaw < prevWingYaw && !atWingYawBottom) {
                    atWingYawBottom = true;
                    flapCount--;
                } else if (curWingYaw > prevWingYaw) {

                    atWingYawBottom = false;
                }
                prevWingYaw = curWingYaw;

            } else {

                float baseWingYaw = 0.174533F;
                float lDif = -baseWingYaw - curWingYaw;
                if (Math.abs(lDif) > 0.005F) {
                    curWingYaw += lDif / 6;
                }
            }
        }
    }

    public float getWingRoll() {
        if (flapCount <= 0 || isSaddled()) {
            if (entityData.get(AIR_TICKS) >= 4) {

                curWingRoll = Mth.sin(tickCount / wingFlapSpeed) * 0.73F + 0.1F;
                atWingBottom = false;
            } else {

                float baseWingRoll = Mth.sin(tickCount / idleFlapSpeed + (randFlapSpeed * 0.1f)) * 0.05F + 1.39626F;
                float lDif = -baseWingRoll - curWingRoll;
                if (Math.abs(lDif) > 0.0005F) {
                    curWingRoll += lDif / 6;
                }
            }
        }
        return curWingRoll;
    }

    public float getWingYaw() {
        if (flapCount <= 0 || isSaddled()) {
            float baseWingYaw = isGliding() ? 0.95626F : 0.174533F;
            float lDif = -baseWingYaw - curWingYaw;
            if (Math.abs(lDif) > 0.005F) {
                curWingYaw += lDif / 12.75f;
            }
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

    public float getRandomFloat(float from, float to) {
        float finalNumber = 1;
        from *= 100;
        to *= 100;
        finalNumber = from + this.random.nextInt((int) to);
        return finalNumber / 100;
    }

    int moaSoundCallCooldown = 200;
    private float soundChance = 0;
    private float songChance = 0;

    public void attemptMoaSound() {
        if (this.random.nextFloat() < 0.2f + this.soundChance) {
            if (this.random.nextFloat() > 0.05f + this.songChance || isBaby()) {

                this.moaSoundCallCooldown = (int) getRandomFloat(60, 150);
                this.songChance += getRandomFloat(0.04f, 0.1f);

                if (!isBaby()) {
                    this.level().playSound(null, this.getX(), this.getY(), this.getZ(), ParadiseLostSoundEvents.ENTITY_MOA_AMBIENT, SoundSource.NEUTRAL, 0.4f, getRandomFloat(0.85f, 0.92f));
                } else {
                    this.level().playSound(null, this.getX(), this.getY(), this.getZ(), ParadiseLostSoundEvents.ENTITY_MOA_AMBIENT, SoundSource.NEUTRAL, 0.35f, getRandomFloat(1f, 1.1f));
                }
            } else {

                this.moaSoundCallCooldown = (int) getRandomFloat(60, 150);
                this.songChance = 0;
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), ParadiseLostSoundEvents.ENTITY_MOA_AMBIENT_SING, SoundSource.NEUTRAL, 0.3f, getRandomFloat(0.98f, 1.02f));
            }
        } else {
            if (isSaddled()) {
                this.moaSoundCallCooldown = (int) getRandomFloat(60, 150);
                this.soundChance += getRandomFloat(0.02f, 0.06f);
            } else {
                this.moaSoundCallCooldown = (int) getRandomFloat(30, 90);
                this.soundChance += getRandomFloat(0.02f, 0.09f);
            }
        }
    }
    private boolean canFlap = true;
    public void attemptMoaFlap(boolean bypassFlapCheck) {
        if (getWingRoll() > 0.8 && canFlap || bypassFlapCheck) {
            if (!this.level().isClientSide) {
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), ParadiseLostSoundEvents.ENTITY_MOA_GLIDING, SoundSource.NEUTRAL, 0.9F, getRandomFloat(0.9f, 0.97f));
            }
            this.canFlap = false;
        } else if (getWingRoll() < -0.3f) {
            this.canFlap = true;
        }
    }

    public int getRandomEggTime() {
        return 775 + this.random.nextInt(50);
    }

    @Override
    protected void playHurtSound(DamageSource source) {
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), ParadiseLostSoundEvents.ENTITY_MOA_HURT, SoundSource.NEUTRAL, 0.2F, getRandomFloat(0.78f, 0.82f));
    }
    boolean shouldRoll;
    @Override
    public void tick() {
        isInAir = !onGround();

        if (!this.level().isClientSide) {
            if (this.moaSoundCallCooldown > 0) {
                this.moaSoundCallCooldown--;
            } else {
                this.attemptMoaSound();
            }
        }

        if (flapCount > 0 && !isSaddled()) {
            pickRollOrYawFlapping(shouldRoll);
        }

        if (isInAir) {
            entityData.set(AIR_TICKS, entityData.get(AIR_TICKS) + 1);
            attemptMoaFlap(false);
        } else {
            entityData.set(AIR_TICKS, 0);
            if (randFlapTimer <= 0) {
                shouldRoll = getRandomFloat(0f, 1f) > 0.5f;
                randFlapSpeed = getRandomFloat(0f, 2f);
                flapCount = (int) getRandomFloat(2, 6);
                randFlapTimer = (int) getRandomFloat(150, 800);
            } else {
                randFlapTimer--;
            }
        }

        if (isVehicle()) {
            getPassengersAndSelf().forEach(entity -> entity.fallDistance = 0);
        }
        MoaGenes genes = getGenes();
        float hunger = genes.getHunger();
        if (genes.isTamed()) {
            if (random.nextBoolean()) {
                genes.setHunger(hunger - (1F / 12000F));
            }
        }
        if (getHealth() < getMaxHealth() && hunger > 65F && level().getGameTime() % 20 == 0 && random.nextBoolean()) {
            heal(1);
            genes.setHunger(hunger - 0.5F);
        }

        if ((hunger < 15F && level().getGameTime() % 10 == 0 && isSaddled())) {
            produceParticlesServer(ParticleTypes.ANGRY_VILLAGER, random.nextInt(3), 1, 0);
            if (hunger < 8F && isVehicle()) {
                ejectPassengers();
                playSound(ParadiseLostSoundEvents.ENTITY_MOA_DEATH, 0.15f, 1.5F + random.nextFloat() * 0.5F);
            }
        }
        if (getGenes().getRace().legendary() && getDeltaMovement().lengthSqr() <= 0.02 && random.nextFloat() < 0.1F && random.nextBoolean()) {
            produceParticles((ParticleOptions) getGenes().getRace().particles(), 5, 0.25F);
        }

        this.fall();
        super.tick();
    }

    @Override
        protected Component getTypeName() {
        return Component.translatable(getGenes().getRaceTranslationKey(), "Moa");
    }

    @Override
    protected int calculateFallDamage(float fallDistance, float damageMultiplier) {
        return 0;
    }

    public boolean isGliding() {
        return !isInWater() && entityData.get(AIR_TICKS) > 4;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(ParadiseLostItemTags.MOA_BREEDABLES);
    }

    @Override
    public boolean isSaddleable() {
        return getGenes().isTamed() && super.isSaddleable();
    }

    @Override
    public boolean canBeControlledByRider() {
        return this.isSaddled();
    }

    @Nullable
    public LivingEntity getControllingPassenger() {
        Entity var3 = this.getFirstPassenger();
        if (var3 instanceof Mob mobEntity) {
            return mobEntity;
        } else {
            if (this.isSaddled()) {
                var3 = this.getFirstPassenger();
                if (var3 instanceof Player) {
                    Player playerEntity = (Player) var3;
                    return playerEntity;
                }
            }
            return null;
        }
    }

    protected Vec3 getRiddenInput(Player controllingPlayer, Vec3 movementInput) {
        float f = controllingPlayer.xxa * 0.5F;
        float g = controllingPlayer.zza;
        if (g <= 0.0F) {
            g *= 0.25F;
        }
        return new Vec3(f, 0.0, g);
    }

    float curGroundSpeed, curFlyingSpeed;
    float groundAcceleration = 0.004F;
    float flyingAcceleration = 0.04F;

    float genGroundSpeed, genGlidingSpeed, genGlidingDecay, genJumpHeight;

    private void calcGeneSpeeds() {
        genGroundSpeed = (getGenes().getAttribute(MoaAttributes.GROUND_SPEED) - 0.24f) * 0.072f + 0.1f;
        genGlidingSpeed = (getGenes().getAttribute(MoaAttributes.GLIDING_SPEED) - 0.055f) * 0.52f + 0.37f;
        genGlidingDecay = (getGenes().getAttribute(MoaAttributes.GLIDING_DECAY) * -0.23f) + 0.42f;
        genJumpHeight = (getGenes().getAttribute(MoaAttributes.JUMPING_STRENGTH) - 0.15f) * 0.14f + 0.033f;

        groundAcceleration = (getGenes().getAttribute(MoaAttributes.GROUND_SPEED) - 0.24f) * 0.05f + 0.01f;
        flyingAcceleration = (getGenes().getAttribute(MoaAttributes.GLIDING_SPEED) * 0.1f);
    }

    private void calcAcceleration(Player controllingPlayer) {
        float f = controllingPlayer.xxa * 0.5F;
        float g = controllingPlayer.zza;
        calcGeneSpeeds();

        if (g == 0 && f == 0) {
            curGroundSpeed = Math.clamp(curGroundSpeed - 0.1f, genGroundSpeed * 0.07f, genGroundSpeed);
            curFlyingSpeed = Math.clamp(curFlyingSpeed - 0.05f, genGlidingSpeed * 0.1f, genGlidingSpeed);
        } else {
            curGroundSpeed = Math.clamp(curGroundSpeed + groundAcceleration, genGroundSpeed * 0.07f, genGroundSpeed);
            curFlyingSpeed = Math.clamp(curFlyingSpeed + flyingAcceleration + (Math.abs((float) getDeltaMovement().y / 10)), genGlidingSpeed * 0.1f, genGlidingSpeed);
        }
    }

    @Override
    protected void tickRidden(Player controllingPlayer, Vec3 movementInput) {
        if (this.isAlive()) {
            if (this.isVehicle() && controllingPlayer != null && this.canBeControlledByRider()) {
                this.yRotO = this.getYRot();
                this.setYRot(controllingPlayer.getYRot());
                this.setXRot(controllingPlayer.getXRot() * 0.5F);
                this.setRot(this.getYRot(), this.getXRot());
                this.yBodyRot = this.getYRot();
                this.yHeadRot = this.yBodyRot;
                var movement = getRiddenInput(controllingPlayer, movementInput);

                calcAcceleration(controllingPlayer);
                if (this.jumpStrength > 0.0F && this.onGround() && !this.isInAir) {

                    double jumpVelocity = 0.1 * this.jumpStrength * this.getBlockJumpFactor();

                    if (this.hasEffect(MobEffects.JUMP)) {
                        int boostLevel = this.getEffect(MobEffects.JUMP).getAmplifier() + 1;
                        jumpVelocity += boostLevel * 0.1;
                    }

                    Vec3 currentVelocity = this.getDeltaMovement();
                    this.setDeltaMovement(currentVelocity.x, jumpVelocity, currentVelocity.z);

                    if (movement.z > 0.0F) {
                        float adjVel = jumpStrength / 3F;
                        float i = Mth.sin(this.getYRot() * 0.017453292F);
                        float j = Mth.cos(this.getYRot() * 0.017453292F);
                        this.setDeltaMovement(this.getDeltaMovement().add(-0.3F * i * adjVel, 0.0D, 0.3F * j * adjVel));
                    }
                    this.jumpStrength = 0.0F;
                }

                if (jumpStrength <= 0.01F && onGround()) {
                    this.jumpStrength = 0.0F;
                    this.jumping = false;
                    isInAir = false;
                }

                if (this.isControlledByLocalInstance()) {
                    super.travel(new Vec3(movement.x, movement.y, movement.z));
                } else {
                    this.setDeltaMovement(Vec3.ZERO);
                }

                this.calculateEntityAnimation(false);
            } else {
                super.travel(movementInput);
            }

            if (getGenes().getRace().legendary() && getDeltaMovement().lengthSqr() > 0.02 && random.nextFloat() < 0.55F) {
                produceParticles((ParticleOptions) getGenes().getRace().particles(), 3, 0.25F);
            }
        }
    }
    @Override
    protected void updateWalkAnimation(float posDelta) {
        if (isVehicle()) {
            float f = Math.min(posDelta * 2.0F, 0.5F);
            this.walkAnimation.update(f, 0.4F);
        } else {
            float f = Math.min(posDelta * 4.0F, 3F);
            this.walkAnimation.update(f, 0.5F);
        }

    }

    @Override
    public float getMountedMoveSpeed() {
        return getSpeed();
    }

    @Override
    public float getSpeed() {
        if (!isVehicle()) {
            return isGliding() ? 0.1f : 0.08f;
        } else {
            return isGliding() ? curFlyingSpeed : curGroundSpeed;
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (player.getItemInHand(hand).getItem() instanceof BloodstoneItem) {
            return InteractionResult.PASS;
        }

        if (!level().isClientSide()) {
            ItemStack heldStack = player.getItemInHand(hand);
            if (getGenes().isTamed()) {

                if (player.isShiftKeyDown()) {
                    openCustomInventoryScreen(player);
                    return InteractionResult.SUCCESS;
                }

                if (heldStack.isEmpty()) {
                    return super.mobInteract(player, hand);
                }

                var item = heldStack.getItem();
                if (heldStack.is(ItemTags.MEAT)) {
                    feedMob(heldStack);
                    return InteractionResult.sidedSuccess(level().isClientSide());
                } else if (!hasChest() && item instanceof BlockItem blockItem && blockItem.getBlock() instanceof AbstractChestBlock) {

                    var chestStack = heldStack.copy();
                    chestStack.setCount(1);
                    if (!player.isCreative()) {
                        heldStack.shrink(1);
                    }
                    setChest(chestStack);
                    return InteractionResult.sidedSuccess(level().isClientSide);
                }
            } else {
                if (!heldStack.is(ParadiseLostItemTags.MOA_BREEDABLES) && heldStack.is(ParadiseLostItemTags.MOA_TEMPTABLES)) {
                    usePlayerItem(player, hand, heldStack);
                    playSound(ParadiseLostSoundEvents.ENTITY_MOA_EAT, 1, 0.4F + random.nextFloat() / 3F);
                    produceParticlesServer(new ItemParticleOption(ParticleTypes.ITEM, heldStack), 2 + random.nextInt(4), 7, 0);

                    var foodComponent = heldStack.getOrDefault(DataComponents.FOOD, null);
                    if (random.nextFloat() < 0.04F * (foodComponent == null ? 2 : foodComponent.nutrition())) {
                        getGenes().tame(player.getUUID());
                        syncGenes();
                        playSound(ParadiseLostSoundEvents.ENTITY_MOA_AMBIENT, 0.34f, 0.75F);
                        produceParticlesServer(ParticleTypes.HAPPY_VILLAGER, 2 + random.nextInt(4), 7, 0);
                        if (player instanceof ServerPlayer) {
                            CriteriaTriggers.TAME_ANIMAL.trigger((ServerPlayer) player, this);
                        }
                    }

                    return InteractionResult.CONSUME;
                }
            }
        }
        return super.mobInteract(player, hand);
    }

    private void feedMob(ItemStack heldStack) {
        float hungerRestored = heldStack.get(DataComponents.FOOD).nutrition() * 4;
        float satiation = getGenes().getHunger();
        float hunger = 100 - satiation;
        if (hunger > 1) {
            int consumption = Math.min((int) Math.ceil(hunger / hungerRestored), heldStack.getCount());
            triggerItemUseEffects(heldStack, 10 + random.nextInt(consumption * 2 + 1));
            heldStack.shrink(consumption);
            getGenes().setHunger(satiation + (consumption * hungerRestored));
            playSound(ParadiseLostSoundEvents.ENTITY_MOA_EAT, 1.5F, 0.8F);
            produceParticles(ParticleTypes.HAPPY_VILLAGER);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("airTicks", entityData.get(AIR_TICKS));
        compound.put("chest", entityData.get(CHEST).saveOptional(this.registryAccess()));
        if (inventory != DUMMY) {
            // SimpleContainer.createTag/fromTag pack non-empty stacks into consecutive slots,
            // which reshuffles the chest on relog. Write Slot indices ourselves.
            compound.put("chestContents", saveChestContents());
        }
        CompoundTag genesTag = new CompoundTag();
        getGenes().writeToNbt(genesTag, this.registryAccess());
        compound.put("moaGenes", genesTag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        entityData.set(AIR_TICKS, compound.getInt("airTicks"));
        entityData.set(CHEST, ItemStack.parseOptional(this.registryAccess(), compound.getCompound("chest")));
        refreshChest(false);
        if (inventory != DUMMY && compound.contains("chestContents", Tag.TAG_LIST)) {
            loadChestContents(compound.getList("chestContents", Tag.TAG_COMPOUND));
        }
        if (compound.contains("moaGenes", Tag.TAG_COMPOUND)) {
            getGenes().readFromNbt(compound.getCompound("moaGenes"), this.registryAccess());
        }
        syncGenes();

        setSpeed(getGenes().getAttribute(MoaAttributes.GROUND_SPEED));
        calcGeneSpeeds();
        moaSoundCallCooldown = 50 + random.nextInt(150);
        songChance = Mth.clamp(random.nextFloat(), 0f, 0.3f);
        randFlapTimer = getRandomFloat(60, 1000);
    }

    @Override
    public boolean canSpawnSprintParticle() {
        return curGroundSpeed >= genGroundSpeed - 0.01f;

    }

    @Override
    public void spawnChildFromBreeding(ServerLevel world, Animal other) {
        MoaGenes genes = getGenes();
        if (genes.getHunger() > 80F) {
            ItemStack egg = genes.getEggForBreeding(((MoaEntity) other).getGenes(), world, blockPosition());
            playSound(ParadiseLostSoundEvents.ENTITY_MOA_LAY_EGG, 0.8F, 1.5F);

            Containers.dropItemStack(world, getX(), getY(), getZ(), egg);
            this.setAge(6000);
            other.setAge(6000);
            this.resetLove();
            other.resetLove();
            world.broadcastEntityEvent(this, (byte) 18);
            if (world.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
                world.addFreshEntity(new ExperienceOrb(world, this.getX(), this.getY(), this.getZ(), this.getRandom().nextInt(16) + 4));
            }

            produceParticlesServer(ParticleTypes.HAPPY_VILLAGER, 6 + random.nextInt(7), 1, 0);
            ((MoaEntity) other).produceParticlesServer(ParticleTypes.HAPPY_VILLAGER, 6 + random.nextInt(7), 1, 0);
        }
    }

    @Override
    protected void playStepSound(BlockPos posIn, BlockState stateIn) {
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), ParadiseLostSoundEvents.ENTITY_MOA_STEP, SoundSource.NEUTRAL, 0.12F, 1F);
    }

    public void fall() {
        calcGeneSpeeds();
        if (this.getDeltaMovement().y < 0.0D && !this.isShiftKeyDown()) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(1D, isGliding() ? genGlidingDecay : 1f, 1.0D));
        }

    }

    @Override
    public void setJumping(boolean jump) {
        super.setJumping(jump);
    }

    public double getMountedHeightOffset() {
        return 1.03;
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob matingAnimal) {
        if (!(matingAnimal instanceof MoaEntity matingMoa)) {
            return null;
        }

        var genesA = getGenes();
        var genesB = matingMoa.getGenes();

        var eggStack = genesA.getEggForBreeding(genesB, world, blockPosition());
        var baby = EntityRegistry.MOA.get().create(world);
        if (baby == null) {
            return null;
        }
        var babyGenes = baby.getGenes();
        babyGenes.fromComponent(eggStack.get(ParadiseLostDataComponentTypes.MOA_GENES), world.registryAccess());
        baby.syncGenes();
        return baby;
    }

    @Override
    protected void dropFromLootTable(DamageSource source, boolean causedByPlayer) {
        super.dropFromLootTable(source, causedByPlayer);
        spawnAtLocation(new ItemStack(ItemRegistry.MOA_MEAT.get(), (int) Math.round(0.337 + random.nextFloat() * getGenes().getAttribute(MoaAttributes.DROP_MULTIPLIER))));
    }

    @Override
    public void onPlayerJump(int heldJumpStrength) {

        if (this.isSaddled()) {
            if (heldJumpStrength <= 0.2f) {
                heldJumpStrength = 0;
            } else {
                if (heldJumpStrength >= 0.9f) {
                    jumpStrength = ((heldJumpStrength * 4) - 2.8f) * genJumpHeight;
                } else {
                    jumpStrength = ((heldJumpStrength * 4) - 3) * genJumpHeight;
                }

                this.jumping = true;
            }
        } else {
            heldJumpStrength = 0;
            this.jumping = false;
        }
    }

    @Override
    public boolean canJump() {
        return this.isSaddled();
    }

    @Override
    public void handleStartJump(int height) {
        if (!isGliding() && onGround()) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), ParadiseLostSoundEvents.ENTITY_MOA_GLIDING, SoundSource.NEUTRAL, 0.25F, getRandomFloat(0.64f, 0.69f));
            this.jumping = true;
        } else {
            this.jumping = false;
        }
    }

    @Override
    public void handleStopJump() {
    }

    public MoaGenes getGenes() {
        if (genes == null) {
            genes = level().isClientSide ? readSyncedGenes() : new MoaGenes();
        }
        return genes;
    }

    private MoaGenes readSyncedGenes() {
        CompoundTag tag = entityData.get(GENES);
        if (tag.isEmpty()) {
            return new MoaGenes();
        }
        MoaGenes synced = new MoaGenes();
        synced.readFromNbt(tag, this.registryAccess());
        return synced;
    }

    public void syncGenes() {
        if (level().isClientSide) {
            return;
        }
        CompoundTag tag = new CompoundTag();
        getGenes().writeToNbt(tag, this.registryAccess());
        entityData.set(GENES, tag);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);

        if (key == GENES && level().isClientSide) {

            this.genes = null;
        }
    }

    @Nullable
    @Override
    public UUID getOwnerUUID() {
        return getGenes().getOwner();
    }

    @Nullable
    @Override
    public LivingEntity getOwner() {
        return Optional.ofNullable(getOwnerUUID()).map(level()::getPlayerByUUID).orElse(null);
    }

    @Override
    public void containerChanged(Container sender) {

    }

    @Override
    public void openCustomInventoryScreen(Player player) {
        if (!level().isClientSide && (!isVehicle() || hasPassenger(player)) && getGenes().isTamed() && player instanceof ServerPlayer serverPlayer) {
            Services.MISC.openMoaScreen(serverPlayer, this);
        }
    }

    @Override
    protected Vec3 getPassengerAttachmentPoint(Entity passenger, EntityDimensions dimensions, float scaleFactor) {
        return new Vec3(0.0F, this.getPassengerAttachmentY(dimensions, scaleFactor), 0.0F);
    }

    protected float getPassengerAttachmentY(EntityDimensions dimensions, float scaleFactor) {
        return dimensions.height() + -0.75F * scaleFactor;
    }

    public Container getInventory() {
        return inventory;
    }

    private net.minecraft.nbt.ListTag saveChestContents() {
        net.minecraft.nbt.ListTag list = new net.minecraft.nbt.ListTag();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }
            CompoundTag entry = new CompoundTag();
            entry.putByte("Slot", (byte) i);
            list.add(stack.save(this.registryAccess(), entry));
        }
        return list;
    }

    private void loadChestContents(net.minecraft.nbt.ListTag list) {
        inventory.clearContent();
        boolean hasSlots = false;
        for (int i = 0; i < list.size(); i++) {
            if (list.getCompound(i).contains("Slot", Tag.TAG_BYTE)) {
                hasSlots = true;
                break;
            }
        }
        if (hasSlots) {
            for (int i = 0; i < list.size(); i++) {
                CompoundTag entry = list.getCompound(i);
                int slot = entry.getByte("Slot") & 255;
                if (slot < inventory.getContainerSize()) {
                    inventory.setItem(slot, ItemStack.parse(this.registryAccess(), entry).orElse(ItemStack.EMPTY));
                }
            }
        } else {
            // Pre-fix worlds: packed list with no Slot indices — best-effort restore
            inventory.fromTag(list, this.registryAccess());
        }
    }

    private class MoaEscapeDangerGoal extends PanicGoal {

        MoaEscapeDangerGoal(PathfinderMob mob, double speed) {
            super(mob, speed);
        }

        @Override
        public boolean canUse() {
            boolean ownerNear = MoaEntity.this.getLastHurtByMob() != MoaEntity.this.getOwner();
            return ownerNear && super.canUse();
        }
    }

    public class EatFromBowlGoal extends MoveToBlockGoal {
        protected int timer;

        public EatFromBowlGoal(double speed, int range, int maxYDifference) {
            super(MoaEntity.this, speed, range, maxYDifference);
        }

        public double getDesiredSquaredDistanceToTarget() {
            return 4.0D;
        }

        public boolean shouldRecalculatePath() {
            return this.tryTicks % 100 == 0;
        }

        protected boolean isValidTarget(LevelReader world, BlockPos pos) {
            if (world.getBlockEntity(pos) instanceof FoodBowlBlockEntity foodBowl) {
                ItemStack foodStack = foodBowl.getContainedItem();
                return foodStack.is(ItemTags.MEAT);
            }
            return false;
        }

        public void tick() {
            if (this.isReachedTarget()) {
                if (this.timer >= 20) {
                    this.tryEat();
                } else {
                    ++this.timer;
                }
            } else if (!this.isReachedTarget() && MoaEntity.this.random.nextFloat() < 0.025F) {
                MoaEntity.this.playSound(ParadiseLostSoundEvents.ENTITY_MOA_DEATH, 0.5F, 2.0F);
            }

            super.tick();
        }

        protected void tryEat() {
            if (level().getBlockEntity(blockPos) instanceof FoodBowlBlockEntity foodBowl) {
                ItemStack foodStack = foodBowl.getContainedItem();
                if (foodStack.is(ItemTags.MEAT)) {
                    feedMob(foodStack);
                }
            }
        }

        @Override
        public boolean canUse() {
            return getGenes().getHunger() < 80F && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return MoaEntity.this.getGenes().getHunger() < 98F && super.canContinueToUse();
        }

        @Override
        public void start() {
            this.timer = 0;
            super.start();
        }
    }
}
