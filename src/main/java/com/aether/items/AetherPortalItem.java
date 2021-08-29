package com.aether.items;

import net.kyrptonaught.customportalapi.portal.PortalIgnitionSource;
import net.kyrptonaught.customportalapi.portal.PortalPlacer;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;

public class AetherPortalItem extends Item {
	private static final byte[][] GLOWSTONE = new byte[][] {
			new byte[] {-2, 0, 0},
			new byte[] {-1, 0, 0},
			new byte[] {0, 0, 0},
			new byte[] {1, 0, 0},
			new byte[] {2, 0, 0},
			new byte[] {2, 1, 0},
			new byte[] {2, 2, 0},
			new byte[] {2, 3, 0},
			new byte[] {2, 4, 0},
			new byte[] {1, 4, 0},
			new byte[] {0, 4, 0},
			new byte[] {-1, 4, 0},
			new byte[] {-2, 4, 0},
			new byte[] {-2, 3, 0},
			new byte[] {-2, 2, 0},
			new byte[] {-2, 1, 0}
	};

	public AetherPortalItem(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		if (!context.getWorld().isClient) {
			BlockPos.Mutable mut = new BlockPos.Mutable();

			// frame
			for (byte[] b : GLOWSTONE) {
				mut.set(context.getBlockPos());
				mut.move(
						context.getPlayerFacing().getAxis() == Direction.Axis.X ? b[2] : b[0],
						b[1],
						context.getPlayerFacing().getAxis() == Direction.Axis.X ? b[0] : b[2]
				);

				context.getWorld().setBlockState(mut, Blocks.GLOWSTONE.getDefaultState());
			}

			// clear the inside
			mut.set(context.getBlockPos());
			mut.move(context.getPlayerFacing().rotateYCounterclockwise());
			for (int i = -1; i < 2; i++){
				for (int j = -1; j < 2; j++){
					mut.move(Direction.UP);
					context.getWorld().removeBlock(mut, false);
				}
				mut.move(context.getPlayerFacing().rotateYClockwise());
				mut.move(Vec3i.ZERO.down().down().down());
			}

			mut.set(context.getBlockPos());
			PortalPlacer.attemptPortalLight(context.getWorld(), mut.up(), mut, PortalIgnitionSource.WATER);

			if (context.getPlayer() != null && !context.getPlayer().isCreative()) {
				context.getStack().decrement(1);
			}
		}

		return ActionResult.SUCCESS;
	}
}
