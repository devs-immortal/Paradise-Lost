package com.aether.mixin.client.render;

import com.aether.client.rendering.entity.layer.AetherModelLayers;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModels;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;

@Mixin(EntityModels.class)
public class EntityModelsMixin {
    @Inject(method = "getModels", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableMap$Builder;build()Lcom/google/common/collect/ImmutableMap;"), locals = LocalCapture.CAPTURE_FAILEXCEPTION, remap = false)
    private static void registerExtraModelData(CallbackInfoReturnable<Map<EntityModelLayer, TexturedModelData>> info, ImmutableMap.Builder<EntityModelLayer, TexturedModelData> builder) {
        for (Map.Entry<EntityModelLayer, TexturedModelData> entry : AetherModelLayers.ENTRIES.entrySet()) {
            builder.put(entry.getKey(), entry.getValue());
        }
    }
}
