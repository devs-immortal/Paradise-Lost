package com.aether.mixin.item;

import com.aether.util.MapDimensionData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapState;
import net.minecraft.network.Packet;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MapState.class)
public class MapStateMixin {

    @Shadow
    public RegistryKey<World> dimension;

    @Inject(method = "getPlayerMarkerPacket", at = @At("RETURN"), cancellable = true)
    public void addDimensionData(ItemStack itemStack_1, BlockView blockView_1, PlayerEntity playerEntity_1, CallbackInfoReturnable<Packet<?>> ci) {
        Packet<?> packetData = ci.getReturnValue();

        if (packetData instanceof MapDimensionData) {
            ((MapDimensionData) packetData).setDimension(this.dimension);
        }
    }

}
