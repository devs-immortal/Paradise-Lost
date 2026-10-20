package net.id.paradise_lost.test.client;

import net.id.paradise_lost.block.blockentity.CalciteDecoratedPotBlockEntity;
import net.id.paradise_lost.test.Step;
import net.id.paradise_lost.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.PotDecorations;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static net.id.paradise_lost.test.TestHelpers.*;
import static net.id.paradise_lost.test.client.ClientTestHelpers.*;
import static net.id.paradise_lost.registry.ItemRegistry.*;

public final class PotTests {

    private PotTests() {
    }

    @ClientTest("default calcite pot shatters into 5 calcite, not bricks")
    private static Step calcitePotShatters() {
        return Step.run(0, () -> onServer(server -> {
            String wrong = breakPot(player(server), 0, BlockRegistry.CALCITE_DECORATED_POT.get(), null, Items.IRON_PICKAXE, Map.of(Items.CALCITE, 5));
            check(wrong == null, wrong);
        }));
    }

    @ClientTest("pots drop items when broken")
    private static Step potsDrop() {
        return Step.run(0, () -> onServer(server -> {
            ServerPlayer player = player(server);
            Item calcitePot = BlockRegistry.CALCITE_DECORATED_POT.get().asItem();
            PotDecorations plSherds = new PotDecorations(SOL_POTTERY_SHERD.get(), COO_POTTERY_SHERD.get(), SOL_POTTERY_SHERD.get(), COO_POTTERY_SHERD.get());

            List<String> wrong = new ArrayList<>();
            wrong.add(breakPot(player, -6, Blocks.DECORATED_POT, null, null, Map.of(Items.DECORATED_POT, 1)));
            wrong.add(breakPot(player, -2, Blocks.DECORATED_POT, null, Items.IRON_PICKAXE, Map.of(Items.BRICK, 4)));
            wrong.add(breakPot(player, 2, BlockRegistry.CALCITE_DECORATED_POT.get(), plSherds, null, Map.of(calcitePot, 1)));
            wrong.add(breakPot(player, 6, BlockRegistry.CALCITE_DECORATED_POT.get(), plSherds, Items.IRON_PICKAXE, Map.of(SOL_POTTERY_SHERD.get(), 2, COO_POTTERY_SHERD.get(), 2, Items.CALCITE, 1)));
            wrong.removeIf(Objects::isNull);

            checkAll(wrong);
        }));
    }

    private static List<ItemStack> breakCalcitePot(ServerPlayer player, PotDecorations sherds, ItemStack tool) {
        ServerLevel world = player.serverLevel();
        BlockPos potPos = ahead(player, 4, 0);

        world.setBlockAndUpdate(potPos, BlockRegistry.CALCITE_DECORATED_POT.get().defaultBlockState());
        ((CalciteDecoratedPotBlockEntity) world.getBlockEntity(potPos)).readFrom(CalciteDecoratedPotBlockEntity.getStackWith(sherds));

        player.setItemInHand(InteractionHand.MAIN_HAND, tool);
        player.gameMode.destroyBlock(potPos);

        return dropStacks(world, potPos, 1.5);
    }

    @ClientTest("calcite pot with sherds keeps them when broken by hand")
    private static Step calcitePotKeepsSherds() {
        return Step.run(0, () -> onServer(server -> {
            PotDecorations sherds = new PotDecorations(SOL_POTTERY_SHERD.get(), COO_POTTERY_SHERD.get(), Items.ANGLER_POTTERY_SHERD, SOL_POTTERY_SHERD.get());
            List<ItemStack> dropped = breakCalcitePot(player(server), sherds, ItemStack.EMPTY);
            check(dropped.size() == 1 && dropped.getFirst().is(BlockRegistry.CALCITE_DECORATED_POT.get().asItem()), "dropped " + dropped);

            PotDecorations kept = dropped.getFirst().get(DataComponents.POT_DECORATIONS);
            check(sherds.equals(kept), "dropped pot has " + kept + " expected " + sherds);
        }));
    }

    @ClientTest("calcite pot with 2 sherds shatters into 2 sherds and 3 calcite")
    private static Step calcitePotHalfSherds() {
        return Step.run(0, () -> onServer(server -> {
            PotDecorations sherds = new PotDecorations(Optional.of(SOL_POTTERY_SHERD.get()), Optional.empty(), Optional.of(COO_POTTERY_SHERD.get()), Optional.empty());
            Map<Item, Integer> drops = new LinkedHashMap<>();
            breakCalcitePot(player(server), sherds, new ItemStack(Items.IRON_PICKAXE)).forEach(stack -> drops.merge(stack.getItem(), stack.getCount(), Integer::sum));

            Map<Item, Integer> expected = Map.of(SOL_POTTERY_SHERD.get(), 1, COO_POTTERY_SHERD.get(), 1, Items.CALCITE, 3);
            check(drops.equals(expected), "dropped " + describe(drops) + " expected " + describe(expected));
        }));
    }

    @ClientTest("calcite pot broken with a silk touch pickaxe drops the whole pot")
    private static Step calcitePotSilkTouch() {
        return Step.run(0, () -> onServer(server -> {
            ServerPlayer player = player(server);
            ItemStack pickaxe = new ItemStack(Items.IRON_PICKAXE);
            pickaxe.enchant(player.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SILK_TOUCH), 1);

            PotDecorations sherds = new PotDecorations(SOL_POTTERY_SHERD.get(), COO_POTTERY_SHERD.get(), SOL_POTTERY_SHERD.get(), COO_POTTERY_SHERD.get());
            List<ItemStack> dropped = breakCalcitePot(player, sherds, pickaxe);
            check(dropped.size() == 1 && dropped.getFirst().is(BlockRegistry.CALCITE_DECORATED_POT.get().asItem())
                    && sherds.equals(dropped.getFirst().get(DataComponents.POT_DECORATIONS)), "dropped " + dropped);
        }));
    }

    private static String breakPot(ServerPlayer player, int side, Block block, PotDecorations sherds, Item tool, Map<Item, Integer> expected) {
        ServerLevel world = player.serverLevel();
        BlockPos potPos = ahead(player, 4, side);
        world.setBlockAndUpdate(potPos, block.defaultBlockState());
        if (sherds != null && world.getBlockEntity(potPos) instanceof CalciteDecoratedPotBlockEntity pot) {
            pot.readFrom(CalciteDecoratedPotBlockEntity.getStackWith(sherds));
        }
        player.setItemInHand(InteractionHand.MAIN_HAND, tool == null ? ItemStack.EMPTY : new ItemStack(tool));
        player.gameMode.destroyBlock(potPos);

        Map<Item, Integer> drops = drops(world, potPos);
        if (new LinkedHashMap<>(expected).equals(drops)) return null;

        String pot = BuiltInRegistries.BLOCK.getKey(block).getPath();
        if (sherds != null) pot += " with sherds";
        String with = tool == null ? "by hand" : "with pickaxe";
        return pot + " broken " + with + " dropped " + describe(drops) + " expected " + describe(expected);
    }
}
