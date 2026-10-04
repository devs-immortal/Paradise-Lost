package net.id.paradise_lost.mixin.server;

import net.id.paradise_lost.entity.ParadiseLostEntityExtensions;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.bundle.PacketAndPayloadAcceptor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerEntity.class)
public class EntityTrackerEntryMixin {

    private boolean flipped = false;
    private int gravFlippedTime = 0;

    @Final
    @Shadow
    private Entity entity;

    @Inject(method = "sendPairingData", at = @At("HEAD"))
    private void sendPackets(ServerPlayer player, PacketAndPayloadAcceptor<ClientGamePacketListener> sender, CallbackInfo ci) {
        if (this.entity instanceof LivingEntity) {
            this.flipped = ((ParadiseLostEntityExtensions) this.entity).getFlipped();
            this.gravFlippedTime = ((ParadiseLostEntityExtensions) this.entity).getFlipTime();
        }
    }
}
