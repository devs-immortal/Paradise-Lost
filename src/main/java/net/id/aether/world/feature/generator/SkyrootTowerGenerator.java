package net.id.aether.world.feature.generator;

import net.id.aether.Aether;
import net.id.aether.world.feature.AetherFeatures;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.*;
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;

import java.util.Random;

public class SkyrootTowerGenerator {
    private static final Identifier SKYROOT_TOWER = Aether.locate("skyroot_tower");

    public static void addPieces(StructureManager manager, StructurePiecesHolder structurePiecesHolder, BlockRotation blockRotation, BlockPos pos) {
        structurePiecesHolder.addPiece(new SkyrootTowerGenerator.Piece(manager, SKYROOT_TOWER, pos, blockRotation));
    }

    public static class Piece extends SimpleStructurePiece {
        private boolean shifted = false;

        public Piece(StructureManager manager, Identifier template, BlockPos pos, BlockRotation rotation) {
            super(AetherFeatures.SKYROOT_TOWER_PIECE, 0, manager, template, template.toString(), createPlacementData(rotation), pos);
        }

        public Piece(StructureManager manager, NbtCompound nbt) {
            super(AetherFeatures.SKYROOT_TOWER_PIECE, nbt, manager, (identifier) -> createPlacementData(BlockRotation.valueOf(nbt.getString("Rot"))));
        }

        public Piece(StructureContext context, NbtCompound nbtCompound) {
            this(context.structureManager(), nbtCompound);
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
            //if (this.pos.getY() > 2) {
            if (!shifted) {
                this.pos = this.pos.down(1);
                shifted = true;
            }
            boundingBox.encompass(this.structure.calculateBoundingBox(this.placementData, this.pos));
            super.generate(world, structureAccessor, chunkGenerator, random, boundingBox, chunkPos, pos);
            //}
            //return false;
        }
    }
}
