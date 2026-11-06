package net.id.paradise_lost.mixin.debug;

import net.id.paradise_lost.debug.MinecartRotationDebug;
import net.id.paradise_lost.util.MinecartFloatingHelper;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerEntity.class)
public abstract class ServerEntityRotationDebugMixin {
    @Final
    @Shadow
    private Entity entity;

    @Final
    @Shadow
    private int updateInterval;

    @Shadow
    private int lastSentYRot;

    @Shadow
    private int tickCount;

    @Unique
    private int paradiseLost$lastSentBefore;

    @Inject(method = "sendChanges", at = @At("HEAD"))
    private void paradiseLost$rememberLastSent(CallbackInfo ci) {
        this.paradiseLost$lastSentBefore = this.lastSentYRot;
    }

    @Inject(method = "sendChanges", at = @At("RETURN"))
    private void paradiseLost$logRotation(CallbackInfo ci) {
        if (!(this.entity instanceof AbstractMinecart cart) || !MinecartFloatingHelper.isFloating(cart)) {
            return;
        }
        int entityByte = Mth.floor(cart.getYRot() * 256.0F / 360.0F);
        boolean sent = this.lastSentYRot != this.paradiseLost$lastSentBefore;
        boolean pending = Math.abs(entityByte - this.lastSentYRot) >= 1;
        boolean intervalTick = (this.tickCount - 1) % this.updateInterval == 0;
        if (!sent && !pending && !MinecartRotationDebug.moved(cart, "serverEntityYaw", cart.getYRot(), 0.5F)) {
            return;
        }
        MinecartRotationDebug.log("ServerEntity", cart,
                "rotSent=" + sent
                        + " pendingRot=" + pending
                        + " intervalTick=" + intervalTick
                        + " interval=" + this.updateInterval
                        + " hasImpulse=" + cart.hasImpulse
                        + " lastSentByte=" + this.paradiseLost$lastSentBefore + "->" + this.lastSentYRot
                        + " (" + MinecartRotationDebug.deg(this.lastSentYRot * 360.0F / 256.0F) + " deg)"
                        + " entityByte=" + entityByte
                        + " " + MinecartRotationDebug.state(cart));
    }
}
