package net.id.paradise_lost.mixin.client.render;

import net.id.paradise_lost.world.dimension.ParadiseLostDimension;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.LevelHeightAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientLevel.ClientLevelData.class)
public class ClientWorldPropertiesMixin {

    @Inject(method = "getHorizonHeight", at = @At("HEAD"), cancellable = true)
    private void getSkyDarknessHeight(LevelHeightAccessor world, CallbackInfoReturnable<Double> ci) {
        if (((ClientLevel) world).dimension() == ParadiseLostDimension.PARADISE_LOST_WORLD_KEY) {
            ci.setReturnValue(-200.0D);
        }
    }

}
