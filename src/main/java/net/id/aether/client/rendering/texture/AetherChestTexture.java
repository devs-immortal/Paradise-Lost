package net.id.aether.client.rendering.texture;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.aether.util.Holiday;
import net.minecraft.client.util.SpriteIdentifier;

import java.util.Locale;
import java.util.Set;

import static net.id.aether.Aether.locate;
import static net.minecraft.client.render.TexturedRenderLayers.CHEST_ATLAS_TEXTURE;

@Environment(EnvType.CLIENT)
public enum AetherChestTexture{
    SKYROOT,
    GOLDEN_OAK,
    ORANGE,
    CRYSTAL,
    WISTERIA,
    ;
    
    public final SpriteIdentifier single;
    public final SpriteIdentifier left;
    public final SpriteIdentifier right;
    
    AetherChestTexture(){
        // return new SpriteIdentifier(CHEST_ATLAS_TEXTURE, new Identifier("entity/chest/" + variant));
        String name = name().toLowerCase(Locale.ROOT);
        // For now we only support Christmas chests.
        var prefix = (switch (Holiday.get()){
            case CHRISTMAS -> Holiday.CHRISTMAS;
            default -> Holiday.NONE;
        }).getName();
        single = new SpriteIdentifier(CHEST_ATLAS_TEXTURE, locate("entity/chest/" + name + '/' + prefix));
        left = new SpriteIdentifier(CHEST_ATLAS_TEXTURE, locate("entity/chest/" + name + '/' + prefix + "_left"));
        right = new SpriteIdentifier(CHEST_ATLAS_TEXTURE, locate("entity/chest/" + name + '/' + prefix + "_right"));
    }
    
    Set<SpriteIdentifier> textures(){
        return Set.of(single, left, right);
    }
    
    public SpriteIdentifier single(){
        return single;
    }
    
    public SpriteIdentifier left(){
        return left;
    }
    
    public SpriteIdentifier right(){
        return right;
    }
}
