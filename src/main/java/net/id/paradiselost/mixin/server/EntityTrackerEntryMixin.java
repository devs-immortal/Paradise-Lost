package net.id.paradiselost.mixin.server;

import net.id.paradiselost.entities.ParadiseLostEntityExtensions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.Packet;
import net.minecraft.server.network.EntityTrackerEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(EntityTrackerEntry.class)
public class EntityTrackerEntryMixin {

    private boolean flipped = false;
    private int gravFlippedTime = 0;
    @Final
    @Shadow
    private
    Entity entity;

    @Inject(method = "sendPackets", at = @At("HEAD"))
    private void sendPackets(Consumer<Packet<?>> sender, CallbackInfo ci) {
        if (entity instanceof LivingEntity) {
            flipped = ((ParadiseLostEntityExtensions) entity).getFlipped();
            gravFlippedTime = ((ParadiseLostEntityExtensions) entity).getFlipTime();
        }
    }
}
