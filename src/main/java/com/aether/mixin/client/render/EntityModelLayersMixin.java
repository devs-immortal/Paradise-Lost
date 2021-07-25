package com.aether.mixin.client.render;

import com.aether.util.AetherSignType;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.util.Identifier;
import net.minecraft.util.SignType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityModelLayers.class)
public class EntityModelLayersMixin {
//    @Inject(method = "createSign", at = @At("HEAD"), cancellable = true)
//    private static void createAetherSign(SignType type, CallbackInfoReturnable<EntityModelLayer> cir){
//        if(type instanceof AetherSignType) {
//            cir.setReturnValue(new EntityModelLayer(new Identifier("the_aether", "sign/" + type.getName()), "main"));
//        }
//    }
}
