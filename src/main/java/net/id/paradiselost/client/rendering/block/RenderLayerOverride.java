package net.id.paradiselost.client.rendering.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;

// Workaround for some Mojank
@Environment(EnvType.CLIENT)
public interface RenderLayerOverride {
    RenderLayer getRenderLayerOverride(boolean fancy);
}
