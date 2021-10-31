package net.id.aether.world.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.id.aether.blocks.AetherBlocks;
import net.id.aether.world.feature.config.DynamicConfiguration;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.LightType;
import net.minecraft.world.ModifiableWorld;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class AetherLakeFeature extends Feature<DynamicConfiguration>{
    private static final BlockState CAVE_AIR;
    
    private static final Codec<DynamicConfiguration> CODEC = RecordCodecBuilder.create(instance->instance.group(
        BlockState.CODEC.fieldOf("state").forGetter(DynamicConfiguration::getState),
        Codec.STRING.optionalFieldOf("genType").forGetter(DynamicConfiguration::getGenString)
    ).apply(instance, DynamicConfiguration::new));
    
    static{
        CAVE_AIR = Blocks.CAVE_AIR.getDefaultState();
    }
    
    public AetherLakeFeature(){
        super(CODEC);
    }
    
    public boolean generate(FeatureContext<DynamicConfiguration> context){
        BlockPos blockPos = context.getOrigin();
        
        // Find the highest solid block or give up at a Y level of 60
        while(blockPos.getY() > 60 && context.getWorld().isAir(blockPos)){
            blockPos = blockPos.down();
        }
        
        if(blockPos.getY() <= 59){
            // Too low, didn't find a high enough block
            return false;
        }else{
            // Move the position to the bottom of the lake
            blockPos = blockPos.down(4);
            
            // Bail if there is a village in the current chunk.
            if(context.getWorld().getStructures(ChunkSectionPos.from(blockPos), StructureFeature.VILLAGE).findAny().isPresent()){
                return false;
            }else{
                boolean[] waterMap = new boolean[2048];
                int lakeSize = context.getRandom().nextInt(4) + 4;
                
                // Generate water map
                for(int i = 0; i < lakeSize; i++){
                    double xSize = context.getRandom().nextDouble() * 6.0D + 3.0D;
                    double ySize = context.getRandom().nextDouble() * 4.0D + 2.0D;
                    double zSize = context.getRandom().nextDouble() * 6.0D + 3.0D;
                    double xCenter = context.getRandom().nextDouble() * (16.0D - xSize - 2.0D) + 1.0D + xSize / 2.0D;
                    double yCenter = context.getRandom().nextDouble() * (8.0D - ySize - 4.0D) + 2.0D + ySize / 2.0D;
                    double zCenter = context.getRandom().nextDouble() * (16.0D - zSize - 2.0D) + 1.0D + zSize / 2.0D;
                    
                    for(int xOff = 1; xOff < 15; xOff++){
                        for(int zOff = 1; zOff < 15; zOff++){
                            for(int yOff = 1; yOff < 7; yOff++){
                                double o = ((double)xOff - xCenter) / (xSize / 2.0D);
                                double p = ((double)yOff - yCenter) / (ySize / 2.0D);
                                double q = ((double)zOff - zCenter) / (zSize / 2.0D);
                                double r = o * o + p * p + q * q;
                                if(r < 1.0D){
                                    waterMap[(xOff * 16 + zOff) * 8 + yOff] = true;
                                }
                            }
                        }
                    }
                }
                
                for(int xOff = 0; xOff < 16; xOff++){
                    for(int zOff = 0; zOff < 16; zOff++){
                        for(int yOff = 0; yOff < 8; yOff++){
                            //TODO Break this thing down some.
                            boolean lakeEdge =
                                !waterMap[(xOff * 16 + zOff) * 8 + yOff] &&
                                (
                                    xOff < 15 && waterMap[((xOff + 1) * 16 + zOff) * 8 + yOff] ||
                                    xOff > 0 && waterMap[((xOff - 1) * 16 + zOff) * 8 + yOff] ||
                                    zOff < 15 && waterMap[(xOff * 16 + zOff + 1) * 8 + yOff] ||
                                    zOff > 0 && waterMap[(xOff * 16 + (zOff - 1)) * 8 + yOff] ||
                                    yOff < 7 && waterMap[(xOff * 16 + zOff) * 8 + yOff + 1] ||
                                    yOff > 0 && waterMap[(xOff * 16 + zOff) * 8 + (yOff - 1)]
                                );
                            
                            if(lakeEdge){
                                var state = context.getWorld().getBlockState(blockPos.add(xOff, yOff, zOff));
                                if(state.isOf(AetherBlocks.SKYROOT_LEAF_PILE)){
                                    System.out.flush();
                                }
                                
                                Material material = state.getMaterial();
                                // There is a liquid above the lake, abort
                                if(yOff >= 4 && material.isLiquid()){
                                    return false;
                                }
                                
                                // There is a non-solid, non-lake block on the edge, abort
                                if(yOff < 4 && !material.isSolid() && context.getWorld().getBlockState(blockPos.add(xOff, yOff, zOff)) != context.getConfig().state){
                                    return false;
                                }
                            }
                        }
                    }
                }
                
                // Fill the lake with fluid, including air
                for(int xOff = 0; xOff < 16; xOff++){
                    for(int zOff = 0; zOff < 16; zOff++){
                        for(int yOff = 0; yOff < 8; yOff++){
                            if(waterMap[(xOff * 16 + zOff) * 8 + yOff]){
                                context.getWorld().setBlockState(blockPos.add(xOff, yOff, zOff), yOff >= 4 ? CAVE_AIR : context.getConfig().state, Block.NOTIFY_LISTENERS);
                            }
                        }
                    }
                }
                
                // Replace soil that has access to any skylight with grass
                for(int xOff = 0; xOff < 16; xOff++){
                    for(int zOff = 0; zOff < 16; zOff++){
                        for(int yOff = 4; yOff < 8; yOff++){
                            if(waterMap[(xOff * 16 + zOff) * 8 + yOff]){
                                var blockPos3 = blockPos.add(xOff, yOff - 1, zOff);
                                if(isSoil(context.getWorld().getBlockState(blockPos3)) && context.getWorld().getLightLevel(LightType.SKY, blockPos.add(xOff, yOff, zOff)) > 0){
                                    context.getWorld().setBlockState(blockPos3, AetherBlocks.AETHER_GRASS_BLOCK.getDefaultState(), Block.NOTIFY_LISTENERS);
                                }
                            }
                        }
                    }
                }
                
                // Replace 50% of lave lake edge blocks with holystone.
                if(context.getConfig().state.getMaterial() == Material.LAVA){
                    for(int xOff = 0; xOff < 16; xOff++){
                        for(int zOff = 0; zOff < 16; zOff++){
                            for(int yOff = 0; yOff < 8; yOff++){
                                boolean lakeEdge =
                                    !waterMap[(xOff * 16 + zOff) * 8 + yOff] &&
                                    (
                                        xOff < 15 && waterMap[((xOff + 1) * 16 + zOff) * 8 + yOff] ||
                                        xOff > 0 && waterMap[((xOff - 1) * 16 + zOff) * 8 + yOff] ||
                                        zOff < 15 && waterMap[(xOff * 16 + zOff + 1) * 8 + yOff] ||
                                        zOff > 0 && waterMap[(xOff * 16 + (zOff - 1)) * 8 + yOff] ||
                                        yOff < 7 && waterMap[(xOff * 16 + zOff) * 8 + yOff + 1] ||
                                        yOff > 0 && waterMap[(xOff * 16 + zOff) * 8 + (yOff - 1)]
                                    );
                                if(lakeEdge && (yOff < 4 || context.getRandom().nextInt(2) != 0) && context.getWorld().getBlockState(blockPos.add(xOff, yOff, zOff)).getMaterial().isSolid()){
                                    context.getWorld().setBlockState(blockPos.add(xOff, yOff, zOff), AetherBlocks.HOLYSTONE.getDefaultState(), Block.NOTIFY_LISTENERS);
                                }
                            }
                        }
                    }
                }
                
                // Cover water lakes with ice in frozen biomes
                if(context.getConfig().state.getMaterial() == Material.WATER){
                    for(int xOff = 0; xOff < 16; xOff++){
                        for(int zOff = 0; zOff < 16; zOff++){
                            var blockPos3 = blockPos.add(xOff, 4, zOff);
                            if(context.getWorld().getBiome(blockPos3).canSetIce(context.getWorld(), blockPos3, false)){
                                context.getWorld().setBlockState(blockPos3, Blocks.ICE.getDefaultState(), Block.NOTIFY_LISTENERS);
                            }
                        }
                    }
                }
                
                return true;
            }
        }
    }
}