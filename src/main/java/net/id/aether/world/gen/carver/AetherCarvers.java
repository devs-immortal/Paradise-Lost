package net.id.aether.world.gen.carver;

import net.id.aether.Aether;
import net.id.aether.blocks.AetherBlocks;
import net.minecraft.util.math.floatprovider.ConstantFloatProvider;
import net.minecraft.util.math.floatprovider.UniformFloatProvider;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.carver.CarverConfig;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.heightprovider.UniformHeightProvider;

@SuppressWarnings("unused")
public class AetherCarvers {

    public static final Carver<CloudCarverConfig> AERCLOUD_CARVER = register("aercloud_carver", new CloudCarver(CloudCarverConfig.CODEC));

    public static final ConfiguredCarver<?> UPPER_COLD_AERCLOUD_CARVER = register("cold_aercloud_upper", AERCLOUD_CARVER.configure(new CloudCarverConfig(0.021F, UniformHeightProvider.create(YOffset.aboveBottom(96), YOffset.fixed(256)), UniformFloatProvider.create(0.5F, 1.1F), YOffset.aboveBottom(32), false, UniformFloatProvider.create(1.0F, 2.15F), UniformFloatProvider.create(0.6F, 1.0F), AetherBlocks.COLD_AERCLOUD.getDefaultState(), ConstantFloatProvider.create(0.1F), UniformFloatProvider.create(0.285F, 0.45F), UniformIntProvider.create(3, 5), ConstantFloatProvider.create(1), ConstantIntProvider.create(8), ConstantFloatProvider.create(0.25F))));
    public static final ConfiguredCarver<?> LOWER_COLD_AERCLOUD_CARVER = register("cold_aercloud_lower", AERCLOUD_CARVER.configure(new CloudCarverConfig(0.0105F, UniformHeightProvider.create(YOffset.aboveBottom(4), YOffset.fixed(48)), UniformFloatProvider.create(0.5F, 1.1F), YOffset.aboveBottom(0), false, UniformFloatProvider.create(0.5F, 1.25F), UniformFloatProvider.create(0.6F, 1.0F), AetherBlocks.COLD_AERCLOUD.getDefaultState(), ConstantFloatProvider.create(0.15F), UniformFloatProvider.create(0.285F, 0.45F), UniformIntProvider.create(3, 4), ConstantFloatProvider.create(2), ConstantIntProvider.create(4), ConstantFloatProvider.create(0.25F))));
    public static final ConfiguredCarver<?> GIANT_COLD_AERCLOUD_CARVER = register("cold_aercloud_giant", AERCLOUD_CARVER.configure(new CloudCarverConfig(0.00105F, UniformHeightProvider.create(YOffset.aboveBottom(112), YOffset.fixed(240)), UniformFloatProvider.create(0.5F, 1.1F), YOffset.aboveBottom(32), false, UniformFloatProvider.create(1.0F, 1.75F), UniformFloatProvider.create(0.6F, 1.0F), AetherBlocks.COLD_AERCLOUD.getDefaultState(), ConstantFloatProvider.create(0.0125F), UniformFloatProvider.create(0.35F, 0.65F), UniformIntProvider.create(12, 18), ConstantFloatProvider.create(1), ConstantIntProvider.create(10), ConstantFloatProvider.create(2.75F))));

    public static final ConfiguredCarver<?> UPPER_BLUE_AERCLOUD_CARVER = register("blue_aercloud_upper", AERCLOUD_CARVER.configure(new CloudCarverConfig(0.0161F, UniformHeightProvider.create(YOffset.aboveBottom(80), YOffset.fixed(256)), UniformFloatProvider.create(0.5F, 1.1F), YOffset.aboveBottom(32), false, UniformFloatProvider.create(0.5F, 1.25F), UniformFloatProvider.create(0.6F, 1.0F), AetherBlocks.BLUE_AERCLOUD.getDefaultState(), ConstantFloatProvider.create(0.233F), UniformFloatProvider.create(0.285F, 0.35F), UniformIntProvider.create(2, 4), ConstantFloatProvider.create(1), ConstantIntProvider.create(6), ConstantFloatProvider.create(0.25F))));
    public static final ConfiguredCarver<?> LOWER_BLUE_AERCLOUD_CARVER = register("blue_aercloud_lower", AERCLOUD_CARVER.configure(new CloudCarverConfig(0.007F, UniformHeightProvider.create(YOffset.aboveBottom(4), YOffset.fixed(32)), UniformFloatProvider.create(0.5F, 1.1F), YOffset.aboveBottom(0), false, UniformFloatProvider.create(0.5F, 1.25F), UniformFloatProvider.create(0.6F, 1.0F), AetherBlocks.BLUE_AERCLOUD.getDefaultState(), ConstantFloatProvider.create(0.175F), UniformFloatProvider.create(0.15F, 0.3F), UniformIntProvider.create(3, 4), ConstantFloatProvider.create(2), ConstantIntProvider.create(3), ConstantFloatProvider.create(0.25F))));

    public static final ConfiguredCarver<?> GOLDEN_AERCLOUD_CARVER = register("golden_aercloud", AERCLOUD_CARVER.configure(new CloudCarverConfig(0.014F, UniformHeightProvider.create(YOffset.aboveBottom(64), YOffset.fixed(300)), UniformFloatProvider.create(0.5F, 1.1F), YOffset.aboveBottom(32), false, UniformFloatProvider.create(0.5F, 1.25F), UniformFloatProvider.create(0.6F, 1.0F), AetherBlocks.GOLDEN_AERCLOUD.getDefaultState(), ConstantFloatProvider.create(0.225F), UniformFloatProvider.create(0.35F, 0.5F), UniformIntProvider.create(2, 5), ConstantFloatProvider.create(1.5F), ConstantIntProvider.create(3), ConstantFloatProvider.create(0.35F))));

    @SuppressWarnings("unchecked")
    public static <T extends CarverConfig> Carver<T> register(String name, Carver<?> carver) {
        return (Carver<T>) Registry.register(Registry.CARVER, Aether.locate(name), carver);
    }

    public static ConfiguredCarver<?> register(String name, ConfiguredCarver<?> carver) {
        return BuiltinRegistries.add(BuiltinRegistries.CONFIGURED_CARVER, Aether.locate(name), carver);
    }

    public static void init() {
    }
}
