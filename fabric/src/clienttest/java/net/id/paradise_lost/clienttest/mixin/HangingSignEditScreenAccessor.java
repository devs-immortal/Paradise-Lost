package net.id.paradise_lost.clienttest.mixin;

import net.minecraft.client.gui.screens.inventory.HangingSignEditScreen;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(HangingSignEditScreen.class)
public interface HangingSignEditScreenAccessor {
    @Accessor("texture")
    ResourceLocation clienttest$texture();
}
