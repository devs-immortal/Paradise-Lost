package com.aether.mixin.client;

import com.aether.util.RegistryUtil;
import com.aether.world.dimension.AetherDimension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.phys.Vec3;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FogRenderer.class)
public class BackgroundRendererMixin {
    @Redirect(method = "render", at = @At(value = "FIELD", target = "Lnet/minecraft/util/math/Vec3d;y:D", opcode = Opcodes.GETFIELD, ordinal = 1))
    private static double adjustVoidVector(Vec3 vec3d) {
        return Minecraft.getInstance().level != null && Minecraft.getInstance().level.dimension() == AetherDimension.AETHER_WORLD_KEY ? Double.MAX_VALUE : vec3d.y;
    }

    // TODO: Migrate to shader events in 1.17
//    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/fluid/FluidState;isIn(Lnet/minecraft/tag/Tag;)Z"))
//    private static boolean dontChangeFogColor(FluidState fluidState, Tag<Fluid> tag) {
//        return (!RegistryUtil.dimensionMatches(MinecraftClient.getInstance().world, AetherDimension.TYPE)) && fluidState.isIn(tag);
//    }
//
//    @Inject(method = "applyFog", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;fogDensity(F)V"), cancellable = true)
//    private static void dontChangeFogDensity(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, CallbackInfo ci) {
//        if (RegistryUtil.dimensionMatches(MinecraftClient.getInstance().world, AetherDimension.TYPE)) {
//            ci.cancel();
//        }
//    }
}
