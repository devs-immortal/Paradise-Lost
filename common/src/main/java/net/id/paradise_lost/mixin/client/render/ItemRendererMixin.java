package net.id.paradise_lost.mixin.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.id.paradise_lost.registry.ItemRegistry;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.id.paradise_lost.ModConstants.id;

// The olvite spyglass item model is the in-hand one (see ItemRegistry); like the vanilla spyglass,
// GUI, ground and item frame rendering swap to the flat inventory model instead.
@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @Unique
    private static final ModelResourceLocation OLVITE_SPYGLASS = ModelResourceLocation.inventory(id("olvite_spyglass"));

    @Final
    @Shadow
    private ModelManager modelManager;

    @Shadow
    protected abstract void renderItemModelRaw(ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, BakedModel model, boolean renderOpenBundle, float z);

    @Inject(method = "renderSimpleItemModel", at = @At("HEAD"), cancellable = true)
    private void paradiseLost$renderFlatOlviteSpyglass(ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, BakedModel model, boolean renderFlat, CallbackInfo ci) {
        if (renderFlat && itemStack.is(ItemRegistry.OLVITE_SPYGLASS.get())) {
            this.renderItemModelRaw(itemStack, displayContext, leftHand, poseStack, bufferSource, packedLight, packedOverlay, this.modelManager.getModel(OLVITE_SPYGLASS), true, -0.5F);
            ci.cancel();
        }
    }
}
