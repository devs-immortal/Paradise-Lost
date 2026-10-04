package net.id.paradise_lost.mixin.client.render;

import net.id.paradise_lost.entity.ParadiseLostEntityExtensions;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {

    @Inject(method = "isEntityUpsideDown", at = @At("HEAD"), cancellable = true)
    private static void paradiseLost$wandFlip(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if (((ParadiseLostEntityExtensions) entity).getFlipped() && !(entity instanceof Player)) {
            cir.setReturnValue(true);
        }
    }
}
