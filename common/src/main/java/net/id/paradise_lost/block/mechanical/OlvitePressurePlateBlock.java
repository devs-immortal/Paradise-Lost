package net.id.paradise_lost.block.mechanical;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class OlvitePressurePlateBlock extends PressurePlateBlock {
    public OlvitePressurePlateBlock(Properties settings) {
        super(BlockSetType.IRON, settings);
    }

    @Override
    protected int getSignalStrength(Level world, BlockPos pos) {
        return getEntityCount(world, TOUCH_AABB.move(pos), Player.class) > 0 ? 15 : 0;
    }

}
