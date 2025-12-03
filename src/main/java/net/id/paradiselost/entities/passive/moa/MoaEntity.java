package net.id.paradiselost.entities.passive.moa;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.id.paradiselost.blocks.blockentity.FoodBowlBlockEntity;
import net.id.paradiselost.component.MoaGenes;
import net.id.paradiselost.entities.ParadiseLostEntityTypes;
import net.id.paradiselost.entities.util.SaddleMountEntity;
import net.id.paradiselost.items.ParadiseLostItems;
import net.id.paradiselost.items.tools.bloodstone.BloodstoneItem;
import net.id.paradiselost.items.utils.ParadiseLostDataComponentTypes;
import net.id.paradiselost.screen.handler.MoaScreenHandler;
import net.id.paradiselost.tag.ParadiseLostItemTags;
import net.id.paradiselost.util.DummyInventory;
import net.id.paradiselost.util.ParadiseLostSoundEvents;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.AbstractChestBlock;
import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryChangedListener;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class MoaEntity extends SaddleMountEntity implements JumpingMount, Tameable, InventoryChangedListener, RideableInventory {
    private static final SimpleInventory DUMMY = new DummyInventory();

    public static final TrackedData<Integer> AIR_TICKS = DataTracker.registerData(MoaEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<ItemStack> CHEST = DataTracker.registerData(MoaEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);
    public float curWingRoll, curWingYaw, curLegPitch;
    public float jumpStrength;
    public boolean isInAir;
    protected int secsUntilEgg;
    private MoaGenes genes;
    
    @NotNull private SimpleInventory inventory = DUMMY;

    public MoaEntity(EntityType<? extends MoaEntity> entityType, World world) {
        super(entityType, world);
        this.secsUntilEgg = this.getRandomEggTime();
        refreshChest(false);
    }

    public static DefaultAttributeContainer.Builder createMoaAttributes() {
        return createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 35.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 1.0D)
                .add(EntityAttributes.GENERIC_STEP_HEIGHT, 1.0);
                //.add(EntityAttributes.GENERIC_GRAVITY, 0.95f)
                //.add(EntityAttributes.GENERIC_JUMP_STRENGTH, 0.22f)
                //.add(EntityAttributes.GENERIC_FLYING_SPEED, 0.5f);
    }

    @Override
    protected void initGoals() {

        this.goalSelector.add(0, new MoaEscapeDangerGoal(this, 0.9f));

        this.goalSelector.add(1, new EatFromBowlGoal(0.4, 24, 16));
        this.goalSelector.add(1, new AnimalMateGoal(this, 0.25F));
        this.goalSelector.add(2, new TemptGoal(this, 0.7D, Ingredient.fromTag(ParadiseLostItemTags.MOA_TEMPTABLES), false));


        this.goalSelector.add(7, new LookAtEntityGoal(this, ParrotEntity.class, 18F, 100f));
        this.goalSelector.add(7, new StopAndLookAtEntityGoal(this, ParrotEntity.class, 25, 120f));

        this.goalSelector.add(8, new LookAtEntityGoal(this, LivingEntity.class, 10F, 150f));
        this.goalSelector.add(8, new StopAndLookAtEntityGoal(this, LivingEntity.class, 4, 180f));

        this.goalSelector.add(9, new WanderAroundFarGoal(this, 0.32F, 0.01f)); //WanderGoal
        this.goalSelector.add(9, new MoaWanderAroundGoal(this, 0.320D, 230));
        //this.goalSelector.add(10, new MoaWanderAroundGoal(this, 0.400D, 290));
        this.goalSelector.add(10, new LookAroundGoal(this)); //LookGoal
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(12, new FollowParentGoal(this, 0.33D));

        super.initGoals();
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        if (!genes.isInitialized()) {
            genes.initMoa(this);
            setHealth(genes.getAttribute(MoaAttributes.MAX_HEALTH));
        }
        return super.initialize(world, difficulty, spawnReason, entityData);
    }

    @Override
    public void move(MovementType movement, Vec3d motion) {
        super.move(movement, motion);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(AIR_TICKS, 0);
        builder.add(CHEST, ItemStack.EMPTY);
    }
    
    /**
     * Gets the {@link ItemStack} of the chest on this Moa.
     *
     * @return The chest stack
     */
    public ItemStack getChest() {
        return dataTracker.get(CHEST);
    }
    
    /**
     * Checks if this Moa is wearing a chest.
     *
     * @return True if there is a chest
     */
    public boolean hasChest() {
        return !getChest().isEmpty();
    }
    
    /**
     * Sets a new chest for this Moa from the provided {@link ItemStack}.
     *
     * This will drop the items if the chest is removed.
     *
     * @param stack The new stack
     * @throws IllegalArgumentException If the stack was not a chest
     */
    public void setChest(ItemStack stack) {
        if (!stack.isEmpty() && (!(stack.getItem() instanceof BlockItem blockItem) || !(blockItem.getBlock() instanceof AbstractChestBlock<?>))) {
            throw new IllegalArgumentException("Can not set a Moa chest to be a non-chest or empty item stack!");
        }
        dataTracker.set(CHEST, stack);
        refreshChest(true);
    }
    
    /**
     * Refreshes the inventory of this Moa when the chest changes.
     *
     * @param scatterItems True if the inventory should be scattered
     */
    public void refreshChest(boolean scatterItems) {
        if (hasChest()) {
            if (inventory.size() != 20) {
                inventory = new SimpleInventory(20);
                inventory.addListener(this);
            }
        } else {
            if (!inventory.isEmpty()) {
                inventory.removeListener(this);
                if (scatterItems && !getWorld().isClient) {
                    ItemScatterer.spawn(getWorld(), this, inventory);
                }
                inventory.clear();
                inventory = DUMMY;
            }
        }
    }
    
    @Override
    protected void dropInventory() {
        super.dropInventory();
        if (hasChest()) {
            if (!getWorld().isClient) {
                dropStack(getChest());
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
                //Smoothing to randomly roll flap
                float baseWingRoll = MathHelper.sin(randFlapTimer / (wingFlapSpeed - randFlapSpeed)) * (0.67F + (randFlapSpeed / 8));
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

/*                if (flapCount != prevFlapCount) {
                    prevFlapCount = flapCount;
                    this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), ParadiseLostSoundEvents.ENTITY_MOA_GLIDING, SoundCategory.NEUTRAL, 0.3F, getRandomFloat(0.95f, 0.97f));
                }*/
            } else {
                //Smoothing to base
                float baseWingRoll = 1.39626F; //Idle position (Default was 1.39626)
                float lDif = -baseWingRoll - curWingRoll;
                if (Math.abs(lDif) > 0.005F) {
                    curWingRoll += lDif / 6;
                }
            }
        } else {
            if (flapCount > 0 && !isSaddled()) {
                //Smoothing to randomly yaw flap
                float baseWingYaw = MathHelper.sin(randFlapTimer / 1) * (0.42F + (randFlapSpeed / 12));
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

/*                if (flapCount != prevFlapCount) {
                    prevFlapCount = flapCount;
                    this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), ParadiseLostSoundEvents.ENTITY_MOA_GLIDING, SoundCategory.NEUTRAL, 0.3F, getRandomFloat(1.15f, 1.17f));
                }*/
            } else {
                //Base position when not flapping etc
                float baseWingYaw = 0.174533F; //Idle position (Default was 0.174533)
                float lDif = -baseWingYaw - curWingYaw;
                if (Math.abs(lDif) > 0.005F) {
                    curWingYaw += lDif / 6;
                }
            }
        }
    }

    public float getWingRoll() {
        if (flapCount <= 0 || isSaddled()) {
            if (dataTracker.get(AIR_TICKS) >= 4) {
                //Gliding flapping system
                curWingRoll = MathHelper.sin(age / wingFlapSpeed) * 0.73F + 0.1F;
                atWingBottom = false;  // Reset peak for landing
            } else {
                //Base position when not flapping etc, this sine is for "breating" animations
                float baseWingRoll = MathHelper.sin(age / idleFlapSpeed + (randFlapSpeed * 0.1f)) * 0.05F + 1.39626F; //Idle position (Default was 1.39626)
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

    //Moa Sound stuff
    int moaSoundCallCooldown = 200;
    private float soundChance = 0;
    private float songChance = 0;

    public void attemptMoaSound() {
        if (this.random.nextFloat() < 0.2f + this.soundChance) {
            if (this.random.nextFloat() > 0.05f + this.songChance || isBaby()) {
                //Small chirp
                this.moaSoundCallCooldown = (int) getRandomFloat(60, 150);
                this.songChance += getRandomFloat(0.04f, 0.1f);

                if (!isBaby()) {
                    this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), ParadiseLostSoundEvents.ENTITY_MOA_AMBIENT, SoundCategory.NEUTRAL, 0.4f, getRandomFloat(0.85f, 0.92f));
                } else {
                    this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), ParadiseLostSoundEvents.ENTITY_MOA_AMBIENT, SoundCategory.NEUTRAL, 0.35f, getRandomFloat(1f, 1.1f));
                }
            } else {
                //Play little song sometimes so it doesn't get annoying
                this.moaSoundCallCooldown = (int) getRandomFloat(60, 150);
                this.songChance = 0;
                this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), ParadiseLostSoundEvents.ENTITY_MOA_AMBIENT_SING, SoundCategory.NEUTRAL, 0.3f, getRandomFloat(0.98f, 1.02f));
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
            if (!this.getWorld().isClient) {
                this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), ParadiseLostSoundEvents.ENTITY_MOA_GLIDING, SoundCategory.NEUTRAL, 0.9F, getRandomFloat(0.9f, 0.97f));
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
        this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), ParadiseLostSoundEvents.ENTITY_MOA_HURT, SoundCategory.NEUTRAL, 0.2F, getRandomFloat(0.78f, 0.82f));
    }
    boolean shouldRoll;
    @Override
    public void tick() {
        isInAir = !isOnGround();

        if (!this.getWorld().isClient) {
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
            dataTracker.set(AIR_TICKS, dataTracker.get(AIR_TICKS) + 1);
            attemptMoaFlap(false);
        } else {
            dataTracker.set(AIR_TICKS, 0);
            if (randFlapTimer <= 0) {
                shouldRoll = getRandomFloat(0f, 1f) > 0.5f;
                randFlapSpeed = getRandomFloat(0f, 2f);
                flapCount = (int) getRandomFloat(2, 6);
                randFlapTimer = (int) getRandomFloat(150, 800); // Time For next flap
            } else {
                randFlapTimer--;
            }
        }



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
        if (getHealth() < getMaxHealth() && hunger > 65F && getWorld().getTime() % 20 == 0 && random.nextBoolean()) {
            heal(1);
            genes.setHunger(hunger - 0.5F);
        }

        if ((hunger < 15F && getWorld().getTime() % 10 == 0 && isSaddled())) {
            produceParticlesServer(ParticleTypes.ANGRY_VILLAGER, random.nextInt(3), 1, 0);
            if (hunger < 8F && hasPassengers()) {
                removeAllPassengers();
                playSound(ParadiseLostSoundEvents.ENTITY_MOA_DEATH, 0.15f, 1.5F + random.nextFloat() * 0.5F);
            }
        }
        if (getGenes().getRace().legendary() && getVelocity().lengthSquared() <= 0.02 && random.nextFloat() < 0.1F && random.nextBoolean()) {
            produceParticles((ParticleEffect) getGenes().getRace().particles(), 5, 0.25F);
        }

        this.fall();
        super.tick();
    }


    @Override
    @Environment(EnvType.CLIENT)
    protected Text getDefaultName() {
        return Text.translatable(getGenes().getRace().getTranslationKey(), "Moa");
    }

    @Override
    protected int computeFallDamage(float fallDistance, float damageMultiplier) {
        return 0;
    }

    public boolean isGliding() {
        return !isTouchingWater() && dataTracker.get(AIR_TICKS) > 4;
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isIn(ParadiseLostItemTags.MOA_BREEDABLES);
    }

    @Override
    public boolean canBeSaddled() {
        return getGenes().isTamed() && super.canBeSaddled();
    }

    @Override
    public boolean canBeControlledByRider() {
        return this.isSaddled();
    }

    @Nullable
    public LivingEntity getControllingPassenger() {
        Entity var3 = this.getFirstPassenger();
        if (var3 instanceof MobEntity mobEntity) {
            return mobEntity;
        } else {
            if (this.isSaddled()) {
                var3 = this.getFirstPassenger();
                if (var3 instanceof PlayerEntity) {
                    PlayerEntity playerEntity = (PlayerEntity) var3;
                    return playerEntity;
                }
            }
            return null;
        }
    }

    protected Vec3d getControlledMovementInput(PlayerEntity controllingPlayer, Vec3d movementInput) {
        float f = controllingPlayer.sidewaysSpeed * 0.5F;
        float g = controllingPlayer.forwardSpeed;
        if (g <= 0.0F) {
            g *= 0.25F;
        }
        return new Vec3d(f, 0.0, g);
    }

    float curGroundSpeed, curFlyingSpeed;
    float groundAcceleration = 0.004F;
    float flyingAcceleration = 0.04F;

    float genGroundSpeed, genGlidingSpeed, genGlidingDecay, genJumpHeight;

    private void calcGeneSpeeds() {
        genGroundSpeed = (getGenes().getAttribute(MoaAttributes.GROUND_SPEED) - 0.24f) * 0.072f + 0.1f; //From 0.1 to ~0.3
        genGlidingSpeed = (getGenes().getAttribute(MoaAttributes.GLIDING_SPEED) - 0.055f) * 0.52f + 0.37f; //From 0.37 to ~0.47
        genGlidingDecay = (getGenes().getAttribute(MoaAttributes.GLIDING_DECAY) * -0.23f) + 0.42f; //From 0.42 to ~0.19
        genJumpHeight = (getGenes().getAttribute(MoaAttributes.JUMPING_STRENGTH) - 0.15f) * 0.14f + 0.033f; //0.033 to ~0.047
        //Higher is better with all of these

        groundAcceleration = (getGenes().getAttribute(MoaAttributes.GROUND_SPEED) - 0.24f) * 0.05f + 0.01f; //0.01 to ~0.05
        flyingAcceleration = (getGenes().getAttribute(MoaAttributes.GLIDING_SPEED) * 0.1f); //0.005 to 0.025
    }

    private void calcAcceleration(PlayerEntity controllingPlayer) {
        float f = controllingPlayer.sidewaysSpeed * 0.5F;
        float g = controllingPlayer.forwardSpeed;
        calcGeneSpeeds();

        if (g == 0 && f == 0) {
            curGroundSpeed = Math.clamp(curGroundSpeed - 0.1f, genGroundSpeed * 0.07f, genGroundSpeed);
            curFlyingSpeed = Math.clamp(curFlyingSpeed - 0.05f, genGlidingSpeed * 0.1f, genGlidingSpeed);
        } else {
            curGroundSpeed = Math.clamp(curGroundSpeed + groundAcceleration, genGroundSpeed * 0.07f, genGroundSpeed);
            curFlyingSpeed = Math.clamp(curFlyingSpeed + flyingAcceleration + (Math.abs((float) getVelocity().y / 10)), genGlidingSpeed * 0.1f, genGlidingSpeed);
        }
    }

    @Override
    protected void tickControlled(PlayerEntity controllingPlayer, Vec3d movementInput) {
        if (this.isAlive()) {
            if (this.hasPassengers() && controllingPlayer != null && this.canBeControlledByRider()) {
                this.prevYaw = this.getYaw();
                this.setYaw(controllingPlayer.getYaw());
                this.setPitch(controllingPlayer.getPitch() * 0.5F);
                this.setRotation(this.getYaw(), this.getPitch());
                this.bodyYaw = this.getYaw();
                this.headYaw = this.bodyYaw;
                var movement = getControlledMovementInput(controllingPlayer, movementInput);

                calcAcceleration(controllingPlayer);
                if (this.jumpStrength > 0.0F && this.isOnGround() && !this.isInAir) {
                    //Calculate base jump velocity based on jump strength and multiplier
                    double jumpVelocity = 0.1 * this.jumpStrength * this.getJumpVelocityMultiplier();

                    //Add jump boost
                    if (this.hasStatusEffect(StatusEffects.JUMP_BOOST)) {
                        int boostLevel = this.getStatusEffect(StatusEffects.JUMP_BOOST).getAmplifier() + 1;
                        jumpVelocity += boostLevel * 0.1;
                    }

                    //Set new up velocity
                    Vec3d currentVelocity = this.getVelocity();
                    this.setVelocity(currentVelocity.x, jumpVelocity, currentVelocity.z);
                    //Forward jumping movement
                    if (movement.z > 0.0F) {
                        float adjVel = jumpStrength / 3F;
                        float i = MathHelper.sin(this.getYaw() * 0.017453292F);
                        float j = MathHelper.cos(this.getYaw() * 0.017453292F);
                        this.setVelocity(this.getVelocity().add(-0.3F * i * adjVel, 0.0D, 0.3F * j * adjVel));
                    }
                    this.jumpStrength = 0.0F;
                }

                //Reset jump when grounded
                if (jumpStrength <= 0.01F && isOnGround()) {
                    this.jumpStrength = 0.0F;
                    this.jumping = false;
                    isInAir = false;
                }

                if (this.isLogicalSideForUpdatingMovement()) {
                    super.travel(new Vec3d(movement.x, movement.y, movement.z));
                } else {
                    this.setVelocity(Vec3d.ZERO);
                }
                //System.out.println(getVelocity());
                this.updateLimbs(false);
            } else {
                super.travel(movementInput);
            }

            //Some sort of trail?
            if (getGenes().getRace().legendary() && getVelocity().lengthSquared() > 0.02 && random.nextFloat() < 0.55F) {
                produceParticles((ParticleEffect) getGenes().getRace().particles(), 3, 0.25F);
            }
        }
    }
    @Override
    protected void updateLimbs(float posDelta) {
        if (hasPassengers()) {
            float f = Math.min(posDelta * 2.0F, 0.5F);
            this.limbAnimator.updateLimbs(f, 0.4F);
        } else {
            float f = Math.min(posDelta * 4.0F, 3F);
            this.limbAnimator.updateLimbs(f, 0.5F);
        }

    }

    @Override
    public float getMountedMoveSpeed() {
        return getMovementSpeed();
    }

    @Override
    public float getMovementSpeed() {
        if (!hasPassengers()) {
            return isGliding() ? 0.1f : 0.08f;
        } else {
            return isGliding() ? curFlyingSpeed : curGroundSpeed;
        }
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (player.getStackInHand(hand).getItem() instanceof BloodstoneItem) {
            return ActionResult.PASS;
        }

        if (!getWorld().isClient()) {
            ItemStack heldStack = player.getStackInHand(hand);
            if (getGenes().isTamed()) {
                // Allow the player to open the GUI at any time
                if (player.isSneaking()) {
                    openInventory(player);
                    return ActionResult.SUCCESS;
                }
                
                // Short circuit to hopefully save a few cycles.
                if (heldStack.isEmpty()) {
                    return super.interactMob(player, hand);
                }
                
                var item = heldStack.getItem();
                if (heldStack.isIn(ConventionalItemTags.RAW_MEAT_FOODS)) {
                    feedMob(heldStack);
                    return ActionResult.success(getWorld().isClient());
                } else if (!hasChest() && item instanceof BlockItem blockItem && blockItem.getBlock() instanceof AbstractChestBlock) {
                    // Set a new chest, if there is none.
                    var chestStack = heldStack.copy();
                    chestStack.setCount(1);
                    if (!player.isCreative()) {
                        heldStack.decrement(1);
                    }
                    setChest(heldStack);
                    return ActionResult.success(getWorld().isClient);
                }
            } else {
                if (!heldStack.isIn(ParadiseLostItemTags.MOA_BREEDABLES) && heldStack.isIn(ParadiseLostItemTags.MOA_TEMPTABLES)) {
                    eat(player, hand, heldStack);
                    playSound(ParadiseLostSoundEvents.ENTITY_MOA_EAT, 1, 0.4F + random.nextFloat() / 3F);
                    produceParticlesServer(new ItemStackParticleEffect(ParticleTypes.ITEM, heldStack), 2 + random.nextInt(4), 7, 0);
                    //spawnConsumptionEffects(heldStack, 7 + random.nextInt(13));

                    var foodComponent = heldStack.getOrDefault(DataComponentTypes.FOOD, null);
                    if (random.nextFloat() < 0.04F * (foodComponent == null ? 2 : foodComponent.nutrition())) {
                        getGenes().tame(player.getUuid());
                        playSound(ParadiseLostSoundEvents.ENTITY_MOA_AMBIENT, 0.34f, 0.75F);
                        produceParticlesServer(ParticleTypes.HAPPY_VILLAGER, 2 + random.nextInt(4), 7, 0);
                        if (player instanceof ServerPlayerEntity) {
                            Criteria.TAME_ANIMAL.trigger((ServerPlayerEntity) player, this);
                        }
                    }

                    return ActionResult.CONSUME;
                }
            }
        }
        return super.interactMob(player, hand);
    }

    private void feedMob(ItemStack heldStack) {
        float hungerRestored = heldStack.get(DataComponentTypes.FOOD).nutrition() * 4;
        float satiation = getGenes().getHunger();
        float hunger = 100 - satiation;
        if (hunger > 1) {
            int consumption = Math.min((int) Math.ceil(hunger / hungerRestored), heldStack.getCount());
            spawnConsumptionEffects(heldStack, 10 + random.nextInt(consumption * 2 + 1));
            heldStack.decrement(consumption);
            getGenes().setHunger(satiation + (consumption * hungerRestored));
            playSound(ParadiseLostSoundEvents.ENTITY_MOA_EAT, 1.5F, 0.8F);
            produceParticles(ParticleTypes.HAPPY_VILLAGER);
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound compound) {
        super.writeCustomDataToNbt(compound);
        compound.putInt("airTicks", dataTracker.get(AIR_TICKS));
        compound.put("chest", dataTracker.get(CHEST).encodeAllowEmpty(this.getRegistryManager()));
        if (inventory != DUMMY) {
            compound.put("chestContents", inventory.toNbtList(this.getRegistryManager()));
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compound) {
        super.readCustomDataFromNbt(compound);
        dataTracker.set(AIR_TICKS, compound.getInt("airTicks"));
        dataTracker.set(CHEST, ItemStack.fromNbtOrEmpty(this.getRegistryManager(), compound.getCompound("chest")));
        refreshChest(false);
        if (inventory != DUMMY) {
            inventory.readNbtList(compound.getList("chestContents", NbtElement.COMPOUND_TYPE), this.getRegistryManager());
        }

        setMovementSpeed(genes.getAttribute(MoaAttributes.GROUND_SPEED));
        calcGeneSpeeds();
        moaSoundCallCooldown = 50 + random.nextInt(150);
        songChance = MathHelper.clamp(random.nextFloat(), 0f, 0.3f);
        randFlapTimer = getRandomFloat(60, 1000);
    }

    @Override
    public boolean shouldSpawnSprintingParticles() {
        return curGroundSpeed >= genGroundSpeed - 0.01f;
        //return Math.abs(getVelocity().multiply(1d, 0, 1d).length()) > 0.4f && !isTouchingWater() && !isGliding() && isBaby();
    }

    @Override
    public void breed(ServerWorld world, AnimalEntity other) {
        MoaGenes genes = getGenes();
        if (genes.getHunger() > 80F) {
            ItemStack egg = genes.getEggForBreeding(((MoaEntity) other).genes, world, getBlockPos());
            playSound(ParadiseLostSoundEvents.ENTITY_MOA_LAY_EGG, 0.8F, 1.5F);

            ItemScatterer.spawn(world, getX(), getY(), getZ(), egg);
            this.setBreedingAge(6000);
            other.setBreedingAge(6000);
            this.resetLoveTicks();
            other.resetLoveTicks();
            world.sendEntityStatus(this, (byte) 18);
            if (world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
                world.spawnEntity(new ExperienceOrbEntity(world, this.getX(), this.getY(), this.getZ(), this.getRandom().nextInt(16) + 4));
            }

            produceParticlesServer(ParticleTypes.HAPPY_VILLAGER, 6 + random.nextInt(7), 1, 0);
            ((MoaEntity) other).produceParticlesServer(ParticleTypes.HAPPY_VILLAGER, 6 + random.nextInt(7), 1, 0);
        }
    }

    @Override
    protected void playStepSound(BlockPos posIn, BlockState stateIn) {
        this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), ParadiseLostSoundEvents.ENTITY_MOA_STEP, SoundCategory.NEUTRAL, 0.12F, 1F);
    }

    public void fall() {
        calcGeneSpeeds();
        if (this.getVelocity().y < 0.0D && !this.isSneaking()) {
            this.setVelocity(this.getVelocity().multiply(1D, isGliding() ? genGlidingDecay : 1f, 1.0D));
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
    public PassiveEntity createChild(ServerWorld world, PassiveEntity matingAnimal) {
        if (!(matingAnimal instanceof MoaEntity matingMoa)) {
            return null;
        }
        
        var genesA = getGenes();
        var genesB = matingMoa.getGenes();
        
        var eggStack = genesA.getEggForBreeding(genesB, world, getBlockPos());
        var baby = ParadiseLostEntityTypes.MOA.create(world);
        if (baby == null) {
            return null;
        }
        var babyGenes = baby.getGenes();
        babyGenes.fromComponent(eggStack.get(ParadiseLostDataComponentTypes.MOA_GENES));
        return baby;
    }

    @Override
    protected void dropLoot(DamageSource source, boolean causedByPlayer) {
        super.dropLoot(source, causedByPlayer);
        dropStack(new ItemStack(ParadiseLostItems.MOA_MEAT, (int) Math.round(0.337 + random.nextFloat() * getGenes().getAttribute(MoaAttributes.DROP_MULTIPLIER))));
    }

    @Override
    public void setJumpStrength(int heldJumpStrength) {
        //
        if (this.isSaddled()) {
            if (heldJumpStrength <= 0.2f) {
                heldJumpStrength = 0;
            } else {
                if (heldJumpStrength >= 0.9f) {
                    jumpStrength = ((heldJumpStrength * 4) - 2.8f) * genJumpHeight;
                } else {
                    jumpStrength = ((heldJumpStrength * 4) - 3) * genJumpHeight;
                }
                //System.out.println(jumpStrength);
                //System.out.println(genJumpHeight);


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
    public void startJumping(int height) {
        if (!isGliding() && isOnGround()) {
            this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), ParadiseLostSoundEvents.ENTITY_MOA_GLIDING, SoundCategory.NEUTRAL, 0.25F, getRandomFloat(0.64f, 0.69f));
            this.jumping = true;
        } else {
            this.jumping = false;
        }
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
    public LivingEntity getOwner() {
        return Optional.ofNullable(getOwnerUuid()).map(getWorld()::getPlayerByUuid).orElse(null);
    }
    
    @Override
    public void onInventoryChanged(Inventory sender) {
        // TODO?
    }
    
    @Override
    public void openInventory(PlayerEntity player) {
        if (!getWorld().isClient && (!hasPassengers() || hasPassenger(player)) && getGenes().isTamed()) {
            player.openHandledScreen(new ExtendedScreenHandlerFactory() {
                @Override
                public Object getScreenOpeningData(ServerPlayerEntity player) {
                    return new MoaScreenHandler.MoaScreenData(MoaEntity.this.getId());
                }

                @Override
                public Text getDisplayName() {
                    return Text.translatable("container.paradise_lost.moa");
                }
    
                @Override
                public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
                    return new MoaScreenHandler(syncId, inv, inventory, MoaEntity.this);
                }
            });
        }
    }

    @Override
    protected Vec3d getPassengerAttachmentPos(Entity passenger, EntityDimensions dimensions, float scaleFactor) {
        return new Vec3d(0.0F, this.getPassengerAttachmentY(dimensions, scaleFactor), 0.0F);
    }

    protected float getPassengerAttachmentY(EntityDimensions dimensions, float scaleFactor) {
        return dimensions.height() + -0.75F * scaleFactor;
    }
    
    /**
     * Gets the current {@link Inventory} of this Moa, may be empty.
     *
     * @return The current inventory
     */
    public Inventory getInventory() {
        return inventory;
    }


    private class MoaEscapeDangerGoal extends EscapeDangerGoal {

        MoaEscapeDangerGoal(PathAwareEntity mob, double speed) {
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
            return 4.0D;
        }

        public boolean shouldResetPath() {
            return this.tryingTime % 100 == 0;
        }

        protected boolean isTargetPos(WorldView world, BlockPos pos) {
            if (world.getBlockEntity(pos) instanceof FoodBowlBlockEntity foodBowl) {
                ItemStack foodStack = foodBowl.getContainedItem();
                return foodStack.isIn(ConventionalItemTags.RAW_MEATS_FOODS);
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
                MoaEntity.this.playSound(ParadiseLostSoundEvents.ENTITY_MOA_DEATH, 0.5F, 2.0F);
            }



            super.tick();
        }

        protected void tryEat() {
            if (getWorld().getBlockEntity(targetPos) instanceof FoodBowlBlockEntity foodBowl) {
                ItemStack foodStack = foodBowl.getContainedItem();
                if (foodStack.isIn(ConventionalItemTags.RAW_MEATS_FOODS)) {
                    feedMob(foodStack);
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

        @Override
        public void start() {
            this.timer = 0;
            super.start();
        }
    }
}
