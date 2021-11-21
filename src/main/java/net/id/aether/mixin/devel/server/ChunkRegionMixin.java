package net.id.aether.mixin.devel.server;

import java.util.function.Supplier;
import net.id.aether.Aether;
import net.id.aether.devel.AetherDevel;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkRegion.class)
public abstract class ChunkRegionMixin{
    @Shadow private @Nullable Supplier<String> field_33756;
    
    @Inject(
        method = "isValidForSetBlock",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/Util;error(Ljava/lang/String;)V",
            shift = At.Shift.AFTER
        )
    )
    private void isValidForSetBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir){
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        for(StackTraceElement traceElement : trace){
            if(traceElement.getModuleName() != null){
                continue;
            }
            System.err.println("\tat " + traceElement);
        }
        if(field_33756 != null){
            var feature = field_33756.get();
            if(feature != null && feature.contains(Aether.MOD_ID)){
                AetherDevel.logBadFeature(feature);
            }
        }
    }
}
