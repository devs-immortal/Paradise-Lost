package net.id.aether.api;

import net.gudenau.minecraft.moretags.MoreTags;
import net.id.aether.entities.block.FloatingBlockEntity;
import net.id.aether.entities.util.FloatingBlockStructure;
import net.id.aether.entities.util.FloatingBlockStructure.FloatingBlockInfoWrapper;
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
    /**
     * A general purpose floating block helper.
     */
    FloatingBlockHelper ANY = new GeneralFloatingBlockHelper();

    /**
     * A standard floating block helper.
     */
    FloatingBlockHelper STANDARD = new GenericFloatingBlockHelper();

    /**
     * A double floating block helper, such as a door or tall grass plant.
     */
    FloatingBlockHelper DOUBLE = new DoubleFloatingBlockHelper();

    /**
     * A piston-like floating block structure helper.
     */
    FloatingBlockHelper PUSHER = new PusherFloatingBlockHelper();

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
    boolean tryCreate(World world, BlockPos pos);

    /**
     * Try to create a floating block of this type
     * @param pos The position of the (root) block you want to float.
     * @param force Whether to override the non-floaters blacklist.
     * @return Whether the block(s) were successfully floated.
     */
    boolean tryCreate(World world, BlockPos pos, boolean force);

    /**
     * @param pos The position of the block to query.
     * @return Whether the block at the specified location can be floated.
     */
    static boolean isBlockFloatable(World world, BlockPos pos){
        BlockState state = world.getBlockState(pos);
        return !state.isIn(AetherBlockTags.NON_FLOATERS) && PistonBlock.isMovable(state, world, pos, Direction.UP, true, Direction.UP);
    }

    /**
     * @return Whether this type of floating block is suitable for this block.
     */
    boolean isSuitableFor(BlockState state);

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
                && !AetherBlockTags.NON_FLOATERS.contains(state.getBlock());
    }

    /**
     * @param shouldDrop Whether the given block is floating or dropping.
     * @param below The block below the floating block you wish to create.
     * @param above The block above the floating block you wish to create.
     * @return Whether a floating block will not be blocked upon creation.
     */
    default boolean isNotBlocked(boolean shouldDrop, BlockState below, BlockState above) {
        if (shouldDrop) {
            return FallingBlock.canFallThrough(below);
        } else {
            return FallingBlock.canFallThrough(above);
        }
    }

    /**
     * A structure builder intended to aid the creation of floating block structures.
     */
    class FloatingBlockStructureBuilder {
        private final World world;
        private final FloatingBlockStructure structure;
        private final BlockPos origin;

        /**
         * @param initial The position of the first block in the structure.
         */
        public FloatingBlockStructureBuilder(World world, BlockPos initial) {
            this.world = world;
            this.structure = new FloatingBlockStructure(new ArrayList<>(0));
            this.origin = initial;
            this.add(initial);
        }

        /**
         * @param pos The position of the block that should be added to the structure.
         */
        public FloatingBlockStructureBuilder add(BlockPos pos){
            FloatingBlockEntity entity = new FloatingBlockEntity(this.world, pos, world.getBlockState(pos), true);
            this.structure.blockInfos.add(new FloatingBlockInfoWrapper(entity, pos.subtract(this.origin)));
            return this;
        }

        /**
         * @param pos The position of the block that should be added to the structure.
         * @param p A predicate to test whether the block should be added.
         */
        public FloatingBlockStructureBuilder addIf(BlockPos pos, Predicate<ArrayList<FloatingBlockInfoWrapper>> p){
            if (p.test(this.structure.blockInfos)){
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
         * @return The list of {@link FloatingBlockInfoWrapper}s so far
         */
        public ArrayList<FloatingBlockInfoWrapper> list(){
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
                    wrapper.offset.equals(pos.subtract(origin)));
        }

        /**
         * Cancel building the structure.
         */
        public boolean cancel() {
            return structure.remove();
        }
    }
}

class GeneralFloatingBlockHelper implements FloatingBlockHelper {
    /**
     * Try to create whatever floating block type is appropriate for the given position.
     *
     * @param pos The position of the block that should be floated.
     * @return Whether a floating block could be created.
     */
    public boolean tryCreate(World world, BlockPos pos) {
        return tryCreate(world, pos, false);
    }

    /**
     * Try to create whatever floating block type is appropriate for the given position.
     *
     * @param pos   The position of the block that should be floated.
     * @param force If true, the block will be floated even if it is on the blacklist
     *              ({@code AetherBlockTags.NON_FLOATERS}) or immovable by pistons.
     * @return Whether a floating block could be created.
     */
    public boolean tryCreate(World world, BlockPos pos, boolean force) {
        BlockState state = world.getBlockState(pos);
        // try making a pusher, then a double, then a generic
        return (PUSHER.isSuitableFor(state) && PUSHER.tryCreate(world, pos))
                || (DOUBLE.isSuitableFor(state) && DOUBLE.tryCreate(world, pos))
                || (STANDARD.tryCreate(world, pos, force));
    }

