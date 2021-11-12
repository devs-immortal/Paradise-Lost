package net.id.aether.mixin.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderPhase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import static net.id.aether.util.MiscUtil.dummyObject;

@Environment(EnvType.CLIENT)
@Mixin(RenderPhase.class)
public interface RenderPhaseAccessor{
    @Accessor("ENABLE_LIGHTMAP") static RenderPhase.Lightmap getEnableLightmap(){return dummyObject();}
    @Accessor("MIPMAP_BLOCK_ATLAS_TEXTURE") static RenderPhase.Texture getMipmapBlockAtlasTexture(){return dummyObject();}
}
