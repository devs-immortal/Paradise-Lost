package net.id.aether.blocks.blockentity;

import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.id.aether.blocks.AetherBlocks;
import net.id.aether.client.rendering.block.IncubatorBlockEntityRenderer;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;

import static net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder.create;
import static net.id.aether.Aether.locate;

public class AetherBlockEntityTypes {
    public static void init() {
        Registry.register(Registry.BLOCK_ENTITY_TYPE, locate("food_bowl"), FOOD_BOWL);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, locate("incubator"), INCUBATOR);
    }    public static final BlockEntityType<FoodBowlBlockEntity> FOOD_BOWL = create(FoodBowlBlockEntity::new, AetherBlocks.FOOD_BOWL).build();

    public static void initClient() {
        BlockEntityRendererRegistry.INSTANCE.register(INCUBATOR, IncubatorBlockEntityRenderer::new);
    }    public static final BlockEntityType<IncubatorBlockEntity> INCUBATOR = create(IncubatorBlockEntity::new, AetherBlocks.INCUBATOR).build();




}
