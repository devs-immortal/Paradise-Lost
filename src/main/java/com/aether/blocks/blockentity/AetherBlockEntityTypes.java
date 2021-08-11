package com.aether.blocks.blockentity;

import com.aether.blocks.AetherBlocks;
import com.aether.client.rendering.block.IncubatorBlockEntityRenderer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;

import static com.aether.Aether.locate;
import static net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder.create;

public class AetherBlockEntityTypes {
    public static final BlockEntityType<FoodBowlBlockEntity> FOOD_BOWL = create(FoodBowlBlockEntity::new, AetherBlocks.FOOD_BOWL).build();
    public static final BlockEntityType<IncubatorBlockEntity> INCUBATOR = create(IncubatorBlockEntity::new, AetherBlocks.INCUBATOR).build();

    public static void init() {
        Registry.register(Registry.BLOCK_ENTITY_TYPE, locate("food_bowl"), FOOD_BOWL);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, locate("incubator"), INCUBATOR);
    }

    public static void initClient() {
        BlockEntityRendererRegistry.INSTANCE.register(INCUBATOR, IncubatorBlockEntityRenderer::new);
    }
}
