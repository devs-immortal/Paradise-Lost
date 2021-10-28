package net.id.aether.world.feature.structure.sliderdungeon;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.*;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

import static net.id.aether.Aether.locate;

public final class SliderDungeonFeature extends StructureFeature<DefaultFeatureConfig>{
    static final Identifier START = locate("slider_dungeon/start");
    
    static final StructurePieceType PIECE_START = SliderDungeonStartPiece::new;
    static final StructureFeature<DefaultFeatureConfig> DUNGEON = new SliderDungeonFeature(DefaultFeatureConfig.CODEC);
    static final ConfiguredStructureFeature<?, ?> CONFIGURED_DUNGEON = DUNGEON.configure(DefaultFeatureConfig.INSTANCE);
    
    public SliderDungeonFeature(Codec<DefaultFeatureConfig> codec){
        super(codec);
    }
    
    public static void register(){
        Registry.register(Registry.STRUCTURE_PIECE, START, PIECE_START);
        FabricStructureBuilder.create(locate("slider_dungeon"), DUNGEON)
            .step(GenerationStep.Feature.SURFACE_STRUCTURES)
            .defaultConfig(32, 8, 12345)
            .adjustsSurface()
            .register();
        
        var configured = RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY, locate("slider_dungeon"));
        BuiltinRegistries.add(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, configured.getValue(), CONFIGURED_DUNGEON);
    
        BiomeModifications.addStructure(BiomeSelectors.all(), configured);
    }
    
    @Override
    public StructureStartFactory<DefaultFeatureConfig> getStructureStartFactory(){
        return Start::new;
    }
    
    public static final class Start extends StructureStart<DefaultFeatureConfig>{
        public Start(StructureFeature<DefaultFeatureConfig> feature, ChunkPos pos, int references, long seed){
            super(feature, pos, references, seed);
        }
    
        @Override
        public void init(DynamicRegistryManager registryManager, ChunkGenerator chunkGenerator, StructureManager manager, ChunkPos chunkPos, Biome biome, DefaultFeatureConfig config, HeightLimitView world){
            int x = chunkPos.x << 4;
            int z = chunkPos.z << 4;
            int y = chunkGenerator.getHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG, world);
            var blockPos = new BlockPos(x, y, z);
            SliderDungeonGenerator.addPieces(manager, blockPos, children);
            setBoundingBoxFromChildren();
        }
    }
}
