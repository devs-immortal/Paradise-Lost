package net.id.paradise_lost.mixin.entity;

import net.id.paradise_lost.component.FloatingComponent;
import net.id.paradise_lost.component.MinecartFloating;
import net.id.paradise_lost.particle.ParadiseLostParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractMinecart.class)
public abstract class AbstractMinecartEntityMixin extends VehicleEntity {

    public AbstractMinecartEntityMixin(EntityType<?> entityType, Level world) {
        super(entityType, world);
    }

    @Shadow
    protected abstract double getMaxSpeed();

    @Shadow
    public abstract boolean isOnRails();

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void paradiseLost$saveFloating(CompoundTag tag, CallbackInfo ci) {
        CompoundTag floating = new CompoundTag();
        MinecartFloating.get((AbstractMinecart) (Object) this).writeToNbt(floating);
        tag.put("paradiseLostFloating", floating);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void paradiseLost$loadFloating(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("paradiseLostFloating", Tag.TAG_COMPOUND)) {
            MinecartFloating.get((AbstractMinecart) (Object) this).readFromNbt(tag.getCompound("paradiseLostFloating"));
        }
    }

    @Inject(method = "comeOffTrack", at = @At("HEAD"), cancellable = true)
    protected void moveOffRail(CallbackInfo ci) {
        var floatingComponent = MinecartFloating.get((AbstractMinecart) (Object) this);
        if (!this.onGround() && floatingComponent.getFloating() && floatingComponent.getFloatTime() > 0) {
            double d = this.getMaxSpeed();
            Vec3 vec3d = this.getDeltaMovement();
            this.setDeltaMovement(Mth.clamp(vec3d.x, -d, d), 0, Mth.clamp(vec3d.z, -d, d));
            this.move(MoverType.SELF, this.getDeltaMovement());
            floatingComponent.tick();
            MinecartFloating.sync((AbstractMinecart) (Object) this);
            ci.cancel();
        }
    }

    @Inject(method = "tick", at = @At("RETURN"))
    public void tick(CallbackInfo ci) {
        var floatingComponent = MinecartFloating.get((AbstractMinecart) (Object) this);
        if (this.level().isClientSide() && !floatingComponent.isCartOnRail((AbstractMinecart) (VehicleEntity) this) && floatingComponent.getFloating()) {
            var pos = this.position();
            var rightParticlePos = pos.add(this.getDeltaMovement().normalize().scale(0.35F).yRot(1.57F));
            var leftParticlePos = pos.add(this.getDeltaMovement().normalize().scale(0.35F).yRot(-1.57F));
            this.level().addParticle(ParadiseLostParticleTypes.LEVITA_BLOOP, rightParticlePos.x(), this.getY(), rightParticlePos.z(), 0, 0, 0);
            this.level().addParticle(ParadiseLostParticleTypes.LEVITA_BLOOP, leftParticlePos.x(), this.getY(), leftParticlePos.z(), 0, 0, 0);
        }
    }
}
