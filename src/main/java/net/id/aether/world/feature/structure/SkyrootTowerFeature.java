package net.id.aether.world.feature.structure;

import com.mojang.serialization.Codec;
import net.id.aether.Aether;
import net.id.aether.world.feature.generator.SkyrootTowerGenerator;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public class SkyrootTowerFeature extends StructureFeature<DefaultFeatureConfig> {
    public SkyrootTowerFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public StructureStartFactory<DefaultFeatureConfig> getStructureStartFactory() {
        return SkyrootTowerFeature.Start::new;
    }

    @Override
    protected boolean shouldStartAt(ChunkGenerator chunkGenerator, BiomeSource biomeSource, long worldSeed, ChunkRandom random, ChunkPos pos, Biome biome, ChunkPos chunkPos, DefaultFeatureConfig config, HeightLimitView world) {
        return chunkGenerator.getHeight(chunkPos.x * 16, chunkPos.z * 16, Heightmap.Type.WORLD_SURFACE_WG, world) >= 7;
    }

    @Override
    public GenerationStep.Feature getGenerationStep() {
        return GenerationStep.Feature.SURFACE_STRUCTURES;
    }

    public static class Start extends StructureStart<DefaultFeatureConfig> {

        public Start(StructureFeature<DefaultFeatureConfig> feature, ChunkPos pos, int references, long seed) {
            super(feature, pos, references, seed);
        }

        @Override
        public void init(DynamicRegistryManager registryManager, ChunkGenerator chunkGenerator, StructureManager manager, ChunkPos pos, Biome biome, DefaultFeatureConfig config, HeightLimitView world) {
            Structure structure = manager.getStructureOrBlank(Aether.locate("skyroot_tower"));
            BlockRotation blockRotation = BlockRotation.NONE;
            BlockPos pivot = new BlockPos(structure.getSize().getX() / 2, 0, structure.getSize().getZ() / 2);
            BlockBox boundingBox = structure.calculateBoundingBox(pos.getStartPos(), blockRotation, pivot, BlockMirror.NONE);
            BlockPos center = boundingBox.getCenter();
            int y = chunkGenerator.getHeight(pos.getStartPos().getX() - 4, pos.getStartPos().getZ() - 4, Heightmap.Type.WORLD_SURFACE_WG, world);
            BlockPos newPos = new BlockPos(pos.getStartPos().getX() - 4, y, pos.getStartPos().getZ() - 4);
            SkyrootTowerGenerator.addPieces(manager, this, blockRotation, newPos);
            this.getBoundingBox();
        }
    }
}
