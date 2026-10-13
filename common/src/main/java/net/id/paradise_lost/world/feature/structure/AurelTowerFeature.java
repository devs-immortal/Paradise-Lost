package net.id.paradise_lost.world.feature.structure;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.MapCodec;
import net.id.paradise_lost.world.feature.structure.generator.AurelTowerGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import java.util.Optional;
import net.id.paradise_lost.ModConstants;

public class AurelTowerFeature extends Structure {
    public static final MapCodec<AurelTowerFeature> CODEC = simpleCodec(AurelTowerFeature::new);

    private static final int X_OFFSET = 4;
    private static final int Z_OFFSET = 4;

    public AurelTowerFeature(Structure.StructureSettings config) {
        super(config);
    }

    private static void addPieces(StructurePiecesBuilder collector, GenerationContext context) {
        StructureTemplate structure = context.structureTemplateManager().getOrCreate(ModConstants.id("aurel_tower"));
        Rotation blockRotation = Rotation.NONE;
        ChunkPos pos = context.chunkPos();
        BlockPos pivot = new BlockPos(structure.getSize().getX() / 2, 0, structure.getSize().getZ() / 2);
        BoundingBox boundingBox = structure.getBoundingBox(pos.getWorldPosition(), blockRotation, pivot, Mirror.NONE);
        BlockPos center = boundingBox.getCenter();
        int y = context.chunkGenerator().getBaseHeight(pos.getWorldPosition().getX() - X_OFFSET, pos.getWorldPosition().getZ() - Z_OFFSET, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());
        if (y < 0) {
            return;
        }
        BlockPos newPos = new BlockPos(pos.getWorldPosition().getX() - X_OFFSET, y, pos.getWorldPosition().getZ() - Z_OFFSET);
        AurelTowerGenerator.addPieces(context.structureTemplateManager(), collector, Rotation.NONE, newPos);
    }

    @Override
    public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        context.random().nextDouble();
        ChunkPos chunkPos = context.chunkPos();
        BlockPos blockPos = new BlockPos(chunkPos.getMiddleBlockX(), 50, chunkPos.getMinBlockZ());
        StructurePiecesBuilder structurePiecesCollector = new StructurePiecesBuilder();
        addPieces(structurePiecesCollector, context);
        return Optional.of(new Structure.GenerationStub(blockPos, Either.right(structurePiecesCollector)));
    }

    @Override
    public StructureType<?> type() {
        return ParadiseLostStructureFeatures.AUREL_TOWER;
    }
}
