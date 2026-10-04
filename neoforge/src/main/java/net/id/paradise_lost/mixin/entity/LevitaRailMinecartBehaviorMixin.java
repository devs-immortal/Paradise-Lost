package net.id.paradise_lost.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.id.paradise_lost.block.mechanical.LevitaRailBlock;
import net.minecraft.world.entity.vehicle.OldMinecartBehavior;
import net.minecraft.world.level.block.PoweredRailBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(OldMinecartBehavior.class)
public class LevitaRailMinecartBehaviorMixin {

    @WrapOperation(
            method = "moveAlongTrack",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/PoweredRailBlock;isActivatorRail()Z")
    )
    private boolean paradiseLost$levitaRailIsNotAPoweredRail(PoweredRailBlock rail, Operation<Boolean> original) {
        return rail instanceof LevitaRailBlock || original.call(rail);
    }
}
