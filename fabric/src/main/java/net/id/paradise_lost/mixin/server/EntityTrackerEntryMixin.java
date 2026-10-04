package net.id.paradise_lost.mixin.server;

import net.id.paradise_lost.entity.ParadiseLostEntityExtensions;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ServerEntity.class)
public class EntityTrackerEntryMixin {

    private boolean paradise_lost$flipped = false;
    private int paradise_lost$gravFlippedTime = 0;

    @Final
    @Shadow
    private Entity entity;

    @Inject(method = "sendPairingData", at = @At("HEAD"))
    private void sendPackets(ServerPlayer player, Consumer<Packet<ClientGamePacketListener>> sender, CallbackInfo ci) {
        if (this.entity instanceof LivingEntity) {
            this.paradise_lost$flipped = ((ParadiseLostEntityExtensions) this.entity).getFlipped();
            this.paradise_lost$gravFlippedTime = ((ParadiseLostEntityExtensions) this.entity).getFlipTime();
        }
    }
}
