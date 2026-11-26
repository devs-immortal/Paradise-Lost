package net.id.paradise_lost.mixin.util;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.id.paradise_lost.item.armor.FloatyBootsItem;
import net.id.paradise_lost.tag.ParadiseLostGameEventTags;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(VibrationSystem.Listener.class)
public class VibrationMixin {
    @WrapMethod(method = "handleGameEvent")
    private boolean handleFloatyBoots(ServerLevel level, Holder<GameEvent> event, GameEvent.Context context, Vec3 pos, Operation<Boolean> original) {
        if (context.sourceEntity() instanceof LivingEntity livingEntity
                && FloatyBootsItem.isWearing(livingEntity)
                && event.is(ParadiseLostGameEventTags.FLOATY_BOOTS_DAMPENED)) {
            return false;
        }
        return original.call(level, event, context, pos);
    }
}
