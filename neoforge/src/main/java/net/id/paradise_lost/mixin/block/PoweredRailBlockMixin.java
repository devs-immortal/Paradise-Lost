package net.id.paradise_lost.mixin.block;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.id.paradise_lost.block.mechanical.LevitaRailBlock;
import net.minecraft.world.level.block.PoweredRailBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PoweredRailBlock.class)
public class PoweredRailBlockMixin {
    @Definition(
            id = "isActivatorRail",
            method = "Lnet/minecraft/world/level/block/PoweredRailBlock;isActivatorRail()Z"
    )
    @Definition(
            id = "other",
            local = @Local(type = PoweredRailBlock.class)
    )
    @Expression("this.isActivatorRail() != other.isActivatorRail()")
    @ModifyExpressionValue(method = "isSameRailWithPower", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean paradiseLost$levitaRailIsItsOwnKindOfRun(
            boolean activatorStatusMismatch,
            @Local(type = PoweredRailBlock.class) PoweredRailBlock other
    ) {
        return activatorStatusMismatch
                || ((Object) this instanceof LevitaRailBlock) != (other instanceof LevitaRailBlock);
    }
}
