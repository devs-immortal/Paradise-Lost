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

    public static final EntityModelLayer AECHOR_PLANT = register("aechor_plant", "main", ParadiseLostPlantModel.getTexturedModelData());
    public static final EntityModelLayer AERBUNNY = register("aerbunny", "main", AerbunnyModel.getTexturedModelData());
    public static final EntityModelLayer AERWHALE = register("aerwhale", "main", AerwhaleModel.getTexturedModelData());
    public static final EntityModelLayer COCKATRICE = register("cockatrice", "main", CockatriceModel.getTexturedModelData());
    public static final EntityModelLayer MIMIC = register("mimic", "main", ChestMimicModel.getTexturedModelData());
    public static final EntityModelLayer MOA = register("moa", "main", MoaModel.getTexturedModelData());
    public static final EntityModelLayer ROOK = register("rook", "main", RookModel.getTexturedModelData());
    public static final EntityModelLayer ROOK_GLOW = register("rook_glow", "glow", RookModel.getTexturedModelData());
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
