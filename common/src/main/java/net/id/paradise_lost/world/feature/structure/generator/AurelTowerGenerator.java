package net.id.paradise_lost.world.feature.structure.generator;

import net.id.paradise_lost.registry.BlockRegistry;
import net.id.paradise_lost.tag.ParadiseLostBlockTags;
import net.id.paradise_lost.world.feature.structure.ParadiseLostStructureFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.id.paradise_lost.ModConstants;

public class AurelTowerGenerator {
    private static final ResourceLocation AUREL_TOWER = ModConstants.id("aurel_tower");

    public static void addPieces(StructureTemplateManager manager, StructurePieceAccessor structurePiecesHolder, Rotation blockRotation, BlockPos pos) {
        structurePiecesHolder.addPiece(new AurelTowerGenerator.Piece(manager, AUREL_TOWER, pos, blockRotation));
    }

    public static class Piece extends TemplateStructurePiece {

        public Piece(StructureTemplateManager manager, ResourceLocation template, BlockPos pos, Rotation rotation) {
            super(ParadiseLostStructureFeatures.AUREL_TOWER_PIECE, 0, manager, template, template.toString(), createPlacementData(rotation), pos);
        }

        public Piece(StructureTemplateManager manager, CompoundTag nbt) {
            super(ParadiseLostStructureFeatures.AUREL_TOWER_PIECE, nbt, manager, (identifier) -> createPlacementData(Rotation.valueOf(nbt.getString("Rot"))));
        }

        public Piece(StructurePieceSerializationContext context, CompoundTag nbtCompound) {
            this(context.structureTemplateManager(), nbtCompound);
        }

        private static StructurePlaceSettings createPlacementData(Rotation rotation) {
            return (new StructurePlaceSettings()).setRotation(rotation).setMirror(Mirror.NONE).addProcessor(BlockIgnoreProcessor.STRUCTURE_AND_AIR);
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag nbt) {
            super.addAdditionalSaveData(ctx, nbt);
            nbt.putString("Rot", this.placeSettings.getRotation().name());
        }

        @Override
        protected void handleDataMarker(String metadata, BlockPos pos, ServerLevelAccessor world, RandomSource random, BoundingBox boundingBox) {
        }

        @Override
        public void postProcess(WorldGenLevel world, StructureManager structureAccessor, ChunkGenerator chunkGenerator, RandomSource random, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos pos) {

            fillSupport(world, pos.below().north(2).east(2), BlockRegistry.AUREL_WOODSTUFF.log().get().defaultBlockState());
            fillSupport(world, pos.below().north(2).east(-2), BlockRegistry.AUREL_WOODSTUFF.log().get().defaultBlockState());
            fillSupport(world, pos.below().north(-2).east(2), BlockRegistry.AUREL_WOODSTUFF.log().get().defaultBlockState());
            fillSupport(world, pos.below().north(-2).east(-2), BlockRegistry.AUREL_WOODSTUFF.log().get().defaultBlockState());
            fillSupport(world, pos.below(), BlockRegistry.AUREL_WOODSTUFF.strippedLog().get().defaultBlockState());

            for (int x = -2; x <= 2; x++) {
                for (int z = -2; z <= 2; z++) {
                    if ((Math.abs(x) < 2 && Math.abs(z) < 2) || random.nextBoolean()) {
                        pathGround(world, new BlockPos(pos.getX() + x, pos.getY() + 1, pos.getZ() + z));
                    }
                }
            }
            boundingBox.encapsulate(this.template.getBoundingBox(this.placeSettings, this.templatePosition));
            super.postProcess(world, structureAccessor, chunkGenerator, random, boundingBox, chunkPos, pos);
        }

        private void fillSupport(WorldGenLevel world, BlockPos pillarBottom, BlockState block) {
            int offset = 0;
            while (offset < 7 && !world.getBlockState(pillarBottom.below(offset)).isCollisionShapeFullBlock(world, pillarBottom.below(offset))) {
                world.setBlock(pillarBottom.below(offset), block, 0);
                offset++;
            }
        }

        private void pathGround(WorldGenLevel world, BlockPos pos) {
            int offset = 0;
            while (offset < 3 && world.getBlockState(pos.below(offset)).isAir()) {
                offset++;
            }
            if (world.getBlockState(pos.below(offset)).is(ParadiseLostBlockTags.DIRT_BLOCKS) && world.getBlockState(pos.below(offset - 1)).isAir()) {
                world.setBlock(pos.below(offset), BlockRegistry.DIRT_PATH.get().defaultBlockState(), 0);
            }
        }
    }
}
