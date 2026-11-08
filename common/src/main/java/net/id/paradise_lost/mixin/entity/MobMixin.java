package net.id.paradise_lost.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.item.tool.base_tools.GravityTool;
import net.id.paradise_lost.registry.ItemRegistry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity {


    protected MobMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @WrapOperation(
            method = "interact(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Mob;mobInteract(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;"
            )
    )
    private InteractionResult yourmod$wrapMobInteract(
            Mob instance, Player player, InteractionHand hand,
            Operation<InteractionResult> original) {

        ItemStack stack = player.getItemInHand(hand);

        if (stack.getItem() == ItemRegistry.LEVITA_WAND.get()) {
            GravityTool.flipEntity(stack, player, this, hand);
            return InteractionResult.SUCCESS;
        } else {
            return original.call(instance, player, hand);
        }
    }





    /*@Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    public void wandGoFloat(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        ModConstants.LOGGER.info("wandGoFloat");
        GravityTool.flipEntity(player.getItemInHand(hand), player, this, hand);
    }*/

}