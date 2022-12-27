package net.id.aether.world.gen.carver;

import net.id.aether.Aether;
import net.id.aether.blocks.AetherBlocks;
import net.id.aether.blocks.natural.aercloud.DirectionalAercloudBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.Direction;
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
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;

@SuppressWarnings("unused")
public class AetherCarvers {
    private static final BlockState DEFAULT_PURPLE_AERCLOUD = AetherBlocks.PURPLE_AERCLOUD.getStateManager().getDefaultState();
    private static final WeightedBlockStateProvider PURPLE_AERCLOUD_STATE_PROVIDER = new WeightedBlockStateProvider(
            DataPool.<BlockState>builder()
                    .add(DEFAULT_PURPLE_AERCLOUD.with(DirectionalAercloudBlock.FACING, Direction.UP), 1)
                    .add(DEFAULT_PURPLE_AERCLOUD.with(DirectionalAercloudBlock.FACING, Direction.DOWN), 1)
                    .add(DEFAULT_PURPLE_AERCLOUD.with(DirectionalAercloudBlock.FACING, Direction.NORTH), 1)
                    .add(DEFAULT_PURPLE_AERCLOUD.with(DirectionalAercloudBlock.FACING, Direction.EAST), 1)
                    .add(DEFAULT_PURPLE_AERCLOUD.with(DirectionalAercloudBlock.FACING, Direction.SOUTH), 1)
                    .add(DEFAULT_PURPLE_AERCLOUD.with(DirectionalAercloudBlock.FACING, Direction.WEST), 1)
                    .build());

    public static final Carver<CloudCarverConfig> AERCLOUD_CARVER = register("aercloud_carver", new CloudCarver(CloudCarverConfig.CODEC));

    //    public static final ConfiguredCarver<?> GIANT_COLD_AERCLOUD_CARVER = register("cold_aercloud_giant", AERCLOUD_CARVER.configure(new CloudCarverConfig(0.00105F, UniformHeightProvider.create(YOffset.aboveBottom(112), YOffset.fixed(240)), UniformFloatProvider.create(0.5F, 1.1F), YOffset.aboveBottom(32), UniformFloatProvider.create(1.0F, 1.75F), UniformFloatProvider.create(0.6F, 1.0F), AetherBlocks.COLD_AERCLOUD.getDefaultState(), ConstantFloatProvider.create(0.0125F), UniformFloatProvider.create(0.35F, 0.65F), UniformIntProvider.create(12, 18), ConstantFloatProvider.create(1), ConstantIntProvider.create(10), ConstantFloatProvider.create(2.75F))));
    public static final RegistryEntry<ConfiguredCarver<?>> COLD_AERCLOUD_CARVER = registerCloud("cold_aercloud", AetherBlocks.COLD_AERCLOUD);
    public static final RegistryEntry<ConfiguredCarver<?>> STORM_AERCLOUD_CARVER = registerCloud("storm_aercloud", AetherBlocks.STORM_AERCLOUD);
    public static final RegistryEntry<ConfiguredCarver<?>> TEAL_AERCLOUD_CARVER = registerCloud("teal_aercloud", AetherBlocks.TEAL_AERCLOUD);
    public static final RegistryEntry<ConfiguredCarver<?>> BLUE_AERCLOUD_CARVER = registerCloud("blue_aercloud", AetherBlocks.BLUE_AERCLOUD);
    public static final RegistryEntry<ConfiguredCarver<?>> PINK_AERCLOUD_CARVER = registerCloud("pink_aercloud", AetherBlocks.PINK_AERCLOUD);
    public static final RegistryEntry<ConfiguredCarver<?>> GOLDEN_AERCLOUD_CARVER = registerCloud("golden_aercloud", AetherBlocks.GOLDEN_AERCLOUD);
    public static final RegistryEntry<ConfiguredCarver<?>> PURPLE_AERCLOUD_CARVER = registerCloud("purple_aercloud", AetherBlocks.PURPLE_AERCLOUD);
    public static final RegistryEntry<ConfiguredCarver<?>> GREEN_AERCLOUD_CARVER = registerCloud("green_aercloud", AetherBlocks.GREEN_AERCLOUD);
    public static final RegistryEntry<ConfiguredCarver<?>> IRRADIATED_AERCLOUD_CARVER = registerCloud("irradiated_aercloud", AetherBlocks.IRRADIATED_AERCLOUD);
    public static final RegistryEntry<ConfiguredCarver<?>> BLAZING_AERCLOUD_CARVER = registerCloud("blazing_aercloud", AetherBlocks.BLAZING_AERCLOUD);

    @SuppressWarnings("unchecked")
    public static <T extends CarverConfig> Carver<T> register(String name, Carver<?> carver) {
        return (Carver<T>) Registry.register(Registry.CARVER, Aether.locate(name), carver);
    }

    public static RegistryEntry<ConfiguredCarver<?>> register(String name, ConfiguredCarver<?> carver) {
        return BuiltinRegistries.add(BuiltinRegistries.CONFIGURED_CARVER, Aether.locate(name), carver);
    }

    public static <B extends Block> RegistryEntry<ConfiguredCarver<?>> registerCloud(String id, B block) {
        return register(
                id,
                AERCLOUD_CARVER.configure(
                        new CloudCarverConfig(
                                0.01F,
                                UniformHeightProvider.create(YOffset.aboveBottom(100), YOffset.fixed(260)),
                                UniformFloatProvider.create(1F, 1.1F),
                                YOffset.aboveBottom(0),
                                UniformFloatProvider.create(0F, 0.01F),
                                UniformFloatProvider.create(1.5F, 2.25F),
                                BlockStateProvider.of(block),
                                ConstantFloatProvider.create(0.15F),
                                UniformFloatProvider.create(0.785F, 1.25F),
                                UniformIntProvider.create(3, 4),
                                ConstantFloatProvider.create(3),
                                ConstantIntProvider.create(1),
                                ConstantFloatProvider.create(0.1F)
                        )
                )
        );
    }

    public static void init() {
    }
}
