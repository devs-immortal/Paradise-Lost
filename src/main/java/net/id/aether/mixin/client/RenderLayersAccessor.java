package net.id.aether.mixin.client;

import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.fluid.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(RenderLayers.class)
public interface RenderLayersAccessor {
    @Accessor
    static Map<Block, RenderLayer> getBLOCKS() {
        throw new AssertionError();
    }

    @Accessor
    static Map<Fluid, RenderLayer> getFLUIDS() {
        throw new AssertionError();
    }
}
