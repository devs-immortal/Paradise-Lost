package net.id.paradiselost.blocks.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.util.math.BlockPos;

public class ParadiseHangingSignBlockEntity extends SignBlockEntity {
    private static final int MAX_TEXT_WIDTH = 60;
    private static final int TEXT_LINE_HEIGHT = 9;

    public ParadiseHangingSignBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ParadiseLostBlockEntityTypes.HANGING_SIGN, blockPos, blockState);
    }

    public int getTextLineHeight() {
        return 9;
    }

    public int getMaxTextWidth() {
        return 60;
    }
}
