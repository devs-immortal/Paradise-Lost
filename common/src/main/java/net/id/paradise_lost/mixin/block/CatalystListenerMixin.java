package net.id.paradise_lost.mixin.block;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.id.paradise_lost.registry.ItemRegistry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.SculkCatalystBlockEntity;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.stream.StreamSupport;

@Mixin(SculkCatalystBlockEntity.CatalystListener.class)
public abstract class CatalystListenerMixin {

    @Definition(id = "livingEntity", type = LivingEntity.class)
    @Expression("? instanceof livingEntity")
    @ModifyExpressionValue(
        method = "handleGameEvent",
        at = @At("MIXINEXTRAS:EXPRESSION")
    )

    private boolean paradiseLost$filterDeathSource(boolean original,
            @Local(argsOnly = true) GameEvent.Context context) {
        Entity source = context.sourceEntity();
        if (source instanceof LivingEntity livingEntity) {
            return StreamSupport.stream(livingEntity.getArmorSlots().spliterator(), false)
                    .noneMatch(stack -> stack.is(ItemRegistry.XP_CIRCLET.get()));
        }
        return false;
    }
}
