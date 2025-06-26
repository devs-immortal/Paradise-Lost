package net.id.paradiselost.client.model;

import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.material.ShadeMode;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.ModelHelper;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.id.paradiselost.ParadiseLost;
import net.id.paradiselost.blocks.decorative.CalciteFlowerPotBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;

import java.util.function.Supplier;

public class ModifiedFlowerPotModel extends ForwardingBakedModel {

    private static final Identifier SOIL_TEXTURE = ParadiseLost.locate("block/dirt");
    private static final Identifier POT_TEXTURE = ParadiseLost.locate("block/calcite_flower_pot");

    private static final RenderMaterial STANDARD_MATERIAL = RendererAccess.INSTANCE.getRenderer().materialFinder().shadeMode(ShadeMode.VANILLA).find();

    public ModifiedFlowerPotModel(BakedModel model) {
        wrapped = model;
    }

    @Override
    public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
        if (state.getBlock() instanceof FlowerPotBlock) {
            QuadEmitter emitter = context.getEmitter();

            var atlas = MinecraftClient.getInstance().getSpriteAtlas(Identifier.of("textures/atlas/blocks.png"));
            var shouldReplace = state.get(CalciteFlowerPotBlock.IS_CALCITE);

            for (int i = 0; i <= ModelHelper.NULL_FACE_ID; i++) {
                final Direction cullFace = ModelHelper.faceFromIndex(i);

                for (BakedQuad q : this.getQuads(state, cullFace, randomSupplier.get())) {
                    emitter.fromVanilla(q, STANDARD_MATERIAL, cullFace);
                    if (shouldReplace && q.getSprite().getContents().getId().equals(Identifier.ofVanilla("block/flower_pot"))) {
                        emitter.spriteBake(atlas.apply(POT_TEXTURE), MutableQuadView.BAKE_LOCK_UV);
                    }
                    if (shouldReplace && q.getSprite().getContents().getId().equals(Identifier.ofVanilla("block/dirt"))) {
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
