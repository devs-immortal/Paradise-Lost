package net.id.aether.client.model.armor;

import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.id.aether.client.rendering.armor.PhoenixArmorRenderer;
import net.id.aether.items.AetherItems;

public class AetherArmorModels {

    public static void registerArmorModels() {
        ArmorRenderer.register(new PhoenixArmorRenderer(), AetherItems.PHOENIX_HELMET);
    }
}
