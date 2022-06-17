package net.id.aether.world.feature.structure;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.id.aether.world.feature.structure.generator.OrangeRuinGenerator;
import net.minecraft.structure.StructurePiecesCollector;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

import java.util.Optional;

public class OrangeRuinFeature extends Structure {
    public static final Codec<OrangeRuinFeature> CODEC = createCodec(OrangeRuinFeature::new);
    
    public OrangeRuinFeature(Structure.Config config) {
        super(config);
    }
    
    private static void addPieces(StructurePiecesCollector collector, Context context) {
        int x = context.chunkPos().x * 16;
        int z = context.chunkPos().z * 16;
        int y = context.chunkGenerator().getHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG, context.world(), context.noiseConfig());
        BlockPos newPos = new BlockPos(x, y, z);
        OrangeRuinGenerator.addPieces(context.structureTemplateManager(), collector, context.random(), newPos);
    }
    
    @Override
    public Optional<StructurePosition> getStructurePosition(Context context) {
        context.random().nextDouble();
        ChunkPos chunkPos = context.chunkPos();
        BlockPos blockPos = new BlockPos(chunkPos.getCenterX(), 50, chunkPos.getStartZ());
        StructurePiecesCollector structurePiecesCollector = new StructurePiecesCollector();
        addPieces(structurePiecesCollector, context);
        return Optional.of(new Structure.StructurePosition(blockPos, Either.right(structurePiecesCollector)));
    }
    
    @Override
    public StructureType<?> getType() {
        return AetherStructureFeatures.ORANGE_RUIN;
    }
}
