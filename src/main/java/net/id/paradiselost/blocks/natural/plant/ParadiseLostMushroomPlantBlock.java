package net.id.paradiselost.blocks.natural.plant;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.*;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import java.util.Iterator;
import java.util.Optional;
import java.util.Random;

public class ParadiseLostMushroomPlantBlock extends PlantBlock implements Fertilizable {

    public static final MapCodec<ParadiseLostMushroomPlantBlock> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            TagKey.codec(RegistryKeys.BLOCK).fieldOf("plantable_on").forGetter((block) -> block.plantableOn),
            RegistryKey.createCodec(RegistryKeys.CONFIGURED_FEATURE).fieldOf("feature").forGetter(block -> block.featureKey),
            createSettingsCodec()
    ).apply(instance, ParadiseLostMushroomPlantBlock::new));
    protected final TagKey<Block> plantableOn;
    private final RegistryKey<ConfiguredFeature<?, ?>> featureKey;

    public ParadiseLostMushroomPlantBlock(TagKey<Block> plantableOn, RegistryKey<ConfiguredFeature<?, ?>> featureKey, Settings settings) {
        super(settings);
        this.plantableOn = plantableOn;
        this.featureKey = featureKey;
    }
    
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return Block.createCuboidShape(5.0D, 0.0D, 5.0D, 11.0D, 6.0D, 11.0D);
    }
    
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (random.nextInt(25) == 0) {
            int i = 5;
            Iterator var7 = BlockPos.iterate(pos.add(-4, -1, -4), pos.add(4, 1, 4)).iterator();
            
            while (var7.hasNext()) {
                BlockPos blockPos = (BlockPos) var7.next();
                if (world.getBlockState(blockPos).isOf(this)) {
                    --i;
                    if (i <= 0) {
                        return;
                    }
                }
            }
            
            BlockPos blockPos2 = pos.add(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);
            
            for (int k = 0; k < 4; ++k) {
                if (world.isAir(blockPos2) && state.canPlaceAt(world, blockPos2)) {
                    pos = blockPos2;
                }
                
                blockPos2 = pos.add(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);
            }
            
            if (world.isAir(blockPos2) && state.canPlaceAt(world, blockPos2)) {
                world.setBlockState(blockPos2, state, 2);
            }
        }
        
    }

    @Override
    protected MapCodec<? extends PlantBlock> getCodec() {
        return CODEC;
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.isOpaqueFullCube(world, pos);
    }
    
    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockPos blockPos = pos.down();
        BlockState blockState = world.getBlockState(blockPos);
        if (blockState.isIn(plantableOn)) {
            return true;
        } else {
            return world.getBaseLightLevel(pos, 0) < 13 && canPlantOnTop(blockState, world, blockPos);
        }
    }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public boolean canGrow(World world, net.minecraft.util.math.random.Random random, BlockPos pos, BlockState state) {
        return random.nextFloat() < 0.4;
    }

    @Override
    public void grow(ServerWorld world, net.minecraft.util.math.random.Random random, BlockPos pos, BlockState state) {
        Optional<? extends RegistryEntry<ConfiguredFeature<?, ?>>> optional = world.getRegistryManager()
                .get(RegistryKeys.CONFIGURED_FEATURE)
                .getEntry(this.featureKey);
        if (!optional.isEmpty()) {
            world.removeBlock(pos, false);
            if (!((ConfiguredFeature) ((RegistryEntry) optional.get()).value()).generate(world, world.getChunkManager().getChunkGenerator(), random, pos)) {
                world.setBlockState(pos, state, Block.NOTIFY_ALL);
            }
        }
    }
}
