package net.id.paradiselost.world.feature.structure;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.MapCodec;
import net.id.paradiselost.world.feature.structure.generator.AurelTowerGenerator;
import net.minecraft.structure.*;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

import java.util.Optional;

public class AurelTowerFeature extends Structure {
    public static final MapCodec<AurelTowerFeature> CODEC = createCodec(AurelTowerFeature::new);

    private static final int X_OFFSET = 4;
    private static final int Z_OFFSET = 4;

    public AurelTowerFeature(Structure.Config config) {
        super(config);
    }

    private static void addPieces(StructurePiecesCollector collector, Context context) {
        ChunkPos pos = context.chunkPos();
        int y = context.chunkGenerator().getHeight(
                pos.getStartPos().getX() - X_OFFSET,
                pos.getStartPos().getZ() - Z_OFFSET,
                Heightmap.Type.WORLD_SURFACE_WG,
                context.world(),
                context.noiseConfig()
        );
        // DON'T PLACE ON THE BOTTOM OF THE WORLD
        if (y < 0) {
            return;
        }
        BlockPos newPos = new BlockPos(pos.getStartPos().getX() - X_OFFSET, y, pos.getStartPos().getZ() - Z_OFFSET);
        AurelTowerGenerator.addPieces(context.structureTemplateManager(), collector, BlockRotation.NONE, newPos);
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
        return ParadiseLostStructureFeatures.AUREL_TOWER;
    }
}
