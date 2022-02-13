package net.id.aether.entities.util;

import net.id.aether.entities.block.BlockLikeEntity;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.*;
import java.util.function.Predicate;

/**
 * An object designed to hold {@link BlockLikeEntity}s together.
 * <br>These are ticked every post-tick until destroyed. (Similar to {@link PostTickEntity})
 * @author Jack Papel
 */
public class BlockLikeEntitySet {
    private static final Set<BlockLikeEntitySet> allSets = new HashSet<>();
    private final Map<Vec3i, BlockLikeEntity> entries;

    /**
     * This constructor is useful for doors and other two-block {@link BlockLikeEntitySet}s.
     * @param offset entity2's position - entity1's position
     */
    public BlockLikeEntitySet(BlockLikeEntity entity1, BlockLikeEntity entity2, Vec3i offset) {
        this.entries = Map.of(
                Vec3i.ZERO,     entity1,
                offset,         entity2
        );
    }

    /**
     * One of the entries in this map must have an offset of {@link Vec3i#ZERO}.
     * <br>"Must" is a strong word. It's recommended. If it isn't the case, the physics won't be consistent, probably.
     */
    public BlockLikeEntitySet(Map<Vec3i, BlockLikeEntity> entries) {
        this.entries = Map.copyOf(entries);
    }

    /**
     * @return An unordered iterator of all active {@link BlockLikeEntitySet}s. Active means the set has not landed.
     */
    public static Iterator<BlockLikeEntitySet> getActiveSets() {
        return Set.copyOf(allSets).iterator();
    }

    /**
     * @return An immutable copy of this {@link BlockLikeEntitySet}'s entries.
     */
    public Map<Vec3i, BlockLikeEntity> getEntries(){
        return Map.copyOf(entries);
    }

    public void spawn(World world) {
        entries.values().forEach(fbe -> {
            fbe.markPartOfSet();
            world.spawnEntity(fbe);
        });
        init();
    }

    public void postTick() {
        this.synchronize();
        // Checks for blocks that have landed (or have been removed)
        for (var ble : entries.values()) {
            if (ble.isRemoved()) {
                World world = ble.world;
                BlockState state = ble.getBlockState();
                boolean success = world.getBlockState(ble.getBlockPos()).isOf(state.getBlock());
                land(ble, success);
                break;
            }
        }
    }

    // Aligns all BLEs to the master block
    private void synchronize() {
        BlockLikeEntity master = getMasterBlock();
        entries.forEach((offset, block) -> block.alignWith(master, offset));
    }

    public void land(BlockLikeEntity lander, boolean success) {
        synchronize();
        for (var fbe : entries.values()) {
            if (fbe != lander) {
                double impact = entries.get(Vec3i.ZERO).getVelocity().length();
                if (success) {
                    fbe.tryLand(impact);
                } else {
                    fbe.crashLand(impact);
                }
            }
            fbe.dropItem = false;
        }
        this.remove();
    }

    private void init() {
        allSets.add(this);
    }

    public boolean remove() {
        entries.values().forEach(BlockLikeEntity::discard);
        return allSets.remove(this);
    }

    protected BlockLikeEntity getMasterBlock(){
        BlockLikeEntity master = entries.get(Vec3i.ZERO);
        if (master == null) {
            return entries.values().iterator().next();
        }
        return master;
    }

    /**
     * A builder intended to aid the creation of {@link BlockLikeEntitySet}s.
     */
    @SuppressWarnings("unused")
    public static class Builder {
        protected final Map<Vec3i, BlockLikeEntity> entries;
        protected final BlockPos origin;

        /**
         * @param origin The position of the first block in the {@link BlockLikeEntitySet}.
         */
        public Builder(BlockPos origin) {
            this.origin = origin;
            this.entries = new HashMap<>(2);
        }

        /**
         * @param entity The BlockLikeEntity to add to the {@link BlockLikeEntitySet}.
         *               If there has already been an entity added at that location,
         *               this entity will be ignored and not added.
         */
        public Builder add(BlockLikeEntity entity){
            BlockPos pos = entity.getBlockPos();
            if (!isAlreadyInSet(pos)) {
                this.entries.put(pos.subtract(origin), entity);
            }
            return this;
        }

        /**
         * Allows one to add to a {@link BlockLikeEntitySet} only if a certain condition is met.
         * The predicate acts on an immutable copy of the entries so far.
         * @param pos The position of the block that should be added to the {@link BlockLikeEntitySet}.
         * @param predicate A {@link Predicate} to test whether the block should be added.
         */
        public Builder addIf(BlockLikeEntity entity, Predicate<Map<Vec3i, BlockLikeEntity>> predicate){
            if (predicate.test(Map.copyOf(entries))){
                return this.add(entity);
            }
            return this;
        }

        /**
         * @return The size of the {@link BlockLikeEntitySet} so far.
         */
        public int size(){
            return entries.size();
        }

        /**
         * @return The {@link BlockLikeEntitySet} that has been built.
         */
        public BlockLikeEntitySet build(){
            return new BlockLikeEntitySet(entries);
        }

        /**
         * @param pos The position of the block to test
         * @return Whether the given block is already in the set.
         */
        public boolean isAlreadyInSet(BlockPos pos) {
            return this.entries.containsKey(pos.subtract(origin));
        }
    }
}
