package net.id.paradise_lost.mixin.client.render;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.id.paradise_lost.item.armor.FloatyLeggingsItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorMaterial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(HumanoidArmorLayer.class)
public class HumanoidArmorLayerMixin {

    @WrapOperation(
            method = "renderArmorPiece",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ArmorMaterial$Layer;texture(Z)Lnet/minecraft/resources/ResourceLocation;"
            )
    )
    private ResourceLocation paradiseLost$floatyAnchoredTexture(
            ArmorMaterial.Layer layer,
            boolean secondLayer,
            Operation<ResourceLocation> original,
            PoseStack poseStack,
            MultiBufferSource buffer,
            LivingEntity entity,
            EquipmentSlot slot,
            int light,
            HumanoidModel<?> model
    ) {
        ResourceLocation anchored = FloatyLeggingsItem.anchoredTexture(entity.getItemBySlot(slot), entity, secondLayer);
        return anchored != null ? anchored : original.call(layer, secondLayer);
    }
}
