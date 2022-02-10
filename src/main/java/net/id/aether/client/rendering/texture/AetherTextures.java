package net.id.aether.client.rendering.texture;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

import static net.id.aether.Aether.locate;

@Environment(EnvType.CLIENT)
public final class AetherTextures{
    private AetherTextures(){}
    
    public static void initClient(){
        ClientSpriteRegistryCallback.event(new Identifier("textures/atlas/mob_effects.png")).register((atlasTexture, registry) -> {
            registry.register(locate("hud/bloodstone/affinity"));
            registry.register(locate("hud/bloodstone/race"));
        });
    }
    
    public static void addDefaultTextures(Consumer<SpriteIdentifier> adder){
        for(var texture : AetherChestTexture.values()){
            texture.textures().forEach(adder);
        }
    }
}
