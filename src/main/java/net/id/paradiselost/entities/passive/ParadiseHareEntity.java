package net.id.paradiselost.entities.passive;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.id.paradiselost.entities.ParadiseLostEntityExtensions;
import net.id.paradiselost.entities.ParadiseLostEntityTypes;
import net.id.paradiselost.items.ParadiseLostItems;
import net.id.paradiselost.util.ParadiseLostSoundEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SweetBerryBushBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class ParadiseHareEntity extends ParadiseLostAnimalEntity {

    public static final TrackedData<Byte> PUFF = DataTracker.registerData(ParadiseHareEntity.class, TrackedDataHandlerRegistry.BYTE);
    public float floof;

    public ParadiseHareEntity(EntityType<? extends ParadiseHareEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createParadiseHareAttributes() {
        return createMobAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 5.0D);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.25D));
        this.goalSelector.add(2, new WanderAroundFarGoal(this, 1.0D, 20));
        this.goalSelector.add(2, new WanderAroundGoal(this, 1.0D, 15));
        this.goalSelector.add(3, new EatBlueberriesGoal(0.9D, 40, 8));
        this.goalSelector.add(4, new AnimalMateGoal(this, 1.0D));
        this.goalSelector.add(5, new TemptGoal(this, 1.15D, Ingredient.ofItems(ParadiseLostItems.BLACKCURRANT), false));
        this.goalSelector.add(6, new LookAroundGoal(this));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 4.0F, 32));
        //this.goalSelector.add(6, new EntityAIBunnyHop(this));
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(PUFF, (byte) 0);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public boolean shouldRender(double par1) {
        return true;
    }

    @Override
    public double getHeightOffset() {
        return 0.4D;
    }

    @Override
    public void playSpawnEffects() {
        if (this.world.isClient) {
            for (int i = 0; i < 5; ++i) {
                double double_1 = this.random.nextGaussian() * 0.02D;
                double double_2 = this.random.nextGaussian() * 0.02D;
                double double_3 = this.random.nextGaussian() * 0.02D;

                this.world.addParticle(ParticleTypes.POOF, this.getX() + (double) (this.random.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth() - double_1 * 10.0D, this.getY() + (double) (this.random.nextFloat() * this.getHeight()) - double_2 * 10.0D, this.getZ() + (double) (this.random.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth() - double_3 * 10.0D, double_1, double_2, double_3);
            }
        } else {
            this.world.sendEntityStatus(this, (byte) 20);
        }
    }

    //@Override public boolean canRiderInteract() { return true; }

    public int getPuffiness() {
        return (int) this.dataTracker.get(PUFF);
    }

    public void setPuffiness(int i) {
        this.dataTracker.set(PUFF, (byte) i);
    }

    @Override
    public void tick() {
        super.tick();
        int puff = getPuffiness();
        if (puff > 0 && world.getTime() % 4 == 0) {
            Vec3d pos = getPos();
            world.addParticle(ParticleTypes.CLOUD, pos.x, pos.y + 0.2, pos.z, 0, 0, 0);
        } else if (isOnGround() && puff > 0) {
            setPuffiness(0);
        }

        if (random.nextFloat() <= 0.03F) {
            playSound(ParadiseLostSoundEvents.ENTITY_PARADISE_HARE_SNIFF, 1.0F, 2.0F);
        }

        if (this.hasVehicle() && (this.getVehicle().isSneaking() || this.getVehicle().getVelocity().y < -0.7)) {
            ((ParadiseLostEntityExtensions) this.getVehicle()).setParadiseHareFallen(true);
            this.dismountVehicle();
        }
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        if (this.isOnGround() && ((getVelocity().x > 0.025 || getVelocity().z > 0.025) && random.nextInt(4) == 0)) {
            jump();
        }
        // Slows down ParadiseHare while falling
        if (!this.isOnGround() && getVelocity().y < 0.0D) {
            this.setVelocity(getVelocity().multiply(1.0D, 0.65D, 1.0D));
        }
    }

    @Override
    protected float getJumpVelocity() {
        if (!this.horizontalCollision && (!this.moveControl.isMoving() || !(this.moveControl.getTargetY() > this.getY() + 0.5D))) {
            Path path = this.navigation.getCurrentPath();
            if (path != null && !path.isFinished()) {
                Vec3d vec3d = path.getNodePosition(this);
                if (vec3d.y > this.getY() + 0.5D) {
                    return 0.45F;
                }
            }
            return this.moveControl.getSpeed() <= 0.6D ? 0.3F : 0.4F;
        } else {
            return 0.45F;
        }
    }

    @Override
    protected void jump() {
        setPuffiness(1);
        Vec3d pos = getPos();
        for (int i = 0; i < 4; i++) {
            world.addParticle(ParticleTypes.CLOUD, pos.x + (random.nextGaussian() * 0.2), pos.y + (random.nextGaussian() * 0.2), pos.z + (random.nextGaussian() * 0.2), 0, 0, 0);
        }
        world.playSoundFromEntity(null, this, ParadiseLostSoundEvents.ENTITY_PARADISE_HARE_JUMP, SoundCategory.NEUTRAL, 1, 1);
        super.jump();
    }

    @Override
    public boolean shouldSpawnSprintingParticles() {
        return false;
    }

    @Override
    public void move(MovementType type, Vec3d movement) {
        super.move(type, isOnGround() ? movement : movement.multiply(3.5, (movement.y < 0 && getPuffiness() > 0) ? 0.15 : 1, 3.5));
    }

    @Override
    public boolean handleFallDamage(float distance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        if (!stack.isEmpty()) {
            return super.interactMob(player, hand);
        } else {
//            this.world.playSound(this.getX(), this.getY(), this.getZ(), ParadiseLostSounds.PARADISE_HARE_LIFT, SoundCategory.NEUTRAL, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F, false);

            if (getPrimaryPassenger() != null) {
                stopRiding();
            } else {
                startRiding(player);
            }

            return ActionResult.SUCCESS;
        }
    }

    @Override
    public boolean damage(DamageSource source, float damage) {
        return (this.getPrimaryPassenger() == null || source.getAttacker() != this.getPrimaryPassenger()) && super.damage(source, damage);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return ParadiseLostSoundEvents.ENTITY_PARADISE_HARE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ParadiseLostSoundEvents.ENTITY_PARADISE_HARE_DEATH;
    }

    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity mate) {
        return ParadiseLostEntityTypes.PARADISE_HARE.create(world);
    }

    public class EatBlueberriesGoal extends MoveToTargetPosGoal {
        protected int timer;

        public EatBlueberriesGoal(double speed, int range, int maxYDifference) {
            super(ParadiseHareEntity.this, speed, range, maxYDifference);
        }

        public double getDesiredSquaredDistanceToTarget() {
            return 2.0D;
        }

        @Override
        public boolean shouldResetPath() {
            return this.tryingTime % 100 == 0;
        }

        @Override
        protected boolean isTargetPos(WorldView world, BlockPos pos) {
            BlockState blockState = world.getBlockState(pos);
            return blockState.isOf(ParadiseLostBlocks.BLACKCURRANT_BUSH) && blockState.get(SweetBerryBushBlock.AGE) >= 3;
        }

        @Override
        public void tick() {
            if (this.hasReached()) {
                if (this.timer >= 40) {
                    this.eatSweetBerry();
                } else {
                    ++this.timer;
                }
            } else if (!this.hasReached() && ParadiseHareEntity.this.random.nextFloat() < 0.05F) {
                ParadiseHareEntity.this.playSound(ParadiseLostSoundEvents.ENTITY_PARADISE_HARE_SNIFF, 1.0F, 2.0F);
            }
            super.tick();
        }

        protected void eatSweetBerry() {
            BlockState blockState = ParadiseHareEntity.this.world.getBlockState(this.targetPos);
            if (blockState.isOf(ParadiseLostBlocks.BLACKCURRANT_BUSH) && blockState.get(SweetBerryBushBlock.AGE) == 3) {
                ParadiseHareEntity.this.setLoveTicks(40);
                ParadiseHareEntity.this.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 10, 2));
                ParadiseHareEntity.this.playSound(ParadiseLostSoundEvents.BLOCK_BLACKCURRANT_BUSH_PICK_BLUEBERRIES, 1.0F, 1.0F);
                ParadiseHareEntity.this.playSound(ParadiseLostSoundEvents.ENTITY_PARADISE_HARE_EAT, 0.8F, 2.0F);
                ParadiseHareEntity.this.world.setBlockState(this.targetPos, blockState.with(SweetBerryBushBlock.AGE, 1), Block.NOTIFY_LISTENERS);
            }
        }

        @Override
        public boolean shouldContinue() {
            return true;
        }

        @Override
        public void start() {
            this.timer = 0;
            super.start();
        }
    }

}
