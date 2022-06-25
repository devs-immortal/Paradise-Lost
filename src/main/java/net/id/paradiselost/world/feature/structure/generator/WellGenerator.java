package net.id.paradiselost.world.feature.structure.generator;

import net.id.paradiselost.ParadiseLost;
import net.id.paradiselost.world.feature.structure.ParadiseLostStructureFeatures;
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

public class WellGenerator {
    private static final Identifier WELL = ParadiseLost.locate("well");


    public static void addPieces(StructureTemplateManager manager, StructurePiecesHolder structurePiecesHolder, Random random, BlockPos pos) {
        BlockRotation blockRotation = BlockRotation.random(random);
        structurePiecesHolder.addPiece(new Piece(manager, WELL, pos, blockRotation));
    }

    public static class Piece extends SimpleStructurePiece {
        private boolean shifted = false;

        public Piece(StructureTemplateManager manager, Identifier template, BlockPos pos, BlockRotation rotation) {
            super(ParadiseLostStructureFeatures.WELL_PIECE, 0, manager, template, template.toString(), createPlacementData(rotation), pos);
        }

        public Piece(StructureTemplateManager manager, NbtCompound nbt) {
            super(ParadiseLostStructureFeatures.WELL_PIECE, nbt, manager, (identifier) -> createPlacementData(BlockRotation.valueOf(nbt.getString("Rot"))));
        }

        public Piece(StructureContext context, NbtCompound nbtCompound) {
            this(context.structureTemplateManager(), nbtCompound);
        }

        private static StructurePlacementData createPlacementData(BlockRotation rotation) {
            return (new StructurePlacementData()).setRotation(rotation).setMirror(BlockMirror.NONE).addProcessor(BlockIgnoreStructureProcessor.IGNORE_AIR_AND_STRUCTURE_BLOCKS);
        }

        @Override
        protected void writeNbt(StructureContext context, NbtCompound nbt) {
            super.writeNbt(context, nbt);
            nbt.putString("Rot", placementData.getRotation().name());
        }

        @Override
        protected void handleMetadata(String metadata, BlockPos pos, ServerWorldAccess world, Random random, BlockBox boundingBox) {
        }

        @Override
        public void generate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox boundingBox, ChunkPos chunkPos, BlockPos pos) {
            if (this.pos.getY() > 2) {
                if (!shifted) {
                    this.pos = this.pos.down(3);
                    shifted = true;
                }
                boundingBox.encompass(template.calculateBoundingBox(placementData, this.pos));
                super.generate(world, structureAccessor, chunkGenerator, random, boundingBox, chunkPos, pos);
            }
        }
    }
}
