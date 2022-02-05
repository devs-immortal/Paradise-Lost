package net.id.aether.tag;

import net.fabricmc.fabric.api.tag.TagFactory;
import net.id.aether.Aether;
import net.minecraft.fluid.Fluid;
import net.minecraft.tag.Tag;

public class AetherFluidTags {
    public static final Tag<Fluid> SWET_TRANSFORMERS_BLUE = TagFactory.FLUID.create(Aether.locate("swet_transformers/blue"));
    public static final Tag<Fluid> SWET_TRANSFORMERS_GOLDEN = TagFactory.FLUID.create(Aether.locate("swet_transformers/golden"));
    public static final Tag<Fluid> SWET_TRANSFORMERS_PURPLE = TagFactory.FLUID.create(Aether.locate("swet_transformers/purple"));
    public static final Tag<Fluid> SWET_TRANSFORMERS_VERMILION = TagFactory.FLUID.create(Aether.locate("swet_transformers/vermilion"));
    public static final Tag<Fluid> SPRING_WATER = TagFactory.FLUID.create(Aether.locate("spring_water"));
}
