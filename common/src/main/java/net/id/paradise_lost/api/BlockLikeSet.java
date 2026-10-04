package net.id.paradise_lost.api;

import java.util.*;
import java.util.function.Predicate;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

@SuppressWarnings("unused")
public record BlockLikeSet(Map<Vec3i, BlockLikeEntity> entries) {
    private static final Set<BlockLikeSet> structures = new HashSet<>();

    public BlockLikeSet(BlockLikeEntity entity1, BlockLikeEntity entity2, Vec3i offset) {
        this(Map.of(
                Vec3i.ZERO, entity1,
                offset, entity2
        ));
    }

    public BlockLikeSet {
    }

    public static Iterator<BlockLikeSet> getAllSets() {
        return Set.copyOf(structures).iterator();
    }

    @Override
    public Map<Vec3i, BlockLikeEntity> entries() {
        return Map.copyOf(entries);
    }

    public void spawn(Level world) {
        entries.forEach((offset, block) -> {
            block.markPartOfSet();
            world.removeBlock(block.blockPosition(), false);
            world.addFreshEntity(block);
        });
        init();
    }

    protected void synchronize() {
        BlockLikeEntity master = getMasterBlock();
        entries.forEach((offset, block) -> block.alignWith(master, offset));
    }

    public void postTick() {
        this.synchronize();

        for (BlockLikeEntity block : entries.values()) {
            if (block.isRemoved()) {

                Level world = block.level();
                BlockState state = block.getBlockState();
                boolean success = world.getBlockState(block.blockPosition()).is(state.getBlock());
                this.land(block, success);
                break;
            }
        }
    }

    public void land(BlockLikeEntity lander, boolean success) {
        this.synchronize();

        for (BlockLikeEntity block : entries.values()) {
            if (block != lander) {
                if (success) {
                    block.cease();
                } else {
                    Level world = block.level();
                    BlockState state = block.getBlockState();
                    BlockPos pos = block.blockPosition();

                    if (world.getBlockState(pos).is(state.getBlock())) {
                        world.removeBlock(pos, false);
                    }
                    block.breakApart();
                }
            }
            block.dropItem = false;
        }
        this.remove();
    }

    public BlockLikeEntity getMasterBlock() {
        if (entries.containsKey(Vec3i.ZERO)) {
            return entries.get(Vec3i.ZERO);
        } else {
            return entries.values().iterator().next();
        }
    }

    private void init() {
        structures.add(this);
    }

    public void remove() {
        structures.remove(this);
    }

    @SuppressWarnings("unused")
    public static class Builder {
        protected final Map<Vec3i, BlockLikeEntity> entries;
        protected final BlockPos origin;

        public Builder(BlockPos origin) {
            this.origin = origin;
            this.entries = new HashMap<>(2);
        }

        public Builder add(BlockLikeEntity entity) {
            BlockPos pos = entity.blockPosition();
            if (!isAlreadyInSet(pos)) {
                this.entries.put(pos.subtract(origin), entity);
            }
            return this;
        }

        public Builder addIf(BlockLikeEntity entity, Predicate<Map<Vec3i, BlockLikeEntity>> predicate) {
            if (predicate.test(Map.copyOf(entries))) {
                return this.add(entity);
            }
            return this;
        }

        public int size() {
            return entries.size();
        }

        public BlockLikeSet build() {
            return new BlockLikeSet(entries);
        }

        public boolean isAlreadyInSet(BlockPos pos) {
            return this.entries.containsKey(pos.subtract(origin));
        }
    }
}
