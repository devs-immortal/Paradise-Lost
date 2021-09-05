package net.id.aether.entities.util.floatingblock;

import net.id.aether.entities.block.FloatingBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.ArrayList;

public class FloatingBlockStructure {
    private static final ArrayList<FloatingBlockStructure> allStructures = new ArrayList<>(0);
    protected ArrayList<FloatingBlockInfoWrapper> blockInfos = new ArrayList<>(0);

    public FloatingBlockStructure(FloatingBlockEntity entity1, FloatingBlockEntity entity2, Vec3i offset) {
        this.blockInfos.add(new FloatingBlockInfoWrapper(entity1, Vec3i.ZERO));
        this.blockInfos.add(new FloatingBlockInfoWrapper(entity2, offset));
    }

    public FloatingBlockStructure(ArrayList<FloatingBlockInfoWrapper> arr) {
        this.blockInfos.addAll(arr);
    }

    public static ArrayList<FloatingBlockStructure> getAllStructures() {
        return allStructures;
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
        FloatingBlockInfoWrapper master = blockInfos.get(0);
        for (FloatingBlockInfoWrapper blockInfo : blockInfos) {
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

    protected void alignToMaster(FloatingBlockInfoWrapper blockInfo) {
        FloatingBlockInfoWrapper master = blockInfos.get(0);
        Vec3d newPos = master.block.getPos().add(Vec3d.of(blockInfo.offset));
        blockInfo.block.setPos(newPos.x, newPos.y, newPos.z);
        blockInfo.block.setVelocity(master.block.getVelocity());
        blockInfo.block.setDropping(master.block.isDropping());
    }

    public void land(FloatingBlockInfoWrapper lander, boolean success) {
        for (FloatingBlockInfoWrapper blockInfo : blockInfos) {
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
        allStructures.remove(this);
    }

    protected void init() {
        allStructures.add(this);
    }

    public static class FloatingBlockInfoWrapper {
        FloatingBlockEntity block;
        Vec3i offset;

        public FloatingBlockInfoWrapper(FloatingBlockEntity block, Vec3i offset) {
            this.block = block;
            this.offset = offset;
        }
    }
}
