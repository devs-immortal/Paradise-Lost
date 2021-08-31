package net.id.aether.world.feature.generator;

import net.id.aether.Aether;
import net.id.aether.world.feature.AetherFeatures;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.SimpleStructurePiece;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePiecesHolder;
import net.minecraft.structure.StructurePlacementData;
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

public class OrangeRuinGenerator {
    private static final Identifier ORANGE_RUIN = Aether.locate("orange_ruin");

    public static void addPieces(StructureManager manager, StructurePiecesHolder structurePiecesHolder, Random random, BlockPos pos) {
        BlockRotation blockRotation = BlockRotation.random(random);
        structurePiecesHolder.addPiece(new OrangeRuinGenerator.Piece(manager, ORANGE_RUIN, pos, blockRotation));
    }

    public static class Piece extends SimpleStructurePiece {
        private boolean shifted = false;

        public Piece(StructureManager manager, Identifier template, BlockPos pos, BlockRotation rotation) {
            super(AetherFeatures.ORANGE_RUIN_PIECE, 0, manager, template, template.toString(), createPlacementData(rotation), pos);
        }

        public Piece(ServerWorld world, NbtCompound nbt) {
            super(AetherFeatures.ORANGE_RUIN_PIECE, nbt, world, (identifier) -> createPlacementData(BlockRotation.valueOf(nbt.getString("Rot"))));
        }

        private static StructurePlacementData createPlacementData(BlockRotation rotation) {
            return (new StructurePlacementData()).setRotation(rotation).setMirror(BlockMirror.NONE).addProcessor(BlockIgnoreStructureProcessor.IGNORE_AIR_AND_STRUCTURE_BLOCKS);
        }

        protected void writeNbt(ServerWorld world, NbtCompound nbt) {
            super.writeNbt(world, nbt);
            nbt.putString("Rot", this.placementData.getRotation().name());
        }

        protected void handleMetadata(String metadata, BlockPos pos, ServerWorldAccess world, Random random, BlockBox boundingBox) {
        }

        public boolean generate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox boundingBox, ChunkPos chunkPos, BlockPos pos) {
            if (this.pos.getY() > 2) {
                if (!shifted) {
                    this.pos = this.pos.down(1);
                    shifted = true;
                }
                boundingBox.encompass(this.structure.calculateBoundingBox(this.placementData, this.pos));
                return super.generate(world, structureAccessor, chunkGenerator, random, boundingBox, chunkPos, pos);
            }
            return false;
        }
    }
}
