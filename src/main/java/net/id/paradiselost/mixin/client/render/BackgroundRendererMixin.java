package net.id.paradiselost.mixin.client.render;

import net.id.paradiselost.util.RegistryUtil;
import net.id.paradiselost.world.dimension.ParadiseLostDimension;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3d;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {
    @Redirect(
            method = "getFogColor",
            at = @At(value = "FIELD", target = "Lnet/minecraft/util/math/Vec3d;y:D", opcode = Opcodes.GETFIELD)
    )
    private static double paradise_lost$skipVoidDarkening(Vec3d pos, Camera camera, float tickDelta, ClientWorld world, int clampedViewDistance, float skyDarkness) {
        return RegistryUtil.dimensionMatches(world, ParadiseLostDimension.DIMENSION_TYPE) ? Double.MAX_VALUE : pos.y;
    }
}
