package net.id.aether.client.rendering.shader;

import com.google.common.collect.ImmutableMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormatElement;

import static net.minecraft.client.render.VertexFormats.*;

@Environment(EnvType.CLIENT)
public final class AetherVertexFormats{
    private AetherVertexFormats(){}
    
    public static final VertexFormat POSITION_COLOR_LIGHT_NORMAL = new VertexFormat(ImmutableMap.<String, VertexFormatElement>builder()
        .put("Position", POSITION_ELEMENT)
        .put("Color", COLOR_ELEMENT)
        .put("UV2", LIGHT_ELEMENT)
        .put("Normal", NORMAL_ELEMENT)
        .put("Padding", PADDING_ELEMENT)
        .build()
    );
}
