package net.id.paradise_lost.test.server;

import net.id.paradise_lost.entity.passive.moa.MoaEntity;
import net.id.paradise_lost.entity.passive.moa.MoaGenes;
import net.id.paradise_lost.registry.EntityRegistry;
import net.id.paradise_lost.registry.MoaRaceRegistry;
import net.id.paradise_lost.test.Step;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static net.id.paradise_lost.test.TestHelpers.*;
import static net.id.paradise_lost.test.server.ServerTestHelpers.ahead;
import static net.id.paradise_lost.test.server.ServerTestHelpers.level;

public final class MoaTests {
    private MoaTests() {
    }

    public static ItemStack goldenrodEgg(ServerLevel world, boolean baby) {
        ResourceLocation raceId = MoaRaceRegistry.GOLDENROD.location();
        return MoaGenes.getEggForCommand(MoaRaceRegistry.getOrFallback(world.registryAccess(), raceId), raceId, world, baby);
    }

    @ServerTest("inventory keeps slots after save and load")
    private static Step moaInventorySaveLoad() {
        return Step.run(0, () -> {
            ServerLevel world = level();
            MoaEntity moa = MoaGenes.getMoaFromEgg(world, goldenrodEgg(world, false), UUID.randomUUID(), ahead(3, 0));
            moa.setChest(new ItemStack(Items.CHEST));
            Map<Integer, Item> placed = Map.of(2, Items.DIRT, 7, Items.STONE, 19, Items.DIAMOND);
            placed.forEach((slot, item) -> moa.getInventory().setItem(slot, new ItemStack(item)));

            CompoundTag nbt = moa.saveWithoutId(new CompoundTag());
            MoaEntity loaded = EntityRegistry.MOA.get().create(world);
            loaded.getGenes();
            loaded.load(nbt);

            List<String> wrong = new ArrayList<>();
            for (int slot = 0; slot < moa.getInventory().getContainerSize(); slot++) {
                Item expected = placed.getOrDefault(slot, Items.AIR);
                Item actual = loaded.getInventory().getItem(slot).getItem();
                if (expected != actual) {
                    wrong.add("slot " + slot + " has " + BuiltInRegistries.ITEM.getKey(actual) + " expected " + BuiltInRegistries.ITEM.getKey(expected));
                }
            }
            checkAll(wrong);
        });
    }

    @ServerTest("2.4 CCA moa genetics migrate into moaGenes")
    private static Step moaGenesCcaMigrates() {
        return Step.run(0, () -> {
            ServerLevel world = level();
            MoaEntity source = MoaGenes.getMoaFromEgg(world, goldenrodEgg(world, false), UUID.randomUUID(), ahead(3, 0));
            source.getGenes().setHunger(73.5F);
            CompoundTag genes = new CompoundTag();
            source.getGenes().writeToNbt(genes, world.registryAccess());

            CompoundTag nbt = new CompoundTag();
            CompoundTag cca = new CompoundTag();
            cca.put("paradise_lost:moa_genetics", genes);
            nbt.put("cardinal_components", cca);

            MoaEntity loaded = EntityRegistry.MOA.get().create(world);
            loaded.load(nbt);

            List<String> wrong = new ArrayList<>();
            if (Math.abs(loaded.getGenes().getHunger() - 73.5F) > 0.01F) {
                wrong.add("hunger is " + loaded.getGenes().getHunger() + " expected 73.5");
            }
            if (!loaded.getGenes().getRaceId().equals(MoaRaceRegistry.GOLDENROD.location())) {
                wrong.add("race is " + loaded.getGenes().getRaceId());
            }
            CompoundTag resaved = loaded.saveWithoutId(new CompoundTag());
            if (!resaved.contains("moaGenes")) {
                wrong.add("re-save missing moaGenes");
            }
            if (resaved.contains("cardinal_components")) {
                wrong.add("re-save still has CCA genetics");
            }
            checkAll(wrong);
        });
    }
}
