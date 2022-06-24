package net.id.paradiselost.blocks.blockentity;


import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.id.paradiselost.mixin.block.ChestBlockEntityAccessor;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.util.registry.Registry;

import static net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder.create;
import static net.id.paradiselost.ParadiseLost.locate;

public class ParadiseLostBlockEntityTypes {
    public static final BlockEntityType<FoodBowlBlockEntity> FOOD_BOWL = create(FoodBowlBlockEntity::new, ParadiseLostBlocks.FOOD_BOWL).build();
    public static final BlockEntityType<IncubatorBlockEntity> INCUBATOR = create(IncubatorBlockEntity::new, ParadiseLostBlocks.INCUBATOR).build();
//    public static final BlockEntityType<DungeonSwitchBlockEntity> DUNGEON_SWITCH = create(DungeonSwitchBlockEntity::new, ParadiseLostBlocks.DUNGEON_SWITCH).build();
    
    public static final BlockEntityType<ChestBlockEntity> SKYROOT_CHEST = create((pos, state)->ChestBlockEntityAccessor.init(ParadiseLostBlockEntityTypes.SKYROOT_CHEST, pos, state), ParadiseLostBlocks.SKYROOT_CHEST).build();
    public static final BlockEntityType<ChestBlockEntity> GOLDEN_OAK_CHEST = create((pos, state)->ChestBlockEntityAccessor.init(ParadiseLostBlockEntityTypes.GOLDEN_OAK_CHEST, pos, state), ParadiseLostBlocks.GOLDEN_OAK_CHEST).build();
    public static final BlockEntityType<ChestBlockEntity> ORANGE_CHEST = create((pos, state)->ChestBlockEntityAccessor.init(ParadiseLostBlockEntityTypes.ORANGE_CHEST, pos, state), ParadiseLostBlocks.ORANGE_CHEST).build();
    public static final BlockEntityType<ChestBlockEntity> CRYSTAL_CHEST = create((pos, state)->ChestBlockEntityAccessor.init(ParadiseLostBlockEntityTypes.CRYSTAL_CHEST, pos, state), ParadiseLostBlocks.CRYSTAL_CHEST).build();
    public static final BlockEntityType<ChestBlockEntity> WISTERIA_CHEST = create((pos, state)->ChestBlockEntityAccessor.init(ParadiseLostBlockEntityTypes.WISTERIA_CHEST, pos, state), ParadiseLostBlocks.WISTERIA_CHEST).build();

    public static void init() {
        register("food_bowl", FOOD_BOWL);
        register("incubator", INCUBATOR);
//        register("dungeonswitch", DUNGEON_SWITCH);
    
        register("skyroot_chest", SKYROOT_CHEST);
        register("golden_oak_chest", GOLDEN_OAK_CHEST);
        register("orange_chest", ORANGE_CHEST);
        register("crystal_chest", CRYSTAL_CHEST);
        register("wisteria_chest", WISTERIA_CHEST);
    }
    
    private static void register(String name, BlockEntityType<?> type){
        Registry.register(Registry.BLOCK_ENTITY_TYPE, locate(name), type);
    }
}
