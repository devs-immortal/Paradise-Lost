package com.aether.mixin.client;

import com.aether.util.item.AetherItemExtensions;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Gui;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Gui.class)
public class InGameHudMixin {


    @Shadow private ItemStack currentStack;

    @Redirect(method = "renderHeldItemTooltip", at = @At(value = "FIELD", target = "Lnet/minecraft/util/Rarity;formatting:Lnet/minecraft/util/Formatting;", opcode = Opcodes.GETFIELD))
    private ChatFormatting getCustomRarityFormatting(Rarity rarity) {
        if (rarity != null) return rarity.color;
        return ((AetherItemExtensions) this.currentStack.getItem()).getCustomRarityFormatting();
    }
}
