package net.id.paradiselost.mixin.block;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;

@Mixin(BlockEntityType.class)
public class BlockEntityTypeMixin {
    @Mutable
    @Shadow
    @Final
    private Set<Block> blocks;

    /**
     * Makes the set of blocks in the BlockEntityTypes mutable, so that they can be modified.
     * Only do this once the set has been created (made immutable).
     */
    @Inject(method = "<init>", at = @At("RETURN"))
    private void makeBlocksMutable(CallbackInfo ci) {
        if (this.blocks instanceof ImmutableSet<Block>) {
            this.blocks = new HashSet<>(this.blocks);
        }
    }
}
