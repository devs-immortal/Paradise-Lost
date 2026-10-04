package net.id.paradise_lost.client.model;

import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.material.ShadeMode;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.ModelHelper;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.block.decorative.CalciteFlowerPotBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockState;
import java.util.function.Supplier;

public class ModifiedFlowerPotModel extends ForwardingBakedModel {

    private static final ResourceLocation SOIL_TEXTURE = ModConstants.id("block/dirt");
    private static final ResourceLocation POT_TEXTURE = ModConstants.id("block/calcite_flower_pot");

    private static final RenderMaterial STANDARD_MATERIAL = RendererAccess.INSTANCE.getRenderer().materialFinder().shadeMode(ShadeMode.VANILLA).find();

    public ModifiedFlowerPotModel(BakedModel model) {
        wrapped = model;
    }

    @Override
    public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context) {
        if (state.getBlock() instanceof FlowerPotBlock) {
            QuadEmitter emitter = context.getEmitter();

            var atlas = Minecraft.getInstance().getTextureAtlas(ResourceLocation.parse("textures/atlas/blocks.png"));
            var shouldReplace = state.getValue(CalciteFlowerPotBlock.IS_CALCITE);

            for (int i = 0; i <= ModelHelper.NULL_FACE_ID; i++) {
                final Direction cullFace = ModelHelper.faceFromIndex(i);

                for (BakedQuad q : this.getQuads(state, cullFace, randomSupplier.get())) {
                    emitter.fromVanilla(q, STANDARD_MATERIAL, cullFace);
                    if (shouldReplace && q.getSprite().contents().name().equals(ResourceLocation.withDefaultNamespace("block/flower_pot"))) {
                        emitter.spriteBake(atlas.apply(POT_TEXTURE), MutableQuadView.BAKE_LOCK_UV);
                    }
                    if (shouldReplace && q.getSprite().contents().name().equals(ResourceLocation.withDefaultNamespace("block/dirt"))) {
                        emitter.spriteBake(atlas.apply(SOIL_TEXTURE), MutableQuadView.BAKE_LOCK_UV);
                    }
                    emitter.emit();
                }
            }
        } else {
            super.emitBlockQuads(blockView, state, pos, randomSupplier, context);
        }
    }
}
