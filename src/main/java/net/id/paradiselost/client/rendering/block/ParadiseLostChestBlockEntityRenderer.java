package net.id.paradiselost.client.rendering.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.paradiselost.client.rendering.texture.ParadiseLostChestTexture;
import net.minecraft.block.*;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.enums.ChestType;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.LightmapCoordinatesRetriever;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public final class ParadiseLostChestBlockEntityRenderer implements BlockEntityRenderer<ChestBlockEntity>{
    private final ModelPart singleChestLid;
    private final ModelPart singleChestBase;
    private final ModelPart singleChestLatch;
    private final ModelPart doubleChestRightLid;
    private final ModelPart doubleChestRightBase;
    private final ModelPart doubleChestRightLatch;
    private final ModelPart doubleChestLeftLid;
    private final ModelPart doubleChestLeftBase;
    private final ModelPart doubleChestLeftLatch;
    
    private final ParadiseLostChestTexture textures;
    
    public ParadiseLostChestBlockEntityRenderer(BlockEntityRendererFactory.Context ctx, ParadiseLostChestTexture textures){
        this.textures = textures;
        
        ModelPart singleChest = ctx.getLayerModelPart(EntityModelLayers.CHEST);
        singleChestBase = singleChest.getChild("bottom");
        singleChestLid = singleChest.getChild("lid");
        singleChestLatch = singleChest.getChild("lock");
        ModelPart leftChest = ctx.getLayerModelPart(EntityModelLayers.DOUBLE_CHEST_LEFT);
        doubleChestRightBase = leftChest.getChild("bottom");
        doubleChestRightLid = leftChest.getChild("lid");
        doubleChestRightLatch = leftChest.getChild("lock");
        ModelPart rightChest = ctx.getLayerModelPart(EntityModelLayers.DOUBLE_CHEST_RIGHT);
        doubleChestLeftBase = rightChest.getChild("bottom");
        doubleChestLeftLid = rightChest.getChild("lid");
        doubleChestLeftLatch = rightChest.getChild("lock");
    }
    
    public void render(ChestBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay){
        World world = entity.getWorld();
        boolean useCache = world != null;
        BlockState blockState = useCache ? entity.getCachedState() : Blocks.CHEST.getDefaultState().with(ChestBlock.FACING, Direction.SOUTH);
        ChestType chestType = blockState.contains(ChestBlock.CHEST_TYPE) ? blockState.get(ChestBlock.CHEST_TYPE) : ChestType.SINGLE;
        Block block = blockState.getBlock();
        
        if(block instanceof ChestBlock chestBlock){
            boolean doubleChest = chestType != ChestType.SINGLE;

            matrices.push();

            float rotation = blockState.get(ChestBlock.FACING).asRotation();
            matrices.translate(0.5D, 0.5D, 0.5D);
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-rotation));
            matrices.translate(-0.5D, -0.5D, -0.5D);

            DoubleBlockProperties.PropertySource<? extends ChestBlockEntity> propSource;
            if(useCache){
                propSource = chestBlock.getBlockEntitySource(blockState, world, entity.getPos(), true);
            }else{
                propSource = DoubleBlockProperties.PropertyRetriever::getFallback;
            }
            
            float animationProgress = propSource.apply(ChestBlock.getAnimationProgressRetriever(entity)).get(tickDelta);
            animationProgress = 1.0F - animationProgress;
            animationProgress = 1.0F - animationProgress * animationProgress * animationProgress;
            
            int lightCoords = propSource.apply(new LightmapCoordinatesRetriever<>()).applyAsInt(light);
            
            SpriteIdentifier spriteIdentifier = getChestTexture(chestType);
            VertexConsumer vertexConsumer = spriteIdentifier.getVertexConsumer(vertexConsumers, RenderLayer::getEntityCutout);
            if(doubleChest){
                if(chestType == ChestType.LEFT){
                    render(matrices, vertexConsumer, doubleChestRightLid, doubleChestRightLatch, doubleChestRightBase, animationProgress, lightCoords, overlay);
                }else{
                    render(matrices, vertexConsumer, doubleChestLeftLid, doubleChestLeftLatch, doubleChestLeftBase, animationProgress, lightCoords, overlay);
                }
            }else{
                render(matrices, vertexConsumer, singleChestLid, singleChestLatch, singleChestBase, animationProgress, lightCoords, overlay);
            }
            
            matrices.pop();
        }
    }
    
    private SpriteIdentifier getChestTexture(ChestType chestType){
        return switch(chestType){
            case SINGLE -> textures.single();
            case LEFT -> textures.left();
            case RIGHT -> textures.right();
        };
    }
    
    private void render(MatrixStack matrices, VertexConsumer vertices, ModelPart lid, ModelPart latch, ModelPart base, float openFactor, int light, int overlay){
        lid.pitch = -(openFactor * 1.5707964F);
        latch.pitch = lid.pitch;
        lid.render(matrices, vertices, light, overlay);
        latch.render(matrices, vertices, light, overlay);
        base.render(matrices, vertices, light, overlay);
    }
}
