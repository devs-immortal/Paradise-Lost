package com.aether.entities.passive;

import com.aether.api.AetherAPI;
import com.aether.entities.AetherEntityTypes;
import com.aether.items.AetherItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

//import com.aether.world.storage.loot.AetherLootTableList;

public class AerbunnyEntity extends AetherAnimalEntity {

    public static final TrackedData<Byte> PUFF = DataTracker.registerData(AerbunnyEntity.class, TrackedDataHandlerRegistry.BYTE);
    public int puffiness;
    private int jumpTicks;
    private int jumps;

    public AerbunnyEntity(World world) {
        super(AetherEntityTypes.AERBUNNY, world);
        this.ignoreCameraFrustum = true;
    }

    @Override
    protected void initGoals() {
        initAttributes();

        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new WanderAroundFarGoal(this, 2D, 6));
        this.goalSelector.add(2, new AnimalMateGoal(this, 1.0D));
        this.goalSelector.add(3, new TemptGoal(this, 1.25D, Ingredient.ofItems(AetherItems.BLUE_BERRY), false));
        this.goalSelector.add(4, new LookAtEntityGoal(this, PlayerEntity.class, 10.0F));
        this.goalSelector.add(5, new LookAroundGoal(this));
        //this.goalSelector.add(6, new EntityAIBunnyHop(this));
    }

    @Override
    protected void initAttributes() {
        super.initAttributes();

        this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.25D);
        this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(5.0D);
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
    public double getMountedHeightOffset() {
        return 0.4D;
    }

    @Override
    public void setJumping(boolean jump) {
        if (jump) {
            this.playSpawnEffects();
            this.setPuffiness(11);
            --this.jumps;
        }

        super.setJumping(jump);
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

        this.setPuffiness(this.getPuffiness() - 1);

        if (this.getPuffiness() < 0) this.setPuffiness(0);
    }

    @Override
    public void tickMovement() {
        if (!this.onGround) {
            this.jumps = 1;
            this.jumpTicks = 2;
        } else if (this.jumpTicks > 0) {
            --this.jumpTicks;
        }

        if (this.jumpTicks <= 0 && this.jumps > 0) {
            this.setJumping(true);
            this.jumps = 1;
            this.jumpTicks = 2;
            this.setVelocity(new Vec3d(this.getVelocity().x, 0.42D, this.getVelocity().z));
        }

        if (this.getVelocity().y < -0.1D)
            this.setVelocity(new Vec3d(this.getVelocity().x, -0.1D, this.getVelocity().z));

        if (this.getPrimaryPassenger() != null && this.getPrimaryPassenger() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) this.getPrimaryPassenger();

            this.getNavigation().stop();
            this.setRotation(player.yaw, player.pitch);
            player.fallDistance = 0.0F;

            if (!player.isOnGround() && !player.isFallFlying()) {
                if (!player.abilities.flying) this.setVelocity(this.getVelocity().add(0.0D, 0.05D, 0.0D));

                player.fallDistance = 0.0F;

                if (player.getVelocity().y < -0.22499999403953552D) {
                    if (AetherAPI.get(player).isJumping()) {
                        this.setVelocity(new Vec3d(this.getVelocity().x, 0.125D, this.getVelocity().z));

                        this.setPuffiness(11);
                        this.playSpawnEffects();
                    }
                }
            }
        }

        super.tickMovement();
    }

    @Override
    public boolean handleFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    public boolean isInsideWall() {
        return false;
    }

    @Override
    public boolean canClimb() {
        return this.isOnGround();
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        if (!stack.isEmpty()) {
            return super.interactMob(player, hand);
        } else {
//            this.world.playSound(this.getX(), this.getY(), this.getZ(), AetherSounds.AERBUNNY_LIFT, SoundCategory.NEUTRAL, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F, false);

            if (this.getPrimaryPassenger() != null) this.stopRiding();
            else this.startRiding(player);

            return ActionResult.SUCCESS;
        }
    }

    @Override
    public boolean damage(DamageSource source, float damage) {
        return (this.getPrimaryPassenger() == null || source.getAttacker() != this.getPrimaryPassenger()) && super.damage(source, damage);
    }

//    @Override
//    protected SoundEvent getHurtSound(DamageSource source) {
//        return AetherSounds.AERBUNNY_HURT;
//    }
//
//    @Override
//    protected SoundEvent getDeathSound() {
//        return AetherSounds.AERBUNNY_DEATH;
//    }

    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entityageable) {
        return new AerbunnyEntity(this.world);
    }

    @Override
    public Identifier getLootTableId() {
        return null;//AetherLootTableList.ENTITIES_AERBUNNY;
    }

}