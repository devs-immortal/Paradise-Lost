package net.id.paradiselost.mixin.server;


import net.id.paradiselost.entities.ParadiseLostEntityExtensions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends Entity implements ParadiseLostEntityExtensions {
    
    public ServerPlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }
//
//    private boolean flipped = false;
//
//    private int gravFlipTime;

    // inject into playerTick instead
//    @Inject(method = "tick", at = @At("TAIL"))
//    private void tick(CallbackInfo ci) {
//        if (flipped) {
//            gravFlipTime++;
//            if (gravFlipTime > 20) {
//                flipped = false;
//                this.fallDistance = 0;
//            }
//            if (!this.hasNoGravity()) {
//                Vec3d antiGravity = new Vec3d(0, 0.12D, 0);
//                this.setVelocity(this.getVelocity().add(antiGravity));
//            }
//        }
//    }
}
