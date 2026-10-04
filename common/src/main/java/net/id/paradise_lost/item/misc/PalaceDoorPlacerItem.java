package net.id.paradise_lost.item.misc;

import net.id.paradise_lost.registry.BlockRegistry;
import net.id.paradise_lost.block.mechanical.PalaceDoorBlock;
import net.id.paradise_lost.block.mechanical.PalaceDoorExtensionBlock;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;

public class PalaceDoorPlacerItem extends Item {

    public PalaceDoorPlacerItem(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (!context.getLevel().isClientSide) {
            var player = context.getPlayer();
            var pos = context.getClickedPos().above();
            var facing = context.getHorizontalDirection();
            var world = context.getLevel();

            for (int x = -2; x <= 2; x++) {
                for (int z = -2; z <= 2; z++) {
                    for (int y = -6; y <= 6; y++) {
                        var blockAt = world.getBlockState(pos.offset(x, y, z)).getBlock();
                        if (blockAt instanceof PalaceDoorBlock) {
                            blockAt.playerWillDestroy(world, pos.offset(x, y, z), world.getBlockState(pos.offset(x, y, z)), player);
                            world.setBlockAndUpdate(pos.offset(x, y, z), Blocks.AIR.defaultBlockState());
                        }
                    }
                }
            }

            var doorState = BlockRegistry.PALACE_DOOR.get().defaultBlockState().setValue(PalaceDoorBlock.FACING, facing);
            var extensionState = BlockRegistry.PALACE_DOOR_EXTENSION.get().defaultBlockState().setValue(PalaceDoorExtensionBlock.FACING, facing);

            for (int y = 0; y < 7; y++) {
                world.setBlockAndUpdate(pos.above(y), extensionState);
                world.setBlockAndUpdate(pos.above(y).relative(facing.getCounterClockWise()), extensionState);
                world.setBlockAndUpdate(pos.above(y).relative(facing.getClockWise()), extensionState);
            }
            world.setBlockAndUpdate(pos.above(4), doorState);

            if (player != null && !player.isCreative()) {
                context.getItemInHand().shrink(1);
            }
        }

        return InteractionResult.SUCCESS;
    }

}
