package net.id.paradise_lost.entity.util;

import net.id.paradise_lost.api.BlockLikeSet;
import net.id.paradise_lost.api.FloatingBlockHelper;
import net.id.paradise_lost.entity.block.FloatingBlockEntity;
import net.id.paradise_lost.tag.ParadiseLostBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import static net.id.paradise_lost.api.FloatingBlockHelper.*;

public class FloatingBlockHelperImpls {

    public static class Any implements FloatingBlockHelper {
        private static final Any INSTANCE = new Any();

        private Any() {
        }

        public static Any getInstance() {
            return INSTANCE;
        }

        @Override
        public boolean tryCreate(Level world, BlockPos pos, boolean force) {

            return DOUBLE.tryCreate(world, pos, force) || STANDARD.tryCreate(world, pos, force);
        }

        @Override
        public boolean isSuitableFor(BlockState state) {
            return STANDARD.isSuitableFor(state) || DOUBLE.isSuitableFor(state);
        }

        @Override
        public boolean isBlocked(boolean shouldDrop, Level world, BlockPos pos) {
            BlockState state = world.getBlockState(pos);
            return STANDARD.isBlocked(shouldDrop, world, pos)
                    && (DOUBLE.isSuitableFor(state) && DOUBLE.isBlocked(shouldDrop, world, pos));
        }
    }

    public static class Standard implements FloatingBlockHelper {
        private static final Standard INSTANCE = new Standard();

        private Standard() {
        }

        public static Standard getInstance() {
            return INSTANCE;
        }

        @Override
        public boolean tryCreate(Level world, BlockPos pos, boolean force) {
            BlockState state = world.getBlockState(pos);
            boolean dropping = FloatingBlockHelper.willBlockDrop(world, pos, state, false);
            if ((!force && isBlockBlacklisted(world, pos)) || isBlocked(dropping, world, pos)) {
                return false;
            }
            FloatingBlockEntity entity = new FloatingBlockEntity(world, pos, state, false);

            if (state.is(Blocks.TNT)) {
                entity.setOnEndFloating((impact, landed) -> {
                    if (impact >= 0.8) {
                        BlockPos landingPos = entity.blockPosition();
                        world.destroyBlock(landingPos, false);
                        world.explode(entity, landingPos.getX(), landingPos.getY(), landingPos.getZ(), (float) Mth.clamp(impact * 5.5, 0, 10), Level.ExplosionInteraction.BLOCK);
                    }
                });
            }
            if (state.is(Blocks.LIGHTNING_ROD)) {
                entity.setOnEndFloating((impact, landed) -> {
                    if (world.isThundering() && landed && impact >= 1.1) {
                        LightningBolt lightning = new LightningBolt(EntityType.LIGHTNING_BOLT, world);
                        lightning.setPos(Vec3.atCenterOf(entity.blockPosition()));
                        world.addFreshEntity(lightning);
                    }
                });
            }
            world.addFreshEntity(entity);
            return true;
        }

        @Override
        public boolean isSuitableFor(BlockState state) {
            return true;
        }

        @Override
        public boolean isBlocked(boolean shouldDrop, Level world, BlockPos pos) {
            BlockState above = world.getBlockState(pos.above());
            BlockState below = world.getBlockState(pos.below());
            if (shouldDrop) {
                return !FallingBlock.isFree(below);
            } else {
                return !FallingBlock.isFree(above);
            }
        }
    }

    public static class Double implements FloatingBlockHelper {
        private static final Double INSTANCE = new Double();

        private Double() {
        }

        public static Double getInstance() {
            return INSTANCE;
        }

