package net.id.paradiselost.mixin.cpa;

import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.kyrptonaught.customportalapi.portal.PortalIgnitionSource;
import net.kyrptonaught.customportalapi.portal.PortalPlacer;
import net.kyrptonaught.customportalapi.util.PortalLink;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.id.paradiselost.world.ParadiseLostGameRules.PARADISE_PORTAL_ENABLED;

@Mixin(PortalPlacer.class)
public class PortalPlacerMixin {

    @Inject(method = "createPortal", at = @At("HEAD"), cancellable = true)
    private static void createPortal(PortalLink link, Block foundationBlock, World world, BlockPos portalPos, BlockPos framePos, PortalIgnitionSource ignitionSource, CallbackInfoReturnable<Boolean> cir) {
        if (foundationBlock == ParadiseLostBlocks.BLOOMED_CALCITE && !world.getGameRules().getBoolean(PARADISE_PORTAL_ENABLED))
            cir.cancel();
    }

}
