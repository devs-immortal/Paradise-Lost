package com.aether.mixin.item;

import com.aether.Aether;
import com.aether.blocks.AetherBlocks;
import com.aether.blocks.AetherPortalBlock;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.*;

@Mixin(BucketItem.class)
public class BucketItemMixin extends Item {
    public BucketItemMixin(Settings settings) {
        super(settings);
    }

    @Unique
    private Set<Block> compatiblePortalBlocks = ImmutableSet.of(Blocks.GLOWSTONE);

    @Shadow
    @Final
    private Fluid fluid;

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    public void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        Aether.modLogger.error("1");
        if (!user.isSneaking() && fluid.matchesType(Fluids.WATER)) {
            Aether.modLogger.error("2");
            BlockHitResult result = raycast(world, user, FluidHandling.NONE);
            if (result.getType() == HitResult.Type.BLOCK) {
                Aether.modLogger.error("3");
                if (compatiblePortalBlocks.contains(world.getBlockState(result.getBlockPos()).getBlock())) {
                    Aether.modLogger.error("4");
                    BlockPos pos = result.getBlockPos();
                    if (buildPortal(world, user, pos, (compatiblePortalBlocks.contains(world.getBlockState(pos.offset(Direction.NORTH)).getBlock()) || compatiblePortalBlocks.contains(world.getBlockState(pos.offset(Direction.SOUTH)).getBlock())))) {
                        Aether.modLogger.error("5");
                        cir.setReturnValue(TypedActionResult.success(ItemUsage.method_30012(user.getStackInHand(hand), user, new ItemStack(fluid.getBucketItem())), world.isClient()));
                    }
                }
            }
        }
    }

    private boolean buildPortal(World world, PlayerEntity user, BlockPos pos, boolean NS) {
        List<Direction> dirs = (NS ?
            ImmutableList.of(Direction.NORTH, Direction.UP, Direction.SOUTH, Direction.DOWN) :
            ImmutableList.of(Direction.EAST, Direction.UP, Direction.WEST, Direction.DOWN));
        Set<BlockPos> frameScan = verifyFrame(world, pos, dirs);
        boolean valid = false;
        if (!frameScan.isEmpty()) {
            Aether.modLogger.error("1B");
            BlockPos testPos = pos;
            while (testPos.getY() < world.getHeight()) {
                testPos = testPos.up();
                if (compatiblePortalBlocks.contains(world.getBlockState(testPos).getBlock()) && frameScan.contains(testPos)) {
                    Aether.modLogger.error("2B");
                    valid = true;
                    break;
                }
            }
            if (valid) {
                Aether.modLogger.error("3B");
                BlockPos startPos = pos.up();
                floodFill(world, startPos, dirs, AetherBlocks.BLUE_PORTAL.getDefaultState().with(AetherPortalBlock.AXIS, NS ? Direction.Axis.Z : Direction.Axis.X), startPos);
            }
        }
        return valid;
    }

    private void floodFill(World world, BlockPos start, List<Direction> directions, BlockState portal, BlockPos curPos) {
        if (MathHelper.sqrt(curPos.getSquaredDistance(start)) <= 64) {
            world.setBlockState(curPos, portal);
            for (Direction dir : directions) {
                BlockPos probePos = curPos.offset(dir);
                if (world.getBlockState(probePos).isAir())
                    floodFill(world, start, directions, portal, probePos);
            }
        }
    }

    private Set<BlockPos> verifyFrame(World world, BlockPos start, List<Direction> directions) {
        Aether.modLogger.error(start);
        BlockPos pos = start;
        boolean valid = false;
        Set<BlockPos> path = new HashSet<BlockPos>();
        boolean endpoint = false;
        pathfinder: while (true) {
            for (Direction dir : directions) {
                BlockPos probePos = pos.offset(dir);
                if ((path.size() >= 8 && probePos.equals(start))) {
                    Aether.modLogger.error("1C");
                    valid = true;
                    break pathfinder;
                }
                if (!path.contains(probePos) && !(path.size() < 8 && probePos.equals(start)) && compatiblePortalBlocks.contains(world.getBlockState(probePos).getBlock())) {
                    Aether.modLogger.info(probePos);
                    path.add(probePos);
                    pos = probePos;
                    endpoint = false;
                }
            }
            if (endpoint) {
                Aether.modLogger.error("2C");
                break;
            }
            endpoint = true;
        }
        return valid ? path : ImmutableSet.of();
    }
}