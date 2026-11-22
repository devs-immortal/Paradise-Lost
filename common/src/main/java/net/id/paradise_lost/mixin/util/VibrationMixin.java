package net.id.paradise_lost.mixin.util;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.id.paradise_lost.item.armor.FloatyBootsItem;
import net.id.paradise_lost.tag.ParadiseLostGameEventTags;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(VibrationSystem.User.class)
public interface VibrationMixin {
    @ModifyExpressionValue(method = "isValidVibration", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;dampensVibrations()Z"))
    private boolean handleFloatyBoots(boolean original, @Local(argsOnly = true) Holder<GameEvent> event,
                                      @Local(argsOnly = true) GameEvent.Context context) {
        return original && !(context.sourceEntity() instanceof LivingEntity livingEntity &&
                FloatyBootsItem.isWearing(livingEntity) && event.is(ParadiseLostGameEventTags.FLOATY_BOOTS_DAMPENED));
    }
}
