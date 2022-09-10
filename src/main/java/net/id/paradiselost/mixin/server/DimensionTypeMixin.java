package net.id.paradiselost.mixin.server;

import net.id.paradiselost.world.dimension.ParadiseLostDimension;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(DimensionType.class)
public abstract class DimensionTypeMixin {
    /*
    @Inject(
        method = "addRegistryDefaults",
        at = @At("RETURN"),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private static void addRegistryDefaults(
        DynamicRegistryManager.Mutable registryManager,
        CallbackInfoReturnable<DynamicRegistryManager.Mutable> cir,
        MutableRegistry<DimensionType> registry
    ) {
        ParadiseLostDimension.registerDimensionTypes(registry);
    }
    
    @Inject(
        method = "createDefaultDimensionOptions(Lnet/minecraft/util/registry/DynamicRegistryManager;JZ)Lnet/minecraft/util/registry/Registry;",
        at = @At("RETURN"),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private static void createDefaultDimensionOptions(
        DynamicRegistryManager registryManager, long seed, boolean useInstance,
        CallbackInfoReturnable<Registry<DimensionOptions>> cir,
        MutableRegistry<DimensionOptions> registry
    ) {
        ParadiseLostDimension.registerDefaultOptions(registryManager, (SimpleRegistry<DimensionOptions>) registry, seed, useInstance);
    }
    */
}
