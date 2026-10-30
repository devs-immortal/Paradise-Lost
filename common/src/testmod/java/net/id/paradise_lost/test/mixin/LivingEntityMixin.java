package net.id.paradise_lost.test.mixin;

import net.id.paradise_lost.test.Recorder;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow
    public abstract boolean isDeadOrDying();

    @Inject(method = "triggerItemUseEffects", at = @At("HEAD"))
    private void testmod$countSip(ItemStack stack, int particleCount, CallbackInfo ci) {
        if ((Object) this instanceof LivingEntity entity) {
            if (entity != Minecraft.getInstance().player)
                return;

            if (particleCount == 16) {
                Recorder.finalSips.incrementAndGet();
            } else if (entity.getUseItemRemainingTicks() > 0) {
                Recorder.sips.incrementAndGet();
            }
        }
    }

    @Inject(method = "hurt", at = @At("TAIL"))
    private void testmod$recordDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof ServerPlayer && !isDeadOrDying()) {
            Recorder.playerDamage.add(source);
        }
    }
}
