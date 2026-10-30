package net.id.paradise_lost.test.server;

import net.id.paradise_lost.entity.passive.PopomEntity;
import net.id.paradise_lost.loot.ParadiseLostLootTables;
import net.id.paradise_lost.registry.EntityRegistry;
import net.id.paradise_lost.test.Step;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;

import static net.id.paradise_lost.test.TestHelpers.check;
import static net.id.paradise_lost.test.server.ServerTestHelpers.level;

public final class EntityTests {
    private EntityTests() {
    }

    @ServerTest(value = "keeps its fur loot table after save and load", group = "Popom")
    private static Step popomLootAfterLoad() {
        return Step.run(0, () -> {
            ServerLevel world = level();
            PopomEntity popom = EntityRegistry.POPOM.get().create(world);
            check(popom.getLootTable().equals(ParadiseLostLootTables.POPOM_JELLY_LEVEL_0), "new popom loot table is " + popom.getLootTable());
            popom.setFurSize(3);

            CompoundTag nbt = popom.saveWithoutId(new CompoundTag());
            PopomEntity loaded = EntityRegistry.POPOM.get().create(world);
            loaded.load(nbt);
            check(loaded.getLootTable().equals(ParadiseLostLootTables.POPOM_JELLY_LEVEL_3), "loaded popom loot table is " + loaded.getLootTable());
        });
    }
}
