package net.id.paradiselost.mixin.block;

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

    @Inject(method = "<init>", at = @At("RETURN"))
    private void makeBlocksMutable(CallbackInfo ci) {
        this.blocks = new HashSet<>(this.blocks);
    }
}
