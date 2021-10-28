package net.id.aether.world.feature.structure.sliderdungeon;

import java.util.List;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.math.BlockPos;

public final class SliderDungeonGenerator{
    public static void addPieces(StructureManager manager, BlockPos pos, List<StructurePiece> pieces){
        pieces.add(new SliderDungeonStartPiece(manager, SliderDungeonFeature.START, pos));
    }
}
