package net.id.paradiselost.client.rendering.texture;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

import static net.id.paradiselost.ParadiseLost.locate;

@Environment(EnvType.CLIENT)
public final class ParadiseLostTextures {
    private ParadiseLostTextures(){}
    
    public static void initClient(){
        ClientSpriteRegistryCallback.event(new Identifier("textures/atlas/mob_effects.png")).register((atlasTexture, registry) -> {
            registry.register(locate("hud/bloodstone/affinity"));
            registry.register(locate("hud/bloodstone/race"));
        });
    }
}
