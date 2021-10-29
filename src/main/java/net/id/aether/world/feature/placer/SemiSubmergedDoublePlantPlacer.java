package net.id.aether.world.feature.placer;

import net.minecraft.block.BlockState;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.placer.DoublePlantPlacer;

import java.util.Random;

public class SemiSubmergedDoublePlantPlacer extends DoublePlantPlacer {

    public void generate(WorldAccess world, BlockPos pos, BlockState state, Random random) {
        TallPlantBlock.placeAt(world, state, pos, 2);
    }

}
