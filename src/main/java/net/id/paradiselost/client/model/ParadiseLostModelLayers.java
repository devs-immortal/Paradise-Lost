package net.id.paradiselost.client.model;

import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.mixin.client.rendering.EntityModelLayersAccessor;
import net.id.paradiselost.ParadiseLost;
import net.id.paradiselost.client.model.armor.OrnateOlviteArmorModel;
import net.id.paradiselost.client.model.entity.*;
import net.id.paradiselost.client.rendering.block.PalaceDoorBlockEntityRenderer;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

import java.util.Map;

@Environment(EnvType.CLIENT)
public class ParadiseLostModelLayers {

    public static final TexturedModelData BIPED_MODEL_DATA = TexturedModelData.of(BipedEntityModel.getModelData(Dilation.NONE, 0.0F), 64, 64);
    public static final TexturedModelData INNER_ARMOR_MODEL_DATA = TexturedModelData.of(BipedEntityModel.getModelData(new Dilation(0.5F), 0.0F), 64, 32);
    public static final TexturedModelData OUTER_ARMOR_MODEL_DATA = TexturedModelData.of(BipedEntityModel.getModelData(new Dilation(1.0F), 0.0F), 64, 32);

    public static final Map<EntityModelLayer, TexturedModelData> ENTRIES = Maps.newHashMap();

    public static final EntityModelLayer ENVOY = register("envoy", "main", EnvoyEntityModel.getTexturedModelData());
    public static final EntityModelLayer ENVOY_INNER_ARMOR = register("envoy", "inner_armor", INNER_ARMOR_MODEL_DATA);
    public static final EntityModelLayer ENVOY_OUTER_ARMOR = register("envoy", "outer_armor", OUTER_ARMOR_MODEL_DATA);
    public static final EntityModelLayer SENTINEL = register("sentinel", "main", BIPED_MODEL_DATA);
    public static final EntityModelLayer SENTINEL_INNER_ARMOR = register("sentinel", "inner_armor", INNER_ARMOR_MODEL_DATA);
    public static final EntityModelLayer SENTINEL_OUTER_ARMOR = register("sentinel", "outer_armor", OUTER_ARMOR_MODEL_DATA);
    public static final EntityModelLayer QUINT = register("quint", "main", QuintEntityModel.getTexturedModelData());
    public static final EntityModelLayer MOA = register("moa", "main", MoaModel.getTexturedModelData());
    public static final EntityModelLayer POPOM = register("popom", "main", PopomEntityModel.getTexturedModelData());

    public static final EntityModelLayer ORNATE_OLVITE_ARMOR = register("ornate_olvite", "main", OrnateOlviteArmorModel.getTexturedModelData());

    public static final EntityModelLayer PALACE_DOOR = register("palace_door", "main", PalaceDoorBlockEntityRenderer.getTexturedModelData());


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
        return register(ParadiseLost.locate(id), layer, data);
    }

    public static void initClient() {
        // Endless void.
    }
}
