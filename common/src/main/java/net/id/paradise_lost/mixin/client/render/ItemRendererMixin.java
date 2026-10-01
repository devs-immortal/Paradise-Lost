package net.id.paradise_lost.mixin.client.render;

import net.id.paradise_lost.registry.ItemRegistry;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.id.paradise_lost.ModConstants.id;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @Unique
    private static final ModelResourceLocation OLVITE_SPYGLASS = ModelResourceLocation.inventory(id("olvite_spyglass"));
    @Unique
    private static final ModelResourceLocation OLVITE_SPYGLASS_IN_HAND = ModelResourceLocation.inventory(id("olvite_spyglass_in_hand"));

    @Final
    @Shadow
    private ItemModelShaper itemModelShaper;

    @Shadow
    public static VertexConsumer getFoilBufferDirect(MultiBufferSource provider, RenderType layer, boolean solid, boolean glint) {
        return null;
    }

    @Shadow
    protected abstract void renderModelLists(BakedModel model, ItemStack stack, int light, int overlay, PoseStack matrices, VertexConsumer vertices);

    @Inject(method = "render", at = @At(value = "HEAD"), cancellable = true)
    public void renderItem(
            ItemStack stack,
            ItemDisplayContext renderMode,
            boolean leftHanded,
            PoseStack matrices,
            MultiBufferSource vertexConsumers,
            int light,
            int overlay,
            BakedModel model,
            CallbackInfo ci
    ) {
        if (!stack.isEmpty()) {
            boolean bl = renderMode == ItemDisplayContext.GUI || renderMode == ItemDisplayContext.GROUND || renderMode == ItemDisplayContext.FIXED;
            if (bl && stack.is(ItemRegistry.OLVITE_SPYGLASS.get())) {
                matrices.pushPose();
                model = this.itemModelShaper.getModelManager().getModel(OLVITE_SPYGLASS);
                model.getTransforms().getTransform(renderMode).apply(leftHanded, matrices);
                matrices.translate(-0.5F, -0.5F, -0.5F);

                RenderType renderLayer = ItemBlockRenderTypes.getRenderType(stack, true);
                VertexConsumer vertexConsumer;
                vertexConsumer = getFoilBufferDirect(vertexConsumers, renderLayer, true, stack.hasFoil());

                this.renderModelLists(model, stack, light, overlay, matrices, vertexConsumer);

                matrices.popPose();
                ci.cancel();
            }
        }
    }

    @Inject(method = "getModel", at = @At(value = "HEAD"), cancellable = true)
    public void getModel(ItemStack stack, Level world, LivingEntity entity, int seed, CallbackInfoReturnable<BakedModel> cir) {
        if (stack.is(ItemRegistry.OLVITE_SPYGLASS.get())) {
            BakedModel bakedModel = this.itemModelShaper.getModelManager().getModel(OLVITE_SPYGLASS_IN_HAND);
            ClientLevel clientWorld = world instanceof ClientLevel ? (ClientLevel) world : null;
            BakedModel bakedModel2 = bakedModel.getOverrides().resolve(bakedModel, stack, clientWorld, entity, seed);
            cir.setReturnValue(bakedModel2 == null ? this.itemModelShaper.getModelManager().getMissingModel() : bakedModel2);
        }
    }

}
