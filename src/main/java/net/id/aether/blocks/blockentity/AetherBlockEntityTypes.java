package net.id.aether.blocks.blockentity;


import net.id.aether.blocks.AetherBlocks;
import net.id.aether.mixin.block.ChestBlockEntityAccessor;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.util.registry.Registry;

import static net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder.create;
import static net.id.aether.Aether.locate;

public class AetherBlockEntityTypes {
    public static final BlockEntityType<FoodBowlBlockEntity> FOOD_BOWL = create(FoodBowlBlockEntity::new, AetherBlocks.FOOD_BOWL).build();
    public static final BlockEntityType<IncubatorBlockEntity> INCUBATOR = create(IncubatorBlockEntity::new, AetherBlocks.INCUBATOR).build();
    public static final BlockEntityType<AmbrosiumCampfireBlockEntity> AMBROSIUM_CAMPFIRE = create(AmbrosiumCampfireBlockEntity::new, AetherBlocks.AMBROSIUM_CAMPFIRE).build();
//    public static final BlockEntityType<DungeonSwitchBlockEntity> DUNGEON_SWITCH = create(DungeonSwitchBlockEntity::new, AetherBlocks.DUNGEON_SWITCH).build();
    
    public static final BlockEntityType<ChestBlockEntity> SKYROOT_CHEST = create((pos, state)->ChestBlockEntityAccessor.init(AetherBlockEntityTypes.SKYROOT_CHEST, pos, state), AetherBlocks.SKYROOT_CHEST).build();
    public static final BlockEntityType<ChestBlockEntity> GOLDEN_OAK_CHEST = create((pos, state)->ChestBlockEntityAccessor.init(AetherBlockEntityTypes.GOLDEN_OAK_CHEST, pos, state), AetherBlocks.GOLDEN_OAK_CHEST).build();
    public static final BlockEntityType<ChestBlockEntity> ORANGE_CHEST = create((pos, state)->ChestBlockEntityAccessor.init(AetherBlockEntityTypes.ORANGE_CHEST, pos, state), AetherBlocks.ORANGE_CHEST).build();
    public static final BlockEntityType<ChestBlockEntity> CRYSTAL_CHEST = create((pos, state)->ChestBlockEntityAccessor.init(AetherBlockEntityTypes.CRYSTAL_CHEST, pos, state), AetherBlocks.CRYSTAL_CHEST).build();
    public static final BlockEntityType<ChestBlockEntity> WISTERIA_CHEST = create((pos, state)->ChestBlockEntityAccessor.init(AetherBlockEntityTypes.WISTERIA_CHEST, pos, state), AetherBlocks.WISTERIA_CHEST).build();

    public static void init() {
        register("food_bowl", FOOD_BOWL);
        register("incubator", INCUBATOR);
        register("ambrosium_campfire", AMBROSIUM_CAMPFIRE);
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
