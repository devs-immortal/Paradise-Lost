package net.id.aether.client.rendering.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.util.Identifier;

import static net.id.aether.Aether.locate;

@Environment(EnvType.CLIENT)
public final class AetherTextures{
    private AetherTextures(){}
    
    public static void init(){
        ClientSpriteRegistryCallback.event(new Identifier("textures/atlas/mob_effects.png")).register((atlasTexture, registry) -> {
            registry.register(locate("hud/bloodstone/affinity"));
            registry.register(locate("hud/bloodstone/race"));
        });
    }
}
