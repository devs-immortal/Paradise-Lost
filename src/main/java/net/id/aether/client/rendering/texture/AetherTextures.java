package net.id.aether.client.rendering.texture;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

import static net.id.aether.Aether.locate;

@Environment(EnvType.CLIENT)
public final class AetherTextures{
    private AetherTextures(){}
    
    private static final Set<Identifier> DEFAULT_CUBE_MAPS = Set.of(
    
    );
    
    public static void init(){
        ClientSpriteRegistryCallback.event(new Identifier("textures/atlas/mob_effects.png")).register((atlasTexture, registry) -> {
            registry.register(locate("hud/bloodstone/affinity"));
            registry.register(locate("hud/bloodstone/race"));
        });
    
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener(){
            @Override
            public void reload(ResourceManager manager){
                var textureManager = MinecraftClient.getInstance().getTextureManager();
                for(Identifier identifier : DEFAULT_CUBE_MAPS){
                    var abstractTexture = textureManager.getOrDefault(identifier, null);
                    if(abstractTexture == null){
                        textureManager.registerTexture(identifier, new CubeMapTexture(identifier));
                    }else if(!(abstractTexture instanceof CubeMapTexture)){
                        throw new IllegalStateException("Cubemap texture is not a cubemap texutre: " + identifier);
                    }
                }
            }
    
            @Override
            public Identifier getFabricId(){
                return locate("cubemap_reloader");
            }
        });
    }
    
    public static void addDefaultTextures(Consumer<SpriteIdentifier> adder){
        for(var texture : AetherChestTexture.values()){
            texture.textures().forEach(adder);
        }
    }
    
    public static Set<CubeMapTexture> getCubeMaps(){
        return Set.of();
    }
}
