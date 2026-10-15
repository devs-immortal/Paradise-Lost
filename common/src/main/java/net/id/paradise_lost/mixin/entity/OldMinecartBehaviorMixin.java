package net.id.paradise_lost.mixin.entity;

import net.id.paradise_lost.attachments.MinecartFloating;
import net.id.paradise_lost.entity.ParadiseLostMinecartExtensions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.MinecartBehavior;
import net.minecraft.world.entity.vehicle.OldMinecartBehavior;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(OldMinecartBehavior.class)
public abstract class OldMinecartBehaviorMixin extends MinecartBehavior {
    protected OldMinecartBehaviorMixin(AbstractMinecart minecart) {
        super(minecart);
    }

    @Inject(method = "moveAlongTrack", at = @At("HEAD"), cancellable = true)
    private void paradiseLost$skipMidairRailResnap(ServerLevel level, CallbackInfo ci) {
        if (((ParadiseLostMinecartExtensions) this.minecart).paradiseLost$skipMoveAlongTrack(level, this.minecart.getCurrentBlockPosOrRailBelow())) {
            ci.cancel();
        }
    }

    /**
     * Off-rail float only: force the vanilla off-track render path (entity yaw/pitch). Do <em>not</em> rewrite seated
     * {@code getPos}/{@code getPosOffs} — that broke curve posing (offs collapsed → renderer fell back to stuck entity
     * yaw on corners).
     */
    @Inject(method = "getPos(DDD)Lnet/minecraft/world/phys/Vec3;", at = @At("HEAD"), cancellable = true)
    private void paradiseLost$skipFloatingRailSnap(double x, double y, double z, CallbackInfoReturnable<Vec3> cir) {
        if (MinecartFloating.shouldSkipRailRenderSnap(this.minecart)) {
            cir.setReturnValue(null);
        }
    }

    @Inject(method = "getPosOffs(DDDD)Lnet/minecraft/world/phys/Vec3;", at = @At("HEAD"), cancellable = true)
    private void paradiseLost$skipFloatingRailOffs(
            double x, double y, double z, double offset, CallbackInfoReturnable<Vec3> cir) {
        if (MinecartFloating.shouldSkipRailRenderSnap(this.minecart)) {
            cir.setReturnValue(null);
        }
    }
}
