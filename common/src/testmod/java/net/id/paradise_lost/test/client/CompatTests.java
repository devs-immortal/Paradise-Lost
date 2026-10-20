package net.id.paradise_lost.test.client;

import net.id.paradise_lost.test.Step;
import net.id.paradise_lost.test.Test;
import net.id.paradise_lost.test.TestFactory;
import net.id.paradise_lost.platform.Services;
import net.id.paradise_lost.world.dimension.ParadiseLostDimension;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.status.ChunkStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static net.id.paradise_lost.test.TestHelpers.check;
import static net.id.paradise_lost.test.client.ClientTestHelpers.client;
import static net.id.paradise_lost.test.client.ClientTestHelpers.onServer;

public final class CompatTests {
    private static final String GROUP = "Compat";
    private static final int GENERATION_RADIUS = 3;
    private static final int GENERATION_CENTER = 12;

    private static List<CompletableFuture<?>> generation;
    private static CompletableFuture<Boolean> save;

    private CompatTests() {
    }

    @TestFactory
    private static List<Test> wilderWild() {
        if (!Services.PLATFORM.isModLoaded("wilderwild")) {
            return List.of();
        }
        return List.of(
                new Test(GROUP, "Wilder Wild and FrozenLib are loaded", Step.run(0, () -> {
                    check(Services.PLATFORM.isModLoaded("frozenlib"), "FrozenLib is missing");
                })),
                new Test(GROUP, "Wilder Wild: Paradise Lost chunks finish generating", generateChunks()),
                new Test(GROUP, "Wilder Wild: portal leads to Paradise Lost and back", DimensionTests.portalTravelSteps()),
                new Test(GROUP, "Wilder Wild: the world saves after visiting Paradise Lost", saveWorld())
        );
    }

    private static List<Step> generateChunks() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerLevel paradise = server.getLevel(ParadiseLostDimension.PARADISE_LOST_WORLD_KEY);
                    check(paradise != null, "Paradise Lost is not loaded");
                    generation = new ArrayList<>();
                    for (int x = -GENERATION_RADIUS; x <= GENERATION_RADIUS; x++) {
                        for (int z = -GENERATION_RADIUS; z <= GENERATION_RADIUS; z++) {
                            generation.add(paradise.getChunkSource().getChunkFuture(GENERATION_CENTER + x, GENERATION_CENTER + z, ChunkStatus.FULL, true));
                        }
                    }
                })),
                Step.until("chunks to generate", 20 * 120, () -> generation.stream().allMatch(CompletableFuture::isDone)),
                Step.run(0, () -> check(generation.stream().noneMatch(CompletableFuture::isCompletedExceptionally), "a chunk failed to generate"))
        );
    }

    private static List<Step> saveWorld() {
        return List.of(
                Step.run(0, () -> {
                    var server = client().getSingleplayerServer();
                    check(server != null, "integrated server is gone");
                    save = server.submit(() -> server.saveEverything(true, true, true));
                }),
                Step.until("world to save", 20 * 60, () -> save.isDone()),
                Step.run(0, () -> check(save.join(), "saving the world failed"))
        );
    }
}
