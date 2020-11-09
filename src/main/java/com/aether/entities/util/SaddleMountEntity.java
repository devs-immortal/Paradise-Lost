package com.aether.entities.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public abstract class SaddleMountEntity extends MountableEntity {

    private static final TrackedData<Boolean> SADDLED = DataTracker.registerData(SaddleMountEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public SaddleMountEntity(EntityType<? extends AnimalEntity> type, World world) {
        super(type, world);
    }

    public SaddleMountEntity(World world) {
        super(EntityType.PIG, world);
    }

    @Override
    public Entity getPrimaryPassenger() {
        return this.getPassengerList().isEmpty() ? null : this.getPassengerList().get(0);
    }

    @Override
    public boolean damage(DamageSource damagesource, float i) {
        if ((damagesource.getAttacker() instanceof PlayerEntity) && (!this.getPassengerList().isEmpty() && this.getPassengerList().get(0) == damagesource.getSource()))
            return false;

        return super.damage(damagesource, i);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        initAttributes();

        this.dataTracker.startTracking(SADDLED, false);
    }

    protected void initAttributes() {
        //
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack heldItem = player.getStackInHand(hand);

        if (!this.canSaddle()) return super.interactMob(player, hand);

        if (!this.getSaddled()) {
            if (heldItem.getItem() == Items.SADDLE && !this.isBaby()) {
                if (!player.isCreative()) player.setStackInHand(hand, ItemStack.EMPTY);

                if (player.world.isClient)
                    player.world.playSound(player, player.getBlockPos(), SoundEvents.ENTITY_PIG_SADDLE, SoundCategory.AMBIENT, 1.0F, 1.0F);

                this.setSaddled(true);
                return ActionResult.SUCCESS;
            }
        } else {
            if (this.getPassengerList().isEmpty()) {
                if (!player.world.isClient) {
                    player.startRiding(this);
                    player.prevYaw = player.yaw = this.yaw;
                }

                return ActionResult.SUCCESS;
            }
        }
        return super.interactMob(player, hand);
    }

    @Override
    public boolean canBeControlledByRider() {
        return true;
    }

    @Override
    public boolean isInsideWall() {
        if (!this.getPassengerList().isEmpty()) return false;
        return super.isInsideWall();
    }

    @Override
    public void writeCustomDataToTag(CompoundTag nbt) {
        super.writeCustomDataToTag(nbt);
        nbt.putBoolean("saddled", this.getSaddled());
    }

    @Override
    public void readCustomDataFromTag(CompoundTag nbt) {
        super.readCustomDataFromTag(nbt);
        this.setSaddled(nbt.getBoolean("saddled"));
    }

    public boolean getSaddled() {
        return this.dataTracker.get(SADDLED);
    }

    public void setSaddled(boolean saddled) {
        this.dataTracker.set(SADDLED, saddled);
    }

    public boolean canSaddle() {
        return true;
    }

}