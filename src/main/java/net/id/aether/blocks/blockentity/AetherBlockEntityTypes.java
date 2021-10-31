package net.id.aether.blocks.blockentity;

import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.id.aether.Aether;
import net.id.aether.blocks.AetherBlocks;
import net.id.aether.blocks.blockentity.dungeon.DungeonSwitchBlockEntity;
import net.id.aether.blocks.dungeon.DungeonSwitchBlock;
import net.id.aether.client.rendering.block.IncubatorBlockEntityRenderer;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;

import static net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder.create;
import static net.id.aether.Aether.locate;

public class AetherBlockEntityTypes {
    public static final BlockEntityType<FoodBowlBlockEntity> FOOD_BOWL = create(FoodBowlBlockEntity::new, AetherBlocks.FOOD_BOWL).build();
    public static final BlockEntityType<IncubatorBlockEntity> INCUBATOR = create(IncubatorBlockEntity::new, AetherBlocks.INCUBATOR).build();
    public static final BlockEntityType<DungeonSwitchBlockEntity> DUNGEON_SWITCH = create(DungeonSwitchBlockEntity::new, AetherBlocks.DUNGEON_SWITCH).build();

    public static void init() {
        Registry.register(Registry.BLOCK_ENTITY_TYPE, locate("food_bowl"), FOOD_BOWL);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, locate("incubator"), INCUBATOR);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, locate("dungeonswitch"), DUNGEON_SWITCH);
    }

    public static void initClient() {
        BlockEntityRendererRegistry.register(INCUBATOR, IncubatorBlockEntityRenderer::new);
    }
}
