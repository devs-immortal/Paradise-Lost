package net.id.aether.entities.util;

import net.gudenau.minecraft.moretags.MoreBlockTags;
import net.id.aether.api.FloatingBlockHelper;
import net.id.aether.entities.block.FloatingBlockEntity;
import net.id.aether.tag.AetherBlockTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

import static net.id.aether.api.FloatingBlockHelper.*;

public class FloatingBlockHelperImpls {
    /**
     * A general purpose floating block helper. This one is most recommended for everyday use.
     */
    public static class Any implements FloatingBlockHelper {
        private static final Any INSTANCE = new Any();

        private Any(){}

        public static Any getInstance(){
            return INSTANCE;
        }

        /**
         * Try to create whatever floating block type is appropriate for the given position.
         * @param pos   The position of the block that should be floated.
         * @param force If true, the block will be floated even if it is on the blacklist
         *              ({@code AetherBlockTags.NON_FLOATERS}) or immovable by pistons.
         * @return Whether a floating block could be created.
         */
        @Override
        public boolean tryCreate(World world, BlockPos pos, boolean force) {
            // try making a pusher, then a double, then a generic
            return PUSHER.tryCreate(world, pos, force)
                    || DOUBLE.tryCreate(world, pos, force)
                    || STANDARD.tryCreate(world, pos, force);
        }

        @Override
        public boolean isSuitableFor(BlockState state) {
            return STANDARD.isSuitableFor(state) || DOUBLE.isSuitableFor(state) || PUSHER.isSuitableFor(state);
        }

        @Override
        public boolean isBlocked(boolean shouldDrop, World world, BlockPos pos) {
            BlockState state = world.getBlockState(pos);
            return STANDARD.isBlocked(shouldDrop, world, pos)
                    && (DOUBLE.isSuitableFor(state) && DOUBLE.isBlocked(shouldDrop, world, pos))
                    && (PUSHER.isSuitableFor(state) && PUSHER.isBlocked(shouldDrop, world, pos));
        }
    }

    /**
     * A standard floating block helper.
     */
    public static class Standard implements FloatingBlockHelper {
        private static final Standard INSTANCE = new Standard();

        private Standard(){}

