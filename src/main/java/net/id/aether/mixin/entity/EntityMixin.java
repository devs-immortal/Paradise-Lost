package net.id.aether.mixin.entity;

import net.id.aether.blocks.AetherBlocks;
import net.id.aether.world.AetherGameRules;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Redirect(method = "getVelocityMultiplier", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getVelocityMultiplier()F"), require = 2)
    private float getVelocityMultiplier(Block target) {
        if (target == AetherBlocks.QUICKSOIL || target == AetherBlocks.QUICKSOIL_GLASS || target == AetherBlocks.QUICKSOIL_GLASS_PANE) {
            double maxSpeed = ((Entity) (Object) this).world.getGameRules().get(AetherGameRules.MAX_QUICKSOIL_SPEED).get();
            return (float) (1 + Math.max(
                    (maxSpeed - ((Entity) (Object) this).getVelocity().horizontalLength()) / maxSpeed * 0.102,
                    0));
        }
        return target.getVelocityMultiplier();
    }
}
