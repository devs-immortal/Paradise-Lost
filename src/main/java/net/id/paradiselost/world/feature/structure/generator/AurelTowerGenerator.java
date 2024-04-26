package net.id.paradiselost.world.feature.structure.generator;

import net.id.paradiselost.ParadiseLost;
import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.id.paradiselost.tag.ParadiseLostBlockTags;
import net.id.paradiselost.world.feature.structure.ParadiseLostStructureFeatures;
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

public class AurelTowerGenerator {
    private static final Identifier AUREL_TOWER = ParadiseLost.locate("aurel_tower");

    public static void addPieces(StructureTemplateManager manager, StructurePiecesHolder structurePiecesHolder, BlockRotation blockRotation, BlockPos pos) {
        structurePiecesHolder.addPiece(new AurelTowerGenerator.Piece(manager, AUREL_TOWER, pos, blockRotation));
    }

    public static class Piece extends SimpleStructurePiece {

        public Piece(StructureTemplateManager manager, Identifier template, BlockPos pos, BlockRotation rotation) {
            super(ParadiseLostStructureFeatures.AUREL_TOWER_PIECE, 0, manager, template, template.toString(), createPlacementData(rotation), pos);
        }

        public Piece(StructureTemplateManager manager, NbtCompound nbt) {
            super(ParadiseLostStructureFeatures.AUREL_TOWER_PIECE, nbt, manager, (identifier) -> createPlacementData(BlockRotation.valueOf(nbt.getString("Rot"))));
        }

        public Piece(StructureContext context, NbtCompound nbtCompound) {
            this(context.structureTemplateManager(), nbtCompound);
        }

        private static StructurePlacementData createPlacementData(BlockRotation rotation) {
            return (new StructurePlacementData()).setRotation(rotation).setMirror(BlockMirror.NONE).addProcessor(BlockIgnoreStructureProcessor.IGNORE_AIR_AND_STRUCTURE_BLOCKS);
        }

        @Override
        protected void writeNbt(StructureContext ctx, NbtCompound nbt) {
            super.writeNbt(ctx, nbt);
            nbt.putString("Rot", this.placementData.getRotation().name());
        }

        @Override
        protected void handleMetadata(String metadata, BlockPos pos, ServerWorldAccess world, Random random, BlockBox boundingBox) {
        }

        @Override
        public void generate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox boundingBox, ChunkPos chunkPos, BlockPos pos) {
            // Fill in pillars
            fillSupport(world, pos.down().north(2).east(2), ParadiseLostBlocks.AUREL_WOODSTUFF.log().getDefaultState());
            fillSupport(world, pos.down().north(2).east(-2), ParadiseLostBlocks.AUREL_WOODSTUFF.log().getDefaultState());
            fillSupport(world, pos.down().north(-2).east(2), ParadiseLostBlocks.AUREL_WOODSTUFF.log().getDefaultState());
            fillSupport(world, pos.down().north(-2).east(-2), ParadiseLostBlocks.AUREL_WOODSTUFF.log().getDefaultState());
            fillSupport(world, pos.down(), ParadiseLostBlocks.AUREL_WOODSTUFF.strippedLog().getDefaultState());
            // Add path blocks
            for (int x = -2; x <= 2; x++) {
                for (int z = -2; z <= 2; z++) {
                    if ((Math.abs(x) < 2 && Math.abs(z) < 2) || random.nextBoolean()) {
                        pathGround(world, new BlockPos(pos.getX() + x, pos.getY() + 1, pos.getZ() + z));
                    }
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
            if (world.getBlockState(pos.down(offset)).isIn(ParadiseLostBlockTags.DIRT_BLOCKS) && world.getBlockState(pos.down(offset - 1)).isAir()) {
                world.setBlockState(pos.down(offset), ParadiseLostBlocks.DIRT_PATH.getDefaultState(), 0);
            }
        }
    }
}
