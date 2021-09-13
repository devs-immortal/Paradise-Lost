package net.id.aether.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.aether.blocks.aercloud.AercloudBlock;
import net.id.aether.fluids.DenseAercloudFluid;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InGameOverlayRenderer.class)
@Environment(EnvType.CLIENT)
public abstract class InGameOverlayRendererMixin {

    @Shadow
    @Nullable
    private static BlockState getInWallBlockState(PlayerEntity player) {
        return null;
    }

    @Inject(method = "renderInWallOverlay", at = @At("HEAD"), cancellable = true)
    private static void renderAercloudOverlay(Sprite sprite, MatrixStack matrices, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        BlockState overlayState = getInWallBlockState(client.player);
        if (overlayState != null && overlayState.getBlock() instanceof AercloudBlock) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.enableTexture();
            RenderSystem.setShaderTexture(0, new Identifier("the_aether", "textures/block/" + Registry.BLOCK.getId(overlayState.getBlock()).getPath() + ".png"));
            BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
            float f = client.player.getBrightnessAtEyes();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderColor(f, f, f, 0.775F);
            float yaw = client.player.getYaw() / 192.0F;
            float pitch = client.player.getPitch() / 192.0F;
            Matrix4f matrix4f = matrices.peek().getModel();
            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
            bufferBuilder.vertex(matrix4f, -1.0F, -1.0F, -0.5F).texture(1.0F - yaw, 1.0F + pitch).next();
            bufferBuilder.vertex(matrix4f, 1.0F, -1.0F, -0.5F).texture(0.0F - yaw, 1.0F + pitch).next();
            bufferBuilder.vertex(matrix4f, 1.0F, 1.0F, -0.5F).texture(0.0F - yaw, 0.0F + pitch).next();
            bufferBuilder.vertex(matrix4f, -1.0F, 1.0F, -0.5F).texture(1.0F - yaw, 0.0F + pitch).next();
            bufferBuilder.end();
            BufferRenderer.draw(bufferBuilder);
            RenderSystem.disableBlend();
            ci.cancel();
        }
    }

    @Inject(method = "getInWallBlockState", at = @At("HEAD"), cancellable = true)
    private static void getInWallBlockState(PlayerEntity player, CallbackInfoReturnable<BlockState> cir) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();

        for (int i = 0; i < 8; ++i) {
            double d = player.getX() + (double) (((float) ((i) % 2) - 0.5F) * player.getWidth() * 0.8F);
            double e = player.getEyeY() + (double) (((float) ((i >> 1) % 2) - 0.5F) * 0.1F);
            double f = player.getZ() + (double) (((float) ((i >> 2) % 2) - 0.5F) * player.getWidth() * 0.8F);
            mutable.set(d, e, f);
            BlockState blockState = player.world.getBlockState(mutable);
            if (blockState.getBlock() instanceof AercloudBlock) {
                cir.setReturnValue(blockState);
                cir.cancel();
            }
        }
    }

    @Inject(method = "renderUnderwaterOverlay", at = @At("HEAD"), cancellable = true)
    private static void renderDenseAercloudOverlay(MinecraftClient client, MatrixStack matrices, CallbackInfo ci) {
        BlockPos pos = new BlockPos(client.player.getEyePos());
        World world = client.player.world;
        if (world.getFluidState(pos).getFluid() instanceof DenseAercloudFluid) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.enableTexture();
            RenderSystem.setShaderTexture(0, new Identifier("the_aether", "textures/block/dense_aercloud_still.png"));
            BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
            float f = client.player.getBrightnessAtEyes();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderColor(f, f, f, 0.8F);
            float m = -client.player.getYaw() / 64.0F;
            float n = client.player.getPitch() / 64.0F;
            Matrix4f matrix4f = matrices.peek().getModel();
            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
            bufferBuilder.vertex(matrix4f, -1.0F, -1.0F, -0.5F).texture(4.0F + m, 4.0F + n).next();
            bufferBuilder.vertex(matrix4f, 1.0F, -1.0F, -0.5F).texture(0.0F + m, 4.0F + n).next();
            bufferBuilder.vertex(matrix4f, 1.0F, 1.0F, -0.5F).texture(0.0F + m, 0.0F + n).next();
            bufferBuilder.vertex(matrix4f, -1.0F, 1.0F, -0.5F).texture(4.0F + m, 0.0F + n).next();
            bufferBuilder.end();
            BufferRenderer.draw(bufferBuilder);
            RenderSystem.disableBlend();
            ci.cancel();
        }
    }
}
