package net.id.aether.entities.util.floatingblock;

import net.gudenau.minecraft.moretags.MoreTags;
import net.id.aether.blocks.AetherBlocks;
import net.id.aether.entities.block.FloatingBlockEntity;
import net.id.aether.tag.AetherBlockTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PistonBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.block.piston.PistonHandler;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.function.Function;

public class FloatingBlockHelper {
    public static final Function<FloatingBlockEntity, Boolean> DEFAULT_DROP_STATE = (entity) -> {
        World world = entity.world;
        BlockPos pos = entity.getBlockPos();
        int distFromTop = world.getTopY() - pos.getY();
        return !entity.isFastFloater() && distFromTop <= 50;
    };

    public static boolean tryCreatePusher(World world, BlockPos pos) {
        boolean dropping = willBlockDrop(world, pos, world.getBlockState(pos), true);
        if (dropping) {
            return tryCreateGeneric(world, pos);
        }

        FloatingBlockStructure structure = FloatingBlockPusherHandler.construct(world, pos);
        if (structure != null) {
            if (structure.blockInfos.size() == 1) {
                world.spawnEntity(structure.blockInfos.get(0).block);
            } else {
                structure.spawn(world);
            }
            return true;
        } else {
            return false;
        }
    }

    public static boolean tryCreateDouble(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        boolean dropping = willBlockDrop(world, pos, state, true);
        if (!canCreateDouble(world, pos, dropping)) {
            return false;
        }
        if (state.get(Properties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER) {
            pos = pos.down();
            state = world.getBlockState(pos);
        }
        BlockState upperState = world.getBlockState(pos.up());
        FloatingBlockEntity upper = new FloatingBlockEntity(world, pos.up(), upperState, true);
        FloatingBlockEntity lower = new FloatingBlockEntity(world, pos, state, true);
        upper.dropItem = false;
        FloatingBlockStructure structure = new FloatingBlockStructure(lower, upper, Vec3i.ZERO.up());
        structure.spawn(world);
        return true;
    }

    public static boolean tryCreateGeneric(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        boolean dropping = willBlockDrop(world, pos, state, false);
        if (!canCreateGeneric(world, pos, dropping)) {
            return false;
        }
        FloatingBlockEntity entity = new FloatingBlockEntity(world, pos, state, false);

        if (state.isOf(Blocks.TNT)) {
            entity.setOnEndFloating((impact, landed) -> {
                if (impact >= 0.8) {
                    BlockPos landingPos = entity.getBlockPos();
                    world.breakBlock(landingPos, false);
                    world.createExplosion(entity, landingPos.getX(), landingPos.getY(), landingPos.getZ(), (float) MathHelper.clamp(impact * 5.5, 0, 10), Explosion.DestructionType.BREAK);
                }
            });
        }
        if (state.isOf(Blocks.LIGHTNING_ROD)) {
            entity.setOnEndFloating((impact, landed) -> {
                if (world.isThundering() && landed && impact >= 1.1) {
                    LightningEntity lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
                    lightning.setPosition(Vec3d.ofCenter(entity.getBlockPos()));
                    world.spawnEntity(lightning);
                }
            });
        }
        world.spawnEntity(entity);
        return true;
    }

    private static boolean canCreateDouble(World world, BlockPos pos, boolean dropping) {
        BlockState state = world.getBlockState(pos);
        if (state.get(Properties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER) {
            return FloatingBlockEntity.canMakeBlock(dropping, world.getBlockState(pos.down()), world.getBlockState(pos.up().up()));
        } else {
            return FloatingBlockEntity.canMakeBlock(dropping, world.getBlockState(pos.down().down()), world.getBlockState(pos.up()));
        }
    }

    private static boolean canCreateGeneric(World world, BlockPos pos, boolean dropping) {
        return FloatingBlockEntity.canMakeBlock(dropping, world.getBlockState(pos.down()), world.getBlockState(pos.up()));
    }

    public static boolean willBlockDrop(World world, BlockPos pos, BlockState state, boolean partOfStructure) {
        FloatingBlockEntity entity = new FloatingBlockEntity(world, pos, state, partOfStructure);
        boolean willDrop = entity.getDropState().get();
        entity.discard();
        return willDrop;
    }

    public static boolean isToolAdequate(ItemUsageContext context) {
        BlockPos pos = context.getBlockPos();
        World world = context.getWorld();
        BlockState state = world.getBlockState(pos);
        Item heldItem = context.getStack().getItem();
        return world.getBlockEntity(pos) == null && state.getHardness(world, pos) != -1.0F
                && (!state.isToolRequired() || heldItem.isSuitableFor(state))
                && !AetherBlockTags.NON_FLOATERS.contains(state.getBlock());
    }

    public static class FloatingBlockPusherHandler {
        public static int MAX_MOVABLE_BLOCKS = PistonHandler.MAX_MOVABLE_BLOCKS;

        @Nullable
        public static FloatingBlockStructure construct(World world, BlockPos pos) {
            if (!world.getBlockState(pos).isOf(AetherBlocks.GRAVITITE_LEVITATOR)) {
                return null;
            }
            ArrayList<FloatingBlockStructure.FloatingBlockInfoWrapper> infos = new ArrayList<>(0);
            Vec3i offset = Vec3i.ZERO;
            if (continueTree(world, pos, offset, infos)) {
                return new FloatingBlockStructure(infos);
            } else {
                return null;
            }
        }

        // returns false if the tree is unable to move. returns true otherwise.
        private static boolean continueTree(World world, BlockPos origin, Vec3i offset, ArrayList<FloatingBlockStructure.FloatingBlockInfoWrapper> infos) {
            if (infos.size() > MAX_MOVABLE_BLOCKS + 1) {
                return false;
            }
            BlockPos pos = origin.add(offset);
            BlockState state = world.getBlockState(pos);

            if (state.isAir()
                    || !PistonBlock.isMovable(state, world, pos, Direction.UP, false, Direction.UP)
                    || alreadyCounted(infos, offset)) {
                return true;
            }
            // adds the block to the structure
            FloatingBlockEntity newBlock = new FloatingBlockEntity(world, pos, state, true);
            infos.add(new FloatingBlockStructure.FloatingBlockInfoWrapper(newBlock, offset));
            // check if above block is movable
            if (!PistonBlock.isMovable(world.getBlockState(pos.up()), world, pos.up(), Direction.UP, true, Direction.UP)) {
                return false;
            }
            // check if rest of tree above is movable
            if (!continueTree(world, origin, offset.up(), infos)) {
                return false;
            }
            // sides and bottom (sticky blocks)
            if (MoreTags.STICKY_BLOCKS.contains(state.getBlock())) {
                // checks each of the sides
                for (Vec3i newOff : new Vec3i[]{
                        offset.north(),
                        offset.east(),
                        offset.south(),
                        offset.west(),
                        offset.down()
                        /* up has already been checked */
                }) {
                    BlockState adjacentState = world.getBlockState(origin.add(newOff));
                    if (isAdjacentBlockStuck(state, adjacentState)) {
                        // check the rest of the tree above the side block
                        if (!continueTree(world, origin, newOff, infos)) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }

        private static boolean alreadyCounted(ArrayList<FloatingBlockStructure.FloatingBlockInfoWrapper> infos, Vec3i offset1) {
            for (FloatingBlockStructure.FloatingBlockInfoWrapper info : infos) {
                if (info.offset.equals(offset1)) {
                    return true;
                }
            }
            return false;
        }

        private static boolean isAdjacentBlockStuck(BlockState state, BlockState adjacentState) {
            if (MoreTags.HONEY_BLOCKS.contains(state.getBlock()) && MoreTags.SLIME_BLOCKS.contains(adjacentState.getBlock())) {
                return false;
            } else if (MoreTags.SLIME_BLOCKS.contains(state.getBlock()) && MoreTags.HONEY_BLOCKS.contains(adjacentState.getBlock())) {
                return false;
            } else {
                return MoreTags.STICKY_BLOCKS.contains(state.getBlock()) || MoreTags.STICKY_BLOCKS.contains(adjacentState.getBlock());
            }
        }
    }

}