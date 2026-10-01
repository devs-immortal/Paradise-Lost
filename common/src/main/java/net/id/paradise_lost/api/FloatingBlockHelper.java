package net.id.paradise_lost.api;

import net.id.paradise_lost.entity.block.FloatingBlockEntity;
import net.id.paradise_lost.entity.util.FloatingBlockHelperImpls;
import net.id.paradise_lost.entity.util.FloatingBlockHelperImpls.Any;
import net.id.paradise_lost.entity.util.FloatingBlockHelperImpls.Pusher;
import net.id.paradise_lost.entity.util.FloatingBlockHelperImpls.Standard;
import net.id.paradise_lost.item.tool.base_tools.GravityWandItem;
import net.id.paradise_lost.tag.ParadiseLostBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.piston.PistonStructureResolver;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public interface FloatingBlockHelper {
    int MAX_MOVABLE_BLOCKS = PistonStructureResolver.MAX_PUSH_DEPTH;

    Any ANY = Any.getInstance();

    Standard STANDARD = Standard.getInstance();

    FloatingBlockHelperImpls.Double DOUBLE = FloatingBlockHelperImpls.Double.getInstance();

    Pusher PUSHER = Pusher.getInstance();

    Function<FloatingBlockEntity, Boolean> DEFAULT_DROP_STATE = (entity) -> {
        Level world = entity.level();
        BlockPos pos = entity.blockPosition();
        int distFromTop = world.getMaxBuildHeight() - pos.getY();
        return !entity.isInTag(ParadiseLostBlockTags.DECAYING_FLOATERS) && distFromTop <= 50;
    };

    default boolean tryCreate(Level world, BlockPos pos) {
        return tryCreate(world, pos, false);
    }

    boolean tryCreate(Level world, BlockPos pos, boolean force);

    boolean isSuitableFor(BlockState state);

    boolean isBlocked(boolean shouldDrop, Level world, BlockPos pos);

    static boolean isBlockBlacklisted(Level world, BlockPos pos, BlockState state) {
        return state.is(ParadiseLostBlockTags.NON_FLOATERS) || !PistonBaseBlock.isPushable(state, world, pos, Direction.UP, true, Direction.UP);
    }

    static boolean isBlockBlacklisted(Level world, BlockPos pos) {
        return isBlockBlacklisted(world, pos, world.getBlockState(pos));
    }

    static boolean willBlockDrop(Level world, BlockPos pos, BlockState state, boolean partOfStructure) {
        FloatingBlockEntity entity = new FloatingBlockEntity(world, pos, state, partOfStructure);
        boolean willDrop = entity.getDropState().get();
        entity.discard();
        return willDrop;
    }

    static boolean isToolAdequate(UseOnContext context) {
        BlockPos pos = context.getClickedPos();
        Level world = context.getLevel();
        BlockState state = world.getBlockState(pos);
        Item heldItem = context.getItemInHand().getItem();
        return world.getBlockEntity(pos) == null && state.getDestroySpeed(world, pos) != -1.0F
                && (!state.requiresCorrectToolForDrops() || heldItem.canAttackBlock(state, world, pos, context.getPlayer()) || (heldItem instanceof GravityWandItem && validForWand(state)))
                && !state.is(ParadiseLostBlockTags.NON_FLOATERS);
    }

    static boolean validForWand(BlockState bs) {
        return !bs.is(BlockTags.NEEDS_DIAMOND_TOOL);
    }

    @SuppressWarnings("unused")
    class SetBuilder extends BlockLikeSet.Builder {
        private final Level world;

        public SetBuilder(Level world, BlockPos initial) {
            super(initial);
            this.world = world;
            this.add(initial);
        }

        public SetBuilder add(BlockPos pos) {
            FloatingBlockEntity entity = new FloatingBlockEntity(this.world, pos, world.getBlockState(pos), true);
            this.entries.put(pos.subtract(this.origin), entity);
            return this;
        }

        public SetBuilder addif(BlockPos pos, Predicate<Map<Vec3i, BlockLikeEntity>> predicate) {
            if (predicate.test(Map.copyOf(this.entries))) {
                return this.add(pos);
            }
            return this;
        }
    }
}
