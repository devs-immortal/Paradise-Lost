package com.aether.entities.block;

import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.ArrayList;

public class FloatingBlockStructure {
    private static ArrayList<FloatingBlockStructure> allStructures = new ArrayList<>(0);
    protected ArrayList<FloatingBlockInfoWrapper> blockInfos = new ArrayList<>(0);

    public FloatingBlockStructure(FloatingBlockEntity entity1, FloatingBlockEntity entity2, Vec3i offset) {
        this.blockInfos.add(new FloatingBlockInfoWrapper(entity1, Vec3i.ZERO));
        this.blockInfos.add(new FloatingBlockInfoWrapper(entity2, offset));
    }

    public FloatingBlockStructure(ArrayList<FloatingBlockInfoWrapper> arr){
        this.blockInfos.addAll(arr);
    }

    public void spawn(World world){
        blockInfos.forEach(blockInfo -> {
            blockInfo.block.markPartOfStructure();
            if(!blockInfo.equals(blockInfos.get(0))){
                blockInfo.block.dropItem = false;
            }
            blockInfo.block.floatTime = -1;
            world.spawnEntity(blockInfo.block);
        });
        init();
    }

    public void postTick(){
        FloatingBlockInfoWrapper master = blockInfos.get(0);
        for(FloatingBlockInfoWrapper blockInfo : blockInfos){
            if (!blockInfo.equals(master)) {
                this.alignToMaster(blockInfo);
            }
            if(blockInfo.block.isRemoved()){
                land(blockInfo);
                break;
            }
        }
    }

    protected void alignToMaster(FloatingBlockInfoWrapper blockInfo){
        FloatingBlockInfoWrapper master = blockInfos.get(0);
        Vec3d newPos = master.block.getPos().add(Vec3d.of(blockInfo.offset));
        blockInfo.block.setPos(newPos.x, newPos.y, newPos.z);
        blockInfo.block.setVelocity(master.block.getVelocity());
        blockInfo.block.setDropping(master.block.isDropping());
    }

    public void land(FloatingBlockInfoWrapper lander){
        for(FloatingBlockInfoWrapper blockInfo : blockInfos){
            alignToMaster(blockInfo);
            if (!blockInfo.equals(lander)) {
                blockInfo.block.land((float) blockInfos.get(0).block.getVelocity().length());
            }
        }
        allStructures.remove(this);
    }

    public static ArrayList<FloatingBlockStructure> getAllStructures(){
        return allStructures;
    }

    protected void init(){
        allStructures.add(this);
    }

    public static class FloatingBlockInfoWrapper {
        FloatingBlockEntity block;
        Vec3i offset;
        public FloatingBlockInfoWrapper(FloatingBlockEntity block, Vec3i offset){
            this.block = block;
            this.offset = offset;
        }
    }
}
