package net.id.paradise_lost.mixin.entity;

import net.id.paradise_lost.util.ParadiseLostVoidEscape;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Inject(method = "onBelowWorld", at = @At("HEAD"), cancellable = true)
    private void paradiseLost$voidEscape(CallbackInfo ci) {
        if (ParadiseLostVoidEscape.tryEscape((Entity) (Object) this)) {
            ci.cancel();
        }
    }
}
