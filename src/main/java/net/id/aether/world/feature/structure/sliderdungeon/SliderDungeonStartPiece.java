package net.id.aether.world.feature.structure.sliderdungeon;

import java.util.Random;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.SimpleStructurePiece;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;

public final class SliderDungeonStartPiece extends SimpleStructurePiece{
    public SliderDungeonStartPiece(StructureManager manager, Identifier identifier, BlockPos pos){
        super(SliderDungeonFeature.PIECE_START, 0, manager, identifier, identifier.toString(), createPlacementData(), pos);
    }
    
    public SliderDungeonStartPiece(ServerWorld world, NbtCompound nbt){
        super(SliderDungeonFeature.PIECE_START, nbt, world, (identifier)->createPlacementData());
    }
    
    private static StructurePlacementData createPlacementData(){
        return new StructurePlacementData().addProcessor(BlockIgnoreStructureProcessor.IGNORE_STRUCTURE_BLOCKS);
    }
    
    @Override
    protected void handleMetadata(String metadata, BlockPos pos, ServerWorldAccess world, Random random, BlockBox boundingBox){}
}
