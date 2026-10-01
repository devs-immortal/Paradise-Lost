package net.id.paradise_lost.client.model;

import net.id.paradise_lost.ModConstants;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.event.ModelEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class CalciteFlowerPotModel implements BakedModel {

    private static final ResourceLocation POT_TEXTURE = ModConstants.id("block/calcite_flower_pot");
    private static final ResourceLocation SOIL_TEXTURE = ModConstants.id("block/dirt");
    private static final ResourceLocation VANILLA_POT_TEXTURE = ResourceLocation.withDefaultNamespace("block/flower_pot");
    private static final ResourceLocation VANILLA_SOIL_TEXTURE = ResourceLocation.withDefaultNamespace("block/dirt");

    private static final int STRIDE = 8;

    private static final int UV = 4;

    private final BakedModel wrapped;
    private final TextureAtlasSprite pot;
    private final TextureAtlasSprite soil;

    private CalciteFlowerPotModel(BakedModel wrapped, TextureAtlasSprite pot, TextureAtlasSprite soil) {
        this.wrapped = wrapped;
        this.pot = pot;
        this.soil = soil;
    }

    public static void onModifyBakingResult(ModelEvent.ModifyBakingResult event) {
        Function<Material, TextureAtlasSprite> textures = event.getTextureGetter();
        TextureAtlasSprite pot = textures.apply(new Material(TextureAtlas.LOCATION_BLOCKS, POT_TEXTURE));
        TextureAtlasSprite soil = textures.apply(new Material(TextureAtlas.LOCATION_BLOCKS, SOIL_TEXTURE));

        event.getModels().replaceAll((location, model) ->
                wantsCalcite(location.getVariant())
                        ? new CalciteFlowerPotModel(model, pot, soil)
                        : model);
    }

    private static boolean wantsCalcite(String variant) {
        for (String property : variant.split(",")) {
            if ("calcite=true".equals(property)) {
                return true;
            }
        }
        return false;
    }

    private static BakedQuad retexture(BakedQuad quad, TextureAtlasSprite replacement) {
        TextureAtlasSprite source = quad.getSprite();
        float u0 = source.getU0();
        float v0 = source.getV0();
        float du = source.getU1() - u0;
        float dv = source.getV1() - v0;

        int[] vertices = quad.getVertices().clone();
        for (int base = 0; base + UV + 1 < vertices.length; base += STRIDE) {
            float u = (Float.intBitsToFloat(vertices[base + UV]) - u0) / du;
            float v = (Float.intBitsToFloat(vertices[base + UV + 1]) - v0) / dv;
            vertices[base + UV] = Float.floatToRawIntBits(replacement.getU(u));
            vertices[base + UV + 1] = Float.floatToRawIntBits(replacement.getV(v));
        }

        return new BakedQuad(vertices, quad.getTintIndex(), quad.getDirection(), replacement,
                quad.isShade(), quad.hasAmbientOcclusion());
    }

    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource rand) {
        List<BakedQuad> quads = this.wrapped.getQuads(state, side, rand);
        List<BakedQuad> out = null;

        for (int i = 0; i < quads.size(); i++) {
            BakedQuad quad = quads.get(i);
            ResourceLocation sprite = quad.getSprite().contents().name();
            BakedQuad replacement = null;

            if (VANILLA_POT_TEXTURE.equals(sprite)) {
                replacement = retexture(quad, this.pot);
            } else if (VANILLA_SOIL_TEXTURE.equals(sprite)) {
                replacement = retexture(quad, this.soil);
            }

            if (replacement != null) {
                if (out == null) {
                    out = new ArrayList<>(quads);
                }
                out.set(i, replacement);
            }
        }

        return out == null ? quads : out;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return this.wrapped.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return this.wrapped.isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return this.wrapped.usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer() {
        return this.wrapped.isCustomRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return this.wrapped.getParticleIcon();
    }

    @Override
    public ItemTransforms getTransforms() {
        return this.wrapped.getTransforms();
    }

    @Override
    public ItemOverrides getOverrides() {
        return this.wrapped.getOverrides();
    }
}
