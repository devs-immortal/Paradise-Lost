package net.id.paradise_lost.world.feature.structure.processor;

import com.mojang.serialization.MapCodec;
import net.id.paradise_lost.registry.StructureProcessorsRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class TrialSpawnerMarkerProcessor extends StructureProcessor {
    public static final MapCodec<TrialSpawnerMarkerProcessor> CODEC = MapCodec.unit(TrialSpawnerMarkerProcessor::new);

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos offset, BlockPos pivot,
                                                             StructureTemplate.StructureBlockInfo original, StructureTemplate.StructureBlockInfo current,
                                                             StructurePlaceSettings settings) {
        if (!current.state().is(Blocks.TRIAL_SPAWNER)) return current;
        return level.getBlockState(current.pos()).isAir() ? null : current;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return StructureProcessorsRegistry.SUPPORT_CHECKER_PROCESSOR.get();
    }
}
