package com.aether.fluids;

import com.aether.client.rendering.block.FluidRenderSetup;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.util.registry.Registry;

import static com.aether.Aether.locate;

public class AetherFluids {
    public static final FlowableFluid DENSE_AERCLOUD = new DenseAercloudFluid();

    public static void init() {
        Registry.register(Registry.FLUID, locate("dense_aercloud"), DENSE_AERCLOUD);
    }

    @Environment(EnvType.CLIENT)
    public static void initClient() {
        RenderLayers.FLUIDS.put(DENSE_AERCLOUD, RenderLayer.getTranslucent());
        FluidRenderSetup.setupFluidRendering(AetherFluids.DENSE_AERCLOUD, null, locate("dense_aercloud"), 0xFFFFFF);
    }
}
