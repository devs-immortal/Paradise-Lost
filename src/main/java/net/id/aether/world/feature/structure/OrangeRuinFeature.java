package net.id.aether.world.feature.structure;

import com.mojang.serialization.Codec;
import net.id.aether.world.feature.generator.OrangeRuinGenerator;
import net.minecraft.structure.StructureGeneratorFactory;
import net.minecraft.structure.StructurePiecesCollector;
import net.minecraft.structure.StructurePiecesGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public class OrangeRuinFeature extends StructureFeature<DefaultFeatureConfig> {
    public OrangeRuinFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec, StructureGeneratorFactory.simple(StructureGeneratorFactory.checkForBiomeOnTop(Heightmap.Type.WORLD_SURFACE_WG), OrangeRuinFeature::addPieces));
    }

    private static void addPieces(StructurePiecesCollector collector, StructurePiecesGenerator.Context<DefaultFeatureConfig> context) {
        int x = context.chunkPos().x * 16;
        int z = context.chunkPos().z * 16;
        int y = context.chunkGenerator().getHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG, context.world());
        BlockPos newPos = new BlockPos(x, y, z);
        OrangeRuinGenerator.addPieces(context.structureManager(), collector, context.random(), newPos);
    }

    @Override
    public GenerationStep.Feature getGenerationStep() {
        return GenerationStep.Feature.SURFACE_STRUCTURES;
    }
}
