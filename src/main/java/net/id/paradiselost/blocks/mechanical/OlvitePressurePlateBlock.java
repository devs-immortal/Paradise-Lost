package net.id.paradiselost.blocks.mechanical;

import net.minecraft.block.BlockSetType;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class OlvitePressurePlateBlock extends PressurePlateBlock {
    public OlvitePressurePlateBlock(Settings settings) {
        super(BlockSetType.IRON, settings);
    }

    @Override
    protected int getRedstoneOutput(World world, BlockPos pos) {
        return getEntityCount(world, BOX.offset(pos), PlayerEntity.class) > 0 ? 15 : 0;
    }

}
