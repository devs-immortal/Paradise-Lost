package com.aether.world.feature.structure;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;

import java.util.Random;

public abstract class AetherStructureGenerator extends StructurePiece {

    public int chance;
    public final BlockState airState = Blocks.AIR.getDefaultState();
    public BlockState blockState, extraBlockState;
    public boolean replaceAir, replaceSolid;
    public StructureWorldAccess world;
    public Random random;
    public BlockBox structureBoundingBox;
    public ChunkGenerator chunkGenerator;
    public ChunkPos chunkPos;
    private int startX, startY, startZ;

    public AetherStructureGenerator(StructurePieceType piece, NbtCompound compound) {
        super(piece, compound);
    }

    public void setBlocks(BlockState blockState) {
        this.blockState = blockState;
        this.extraBlockState = null;
        this.chance = 0;
    }

    public void setBlocks(BlockState blockState, BlockState extraBlockState, int chance) {
        this.blockState = blockState;
        this.extraBlockState = extraBlockState;
        this.chance = chance;

        if (this.chance < 1) this.chance = 1;
    }

    public void setStructureOffset(int x, int y, int z) {
        this.startX = x;
        this.startY = y;
        this.startZ = z;
    }

    public void addLineX(int x, int y, int z, int xRange) {
        for (int lineX = x; lineX < x + xRange; lineX++) {
            Block block = this.getBlockState(lineX + this.startX, y + this.startY, z + this.startZ).getBlock();

            if ((this.replaceAir || block != Blocks.AIR) && (this.replaceSolid || block == Blocks.AIR))
                this.setBlock(lineX + this.startX, y + this.startY, z + this.startZ);
        }
    }

    public void addLineY(int x, int y, int z, int yRange) {
        for (int lineY = y; lineY < y + yRange; lineY++) {
            Block block = this.getBlockState(x + this.startX, lineY + this.startY, z + this.startZ).getBlock();

            if ((this.replaceAir || block != Blocks.AIR) && (this.replaceSolid || block == Blocks.AIR))
                this.setBlock(x + this.startX, lineY + this.startY, z + this.startZ);
        }
    }

    public void addLineZ(int x, int y, int z, int zRange) {
        for (int lineZ = z; lineZ < z + zRange; lineZ++) {
            Block block = this.getBlockState(x + this.startX, y + this.startY, lineZ + this.startZ).getBlock();

            if ((this.replaceAir || block != Blocks.AIR) && (this.replaceSolid || block == Blocks.AIR))
                this.setBlock(x + this.startX, y + this.startY, lineZ + this.startZ);
        }
    }

    public void addPlaneX(int x, int y, int z, int yRange, int zRange) {
        for (int lineY = y; lineY < y + yRange; lineY++) {
            for (int lineZ = z; lineZ < z + zRange; lineZ++) {
                Block block = this.getBlockState(x + this.startX, lineY + this.startY, lineZ + this.startZ).getBlock();

                if ((this.replaceAir || block != Blocks.AIR) && (this.replaceSolid || block == Blocks.AIR))
                    this.setBlock(x + this.startX, lineY + this.startY, lineZ + this.startZ);
            }
        }
    }

    public void addPlaneY(int x, int y, int z, int xRange, int zRange) {
        for (int lineX = x; lineX < x + xRange; lineX++) {
            for (int lineZ = z; lineZ < z + zRange; lineZ++) {
                Block block = this.getBlockState(lineX + this.startX, y + this.startY, lineZ + this.startZ).getBlock();

                if ((this.replaceAir || block != Blocks.AIR) && (this.replaceSolid || block == Blocks.AIR))
                    this.setBlock(lineX + this.startX, y + this.startY, lineZ + this.startZ);
            }
        }
    }

    public void addPlaneZ(int x, int y, int z, int xRange, int yRange) {
        for (int lineX = x; lineX < x + xRange; lineX++) {
            for (int lineY = y; lineY < y + yRange; lineY++) {
                Block block = this.getBlockState(lineX + this.startX, lineY + this.startY, z + this.startZ).getBlock();

                if ((this.replaceAir || block != Blocks.AIR) && (this.replaceSolid || block == Blocks.AIR))
                    this.setBlock(lineX + this.startX, lineY + this.startY, z + this.startZ);
            }
        }
    }

    public void addHollowBox(int x, int y, int z, int xRange, int yRange, int zRange) {
        BlockState temp1 = this.blockState;
        BlockState temp2 = this.extraBlockState;

        this.setBlocks(this.airState, this.airState, this.chance);
        this.addSolidBox(x, y, z, xRange, yRange, zRange);
        this.setBlocks(temp1, temp2, this.chance);
        this.addPlaneY(x, y, z, xRange, zRange);
        this.addPlaneY(x, y + (yRange - 1), z, xRange, zRange);
        this.addPlaneX(x, y, z, yRange, zRange);
        this.addPlaneX(x + (xRange - 1), y, z, yRange, zRange);
        this.addPlaneZ(x, y, z, xRange, yRange);
        this.addPlaneZ(x, y, z + (zRange - 1), xRange, yRange);
    }

    public void addSquareTube(int x, int y, int z, int xRange, int yRange, int zRange, int angel) {
        BlockState temp1 = this.blockState;
        BlockState temp2 = this.extraBlockState;

        this.setBlocks(this.airState, this.airState, this.chance);
        this.addSolidBox(x, y, z, xRange, yRange, zRange);
        this.setBlocks(temp1, temp2, this.chance);

        if (angel == 0 || angel == 2) {
            this.addPlaneY(x, y, z, xRange, zRange);
            this.addPlaneY(x, y + (yRange - 1), z, xRange, zRange);
        }
        if (angel == 1 || angel == 2) {
            this.addPlaneX(x, y, z, yRange, zRange);
            this.addPlaneX(x + (xRange - 1), y, z, yRange, zRange);
        }
        if (angel == 0 || angel == 1) {
            this.addPlaneZ(x, y, z, xRange, yRange);
            this.addPlaneZ(x, y, z + (zRange - 1), xRange, yRange);
        }
    }

