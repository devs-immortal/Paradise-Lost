package net.id.aether.entities.util;

import net.id.aether.entities.block.FloatingBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.ArrayList;

/**
 * An object designed to hold floating block entities together.
 * @author Jack Papel
 */
public class FloatingBlockStructure {
    private static final ArrayList<FloatingBlockStructure> structures = new ArrayList<>(0);
    // Maybe find a better name for this variable. It's fine for now, though.
    public ArrayList<FloatingBlockInfo> blockInfos = new ArrayList<>(0);

    public FloatingBlockStructure(FloatingBlockEntity entity1, FloatingBlockEntity entity2, Vec3i offset) {
        this.blockInfos.add(new FloatingBlockInfo(entity1, Vec3i.ZERO));
        this.blockInfos.add(new FloatingBlockInfo(entity2, offset));
    }

    public FloatingBlockStructure(ArrayList<FloatingBlockInfo> arr) {
        this.blockInfos.addAll(arr);
    }

    public static ArrayList<FloatingBlockStructure> getAllStructures() {
        return structures;
    }

    public void spawn(World world) {
        blockInfos.forEach(blockInfo -> {
            blockInfo.block.markPartOfStructure();
            world.removeBlock(blockInfo.block.getBlockPos(), false);
            world.spawnEntity(blockInfo.block);
        });
        init();
    }

    public void postTick() {
        FloatingBlockInfo master = blockInfos.get(0);
        for (FloatingBlockInfo blockInfo : blockInfos) {
            if (!blockInfo.equals(master)) {
                this.alignToMaster(blockInfo);
            }
            if (blockInfo.block.isRemoved()) {
                World world = blockInfo.block.world;
                BlockState state = blockInfo.block.getBlockState();
                boolean success = world.getBlockState(blockInfo.block.getBlockPos()).isOf(state.getBlock());
                land(blockInfo, success);
                break;
            }
        }
    }

    private void alignToMaster(FloatingBlockInfo blockInfo) {
        FloatingBlockInfo master = blockInfos.get(0);
        Vec3d newPos = master.block.getPos().add(Vec3d.of(blockInfo.offset));
        blockInfo.block.setPos(newPos.x, newPos.y, newPos.z);
        blockInfo.block.setVelocity(master.block.getVelocity());
        blockInfo.block.setDropping(master.block.isDropping());
    }

    public void land(FloatingBlockInfo lander, boolean success) {
        for (FloatingBlockInfo blockInfo : blockInfos) {
            alignToMaster(blockInfo);
            if (!blockInfo.equals(lander)) {
                double impact = blockInfos.get(0).block.getVelocity().length();
                if (success) {
                    blockInfo.block.land(impact);
                } else {
                    blockInfo.block.crashLand(impact);
                }
            }
            blockInfo.block.dropItem = false;
        }
        this.blockInfos.clear();
        structures.remove(this);
    }

    private void init() {
        structures.add(this);
    }

    public boolean remove() {
        return structures.remove(this);
    }

    /**
     * A wrapper class for a floating block entity and its offset
     * from the origin of a {@link FloatingBlockStructure}.
     */
    public record FloatingBlockInfo(FloatingBlockEntity block, Vec3i offset) {
    }
}
