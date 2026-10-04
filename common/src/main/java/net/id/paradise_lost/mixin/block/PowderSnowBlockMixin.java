package net.id.paradise_lost.mixin.block;

import net.id.paradise_lost.item.armor.FloatyBootsItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.PowderSnowBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PowderSnowBlock.class)
public class PowderSnowBlockMixin {

    @Inject(method = "canEntityWalkOnPowderSnow", at = @At("RETURN"), cancellable = true)
    private static void paradiseLost$floatyBootsWalkOnPowderSnow(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue() && entity instanceof LivingEntity living && FloatyBootsItem.isWearing(living)) {
            cir.setReturnValue(true);
        }
    }
}
