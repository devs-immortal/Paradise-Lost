package net.id.aether.api;

import net.gudenau.minecraft.moretags.MoreTags;
import net.id.aether.entities.block.FloatingBlockEntity;
import net.id.aether.entities.util.FloatingBlockStructure;
import net.id.aether.entities.util.FloatingBlockStructure.FloatingBlockInfo;
import net.id.aether.tag.AetherBlockTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FallingBlock;
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
import java.util.function.Predicate;

/**
 * A helper class designed to aid in the creation of floating blocks.
 * @author Jack Papel
 */
public interface FloatingBlockHelper {
    int MAX_MOVABLE_BLOCKS = PistonHandler.MAX_MOVABLE_BLOCKS;

    /**
     * The default conditions under which a floating block goes from floating to falling.
     * By default, this is when a floating block is 50 blocks from the height limit, and
     * isn't a fast floater.
     */
    Function<FloatingBlockEntity, Boolean> DEFAULT_DROP_STATE = (entity) -> {
        World world = entity.world;
        BlockPos pos = entity.getBlockPos();
        int distFromTop = world.getTopY() - pos.getY();
        return !entity.isFastFloater() && distFromTop <= 50;
    };

    /**
     * Try to create a floating block of this type
     * @param pos The position of the (root) block you want to float.
     * @return Whether the block(s) were successfully floated.
     */
    default boolean tryCreate(World world, BlockPos pos){
        return tryCreate(world, pos, false);
    }

    /**
     * Try to create a floating block of this type
     * @param pos The position of the (root) block you want to float.
     * @param force Whether to override the non-floaters blacklist.
     * @return Whether the block(s) were successfully floated.
     */
    boolean tryCreate(World world, BlockPos pos, boolean force);

    /**
     * @return Whether this type of floating block is suitable for this block.
     */
    boolean isSuitableFor(BlockState state);

    /**
     * @param shouldDrop Whether the given block is floating or dropping.
     * @param pos The position of the block to test.
     * @return Whether a floating block will be blocked at the given position.
     */
    boolean isBlocked(boolean shouldDrop, World world, BlockPos pos);

    /**
     * @param pos The position of the block to query.
     * @return Whether the block at the specified location cannot be floated.
     */
    static boolean isBlockBlacklisted(World world, BlockPos pos){
        BlockState state = world.getBlockState(pos);
        return state.isIn(AetherBlockTags.NON_FLOATERS) || !PistonBlock.isMovable(state, world, pos, Direction.UP, true, Direction.UP);
    }

    /**
     * @param pos The position of the block
     * @param state The blockstate of the block
     * @param partOfStructure Whether the block is part of a structure
     *                        (A double floating block or floating block pusher)
     * @return Whether a floating block created at the given position will drop.
     */
    static boolean willBlockDrop(World world, BlockPos pos, BlockState state, boolean partOfStructure) {
        FloatingBlockEntity entity = new FloatingBlockEntity(world, pos, state, partOfStructure);
        boolean willDrop = entity.getDropState().get();
        entity.discard();
        return willDrop;
    }

    /**
     * @param context The item usage context
     * @return Whether the tool, as given by the item usage context, is adequate to float the block.
     */
    static boolean isToolAdequate(ItemUsageContext context) {
        BlockPos pos = context.getBlockPos();
        World world = context.getWorld();
        BlockState state = world.getBlockState(pos);
        Item heldItem = context.getStack().getItem();
        return world.getBlockEntity(pos) == null && state.getHardness(world, pos) != -1.0F
                && (!state.isToolRequired() || heldItem.isSuitableFor(state))
                && !state.isIn(AetherBlockTags.NON_FLOATERS);
    }

    /**
     * A general purpose floating block helper.
     */
    FloatingBlockHelper ANY = new FloatingBlockHelper(){
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
    };

    /**
     * A standard floating block helper.
     */
    FloatingBlockHelper STANDARD = new FloatingBlockHelper(){

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
    };

