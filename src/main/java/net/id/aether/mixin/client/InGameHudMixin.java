package net.id.aether.mixin.client;

import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(InGameHud.class)
public class InGameHudMixin {


    /*@Shadow private ItemStack currentStack;

    @Redirect(method = "renderHeldItemTooltip", at = @At(value = "FIELD", target = "Lnet/minecraft/util/Rarity;formatting:Lnet/minecraft/util/Formatting;", opcode = Opcodes.GETFIELD))
    private Formatting getCustomRarityFormatting(Rarity rarity) {
        if (rarity != null) return rarity.formatting;
        return ((AetherItemExtensions) this.currentStack.getItem()).getCustomRarityFormatting();
    }*/
}
