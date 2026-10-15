package net.id.paradise_lost.gametest.mixin;

import net.id.paradise_lost.gametest.Recorder;
import net.minecraft.client.Minecraft;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Consumable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Consumable.class)
public abstract class ConsumableMixin {
    @Inject(method = "emitParticlesAndSounds", at = @At("HEAD"))
    private void clienttest$countSip(RandomSource random, LivingEntity entity, ItemStack stack, int particleCount, CallbackInfo ci) {
        if (entity == Minecraft.getInstance().player) {
            (particleCount == 16 ? Recorder.finalSips : Recorder.sips).incrementAndGet();
        }
    }
}