    /**
     * A double floating block helper, such as a door or tall grass plant.
     */
    FloatingBlockHelper DOUBLE = new FloatingBlockHelper(){
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
            FloatingBlockStructure structure = new FloatingBlockStructure(lower, upper, Vec3i.ZERO.up());
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
    };

    /**
     * A piston-like floating block structure helper.
     */
    FloatingBlockHelper PUSHER = new FloatingBlockHelper(){
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

            FloatingBlockStructure structure = construct(world, pos, force);
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
            FloatingBlockStructure structure = construct(world, pos, false);
            if (structure == null) {
                return true;
            } else {
                structure.remove();
                return false;
            }
        }

        @Nullable
        private static FloatingBlockStructure construct(World world, BlockPos pos, boolean force) {
            StructureBuilder builder = new StructureBuilder(world, pos);
            if (world.getBlockState(pos).isIn(AetherBlockTags.PUSH_FLOATERS)
                    && continueTree(world, pos.up(), builder, force)
                    && builder.size() > 1) {
                return builder.build();
            }
            builder.cancel();
            return null;
        }

        // returns false if the tree is unable to move. returns true otherwise.
        private static boolean continueTree(World world, BlockPos pos, StructureBuilder builder, boolean overrideBlacklist) {
            if (builder.size() > MAX_MOVABLE_BLOCKS + 1) {
                return false;
            }
            BlockState state = world.getBlockState(pos);

            if (state.isAir() || (!overrideBlacklist && isBlockBlacklisted(world, pos)) || builder.isInStructure(pos)) {
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
            if (state.isIn(MoreTags.STICKY_BLOCKS)) {
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
            if (state.isIn(MoreTags.HONEY_BLOCKS) && adjacentState.isIn(MoreTags.SLIME_BLOCKS)) {
                return false;
            } else if (state.isIn(MoreTags.SLIME_BLOCKS) && adjacentState.isIn(MoreTags.HONEY_BLOCKS)) {
                return false;
            } else {
                return state.isIn(MoreTags.STICKY_BLOCKS) || adjacentState.isIn(MoreTags.STICKY_BLOCKS);
            }
        }
    };

    /**
     * A structure builder intended to aid the creation of floating block structures.
     */
    class StructureBuilder {
        private final World world;
        private final FloatingBlockStructure structure;
        private final BlockPos origin;

        /**
         * @param initial The position of the first block in the structure.
         */
        public StructureBuilder(World world, BlockPos initial) {
            this.world = world;
            this.structure = new FloatingBlockStructure(new ArrayList<>(0));
            this.origin = initial;
            this.add(initial);
        }

        /**
         * @param pos The position of the block that should be added to the structure.
         */
        public StructureBuilder add(BlockPos pos){
            FloatingBlockEntity entity = new FloatingBlockEntity(this.world, pos, world.getBlockState(pos), true);
            this.structure.blockInfos.add(new FloatingBlockInfo(entity, pos.subtract(this.origin)));
            return this;
        }

        /**
         * @param pos The position of the block that should be added to the structure.
         * @param predicate A predicate to test whether the block should be added.
         */
        public StructureBuilder addIf(BlockPos pos, Predicate<ArrayList<FloatingBlockInfo>> predicate){
            if (predicate.test(this.structure.blockInfos)){
                return this.add(pos);
            }
            return this;
        }

        /**
         * @return The size of the structure so far.
         */
        public int size(){
            return structure.blockInfos.size();
        }

        /**
         * @return The list of {@link FloatingBlockInfo}s so far
         */
        public ArrayList<FloatingBlockInfo> list(){
            return structure.blockInfos;
        }

        /**
         * @return The structure that has been built.
         */
        public FloatingBlockStructure build(){
            return this.structure;
        }

        /**
         * @param pos The position of the block to test
         * @return Whether the given block is already in the structure.
         */
        public boolean isInStructure(BlockPos pos) {
            return this.structure.blockInfos.stream().anyMatch(wrapper ->
                    wrapper.offset().equals(pos.subtract(origin)));
        }

        /**
         * Cancel building the structure.
         */
        public void cancel() {
            structure.remove();
        }
    }
}