package net.id.paradiselost.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.paradiselost.mixin.util.SignTypeAccessor;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.SignType;

public class ParadiseLostSignType extends SignType {
    public static final SignType SKYROOT = register(new ParadiseLostSignType("paradise_lost_skyroot"));
    public static final SignType ORANGE = register(new ParadiseLostSignType("paradise_lost_orange"));
    public static final SignType WISTERIA = register(new ParadiseLostSignType("paradise_lost_wisteria"));
    public static final SignType GOLDEN_OAK = register(new ParadiseLostSignType("paradise_lost_golden_oak"));
    public static final SignType CRYSTAL = register(new ParadiseLostSignType("paradise_lost_crystal"));
    
    protected ParadiseLostSignType(String name) {
        super(name);
    }
    
    private static SignType register(ParadiseLostSignType type) {
        return SignTypeAccessor.callRegister(type);
    }
    
    @Environment(EnvType.CLIENT)
    public static void clientInit() {
        registerTexture(SKYROOT);
        registerTexture(ORANGE);
        registerTexture(WISTERIA);
        registerTexture(GOLDEN_OAK);
        registerTexture(CRYSTAL);
    }
    
    @Environment(EnvType.CLIENT)
    private static void registerTexture(SignType type) {
        TexturedRenderLayers.WOOD_TYPE_TEXTURES.put(
            type, new SpriteIdentifier(
                TexturedRenderLayers.SIGNS_ATLAS_TEXTURE, new Identifier("entity/signs/" + type.getName())
            )
        );
    }
}
