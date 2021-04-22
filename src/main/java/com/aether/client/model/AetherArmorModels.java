package com.aether.client.model;

import com.aether.client.model.armor.PhoenixArmorModel;
import com.aether.items.AetherItems;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderingRegistry;
import net.minecraft.util.Identifier;

public class AetherArmorModels {

    public static void registerArmorModels() {
        ArmorRenderingRegistry.registerModel((entity, stack, slot, defaultModel) -> new PhoenixArmorModel(1), AetherItems.PHOENIX_HELMET);
        ArmorRenderingRegistry.registerTexture((entity, stack, slot, secondLayer, suffix, defaultTexture) -> new Identifier("minecraft", "textures/models/armor/phoenix_layer_1.png"), AetherItems.PHOENIX_HELMET);
    }
}
