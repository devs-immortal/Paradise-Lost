package net.id.aether.world.feature;

import com.mojang.serialization.Codec;
import java.util.Random;
import net.id.aether.tag.AetherBlockTags;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.DeltaFeature;
import net.minecraft.world.gen.feature.DeltaFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;

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
        
        for(BlockPos currentPos : BlockPos.iterateOutwards(origin, xSize, 0, zSize)){
            if(currentPos.getManhattanDistance(origin) > size){
                break;
            }
            
            if(canPlace(world, currentPos, featureConfig)){
                if(bl3){
                    modified = true;
                    setBlockState(world, currentPos, featureConfig.getRim());
                }
                
                BlockPos blockPos3 = currentPos.add(i, 0, j);
                if(canPlace(world, blockPos3, featureConfig)){
                    modified = true;
                    setBlockState(world, blockPos3, featureConfig.getContents());
                }
            }
        }
        
        return modified;
    }
    
    private static boolean canPlace(WorldAccess world, BlockPos pos, DeltaFeatureConfig config){
        BlockState blockState = world.getBlockState(pos);
    
        if(!AetherBlockTags.FLUID_REPLACEABLES.contains(blockState.getBlock())){
            return false;
        }
        
        if(blockState.isOf(config.getContents().getBlock())){
            return false;
        }else if(blockState.getHardness(world, pos) <= -1){
            return false;
        }else{
            for(Direction direction : Direction.values()){
                boolean isAir = !world.getBlockState(pos.offset(direction)).getMaterial().isSolid();
                if(isAir && direction != Direction.UP || !isAir && direction == Direction.UP){
                    return false;
                }
            }
            
            return true;
        }
    }
}
