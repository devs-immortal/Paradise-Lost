package net.id.paradiselost.mixin.item;

import net.id.paradiselost.util.MapDimensionData;
import net.minecraft.component.type.MapIdComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.map.MapState;
import net.minecraft.network.packet.Packet;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MapState.class)
public class MapStateMixin {

    @Shadow
    @Final
    public RegistryKey<World> dimension;

    @Inject(method = "getPlayerMarkerPacket", at = @At("RETURN"), cancellable = true)
    public void addDimensionData(MapIdComponent mapId, PlayerEntity player, CallbackInfoReturnable<Packet<?>> cir) {
        Packet<?> packetData = cir.getReturnValue();
        if (packetData instanceof MapDimensionData) {
            ((MapDimensionData) packetData).setDimension(dimension);
        }
    }
}
