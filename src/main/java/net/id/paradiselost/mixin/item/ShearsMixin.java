package net.id.paradiselost.mixin.item;

import net.id.paradiselost.tag.ParadiseLostBlockTags;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShearsItem.class)
public class ShearsMixin {

    @Inject(method = "postMine", at = @At("RETURN"), cancellable = true)
    public void postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner, CallbackInfoReturnable<Boolean> cir) {
        if(state.isIn(ParadiseLostBlockTags.PARADISE_LOST_SHEARABLE)) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }

}
