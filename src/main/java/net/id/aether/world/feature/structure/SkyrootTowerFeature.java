package net.id.aether.world.feature.structure;

import com.mojang.serialization.Codec;
import net.id.aether.Aether;
import net.id.aether.world.feature.structure.generator.SkyrootTowerGenerator;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructureGeneratorFactory;
import net.minecraft.structure.StructurePiecesCollector;
import net.minecraft.structure.StructurePiecesGenerator;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public class SkyrootTowerFeature extends StructureFeature<DefaultFeatureConfig> {
    public SkyrootTowerFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec, StructureGeneratorFactory.simple(StructureGeneratorFactory.checkForBiomeOnTop(Heightmap.Type.WORLD_SURFACE_WG), SkyrootTowerFeature::addPieces));
    }

    private static void addPieces(StructurePiecesCollector collector, StructurePiecesGenerator.Context<DefaultFeatureConfig> context) {
        Structure structure = context.structureManager().getStructureOrBlank(Aether.locate("skyroot_tower"));
        BlockRotation blockRotation = BlockRotation.NONE;
        ChunkPos pos = context.chunkPos();
        BlockPos pivot = new BlockPos(structure.getSize().getX() / 2, 0, structure.getSize().getZ() / 2);
        BlockBox boundingBox = structure.calculateBoundingBox(pos.getStartPos(), blockRotation, pivot, BlockMirror.NONE);
        BlockPos center = boundingBox.getCenter();
        int y = context.chunkGenerator().getHeight(pos.getStartPos().getX() - 4, pos.getStartPos().getZ() - 4, Heightmap.Type.WORLD_SURFACE_WG, context.world());
        BlockPos newPos = new BlockPos(pos.getStartPos().getX() - 4, y, pos.getStartPos().getZ() - 4);
        SkyrootTowerGenerator.addPieces(context.structureManager(), collector, blockRotation, newPos);
    }

    // TODO: 1.18 Migrate needed
    /*@Override
    @Override
    protected boolean shouldStartAt(ChunkGenerator chunkGenerator, BiomeSource biomeSource, long worldSeed, ChunkRandom random, ChunkPos pos, Biome biome, ChunkPos chunkPos, DefaultFeatureConfig config, HeightLimitView world) {
        return chunkGenerator.getHeight(chunkPos.x * 16, chunkPos.z * 16, Heightmap.Type.WORLD_SURFACE_WG, world) >= 7;
    }*/

    @Override
    public GenerationStep.Feature getGenerationStep() {
        return GenerationStep.Feature.SURFACE_STRUCTURES;
    }
}
