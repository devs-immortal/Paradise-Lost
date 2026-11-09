package net.id.paradise_lost.block.natural.plant;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import java.util.Iterator;
import java.util.Optional;

public class ParadiseLostMushroomPlantBlock extends BushBlock implements BonemealableBlock {

    public static final MapCodec<ParadiseLostMushroomPlantBlock> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            TagKey.hashedCodec(Registries.BLOCK).fieldOf("plantable_on").forGetter((block) -> block.plantableOn),
            ResourceKey.codec(Registries.CONFIGURED_FEATURE).fieldOf("feature").forGetter(block -> block.featureKey),
            propertiesCodec()
    ).apply(instance, ParadiseLostMushroomPlantBlock::new));
    protected final TagKey<Block> plantableOn;
    private final ResourceKey<ConfiguredFeature<?, ?>> featureKey;

    public ParadiseLostMushroomPlantBlock(TagKey<Block> plantableOn, ResourceKey<ConfiguredFeature<?, ?>> featureKey, Properties settings) {
        super(settings);
        this.plantableOn = plantableOn;
        this.featureKey = featureKey;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return Block.box(5.0D, 0.0D, 5.0D, 11.0D, 6.0D, 11.0D);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (random.nextInt(25) == 0) {
            int remaining = 5;
            Iterator nearby = BlockPos.betweenClosed(pos.offset(-4, -1, -4), pos.offset(4, 1, 4)).iterator();

            while (nearby.hasNext()) {
                BlockPos blockPos = (BlockPos) nearby.next();
                if (world.getBlockState(blockPos).is(this)) {
                    --remaining;
                    if (remaining <= 0) {
                        return;
                    }
                }
            }

            BlockPos spreadPos = pos.offset(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);

            for (int attempt = 0; attempt < 4; ++attempt) {
                if (world.isEmptyBlock(spreadPos) && state.canSurvive(world, spreadPos)) {
                    pos = spreadPos;
                }

                spreadPos = pos.offset(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);
            }

            if (world.isEmptyBlock(spreadPos) && state.canSurvive(world, spreadPos)) {
                world.setBlock(spreadPos, state, 2);
            }
        }

    }

    @Override
    protected MapCodec<? extends BushBlock> codec() {
        return CODEC;
    }

    @Override
    protected boolean mayPlaceOn(BlockState floor, BlockGetter world, BlockPos pos) {
        return floor.isSolidRender(world, pos);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        BlockPos blockPos = pos.below();
        BlockState blockState = world.getBlockState(blockPos);
        if (blockState.is(plantableOn)) {
            return true;
        } else {
            return world.getRawBrightness(pos, 0) < 13 && mayPlaceOn(blockState, world, blockPos);
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
        return random.nextFloat() < 0.4;
    }

    @Override
    public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
        Optional<? extends Holder<ConfiguredFeature<?, ?>>> optional = world.registryAccess()
                .registryOrThrow(Registries.CONFIGURED_FEATURE)
                .getHolder(this.featureKey);
        if (!optional.isEmpty()) {
            world.removeBlock(pos, false);
            if (!((ConfiguredFeature) ((Holder) optional.get()).value()).place(world, world.getChunkSource().getGenerator(), random, pos)) {
                world.setBlock(pos, state, Block.UPDATE_ALL);
            }
        }
    }
}
