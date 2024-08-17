package net.id.paradiselost.entities.passive.moa;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.id.paradiselost.blocks.blockentity.FoodBowlBlockEntity;
import net.id.paradiselost.component.MoaGenes;
import net.id.paradiselost.entities.ParadiseLostEntityTypes;
import net.id.paradiselost.entities.util.SaddleMountEntity;
import net.id.paradiselost.items.ParadiseLostItems;
import net.id.paradiselost.items.tools.bloodstone.BloodstoneItem;
import net.id.paradiselost.screen.handler.MoaScreenHandler;
import net.id.paradiselost.tag.ParadiseLostItemTags;
import net.id.paradiselost.util.ParadiseLostSoundEvents;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.AbstractChestBlock;
import net.minecraft.block.BlockState;
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
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryChangedListener;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Ingredient;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
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
    private static final SimpleInventory DUMMY = new SimpleInventory(0);

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
        this.setStepHeight(1.0F);
        this.secsUntilEgg = this.getRandomEggTime();
        refreshChest(false);
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
        this.goalSelector.add(4, new TemptGoal(this, 1.0D, Ingredient.fromTag(ParadiseLostItemTags.MOA_TEMPTABLES), false));
        this.goalSelector.add(5, new FollowParentGoal(this, 1.1D));
        this.goalSelector.add(6, new WanderAroundFarGoal(this, 0.4F)); //WanderGoal
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
        dataTracker.startTracking(AIR_TICKS, 0);
        dataTracker.startTracking(CHEST, ItemStack.EMPTY);
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
            if (inventory.size() != 0) {
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
        this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), ParadiseLostSoundEvents.ENTITY_MOA_HURT, SoundCategory.NEUTRAL, 0.225F, MathHelper.clamp(this.random.nextFloat(), 0.5f, 0.7f) + MathHelper.clamp(this.random.nextFloat(), 0f, 0.15f));
    }

    @Override
    public void tick() {
        super.tick();

        isInAir = !isOnGround();

        if (isInAir) {
            dataTracker.set(AIR_TICKS, dataTracker.get(AIR_TICKS) + 1);
        } else {
            dataTracker.set(AIR_TICKS, 0);
        }

        if (age % 15 == 0) {
            if (isGliding()) {
                this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), ParadiseLostSoundEvents.ENTITY_MOA_GLIDING, SoundCategory.NEUTRAL, 4.5F, MathHelper.clamp(this.random.nextFloat(), 0.85f, 1.2f) + MathHelper.clamp(this.random.nextFloat(), 0f, 0.35f));
            } else if (random.nextFloat() < 0.057334F) {
                this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), ParadiseLostSoundEvents.ENTITY_MOA_AMBIENT, SoundCategory.NEUTRAL, 1.5F + random.nextFloat() * 2, MathHelper.clamp(this.random.nextFloat(), 0.55f, 0.7f) + MathHelper.clamp(this.random.nextFloat(), 0f, 0.25f));
            }
        }

        if (this.jumping) {
            this.setVelocity(this.getVelocity().add(0.0D, 0.05D, 0.0D));
        }

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
        if (getHealth() < getMaxHealth() && hunger > 65F && getWorld().getTime() % 20 == 0 && random.nextBoolean()) {
            heal(1);
            genes.setHunger(hunger - 0.5F);
        }
        if (hunger < 20F && getWorld().getTime() % 10 == 0) {
            produceParticlesServer(ParticleTypes.ANGRY_VILLAGER, random.nextInt(3), 1, 0);
            if (hunger < 10F && hasPassengers()) {
                removeAllPassengers();
                playSound(ParadiseLostSoundEvents.ENTITY_MOA_DEATH, 1, 1.5F + random.nextFloat() * 0.5F);
            }
        }
        if (getGenes().getRace().legendary() && getVelocity().lengthSquared() <= 0.02 && random.nextFloat() < 0.1F && random.nextBoolean()) {
            produceParticles((ParticleEffect) getGenes().getRace().particles(), 5, 0.25F);
        }

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
        return !isTouchingWater() && dataTracker.get(AIR_TICKS) > 20;
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
        return this.isSaddled();
    }

    @Override
    public void travel(Vec3d movementInput) {
        if (this.isAlive()) {
            LivingEntity livingEntity = (LivingEntity) this.getPrimaryPassenger();
            if (this.hasPassengers() && livingEntity != null && this.canBeControlledByRider()) {
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

                if (this.jumpStrength > 0.0F && !this.isInAir && this.isOnGround()) {
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

                if (jumpStrength <= 0.01F && isOnGround()) {
                    this.jumpStrength = 0.0F;
                    this.jumping = false;
                    isInAir = false;
                }

                //this.airStrafingSpeed = getFlyingSpeed(); TODO?
                if (this.isLogicalSideForUpdatingMovement()) {
                    this.setMovementSpeed(getMountedMoveSpeed());
                    super.travel(new Vec3d(f, movementInput.y, g));
                } else if (livingEntity instanceof PlayerEntity) {
                    this.setVelocity(Vec3d.ZERO);
                }

                this.updateLimbs(false);
            } else {
                //this.airStrafingSpeed = getFlyingSpeed(); TODO?
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
                if (item.isFood() && item.getFoodComponent().isMeat()) {
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
                if (heldStack.getItem() != ParadiseLostItems.ORANGE && heldStack.isIn(ParadiseLostItemTags.MOA_TEMPTABLES)) {
                    eat(player, hand, heldStack);
                    playSound(ParadiseLostSoundEvents.ENTITY_MOA_EAT, 1, 0.5F + random.nextFloat() / 3F);
                    produceParticlesServer(new ItemStackParticleEffect(ParticleTypes.ITEM, heldStack), 2 + random.nextInt(4), 7, 0);
                    //spawnConsumptionEffects(heldStack, 7 + random.nextInt(13));

                    if (random.nextFloat() < 0.04F * (heldStack.isFood() ? heldStack.getItem().getFoodComponent().getHunger() : 2)) {
                        getGenes().tame(player.getUuid());
                        playSound(ParadiseLostSoundEvents.ENTITY_MOA_AMBIENT, 2, 0.75F);
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
        float hungerRestored = heldStack.getItem().getFoodComponent().getHunger() * 4;
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
        compound.put("chest", dataTracker.get(CHEST).writeNbt(new NbtCompound()));
        if (inventory != DUMMY) {
            compound.put("chestContents", inventory.toNbtList());
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compound) {
        super.readCustomDataFromNbt(compound);
        dataTracker.set(AIR_TICKS, compound.getInt("airTicks"));
        dataTracker.set(CHEST, ItemStack.fromNbt(compound.getCompound("chest")));
        refreshChest(false);
        if (inventory != DUMMY) {
            inventory.readNbtList(compound.getList("chestContents", NbtElement.COMPOUND_TYPE));
        }
    }

    @Override
    public boolean shouldSpawnSprintingParticles() {
        return Math.abs(getVelocity().multiply(1, 0, 1).length()) > 0 && !isTouchingWater() && !isGliding();
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
        this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), ParadiseLostSoundEvents.ENTITY_MOA_STEP, SoundCategory.NEUTRAL, 0.15F, 1F);
    }

    public void fall() {
        if (this.getVelocity().y < 0.0D && !this.isSneaking()) {
            this.setVelocity(this.getVelocity().multiply(1.0D, isGliding() ? getGenes().getAttribute(MoaAttributes.GLIDING_DECAY) * 1.4 : 1D, 1.0D));
        }
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
        babyGenes.readFromNbt(eggStack.getOrCreateSubNbt("genes"));
        return baby;
    }

    @Override
    public Identifier getLootTableId() {
        return null;
    }

    @Override
    protected void dropLoot(DamageSource source, boolean causedByPlayer) {
        dropStack(new ItemStack(ParadiseLostItems.MOA_MEAT, (int) Math.round(0.337 + random.nextFloat() * getGenes().getAttribute(MoaAttributes.DROP_MULTIPLIER))));
        if (random.nextBoolean()) {
            dropStack(new ItemStack(Items.FEATHER, (int) (Math.round(0.337 + random.nextFloat() * getGenes().getAttribute(MoaAttributes.DROP_MULTIPLIER)) / 2)));
        }
    }

    @Override
    public void setJumpStrength(int strength) {
        if (this.isSaddled()) {
            if (strength < 0) {
                strength = 0;
            } else {
                this.jumping = true;
            }
            jumpStrength = strength * getGenes().getAttribute(MoaAttributes.JUMPING_STRENGTH) * 0.95F;
        }
    }

    @Override
    public boolean canJump() {
        return this.isSaddled();
    }

    @Override
    public void startJumping(int height) {
        this.jumping = true;
        this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), ParadiseLostSoundEvents.ENTITY_MOA_GLIDING, SoundCategory.NEUTRAL, 7.5F, MathHelper.clamp(this.random.nextFloat(), 0.55f, 0.8f));
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

    @Override
    public EntityView method_48926() {
        return null;
    }

    @Nullable
    @Override
    public LivingEntity getOwner() {
        return Optional.ofNullable(getOwnerUuid()).map(getWorld()::getPlayerByUuid).orElse(null);
    }
    
    @Override
    public void onInventoryChanged(Inventory sender) {
        //TODO
    }
    
    @Override
    public void openInventory(PlayerEntity player) {
        if (!getWorld().isClient && (!hasPassengers() || hasPassenger(player)) && getGenes().isTamed()) {
            player.openHandledScreen(new ExtendedScreenHandlerFactory() {
                @Override
                public Text getDisplayName() {
                    return Text.translatable("container.paradise_lost.moa");
                }
    
                @Override
                public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
                    return new MoaScreenHandler(syncId, inv, inventory, MoaEntity.this);
                }
    
                @Override
                public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
                    buf.writeVarInt(MoaEntity.this.getId());
                }
            });
        }
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
            return 2.0D;
        }

        public boolean shouldResetPath() {
            return this.tryingTime % 100 == 0;
        }

        protected boolean isTargetPos(WorldView world, BlockPos pos) {
            if (world.getBlockEntity(pos) instanceof FoodBowlBlockEntity foodBowl) {
                ItemStack foodStack = foodBowl.getContainedItem();
                return foodStack.isFood() && foodStack.getItem().getFoodComponent().isMeat();
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
                if (foodStack.isFood() && foodStack.getItem().getFoodComponent().isMeat()) {
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
