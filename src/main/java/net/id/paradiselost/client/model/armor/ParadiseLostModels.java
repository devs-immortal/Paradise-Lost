package net.id.paradiselost.client.model.armor;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier;
import net.id.paradiselost.client.model.ModifiedFlowerPotModel;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ParadiseLostModels {
    public static void initClient() {
        ModelLoadingPlugin.register(pluginContext -> {
            pluginContext.modifyModelAfterBake().register(ModelModifier.OVERRIDE_PHASE, (model, context) -> {
                Identifier id = context.resourceId();
                if (id != null && id.toString().contains("potted")) {
                    return new ModifiedFlowerPotModel(model);
                }
                return model;
            });
        });
        //ArmorRenderer.register(new PhoenixArmorRenderer(), ParadiseLostItems.PHOENIX_HELMET);
    }
}
