package net.id.paradiselost.client.model.armor;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier;
import net.fabricmc.fabric.impl.client.rendering.ArmorRendererRegistryImpl;
import net.id.paradiselost.client.model.ModifiedFlowerPotModel;
import net.id.paradiselost.client.rendering.armor.OrnateOlviteArmorRenderer;
import net.id.paradiselost.items.ParadiseLostItems;
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
        ArmorRendererRegistryImpl.register(new OrnateOlviteArmorRenderer(), ParadiseLostItems.OLVITE_HELMET_ORNATE);
    }
}
