package net.id.paradise_lost.mixin.client.render;

import net.id.paradise_lost.attachments.MinecartFloating;
import net.minecraft.client.renderer.entity.MinecartRenderer;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MinecartRenderer.class)
public class MinecartRendererMixin {

    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/vehicle/AbstractMinecart;getPos(DDD)Lnet/minecraft/world/phys/Vec3;",
                    ordinal = 0
            )
    )
    private Vec3 paradiseLost$skipExitRailSnap(AbstractMinecart cart, double x, double y, double z) {
        if (MinecartFloating.shouldUseFloatingRenderRotation(cart)) {
            return null;
        }
        return cart.getPos(x, y, z);
    }

    @ModifyVariable(method = "render", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private float paradiseLost$floatingRailYaw(float entityYaw, AbstractMinecart cart) {
        if (MinecartFloating.shouldUseFloatingRenderRotation(cart)) {
            Float yaw = MinecartFloating.getFloatingRenderYaw(cart);
            if (yaw != null) {
                return yaw;
            }
        }
        return entityYaw;
    }
}
