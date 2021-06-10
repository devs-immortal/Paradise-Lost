package com.aether.entities.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Saddleable;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class SaddleMountEntity extends MountableEntity implements Saddleable {

    private static final TrackedData<Boolean> SADDLED = DataTracker.registerData(SaddleMountEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public SaddleMountEntity(EntityType<? extends AnimalEntity> type, World world) {
        super(type, world);
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
        this.dataTracker.startTracking(SADDLED, false);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack heldItem = player.getStackInHand(hand);

        // TODO: Saddle code here may be able to be removed, due to the implement
        if (!this.canBeSaddled()) return super.interactMob(player, hand);

        if (!this.isSaddled()) {
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
                    player.prevYaw = player.getYaw();
                    player.setYaw(this.getYaw());
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
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("saddled", this.isSaddled());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setSaddled(nbt.getBoolean("saddled"));
    }

    public boolean isSaddled() {
        return this.dataTracker.get(SADDLED);
    }

    public void setSaddled(boolean saddled) {
        this.dataTracker.set(SADDLED, saddled);
    }

    public boolean canBeSaddled() {
        return true;
    }

    public void saddle(@Nullable SoundCategory sound) {
        //this.items.setStack(0, new ItemStack(Items.SADDLE));
        if (sound != null) this.world.playSoundFromEntity(null, this, SoundEvents.ENTITY_PIG_SADDLE, sound, 0.5F, 1.0F);
    }
}