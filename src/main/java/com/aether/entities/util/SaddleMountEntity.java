package com.aether.entities.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Saddleable;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public abstract class SaddleMountEntity extends MountableEntity implements Saddleable {

    private static final EntityDataAccessor<Boolean> SADDLED = SynchedEntityData.defineId(SaddleMountEntity.class, EntityDataSerializers.BOOLEAN);

    public SaddleMountEntity(EntityType<? extends Animal> type, Level world) {
        super(type, world);
    }

    @Override
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }

    @Override
    public boolean hurt(DamageSource damagesource, float i) {
        if ((damagesource.getEntity() instanceof Player) && (!this.getPassengers().isEmpty() && this.getPassengers().get(0) == damagesource.getDirectEntity()))
            return false;

        return super.hurt(damagesource, i);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SADDLED, false);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack heldItem = player.getItemInHand(hand);

        // TODO: Saddle code here may be able to be removed, due to the implement
        if (!this.isSaddleable()) return super.mobInteract(player, hand);

        if (!this.isSaddled()) {
            if (heldItem.getItem() == Items.SADDLE && !this.isBaby()) {
                if (!player.isCreative()) player.setItemInHand(hand, ItemStack.EMPTY);

                if (player.level.isClientSide)
                    player.level.playSound(player, player.blockPosition(), SoundEvents.PIG_SADDLE, SoundSource.AMBIENT, 1.0F, 1.0F);

                this.setSaddled(true);
                return InteractionResult.SUCCESS;
            }
        } else {
            if (this.getPassengers().isEmpty()) {
                if (!player.level.isClientSide) {
                    player.startRiding(this);
                    player.yRotO = player.getYRot();
                    player.setYRot(this.getYRot());
                }

                return InteractionResult.SUCCESS;
            }
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public boolean canBeControlledByRider() {
        return true;
    }

    @Override
    public boolean isInWall() {
        if (!this.getPassengers().isEmpty()) return false;
        return super.isInWall();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("saddled", this.isSaddled());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.setSaddled(nbt.getBoolean("saddled"));
    }

    public boolean isSaddled() {
        return this.entityData.get(SADDLED);
    }

    public void setSaddled(boolean saddled) {
        this.entityData.set(SADDLED, saddled);
    }

    public boolean isSaddleable() {
        return true;
    }

    public void equipSaddle(@Nullable SoundSource sound) {
        //this.items.setStack(0, new ItemStack(Items.SADDLE));
        if (sound != null) this.level.playSound(null, this, SoundEvents.PIG_SADDLE, sound, 0.5F, 1.0F);
    }
}