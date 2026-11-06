package net.id.paradise_lost.mixin.debug;

import net.id.paradise_lost.debug.MinecartRotationDebug;
import net.id.paradise_lost.entity.MinecartFloatPoseAccess;
import net.id.paradise_lost.util.MinecartFloatingHelper;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Locale;

@Mixin(AbstractMinecart.class)
public abstract class AbstractMinecartRotationDebugMixin {
    private static final float THRESHOLD = 0.5F;
    private static final int HEARTBEAT_TICKS = 20;

    @Shadow
    private int lerpSteps;

    @Inject(method = "tick", at = @At("TAIL"))
    private void paradiseLost$logTick(CallbackInfo ci) {
        AbstractMinecart cart = (AbstractMinecart) (Object) this;
        if (!MinecartFloatingHelper.isFloating(cart)) {
            return;
        }
        MinecartFloatPoseAccess pose = (MinecartFloatPoseAccess) cart;
        boolean moved = MinecartRotationDebug.moved(cart, "tickEntityYaw", cart.getYRot(), THRESHOLD);
        moved |= MinecartRotationDebug.moved(cart, "tickSyncYaw", MinecartFloatingHelper.getSyncedFloatYaw(cart), THRESHOLD);
        moved |= MinecartRotationDebug.moved(cart, "tickRailPoseYaw",
                pose.paradiseLost$hasRailRenderPose() ? pose.paradiseLost$getRailRenderYaw() : Float.NaN, THRESHOLD);
        if (!moved && cart.tickCount % HEARTBEAT_TICKS != 0) {
            return;
        }
        String client = "";
        if (cart.level().isClientSide()) {
            client = " lerpSteps=" + this.lerpSteps
                    + " lerpTargetYRot=" + MinecartRotationDebug.deg(cart.lerpTargetYRot())
                    + " renderYaw=" + MinecartRotationDebug.deg(MinecartFloatingHelper.getFloatRenderYaw(cart))
                    + " useFloatPose=" + MinecartFloatingHelper.shouldUseFloatRenderPose(cart);
        }
        MinecartRotationDebug.log("Tick", cart, "changed=" + moved + client + " " + MinecartRotationDebug.state(cart));
    }

    @Inject(method = "lerpTo", at = @At("HEAD"))
    private void paradiseLost$logRotationTarget(
            double x, double y, double z, float yRot, float xRot, int steps, CallbackInfo ci) {
        AbstractMinecart cart = (AbstractMinecart) (Object) this;
        if (!cart.level().isClientSide() || !MinecartFloatingHelper.isFloating(cart)) {
            return;
        }
        MinecartRotationDebug.log("LerpTo", cart,
                "packetYRot=" + MinecartRotationDebug.deg(yRot)
                        + " steps=" + steps
                        + " previousLerpSteps=" + this.lerpSteps
                        + " previousTargetYRot=" + MinecartRotationDebug.deg(cart.lerpTargetYRot())
                        + " target=(" + String.format(Locale.ROOT, "%.2f,%.2f,%.2f", x, y, z) + ")"
                        + " " + MinecartRotationDebug.state(cart));
    }
}
