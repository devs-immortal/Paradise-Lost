package com.aether.mixin.server;

import com.aether.entities.AetherEntityExtensions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends Entity implements AetherEntityExtensions {
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
}