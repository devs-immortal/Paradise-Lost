package com.aether;

import com.aether.blocks.AetherBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class AetherEvents {
    private static final Block portalBlock = AetherBlocks.BLUE_PORTAL;

    public static void ServerTickEnd(MinecraftServer server) {
        server.getPlayerManager().getPlayerList().forEach((ServerPlayerEntity player) -> {
            if (player.getY() < 10 && "the_aether".equals(player.world.getRegistryKey().getValue().getPath())) {
                //TODO: Tweak y coordinate and disable fall damage
                player.teleport(server.getWorld(World.OVERWORLD), player.getX(), 200, player.getZ(), player.yaw, player.pitch);
            }
        });
    }

    public static ActionResult UseBlock(PlayerEntity playerEntity, World world, Hand hand, BlockHitResult blockHitResult) {
        // TODO: cry and rewrite
        if (world.getBlockState(blockHitResult.getBlockPos()).getBlock().is(Blocks.GLOWSTONE) && playerEntity.getMainHandStack().getItem() == Items.WATER_BUCKET) {
            BlockPos pos = blockHitResult.getBlockPos();
            Direction dir = blockHitResult.getSide();
            if (dir == Direction.NORTH)
                pos = pos.north();
            else if (dir == Direction.SOUTH)
                pos = pos.south();
            else if (dir == Direction.WEST)
                pos = pos.west();
            else if (dir == Direction.EAST)
                pos = pos.east();
            else if (dir == Direction.DOWN)
                pos = pos.down();
            else if (dir == Direction.UP)
                pos = pos.up();
            if (activatePortal(pos, dir, world)) {
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }

    private static boolean activatePortal(BlockPos pos, Direction side, World world) {
        Pair<Boolean, BlockPos> ns = isValidNSPortal(pos, side, world);
        Pair<Boolean, BlockPos> ew = isValidEWPortal(pos, side, world);

        if (ns.getLeft()) {
            BlockState state = portalBlock.getDefaultState();
            BlockPos curPosOuter = ns.getRight();
            List<BlockPos> portalBlocks = new ArrayList<BlockPos>();
            while (world.getBlockState(curPosOuter).isAir()) {
                BlockPos curPosInner = curPosOuter;
                while (world.getBlockState(curPosInner).isAir()) {
                    world.setBlockState(curPosInner, state);
                    portalBlocks.add(curPosInner);
                    curPosInner = curPosInner.east();
                }
                curPosOuter = curPosOuter.up();
            }
            Pair<Integer, Integer> dimensions = getDimensions(world, ns.getRight(), Direction.Axis.Z);
            BlockPos center = ns.getRight().east(dimensions.getLeft() / 2);

            //TODO: Register portal
            return true;
        }
        if (ew.getLeft()) {
            BlockState state = portalBlock.getDefaultState().rotate(BlockRotation.CLOCKWISE_90);
            BlockPos curPosOuter = ew.getRight();
            List<BlockPos> portalBlocks = new ArrayList<BlockPos>();
            while (world.getBlockState(curPosOuter).isAir()) {
                BlockPos curPosInner = curPosOuter;
                while (world.getBlockState(curPosInner).isAir()) {
                    world.setBlockState(curPosInner, state);
                    portalBlocks.add(curPosInner);
                    curPosInner = curPosInner.north();
                }
                curPosOuter = curPosOuter.up();
            }
            Pair<Integer, Integer> dimensions = getDimensions(world, ew.getRight(), Direction.Axis.X);
            BlockPos center = ew.getRight().north(dimensions.getRight() / 2 - 1);

            //TODO: Register portal
            return true;
        }
        return false;
    }

    private static Pair<Boolean, BlockPos> isValidEWPortal(BlockPos pos, Direction side, World world) {
        Pair<Boolean, BlockPos> invalid = new Pair<>(false, pos);
        if (!(world.getBlockState(pos.up()).isAir() || world.getBlockState(pos.down()).isAir()))
            return invalid;
        BlockPos startPos = pos;
        if (side != Direction.UP) {
            for (int i = 0; world.getBlockState(startPos.down()).isAir(); i++) {
                if (i > 23)
                    return invalid;
                startPos = startPos.down();
            }
        }
        if (!world.getBlockState(startPos.down()).isFullCube(world, startPos.down()))
            return invalid;
        Block frameMaterial = world.getBlockState(startPos.down()).getBlock();
        BlockPos curPos = startPos;
        for (int i = 0; world.getBlockState(curPos.south()).isAir(); i++) {
            if (i > 23 || !world.getBlockState(curPos.down()).isOf(frameMaterial))
                return invalid;
            curPos = curPos.south();
        }
        if (!world.getBlockState(curPos.down()).isOf(frameMaterial))
            return invalid;
        Pair<Boolean, BlockPos> valid = new Pair<>(true, curPos);
        for (int i = 0; world.getBlockState(curPos.up()).isAir(); i++) {
            if (i > 23 || !world.getBlockState(curPos.south()).isOf(frameMaterial))
                return invalid;
            curPos = curPos.up();
        }
        if (!world.getBlockState(curPos.south()).isOf(frameMaterial))
            return invalid;
        for (int i = 0; world.getBlockState(curPos.north()).isAir(); i++) {
            if (i > 23 || !world.getBlockState(curPos.up()).isOf(frameMaterial))
                return invalid;
            curPos = curPos.north();
        }
        if (!world.getBlockState(curPos.up()).getBlock().is(frameMaterial))
            return invalid;
        for (int i = 0; world.getBlockState(curPos.down()).isAir(); i++) {
            if (i > 23 || !world.getBlockState(curPos.north()).isOf(frameMaterial))
                return invalid;
            curPos = curPos.down();
        }
        if (!world.getBlockState(curPos.north()).isOf(frameMaterial))
            return invalid;
        while (!curPos.equals(startPos)) {
            if (!world.getBlockState(curPos).isAir() ||
                    !world.getBlockState(curPos.down()).isOf(frameMaterial))
                return invalid;
            curPos = curPos.south();
        }
        return valid;
    }

    private static Pair<Boolean, BlockPos> isValidNSPortal(BlockPos pos, Direction side, World world) {
        Pair<Boolean, BlockPos> invalid = new Pair<>(false, pos);
        if (!(world.getBlockState(pos.up()).isAir() || world.getBlockState(pos.down()).isAir()))
            return invalid;
        BlockPos startPos = pos;
        if (side != Direction.UP) {
            for (int i = 0; world.getBlockState(startPos.down()).isAir(); i++) {
                if (i > 23)
                    return invalid;
                startPos = startPos.down();
            }
        }
        if (!world.getBlockState(startPos.down()).isFullCube(world, startPos.down()))
            return invalid;
        Block frameMaterial = world.getBlockState(startPos.down()).getBlock();
        BlockPos curPos = startPos;
        for (int i = 0; world.getBlockState(curPos.west()).isAir(); i++) {
            if (i > 23 || !world.getBlockState(curPos.down()).isOf(frameMaterial))
                return invalid;
            curPos = curPos.west();
        }
        if (!world.getBlockState(curPos.down()).isOf(frameMaterial))
            return invalid;
        Pair<Boolean, BlockPos> valid = new Pair<>(true, curPos);
        for (int i = 0; world.getBlockState(curPos.up()).isAir(); i++) {
            if (i > 23 || !world.getBlockState(curPos.west()).isOf(frameMaterial))
                return invalid;
            curPos = curPos.up();
        }
        if (!world.getBlockState(curPos.west()).isOf(frameMaterial))
            return invalid;
        for (int i = 0; world.getBlockState(curPos.east()).isAir(); i++) {
            if (i > 23 || !world.getBlockState(curPos.up()).isOf(frameMaterial))
                return invalid;
            curPos = curPos.east();
        }
        if (!world.getBlockState(curPos.up()).isOf(frameMaterial))
            return invalid;
        for (int i = 0; world.getBlockState(curPos.down()).isAir(); i++) {
            if (i > 23 || !world.getBlockState(curPos.east()).isOf(frameMaterial))
                return invalid;
            curPos = curPos.down();
        }
        if (!world.getBlockState(curPos.east()).isOf(frameMaterial))
            return invalid;
        while (!curPos.equals(startPos)) {
            if (!world.getBlockState(curPos).isAir() ||
                    !world.getBlockState(curPos.down()).isOf(frameMaterial))
                return invalid;
            curPos = curPos.west();
        }
        return valid;
    }

    private static Pair<Integer, Integer> getDimensions(World world, BlockPos corner, Direction.Axis axis) {
        int x = 0;
        int z = 0;
        if (axis != Direction.Axis.X) {
            for (BlockPos curPos = corner; world.getBlockState(curPos).isOf(portalBlock); curPos = curPos.east()) {
                x++;
            }
        }
        if (axis != Direction.Axis.Z) {
            for (BlockPos curPos = corner; world.getBlockState(curPos).isOf(portalBlock); curPos = curPos.north()) {
                z++;
            }
        }
        return new Pair<Integer, Integer>(x, z);
    }
}
