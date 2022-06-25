package net.id.paradiselost.blocks.blockentity;


import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;

import static net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder.create;
import static net.id.paradiselost.ParadiseLost.locate;

public class ParadiseLostBlockEntityTypes {
    public static final BlockEntityType<FoodBowlBlockEntity> FOOD_BOWL = create(FoodBowlBlockEntity::new, ParadiseLostBlocks.FOOD_BOWL).build();
    public static final BlockEntityType<IncubatorBlockEntity> INCUBATOR = create(IncubatorBlockEntity::new, ParadiseLostBlocks.INCUBATOR).build();
    public static final BlockEntityType<AmbrosiumCampfireBlockEntity> AMBROSIUM_CAMPFIRE = create(AmbrosiumCampfireBlockEntity::new, ParadiseLostBlocks.AMBROSIUM_CAMPFIRE).build();
//    public static final BlockEntityType<DungeonSwitchBlockEntity> DUNGEON_SWITCH = create(DungeonSwitchBlockEntity::new, ParadiseLostBlocks.DUNGEON_SWITCH).build();
    
    public static void init() {
        register("food_bowl", FOOD_BOWL);
        register("incubator", INCUBATOR);
        register("ambrosium_campfire", AMBROSIUM_CAMPFIRE);
//        register("dungeonswitch", DUNGEON_SWITCH);
    }
    
    private static void register(String name, BlockEntityType<?> type){
        Registry.register(Registry.BLOCK_ENTITY_TYPE, locate(name), type);
    }
}
