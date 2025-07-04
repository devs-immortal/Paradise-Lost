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
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
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
    private static final TrackedData<Integer> AIR_TICKS = DataTracker.registerData(MoaEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<ItemStack> CHEST = DataTracker.registerData(MoaEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);
    private static final float WING_FLAP_SPEED = 2.5f;
    private static final float IDLE_FLAP_SPEED = 12;

    private final MoaSounds sounds;

    public boolean isInAir;

    protected int secsUntilEgg;
    private boolean shouldRoll;
    private float currentLegPitch;
    private float currentWingRoll;
    private float currentWingYaw;
    private float currentGroundSpeed;
    private float currentFlyingSpeed;
    private float jumpStrength;
    private MoaGenes genes;

    @NotNull
    private SimpleInventory inventory = DUMMY;

    public MoaEntity(EntityType<? extends MoaEntity> entityType, World world) {
        super(entityType, world);
        secsUntilEgg = getRandomEggTime();
        sounds = new MoaSounds(this);
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

        goalSelector.add(0, new MoaEscapeDangerGoal(this, 0.9f));

        goalSelector.add(1, new EatFromBowlGoal(0.4, 24, 16));
        goalSelector.add(1, new AnimalMateGoal(this, 0.25F));
        goalSelector.add(2, new TemptGoal(this, 0.7D, Ingredient.fromTag(ParadiseLostItemTags.MOA_TEMPTABLES), false));


        goalSelector.add(7, new LookAtEntityGoal(this, ParrotEntity.class, 18F, 100f));
        goalSelector.add(7, new StopAndLookAtEntityGoal(this, ParrotEntity.class, 25, 120f));

        goalSelector.add(8, new LookAtEntityGoal(this, LivingEntity.class, 10F, 150f));
        goalSelector.add(8, new StopAndLookAtEntityGoal(this, LivingEntity.class, 4, 180f));

        goalSelector.add(9, new WanderAroundFarGoal(this, 0.32F, 0.01f)); // WanderGoal
        goalSelector.add(9, new MoaWanderAroundGoal(this, 0.320D, 230));
        // goalSelector.add(10, new MoaWanderAroundGoal(this, 0.400D, 290));
        goalSelector.add(10, new LookAroundGoal(this)); // LookGoal
        goalSelector.add(1, new SwimGoal(this));
        goalSelector.add(12, new FollowParentGoal(this, 0.33D));

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
     * <p>
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

    private float randFlapSpeed;
    private float randFlapTimer = 200f;
    private float prevWingRoll;
    private float prevWingYaw;
    private int flapCount = 0;
    private int prevFlapCount = 0;
    private boolean atWingBottom;
    private boolean atWingYawBottom;

    void pickRollOrYawFlapping(boolean theFinalPickForRollingOrYawingTheWingsOfTheMoa) {
        prevFlapCount = flapCount;
        if (theFinalPickForRollingOrYawingTheWingsOfTheMoa) {
            if (flapCount > 0 && !isSaddled()) {
                // Smoothing to randomly roll flap
                float baseWingRoll = MathHelper.sin(randFlapTimer / (WING_FLAP_SPEED - randFlapSpeed)) * (0.67F + (randFlapSpeed / 8));
                float lDif = -baseWingRoll - currentWingRoll;
                if (Math.abs(lDif) > 0.005F) {
                    currentWingRoll += lDif / 4;
                }

                if (currentWingRoll < prevWingRoll && !atWingBottom) {
                    atWingBottom = true;
                    flapCount--;
                } else if (currentWingRoll > prevWingRoll) {

                    atWingBottom = false;
                }
                prevWingRoll = currentWingRoll;

                // if (flapCount != prevFlapCount) {
                //     prevFlapCount = flapCount;
                //     sounds.playFlapSound();
                // }
            } else {
                // Smoothing to base
                float baseWingRoll = 1.39626F; // Idle position (Default was 1.39626)
                float lDif = -baseWingRoll - currentWingRoll;
                if (Math.abs(lDif) > 0.005F) {
                    currentWingRoll += lDif / 6;
                }
            }
        } else {
            if (flapCount > 0 && !isSaddled()) {
                // Smoothing to randomly yaw flap
                float baseWingYaw = MathHelper.sin(randFlapTimer / 1) * (0.42F + (randFlapSpeed / 12));
                float lDif = -baseWingYaw - currentWingYaw;
                if (Math.abs(lDif) > 0.005F) {
                    currentWingYaw += lDif / 3;
                }

                if (currentWingYaw < prevWingYaw && !atWingYawBottom) {
                    atWingYawBottom = true;
                    flapCount--;
                } else if (currentWingYaw > prevWingYaw) {

                    atWingYawBottom = false;
                }
                prevWingYaw = currentWingYaw;

                // if (flapCount != prevFlapCount) {
                //     prevFlapCount = flapCount;
                //     sounds.playFlapSound();
                // }
            } else {
                // Base position when not flapping etc
                float baseWingYaw = 0.174533F; // Idle position (Default was 0.174533)
                float lDif = -baseWingYaw - currentWingYaw;
                if (Math.abs(lDif) > 0.005F) {
                    currentWingYaw += lDif / 6;
                }
            }
        }
    }

    public float getWingRoll() {
        if (flapCount <= 0 || isSaddled()) {
            if (dataTracker.get(AIR_TICKS) >= 4) {
                // Gliding flapping system
                currentWingRoll = MathHelper.sin(age / WING_FLAP_SPEED) * 0.73F + 0.1F;
                atWingBottom = false;  // Reset peak for landing
            } else {
                // Base position when not flapping etc, this sine is for "breating" animations
                float baseWingRoll = MathHelper.sin(age / IDLE_FLAP_SPEED + (randFlapSpeed * 0.1f)) * 0.05F + 1.39626F; // Idle position (Default was 1.39626)
                float lDif = -baseWingRoll - currentWingRoll;
                if (Math.abs(lDif) > 0.0005F) {
                    currentWingRoll += lDif / 6;
                }
            }
        }
        return currentWingRoll;
    }

    public float getWingYaw() {
        if (flapCount <= 0 || isSaddled()) {
            float baseWingYaw = isGliding() ? 0.95626F : 0.174533F;
            float lDif = -baseWingYaw - currentWingYaw;
            if (Math.abs(lDif) > 0.005F) {
                currentWingYaw += lDif / 12.75f;
            }
        }
        return currentWingYaw;
    }

    public float getLegPitch() {
        float baseLegPitch = isGliding() ? -1.5708F : 0.0174533F;
        float lDif = -baseLegPitch - currentLegPitch;
        if (Math.abs(lDif) > 0.005F) {
            currentLegPitch += lDif / 6;
        }
        return currentLegPitch;
    }

    public float getRandomFloat(float from, float to) {
        return from + random.nextFloat() * (to - from);
    }

    private boolean canFlap = true;

    public void attemptMoaFlap(boolean bypassFlapCheck) {
        if (getWingRoll() > 0.8 && canFlap || bypassFlapCheck) {
            sounds.playFlapSound();
            canFlap = false;
        } else if (getWingRoll() < -0.3f) {
            canFlap = true;
        }
    }

    public int getRandomEggTime() {
        return 775 + random.nextInt(50);
    }

    @Override
    protected void playHurtSound(DamageSource source) {
        sounds.playHurtSound();
    }

    @Override
    public void tick() {
        sounds.tick();

        isInAir = !isOnGround();

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
        if (genes.isTamed() && random.nextBoolean()) {
            genes.setHunger(hunger - (1F / 12000F));
        }

        if (getHealth() < getMaxHealth() && hunger > 65F && getWorld().getTime() % 20 == 0 && random.nextBoolean()) {
            heal(1);
            genes.setHunger(hunger - 0.5F);
        }

        if ((hunger < 15F && getWorld().getTime() % 10 == 0 && isSaddled())) {
            produceParticlesServer(ParticleTypes.ANGRY_VILLAGER, random.nextInt(3), 1, 0);
            if (hunger < 8F && hasPassengers()) {
                removeAllPassengers();
                sounds.playDeathSound(0.15f, getRandomFloat(1.5F, 2.0F));
            }
        }
        if (getGenes().getRace().legendary() && getVelocity().lengthSquared() <= 0.02 && random.nextFloat() < 0.1F && random.nextBoolean()) {
            produceParticles((ParticleEffect) getGenes().getRace().particles(), 5, 0.25F);
        }

        fall();
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
        return stack.getItem() == ParadiseLostItems.ORANGE;
    }

    @Override
    public boolean canBeSaddled() {
        return getGenes().isTamed() && super.canBeSaddled();
    }

    @Override
    public boolean canBeControlledByRider() {
        return isSaddled();
    }

    @Override
    @Nullable
    public LivingEntity getControllingPassenger() {
        Entity passenger = getFirstPassenger();
        if (passenger instanceof MobEntity mobEntity) {
            return mobEntity;
        } else {
            if (isSaddled()) {
                passenger = getFirstPassenger();
                if (passenger instanceof PlayerEntity playerEntity) {
                    return playerEntity;
                }
            }
            return null;
        }
    }

    @Override
    protected Vec3d getControlledMovementInput(PlayerEntity controllingPlayer, Vec3d movementInput) {
        float f = controllingPlayer.sidewaysSpeed * 0.5F;
        float g = controllingPlayer.forwardSpeed;
        if (g <= 0.0F) {
            g *= 0.25F;
        }
        return new Vec3d(f, 0.0, g);
    }

    private record GeneSpeeds(float groundSpeed, float glidingSpeed, float glidingDecay, float jumpHeight,
                              float groundAcceleration, float flyingAcceleration) {
    }

    private GeneSpeeds calcGeneSpeeds() {
        float genGroundSpeed = (getGenes().getAttribute(MoaAttributes.GROUND_SPEED) - 0.24f) * 0.072f + 0.1f; // From 0.1 to ~0.3
        float genGlidingSpeed = (getGenes().getAttribute(MoaAttributes.GLIDING_SPEED) - 0.055f) * 0.52f + 0.37f; // From 0.37 to ~0.47
        float genGlidingDecay = (getGenes().getAttribute(MoaAttributes.GLIDING_DECAY) * -0.23f) + 0.42f; // From 0.42 to ~0.19
        float genJumpHeight = (getGenes().getAttribute(MoaAttributes.JUMPING_STRENGTH) - 0.15f) * 0.14f + 0.033f; //0.033 to ~0.047
        // Higher is better with all of these

        float genGroundAcceleration = (getGenes().getAttribute(MoaAttributes.GROUND_SPEED) - 0.24f) * 0.05f + 0.01f; //0.01 to ~0.05
        float genFlyingAcceleration = (getGenes().getAttribute(MoaAttributes.GLIDING_SPEED) * 0.1f); //0.005 to 0.025

        return new GeneSpeeds(genGroundSpeed, genGlidingSpeed, genGlidingDecay, genJumpHeight, genGroundAcceleration, genFlyingAcceleration);
    }

    private void calcAcceleration(PlayerEntity controllingPlayer) {
        float f = controllingPlayer.sidewaysSpeed * 0.5F;
        float g = controllingPlayer.forwardSpeed;
        GeneSpeeds geneSpeeds = calcGeneSpeeds();

        if (g == 0 && f == 0) {
            currentGroundSpeed = Math.clamp(currentGroundSpeed - 0.1f, geneSpeeds.groundSpeed() * 0.07f, geneSpeeds.groundSpeed());
            currentFlyingSpeed = Math.clamp(currentFlyingSpeed - 0.05f, geneSpeeds.glidingSpeed() * 0.1f, geneSpeeds.glidingSpeed());
        } else {
            currentGroundSpeed = Math.clamp(currentGroundSpeed + geneSpeeds.groundAcceleration(), geneSpeeds.groundSpeed() * 0.07f, geneSpeeds.groundSpeed());
            currentFlyingSpeed = Math.clamp(currentFlyingSpeed + geneSpeeds.flyingAcceleration() + (Math.abs((float) getVelocity().y / 10)), geneSpeeds.glidingSpeed() * 0.1f, geneSpeeds.glidingSpeed());
        }
    }

    @Override
    protected void tickControlled(PlayerEntity controllingPlayer, Vec3d movementInput) {
        if (isAlive()) {
            if (hasPassengers() && controllingPlayer != null && canBeControlledByRider()) {
                prevYaw = getYaw();
                setYaw(controllingPlayer.getYaw());
                setPitch(controllingPlayer.getPitch() * 0.5F);
                setRotation(getYaw(), getPitch());
                bodyYaw = getYaw();
                headYaw = bodyYaw;
                var movement = getControlledMovementInput(controllingPlayer, movementInput);

                calcAcceleration(controllingPlayer);
                if (jumpStrength > 0.0F && isOnGround() && !isInAir) {
                    // Calculate base jump velocity based on jump strength and multiplier
                    double jumpVelocity = 0.1 * jumpStrength * getJumpVelocityMultiplier();

                    // Add jump boost
                    if (hasStatusEffect(StatusEffects.JUMP_BOOST)) {
                        int boostLevel = getStatusEffect(StatusEffects.JUMP_BOOST).getAmplifier() + 1;
                        jumpVelocity += boostLevel * 0.1;
                    }

                    // Set new up velocity
                    Vec3d currentVelocity = getVelocity();
                    setVelocity(currentVelocity.x, jumpVelocity, currentVelocity.z);
                    // Forward jumping movement
                    if (movement.z > 0.0F) {
                        float adjVel = jumpStrength / 3F;
                        float i = MathHelper.sin(getYaw() * 0.017453292F);
                        float j = MathHelper.cos(getYaw() * 0.017453292F);
                        setVelocity(getVelocity().add(-0.3F * i * adjVel, 0.0D, 0.3F * j * adjVel));
                    }
                    jumpStrength = 0.0F;
                }

                // Reset jump when grounded
                if (jumpStrength <= 0.01F && isOnGround()) {
                    jumpStrength = 0.0F;
                    jumping = false;
                    isInAir = false;
                }

                if (isLogicalSideForUpdatingMovement()) {
                    super.travel(new Vec3d(movement.x, movement.y, movement.z));
                } else {
                    setVelocity(Vec3d.ZERO);
                }
                updateLimbs(false);
            } else {
                super.travel(movementInput);
            }

            // Some sort of trail?
            if (getGenes().getRace().legendary() && getVelocity().lengthSquared() > 0.02 && random.nextFloat() < 0.55F) {
                produceParticles((ParticleEffect) getGenes().getRace().particles(), 3, 0.25F);
            }
        }
    }

    @Override
    protected void updateLimbs(float posDelta) {
        if (hasPassengers()) {
            float f = Math.min(posDelta * 2.0F, 0.5F);
            limbAnimator.updateLimbs(f, 0.4F);
        } else {
            float f = Math.min(posDelta * 4.0F, 3F);
            limbAnimator.updateLimbs(f, 0.5F);
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
        }
        return isGliding() ? currentFlyingSpeed : currentGroundSpeed;
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (player.getStackInHand(hand).getItem() instanceof BloodstoneItem) {
            return ActionResult.PASS;
        }

        if (!getWorld().isClient) {
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
                    return ActionResult.success(getWorld().isClient);
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
                if (heldStack.getItem() != ParadiseLostItems.ORANGE && heldStack.isIn(ParadiseLostItemTags.MOA_TEMPTABLES)) {
                    eat(player, hand, heldStack);
                    sounds.playEatSound(1F, getRandomFloat(0.4F, 0.75F));
                    produceParticlesServer(new ItemStackParticleEffect(ParticleTypes.ITEM, heldStack), 2 + random.nextInt(4), 7, 0);
                    // spawnConsumptionEffects(heldStack, 7 + random.nextInt(13));

                    var foodComponent = heldStack.getOrDefault(DataComponentTypes.FOOD, null);
                    if (random.nextFloat() < 0.04F * (foodComponent == null ? 2 : foodComponent.nutrition())) {
                        getGenes().tame(player.getUuid());
                        sounds.playAmbientSound(0.34f, 0.75F);
                        produceParticlesServer(ParticleTypes.HAPPY_VILLAGER, 2 + random.nextInt(4), 7, 0);
                        if (player instanceof ServerPlayerEntity serverPlayer) {
                            Criteria.TAME_ANIMAL.trigger(serverPlayer, this);
                        }
                    }

                    return ActionResult.CONSUME;
                }
            }
        }
        return super.interactMob(player, hand);
    }

    private void feedMob(ItemStack heldStack) {
        float hungerRestored = heldStack.get(DataComponentTypes.FOOD).nutrition() * 4F;
        float satiation = getGenes().getHunger();
        float hunger = 100 - satiation;
        if (hunger > 1) {
            int consumption = Math.min((int) Math.ceil(hunger / hungerRestored), heldStack.getCount());
            spawnConsumptionEffects(heldStack, 10 + random.nextInt(consumption * 2 + 1));
            heldStack.decrement(consumption);
            getGenes().setHunger(satiation + (consumption * hungerRestored));
            sounds.playEatSound(1.5F, 0.8F);
            produceParticles(ParticleTypes.HAPPY_VILLAGER);
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound compound) {
        super.writeCustomDataToNbt(compound);
        compound.putInt("airTicks", dataTracker.get(AIR_TICKS));
        compound.put("chest", dataTracker.get(CHEST).encodeAllowEmpty(getRegistryManager()));
        if (inventory != DUMMY) {
            compound.put("chestContents", inventory.toNbtList(getRegistryManager()));
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compound) {
        super.readCustomDataFromNbt(compound);
        dataTracker.set(AIR_TICKS, compound.getInt("airTicks"));
        dataTracker.set(CHEST, ItemStack.fromNbtOrEmpty(getRegistryManager(), compound.getCompound("chest")));
        refreshChest(false);
        if (inventory != DUMMY) {
            inventory.readNbtList(compound.getList("chestContents", NbtElement.COMPOUND_TYPE), getRegistryManager());
        }

        setMovementSpeed(genes.getAttribute(MoaAttributes.GROUND_SPEED));
        sounds.setSoundCallCooldown(50 + random.nextInt(150));
        sounds.setSongChance(MathHelper.clamp(random.nextFloat(), 0f, 0.3f));
        randFlapTimer = getRandomFloat(60, 1000);
    }

    @Override
    public boolean shouldSpawnSprintingParticles() {
        GeneSpeeds geneSpeeds = calcGeneSpeeds();
        return currentGroundSpeed >= geneSpeeds.groundSpeed() - 0.01f;
    }

    @Override
    public void breed(ServerWorld world, AnimalEntity other) {
        MoaGenes genes = getGenes();
        if (genes.getHunger() > 80F && other instanceof MoaEntity moa) {
            ItemStack egg = genes.getEggForBreeding(moa.genes, world, getBlockPos());
            sounds.playLayingEggSound();

            ItemScatterer.spawn(world, getX(), getY(), getZ(), egg);

            setBreedingAge(6000);
            moa.setBreedingAge(6000);

            resetLoveTicks();
            moa.resetLoveTicks();

            world.sendEntityStatus(this, (byte) 18);

            if (world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
                world.spawnEntity(new ExperienceOrbEntity(world, getX(), getY(), getZ(), getRandom().nextInt(16) + 4));
            }

            produceParticlesServer(ParticleTypes.HAPPY_VILLAGER, 6 + random.nextInt(7), 1, 0);
            moa.produceParticlesServer(ParticleTypes.HAPPY_VILLAGER, 6 + random.nextInt(7), 1, 0);
        }
    }

    @Override
    protected void playStepSound(BlockPos posIn, BlockState stateIn) {
        sounds.playStepSound();
    }

    public void fall() {
        GeneSpeeds geneSpeeds = calcGeneSpeeds();
        if (getVelocity().y < 0.0D && !isSneaking()) {
            setVelocity(getVelocity().multiply(1D, isGliding() ? geneSpeeds.glidingDecay() : 1f, 1.0D));
        }
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
        if (isSaddled()) {
            if (heldJumpStrength <= 0.2f) {
                heldJumpStrength = 0;
            } else {
                GeneSpeeds geneSpeeds = calcGeneSpeeds();
                if (heldJumpStrength >= 0.9f) {
                    jumpStrength = ((heldJumpStrength * 4) - 2.8f) * geneSpeeds.jumpHeight();
                } else {
                    jumpStrength = ((heldJumpStrength * 4) - 3) * geneSpeeds.jumpHeight();
                }
                jumping = true;
            }
        } else {
            heldJumpStrength = 0;
            jumping = false;
        }
    }

    @Override
    public boolean canJump() {
        return isSaddled();
    }

    @Override
    public void startJumping(int height) {
        if (!isGliding() && isOnGround()) {
            sounds.playJumpingSound();
            jumping = true;
        } else {
            jumping = false;
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
        // TODO
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
        return new Vec3d(0.0F, getPassengerAttachmentY(dimensions, scaleFactor), 0.0F);
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

        @Override
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

        @Override
        public void tick() {
            if (hasReached()) {
                if (timer >= 20) {
                    tryEat();
                } else {
                    ++timer;
                }
            } else if (!hasReached() && random.nextFloat() < 0.025F) {
                sounds.playDeathSound(0.5F, 2.0F);
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
