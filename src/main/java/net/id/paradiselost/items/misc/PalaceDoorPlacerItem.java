package net.id.paradiselost.items.misc;

import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.id.paradiselost.blocks.mechanical.PalaceDoorBlock;
import net.id.paradiselost.blocks.mechanical.PalaceDoorExtensionBlock;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;

public class PalaceDoorPlacerItem extends Item {

    public PalaceDoorPlacerItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (!context.getWorld().isClient) {
            var player = context.getPlayer();
            var pos = context.getBlockPos().up();
            var facing = context.getHorizontalPlayerFacing();
            var world = context.getWorld();

            // remove adjacent doors that are too close
            for (int x = -2; x <= 2; x++) {
                for (int z = -2; z <= 2; z++) {
                    for (int y = -6; y <= 6; y++) {
                        var blockAt = world.getBlockState(pos.add(x, y, z)).getBlock();
                        if (blockAt instanceof PalaceDoorBlock) {
                            blockAt.onBreak(world, pos.add(x, y, z), world.getBlockState(pos.add(x, y, z)), player);
                            world.setBlockState(pos.add(x, y, z), Blocks.AIR.getDefaultState());
                        }
                    }
                }
            }

            // place the door
            var doorState = ParadiseLostBlocks.PALACE_DOOR.getDefaultState().with(PalaceDoorBlock.FACING, facing);
            var extensionState = ParadiseLostBlocks.PALACE_DOOR_EXTENSION.getDefaultState().with(PalaceDoorExtensionBlock.FACING, facing);

            for (int y = 0; y < 7; y++) {
                world.setBlockState(pos.up(y), extensionState);
                world.setBlockState(pos.up(y).offset(facing.rotateYCounterclockwise()), extensionState);
                world.setBlockState(pos.up(y).offset(facing.rotateYClockwise()), extensionState);
            }
            world.setBlockState(pos.up(4), doorState);

            if (player != null && !player.isCreative()) {
                context.getStack().decrement(1);
            }
        }

        return ActionResult.SUCCESS;
    }

}
