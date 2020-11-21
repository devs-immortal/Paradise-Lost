package com.aether.mixin.client;

import com.aether.world.dimension.AetherDimension;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.util.math.Vec3d;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {
    @Redirect(method = "render", at = @At(value = "FIELD", target = "Lnet/minecraft/util/math/Vec3d;y:D", opcode = Opcodes.GETFIELD, ordinal = 1))
    private static double adjustVoidVector(Vec3d vec3d) {
        return MinecraftClient.getInstance().world != null && MinecraftClient.getInstance().world.getRegistryKey() == AetherDimension.AETHER_WORLD_KEY ? Double.MAX_VALUE : vec3d.y;
    }
}
