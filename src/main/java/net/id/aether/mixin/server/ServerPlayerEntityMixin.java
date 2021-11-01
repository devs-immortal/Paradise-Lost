package net.id.aether.mixin.server;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.id.aether.entities.AetherEntityExtensions;
import net.id.aether.util.AetherNetworking;
import net.id.aether.world.dimension.AetherDimension;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.Packet;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends Entity implements AetherEntityExtensions {
    @Shadow public abstract ServerWorld getServerWorld();
    
    public ServerPlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }
//
//    private boolean flipped = false;
//
//    private int gravFlipTime;

    // inject into playerTick instead
//    @Inject(method = "tick", at = @At("TAIL"))
//    private void tick(CallbackInfo ci){
//        if(flipped){
//            gravFlipTime++;
//            if(gravFlipTime > 20){
//                flipped = false;
//                this.fallDistance = 0;
//            }
//            if(!this.hasNoGravity()) {
//                Vec3d antiGravity = new Vec3d(0, 0.12D, 0);
//                this.setVelocity(this.getVelocity().add(antiGravity));
//            }
//        }
//    }
    
    @Unique private RegistryKey<World> the_aether$teleportDestination;
    
    @Inject(
        method = "moveToWorld",
        at = @At("HEAD")
    )
    private void moveToWorld(ServerWorld destination, CallbackInfoReturnable<Entity> cir){
        the_aether$teleportDestination = destination == null ? null : destination.getRegistryKey();
    }
    
    @ModifyArg(
        method = "moveToWorld",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;sendPacket(Lnet/minecraft/network/Packet;)V"
        ),
        slice = @Slice(
            from = @At(
                value = "NEW",
                target = "Lnet/minecraft/network/packet/s2c/play/WorldEventS2CPacket;<init>(ILnet/minecraft/util/math/BlockPos;IZ)V"
            ),
            to = @At("TAIL")
        )
    )
    private Packet<?> moveToWorld$sendWorldEventPacket(Packet<?> packet){
        var destination = the_aether$teleportDestination;
        the_aether$teleportDestination = null;
        
        if(
            (destination != null && destination.equals(AetherDimension.AETHER_WORLD_KEY)) ||
            getServerWorld().getRegistryKey().equals(AetherDimension.AETHER_WORLD_KEY)
        ){
            return ServerPlayNetworking.createS2CPacket(AetherNetworking.PLAY_PORTAL_TRAVEL_SOUND, PacketByteBufs.empty());
        }
        return packet;
    }
}