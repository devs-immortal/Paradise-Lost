package net.id.paradiselost.client.model.armor;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.id.paradiselost.client.rendering.armor.PhoenixArmorRenderer;
import net.id.paradiselost.items.ParadiseLostItems;

@Environment(EnvType.CLIENT)
public class ParadiseLostArmorModels {
    public static void initClient() {
        //ArmorRenderer.register(new PhoenixArmorRenderer(), ParadiseLostItems.PHOENIX_HELMET);
    }
}
