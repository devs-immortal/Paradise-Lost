package net.id.paradise_lost.mixin.client.render;

import net.id.paradise_lost.util.RegistryUtil;
import net.id.paradise_lost.world.dimension.ParadiseLostDimension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.phys.Vec3;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FogRenderer.class)
public class BackgroundRendererMixin {
    @Shadow
    private static float fogRed;
    @Shadow
    private static float fogGreen;
    @Shadow
    private static float fogBlue;

    @Redirect(method = "setupColor", at = @At(value = "FIELD", target = "Lnet/minecraft/world/phys/Vec3;y:D", opcode = Opcodes.GETFIELD, ordinal = 1))
    private static double adjustVoidVector(Vec3 vec3d) {
        return RegistryUtil.dimensionMatches(Minecraft.getInstance().level, ParadiseLostDimension.DIMENSION_TYPE) ? Double.MAX_VALUE : vec3d.y;
    }

}
