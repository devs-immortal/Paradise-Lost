package net.id.paradiselost.mixin.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.paradiselost.world.dimension.ParadiseLostDimension;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.HeightLimitView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientWorld.Properties.class)
@Environment(EnvType.CLIENT)
public class ClientWorldPropertiesMixin {

    @Inject(method = "getSkyDarknessHeight", at = @At("HEAD"))
    private void getSkyDarknessHeight(HeightLimitView world, CallbackInfoReturnable<Double> ci) {
        if (((ClientWorld) world).getRegistryKey() == ParadiseLostDimension.PARADISE_LOST_WORLD_KEY) {
            ci.setReturnValue(0.0D);
        }
    }

}
