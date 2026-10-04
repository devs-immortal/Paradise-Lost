package net.id.paradise_lost.mixin.client.render;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.client.rendering.entity.state.FloatyAnchoredRenderState;
import net.id.paradise_lost.item.armor.FloatyLeggingsItem;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.EquipmentModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidArmorLayer.class)
public class HumanoidArmorLayerMixin {
    @Unique
    private static final ResourceLocation FLOATY_ANCHORED = ModConstants.id("floaty_anchored");

    @Unique
    private boolean paradiseLost$floatyAnchored;

    @Inject(
            method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/entity/state/HumanoidRenderState;FF)V",
            at = @At("HEAD")
    )
    private void paradiseLost$captureFloatyAnchored(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, HumanoidRenderState state, float yRot, float xRot, CallbackInfo ci) {
        this.paradiseLost$floatyAnchored = ((FloatyAnchoredRenderState) state).paradiseLost$isFloatyAnchored();
    }

    @WrapOperation(
            method = "renderArmorPiece",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/layers/EquipmentLayerRenderer;renderLayers(Lnet/minecraft/world/item/equipment/EquipmentModel$LayerType;Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/client/model/Model;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"
            )
    )
    private void paradiseLost$floatyAnchoredModel(
            EquipmentLayerRenderer renderer,
            EquipmentModel.LayerType layerType,
            ResourceLocation equipmentModel,
            Model armorModel,
            ItemStack item,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight,
            Operation<Void> original
    ) {
        if (this.paradiseLost$floatyAnchored
                && layerType == EquipmentModel.LayerType.HUMANOID_LEGGINGS
                && item.getItem() instanceof FloatyLeggingsItem) {
            equipmentModel = FLOATY_ANCHORED;
        }
        original.call(renderer, layerType, equipmentModel, armorModel, item, poseStack, bufferSource, packedLight);
    }
}
