package net.id.aether.mixin.client.render;

import net.id.aether.world.dimension.AetherDimension;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
    @Shadow @Nullable private ClientWorld world;
    
    /**
     * Disables the void fog in the Aether. Used to be a pair of redirects.
     *
     * @author gudenau
     */
    @ModifyVariable(
        method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/math/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V",
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/client/world/ClientWorld$Properties;getSkyDarknessHeight(Lnet/minecraft/world/HeightLimitView;)D",
            shift = At.Shift.BY,
            by = 3
        )
    )
    private double dontRenderVoid(double original){
        return world != null && world.getRegistryKey() == AetherDimension.AETHER_WORLD_KEY ? 0 : original;
    }
}
