package net.id.aether.world.gen.carver;

import net.id.aether.Aether;
import net.id.aether.blocks.AetherBlocks;
import net.minecraft.block.Block;
import net.minecraft.util.math.floatprovider.ConstantFloatProvider;
import net.minecraft.util.math.floatprovider.UniformFloatProvider;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
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

    public static final RegistryEntry<ConfiguredCarver<?>> COLD_AERCLOUD_CARVER = registerCloud("cold_aercloud", AetherBlocks.COLD_AERCLOUD, 0.05F);
    public static final RegistryEntry<ConfiguredCarver<?>> STORM_AERCLOUD_CARVER = registerCloud("storm_aercloud", AetherBlocks.STORM_AERCLOUD, 0.02F);
    public static final RegistryEntry<ConfiguredCarver<?>> TEAL_AERCLOUD_CARVER = registerCloud("teal_aercloud", AetherBlocks.TEAL_AERCLOUD, 0.02F);
    public static final RegistryEntry<ConfiguredCarver<?>> BLUE_AERCLOUD_CARVER = registerCloud("blue_aercloud", AetherBlocks.BLUE_AERCLOUD, 0.02F);
    public static final RegistryEntry<ConfiguredCarver<?>> PINK_AERCLOUD_CARVER = registerCloud("pink_aercloud", AetherBlocks.PINK_AERCLOUD, 0.002F);
    public static final RegistryEntry<ConfiguredCarver<?>> GOLDEN_AERCLOUD_CARVER = registerCloud("golden_aercloud", AetherBlocks.GOLDEN_AERCLOUD, 0.02F);
    public static final RegistryEntry<ConfiguredCarver<?>> PURPLE_AERCLOUD_CARVER = registerCloud("purple_aercloud", AetherBlocks.PURPLE_AERCLOUD.DEFAULT_STATES, 0.01F);
    public static final RegistryEntry<ConfiguredCarver<?>> GREEN_AERCLOUD_CARVER = registerCloud("green_aercloud", AetherBlocks.GREEN_AERCLOUD, 0.02F);
    public static final RegistryEntry<ConfiguredCarver<?>> IRRADIATED_AERCLOUD_CARVER = registerCloud("irradiated_aercloud", AetherBlocks.IRRADIATED_AERCLOUD, 0.02F);
    public static final RegistryEntry<ConfiguredCarver<?>> BLAZING_AERCLOUD_CARVER = registerCloud("blazing_aercloud", AetherBlocks.BLAZING_AERCLOUD, 0.02F);

    @SuppressWarnings("unchecked")
    public static <T extends CarverConfig> Carver<T> register(String name, Carver<?> carver) {
        return (Carver<T>) Registry.register(Registry.CARVER, Aether.locate(name), carver);
    }

    public static RegistryEntry<ConfiguredCarver<?>> register(String name, ConfiguredCarver<?> carver) {
        return BuiltinRegistries.add(BuiltinRegistries.CONFIGURED_CARVER, Aether.locate(name), carver);
    }

    public static <B extends Block> RegistryEntry<ConfiguredCarver<?>> registerCloud(String id, B block, float probability) {
        return registerCloud(id, BlockStateProvider.of(block), probability);
    }
    public static <B extends BlockStateProvider> RegistryEntry<ConfiguredCarver<?>> registerCloud(String id, B stateProvider, float probability) {
        return register(
                id,
                AERCLOUD_CARVER.configure(
                        new CloudCarverConfig(
                                probability,
                                UniformHeightProvider.create(YOffset.aboveBottom(100), YOffset.fixed(260)),
                                UniformFloatProvider.create(0.5F, 1.1F), YOffset.aboveBottom(0),
                                UniformFloatProvider.create(0.3F, 0.75F),
                                UniformFloatProvider.create(0.36F, 0.6F),
                                stateProvider,
                                ConstantFloatProvider.create(0.15F),
                                UniformFloatProvider.create(0.785F, 1.25F),
                                UniformIntProvider.create(1, 2),
                                ConstantFloatProvider.create(3),
                                ConstantIntProvider.create(1),
                                ConstantFloatProvider.create(0.065F)
                        )
                )
        );
    }

    public static void init() {
    }
}
