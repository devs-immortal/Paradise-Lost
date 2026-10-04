package net.id.paradise_lost.mixin.portal;

import net.id.paradise_lost.world.portal.ParadiseLostPortalHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LiquidBlock.class)
public class LiquidBlockPortalMixin {
    @Inject(method = "onPlace", at = @At("TAIL"))
    private void paradiseLost$tryLightPortal(
            BlockState state,
            Level level,
            BlockPos pos,
            BlockState oldState,
            boolean moved,
            CallbackInfo ci
    ) {
        if (level.isClientSide) {
            return;
        }
        if (state.getFluidState().is(Fluids.WATER) || state.getFluidState().is(Fluids.FLOWING_WATER)) {
            ParadiseLostPortalHelper.tryLightPortal(level, pos);
        }
    }
}