        public static Standard getInstance(){
            return INSTANCE;
        }
        /**
         * Try to create a standard floating block at the given position.
         * @param pos   The position of the block that should be floated.
         * @param force If true, the block will be floated even if it is on the blacklist
         *              ({@code AetherBlockTags.NON_FLOATERS}) or immovable by pistons.
         * @return Whether a floating block could be created.
         */
        @Override
        public boolean tryCreate(World world, BlockPos pos, boolean force) {
            BlockState state = world.getBlockState(pos);
            boolean dropping = FloatingBlockHelper.willBlockDrop(world, pos, state, false);
            if ((!force && isBlockBlacklisted(world, pos)) || isBlocked(dropping, world, pos)) {
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

        @Override
        public boolean isSuitableFor(BlockState state) {
            return true;
        }

        @Override
        public boolean isBlocked(boolean shouldDrop, World world, BlockPos pos) {
            BlockState above = world.getBlockState(pos.up());
            BlockState below = world.getBlockState(pos.down());
            if (shouldDrop) {
                return !FallingBlock.canFallThrough(below);
            } else {
                return !FallingBlock.canFallThrough(above);
            }
        }
    }

    /**
     * A double floating block helper, such as a door or tall grass plant.
     */
    public static class Double implements FloatingBlockHelper {
        private static final Double INSTANCE = new Double();

        private Double(){}

        public static Double getInstance(){
            return INSTANCE;
        }
        /**
         * Try to create a double floating block, such as a door or tall grass plant.
         * @param pos The position of the block that should be floated. It doesn't matter which
         *            half of the block is given.
         * @return Whether a double floating block could be created.
         */
        @Override
        public boolean tryCreate(World world, BlockPos pos, boolean force) {
            BlockState state = world.getBlockState(pos);
            boolean dropping = FloatingBlockHelper.willBlockDrop(world, pos, state, true);
            if ((!force && isBlockBlacklisted(world, pos)) || !isSuitableFor(state) || isBlocked(dropping, world, pos)) {
                return false;
            }
            if (state.get(Properties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER) {
                pos = pos.down();
                state = world.getBlockState(pos);
                if (!isSuitableFor(state)) {
                    return false;
                }
            } else {
                if (!isSuitableFor(world.getBlockState(pos.up()))) {
                    return false;
                }
            }
            BlockState upperState = world.getBlockState(pos.up());
            FloatingBlockEntity upper = new FloatingBlockEntity(world, pos.up(), upperState, true);
            FloatingBlockEntity lower = new FloatingBlockEntity(world, pos, state, true);
            upper.dropItem = false;
            BlockLikeSet structure = new BlockLikeSet(lower, upper, Vec3i.ZERO.up());
            structure.spawn(world);
            return true;
        }

        @Override
        public boolean isBlocked(boolean shouldDrop, World world, BlockPos pos) {
            BlockState state = world.getBlockState(pos);
            if (state.get(Properties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER) {
                pos = pos.up();
            }
            BlockState above = world.getBlockState(pos.up());
            BlockState below = world.getBlockState(pos.down().down());
            if (shouldDrop) {
                return !FallingBlock.canFallThrough(below);
            } else {
                return !FallingBlock.canFallThrough(above);
            }
        }

        @Override
        public boolean isSuitableFor(BlockState state) {
            return state.contains(Properties.DOUBLE_BLOCK_HALF);
        }
    }

    /**
     * A piston-like {@link BlockLikeSet} helper.
     */
    public static class Pusher implements FloatingBlockHelper {
        private static final Pusher INSTANCE = new Pusher();

        private Pusher(){}

        public static Pusher getInstance(){
            return INSTANCE;
        }
        /**
         * Try to create a floating block pusher, which is a piston-like floating block structure.
         * @param pos The position of the block that should be the pusher.
         * @return Whether a floating block could be created.
         */
        @Override
        public boolean tryCreate(World world, BlockPos pos, boolean force) {
            boolean dropping = FloatingBlockHelper.willBlockDrop(world, pos, world.getBlockState(pos), true);
            if ((!force && isBlockBlacklisted(world, pos)) || dropping || !isSuitableFor(world.getBlockState(pos))) {
                return false;
            }

            BlockLikeSet structure = construct(world, pos, force);
            if (structure != null) {
                structure.spawn(world);
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean isSuitableFor(BlockState state) {
            return state.isIn(AetherBlockTags.PUSH_FLOATERS);
        }

        @Deprecated(forRemoval = false)
        @Override
        public boolean isBlocked(boolean shouldDrop, World world, BlockPos pos) {
            if (shouldDrop) {
                return !FallingBlock.canFallThrough(world.getBlockState(pos.down()));
            }
            BlockLikeSet structure = construct(world, pos, false);
            if (structure == null) {
                return true;
            } else {
                structure.remove();
                return false;
            }
        }

        @Nullable
        private static BlockLikeSet construct(World world, BlockPos pos, boolean force) {
            SetBuilder builder = new SetBuilder(world, pos);
            if (world.getBlockState(pos).isIn(AetherBlockTags.PUSH_FLOATERS)
                    && continueTree(world, pos.up(), builder, force)
                    && builder.size() > 1) {
                return builder.build();
            }
            return null;
        }

        // returns false if the tree is unable to move. returns true otherwise.
        private static boolean continueTree(World world, BlockPos pos, SetBuilder builder, boolean overrideBlacklist) {
            if (builder.size() > MAX_MOVABLE_BLOCKS + 1) {
                return false;
            }
            BlockState state = world.getBlockState(pos);

            if (state.isAir() || (!overrideBlacklist && isBlockBlacklisted(world, pos)) || builder.isAlreadyInSet(pos)) {
                return true;
            }
            // adds the block to the structure
            builder.add(pos);
            // check if above block is movable
            if (!overrideBlacklist && isBlockBlacklisted(world, pos)) {
                return false;
            }
            // check if rest of tree above is movable
            if (!continueTree(world, pos.up(), builder, overrideBlacklist)) {
                return false;
            }
            // sides and bottom (sticky blocks)
            if (state.isIn(MoreBlockTags.STICKY_BLOCKS)) {
                // checks each of the sides
                for (var newPos : new BlockPos[]{
                        pos.north(),
                        pos.east(),
                        pos.south(),
                        pos.west(),
                        pos.down()
                        /* up has already been checked */
                }) {
                    BlockState adjacentState = world.getBlockState(newPos);
                    if (isAdjacentBlockStuck(state, adjacentState)) {
                        // check the rest of the tree above the side block
                        if (!continueTree(world, newPos, builder, overrideBlacklist)) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }

        private static boolean isAdjacentBlockStuck(BlockState state, BlockState adjacentState) {
            if (state.isIn(MoreBlockTags.HONEY_BLOCKS) && adjacentState.isIn(MoreBlockTags.SLIME_BLOCKS)) {
                return false;
            } else if (state.isIn(MoreBlockTags.SLIME_BLOCKS) && adjacentState.isIn(MoreBlockTags.HONEY_BLOCKS)) {
                return false;
            } else {
                return state.isIn(MoreBlockTags.STICKY_BLOCKS) || adjacentState.isIn(MoreBlockTags.STICKY_BLOCKS);
            }
        }
    }
}