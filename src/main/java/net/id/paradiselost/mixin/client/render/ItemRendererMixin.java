package net.id.paradiselost.mixin.client.render;

import net.id.paradiselost.items.ParadiseLostItems;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
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
    private BakedModelManager bakedModelManager;

    @Shadow
    public static VertexConsumer getItemGlintConsumer(VertexConsumerProvider provider, RenderLayer layer, boolean solid, boolean glint) {
        return null;
    }

    @Shadow
    protected abstract void renderBakedItemModel(BakedModel model, ItemStack stack, int light, int overlay, MatrixStack matrices, VertexConsumer vertices);

    @Shadow
    protected abstract BakedModel getModelOrOverride(BakedModel model, ItemStack stack, @Nullable World world, @Nullable LivingEntity entity, int seed);

    @Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", at = @At(value = "HEAD"), cancellable = true)
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
                BakedModel spyglassModel = this.bakedModelManager.getModel(OLVITE_SPYGLASS);
                spyglassModel.getTransformation().getTransformation(renderMode).apply(leftHanded, matrices);
                matrices.translate(-0.5F, -0.5F, -0.5F);

                RenderLayer renderLayer = RenderLayers.getItemLayer(stack);
                VertexConsumer vertexConsumer;
                vertexConsumer = getItemGlintConsumer(vertexConsumers, renderLayer, true, stack.hasGlint());

                this.renderBakedItemModel(spyglassModel, stack, light, overlay, matrices, vertexConsumer);

                matrices.pop();
                ci.cancel();
            }
        }
    }

    @Inject(method = "getModel(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;I)Lnet/minecraft/client/render/model/BakedModel;", at = @At(value = "HEAD"), cancellable = true)
    public void getModel(ItemStack stack, World world, LivingEntity entity, int seed, CallbackInfoReturnable<BakedModel> cir) {
        if (stack.isOf(ParadiseLostItems.OLVITE_SPYGLASS)) {
            BakedModel bakedModel = this.bakedModelManager.getModel(OLVITE_SPYGLASS_IN_HAND);
            cir.setReturnValue(this.getModelOrOverride(bakedModel, stack, world, entity, seed));
        }
    }

}
