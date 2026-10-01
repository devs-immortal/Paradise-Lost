package net.id.paradise_lost.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.id.paradise_lost.block.mechanical.LevitaRailBlock;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractMinecart.class)
public class AbstractMinecartMixin {

    @ModifyExpressionValue(
            method = "moveAlongTrack",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/PoweredRailBlock;isActivatorRail()Z")
    )
    private boolean paradiseLost$levitaRailIsNotAPoweredRail(
            boolean isActivatorRail,
            BlockPos pos,
            BlockState state
    ) {
        return state.getBlock() instanceof LevitaRailBlock || isActivatorRail;
    }
}
