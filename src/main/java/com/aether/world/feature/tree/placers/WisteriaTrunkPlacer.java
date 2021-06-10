package com.aether.world.feature.tree.placers;

import com.aether.world.feature.tree.AetherTreeHell;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.ModifiableTestableWorld;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.trunk.TrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacerType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class WisteriaTrunkPlacer extends TrunkPlacer {

    public static final Codec<WisteriaTrunkPlacer> CODEC = RecordCodecBuilder.create(instance ->
            fillTrunkPlacerFields(instance).apply(instance, WisteriaTrunkPlacer::new));

    public WisteriaTrunkPlacer(int baseHeight, int firstRandomHeight, int secondRandomHeight) {
        super(baseHeight, firstRandomHeight, secondRandomHeight);
    }

    @Override
    protected TrunkPlacerType<?> getType() {
        return AetherTreeHell.WISTERIA_TRUNK;
    }

    @Override
    public List<FoliagePlacer.TreeNode> generate(ModifiableTestableWorld world, Random random, int trunkHeight, BlockPos pos, Set<BlockPos> placedStates, BlockBox box, TreeFeatureConfig config) {
        List<FoliagePlacer.TreeNode> nodes = new ArrayList<>();

        for (int i = 0; i < baseHeight; i++) {
            getAndSetState(world, random, pos, placedStates, box, config);
            pos = pos.up();
        }

        for (Direction dir : new Direction[] {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST}) {
            int vOffset = random.nextInt(3) - 1;
            int branchSize = 2 + random.nextInt(2);

            BlockPos tempPos = pos.down(2);
            tempPos = tempPos.up(vOffset);

            for(int i = 0; i < branchSize; i++) {
                tempPos = tempPos.offset(dir);
                if (random.nextBoolean()) tempPos = tempPos.up();
                getAndSetState(world, random, tempPos, placedStates, box, config);
            }
            nodes.add(new FoliagePlacer.TreeNode(tempPos, 0, false));
        }

        return nodes;
    }
}
