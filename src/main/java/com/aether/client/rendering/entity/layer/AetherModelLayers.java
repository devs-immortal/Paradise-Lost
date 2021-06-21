package com.aether.client.rendering.entity.layer;

import com.aether.Aether;
import com.aether.client.model.entity.AerbunnyModel;
import com.aether.client.model.entity.AerwhaleModel;
import com.aether.client.model.entity.MoaModel;
import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.util.Identifier;

import java.util.Map;

@Environment(EnvType.CLIENT)
public class AetherModelLayers {
    public static final Map<EntityModelLayer, TexturedModelData> ENTRIES = Maps.newHashMap();

    public static final EntityModelLayer AERBUNNY = register("aerbunny", "main", AerbunnyModel.getTexturedModelData());
    public static final EntityModelLayer AERWHALE = register("aerwhale", "main", AerwhaleModel.getTexturedModelData());
    public static final EntityModelLayer MOA = register("moa", "main", MoaModel.getTexturedModelData());

    public static EntityModelLayer register(Identifier id, String layer, TexturedModelData data) {
        EntityModelLayer entityModelLayer = new EntityModelLayer(id, layer);
        if (!EntityModelLayers.LAYERS.add(entityModelLayer)) {
            throw new IllegalStateException("Duplicate registration for " + entityModelLayer);
        } else {
            ENTRIES.put(entityModelLayer, data);
            return entityModelLayer;
        }
    }

    public static EntityModelLayer register(String id, String layer, TexturedModelData data) {
        return register(Aether.locate(id), layer, data);
    }

    public static void initClient() {
        // Endless void.
    }
}
