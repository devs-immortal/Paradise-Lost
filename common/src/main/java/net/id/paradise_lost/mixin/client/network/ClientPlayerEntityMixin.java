package net.id.paradise_lost.mixin.client.network;

import net.id.paradise_lost.block.blockentity.ParadiseHangingSignBlockEntity;
import net.id.paradise_lost.item.armor.FloatyLeggingsItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.HangingSignEditScreen;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public abstract class ClientPlayerEntityMixin {

    @Shadow
    @Final
    protected Minecraft minecraft;

    @Shadow
    public ClientInput input;

    @Unique
    private int paradiseLost$floatyJumpToggleTimer = 0;

    @Unique
    private boolean paradiseLost$floatyWasJumping = false;

    @Inject(method = "openTextEdit", at = @At("HEAD"), cancellable = true)
    public void openEditSignScreen(SignBlockEntity sign, boolean front, CallbackInfo ci) {
        if (sign instanceof ParadiseHangingSignBlockEntity hangingSignBlockEntity) {
            this.minecraft.setScreen(new HangingSignEditScreen(hangingSignBlockEntity, front, this.minecraft.isTextFilteringEnabled()));
            ci.cancel();
        }
    }

    @Inject(method = "aiStep", at = @At("HEAD"))
    private void paradiseLost$floatyDoubleJumpToggle(CallbackInfo ci) {
        LocalPlayer player = (LocalPlayer) (Object) this;
        if (this.paradiseLost$floatyJumpToggleTimer > 0) {
            this.paradiseLost$floatyJumpToggleTimer--;
        }

        boolean jumping = this.input != null && this.input.keyPresses.jump();
        boolean wearing = FloatyLeggingsItem.isWearing(player);
        boolean canToggleAnchor = FloatyLeggingsItem.isSurvivalLike(player);

        if (wearing && canToggleAnchor && !this.paradiseLost$floatyWasJumping && jumping) {
            if (this.paradiseLost$floatyJumpToggleTimer == 0) {
                this.paradiseLost$floatyJumpToggleTimer = FloatyLeggingsItem.JUMP_TOGGLE_TICKS;
            } else if (FloatyLeggingsItem.canAnchor(player)) {
                FloatyLeggingsItem.toggleAnchorFromClient(player);
                this.paradiseLost$floatyJumpToggleTimer = 0;
            }
        }

        this.paradiseLost$floatyWasJumping = jumping;
    }

}
