package net.id.paradise_lost.mixin.potion;

import net.id.paradise_lost.registry.ItemRegistry;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PotionBrewing.class)
public class BrewingRecipeRegistryMixin {

    @Inject(method = "addVanillaMixes", at = @At("TAIL"))
    private static void registerDefaults(PotionBrewing.Builder builder, CallbackInfo ci) {
        builder.addMix(Potions.HEALING, ItemRegistry.POPOM_JELLY.get(), ItemRegistry.HEALTH_BOOST_POTION);
        builder.addMix(ItemRegistry.HEALTH_BOOST_POTION, Items.REDSTONE, ItemRegistry.LONG_HEALTH_BOOST_POTION);
        builder.addMix(ItemRegistry.HEALTH_BOOST_POTION, Items.GLOWSTONE_DUST, ItemRegistry.STRONG_HEALTH_BOOST_POTION);
    }

}
