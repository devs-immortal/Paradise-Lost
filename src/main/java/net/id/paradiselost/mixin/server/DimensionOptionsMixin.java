package net.id.paradiselost.mixin.server;

import com.google.common.collect.ImmutableSet;
import net.id.paradiselost.world.dimension.ParadiseLostDimension;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.dimension.DimensionOptions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(DimensionOptions.class)
public abstract class DimensionOptionsMixin {
    @Shadow
    @Mutable
    @Final private static Set<RegistryKey<DimensionOptions>> BASE_DIMENSIONS;
    
    @Inject(
            method = "<clinit>",
            at = @At("TAIL")
    )
    private static void staticInit(CallbackInfo ci) {
        var builder = ImmutableSet.<RegistryKey<DimensionOptions>>builder();
        builder.addAll(BASE_DIMENSIONS);
        builder.add(ParadiseLostDimension.OPTIONS_KEY);
        BASE_DIMENSIONS = builder.build();
    }
}
