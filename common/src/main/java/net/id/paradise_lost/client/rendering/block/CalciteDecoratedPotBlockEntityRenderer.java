package net.id.paradise_lost.client.rendering.block;

import net.id.paradise_lost.block.blockentity.CalciteDecoratedPotBlockEntity;
import net.id.paradise_lost.ModConstants;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartNames;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.entity.DecoratedPotPatterns;
import net.minecraft.world.level.block.entity.PotDecorations;

import java.util.EnumSet;
import java.util.Optional;

import static net.minecraft.client.renderer.Sheets.DECORATED_POT_SHEET;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

public class CalciteDecoratedPotBlockEntityRenderer implements BlockEntityRenderer<CalciteDecoratedPotBlockEntity> {

    public static final Material CALCITE_DECORATED_POT_BASE = createDecoratedPotPatternTextureId(ModConstants.locate("calcite_decorated_pot_base"));
    public static final Material CALCITE_DECORATED_POT_SIDE = createDecoratedPotPatternTextureId(ModConstants.locate("calcite_decorated_pot_side"));

    private static Material createDecoratedPotPatternTextureId(ResourceLocation patternId) {
        return new Material(DECORATED_POT_SHEET, patternId.withPrefix("entity/decorated_pot/"));
    }

    private static final String NECK = "neck";
    private static final String FRONT = "front";
    private static final String BACK = "back";
    private static final String LEFT = "left";
    private static final String RIGHT = "right";
    private static final String TOP = "top";
    private static final String BOTTOM = "bottom";
    private final ModelPart neck;
    private final ModelPart front;
    private final ModelPart back;
    private final ModelPart left;
    private final ModelPart right;
    private final ModelPart top;
    private final ModelPart bottom;
    private static final float WOBBLE_AMPLITUDE = 0.125F;