        @Override
        public boolean tryCreate(Level world, BlockPos pos, boolean force) {
            BlockState state = world.getBlockState(pos);
            boolean dropping = FloatingBlockHelper.willBlockDrop(world, pos, state, true);
            if ((!force && isBlockBlacklisted(world, pos)) || !isSuitableFor(state) || isBlocked(dropping, world, pos)) {
                return false;
            }
            if (state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER) {
                pos = pos.below();
                state = world.getBlockState(pos);
                if (!isSuitableFor(state)) {
                    return false;
                }
            } else {
                if (!isSuitableFor(world.getBlockState(pos.above()))) {
                    return false;
                }
            }
            BlockState upperState = world.getBlockState(pos.above());
            FloatingBlockEntity upper = new FloatingBlockEntity(world, pos.above(), upperState, true);
            FloatingBlockEntity lower = new FloatingBlockEntity(world, pos, state, true);
            upper.dropItem = false;
            world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            world.setBlockAndUpdate(pos.above(), Blocks.AIR.defaultBlockState());
            BlockLikeSet structure = new BlockLikeSet(lower, upper, Vec3i.ZERO.above());
            structure.spawn(world);
            return true;
        }

        @Override
        public boolean isBlocked(boolean shouldDrop, Level world, BlockPos pos) {
            BlockState state = world.getBlockState(pos);
            if (state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER) {
                pos = pos.above();
            }
            BlockState above = world.getBlockState(pos.above());
            BlockState below = world.getBlockState(pos.below().below());
            if (shouldDrop) {
                return !FallingBlock.isFree(below);
            } else {
                return !FallingBlock.isFree(above);
            }
        }

        @Override
        public boolean isSuitableFor(BlockState state) {
            return state.hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF);
        }
    }

    public static final class Pusher implements FloatingBlockHelper {
        private static final Pusher INSTANCE = new Pusher();

        private Pusher() {
        }

        public static Pusher getInstance() {
            return INSTANCE;
        }

        @Override
        public boolean tryCreate(Level world, BlockPos pos, boolean force) {
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

        public boolean isSuitableFor(BlockState state) {
            return state.is(ParadiseLostBlockTags.PUSH_FLOATERS);
        }

        @Deprecated(forRemoval = false)
        @Override
        public boolean isBlocked(boolean shouldDrop, Level world, BlockPos pos) {
            if (shouldDrop) {
                return !FallingBlock.isFree(world.getBlockState(pos.below()));
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
        private static BlockLikeSet construct(Level world, BlockPos pos, boolean force) {
            SetBuilder builder = new SetBuilder(world, pos);
            if (world.getBlockState(pos).is(ParadiseLostBlockTags.PUSH_FLOATERS)
                    && continueTree(world, pos.above(), builder, force)
                    && builder.size() > 1) {
                return builder.build();
            }
            return null;
        }

        private static boolean continueTree(Level world, BlockPos pos, SetBuilder builder, boolean overrideBlacklist) {
            if (builder.size() > MAX_MOVABLE_BLOCKS + 1) {
                return false;
            }
            BlockState state = world.getBlockState(pos);

            if (state.isAir() || (!overrideBlacklist && isBlockBlacklisted(world, pos)) || builder.isAlreadyInSet(pos)) {
                return true;
            }

            builder.add(pos);

            if (!overrideBlacklist && isBlockBlacklisted(world, pos)) {
                return false;
            }

            if (!continueTree(world, pos.above(), builder, overrideBlacklist)) {
                return false;
            }

            if (state.is(Blocks.SLIME_BLOCK) || state.is(Blocks.HONEY_BLOCK)) {

                for (var newPos : new BlockPos[]{
                        pos.north(),
                        pos.east(),
                        pos.south(),
                        pos.west(),
                        pos.below()

                }) {
                    BlockState adjacentState = world.getBlockState(newPos);
                    if (isAdjacentBlockStuck(state, adjacentState)) {

                        if (!continueTree(world, newPos, builder, overrideBlacklist)) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }

        private static boolean isAdjacentBlockStuck(BlockState state, BlockState adjacentState) {
            if (state.is(Blocks.HONEY_BLOCK) && adjacentState.is(Blocks.SLIME_BLOCK)) {
                return false;
            } else if (state.is(Blocks.SLIME_BLOCK) && adjacentState.is(Blocks.HONEY_BLOCK)) {
                return false;
            } else {
                return (state.is(Blocks.SLIME_BLOCK) || state.is(Blocks.HONEY_BLOCK)) || (adjacentState.is(Blocks.SLIME_BLOCK) || adjacentState.is(Blocks.HONEY_BLOCK));
            }
        }
    }
}
