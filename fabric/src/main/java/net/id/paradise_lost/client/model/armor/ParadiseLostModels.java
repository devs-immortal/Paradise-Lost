package net.id.paradise_lost.client.model.armor;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.id.paradise_lost.client.model.ModifiedFlowerPotModel;
import net.id.paradise_lost.client.rendering.armor.OrnateOlviteArmorRenderer;
import net.id.paradise_lost.registry.ItemRegistry;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class ParadiseLostModels {
    public static void init() {
        ModelLoadingPlugin.register(pluginContext -> {
            pluginContext.modifyModelAfterBake().register(ModelModifier.OVERRIDE_PHASE, (model, context) -> {
                ResourceLocation id = context.resourceId();
                if (id != null && id.toString().contains("potted")) {
                    return new ModifiedFlowerPotModel(model);
                }
                return model;
            });
        });
        ArmorRenderer.register(new OrnateOlviteArmorRenderer(), ItemRegistry.OLVITE_HELMET_ORNATE.get());
    }
}
