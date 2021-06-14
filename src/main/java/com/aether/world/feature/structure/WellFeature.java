package com.aether.world.feature.structure;

import com.mojang.serialization.Codec;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public class WellFeature extends StructureFeature<DefaultFeatureConfig> {
    public WellFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public StructureStartFactory<DefaultFeatureConfig> getStructureStartFactory() {
        return null; // TODO: Stubbed until revisions in 1.17
    }

//    @Override
//    public StructureFeature.StructureStartFactory<DefaultFeatureConfig> getStructureStartFactory() {
//        return Start::new;
//    }
//
//    public static class Start extends StructureStart<DefaultFeatureConfig> {
//        public Start(StructureFeature<DefaultFeatureConfig> feature, int chunkX, int chunkZ, BlockBox box, int references,
//                     long seed) {
//            super(feature, chunkX, chunkZ, box, references, seed);
//        }
//
//        public void init(DynamicRegistryManager registryManager, ChunkGenerator chunkGenerator, StructureManager manager, int chunkX,
//                         int chunkZ, Biome biome, DefaultFeatureConfig config) {
//            int x = chunkX * 16;
//            int z = chunkZ * 16;
//            int y = chunkGenerator.getHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG);
//            BlockPos pos = new BlockPos(x, y, z);
//            BlockRotation rotation = BlockRotation.random(this.random);
//            WellGenerator.addPieces(manager, pos, rotation, this.children);
//            this.setBoundingBoxFromChildren();
//        }
//    }
}
