package net.id.paradise_lost.entity.util;

import net.minecraft.server.level.ServerLevel;
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

    public SaddleMountEntity(Level world) {
        this(null, world);
    }

    public Entity getPrimaryPassenger() {
        return getFirstPassenger();
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource damagesource, float i) {
        if ((damagesource.getEntity() instanceof Player) && (!this.getPassengers().isEmpty() && this.getPassengers().get(0) == damagesource.getDirectEntity()))
            return false;

        return super.hurtServer(level, damagesource, i);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SADDLED, false);
    }

    @Override
    protected void dropEquipment(ServerLevel level) {
        super.dropEquipment(level);
        if (this.isSaddled()) {
            this.spawnAtLocation(level, Items.SADDLE);
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack heldItem = player.getItemInHand(hand);
        InteractionResult returnValue = super.mobInteract(player, hand);

        if (!this.isSaddleable()) return super.mobInteract(player, hand);

        if (!this.isSaddled()) {
            if (heldItem.getItem() == Items.SADDLE && !this.isBaby()) {
                if (!player.isCreative()) player.setItemInHand(hand, ItemStack.EMPTY);

                if (player.level().isClientSide)
                    player.level().playSound(player, player.blockPosition(), SoundEvents.PIG_SADDLE, SoundSource.AMBIENT, 1.0F, 1.0F);

                this.setSaddled(true);
                return InteractionResult.SUCCESS;
            }
        } else {
            if (this.getPassengers().isEmpty()) {
                if (!player.level().isClientSide) {
                    player.startRiding(this);
                    player.yRotO = player.getYRot();
                    player.setYRot(this.getYRot());
                }

                return InteractionResult.SUCCESS;
            }
        }
        return returnValue;
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

    @Override
    public boolean isSaddled() {
        return this.entityData.get(SADDLED);
    }

    public void setSaddled(boolean saddled) {
        entityData.set(SADDLED, saddled);

        if (!saddled) {
            for (Entity entity : getPassengers()) {
                entity.stopRiding();
            }
        }
    }

    @Override
    public boolean isSaddleable() {
        return this.isAlive() && !this.isBaby();
    }

    @Override
    public void equipSaddle(ItemStack stack, @Nullable SoundSource soundCategory) {

        if (soundCategory != null) {
            level().playSound(null, this, SoundEvents.PIG_SADDLE, soundCategory, 0.5F, 1.0F);
        }
    }
}
