package net.id.paradise_lost.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.id.paradise_lost.item.armor.FloatyLeggingsItem;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Player.class)
public class PlayerMiningMixin {
    @WrapOperation(
            method = "getDestroySpeed",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;onGround()Z")
    )
    private boolean paradiseLost$anchoredMiningIsGrounded(Player player, Operation<Boolean> onGround) {
        return onGround.call(player) || FloatyLeggingsItem.isAnchored(player);
    }
}
