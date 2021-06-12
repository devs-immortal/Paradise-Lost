package com.aether.mixin.server;

import com.aether.callback.ServerChunkManagerCallback;
import com.mojang.datafixers.DataFixer;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.entity.ChunkStatusUpdateListener;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.Executor;
import java.util.function.Supplier;

@Mixin(ServerChunkCache.class)
public class ServerChunkManagerMixin {
    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(ServerLevel world, LevelStorageSource.LevelStorageAccess session, DataFixer dataFixer, StructureManager structureManager, Executor workerExecutor, ChunkGenerator chunkGenerator, int viewDistance, boolean bl, ChunkProgressListener worldGenerationProgressListener, ChunkStatusUpdateListener chunkStatusChangeListener, Supplier<DimensionDataStorage> supplier, CallbackInfo ci) {
        ServerChunkManagerCallback.EVENT.invoker().handle((ServerChunkCache) (Object) this);
    }
}