package net.id.paradiselost.client.model;

import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.mixin.client.rendering.EntityModelLayersAccessor;
import net.id.paradiselost.ParadiseLost;
import net.id.paradiselost.client.model.armor.PhoenixArmorModel;
import net.id.paradiselost.client.model.block.DungeonSwitchModel;
import net.id.paradiselost.client.model.entity.*;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

import java.util.Map;

@Environment(EnvType.CLIENT)
public class ParadiseLostModelLayers {
    public static final Map<EntityModelLayer, TexturedModelData> ENTRIES = Maps.newHashMap();

    public static final EntityModelLayer HELLENROSE = register("hellenrose", "main", ParadiseLostPlantModel.getTexturedModelData());
    public static final EntityModelLayer PARADISE_HARE = register("corsican_hare", "main", ParadiseHareModel.getTexturedModelData());
    public static final EntityModelLayer MOA = register("moa", "main", MoaModel.getTexturedModelData());
    public static final EntityModelLayer AMBYST = register("ambyst", "main", AmbystModel.getTexturedModelData());
    public static final EntityModelLayer PHOENIX_ARMOR = register("phoenix_armor", "main", PhoenixArmorModel.getTexturedModelData());

    public static final EntityModelLayer DUNGEON_SWITCH = register("dungeon_switch", "main", DungeonSwitchModel.getTexturedModelData());

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
