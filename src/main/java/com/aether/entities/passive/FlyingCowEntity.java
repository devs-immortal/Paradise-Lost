package com.aether.entities.passive;

import com.aether.entities.AetherEntityTypes;
import com.aether.entities.util.SaddleMountEntity;
import com.aether.items.AetherItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

//import com.aether.world.storage.loot.AetherLootTableList;

public class FlyingCowEntity extends SaddleMountEntity {

    public float wingFold;

    public float wingAngle;
    public int maxJumps;
    public int jumpsRemaining;
    private float aimingForFold;
    private int ticks;

    public FlyingCowEntity(World world) {
        super(AetherEntityTypes.FLYING_COW, world);

        this.ticks = 0;
        this.maxJumps = 1;
        this.jumpsRemaining = 0;
        this.stepHeight = 1.0F;
        this.ignoreCameraFrustum = true;
        this.canJumpMidAir = true;
    }

    public static DefaultAttributeContainer.Builder initAttributes() {
        return AetherEntityTypes.getDefaultAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.20000000298023224D);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 2.0D));
        this.goalSelector.add(2, new AnimalMateGoal(this, 1.0D));
        this.goalSelector.add(3, new TemptGoal(this, 1.25D, Ingredient.ofItems(AetherItems.BLUEBERRY), false));
        this.goalSelector.add(4, new FollowParentGoal(this, 1.25D));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(7, new LookAroundGoal(this));
    }

    @Override
    public void tick() {
        super.tick();

        if (this.onGround) {
            this.wingAngle *= 0.8F;
            this.aimingForFold = 0.1F;
            this.jumpsRemaining = this.maxJumps;
        } else {
            this.aimingForFold = 1.0F;
        }

        this.ticks++;

        this.wingAngle = this.wingFold * (float) Math.sin(this.ticks / 31.83098862F);
        this.wingFold += (this.aimingForFold - this.wingFold) / 5F;
        this.fallDistance = 0;
        this.fall();
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound compound) {
        super.writeCustomDataToNbt(compound);

        compound.putInt("maxJumps", this.maxJumps);
        compound.putInt("remainingJumps", this.jumpsRemaining);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compound) {
        super.readCustomDataFromNbt(compound);

        this.maxJumps = compound.getInt("maxJumps");
        this.jumpsRemaining = compound.getInt("remainingJumps");
    }

    @Override
    public double getMountedHeightOffset() {
        return 1.15D;
    }

    @Override
    public float getMountedMoveSpeed() {
        return 0.3F;
    }

    @Override
    public void setJumping(boolean jump) {
        super.setJumping(jump);
    }

    private void fall() {
        if (!this.onGround) {
            if (this.getVelocity().y < 0.0D && !this.isSneaking())
                this.setVelocity(this.getVelocity().multiply(1.0D, 0.6D, 1.0D));

            if (this.onGround && !this.world.isClient) this.jumpsRemaining = this.maxJumps;
        }
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack currentStack = player.getStackInHand(hand);

        if (currentStack.getItem() == Items.BUCKET && !this.isBaby()) {
            if (currentStack.getCount() == 1) {
                player.setStackInHand(hand, new ItemStack(Items.MILK_BUCKET));
            } else if (!player.getInventory().insertStack(new ItemStack(Items.MILK_BUCKET))) {
                if (!this.world.isClient) {
                    player.dropItem(new ItemStack(Items.MILK_BUCKET), false);

                    if (!player.getAbilities().creativeMode) {
                        currentStack.setCount(currentStack.getCount() - 1);
                    }
                }
            } else if (!player.getAbilities().creativeMode) {
                currentStack.setCount(currentStack.getCount() - 1);
            }
        }

        return super.interactMob(player, hand);
    }

//    @Override
//    protected SoundEvent getAmbientSound() {
//        return AetherSounds.FLYINGCOW_SAY;
//    }
//
//    @Override
//    protected SoundEvent getHurtSound(DamageSource source) {
//        return AetherSounds.FLYINGCOW_HURT;
//    }
//
//    @Override
//    protected SoundEvent getDeathSound() {
//        return AetherSounds.FLYINGCOW_DEATH;
//    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_COW_STEP, SoundCategory.NEUTRAL, 0.15F, 1.0F);
    }

    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return new FlyingCowEntity(this.world);
    }

    @Override
    public Identifier getLootTableId() {
        return null;//AetherLootTableList.ENTITIES_FLYING_COW;
    }

    @Override
    protected double getMountJumpStrength() {
        return 5.0D;
    }
}