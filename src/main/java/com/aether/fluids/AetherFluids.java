package com.aether.fluids;

import com.aether.Aether;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.util.registry.Registry;

public class AetherFluids {
    public static final FlowableFluid DENSE_AERCLOUD = new DenseAercloudFluid();

    public static void init() {
        Registry.register(Registry.FLUID, Aether.locate("dense_aercloud"), DENSE_AERCLOUD);
    }
}
