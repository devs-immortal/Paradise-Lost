package net.id.aether.mixin.devel.server;

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

import java.util.function.Supplier;

@Mixin(ChunkRegion.class)
public abstract class ChunkRegionMixin{
    @Shadow private @Nullable Supplier<String> currentlyGeneratingStructureName;
    
    @Inject(
        method = "isValidForSetBlock",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/Util;error(Ljava/lang/String;)V",
            shift = At.Shift.AFTER
        )
    )
    private void isValidForSetBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir){
        if(AetherDevel.Config.SETBLOCK_STACK_TRACE){
            StackTraceElement[] trace = Thread.currentThread().getStackTrace();
            for(StackTraceElement traceElement : trace){
                if(traceElement.getModuleName() != null){
                    continue;
                }
                System.err.println("\tat " + traceElement);
            }
        }
        if(currentlyGeneratingStructureName != null){
            var feature = currentlyGeneratingStructureName.get();
            if(feature != null && feature.contains(Aether.MOD_ID)){
                AetherDevel.logBadFeature(feature);
            }
        }
    }
}
