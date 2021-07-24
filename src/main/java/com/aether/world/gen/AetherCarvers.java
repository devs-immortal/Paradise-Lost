package com.aether.world.gen;

import com.aether.Aether;
import com.aether.blocks.AetherBlocks;
import net.minecraft.util.math.floatprovider.ConstantFloatProvider;
import net.minecraft.util.math.floatprovider.UniformFloatProvider;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.carver.*;
import net.minecraft.world.gen.heightprovider.UniformHeightProvider;

@SuppressWarnings("unused")
public class AetherCarvers {

    public static final Carver<CloudCarverConfig> AERCLOUD_CARVER = register("aercloud_carver", new CloudCarver(CloudCarverConfig.CODEC));

    public static final ConfiguredCarver<?> UPPER_COLD_AERCLOUD_CARVER = register("cold_aercloud_upper", AERCLOUD_CARVER.configure(new CloudCarverConfig(0.15F, UniformHeightProvider.create(YOffset.aboveBottom(96), YOffset.fixed(256)), UniformFloatProvider.create(0.1F, 0.9F), YOffset.aboveBottom(32), false, UniformFloatProvider.create(0.8F, 2F), UniformFloatProvider.create(0.6F, 1.5F), AetherBlocks.COLD_AERCLOUD.getDefaultState(), ConstantFloatProvider.create(0.1F), UniformFloatProvider.create(0.4F, 0.65F), UniformIntProvider.create(4, 7), ConstantIntProvider.create(2), ConstantIntProvider.create(16), ConstantFloatProvider.create(1))));
    public static final ConfiguredCarver<?> LOWER_COLD_AERCLOUD_CARVER = register("cold_aercloud_lower", AERCLOUD_CARVER.configure(new CloudCarverConfig(0.075F, UniformHeightProvider.create(YOffset.aboveBottom(4), YOffset.fixed(48)), UniformFloatProvider.create(0.1F, 0.9F), YOffset.aboveBottom(0), false, UniformFloatProvider.create(0.8F, 2F), UniformFloatProvider.create(0.6F, 1.5F), AetherBlocks.COLD_AERCLOUD.getDefaultState(), ConstantFloatProvider.create(0.15F), UniformFloatProvider.create(0.4F, 0.65F), UniformIntProvider.create(3, 6), ConstantIntProvider.create(2), ConstantIntProvider.create(8), ConstantFloatProvider.create(1))));
    public static final ConfiguredCarver<?> GIANT_COLD_AERCLOUD_CARVER = register("cold_aercloud_giant", AERCLOUD_CARVER.configure(new CloudCarverConfig(0.0075F, UniformHeightProvider.create(YOffset.aboveBottom(112), YOffset.fixed(240)), UniformFloatProvider.create(0.1F, 0.9F), YOffset.aboveBottom(32), false, UniformFloatProvider.create(0.8F, 2F), UniformFloatProvider.create(0.6F, 1.5F), AetherBlocks.COLD_AERCLOUD.getDefaultState(), ConstantFloatProvider.create(0.0125F), UniformFloatProvider.create(0.55F, 1F), UniformIntProvider.create(15, 20), ConstantIntProvider.create(3), ConstantIntProvider.create(20), ConstantFloatProvider.create(3.5F))));

    public static final ConfiguredCarver<?> UPPER_BLUE_AERCLOUD_CARVER = register("blue_aercloud_upper", AERCLOUD_CARVER.configure(new CloudCarverConfig(0.115F, UniformHeightProvider.create(YOffset.aboveBottom(80), YOffset.fixed(256)), UniformFloatProvider.create(0.1F, 0.9F), YOffset.aboveBottom(32), false, UniformFloatProvider.create(0.8F, 2F), UniformFloatProvider.create(0.6F, 1.5F), AetherBlocks.BLUE_AERCLOUD.getDefaultState(), ConstantFloatProvider.create(0.233F), UniformFloatProvider.create(0.4F, 0.65F), UniformIntProvider.create(2, 4), ConstantIntProvider.create(2), ConstantIntProvider.create(12), ConstantFloatProvider.create(1))));
    public static final ConfiguredCarver<?> LOWER_BLUE_AERCLOUD_CARVER = register("blue_aercloud_lower", AERCLOUD_CARVER.configure(new CloudCarverConfig(0.05F, UniformHeightProvider.create(YOffset.aboveBottom(4), YOffset.fixed(32)), UniformFloatProvider.create(0.1F, 0.9F), YOffset.aboveBottom(0), false, UniformFloatProvider.create(0.8F, 2F), UniformFloatProvider.create(0.6F, 1.5F), AetherBlocks.BLUE_AERCLOUD.getDefaultState(), ConstantFloatProvider.create(0.175F), UniformFloatProvider.create(0.3F, 0.575F), UniformIntProvider.create(3, 5), ConstantIntProvider.create(2), ConstantIntProvider.create(6), ConstantFloatProvider.create(1))));

    public static final ConfiguredCarver<?> GOLDEN_AERCLOUD_CARVER = register("golden_aercloud", AERCLOUD_CARVER.configure(new CloudCarverConfig(0.1F, UniformHeightProvider.create(YOffset.aboveBottom(64), YOffset.fixed(300)), UniformFloatProvider.create(0.1F, 0.9F), YOffset.aboveBottom(32), false, UniformFloatProvider.create(0.8F, 2F), UniformFloatProvider.create(0.6F, 1.5F), AetherBlocks.GOLDEN_AERCLOUD.getDefaultState(), ConstantFloatProvider.create(0.225F), UniformFloatProvider.create(0.55F, 0.7F), UniformIntProvider.create(2, 6), ConstantIntProvider.create(3), ConstantIntProvider.create(3), ConstantFloatProvider.create(1.5F))));

    @SuppressWarnings("unchecked")
    public static <T extends CarverConfig> Carver<T> register(String name, Carver<?> carver) {
        return (Carver<T>) Registry.register(Registry.CARVER, Aether.locate(name), carver);
    }

    public static ConfiguredCarver<?> register(String name, ConfiguredCarver<?> carver) {
        return BuiltinRegistries.add(BuiltinRegistries.CONFIGURED_CARVER, Aether.locate(name), carver);
    }

    public static void init() {}
}
