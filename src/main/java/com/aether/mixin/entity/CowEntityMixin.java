package com.aether.mixin.entity;

import com.aether.items.AetherItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Cow.class)
public class CowEntityMixin extends Animal {

    protected CowEntityMixin(EntityType<? extends Animal> entityType, Level world){
        super(entityType, world);
    }

    @Inject(at = @At("HEAD"), method = "mobInteract", cancellable = true)
    public void interactMob(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.getItem() == AetherItems.SKYROOT_BUCKET && !this.isBaby()) {
            player.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
            ItemStack itemStack2 = ItemUtils.createFilledResult(itemStack, player, AetherItems.SKYROOT_MILK_BUCKET.getDefaultInstance());
            player.setItemInHand(hand, itemStack2);
            cir.setReturnValue(InteractionResult.sidedSuccess(this.level.isClientSide));
        }
    }

    @Shadow
    public Cow getBreedOffspring(ServerLevel world, AgeableMob entity) {
        return null;
    }
}
