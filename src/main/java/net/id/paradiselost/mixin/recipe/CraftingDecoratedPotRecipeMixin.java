package net.id.paradiselost.mixin.recipe;

import net.id.paradiselost.blocks.blockentity.CalciteDecoratedPotBlockEntity;
import net.minecraft.block.entity.Sherds;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.CraftingDecoratedPotRecipe;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CraftingDecoratedPotRecipe.class)
public class CraftingDecoratedPotRecipeMixin {

    @Inject(method = "craft(Lnet/minecraft/recipe/input/CraftingRecipeInput;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/item/ItemStack;", at = @At("HEAD"), cancellable = true)
    public void craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup lookup, CallbackInfoReturnable<ItemStack> cir) {
        Sherds sherds = new Sherds(
                input.getStackInSlot(1).getItem(),
                input.getStackInSlot(3).getItem(),
                input.getStackInSlot(5).getItem(),
                input.getStackInSlot(7).getItem()
        );
        if (sherds.stream().stream().anyMatch(i -> i.equals(Items.CALCITE))) {
            if (sherds.stream().stream().anyMatch(i -> i.equals(Items.BRICK))) {
                cir.setReturnValue(ItemStack.EMPTY);
            } else {
                cir.setReturnValue(CalciteDecoratedPotBlockEntity.getStackWith(sherds));
            }
        }
    }
}
