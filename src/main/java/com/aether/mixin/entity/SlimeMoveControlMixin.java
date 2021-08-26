package com.aether.mixin.entity;

import com.aether.entities.hostile.swet.SwetEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net/minecraft/entity/mob/SlimeEntity$SlimeMoveControl")
public class SlimeMoveControlMixin {
    @Shadow
    @Final
    private SlimeEntity slime;

    @Shadow
    private boolean jumpOften;

    @SuppressWarnings("UnnecessaryQualifiedMemberReference")
    @Inject(method = "Lnet/minecraft/entity/mob/SlimeEntity$SlimeMoveControl;tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/control/JumpControl;setActive()V"), cancellable = true)
    private void cancelIfUnsafe(CallbackInfo ci) {
        if (this.slime instanceof SwetEntity swet && !this.jumpOften) {
            float yaw = (float) Math.toRadians(swet.getYaw());
            Vec3d facing = new Vec3d(-MathHelper.sin(yaw), 0, MathHelper.cos(yaw)).multiply(swet.getSize() * 0.2 + 0.6);
            Vec3d raytraceStart = swet.getPos().add(facing).add(0, 0.5, 0);
            Vec3d raytraceEnd = raytraceStart.add(facing.multiply(0.5)).add(0, -3.4, 0);
            RaycastContext context = new RaycastContext(raytraceStart,
                    raytraceEnd,
                    RaycastContext.ShapeType.COLLIDER,
                    RaycastContext.FluidHandling.NONE,
                    swet);

            if (swet.getEntityWorld().raycast(context).getType() != BlockHitResult.Type.BLOCK) {
                swet.setRandomLookTimer.accept(swet.getRandom().nextInt(16));
                swet.setMovementSpeed(0);
                ci.cancel();
            }
        }
    }
}
