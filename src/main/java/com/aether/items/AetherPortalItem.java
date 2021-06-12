package com.aether.items;

import com.aether.blocks.AetherBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;

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

	public AetherPortalItem(Properties settings) {
		super(settings);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		if (!context.getLevel().isClientSide) {
			BlockPos.MutableBlockPos mut = new BlockPos.MutableBlockPos();

			for (byte[] b : GLOWSTONE) {
				mut.set(context.getClickedPos());
				mut.move(
						context.getHorizontalDirection().getAxis() == Direction.Axis.X ? b[2] : b[0],
						b[1],
						context.getHorizontalDirection().getAxis() == Direction.Axis.X ? b[0] : b[2]
				);

				context.getLevel().setBlockAndUpdate(mut, Blocks.GLOWSTONE.defaultBlockState());
			}

			mut.set(context.getClickedPos());

			for (byte i = 0; i < 2; ++i) {
				mut.move(Direction.UP);
				//context.getWorld().setBlockState(mut, AetherBlocks.BLUE_PORTAL.getDefaultState().with(Properties.HORIZONTAL_AXIS, context.getPlayerFacing().rotateYClockwise().getAxis()));
			}

			if (context.getPlayer() != null && !context.getPlayer().isCreative()) {
				context.getItemInHand().shrink(1);
			}

			return InteractionResult.SUCCESS;
		}

		return InteractionResult.SUCCESS;
	}
}
