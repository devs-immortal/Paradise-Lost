package net.id.aether.world.gen.carver;

import net.id.aether.Aether;
import net.id.aether.blocks.AetherBlocks;
import net.minecraft.block.Block;
import net.minecraft.util.math.floatprovider.ConstantFloatProvider;
import net.minecraft.util.math.floatprovider.UniformFloatProvider;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.carver.CarverConfig;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.heightprovider.UniformHeightProvider;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

@SuppressWarnings("unused")
public class AetherCarvers {

    public static final Carver<CloudCarverConfig> AERCLOUD_CARVER = register("aercloud_carver", new CloudCarver(CloudCarverConfig.CODEC));

    public static final RegistryEntry<ConfiguredCarver<?>> COLD_AERCLOUD_CARVER = register("cold_aercloud", AetherBlocks.COLD_AERCLOUD, 0.1F);
    public static final RegistryEntry<ConfiguredCarver<?>> STORM_AERCLOUD_CARVER = register("storm_aercloud", AetherBlocks.STORM_AERCLOUD, 0.01F);
    public static final RegistryEntry<ConfiguredCarver<?>> TEAL_AERCLOUD_CARVER = register("teal_aercloud", AetherBlocks.TEAL_AERCLOUD, 0.01F);
    public static final RegistryEntry<ConfiguredCarver<?>> BLUE_AERCLOUD_CARVER = register("blue_aercloud", AetherBlocks.BLUE_AERCLOUD, 0.03F);
    public static final RegistryEntry<ConfiguredCarver<?>> PINK_AERCLOUD_CARVER = register("pink_aercloud", AetherBlocks.PINK_AERCLOUD, 0.002F);
    public static final RegistryEntry<ConfiguredCarver<?>> GOLDEN_AERCLOUD_CARVER = register("golden_aercloud", AetherBlocks.GOLDEN_AERCLOUD, 0.01F);
    public static final RegistryEntry<ConfiguredCarver<?>> PURPLE_AERCLOUD_CARVER = register("purple_aercloud", AetherBlocks.PURPLE_AERCLOUD.DEFAULT_STATES, 0.005F);
    public static final RegistryEntry<ConfiguredCarver<?>> GREEN_AERCLOUD_CARVER = register("green_aercloud", AetherBlocks.GREEN_AERCLOUD, 0.01F);
    public static final RegistryEntry<ConfiguredCarver<?>> IRRADIATED_AERCLOUD_CARVER = register("irradiated_aercloud", AetherBlocks.IRRADIATED_AERCLOUD, 0.008F);
    public static final RegistryEntry<ConfiguredCarver<?>> BLAZING_AERCLOUD_CARVER = register("blazing_aercloud", AetherBlocks.BLAZING_AERCLOUD, 0.008F);

    @SuppressWarnings("unchecked")
    public static <T extends CarverConfig> Carver<T> register(String name, Carver<?> carver) {
        return (Carver<T>) Registry.register(Registry.CARVER, Aether.locate(name), carver);
    }

    public static RegistryEntry<ConfiguredCarver<?>> register(String name, ConfiguredCarver<?> carver) {
        return BuiltinRegistries.add(BuiltinRegistries.CONFIGURED_CARVER, Aether.locate(name), carver);
    }

    public static <B extends Block> RegistryEntry<ConfiguredCarver<?>> register(String id, B block, float probability) {
        return register(id, BlockStateProvider.of(block), probability);
    }

    public static <B extends BlockStateProvider> RegistryEntry<ConfiguredCarver<?>> register(String id, B stateProvider, float probability) {
        return register(
                id,
                AERCLOUD_CARVER.configure(
                        new CloudCarverConfig(
                                probability,
                                UniformHeightProvider.create(YOffset.aboveBottom(100), YOffset.fixed(260)),
                                UniformFloatProvider.create(0.5F, 1.1F), YOffset.aboveBottom(0),
                                UniformFloatProvider.create(0.2F, 0.4F),
                                UniformFloatProvider.create(0.4F, 0.6F),
                                stateProvider,
                                ConstantFloatProvider.create(0.15F),
                                UniformFloatProvider.create(1.585F, 2.75F),
                                ConstantIntProvider.create(1),
                                ConstantFloatProvider.create(6),
                                ConstantIntProvider.create(1),
                                ConstantFloatProvider.create(0.125F)
                        )
                )
        );
    }

    public static void init() {}
}
