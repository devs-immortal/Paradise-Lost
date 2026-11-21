package net.id.paradise_lost.mixin.client.render;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
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
    private final CloudMeshCache[] paradiseLost$caches = CloudMeshCache.createAll(paradiseLost$LAYERS.length);
    @Unique
    private final Matrix4f paradiseLost$scratch = new Matrix4f();

    @WrapMethod(method = "renderClouds")
    private void paradiseLost$renderLayers(PoseStack poseStack, Matrix4f frustum, Matrix4f projection,
                                           float partialTick, double camX, double camY, double camZ,
                                           Operation<Void> original) {
        if (level.dimension() != ParadiseLostDimension.PARADISE_LOST_WORLD_KEY) {
            original.call(poseStack, frustum, projection, partialTick, camX, camY, camZ);
            return;
        }
        if (generateClouds) {
            generateClouds = false;
            for (CloudMeshCache cache : paradiseLost$caches) {
                cache.dirty = true;
            }
        }

        float height = level.effects().getCloudHeight();
        double scroll = (ticks + partialTick) * 0.03;
        for (int i = 0; i < paradiseLost$LAYERS.length; i++) {
            CloudLayer layer = paradiseLost$LAYERS[i];
            float scale = layer.scale();
            CloudMeshCache cache = paradiseLost$caches[i];
            paradiseLost$swap(cache);
            try {
                original.call(poseStack,
                        frustum.scale(scale, paradiseLost$scratch),
                        projection, partialTick,
                        (camX + scroll * layer.speed()) / scale - scroll,
                        height - (height - camY + layer.offset()) / scale,
                        camZ / scale);
            } finally {
                paradiseLost$swap(cache);
            }
        }
    }

    @Inject(method = "close", at = @At("HEAD"))
    private void paradiseLost$closeCaches(CallbackInfo ci) {
        for (CloudMeshCache cache : paradiseLost$caches) {
            cache.close();
        }
    }

    @Unique
    private void paradiseLost$swap(CloudMeshCache cache) {
        VertexBuffer buffer = cloudBuffer;
        cloudBuffer = cache.buffer;
        cache.buffer = buffer;
        int x = prevCloudX;
        prevCloudX = cache.x;
        cache.x = x;
        int y = prevCloudY;
        prevCloudY = cache.y;
        cache.y = y;
        int z = prevCloudZ;
        prevCloudZ = cache.z;
        cache.z = z;
        Vec3 color = prevCloudColor;
        prevCloudColor = cache.color;
        cache.color = color;
        CloudStatus type = prevCloudsType;
        prevCloudsType = cache.type;
        cache.type = type;
        boolean dirty = generateClouds;
        generateClouds = cache.dirty;
        cache.dirty = dirty;
    }
}