package net.id.paradise_lost.mixin.item;

import net.id.paradise_lost.util.MapDimensionData;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MapItemSavedData.class)
public class MapStateMixin {

    @Shadow
    @Final
    public ResourceKey<Level> dimension;

    @Inject(method = "getUpdatePacket", at = @At("RETURN"), cancellable = true)
    public void addDimensionData(MapId mapId, Player player, CallbackInfoReturnable<Packet<?>> cir) {
        Packet<?> packetData = cir.getReturnValue();
        if (packetData instanceof MapDimensionData) {
            ((MapDimensionData) packetData).setDimension(dimension);
        }
    }
}
