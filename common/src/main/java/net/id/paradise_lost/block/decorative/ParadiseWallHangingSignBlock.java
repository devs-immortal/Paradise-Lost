package net.id.paradise_lost.block.decorative;

import net.id.paradise_lost.block.blockentity.ParadiseHangingSignBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;

public class ParadiseWallHangingSignBlock extends WallHangingSignBlock {

    public ParadiseWallHangingSignBlock(WoodType woodType, Properties settings) {
        super(woodType, settings);
    }

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ParadiseHangingSignBlockEntity(pos, state);
    }

}
