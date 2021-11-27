package net.id.aether.client.model.armor;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.id.aether.client.rendering.armor.PhoenixArmorRenderer;
import net.id.aether.items.AetherItems;

@Environment(EnvType.CLIENT)
public class AetherArmorModels {
    public static void initClient() {
        ArmorRenderer.register(new PhoenixArmorRenderer(), AetherItems.PHOENIX_HELMET);
    }
}
