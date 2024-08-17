package net.id.paradiselost.blocks.blockentity;


import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.HangingSignBlockEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import static net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder.create;
import static net.id.paradiselost.ParadiseLost.locate;

public class ParadiseLostBlockEntityTypes {
    public static final BlockEntityType<FoodBowlBlockEntity> FOOD_BOWL = create(FoodBowlBlockEntity::new, ParadiseLostBlocks.FOOD_BOWL).build();
    public static final BlockEntityType<IncubatorBlockEntity> INCUBATOR = create(IncubatorBlockEntity::new, ParadiseLostBlocks.INCUBATOR).build();
    public static final BlockEntityType<CherineCampfireBlockEntity> CHERINE_CAMPFIRE = create(CherineCampfireBlockEntity::new, ParadiseLostBlocks.CHERINE_CAMPFIRE).build();
	public static final BlockEntityType<TreeTapBlockEntity> TREE_TAP = create(TreeTapBlockEntity::new, ParadiseLostBlocks.TREE_TAP).build();
    public static final BlockEntityType<ParadiseSignBlockEntity> SIGN = create(ParadiseSignBlockEntity::new,
            ParadiseLostBlocks.AUREL_SIGNS.sign(), ParadiseLostBlocks.AUREL_SIGNS.wallSign(),
            ParadiseLostBlocks.MOTHER_AUREL_SIGNS.sign(), ParadiseLostBlocks.MOTHER_AUREL_SIGNS.wallSign(),
            ParadiseLostBlocks.ORANGE_SIGNS.sign(), ParadiseLostBlocks.ORANGE_SIGNS.wallSign(),
            ParadiseLostBlocks.WISTERIA_SIGNS.sign(), ParadiseLostBlocks.WISTERIA_SIGNS.wallSign()
    ).build();
    public static final BlockEntityType<ParadiseHangingSignBlockEntity> HANGING_SIGN = create(ParadiseHangingSignBlockEntity::new,
            ParadiseLostBlocks.AUREL_SIGNS.hangingSign(), ParadiseLostBlocks.AUREL_SIGNS.wallHangingSign(),
            ParadiseLostBlocks.MOTHER_AUREL_SIGNS.hangingSign(), ParadiseLostBlocks.MOTHER_AUREL_SIGNS.wallHangingSign(),
            ParadiseLostBlocks.ORANGE_SIGNS.hangingSign(), ParadiseLostBlocks.ORANGE_SIGNS.wallHangingSign(),
            ParadiseLostBlocks.WISTERIA_SIGNS.hangingSign(), ParadiseLostBlocks.WISTERIA_SIGNS.wallHangingSign()
    ).build();

    public static void init() {
        register("food_bowl", FOOD_BOWL);
        register("incubator", INCUBATOR);
        register("cherine_campfire", CHERINE_CAMPFIRE);
        register("tree_tap", TREE_TAP);
        register("sign", SIGN);
        register("hanging_sign", HANGING_SIGN);
    }
    
    private static void register(String name, BlockEntityType<?> type) {
        Registry.register(Registries.BLOCK_ENTITY_TYPE, locate(name), type);
    }
}
