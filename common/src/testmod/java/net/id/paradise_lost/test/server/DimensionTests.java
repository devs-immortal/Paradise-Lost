package net.id.paradise_lost.test.server;

import net.id.paradise_lost.test.Step;
import net.id.paradise_lost.world.dimension.ParadiseLostDimension;

import static net.id.paradise_lost.test.TestHelpers.check;

public final class DimensionTests {
    private DimensionTests() {
    }

    @ServerTest("the Paradise Lost level is loaded")
    private static Step levelLoaded() {
        return Step.run(0, () -> check(ServerTestRunner.server().getLevel(ParadiseLostDimension.PARADISE_LOST_WORLD_KEY) != null,
                "level " + ParadiseLostDimension.PARADISE_LOST_WORLD_KEY.location() + " is missing"));
    }
}
