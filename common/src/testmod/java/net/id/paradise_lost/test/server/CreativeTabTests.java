package net.id.paradise_lost.test.server;

import net.id.paradise_lost.test.Step;
import net.id.paradise_lost.registry.BlockRegistry.WoodBlockSet;
import net.id.paradise_lost.registry.CreativeTabRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SaplingBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static net.id.paradise_lost.test.TestHelpers.*;
import static net.id.paradise_lost.registry.BlockRegistry.*;

public final class CreativeTabTests {
    private static final String MOD = "paradise_lost";
    private static final Set<String> NOT_IN_TABS = Set.of("moa_egg");
    private static final List<WoodBlockSet> WOODS = List.of(AUREL_WOODSTUFF, MOTHER_AUREL_WOODSTUFF, MENTH_WOODSTUFF, WISTERIA_WOODSTUFF);

    private CreativeTabTests() {
    }

    private static Set<Item> items(CreativeModeTab tab) {
        MinecraftServer server = ServerTestRunner.server();
        CreativeModeTabs.tryRebuildTabContents(server.getWorldData().enabledFeatures(), true, server.registryAccess());
        return tab.getDisplayItems().stream().map(ItemStack::getItem).collect(Collectors.toSet());
    }

    private static boolean isPlant(Block block) {
        return block instanceof SaplingBlock || block instanceof LeavesBlock || block instanceof FlowerPotBlock;
    }

    @ServerTest("building blocks tab has every wood set block")
    private static Step woodBuildingBlocks() {
        return Step.run(0, () -> {
            Set<Item> tab = items(CreativeTabRegistry.PARADISE_BLOCKS_TAB.get());
            List<String> wrong = new ArrayList<>();
            for (WoodBlockSet wood : WOODS) {
                for (Block block : wood) {
                    if (!isPlant(block))
                        expect(wrong, tab.contains(block.asItem()), BuiltInRegistries.BLOCK.getKey(block).toString());
                }
            }
            for (Block block : List.of(MOTTLED_AUREL_LOG.get(), MOTTLED_AUREL_WOOD.get(), MOTTLED_AUREL_FALLEN_LOG.get())) {
                expect(wrong, tab.contains(block.asItem()), BuiltInRegistries.BLOCK.getKey(block).toString());
            }
            check(wrong.isEmpty(), "not in the building blocks tab: " + wrong);
        });
    }

    @ServerTest("plants tab has every wood set sapling and leaves")
    private static Step woodPlants() {
        return Step.run(0, () -> {
            Set<Item> tab = items(CreativeTabRegistry.PARADISE_PLANTS_TAB.get());
            List<String> wrong = new ArrayList<>();
            for (WoodBlockSet wood : WOODS) {
                for (Block block : wood) {
                    if (block instanceof SaplingBlock || block instanceof LeavesBlock)
                        expect(wrong, tab.contains(block.asItem()), BuiltInRegistries.BLOCK.getKey(block).toString());
                }
            }
            check(wrong.isEmpty(), "not in the plants tab: " + wrong);
        });
    }

    @ServerTest("every item is in a creative tab")
    private static Step everyItem() {
        return Step.run(0, () -> {
            Set<Item> listed = CreativeModeTabs.allTabs().stream()
                    .filter(tab -> tab.getType() == CreativeModeTab.Type.CATEGORY)
                    .flatMap(tab -> items(tab).stream())
                    .collect(Collectors.toSet());
            List<String> wrong = new ArrayList<>();
            for (Item item : BuiltInRegistries.ITEM) {
                var key = BuiltInRegistries.ITEM.getKey(item);
                if (key.getNamespace().equals(MOD) && !NOT_IN_TABS.contains(key.getPath())) expect(wrong, listed.contains(item), key.toString());
            }
            check(wrong.isEmpty(), "not in any creative tab: " + wrong);
        });
    }
}
