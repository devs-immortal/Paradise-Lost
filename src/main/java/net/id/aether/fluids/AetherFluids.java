package net.id.aether.fluids;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.id.aether.client.rendering.block.FluidRenderSetup;
import net.id.aether.registry.AetherRegistryQueues;
import net.id.incubus_core.util.RegistryQueue;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;

import static net.id.aether.Aether.locate;

public class AetherFluids {
    private static final RegistryQueue.Action<Fluid> translucent = (id, fluid) -> BlockRenderLayerMap.INSTANCE.putFluid(fluid, RenderLayer.getTranslucent());

    private static RegistryQueue.Action<Fluid> renderSetup(Fluid flowing, int color){ return RegistryQueue.onClient((id, fluid) -> FluidRenderSetup.setupFluidRendering(fluid, flowing, id, color));}
    private static RegistryQueue.Action<Fluid> onlyStillRenderSetup(int color) {return renderSetup(null, color);}

    public static final FlowableFluid DENSE_AERCLOUD = add("dense_aercloud", new DenseAercloudFluid(), translucent, onlyStillRenderSetup(0xFFFFFF));

    @SafeVarargs
    private static <V extends Fluid> V add(String id, V fluid, RegistryQueue.Action<? super V>... additionalActions) {
        return AetherRegistryQueues.FLUID.add(locate(id), fluid, additionalActions);
    }

    public static void init() {
        AetherRegistryQueues.FLUID.register();
    }

}
