package net.id.paradiselost.mixin.entity;

import net.id.paradiselost.client.rendering.particle.ParadiseLostParticles;
import net.id.paradiselost.component.ParadiseLostComponents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.VehicleEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractMinecartEntity.class)
public abstract class AbstractMinecartEntityMixin extends VehicleEntity {

    public AbstractMinecartEntityMixin(EntityType<?> entityType, World world) {
        super(entityType, world);
    }

    @Shadow
    private boolean onRail;

    @Shadow
    protected abstract double getMaxSpeed();

    @Inject(method = "moveOffRail", at = @At("HEAD"), cancellable = true)
    protected void moveOffRail(CallbackInfo ci) {
        var floatingComponent = ParadiseLostComponents.FLOATING_KEY.get(this);
        if (!this.isOnGround() && floatingComponent.getFloating() && floatingComponent.getFloatTime() > 0) {
            double d = this.getMaxSpeed();
            Vec3d vec3d = this.getVelocity();
            this.setVelocity(MathHelper.clamp(vec3d.x, -d, d), 0, MathHelper.clamp(vec3d.z, -d, d));
            this.move(MovementType.SELF, this.getVelocity());
            // decrement
            floatingComponent.tick();
            ParadiseLostComponents.FLOATING_KEY.sync(this);
            ci.cancel();
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        var floatingComponent = ParadiseLostComponents.FLOATING_KEY.get(this);
        if (this.getWorld().isClient && !this.onRail && floatingComponent.getFloating()) {
            var pos = this.getPos();
            var rightParticlePos = pos.add(this.getVelocity().normalize().multiply(0.35F).rotateY(1.57F));
            var leftParticlePos = pos.add(this.getVelocity().normalize().multiply(0.35F).rotateY(-1.57F));
            this.getWorld().addParticle(ParadiseLostParticles.LEVITA_BLOOP, rightParticlePos.getX(), this.getY(), rightParticlePos.getZ(), 0, 0, 0);
            this.getWorld().addParticle(ParadiseLostParticles.LEVITA_BLOOP, leftParticlePos.getX(), this.getY(), leftParticlePos.getZ(), 0, 0, 0);
        }
    }
}
