package net.id.paradise_lost.block.blockentity;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.registry.BlockRegistry;
import net.id.paradise_lost.registration.RegistrationProvider;
import net.id.paradise_lost.registration.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Set;
import java.util.function.Supplier;

public class ParadiseLostBlockEntityTypes {
    private static final RegistrationProvider<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            RegistrationProvider.get(Registries.BLOCK_ENTITY_TYPE, ModConstants.MODID);

    public static final RegistryObject<BlockEntityType<?>, BlockEntityType<FoodBowlBlockEntity>> FOOD_BOWL = register("food_bowl",
            () -> new BlockEntityType<>(FoodBowlBlockEntity::new, Set.of(BlockRegistry.FOOD_BOWL.get())));
    public static final RegistryObject<BlockEntityType<?>, BlockEntityType<IncubatorBlockEntity>> INCUBATOR = register("incubator",
            () -> new BlockEntityType<>(IncubatorBlockEntity::new, Set.of(BlockRegistry.INCUBATOR.get(), BlockRegistry.NEST.get())));
    public static final RegistryObject<BlockEntityType<?>, BlockEntityType<CherineCampfireBlockEntity>> CHERINE_CAMPFIRE = register("cherine_campfire",
            () -> new BlockEntityType<>(CherineCampfireBlockEntity::new, Set.of(BlockRegistry.CHERINE_CAMPFIRE.get())));
    public static final RegistryObject<BlockEntityType<?>, BlockEntityType<TreeTapBlockEntity>> TREE_TAP = register("tree_tap",
            () -> new BlockEntityType<>(TreeTapBlockEntity::new, Set.of(BlockRegistry.TREE_TAP.get())));
    public static final RegistryObject<BlockEntityType<?>, BlockEntityType<ParadiseSignBlockEntity>> SIGN = register("sign",
            () -> new BlockEntityType<>(ParadiseSignBlockEntity::new, Set.of(BlockRegistry.AUREL_SIGNS.sign().get(), BlockRegistry.AUREL_SIGNS.wallSign().get(),
                    BlockRegistry.MOTHER_AUREL_SIGNS.sign().get(), BlockRegistry.MOTHER_AUREL_SIGNS.wallSign().get(),
                    BlockRegistry.MENTH_SIGNS.sign().get(), BlockRegistry.MENTH_SIGNS.wallSign().get(),
                    BlockRegistry.WISTERIA_SIGNS.sign().get(), BlockRegistry.WISTERIA_SIGNS.wallSign().get())));
    public static final RegistryObject<BlockEntityType<?>, BlockEntityType<ParadiseHangingSignBlockEntity>> HANGING_SIGN = register("hanging_sign",
            () -> new BlockEntityType<>(ParadiseHangingSignBlockEntity::new, Set.of(BlockRegistry.AUREL_SIGNS.hangingSign().get(), BlockRegistry.AUREL_SIGNS.wallHangingSign().get(),
                    BlockRegistry.MOTHER_AUREL_SIGNS.hangingSign().get(), BlockRegistry.MOTHER_AUREL_SIGNS.wallHangingSign().get(),
                    BlockRegistry.MENTH_SIGNS.hangingSign().get(), BlockRegistry.MENTH_SIGNS.wallHangingSign().get(),
                    BlockRegistry.WISTERIA_SIGNS.hangingSign().get(), BlockRegistry.WISTERIA_SIGNS.wallHangingSign().get())));
    public static final RegistryObject<BlockEntityType<?>, BlockEntityType<CalciteDecoratedPotBlockEntity>> CALCITE_DECORATED_POT = register("calcite_decorated_pot",
            () -> new BlockEntityType<>(CalciteDecoratedPotBlockEntity::new, Set.of(BlockRegistry.CALCITE_DECORATED_POT.get())));
    public static final RegistryObject<BlockEntityType<?>, BlockEntityType<PalaceDoorBlockEntity>> PALACE_DOOR = register("palace_door",
            () -> new BlockEntityType<>(PalaceDoorBlockEntity::new, Set.of(BlockRegistry.PALACE_DOOR.get())));

    private ParadiseLostBlockEntityTypes() {}

    public static void init() {

    }

    private static <T extends BlockEntity> RegistryObject<BlockEntityType<?>, BlockEntityType<T>> register(
            String name, Supplier<BlockEntityType<T>> factory) {
        return BLOCK_ENTITY_TYPES.register(name, factory);
    }
}
