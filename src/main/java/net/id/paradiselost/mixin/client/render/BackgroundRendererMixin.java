package net.id.paradiselost.mixin.client.render;

import net.id.paradiselost.util.RegistryUtil;
import net.id.paradiselost.world.dimension.ParadiseLostDimension;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.util.math.Vec3d;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {
    @Shadow
    private static float red;
    @Shadow
    private static float green;
    @Shadow
    private static float blue;

    @Redirect(method = "render", at = @At(value = "FIELD", target = "Lnet/minecraft/util/math/Vec3d;y:D", opcode = Opcodes.GETFIELD, ordinal = 1))
    private static double adjustVoidVector(Vec3d vec3d) {
        return RegistryUtil.dimensionMatches(MinecraftClient.getInstance().world, ParadiseLostDimension.DIMENSION_TYPE) ? Double.MAX_VALUE : vec3d.y;
    }

}
