package net.id.aether.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.id.aether.mixin.util.SignTypeAccessor;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.SignType;

public class AetherSignType extends SignType {
    public static final SignType SKYROOT = register(new AetherSignType("aether_skyroot"));
    public static final SignType ORANGE = register(new AetherSignType("aether_orange"));
    public static final SignType WISTERIA = register(new AetherSignType("aether_wisteria"));
    public static final SignType GOLDEN_OAK = register(new AetherSignType("aether_golden_oak"));
    public static final SignType CRYSTAL = register(new AetherSignType("aether_crystal"));

    protected AetherSignType(String name) {
        super(name);
    }

    private static SignType register(AetherSignType type) {
        SignType sign = SignTypeAccessor.callRegister(type);
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            TexturedRenderLayersPutter.put(sign, new Identifier("entity/signs/" + sign.getName()));
        }

        return sign;
    }
}

// TODO this is a silly solution.
// This should be changed later, but it makes it so that the server does not crash.
// There's no downsides to doing this, other than being a bit hard to read.
class TexturedRenderLayersPutter {
    @Environment(EnvType.CLIENT)
    public static void put(SignType type, Identifier sprite) {
        TexturedRenderLayers.WOOD_TYPE_TEXTURES.put(type, new SpriteIdentifier(TexturedRenderLayers.SIGNS_ATLAS_TEXTURE, sprite));
    }
}
