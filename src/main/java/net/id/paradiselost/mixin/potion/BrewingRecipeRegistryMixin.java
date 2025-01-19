package net.id.paradiselost.mixin.potion;

import net.id.paradiselost.items.ParadiseLostItems;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BrewingRecipeRegistry.class)
public class BrewingRecipeRegistryMixin {

    @Inject(method = "registerDefaults", at = @At("TAIL"))
    private static void registerDefaults(BrewingRecipeRegistry.Builder builder, CallbackInfo ci) {
        builder.registerPotionRecipe(Potions.HEALING, ParadiseLostItems.POPOM_JELLY, ParadiseLostItems.HEALTH_BOOST_POTION);
        builder.registerPotionRecipe(ParadiseLostItems.HEALTH_BOOST_POTION, Items.REDSTONE, ParadiseLostItems.LONG_HEALTH_BOOST_POTION);
        builder.registerPotionRecipe(ParadiseLostItems.HEALTH_BOOST_POTION, Items.GLOWSTONE_DUST, ParadiseLostItems.STRONG_HEALTH_BOOST_POTION);
    }

}
