package com.aether.client.rendering.entity.layer;

import com.aether.Aether;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class AetherModelLayers {
    public static final EntityModelLayer AERBUNNY = register("aerbunny", "main");

    public static EntityModelLayer register(Identifier id, String layer) {
        EntityModelLayer entityModelLayer = new EntityModelLayer(id, layer);
        if (!EntityModelLayers.LAYERS.add(entityModelLayer)) {
            throw new IllegalStateException("Duplicate registration for " + entityModelLayer);
        } else {
            return entityModelLayer;
        }
    }

    public static EntityModelLayer register(String id, String layer) {
        return register(Aether.locate(id), layer);
    }

    public static void initClient() {
        // Endless void.
    }
}