    public void addSolidBox(int x, int y, int z, int xRange, int yRange, int zRange) {
        for (int lineX = x; lineX < x + xRange; lineX++) {
            for (int lineY = y; lineY < y + yRange; lineY++) {
                for (int lineZ = z; lineZ < z + zRange; lineZ++) {
                    Block block = this.getBlockState(lineX + this.startX, lineY + this.startY, lineZ + this.startZ).getBlock();

                    if ((this.replaceAir || block != Blocks.AIR) && (this.replaceSolid || block == Blocks.AIR))
                        this.setBlock(lineX + this.startX, lineY + this.startY, lineZ + this.startZ);
                }
            }
        }
    }

    public boolean isBoxSolid(int x, int y, int z, int xRange, int yRange, int zRange) {
        boolean flag = true;

        for (int lineX = x; lineX < x + xRange; lineX++) {
            for (int lineY = y; lineY < y + yRange; lineY++) {
                for (int lineZ = z; lineZ < z + zRange; lineZ++) {
                    if (this.getBlockState(lineX + this.startX, lineY + this.startY, lineZ + this.startZ).getBlock() == Blocks.AIR)
                        flag = false;
                }
            }
        }

        return flag;
    }

    public boolean isBoxEmpty(int x, int y, int z, int xRange, int yRange, int zRange) {
        boolean flag = true;

        for (int lineX = x; lineX < x + xRange; lineX++) {
            for (int lineY = y; lineY < y + yRange; lineY++) {
                for (int lineZ = z; lineZ < z + zRange; lineZ++) {
                    if (this.getBlockState(lineX + this.startX, lineY + this.startY, lineZ + this.startZ).getBlock() != Blocks.AIR)
                        flag = false;
                }
            }
        }

        return flag;
    }

    public BlockEntity getTileEntityFromPosWithOffset(int x, int y, int z) {
        BlockPos blockpos = new BlockPos(this.getActualX(x, z), this.getActualY(y), this.getActualZ(x, z));
        return !this.structureBoundingBox.contains(blockpos) ? null : this.world.getBlockEntity(blockpos);
    }

    public BlockState getBlockStateWithOffset(int x, int y, int z) {
        return this.getBlockAt(this.world, x + this.startX, y + this.startY, z + this.startZ, this.structureBoundingBox);
    }

    public BlockState getBlockState(int x, int y, int z) {
        return this.getBlockAt(this.world, x, y, z, this.structureBoundingBox);
    }

    public void setBlockWithOffset(int x, int y, int z, BlockState state) {
        this.addBlock(this.world, state, x + this.startX, y + this.startY, z + this.startZ, this.structureBoundingBox);
    }

    public void setBlock(int x, int y, int z, BlockState state) {
        this.addBlock(this.world, state, x, y, z, this.structureBoundingBox);
    }

    public void setBlockWithOffset(int x, int y, int z) {
        if (this.chance == 0) {
            this.setBlock(x + this.startX, y + this.startY, z + this.startZ, this.blockState);
            return;
        }
        if (this.random.nextInt(this.chance) == 0) {
            this.addBlock(this.world, this.extraBlockState, x + this.startX, y + this.startY, z + this.startZ, this.structureBoundingBox);
        } else {
            this.addBlock(this.world, this.blockState, x + this.startX, y + this.startY, z + this.startZ, this.structureBoundingBox);
        }
    }

    public void setBlock(int x, int y, int z) {
        if (this.chance == 0) {
            this.setBlock(x, y, z, this.blockState);
            return;
        }
        if (this.random.nextInt(this.chance) == 0) {
            this.addBlock(this.world, this.extraBlockState, x, y, z, this.structureBoundingBox);
        } else {
            this.addBlock(this.world, this.blockState, x, y, z, this.structureBoundingBox);
        }
    }

    public boolean spawnEntity(Entity entity, int structureX, int structureY, int structureZ) {
        int posX = this.getActualX(structureX, structureZ);
        int posY = this.getActualY(structureY);
        int posZ = this.getActualZ(structureX, structureZ);

        if (this.structureBoundingBox.contains(new BlockPos(posX, posY, posZ))) {
            entity.updatePositionAndAngles((double) posX + 0.5D, (double) posY + 0.5D, (double) posZ + 0.5D, 0.0F, 0.0F);

            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity = ((LivingEntity) entity);
                livingEntity.heal(livingEntity.getMaxHealth());
            }

            if (!this.world.isClient()) // Not taking chances ~Kino
                this.world.spawnEntity(entity);

            return true;
        }
        return false;
    }

    public int getActualX(int structureX, int structureZ) {
        return this.applyXTransform(structureX + this.startX, structureZ + this.startZ);
    }

    public int getActualY(int structureY) {
        return this.applyYTransform(structureY + this.startY);
    }

    public int getActualZ(int structureX, int structureZ) {
        return this.applyZTransform(structureX + this.startX, structureZ + this.startZ);
    }

    public abstract boolean generate();

    @Override
    public boolean generate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox boundingBox, ChunkPos chunkPos, BlockPos blockPos) {
        this.world = world;
        this.chunkGenerator = chunkGenerator;
        this.random = random;
        this.structureBoundingBox = boundingBox;
        this.chunkPos = chunkPos;

        return this.generate();
    }

//    @Override
//    protected void writeNbt(NbtCompound compound) {
//    }
}