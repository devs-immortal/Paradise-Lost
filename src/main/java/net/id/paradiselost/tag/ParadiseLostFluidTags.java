package net.id.paradiselost.tag;

import net.id.paradiselost.ParadiseLost;
import net.minecraft.fluid.Fluid;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.registry.Registry;

public class ParadiseLostFluidTags {
    public static final TagKey<Fluid> SPRING_WATER = register("spring_water");

    private static TagKey<Fluid> register(String id) {
        return TagKey.of(Registry.FLUID_KEY, ParadiseLost.locate(id));
    }
}
