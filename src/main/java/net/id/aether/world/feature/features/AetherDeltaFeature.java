package net.id.aether.world.feature.features;

import com.mojang.serialization.Codec;
import net.id.aether.tag.AetherBlockTags;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.DeltaFeature;
import net.minecraft.world.gen.feature.DeltaFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.HashSet;
import java.util.Set;

public class AetherDeltaFeature extends DeltaFeature{
    
    public AetherDeltaFeature(Codec<DeltaFeatureConfig> codec){
        super(codec);
    }
    
    @Override
    public boolean generate(FeatureContext<DeltaFeatureConfig> context){
        boolean modified = false;
        Random random = context.getRandom();
        StructureWorldAccess world = context.getWorld();
        DeltaFeatureConfig featureConfig = context.getConfig();
        final BlockPos origin = context.getOrigin();
        boolean bl2 = random.nextDouble() < 0.9D;
        int i = bl2 ? featureConfig.getRimSize().get(random) : 0;
        int j = bl2 ? featureConfig.getRimSize().get(random) : 0;
        boolean bl3 = bl2 && i != 0 && j != 0;

        int xSize = featureConfig.getSize().get(random);
        int zSize = featureConfig.getSize().get(random);
        int size = Math.max(xSize, zSize);
        
        Set<BlockPos> filledPositions = new HashSet<>();
        
        var rim = featureConfig.getRim();
        var contents = featureConfig.getContents();
        
        for(BlockPos currentPos : BlockPos.iterateOutwards(origin, xSize, 0, zSize)){
            if(currentPos.getManhattanDistance(origin) > size){
                break;
            }
            
            if(canPlace(world, currentPos, contents, filledPositions)){
                if(bl3){
                    modified = true;
                    setBlockState(world, currentPos, rim);
                    filledPositions.add(currentPos);
                }
                
                BlockPos blockPos3 = currentPos.add(i, 0, j);
                if(canPlace(world, blockPos3, contents, filledPositions)){
                    modified = true;
                    setBlockState(world, blockPos3, contents);
                    filledPositions.add(blockPos3);
                }
            }
        }
        
        return modified;
    }
    
    private static boolean canPlace(WorldAccess world, BlockPos pos, BlockState contents, Set<BlockPos> filledPositions){
        BlockState blockState = world.getBlockState(pos);
    
        if(!blockState.isIn(AetherBlockTags.FLUID_REPLACEABLES)) {
            return false;
        }
        
        if(blockState.isOf(contents.getBlock())){
            return false;
        }else if(blockState.getHardness(world, pos) <= -1){
            return false;
        }else{
            for(Direction direction : Direction.values()){
                var currentPos = pos.offset(direction);
                if(filledPositions.contains(currentPos)){
                    continue;
                }
                boolean isAir = !world.getBlockState(currentPos).getMaterial().isSolid();
                if(isAir && direction != Direction.UP || !isAir && direction == Direction.UP){
                    return false;
                }
            }
            
            return true;
        }
    }
}