    public CalciteDecoratedPotBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        ModelPart baseRoot = context.bakeLayer(ModelLayers.DECORATED_POT_BASE);
        this.neck = baseRoot.getChild(PartNames.NECK);
        this.top = baseRoot.getChild("top");
        this.bottom = baseRoot.getChild("bottom");
        ModelPart sidesRoot = context.bakeLayer(ModelLayers.DECORATED_POT_SIDES);
        this.front = sidesRoot.getChild("front");
        this.back = sidesRoot.getChild("back");
        this.left = sidesRoot.getChild("left");
        this.right = sidesRoot.getChild("right");
    }

    public static LayerDefinition getTopBottomNeckTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        CubeDeformation rimDeformation = new CubeDeformation(0.2F);
        CubeDeformation neckDeformation = new CubeDeformation(-0.1F);
        modelPartData.addOrReplaceChild(
                PartNames.NECK,
                CubeListBuilder.create().texOffs(0, 0).addBox(4.0F, 17.0F, 4.0F, 8.0F, 3.0F, 8.0F, neckDeformation).texOffs(0, 5).addBox(5.0F, 20.0F, 5.0F, 6.0F, 1.0F, 6.0F, rimDeformation),
                PartPose.offsetAndRotation(0.0F, 37.0F, 16.0F, (float) Math.PI, 0.0F, 0.0F)
        );
        CubeListBuilder modelPartBuilder = CubeListBuilder.create().texOffs(-14, 13).addBox(0.0F, 0.0F, 0.0F, 14.0F, 0.0F, 14.0F);
        modelPartData.addOrReplaceChild("top", modelPartBuilder, PartPose.offsetAndRotation(1.0F, 16.0F, 1.0F, 0.0F, 0.0F, 0.0F));
        modelPartData.addOrReplaceChild("bottom", modelPartBuilder, PartPose.offsetAndRotation(1.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(modelData, 32, 32);
    }

    public static LayerDefinition getSidesTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        CubeListBuilder modelPartBuilder = CubeListBuilder.create().texOffs(1, 0).addBox(0.0F, 0.0F, 0.0F, 14.0F, 16.0F, 0.0F, EnumSet.of(Direction.NORTH));
        modelPartData.addOrReplaceChild("back", modelPartBuilder, PartPose.offsetAndRotation(15.0F, 16.0F, 1.0F, 0.0F, 0.0F, (float) Math.PI));
        modelPartData.addOrReplaceChild("left", modelPartBuilder, PartPose.offsetAndRotation(1.0F, 16.0F, 1.0F, 0.0F, (float) (-Math.PI / 2), (float) Math.PI));
        modelPartData.addOrReplaceChild("right", modelPartBuilder, PartPose.offsetAndRotation(15.0F, 16.0F, 15.0F, 0.0F, (float) (Math.PI / 2), (float) Math.PI));
        modelPartData.addOrReplaceChild("front", modelPartBuilder, PartPose.offsetAndRotation(1.0F, 16.0F, 15.0F, (float) Math.PI, 0.0F, 0.0F));
        return LayerDefinition.create(modelData, 16, 16);
    }

    private static Material getTextureIdFromSherd(Optional<Item> sherd) {
        if (sherd.isPresent()) {
            Material normalSprite = Sheets.getDecoratedPotMaterial(DecoratedPotPatterns.getPatternFromItem(sherd.get()));

            if (normalSprite != null) {
                var textureIdentifier = normalSprite.texture();

                var calciteSprite = new Material(
                        normalSprite.atlasLocation(),
                        ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, textureIdentifier.getPath() + "_calcite")
                );
                return calciteSprite;
            }
            ResourceLocation patternId = ResourceLocation.fromNamespaceAndPath("sherdsapi", "sherd_pattern");
            @SuppressWarnings("unchecked")
            DataComponentType<ResourceLocation> patternType =
                    (DataComponentType<ResourceLocation>) BuiltInRegistries.DATA_COMPONENT_TYPE.get(patternId);
            ItemStack stack = sherd.get().getDefaultInstance();
            ResourceLocation pattern = patternType != null ? stack.get(patternType) : null;
            if (pattern != null) {
                return new Material(DECORATED_POT_SHEET,
                        ResourceLocation.fromNamespaceAndPath(pattern.getNamespace(), "entity/decorated_pot/" + pattern.getPath() + "_calcite"));
            }
        }

        return CALCITE_DECORATED_POT_SIDE;
    }

    public void render(
            CalciteDecoratedPotBlockEntity decoratedPotBlockEntity, float partialTick, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int light, int overlay
    ) {
        matrixStack.pushPose();
        Direction direction = decoratedPotBlockEntity.getDirection();
        matrixStack.translate(0.5, 0.0, 0.5);
        matrixStack.mulPose(Axis.YP.rotationDegrees(180.0F - direction.toYRot()));
        matrixStack.translate(-0.5, 0.0, -0.5);
        DecoratedPotBlockEntity.WobbleStyle wobbleType = decoratedPotBlockEntity.lastWobbleStyle;
        if (wobbleType != null && decoratedPotBlockEntity.getLevel() != null) {
            float wobbleProgress = ((float) (decoratedPotBlockEntity.getLevel().getGameTime() - decoratedPotBlockEntity.wobbleStartedAtTick) + partialTick) / (float) wobbleType.duration;
            if (wobbleProgress >= 0.0F && wobbleProgress <= 1.0F) {
                if (wobbleType == DecoratedPotBlockEntity.WobbleStyle.POSITIVE) {
                    float angle = wobbleProgress * (float) (Math.PI * 2);
                    float tiltX = -1.5F * (Mth.cos(angle) + 0.5F) * Mth.sin(angle / 2.0F);
                    matrixStack.rotateAround(Axis.XP.rotation(tiltX * 0.015625F), 0.5F, 0.0F, 0.5F);
                    float tiltZ = Mth.sin(angle);
                    matrixStack.rotateAround(Axis.ZP.rotation(tiltZ * 0.015625F), 0.5F, 0.0F, 0.5F);
                } else {
                    float twist = Mth.sin(-wobbleProgress * 3.0F * (float) Math.PI) * WOBBLE_AMPLITUDE;
                    float fade = 1.0F - wobbleProgress;
                    matrixStack.rotateAround(Axis.YP.rotation(twist * fade), 0.5F, 0.0F, 0.5F);
                }
            }
        }

        VertexConsumer vertexConsumer = CALCITE_DECORATED_POT_BASE.buffer(vertexConsumerProvider, RenderType::entitySolid);
        this.neck.render(matrixStack, vertexConsumer, light, overlay);
        this.top.render(matrixStack, vertexConsumer, light, overlay);
        this.bottom.render(matrixStack, vertexConsumer, light, overlay);
        PotDecorations sherds = decoratedPotBlockEntity.getDecorations();
        this.renderDecoratedSide(this.front, matrixStack, vertexConsumerProvider, light, overlay, getTextureIdFromSherd(sherds.front()));
        this.renderDecoratedSide(this.back, matrixStack, vertexConsumerProvider, light, overlay, getTextureIdFromSherd(sherds.back()));
        this.renderDecoratedSide(this.left, matrixStack, vertexConsumerProvider, light, overlay, getTextureIdFromSherd(sherds.left()));
        this.renderDecoratedSide(this.right, matrixStack, vertexConsumerProvider, light, overlay, getTextureIdFromSherd(sherds.right()));
        matrixStack.popPose();
    }

    private void renderDecoratedSide(
            ModelPart part, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay, Material textureId
    ) {
        part.render(matrices, textureId.buffer(vertexConsumers, RenderType::entitySolid), light, overlay);
    }
}
