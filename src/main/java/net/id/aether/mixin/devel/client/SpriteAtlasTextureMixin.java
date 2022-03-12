package net.id.aether.mixin.devel.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.aether.Aether;
import net.id.aether.devel.AetherDevel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Queue;

@Environment(EnvType.CLIENT)
@Mixin(SpriteAtlasTexture.class)
public abstract class SpriteAtlasTextureMixin{
    @Inject(
        method = "method_18160",
        at = @At(
            value = "INVOKE",
            target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V",
            ordinal = 0
        )
    )
    private void loadSprites$badMeta(Identifier identifier, ResourceManager resourceManager, Queue<Sprite.Info> queue, CallbackInfo ci){
        if(identifier.getNamespace().equals(Aether.MOD_ID)){
            AetherDevel.Client.logBadTexture(identifier);
        }
    }
    
    @Inject(
        method = "method_18160",
        at = @At(
            value = "INVOKE",
            target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V",
            ordinal = 1
        )
    )
    private void loadSprites$notFound(Identifier identifier, ResourceManager resourceManager, Queue<Sprite.Info> queue, CallbackInfo ci){
        if(identifier.getNamespace().equals(Aether.MOD_ID)){
            AetherDevel.Client.logMissingTexture(identifier);
        }
    }
    
    @Inject(
        method = "loadSprite",
        at = @At(
            value = "INVOKE",
            target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V",
            ordinal = 0
        )
    )
    private void loadSprite$badMeta(ResourceManager container, Sprite.Info info, int atlasWidth, int atlasHeight, int maxLevel, int x, int y, CallbackInfoReturnable<Sprite> cir){
        var identifier = info.getId();
        if(identifier.getNamespace().equals(Aether.MOD_ID)){
            AetherDevel.Client.logBadTexture(identifier);
        }
    }
    
    @Inject(
        method = "loadSprite",
        at = @At(
            value = "INVOKE",
            target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V",
            ordinal = 1
        )
    )
    private void loadSprite$notFound(ResourceManager container, Sprite.Info info, int atlasWidth, int atlasHeight, int maxLevel, int x, int y, CallbackInfoReturnable<Sprite> cir){
        var identifier = info.getId();
        if(identifier.getNamespace().equals(Aether.MOD_ID)){
            AetherDevel.Client.logMissingTexture(identifier);
        }
    }
}
