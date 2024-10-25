package net.id.paradiselost.mixin.client.render;

import net.id.paradiselost.items.ParadiseLostItems;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.id.paradiselost.ParadiseLost.locate;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @Unique
    private static final ModelIdentifier OLVITE_SPYGLASS = ModelIdentifier.ofInventoryVariant(locate("olvite_spyglass"));
    @Unique
    private static final ModelIdentifier OLVITE_SPYGLASS_IN_HAND = ModelIdentifier.ofInventoryVariant(locate("olvite_spyglass_in_hand"));

    @Final
    @Shadow
    private ItemModels models;

    @Shadow
    public static VertexConsumer getDirectItemGlintConsumer(VertexConsumerProvider provider, RenderLayer layer, boolean solid, boolean glint) {
        return null;
    }

    @Shadow
    protected abstract void renderBakedItemModel(BakedModel model, ItemStack stack, int light, int overlay, MatrixStack matrices, VertexConsumer vertices);

    @Inject(method = "renderItem", at = @At(value = "HEAD"), cancellable = true)
    public void renderItem(
            ItemStack stack,
            ModelTransformationMode renderMode,
            boolean leftHanded,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            int overlay,
            BakedModel model,
            CallbackInfo ci
    ) {
        if (!stack.isEmpty()) {
            boolean bl = renderMode == ModelTransformationMode.GUI || renderMode == ModelTransformationMode.GROUND || renderMode == ModelTransformationMode.FIXED;
            if (bl && stack.isOf(ParadiseLostItems.OLVITE_SPYGLASS)) {
                matrices.push();
                model = this.models.getModelManager().getModel(OLVITE_SPYGLASS);
                model.getTransformation().getTransformation(renderMode).apply(leftHanded, matrices);
                matrices.translate(-0.5F, -0.5F, -0.5F);

                RenderLayer renderLayer = RenderLayers.getItemLayer(stack, true);
                VertexConsumer vertexConsumer;
                vertexConsumer = getDirectItemGlintConsumer(vertexConsumers, renderLayer, true, stack.hasGlint());

                this.renderBakedItemModel(model, stack, light, overlay, matrices, vertexConsumer);

                matrices.pop();
                ci.cancel();
            }
        }
    }

    @Inject(method = "getModel", at = @At(value = "HEAD"), cancellable = true)
    public void getModel(ItemStack stack, World world, LivingEntity entity, int seed, CallbackInfoReturnable<BakedModel> cir) {
        if (stack.isOf(ParadiseLostItems.OLVITE_SPYGLASS)) {
            BakedModel bakedModel = this.models.getModelManager().getModel(OLVITE_SPYGLASS_IN_HAND);
            ClientWorld clientWorld = world instanceof ClientWorld ? (ClientWorld)world : null;
            BakedModel bakedModel2 = bakedModel.getOverrides().apply(bakedModel, stack, clientWorld, entity, seed);
            cir.setReturnValue(bakedModel2 == null ? this.models.getModelManager().getMissingModel() : bakedModel2);
        }
    }

}
