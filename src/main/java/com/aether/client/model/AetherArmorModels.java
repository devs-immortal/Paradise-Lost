package com.aether.client.model;

import com.aether.client.rendering.armor.PhoenixArmorRenderer;
import com.aether.items.AetherItems;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;

public class AetherArmorModels {

    public static void registerArmorModels() {
        ArmorRenderer.register(new PhoenixArmorRenderer(), AetherItems.PHOENIX_HELMET);
    }
}
