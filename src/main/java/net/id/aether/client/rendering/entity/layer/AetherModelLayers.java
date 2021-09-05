package net.id.aether.client.rendering.entity.layer;

import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.mixin.client.rendering.EntityModelLayersAccessor;
import net.id.aether.Aether;
import net.id.aether.client.model.armor.PhoenixArmorModel;
import net.id.aether.client.model.entity.*;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

import java.util.Map;

@Environment(EnvType.CLIENT)
public class AetherModelLayers {
    public static final Map<EntityModelLayer, TexturedModelData> ENTRIES = Maps.newHashMap();

    public static final EntityModelLayer AECHOR_PLANT = register("aechor_plant", "main", AechorPlantModel.getTexturedModelData());
    public static final EntityModelLayer AERBUNNY = register("aerbunny", "main", AerbunnyModel.getTexturedModelData());
    public static final EntityModelLayer AERWHALE = register("aerwhale", "main", AerwhaleModel.getTexturedModelData());
    public static final EntityModelLayer COCKATRICE = register("cockatrice", "main", CockatriceModel.getTexturedModelData());
    public static final EntityModelLayer MIMIC = register("mimic", "main", ChestMimicModel.getTexturedModelData());
    public static final EntityModelLayer MOA = register("moa", "main", MoaModel.getTexturedModelData());
    public static final EntityModelLayer PHOENIX_ARMOR = register("phoenix_armor", "main", PhoenixArmorModel.getTexturedModelData());

    public static EntityModelLayer register(Identifier id, String layer, TexturedModelData data) {
        EntityModelLayer entityModelLayer = new EntityModelLayer(id, layer);
        if (!EntityModelLayersAccessor.getLayers().add(entityModelLayer)) {
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
