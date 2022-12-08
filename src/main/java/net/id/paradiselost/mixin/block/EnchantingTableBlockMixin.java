package net.id.paradiselost.mixin.block;

import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.minecraft.block.EnchantingTableBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Allows the bookshelves tag to change enchantment level requirements.
 *
 * Credit: Taken from Gudenau's MoreTags
 */
@Mixin(EnchantingTableBlock.class)
public abstract class EnchantingTableBlockMixin {
    @Inject(
            method = "canAccessBookshelf",
            at = @At("TAIL"),
            cancellable = true
    )
    private static void canAccessBookshelf(World world, BlockPos tablePos, BlockPos bookshelfOffset, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(
                cir.getReturnValue()
                        || world.getBlockState(tablePos.add(bookshelfOffset)).isOf(ParadiseLostBlocks.SKYROOT_BOOKSHELF)
                        && world.isAir(tablePos.add(bookshelfOffset.getX() / 2, bookshelfOffset.getY(), bookshelfOffset.getZ() / 2))
        );
    }
}
