package com.aether.mixin.entity;

import com.aether.items.AetherItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CowEntity.class)
public class CowEntityMixin extends AnimalEntity {

    protected CowEntityMixin(EntityType<? extends AnimalEntity> entityType, World world){
        super(entityType, world);
    }

    @Inject(at = @At("HEAD"), method = "interactMob", cancellable = true)
    public void interactMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.getItem() == AetherItems.SKYROOT_BUCKET && !this.isBaby()) {
            player.playSound(SoundEvents.ENTITY_COW_MILK, 1.0F, 1.0F);
            ItemStack itemStack2 = ItemUsage.method_30012(itemStack, player, AetherItems.SKYROOT_MILK_BUCKET.getDefaultStack());
            player.setStackInHand(hand, itemStack2);
            cir.setReturnValue(ActionResult.success(this.world.isClient));
        }
    }

    @Shadow
    public CowEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }
}
