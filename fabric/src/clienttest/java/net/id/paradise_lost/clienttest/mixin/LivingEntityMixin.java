package net.id.paradise_lost.clienttest.mixin;

import net.id.paradise_lost.clienttest.Recorder;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(method = "triggerItemUseEffects", at = @At("HEAD"))
    private void clienttest$countSip(ItemStack stack, int particleCount, CallbackInfo ci) {
        if ((Object) this == Minecraft.getInstance().player) {
            (particleCount == 16 ? Recorder.finalSips : Recorder.sips).incrementAndGet();
        }
    }
}
