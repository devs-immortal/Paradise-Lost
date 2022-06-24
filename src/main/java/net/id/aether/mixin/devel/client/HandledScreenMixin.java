package net.id.aether.mixin.devel.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * Current changes:
 * - Draws slot numbers in containers for debugging
 */
@Environment(EnvType.CLIENT)
@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin<T extends ScreenHandler> extends Screen {
    private HandledScreenMixin() {
        super(null);
    }
    
    @Inject(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShader(Ljava/util/function/Supplier;)V"
        ),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void render(
        MatrixStack matrices, int mouseX, int mouseY, float delta,
        CallbackInfo ci,
        int a, int b, MatrixStack stack2, int index, Slot slot
    ){
        //textRenderer.draw(matrices, String.valueOf(index), slot.x, slot.y, 0x00000000);
    }
}
