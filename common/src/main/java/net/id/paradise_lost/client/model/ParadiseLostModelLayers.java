package net.id.paradise_lost.client.model;

import com.google.common.collect.Maps;
import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.client.model.armor.OrnateOlviteArmorModel;
import net.id.paradise_lost.client.model.entity.*;
import net.id.paradise_lost.client.rendering.block.PalaceDoorBlockEntityRenderer;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class ParadiseLostModelLayers {

    public static final LayerDefinition BIPED_MODEL_DATA = LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64);
    public static final LayerDefinition INNER_ARMOR_MODEL_DATA = LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(0.5F), 0.0F), 64, 32);
    public static final LayerDefinition OUTER_ARMOR_MODEL_DATA = LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(1.0F), 0.0F), 64, 32);

    public static final Map<ModelLayerLocation, LayerDefinition> ENTRIES = Maps.newHashMap();

    public static final ModelLayerLocation ENVOY = register("envoy", "main", EnvoyEntityModel.createBodyLayer());
    public static final ModelLayerLocation ENVOY_INNER_ARMOR = register("envoy", "inner_armor", INNER_ARMOR_MODEL_DATA);
    public static final ModelLayerLocation ENVOY_OUTER_ARMOR = register("envoy", "outer_armor", OUTER_ARMOR_MODEL_DATA);
    public static final ModelLayerLocation SENTINEL = register("sentinel", "main", BIPED_MODEL_DATA);
    public static final ModelLayerLocation SENTINEL_INNER_ARMOR = register("sentinel", "inner_armor", INNER_ARMOR_MODEL_DATA);
    public static final ModelLayerLocation SENTINEL_OUTER_ARMOR = register("sentinel", "outer_armor", OUTER_ARMOR_MODEL_DATA);
    public static final ModelLayerLocation QUINT = register("quint", "main", QuintEntityModel.getTexturedModelData());
    public static final ModelLayerLocation MOA = register("moa", "main", MoaModel.getTexturedModelData());
    public static final ModelLayerLocation POPOM = register("popom", "main", PopomEntityModel.getTexturedModelData());

    public static final ModelLayerLocation ORNATE_OLVITE_ARMOR = register("ornate_olvite", "main", OrnateOlviteArmorModel.getTexturedModelData());

    public static final ModelLayerLocation PALACE_DOOR = register("palace_door", "main", PalaceDoorBlockEntityRenderer.getTexturedModelData());

    public static final ModelLayerLocation AUREL_BOAT = register("boat/aurel", "main", BoatModel.createBoatModel());
    public static final ModelLayerLocation AUREL_CHEST_BOAT = register("chest_boat/aurel", "main", BoatModel.createChestBoatModel());
    public static final ModelLayerLocation MOTHER_AUREL_BOAT = register("boat/mother_aurel", "main", BoatModel.createBoatModel());
    public static final ModelLayerLocation MOTHER_AUREL_CHEST_BOAT = register("chest_boat/mother_aurel", "main", BoatModel.createChestBoatModel());
    public static final ModelLayerLocation MENTH_BOAT = register("boat/menth", "main", BoatModel.createBoatModel());
    public static final ModelLayerLocation MENTH_CHEST_BOAT = register("chest_boat/menth", "main", BoatModel.createChestBoatModel());
    public static final ModelLayerLocation WISTERIA_BOAT = register("boat/wisteria", "main", BoatModel.createBoatModel());
    public static final ModelLayerLocation WISTERIA_CHEST_BOAT = register("chest_boat/wisteria", "main", BoatModel.createChestBoatModel());

    public static ModelLayerLocation register(ResourceLocation id, String layer, LayerDefinition data) {
        ModelLayerLocation entityModelLayer = new ModelLayerLocation(id, layer);
        if (ENTRIES.put(entityModelLayer, data) != null) {
            throw new IllegalStateException("Duplicate registration for " + entityModelLayer);
        }
        return entityModelLayer;
    }

    public static ModelLayerLocation register(String id, String layer, LayerDefinition data) {
        return register(ModConstants.id(id), layer, data);
    }

    public static void initClient() {}
}
