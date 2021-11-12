package net.id.aether.mixin.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexFormat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import static net.id.aether.util.MiscUtil.dummyObject;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
@Mixin(RenderLayer.class)
public interface RenderLayerAccessor{
    @Invoker static RenderLayer.MultiPhase invokeOf(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, RenderLayer.MultiPhaseParameters phases){return dummyObject();}
}
