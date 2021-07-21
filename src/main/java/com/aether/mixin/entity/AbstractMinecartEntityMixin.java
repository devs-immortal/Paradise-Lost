package com.aether.mixin.entity;

import com.aether.blocks.AetherBlocks;
import com.aether.entities.AetherMinecartExtensions;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractMinecartEntity.class)
public abstract class AbstractMinecartEntityMixin implements AetherMinecartExtensions {
    private double boostSpeed = 1;

    @Shadow
    public abstract void moveOnRail(BlockPos pos, BlockState state);

    @Override
    public double getBoostSpeed() {
        return boostSpeed;
    }

    @Override
    public void setBoostSpeed(double boostSpeed) {
        this.boostSpeed = boostSpeed;
    }

    @Inject(method = "getMaxOffRailSpeed", at = @At("TAIL"), cancellable = true)
    private void getMaxOffRailSpeed(CallbackInfoReturnable<Double> ci) {
        ci.setReturnValue(ci.getReturnValueD() *  this.getBoostSpeed());
    }

    @Inject(method = "moveOnRail", at = @At("HEAD"))
    private void moveOnRail(BlockPos pos, BlockState state, CallbackInfo ci) {
        if (state.isOf(AetherBlocks.QUICKSOIL_RAIL)) {
            if (((Entity) (Object) this).world.getGameRules().getBoolean(GameRules.DO_FIRE_TICK)) {
                this.setBoostSpeed(this.getBoostSpeed() * 1.166);
                ((Entity) (Object) this).setVelocity(((Entity) (Object) this).getVelocity().multiply(1.05, 1, 1.05));
            } else {
                this.setBoostSpeed(4);
            }
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        if (((Entity) (Object) this).world.getGameRules().getBoolean(GameRules.DO_FIRE_TICK)) {
            this.setBoostSpeed(Math.max(this.getBoostSpeed() * 0.9, 1));
        } else {
            this.setBoostSpeed(1);
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    private void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        this.setBoostSpeed(nbt.getDouble("BoostSpeed"));
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putDouble("BoostSpeed", this.getBoostSpeed());
    }
}
