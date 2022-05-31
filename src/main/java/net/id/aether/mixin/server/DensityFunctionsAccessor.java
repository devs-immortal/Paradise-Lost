package net.id.aether.mixin.server;

import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@SuppressWarnings("unused")
@Mixin(DensityFunctions.class)
public interface DensityFunctionsAccessor {
    @Accessor("SHIFT_X") static RegistryKey<DensityFunction> getShiftX() { throw new AssertionError(); }
    @Accessor("SHIFT_Z") static RegistryKey<DensityFunction> getShiftZ() { throw new AssertionError(); }
    
    @Accessor("Y") static RegistryKey<DensityFunction> getY() { throw new AssertionError(); }
    
    @Invoker static DensityFunction invokeMethod_41116(RegistryKey<DensityFunction> registryKey){ throw new AssertionError(); }
    
    @Accessor static RegistryKey<DensityFunction> getCONTINENTS_OVERWORLD() { throw new AssertionError(); }
    
    @Accessor static RegistryKey<DensityFunction> getEROSION_OVERWORLD() { throw new AssertionError(); }
    
    @Accessor static RegistryKey<DensityFunction> getDEPTH_OVERWORLD() { throw new AssertionError(); }
    
    @Accessor static RegistryKey<DensityFunction> getRIDGES_OVERWORLD() { throw new AssertionError(); }
    
    @Accessor static RegistryKey<DensityFunction> getFACTOR_OVERWORLD() { throw new AssertionError(); }
    
    @Accessor static RegistryKey<DensityFunction> getSLOPED_CHEESE_OVERWORLD() { throw new AssertionError(); }
    
    @Accessor static RegistryKey<DensityFunction> getCAVES_ENTRANCES_OVERWORLD() { throw new AssertionError(); }
    
    @Accessor static RegistryKey<DensityFunction> getCAVES_SPAGHETTI_2D_OVERWORLD() { throw new AssertionError(); }
    
    @Accessor static RegistryKey<DensityFunction> getCAVES_SPAGHETTI_ROUGHNESS_FUNCTION_OVERWORLD() { throw new AssertionError(); }
    @Accessor static RegistryKey<DensityFunction> getCAVES_PILLARS_OVERWORLD() { throw new AssertionError(); }
    @Accessor static RegistryKey<DensityFunction> getCAVES_NOODLE_OVERWORLD() { throw new AssertionError(); }
}
