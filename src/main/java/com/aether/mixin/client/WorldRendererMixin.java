package com.aether.mixin.client;

import com.aether.world.dimension.AetherDimension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.phys.Vec3;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LevelRenderer.class)
public class WorldRendererMixin {
    @Redirect(method = "renderSky", at = @At(value = "FIELD", target = "Lnet/minecraft/util/math/Vec3d;y:D", opcode = Opcodes.GETFIELD, ordinal = 1))
    private double dontRenderVoid(Vec3 vec3d) {
        return Minecraft.getInstance().level != null && Minecraft.getInstance().level.dimension() == AetherDimension.AETHER_WORLD_KEY ? Double.MAX_VALUE : vec3d.y;
    }

    @Redirect(method = "renderSky", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld$Properties;getSkyDarknessHeight(Lnet/minecraft/world/HeightLimitView;)D"))
    private double dontRenderVoid(ClientLevel.ClientLevelData properties, LevelHeightAccessor world) {
        return Minecraft.getInstance().level != null && Minecraft.getInstance().level.dimension() == AetherDimension.AETHER_WORLD_KEY ? 0 : properties.getHorizonHeight(world);
    }
}
