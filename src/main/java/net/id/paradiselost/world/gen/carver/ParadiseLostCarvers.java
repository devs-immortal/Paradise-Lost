package net.id.paradiselost.world.gen.carver;

import net.id.paradiselost.ParadiseLost;
import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.carver.CarverConfig;

@SuppressWarnings("unused")
public class ParadiseLostCarvers {

    public static final Carver<CloudCarverConfig> CLOUD_CARVER = register("cloud_carver", new CloudCarver(CloudCarverConfig.CODEC));

//    public static final RegistryEntry<ConfiguredCarver<?>> LARGE_COLD_CLOUD_CARVER = register("large_cold_cloud", CLOUD_CARVER.configure(new CloudCarverConfig(0.0003F, UniformHeightProvider.create(YOffset.fixed(260), YOffset.fixed(340)), UniformFloatProvider.create(0.5F, 1.1F), YOffset.aboveBottom(32), UniformFloatProvider.create(1.0F, 2.15F), UniformFloatProvider.create(0.6F, 1.0F), ParadiseLostBlocks.COLD_CLOUD.getDefaultState(), ConstantFloatProvider.create(0.1F), UniformFloatProvider.create(0.285F, 0.45F), UniformIntProvider.create(8, 9), ConstantFloatProvider.create(0.3F), ConstantIntProvider.create(16), ConstantFloatProvider.create(1F))));
//    public static final RegistryEntry<ConfiguredCarver<?>> COLD_CLOUD_CARVER = register("cold_cloud", CLOUD_CARVER.configure(new CloudCarverConfig(0.0105F, UniformHeightProvider.create(YOffset.aboveBottom(4), YOffset.fixed(112)), UniformFloatProvider.create(0.5F, 1.1F), YOffset.aboveBottom(0), UniformFloatProvider.create(0.3F, 0.75F), UniformFloatProvider.create(0.36F, 0.6F), ParadiseLostBlocks.COLD_CLOUD.getDefaultState(), ConstantFloatProvider.create(0.15F), UniformFloatProvider.create(0.285F, 0.45F), UniformIntProvider.create(3, 4), ConstantFloatProvider.create(2), ConstantIntProvider.create(3), ConstantFloatProvider.create(0.25F))));
//    public static final RegistryEntry<ConfiguredCarver<?>> TINY_COLD_CLOUD_CARVER = register("tiny_cold_cloud", CLOUD_CARVER.configure(new CloudCarverConfig(0.01F, UniformHeightProvider.create(YOffset.aboveBottom(100), YOffset.fixed(260)), UniformFloatProvider.create(0.5F, 1.1F), YOffset.aboveBottom(0), UniformFloatProvider.create(0.3F, 0.75F), UniformFloatProvider.create(0.36F, 0.6F), ParadiseLostBlocks.COLD_CLOUD.getDefaultState(), ConstantFloatProvider.create(0.15F), UniformFloatProvider.create(0.785F, 1.25F), UniformIntProvider.create(1, 2), ConstantFloatProvider.create(3), ConstantIntProvider.create(1), ConstantFloatProvider.create(0.065F))));
//
//    public static final RegistryEntry<ConfiguredCarver<?>> LARGE_BLUE_CLOUD_CARVER = register("large_blue_cloud", CLOUD_CARVER.configure(new CloudCarverConfig(0.001F, UniformHeightProvider.create(YOffset.fixed(230), YOffset.fixed(310)), UniformFloatProvider.create(0.5F, 1.1F), YOffset.aboveBottom(32), UniformFloatProvider.create(5.48F, 6.75F), UniformFloatProvider.create(0.3F, 0.5F), ParadiseLostBlocks.BLUE_CLOUD.getDefaultState(), ConstantFloatProvider.create(0.233F), UniformFloatProvider.create(0.285F, 0.35F), UniformIntProvider.create(6, 7), ConstantFloatProvider.create(0.6F), ConstantIntProvider.create(16), ConstantFloatProvider.create(1F))));
//    public static final RegistryEntry<ConfiguredCarver<?>> BLUE_CLOUD_CARVER = register("blue_cloud", CLOUD_CARVER.configure(new CloudCarverConfig(0.007F, UniformHeightProvider.create(YOffset.aboveBottom(4), YOffset.fixed(32)), UniformFloatProvider.create(0.5F, 1.1F), YOffset.aboveBottom(0), UniformFloatProvider.create(0.5F, 1.25F), UniformFloatProvider.create(0.6F, 1.0F), ParadiseLostBlocks.BLUE_CLOUD.getDefaultState(), ConstantFloatProvider.create(0.175F), UniformFloatProvider.create(0.15F, 0.3F), UniformIntProvider.create(3, 4), ConstantFloatProvider.create(2), ConstantIntProvider.create(3), ConstantFloatProvider.create(0.25F))));
//    public static final RegistryEntry<ConfiguredCarver<?>> TINY_BLUE_CLOUD_CARVER = register("tiny_blue_cloud", CLOUD_CARVER.configure(new CloudCarverConfig(0.009F, UniformHeightProvider.create(YOffset.aboveBottom(100), YOffset.fixed(260)), UniformFloatProvider.create(0.5F, 1.1F), YOffset.aboveBottom(0), UniformFloatProvider.create(0.3F, 0.75F), UniformFloatProvider.create(0.36F, 0.6F), ParadiseLostBlocks.BLUE_CLOUD.getDefaultState(), ConstantFloatProvider.create(0.15F), UniformFloatProvider.create(0.785F, 1.25F), UniformIntProvider.create(1, 2), ConstantFloatProvider.create(3), ConstantIntProvider.create(1), ConstantFloatProvider.create(0.065F))));
//
//    public static final RegistryEntry<ConfiguredCarver<?>> LARGE_GOLDEN_CLOUD_CARVER = register("large_golden_cloud", CLOUD_CARVER.configure(new CloudCarverConfig(0.0006F, UniformHeightProvider.create(YOffset.fixed(290), YOffset.fixed(360)), UniformFloatProvider.create(0.5F, 1.1F), YOffset.aboveBottom(32), UniformFloatProvider.create(1.6F, 2.0F), UniformFloatProvider.create(1.5F, 1.85F), ParadiseLostBlocks.GOLDEN_CLOUD.getDefaultState(), ConstantFloatProvider.create(0.322F), UniformFloatProvider.create(0.585F, 0.75F), UniformIntProvider.create(7, 9), ConstantFloatProvider.create(0.4F), ConstantIntProvider.create(16), ConstantFloatProvider.create(1F))));
//    public static final RegistryEntry<ConfiguredCarver<?>> GOLDEN_CLOUD_CARVER = register("golden_cloud", CLOUD_CARVER.configure(new CloudCarverConfig(0.0085F, UniformHeightProvider.create(YOffset.aboveBottom(14), YOffset.fixed(68)), UniformFloatProvider.create(0.5F, 1.1F), YOffset.aboveBottom(32), UniformFloatProvider.create(0.5F, 1.25F), UniformFloatProvider.create(0.6F, 1.0F), ParadiseLostBlocks.GOLDEN_CLOUD.getDefaultState(), ConstantFloatProvider.create(0.225F), UniformFloatProvider.create(0.35F, 0.5F), UniformIntProvider.create(2, 5), ConstantFloatProvider.create(1.5F), ConstantIntProvider.create(3), ConstantFloatProvider.create(0.35F))));
//    public static final RegistryEntry<ConfiguredCarver<?>> TINY_GOLDEN_CLOUD_CARVER = register("tiny_golden_cloud", CLOUD_CARVER.configure(new CloudCarverConfig(0.00875F, UniformHeightProvider.create(YOffset.aboveBottom(100), YOffset.fixed(260)), UniformFloatProvider.create(0.5F, 1.1F), YOffset.aboveBottom(0), UniformFloatProvider.create(0.3F, 0.75F), UniformFloatProvider.create(0.36F, 0.6F), ParadiseLostBlocks.GOLDEN_CLOUD.getDefaultState(), ConstantFloatProvider.create(0.15F), UniformFloatProvider.create(0.785F, 1.25F), UniformIntProvider.create(1, 2), ConstantFloatProvider.create(3), ConstantIntProvider.create(1), ConstantFloatProvider.create(0.065F))));

    @SuppressWarnings("unchecked")
    public static <T extends CarverConfig> Carver<T> register(String name, Carver<?> carver) {
        return (Carver<T>) Registry.register(Registries.CARVER, ParadiseLost.locate(name), carver);
    }

    public static void init() {}

}
