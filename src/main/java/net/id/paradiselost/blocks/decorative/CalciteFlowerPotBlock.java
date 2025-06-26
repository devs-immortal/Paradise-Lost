package net.id.paradiselost.blocks.decorative;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.property.BooleanProperty;

public class CalciteFlowerPotBlock extends FlowerPotBlock {

    public static final BooleanProperty IS_CALCITE = BooleanProperty.of("calcite");

    public CalciteFlowerPotBlock(Settings settings) {
        super(Blocks.AIR, settings);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(IS_CALCITE, true);
    }
}
