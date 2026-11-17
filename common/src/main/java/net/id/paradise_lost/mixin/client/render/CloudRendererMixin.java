package net.id.paradise_lost.mixin.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexBuffer;
import net.id.paradise_lost.client.rendering.CloudMeshCache;
import net.id.paradise_lost.world.dimension.ParadiseLostDimension;
import net.minecraft.client.CloudStatus;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.stream.Stream;

@Mixin(LevelRenderer.class)
public abstract class CloudRendererMixin {
    private record CloudLayer(float offset, float scale, float speed) {
    }

    @Unique
    private static final CloudLayer[] paradiseLost$LAYERS = {
            new CloudLayer(160, 1f, 1f),
            new CloudLayer(64, 1.25f, -2f),
            new CloudLayer(-196, 2f, 1.5f)
    };

    @Shadow
    private ClientLevel level;
    @Shadow
    private int ticks;
    @Shadow
    @Nullable
    private VertexBuffer cloudBuffer;
    @Shadow
    private int prevCloudX;
    @Shadow
    private int prevCloudY;
    @Shadow
    private int prevCloudZ;
    @Shadow
    private Vec3 prevCloudColor;
    @Shadow
    @Nullable
    private CloudStatus prevCloudsType;
    @Shadow
    private boolean generateClouds;
    @Unique
    private final CloudMeshCache[] paradiseLost$caches = Stream.generate(CloudMeshCache::new).limit(paradiseLost$LAYERS.length).toArray(CloudMeshCache[]::new);
    @Unique
    private boolean paradiseLost$renderingLayers;

    @Shadow
    public abstract void renderClouds(PoseStack poseStack, Matrix4f frustumMatrix, Matrix4f projectionMatrix, float partialTick, double camX, double camY, double camZ);

    @Inject(method = "renderClouds", at = @At("HEAD"), cancellable = true)
    private void paradiseLost$renderLayers(PoseStack poseStack, Matrix4f frustumMatrix, Matrix4f projectionMatrix, float partialTick, double camX, double camY, double camZ, CallbackInfo ci) {
        if (this.paradiseLost$renderingLayers || this.level.dimension() != ParadiseLostDimension.PARADISE_LOST_WORLD_KEY) {
            return;
        }
        ci.cancel();
        this.paradiseLost$forwardRebuildToLayers();

        float height = this.level.effects().getCloudHeight();
        double scroll = ((float) this.ticks + partialTick) * 0.03F;
        for (int i = 0; i < paradiseLost$LAYERS.length; i++) {
            CloudLayer layer = paradiseLost$LAYERS[i];
            float scale = layer.scale();
            Vec3 camera = new Vec3(
                    (camX + scroll * layer.speed()) / scale - scroll,
                    height - (height - camY + layer.offset()) / scale,
                    camZ / scale
                );
            this.paradiseLost$renderWithCache(this.paradiseLost$caches[i], poseStack, new Matrix4f(frustumMatrix).scale(scale), projectionMatrix, partialTick, camera);
        }
    }

    @Unique
    private void paradiseLost$forwardRebuildToLayers() {
        if (!this.generateClouds) {
            return;
        }
        this.generateClouds = false;
        for (CloudMeshCache cache : this.paradiseLost$caches) {
            cache.dirty = true;
        }
    }

    @Unique
    private void paradiseLost$renderWithCache(CloudMeshCache cache, PoseStack poseStack, Matrix4f frustumMatrix, Matrix4f projectionMatrix, float partialTick, Vec3 camera) {
        this.paradiseLost$swap(cache);
        this.paradiseLost$renderingLayers = true;
        try {
            this.renderClouds(poseStack, frustumMatrix, projectionMatrix, partialTick, camera.x, camera.y, camera.z);
        } finally {
            this.paradiseLost$renderingLayers = false;
            this.paradiseLost$swap(cache);
        }
    }

    @Unique
    private void paradiseLost$swap(CloudMeshCache cache) {
        VertexBuffer buffer = this.cloudBuffer;
        this.cloudBuffer = cache.buffer;
        cache.buffer = buffer;
        int x = this.prevCloudX;
        this.prevCloudX = cache.x;
        cache.x = x;
        int y = this.prevCloudY;
        this.prevCloudY = cache.y;
        cache.y = y;
        int z = this.prevCloudZ;
        this.prevCloudZ = cache.z;
        cache.z = z;
        Vec3 color = this.prevCloudColor;
        this.prevCloudColor = cache.color;
        cache.color = color;
        CloudStatus type = this.prevCloudsType;
        this.prevCloudsType = cache.type;
        cache.type = type;
        boolean dirty = this.generateClouds;
        this.generateClouds = cache.dirty;
        cache.dirty = dirty;
    }
}
