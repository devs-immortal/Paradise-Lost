package com.aether.mixin.client.render;

import net.minecraft.world.level.newbiome.area.LazyArea;
import net.minecraft.world.level.newbiome.layer.Layer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Layer.class)
public interface BiomeLayerSamplerAccessor {
    @Accessor("area")
    LazyArea getArea();
}