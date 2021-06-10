package com.aether.items;

import com.aether.blocks.AetherBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class AetherPortalItem extends Item {
	private static final byte[][] GLOWSTONE = new byte[][] {
			new byte[] {-1, 0, 0},
			new byte[] {0, 0, 0},
			new byte[] {1, 0, 0},
			new byte[] {1, 1, 0},
			new byte[] {1, 2, 0},
			new byte[] {1, 3, 0},
			new byte[] {0, 3, 0},
			new byte[] {-1, 3, 0},
			new byte[] {-1, 2, 0},
			new byte[] {-1, 1, 0}
	};

	public AetherPortalItem(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		if (!context.getWorld().isClient) {
			BlockPos.Mutable mut = new BlockPos.Mutable();

			for (byte[] b : GLOWSTONE) {
				mut.set(context.getBlockPos());
				mut.move(
						context.getPlayerFacing().getAxis() == Direction.Axis.X ? b[2] : b[0],
						b[1],
						context.getPlayerFacing().getAxis() == Direction.Axis.X ? b[0] : b[2]
				);

				context.getWorld().setBlockState(mut, Blocks.GLOWSTONE.getDefaultState());
			}

			mut.set(context.getBlockPos());

			for (byte i = 0; i < 2; ++i) {
				mut.move(Direction.UP);
				//context.getWorld().setBlockState(mut, AetherBlocks.BLUE_PORTAL.getDefaultState().with(Properties.HORIZONTAL_AXIS, context.getPlayerFacing().rotateYClockwise().getAxis()));
			}

			if (context.getPlayer() != null && !context.getPlayer().isCreative()) {
				context.getStack().decrement(1);
			}

			return ActionResult.SUCCESS;
		}

		return ActionResult.SUCCESS;
	}
}
