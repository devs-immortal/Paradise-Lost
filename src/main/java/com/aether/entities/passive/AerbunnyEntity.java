package com.aether.entities.passive;

import com.aether.blocks.AetherBlocks;
import com.aether.entities.AetherEntityTypes;
import com.aether.items.AetherItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

//import com.aether.world.storage.loot.AetherLootTableList;

public class AerbunnyEntity extends AetherAnimalEntity {

    public static final EntityDataAccessor<Byte> PUFF = SynchedEntityData.defineId(AerbunnyEntity.class, EntityDataSerializers.BYTE);
    public float floof;

    public AerbunnyEntity(Level world) {
        super(AetherEntityTypes.AERBUNNY, world);
    }

    public static AttributeSupplier.Builder initAttributes() {
        return AetherEntityTypes.getDefaultAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.MAX_HEALTH, 5.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D, 20));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1.0D, 15));
        this.goalSelector.addGoal(3, new EatBlueberriesGoal(0.9D, 40, 8));
        this.goalSelector.addGoal(4, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new TemptGoal(this, 1.15D, Ingredient.of(AetherItems.BLUEBERRY), false));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 4.0F, 32));
        //this.goalSelector.add(6, new EntityAIBunnyHop(this));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(PUFF, (byte) 0);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public boolean shouldRenderAtSqrDistance(double par1) {
        return true;
    }

    @Override
    public double getPassengersRidingOffset() {
        return 0.4D;
    }

    @Override
    public void spawnAnim() {
        if (this.level.isClientSide) {
            for (int i = 0; i < 5; ++i) {
                double double_1 = this.random.nextGaussian() * 0.02D;
                double double_2 = this.random.nextGaussian() * 0.02D;
                double double_3 = this.random.nextGaussian() * 0.02D;

                this.level.addParticle(ParticleTypes.POOF, this.getX() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth() - double_1 * 10.0D, this.getY() + (double) (this.random.nextFloat() * this.getBbHeight()) - double_2 * 10.0D, this.getZ() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth() - double_3 * 10.0D, double_1, double_2, double_3);
            }
        } else {
            this.level.broadcastEntityEvent(this, (byte) 20);
        }
    }

    //@Override public boolean canRiderInteract() { return true; }

    public int getPuffiness() {
        return (int) this.entityData.get(PUFF);
    }

    public void setPuffiness(int i) {
        this.entityData.set(PUFF, (byte) i);
    }

    @Override
    public void tick() {
        super.tick();
        int puff = getPuffiness();
        if(puff > 0 && level.getGameTime() % 4 == 0) {
            Vec3 pos = position();
            level.addParticle(ParticleTypes.CLOUD, pos.x, pos.y + 0.2, pos.z, 0, 0, 0);
        }
        else if(isOnGround() && puff > 0) {
            setPuffiness(0);
        }

        if(random.nextFloat() <= 0.03F) {
            AerbunnyEntity.this.playSound(SoundEvents.FOX_SNIFF, 1.0F, 2.0F);
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if(this.isOnGround() && ((getDeltaMovement().x > 0.025 || getDeltaMovement().z > 0.025) && random.nextInt(4) == 0)) {
            jumpFromGround();
        }
    }

    @Override
    protected float getJumpPower() {
        if (!this.horizontalCollision && (!this.moveControl.hasWanted() || !(this.moveControl.getWantedY() > this.getY() + 0.5D))) {
            Path path = this.navigation.getPath();
            if (path != null && !path.isDone()) {
                Vec3 vec3d = path.getNextEntityPos(this);
                if (vec3d.y > this.getY() + 0.5D) {
                    return 0.45F;
                }
            }
            return this.moveControl.getSpeedModifier() <= 0.6D ? 0.3F : 0.4F;
        } else {
            return 0.45F;
        }
    }

    @Override
    protected void jumpFromGround() {
        setPuffiness(1);
        Vec3 pos = position();
        for (int i = 0; i < 4; i++) {
            level.addParticle(ParticleTypes.CLOUD, pos.x + (random.nextGaussian() * 0.2), pos.y + (random.nextGaussian() * 0.2), pos.z + (random.nextGaussian() * 0.2), 0, 0, 0);
        }
        level.playSound(null, this, SoundEvents.RABBIT_JUMP, SoundSource.NEUTRAL, 1, 1);
        super.jumpFromGround();
    }

    @Override
    public boolean canSpawnSprintParticle() {
        return false;
    }

    @Override
    public void move(MoverType type, Vec3 movement) {
        super.move(type, isOnGround() ? movement : movement.multiply(3.5, (movement.y < 0 && getPuffiness() > 0) ? 0.15 : 1, 3.5));
    }

    // TODO: Stubbed. Pending 1.17 rewrite.
//    @Override
//    public boolean handleFallDamage(float distance, float damageMultiplier) {
//        return false;
//    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!stack.isEmpty()) {
            return super.mobInteract(player, hand);
        } else {
//            this.world.playSound(this.getX(), this.getY(), this.getZ(), AetherSounds.AERBUNNY_LIFT, SoundCategory.NEUTRAL, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F, false);

            if (this.getControllingPassenger() != null) this.stopRiding();
            else this.startRiding(player);

            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        return (this.getControllingPassenger() == null || source.getEntity() != this.getControllingPassenger()) && super.hurt(source, damage);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.RABBIT_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.RABBIT_DEATH;
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entityageable) {
        return new AerbunnyEntity(this.level);
    }

    public class EatBlueberriesGoal extends MoveToBlockGoal {
        protected int timer;

        public EatBlueberriesGoal(double speed, int range, int maxYDifference) {
            super(AerbunnyEntity.this, speed, range, maxYDifference);
        }

        public double acceptedDistance() {
            return 2.0D;
        }

        public boolean shouldRecalculatePath() {
            return this.tryTicks % 100 == 0;
        }

        protected boolean isValidTarget(LevelReader world, BlockPos pos) {
            BlockState blockState = world.getBlockState(pos);
            return blockState.is(AetherBlocks.BLUEBERRY_BUSH) && blockState.getValue(SweetBerryBushBlock.AGE) >= 3;
        }

        public void tick() {
            if (this.isReachedTarget()) {
                if (this.timer >= 40) {
                    this.eatSweetBerry();
                } else {
                    ++this.timer;
                }
            } else if (!this.isReachedTarget() && AerbunnyEntity.this.random.nextFloat() < 0.05F) {
                AerbunnyEntity.this.playSound(SoundEvents.FOX_SNIFF, 1.0F, 2.0F);
            }
            super.tick();
        }

        protected void eatSweetBerry() {
            BlockState blockState = AerbunnyEntity.this.level.getBlockState(this.blockPos);
            if (blockState.is(AetherBlocks.BLUEBERRY_BUSH) && blockState.getValue(SweetBerryBushBlock.AGE) == 3) {
                AerbunnyEntity.this.setInLoveTime(40);
                AerbunnyEntity.this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 10, 2));
                AerbunnyEntity.this.playSound(SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, 1.0F, 1.0F);
                AerbunnyEntity.this.playSound(SoundEvents.LLAMA_EAT, 0.8F, 2.0F);
                AerbunnyEntity.this.level.setBlock(this.blockPos, blockState.setValue(SweetBerryBushBlock.AGE, 1), 2);
            }
        }

        @Override
        public boolean canContinueToUse() {
            return true;
        }

        public void start() {
            this.timer = 0;
            super.start();
        }
    }

}