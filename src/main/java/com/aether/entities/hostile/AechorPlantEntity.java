package com.aether.entities.hostile;

import com.aether.blocks.AetherBlocks;
import com.aether.entities.AetherEntityTypes;
import com.aether.entities.passive.AetherAnimalEntity;
import com.aether.items.AetherItems;
import net.minecraft.entity.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

public class AechorPlantEntity extends AetherAnimalEntity implements RangedAttackMob {

    public float sinage;
    public final int poisonRemaining;
    public int size;

    public AechorPlantEntity(Level world) {
        super(AetherEntityTypes.AECHOR_PLANT, world);

        this.size = this.random.nextInt(4) + 1;
        this.sinage = this.random.nextFloat() * 6F;
        this.poisonRemaining = this.random.nextInt(4) + 2;

        this.setPos(this.getX(), this.getY(), this.getZ());
    }

    public static AttributeSupplier.Builder initAttributes() {
        return AetherEntityTypes.getDefaultAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D);
    }

    public EntityDimensions getSizeForStatus(Pose entityPose_1) {
        return EntityDimensions.scalable(0.75F + ((float) this.size * 0.125F), 0.5F + ((float) this.size * 0.075F));
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

        this.goalSelector.addGoal(4, new RangedAttackGoal(this, 0.0D, 30, 1.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, true));
    }

    @Override
    public void tick() {
        super.tick();

        if (this.hurtTime > 0) this.sinage += 0.9F;

        if (this.getLastHurtByMob() != null) this.sinage += 0.3F;
        else this.sinage += 0.1F;

        if (this.sinage > 3.141593F * 2F) this.sinage -= (3.141593F * 2F);

        if (this.level.getBlockState(this.blockPosition().below(1)).getBlock() != AetherBlocks.AETHER_GRASS_BLOCK)
            this.outOfWorld();
    }

    @Override
    public boolean hasLineOfSight(Entity entity) {
        double distance = this.distanceTo(entity);
        return distance <= 4.0F && super.hasLineOfSight(entity);
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType SpawnReason) {
        return worldIn.getBlockState(this.blockPosition().below(1)).getBlock() == AetherBlocks.AETHER_GRASS_BLOCK && this.random.nextInt(400) == 0;
    }

    @Override
    public void performRangedAttack(LivingEntity targetIn, float arg1) {
        double x = targetIn.getX() - this.getX();
        double z = targetIn.getZ() - this.getZ();
        final double sqrt = Math.sqrt((x * x) + (z * z) + 0.1D);
        double y = 0.1D + (sqrt * 0.5D) + ((this.getY() - targetIn.getY()) * 0.25D);
        double distance = 1.5D / sqrt;

//        PoisonNeedleEntity needle = new PoisonNeedleEntity(this, this.world);
//        needle.setVelocity(x * distance, y, z * distance, 0.285F + ((float) y * 0.05F), 1.0F);
//        this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.2F / (this.getRandom().nextFloat() * 0.2F + 0.9F));
//        this.world.spawnEntity(needle);
    }

    @Override
    public InteractionResult mobInteract(Player playerIn, InteractionHand handIn) {
        ItemStack heldItem = playerIn.getItemInHand(handIn);

        if (heldItem.getItem() == AetherItems.SKYROOT_BUCKET && !playerIn.getAbilities().instabuild) {
            heldItem.setCount(heldItem.getCount() - 1);

            if (heldItem.isEmpty()) playerIn.setItemInHand(handIn, new ItemStack(AetherItems.SKYROOT_POISON_BUCKET));
            else if (!playerIn.getInventory().add(new ItemStack(AetherItems.SKYROOT_POISON_BUCKET)))
                playerIn.drop(new ItemStack(AetherItems.SKYROOT_POISON_BUCKET), false);

            return InteractionResult.SUCCESS;
        } else {
            return super.mobInteract(playerIn, handIn);
        }
    }

    @Override
    public void knockback(double strength, double xRatio, double zRatio) {
        if (this.getHealth() <= 0.0F) super.knockback(strength, xRatio, zRatio);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("size", this.size);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.size = compound.getInt("size");
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entityIn) {
        return null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.GENERIC_BIG_FALL;
    }

    @Override
    public ResourceLocation getDefaultLootTable() {
        return null;//AetherLootTableList.ENTITIES_AECHOR_PLANT;
    }

    @Override
    public boolean isPushable() {
        return false;
    }
}