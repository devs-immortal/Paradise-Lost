package net.id.aether.world.feature.structure.generator;

import net.id.aether.Aether;
import net.id.aether.blocks.AetherBlocks;
import net.id.aether.tag.AetherBlockTags;
import net.id.aether.world.feature.structure.AetherStructureFeatures;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.structure.*;
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;

public class SkyrootTowerGenerator {
    private static final Identifier SKYROOT_TOWER = Aether.locate("skyroot_tower");

    public static void addPieces(StructureTemplateManager manager, StructurePiecesHolder structurePiecesHolder, BlockRotation blockRotation, BlockPos pos) {
        structurePiecesHolder.addPiece(new SkyrootTowerGenerator.Piece(manager, SKYROOT_TOWER, pos, blockRotation));
    }

    public static class Piece extends SimpleStructurePiece {

        public Piece(StructureTemplateManager manager, Identifier template, BlockPos pos, BlockRotation rotation) {
            super(AetherStructureFeatures.SKYROOT_TOWER_PIECE, 0, manager, template, template.toString(), createPlacementData(rotation), pos);
        }

        public Piece(StructureTemplateManager manager, NbtCompound nbt) {
            super(AetherStructureFeatures.SKYROOT_TOWER_PIECE, nbt, manager, (identifier) -> createPlacementData(BlockRotation.valueOf(nbt.getString("Rot"))));
        }

        public Piece(StructureContext context, NbtCompound nbtCompound) {
            this(context.structureTemplateManager(), nbtCompound);
        }

        private static StructurePlacementData createPlacementData(BlockRotation rotation) {
            return (new StructurePlacementData()).setRotation(rotation).setMirror(BlockMirror.NONE).addProcessor(BlockIgnoreStructureProcessor.IGNORE_AIR_AND_STRUCTURE_BLOCKS);
        }

        protected void writeNbt(StructureContext ctx, NbtCompound nbt) {
            super.writeNbt(ctx, nbt);
            nbt.putString("Rot", this.placementData.getRotation().name());
        }

        protected void handleMetadata(String metadata, BlockPos pos, ServerWorldAccess world, Random random, BlockBox boundingBox) {
        }

        public void generate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox boundingBox, ChunkPos chunkPos, BlockPos pos) {
            // Fill in pillars
            fillSupport(world, pos.down().north(2).east(2), AetherBlocks.SKYROOT_LOG.getDefaultState());
            fillSupport(world, pos.down().north(2).east(-2), AetherBlocks.SKYROOT_LOG.getDefaultState());
            fillSupport(world, pos.down().north(-2).east(2), AetherBlocks.SKYROOT_LOG.getDefaultState());
            fillSupport(world, pos.down().north(-2).east(-2), AetherBlocks.SKYROOT_LOG.getDefaultState());
            fillSupport(world, pos.down(), AetherBlocks.STRIPPED_SKYROOT_LOG.getDefaultState());
            // Add path blocks
            for (int x = -2; x <= 2; x++) {
                for (int z = -2; z <= 2; z++) {
                    if ((Math.abs(x) < 2 && Math.abs(z) < 2) || random.nextBoolean())
                        pathGround(world, new BlockPos(pos.getX()+x, pos.getY()+1, pos.getZ()+z));
                }
            }
            boundingBox.encompass(this.template.calculateBoundingBox(this.placementData, this.pos));
            super.generate(world, structureAccessor, chunkGenerator, random, boundingBox, chunkPos, pos);
        }

        private void fillSupport(StructureWorldAccess world, BlockPos pillarBottom, BlockState block) {
            int offset = 0;
            while (offset < 7 && !world.getBlockState(pillarBottom.down(offset)).isFullCube(world, pillarBottom.down(offset))) {
                world.setBlockState(pillarBottom.down(offset), block, 0);
                offset++;
            }
        }

        private void pathGround(StructureWorldAccess world, BlockPos pos) {
            int offset = 0;
            while (offset < 3 && world.getBlockState(pos.down(offset)).isAir()) {
                offset++;
            }
            if (world.getBlockState(pos.down(offset)).isIn(AetherBlockTags.DIRT_BLOCKS) && world.getBlockState(pos.down(offset-1)).isAir()) {
                world.setBlockState(pos.down(offset), AetherBlocks.AETHER_DIRT_PATH.getDefaultState(), 0);
            }
        }
    }
}
