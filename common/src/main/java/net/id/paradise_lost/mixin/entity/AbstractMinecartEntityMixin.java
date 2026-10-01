package net.id.paradise_lost.mixin.entity;

import net.id.paradise_lost.attachments.MinecartFloating;
import net.id.paradise_lost.particle.ParadiseLostParticleTypes;
import net.minecraft.nbt.CompoundTag;
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

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void paradiseLost$loadLegacyFloating(CompoundTag tag, CallbackInfo ci) {
        MinecartFloating.loadLegacyNbt((AbstractMinecart) (Object) this, tag);
    }

    @Inject(method = "comeOffTrack", at = @At("HEAD"), cancellable = true)
    protected void moveOffRail(CallbackInfo ci) {
        AbstractMinecart cart = (AbstractMinecart) (Object) this;
        if (!this.onGround() && MinecartFloating.isFloating(cart) && MinecartFloating.getFloatTime(cart) > 0) {
            MinecartFloating.refreshInclineFromCurrentRail(cart);
            double d = this.getMaxSpeed();
            Vec3 vec3d = this.getDeltaMovement();
            double x = Mth.clamp(vec3d.x, -d, d);
            double z = Mth.clamp(vec3d.z, -d, d);
            double y = 0.0D;
            int incline = MinecartFloating.getIncline(cart);
            if (incline != 0) {
                // Continue the 45° ascend/descent from a non-flat levita rail until float time expires.
                y = incline * Math.sqrt(x * x + z * z);
            }
            this.setDeltaMovement(x, y, z);
            this.move(MoverType.SELF, this.getDeltaMovement());
            MinecartFloating.tick(cart);
            ci.cancel();
        }
    }

    @Inject(method = "tick", at = @At("RETURN"))
    public void tick(CallbackInfo ci) {
        AbstractMinecart cart = (AbstractMinecart) (Object) this;
        if (this.level().isClientSide() && !MinecartFloating.isCartOnRail(cart) && MinecartFloating.isFloating(cart)) {
            var pos = this.position();
            var rightParticlePos = pos.add(this.getDeltaMovement().normalize().scale(0.35F).yRot(1.57F));
            var leftParticlePos = pos.add(this.getDeltaMovement().normalize().scale(0.35F).yRot(-1.57F));
            this.level().addParticle(ParadiseLostParticleTypes.LEVITA_BLOOP, rightParticlePos.x(), this.getY(), rightParticlePos.z(), 0, 0, 0);
            this.level().addParticle(ParadiseLostParticleTypes.LEVITA_BLOOP, leftParticlePos.x(), this.getY(), leftParticlePos.z(), 0, 0, 0);
        }
    }
}
