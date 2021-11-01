package net.id.aether.mixin.client;

import net.id.aether.blocks.AetherBlocks;
import net.id.aether.entities.block.FloatingBlockEntity;
import net.id.aether.util.AetherSoundEvents;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

// We need to go after customportalapi which has a priority of 1000.
@Mixin(priority = 1001, value = ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity implements FloatingBlockEntity.PostTickEntity {
    @SuppressWarnings("ConstantConditions")
    public ClientPlayerEntityMixin(){
        super(null, null);
    }
    
    @Unique
    boolean sendMovement = false;
    
    @Shadow
    protected abstract void sendMovementPackets();

    /**
     * Since the player can be moved by FloatingBlockEntity after ClientPlayerEntity.tick()
     * the call to sendMovementPackets() needs to be delayed till after all FloatingBlockEntities have ticked
     */

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;sendMovementPackets()V"))
    void redirectSendMovementPackets(ClientPlayerEntity clientPlayerEntity) {
        sendMovement = true;
    }

    @Override
    public void postTick() {
        if (sendMovement) {
            sendMovementPackets();
            sendMovement = false;
        }
    }
    
    // customportalsapi doesn't let you provide a custom portal sound, hack into it......
    @ModifyArg(
        method = "updateCustomNausea",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/sound/PositionedSoundInstance;ambient(Lnet/minecraft/sound/SoundEvent;FF)Lnet/minecraft/client/sound/PositionedSoundInstance;"
        )
    )
    private SoundEvent updateNausea$BLOCK_PORTAL_TRIGGER(SoundEvent event){
        //TODO Is there a better way? This shouldn't be too bad because it's on the client and somewhat rare, but come on!
    
        Box boundingBox = getBoundingBox();
        BlockPos start = new BlockPos(boundingBox.minX + 0.001D, boundingBox.minY + 0.001D, boundingBox.minZ + 0.001D);
        BlockPos end = new BlockPos(boundingBox.maxX - 0.001D, boundingBox.maxY - 0.001D, boundingBox.maxZ - 0.001D);
        if(world.isRegionLoaded(start, end)){
            BlockPos.Mutable mutable = new BlockPos.Mutable();
        
            for(int x = start.getX(); x <= end.getX(); x++){
                for(int y = start.getY(); y <= end.getY(); y++){
                    for(int z = start.getZ(); z <= end.getZ(); z++){
                        mutable.set(x, y, z);
                        BlockState blockState = world.getBlockState(mutable);
                        if(blockState.isOf(AetherBlocks.BLUE_PORTAL)){
                            return AetherSoundEvents.BLOCK_AETHER_PORTAL_TRIGGER;
                        }
                    }
                }
            }
        }
        
        return event;
    }
}
