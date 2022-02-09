package net.id.aether.api;

import net.id.aether.entities.block.FloatingBlockEntity;
import net.id.aether.entities.util.FloatingBlockHelperImpls;
import net.id.aether.entities.util.FloatingBlockSet;
import net.id.aether.tag.AetherBlockTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.PistonBlock;
import net.minecraft.block.piston.PistonHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

import static net.id.aether.entities.util.FloatingBlockHelperImpls.*;

import java.util.function.Function;

/**
 * A helper class designed to aid in the creation of floating blocks and {@link FloatingBlockSet}s.
 * @author Jack Papel
 */
public interface FloatingBlockHelper {
    int MAX_MOVABLE_BLOCKS = PistonHandler.MAX_MOVABLE_BLOCKS;

    /** {@link Any}*/
    Any ANY = new FloatingBlockHelperImpls.Any();
    /** {@link Standard}*/
    Standard STANDARD = new FloatingBlockHelperImpls.Standard();
    /** {@link FloatingBlockHelperImpls.Double}*/
    FloatingBlockHelperImpls.Double DOUBLE = new FloatingBlockHelperImpls.Double();
    /** {@link Pusher}*/
    Pusher PUSHER = new FloatingBlockHelperImpls.Pusher();

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
     * @param state The state of the block to query.
     * @return Whether the block at the specified location cannot be floated.
     */
    static boolean isBlockBlacklisted(World world, BlockPos pos, BlockState state){
        return state.isIn(AetherBlockTags.NON_FLOATERS) || !PistonBlock.isMovable(state, world, pos, Direction.UP, true, Direction.UP);
    }

    /**
     * @param pos The position of the block to query.
     * @return Whether the block at the specified location cannot be floated.
     */
    static boolean isBlockBlacklisted(World world, BlockPos pos){
        return isBlockBlacklisted(world, pos, world.getBlockState(pos));
    }

    /**
     * Tests whether a floating block will drop under the given conditions.
     * Notice: This does create and discard a floating block entity for this test.
     * @param pos The position of the block
     * @param state The blockstate of the block
     * @param partOfSet Whether the block is part of a {@link FloatingBlockSet}
     * @return Whether a floating block created at the given position will drop.
     */
    static boolean willBlockDrop(World world, BlockPos pos, BlockState state, boolean partOfSet) {
        FloatingBlockEntity entity = new FloatingBlockEntity(world, pos, state, partOfSet);
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
                && !isBlockBlacklisted(world, pos, state);
    }

    /**
     * This class is now deprecated. Instead, use {@link FloatingBlockSet.Builder}.
     */
    @Deprecated(forRemoval = true, since = "1.7")
    class StructureBuilder extends FloatingBlockSet.Builder {
        /**
         * @param origin The position of the first block in the {@link FloatingBlockSet}.
         */
        public StructureBuilder(World world, BlockPos origin) {
            super(world, origin);
        }
    }
}