    @Override
    public boolean isSuitableFor(BlockState state) {
        return true;
    }

}

class GenericFloatingBlockHelper implements FloatingBlockHelper {
    /**
     * Try to create a standard floating block at the given position.
     *
     * @param pos The position of the block that should be floated.
     * @return Whether a floating block could be created.
     */
    @Override
    public boolean tryCreate(World world, BlockPos pos) {
        return tryCreate(world, pos, false);
    }

    /**
     * Try to create a standard floating block at the given position.
     *
     * @param pos   The position of the block that should be floated.
     * @param force If true, the block will be floated even if it is on the blacklist
     *              ({@code AetherBlockTags.NON_FLOATERS}) or immovable by pistons.
     * @return Whether a floating block could be created.
     */
    @Override
    public boolean tryCreate(World world, BlockPos pos, boolean force) {
        BlockState state = world.getBlockState(pos);
        boolean dropping = FloatingBlockHelper.willBlockDrop(world, pos, state, false);
        if (!(force || FloatingBlockHelper.isBlockFloatable(world, pos)) || !isNotBlocked(world, pos, dropping)) {
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

    public boolean isNotBlocked(World world, BlockPos pos, boolean dropping) {
        return ANY.isNotBlocked(dropping, world.getBlockState(pos.down()), world.getBlockState(pos.up()));
    }
}

class DoubleFloatingBlockHelper implements FloatingBlockHelper {
    /**
     * Try to create a double floating block, such as a door or tall grass plant.
     *
     * @param pos The position of the block that should be floated. It doesn't matter which
     *            half of the block is given.
     * @return Whether a double floating block could be created.
     */
    @Override
    public boolean tryCreate(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        boolean dropping = FloatingBlockHelper.willBlockDrop(world, pos, state, true);
        if (!isNotBlocked(world, pos, dropping)) {
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

    @Override
    public boolean tryCreate(World world, BlockPos pos, boolean force) {
        return tryCreate(world, pos);
    }

    public boolean isNotBlocked(World world, BlockPos pos, boolean dropping) {
        BlockState state = world.getBlockState(pos);
        if (state.get(Properties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER) {
            return ANY.isNotBlocked(dropping, world.getBlockState(pos.down()), world.getBlockState(pos.up().up()));
        } else {
            return ANY.isNotBlocked(dropping, world.getBlockState(pos.down().down()), world.getBlockState(pos.up()));
        }
    }

    public boolean isSuitableFor(BlockState state) {
        return state.getProperties().contains(Properties.DOUBLE_BLOCK_HALF);
    }
}

class PusherFloatingBlockHelper implements FloatingBlockHelper {
    public static final int MAX_MOVABLE_BLOCKS = PistonHandler.MAX_MOVABLE_BLOCKS;

    /**
     * Try to create a floating block pusher, which is a piston-like floating block structure.
     * @param pos The position of the block that should be the pusher.
     * @return Whether a floating block could be created.
     */
    @Override
    public boolean tryCreate(World world, BlockPos pos) {
        boolean dropping = FloatingBlockHelper.willBlockDrop(world, pos, world.getBlockState(pos), true);
        if (dropping) {
            return STANDARD.tryCreate(world, pos);
        }

        FloatingBlockStructure structure = construct(world, pos);
        if (structure != null) {
            structure.spawn(world);
            return true;
        } else {
            return STANDARD.tryCreate(world, pos);
        }
    }

    @Override
    public boolean tryCreate(World world, BlockPos pos, boolean force) {
        return tryCreate(world, pos);
    }

    public boolean isSuitableFor(BlockState state) {
        return state.isIn(AetherBlockTags.PUSH_FLOATERS);
    }

    @Nullable
    private static FloatingBlockStructure construct(World world, BlockPos pos) {
        FloatingBlockStructureBuilder builder = new FloatingBlockStructureBuilder(world, pos);
        if (world.getBlockState(pos).isIn(AetherBlockTags.PUSH_FLOATERS)
                && continueTree(world, pos.up(), builder)
                && builder.size() > 1) {
            return builder.build();
        }
        builder.cancel();
        return null;
    }

    // returns false if the tree is unable to move. returns true otherwise.
    private static boolean continueTree(World world, BlockPos pos, FloatingBlockStructureBuilder builder) {
        if (builder.size() > MAX_MOVABLE_BLOCKS + 1) {
            return false;
        }
        BlockState state = world.getBlockState(pos);

        if (state.isAir() || !FloatingBlockHelper.isBlockFloatable(world, pos) || builder.isInStructure(pos)) {
            return true;
        }
        // adds the block to the structure
        builder.add(pos);
        // check if above block is movable
        if (!FloatingBlockHelper.isBlockFloatable(world, pos.up())) {
            return false;
        }
        // check if rest of tree above is movable
        if (!continueTree(world, pos.up(), builder)) {
            return false;
        }
        // sides and bottom (sticky blocks)
        if (MoreTags.STICKY_BLOCKS.contains(state.getBlock())) {
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
                    if (!continueTree(world, newPos, builder)) {
                        return false;
                    }
                }
            }
        }
        return true;
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