package net.id.paradise_lost.block.decorative;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class CalciteFlowerPotBlock extends FlowerPotBlock {

    public static final BooleanProperty IS_CALCITE = BooleanProperty.create("calcite");

    public CalciteFlowerPotBlock(Properties settings) {
        super(Blocks.AIR, settings);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return defaultBlockState().setValue(IS_CALCITE, true);
    }
}
