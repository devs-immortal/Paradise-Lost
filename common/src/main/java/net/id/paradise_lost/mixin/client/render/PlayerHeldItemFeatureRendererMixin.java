package net.id.paradise_lost.mixin.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.id.paradise_lost.registry.ItemRegistry;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.PlayerItemInHandLayer;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerItemInHandLayer.class)
public abstract class PlayerHeldItemFeatureRendererMixin {

    @Inject(
            method = "renderArmWithItem(Lnet/minecraft/client/renderer/entity/state/PlayerRenderState;Lnet/minecraft/client/resources/model/BakedModel;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;Lnet/minecraft/world/entity/HumanoidArm;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At("HEAD"),
            cancellable = true
    )
    public void paradiseLost$renderOlviteSpyglass(PlayerRenderState state, BakedModel model, ItemStack stack, ItemDisplayContext displayContext, HumanoidArm arm, PoseStack matrices, MultiBufferSource vertexConsumers, int light, CallbackInfo ci) {
        InteractionHand hand = arm == state.mainArm ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
        if (model != null
                && stack.is(ItemRegistry.OLVITE_SPYGLASS.get())
                && state.isUsingItem
                && state.useItemHand == hand
                && state.attackTime < 1.0E-5F) {
            this.renderArmWithSpyglass(model, stack, arm, matrices, vertexConsumers, light);
            ci.cancel();
        }
    }

    @Shadow
    protected abstract void renderArmWithSpyglass(BakedModel model, ItemStack item, HumanoidArm arm, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight);
}
