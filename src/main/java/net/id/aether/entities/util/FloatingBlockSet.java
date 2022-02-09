package net.id.aether.entities.util;

import net.id.aether.entities.block.FloatingBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.*;
import java.util.function.Predicate;

/**
 * An object designed to hold floating block entities together.
 * <br>These are ticked every post-tick until destroyed. (Similar to {@link PostTickEntity})
 * @author Jack Papel
 */
public class FloatingBlockSet {
    private static final Set<FloatingBlockSet> allSets = new HashSet<>();
    private final Map<Vec3i, FloatingBlockEntity> entries;

    /**
     * This constructor is useful for doors and other two-block {@link FloatingBlockSet}s.
     * @param offset entity2's position - entity1's position
     */
    public FloatingBlockSet(FloatingBlockEntity entity1, FloatingBlockEntity entity2, Vec3i offset) {
        this.entries = Map.of(
                Vec3i.ZERO,     entity1,
                offset,         entity2
        );
    }

    /**
     * One of the entries in this map must have an offset of {@link Vec3i#ZERO}.
     * <br>"Must" is a strong word. It's recommended. If it isn't the case, the physics won't be consistent, probably.
     */
    public FloatingBlockSet(Map<Vec3i, FloatingBlockEntity> entries) {
        this.entries = Map.copyOf(entries);
    }

    /**
     * @return An unordered iterator of all active {@link FloatingBlockSet}s. Active means the set has not landed.
     */
    public static Iterator<FloatingBlockSet> getActiveSets() {
        return Set.copyOf(allSets).iterator();
    }

    /**
     * @return An immutable copy of this {@link FloatingBlockSet}'s entries.
     */
    public Map<Vec3i, FloatingBlockEntity> getEntries(){
        return Map.copyOf(entries);
    }

    public void spawn(World world) {
        entries.values().forEach(fbe -> {
            fbe.markPartOfSet();
            world.removeBlock(fbe.getBlockPos(), false);
            world.spawnEntity(fbe);
        });
        init();
    }

    public void postTick() {
        this.synchronize();
        // Checks for blocks that have landed
        for (var fbe : entries.values()) {
            if (fbe.isRemoved()) {
                World world = fbe.world;
                BlockState state = fbe.getBlockState();
                boolean success = world.getBlockState(fbe.getBlockPos()).isOf(state.getBlock());
                land(fbe, success);
                break;
            }
        }
    }

    // Aligns all FBEs to the master block
    private void synchronize() {
        FloatingBlockEntity master = getMasterBlock();

        entries.forEach((offset, block) -> {
            if (block == master) return;

            // sync data
            Vec3d newPos = master.getPos().add(Vec3d.of(offset));
            block.setPos(newPos.x, newPos.y, newPos.z);
            block.setVelocity(master.getVelocity());
            block.setDropping(master.isDropping());
        });
    }

    public void land(FloatingBlockEntity lander, boolean success) {
        synchronize();
        for (var fbe : entries.values()) {
            if (fbe != lander) {
                double impact = entries.get(Vec3i.ZERO).getVelocity().length();
                if (success) {
                    fbe.land(impact);
                } else {
                    fbe.crashLand(impact);
                }
            }
            fbe.dropItem = false;
        }
        remove();
    }

    private void init() {
        allSets.add(this);
    }

    public boolean remove() {
        return allSets.remove(this);
    }

    protected FloatingBlockEntity getMasterBlock(){
        FloatingBlockEntity master = entries.get(Vec3i.ZERO);
        if (master == null) {
            return entries.values().iterator().next();
        }
        return master;
    }

    /**
     * A builder intended to aid the creation of {@link FloatingBlockSet}s.
     */
    public static class Builder {
        private final World world;
        private final Map<Vec3i, FloatingBlockEntity> entries;
        private final BlockPos origin;

        /**
         * @param origin The position of the first block in the {@link FloatingBlockSet}.
         */
        public Builder(World world, BlockPos origin) {
            this.world = world;
            this.origin = origin;
            this.entries = new HashMap<>(2);

            this.entries.put(Vec3i.ZERO, new FloatingBlockEntity(this.world, this.origin, world.getBlockState(this.origin), true));
        }

        /**
         * @param pos The position of the block that should be added to the {@link FloatingBlockSet}.
         */
        public Builder add(BlockPos pos){
            FloatingBlockEntity entity = new FloatingBlockEntity(this.world, pos, world.getBlockState(pos), true);
            this.entries.put(pos.subtract(origin), entity);
            return this;
        }

        /**
         * Allows one to add to a {@link FloatingBlockSet} only if a certain condition is met.
         * The predicate acts on an immutable copy of the entries so far.
         * @param pos The position of the block that should be added to the {@link FloatingBlockSet}.
         * @param predicate A {@link Predicate} to test whether the block should be added.
         */
        public Builder addIf(BlockPos pos, Predicate<Map<Vec3i, FloatingBlockEntity>> predicate){
            if (predicate.test(Map.copyOf(entries))){
                return this.add(pos);
            }
            return this;
        }

        /**
         * @return The size of the {@link FloatingBlockSet} so far.
         */
        public int size(){
            return entries.size();
        }

        /**
         * @return The {@link FloatingBlockSet} that has been built.
         */
        public FloatingBlockSet build(){
            return new FloatingBlockSet(entries);
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
