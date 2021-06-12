package com.aether.world.feature.tree.placers;

import com.aether.world.feature.tree.AetherTreeHell;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

public class WisteriaTrunkPlacer extends TrunkPlacer {

    public static final Codec<WisteriaTrunkPlacer> CODEC = RecordCodecBuilder.create(instance ->
            trunkPlacerParts(instance).apply(instance, WisteriaTrunkPlacer::new));

    public WisteriaTrunkPlacer(int baseHeight, int firstRandomHeight, int secondRandomHeight) {
        super(baseHeight, firstRandomHeight, secondRandomHeight);
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return AetherTreeHell.WISTERIA_TRUNK;
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader world, BiConsumer<BlockPos, BlockState> replacer, Random random, int height, BlockPos startPos, TreeConfiguration config) {
        List<FoliagePlacer.FoliageAttachment> nodes = new ArrayList<>();

        // TODO: Revise code for 1.17
//        for (int i = 0; i < baseHeight; i++) {
//            getAndSetState(world, random, pos, placedStates, box, config);
//            pos = pos.up();
//        }
//
//        for (Direction dir : new Direction[] {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST}) {
//            int vOffset = random.nextInt(3) - 1;
//            int branchSize = 2 + random.nextInt(2);
//
//            BlockPos tempPos = pos.down(2);
//            tempPos = tempPos.up(vOffset);
//
//            for(int i = 0; i < branchSize; i++) {
//                tempPos = tempPos.offset(dir);
//                if (random.nextBoolean()) tempPos = tempPos.up();
//                getAndSetState(world, random, tempPos, placedStates, box, config);
//            }
//            nodes.add(new FoliagePlacer.TreeNode(tempPos, 0, false));
//        }

        return nodes;
    }
}
