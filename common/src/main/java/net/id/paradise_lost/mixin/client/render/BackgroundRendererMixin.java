package net.id.paradise_lost.mixin.client.render;

import net.id.paradise_lost.util.RegistryUtil;
import net.id.paradise_lost.world.dimension.ParadiseLostDimension;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.phys.Vec3;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FogRenderer.class)
public class BackgroundRendererMixin {
    @Redirect(
            method = "computeFogColor",
            at = @At(value = "FIELD", target = "Lnet/minecraft/world/phys/Vec3;y:D", opcode = Opcodes.GETFIELD)
    )
    private static double paradiseLost$skipVoidDarkening(Vec3 pos, Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount) {
        return RegistryUtil.dimensionMatches(level, ParadiseLostDimension.DIMENSION_TYPE) ? Double.MAX_VALUE : pos.y;
    }
}
