package net.id.paradiselost.blocks;

import net.id.paradiselost.blocks.decorative.ParadiseHangingSignBlock;
import net.id.paradiselost.blocks.decorative.ParadiseSignBlock;
import net.id.paradiselost.blocks.decorative.ParadiseWallHangingSignBlock;
import net.id.paradiselost.blocks.decorative.ParadiseWallSignBlock;
import net.id.paradiselost.blocks.mechanical.ParadiseLostButtonBlock;
import net.id.paradiselost.blocks.mechanical.ParadiseLostDoorBlock;
import net.id.paradiselost.blocks.mechanical.ParadiseLostPressurePlateBlock;
import net.id.paradiselost.blocks.mechanical.ParadiseLostTrapdoorBlock;
import net.id.paradiselost.blocks.natural.ParadiseLostMultiSaplingBlock;
import net.id.paradiselost.blocks.natural.ParadiseLostSaplingBlock;
import net.id.paradiselost.blocks.natural.tree.ParadiseLostLeavesBlock;
import net.id.paradiselost.world.feature.tree.ParadiseLostSaplingGenerators;
import net.minecraft.block.*;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static net.id.paradiselost.ParadiseLost.locate;
import static net.id.paradiselost.blocks.ParadiseLostBlockActions.*;

public class BlockRegistration {

    @SafeVarargs
    public static <V extends Block> V add(String id, Function<AbstractBlock.Settings, V> factory, AbstractBlock.Settings settings, Consumer<Block>... additionalActions) {
        RegistryKey<Block> key = RegistryKey.of(RegistryKeys.BLOCK, locate(id));
        var registeredBlock = Registry.register(Registries.BLOCK, key, factory.apply(settings.registryKey(key)));
        for (var action : additionalActions) {
            action.accept(registeredBlock);
        }
        return registeredBlock;
    }

    static class ParadiseLostFarmlandBlock extends FarmlandBlock {
        ParadiseLostFarmlandBlock(Settings settings) {
            super(settings);
        }

        public BlockState getPlacementState(ItemPlacementContext ctx) {
            return !this.getDefaultState().canPlaceAt(ctx.getWorld(), ctx.getBlockPos()) ? ParadiseLostBlocks.DIRT.getDefaultState() : super.getPlacementState(ctx);
        }
    }

    static class ParadiseLostPaneBlock extends PaneBlock {
        ParadiseLostPaneBlock(Settings settings) {
            super(settings);
        }
    }

    static class ParadiseLostStairsBlock extends StairsBlock {
        ParadiseLostStairsBlock(BlockState baseBlockState, Settings settings) {
            super(baseBlockState, settings);
        }
    }


    // WOOD BLOCK SET

    public static WoodBlockSet registerWoodBlockSet(WoodType woodType, BlockSetType blockSetType, net.minecraft.block.SaplingGenerator saplingGenerator, MapColor woodColor, MapColor barkColor, MapColor leafColor) {
        var id = woodType.name().split(":")[1];
        return registerWoodBlockSet(
                woodType, blockSetType,
                id + "_sapling", "potted_" + id + "_sapling",
                id + "_log", id + "_wood", "stripped_" + id + "_log", "stripped_" + id + "_wood",
                id + "_leaves",
                id + "_planks", id + "_stairs", id + "_slab",
                id + "_fence", id + "_fence_gate",
                id + "_door", id + "_trapdoor",
                id + "_button", id + "_pressure_plate",
                saplingGenerator, woodColor, barkColor, leafColor
        );
    }

    public static WoodBlockSet registerWoodBlockSetMotherAurel() {
        String id = "mother_aurel";
        var saplingSettings = AbstractBlock.Settings.copy(Blocks.OAK_SAPLING).mapColor(MapColor.GOLD).luminance(state -> 3);
        var flowerPotSettings = AbstractBlock.Settings.copy(Blocks.POTTED_OAK_SAPLING).luminance(state -> 3);
        var leavesSettings = AbstractBlock.Settings.copy(Blocks.OAK_LEAVES).mapColor(MapColor.GOLD).luminance(state -> 5);
        SaplingBlock sapling = add(id + "_sapling", settings -> new ParadiseLostSaplingBlock(ParadiseLostSaplingGenerators.MOTHER_AUREL, settings), saplingSettings, cutoutRenderLayer);
        return registerWoodBlockSet(
                ParadiseLostWoodTypes.MOTHER_AUREL, ParadiseLostBlockSets.MOTHER_AUREL,
                sapling,
                add("potted_" + id + "_sapling", settings -> new FlowerPotBlock(sapling, settings), flowerPotSettings, cutoutRenderLayer),
                id + "_log", id + "_wood", "stripped_" + id + "_log", "stripped_" + id + "_wood",
                add(id + "_leaves", ParadiseLostLeavesBlock::new, leavesSettings, flammableLeaves, cutoutMippedRenderLayer),
                id + "_planks", id + "_stairs", id + "_slab",
                id + "_fence", id + "_fence_gate",
                id + "_door", id + "_trapdoor",
                id + "_button", id + "_pressure_plate",
                MapColor.GOLD, MapColor.TERRACOTTA_RED
        );
    }

    public static WoodBlockSet registerWoodBlockSetWisteria() {
        String id = "wisteria";
        return registerWoodBlockSet(
                ParadiseLostWoodTypes.WISTERIA, ParadiseLostBlockSets.WISTERIA,
                null, null,
                id + "_log", id + "_wood", "stripped_" + id + "_log", "stripped_" + id + "_wood",
                null,
                id + "_planks", id + "_stairs", id + "_slab",
                id + "_fence", id + "_fence_gate",
                id + "_door", id + "_trapdoor",
                id + "_button", id + "_pressure_plate",
                MapColor.PALE_YELLOW, MapColor.BROWN
        );
    }

    private static WoodBlockSet registerWoodBlockSet(
            WoodType woodType, BlockSetType blockSetType,
            String saplingId, String flowerPotId,
            String logId, String woodId, String strippedLogId, String strippedWoodId,
            String leavesId,
            String plankId, String plankStairsId, String plankSlabId,
            String fenceId, String fenceGateId,
            String doorId, String trapdoorId,
            String buttonId, String pressurePlateId,
            net.minecraft.block.SaplingGenerator saplingGenerator, MapColor woodColor, MapColor barkColor, MapColor leafColor
    ) {
        var saplingSettings = AbstractBlock.Settings.copy(Blocks.OAK_SAPLING).mapColor(woodColor);
        var flowerPotSettings = AbstractBlock.Settings.copy(Blocks.POTTED_OAK_SAPLING);
        var logSettings = AbstractBlock.Settings.copy(Blocks.OAK_LOG).mapColor(barkColor).instrument(NoteBlockInstrument.BASS);
        var leavesSettings = AbstractBlock.Settings.copy(Blocks.OAK_LEAVES).mapColor(leafColor);
        var plankSettings = AbstractBlock.Settings.copy(Blocks.OAK_PLANKS).mapColor(woodColor).instrument(NoteBlockInstrument.BASS);
        var doorSettings = AbstractBlock.Settings.copy(Blocks.OAK_DOOR).mapColor(woodColor).instrument(NoteBlockInstrument.BASS);
        var trapdoorSettings = AbstractBlock.Settings.copy(Blocks.OAK_TRAPDOOR).mapColor(woodColor).instrument(NoteBlockInstrument.BASS);
        var buttonSettings = AbstractBlock.Settings.copy(Blocks.OAK_BUTTON).mapColor(woodColor);
        var pressurePlateSettings = AbstractBlock.Settings.copy(Blocks.OAK_PRESSURE_PLATE).mapColor(woodColor).instrument(NoteBlockInstrument.BASS);


        SaplingBlock sapling = add(saplingId, settings -> new ParadiseLostMultiSaplingBlock(saplingGenerator, settings, List.of(
                new Pair<>(ParadiseLostBlocks.MOSSY_FLOESTONE, ParadiseLostSaplingGenerators.MOTTLED_AUREL),
                new Pair<>(ParadiseLostBlocks.LIVERWORT, ParadiseLostSaplingGenerators.THICKET_AUREL)
        )), saplingSettings, cutoutRenderLayer);
        PillarBlock strippedLog = add(strippedLogId, PillarBlock::new, logSettings, flammableLog);
        PillarBlock strippedWood = add(strippedWoodId, PillarBlock::new, logSettings, flammableLog);
        Block planks = add(plankId, Block::new, plankSettings, flammablePlanks);
        return new WoodBlockSet(
                sapling, add(flowerPotId, settings -> new FlowerPotBlock(sapling, settings), flowerPotSettings, cutoutRenderLayer),
                add(logId, PillarBlock::new, logSettings, flammableLog, stripsTo(strippedLog)), add(woodId, PillarBlock::new, logSettings, flammableLog, stripsTo(strippedWood)), strippedLog, strippedWood,
                add(leavesId, LeavesBlock::new, leavesSettings, flammableLeaves, cutoutMippedRenderLayer),
                planks, add(plankStairsId, settings -> new ParadiseLostStairsBlock(planks.getDefaultState(), settings), plankSettings, flammablePlanks), add(plankSlabId, SlabBlock::new, plankSettings, flammablePlanks),
                add(fenceId, FenceBlock::new, plankSettings, flammablePlanks), add(fenceGateId, settings -> new FenceGateBlock(woodType, settings), plankSettings, flammablePlanks),
                add(doorId, settings -> new ParadiseLostDoorBlock(blockSetType, settings), doorSettings, cutoutMippedRenderLayer), add(trapdoorId, settings -> new ParadiseLostTrapdoorBlock(blockSetType, settings), trapdoorSettings, cutoutMippedRenderLayer),
                add(buttonId, settings -> new ParadiseLostButtonBlock(blockSetType, 30, settings), buttonSettings), add(pressurePlateId, settings -> new ParadiseLostPressurePlateBlock(blockSetType, settings), pressurePlateSettings)
        );
    }

    private static WoodBlockSet registerWoodBlockSet(
            WoodType woodType, BlockSetType blockSetType,
            SaplingBlock sapling, FlowerPotBlock flowerPot,
            String logId, String woodId, String strippedLogId, String strippedWoodId,
            LeavesBlock leaves,
            String plankId, String plankStairsId, String plankSlabId,
            String fenceId, String fenceGateId,
            String doorId, String trapdoorId,
            String buttonId, String pressurePlateId,
            MapColor woodColor, MapColor barkColor
    ) {
        var logSettings = AbstractBlock.Settings.copy(Blocks.OAK_LOG).mapColor(barkColor);
        var plankSettings = AbstractBlock.Settings.copy(Blocks.OAK_PLANKS).mapColor(woodColor);
        var doorSettings = AbstractBlock.Settings.copy(Blocks.OAK_DOOR).mapColor(woodColor);
        var trapdoorSettings = AbstractBlock.Settings.copy(Blocks.OAK_TRAPDOOR).mapColor(woodColor);
        var buttonSettings = AbstractBlock.Settings.copy(Blocks.OAK_BUTTON).mapColor(woodColor);
        var pressurePlateSettings = AbstractBlock.Settings.copy(Blocks.OAK_PRESSURE_PLATE).mapColor(woodColor);

        PillarBlock strippedLog = add(strippedLogId, PillarBlock::new, logSettings, flammableLog);
        PillarBlock strippedWood = add(strippedWoodId, PillarBlock::new, logSettings, flammableLog);
        Block planks = add(plankId, Block::new, plankSettings, flammablePlanks);
        return new WoodBlockSet(
                sapling, flowerPot,
                add(logId, PillarBlock::new, logSettings, flammableLog, stripsTo(strippedLog)), add(woodId, PillarBlock::new, logSettings, flammableLog, stripsTo(strippedWood)), strippedLog, strippedWood,
                leaves,
                planks, add(plankStairsId, settings -> new ParadiseLostStairsBlock(planks.getDefaultState(), settings), plankSettings, flammablePlanks), add(plankSlabId, SlabBlock::new, plankSettings, flammablePlanks),
                add(fenceId, FenceBlock::new, plankSettings, flammablePlanks), add(fenceGateId, settings -> new FenceGateBlock(woodType, settings), plankSettings, flammablePlanks),
                add(doorId, settings -> new ParadiseLostDoorBlock(blockSetType, settings), doorSettings, cutoutMippedRenderLayer), add(trapdoorId, settings -> new ParadiseLostTrapdoorBlock(blockSetType, settings), trapdoorSettings, cutoutMippedRenderLayer),
                add(buttonId, settings -> new ParadiseLostButtonBlock(blockSetType, 30, settings), buttonSettings), add(pressurePlateId, settings -> new ParadiseLostPressurePlateBlock(blockSetType, settings), pressurePlateSettings)
        );
    }

    private static WoodBlockSet registerWoodBlockSet(
            WoodType woodType, BlockSetType blockSetType,
            String saplingId, String flowerPotId,
            String logId, String woodId, String strippedLogId, String strippedWoodId,
            LeavesBlock leaves,
            String plankId, String plankStairsId, String plankSlabId,
            String fenceId, String fenceGateId,
            String doorId, String trapdoorId,
            String buttonId, String pressurePlateId,
            net.minecraft.block.SaplingGenerator saplingGenerator, MapColor woodColor, MapColor barkColor
    ) {
        var saplingSettings = AbstractBlock.Settings.copy(Blocks.OAK_SAPLING).mapColor(woodColor);
        var flowerPotSettings = AbstractBlock.Settings.copy(Blocks.POTTED_OAK_SAPLING);
        var logSettings = AbstractBlock.Settings.copy(Blocks.OAK_LOG).mapColor(barkColor);
        var plankSettings = AbstractBlock.Settings.copy(Blocks.OAK_PLANKS).mapColor(woodColor);
        var doorSettings = AbstractBlock.Settings.copy(Blocks.OAK_DOOR).mapColor(woodColor);
        var trapdoorSettings = AbstractBlock.Settings.copy(Blocks.OAK_TRAPDOOR).mapColor(woodColor);
        var buttonSettings = AbstractBlock.Settings.copy(Blocks.OAK_BUTTON).mapColor(woodColor);
        var pressurePlateSettings = AbstractBlock.Settings.copy(Blocks.OAK_PRESSURE_PLATE).mapColor(woodColor);

        SaplingBlock sapling = add(saplingId, settings -> new ParadiseLostSaplingBlock(saplingGenerator, settings), saplingSettings, cutoutRenderLayer);
        PillarBlock strippedLog = add(strippedLogId, PillarBlock::new, logSettings, flammableLog);
        PillarBlock strippedWood = add(strippedWoodId, PillarBlock::new, logSettings, flammableLog);
        Block planks = add(plankId, Block::new, plankSettings, flammablePlanks);
        return new WoodBlockSet(
                sapling, add(flowerPotId, settings -> new FlowerPotBlock(sapling, settings), flowerPotSettings, cutoutRenderLayer),
                add(logId, PillarBlock::new, logSettings, flammableLog, stripsTo(strippedLog)), add(woodId, PillarBlock::new, logSettings, flammableLog, stripsTo(strippedWood)), strippedLog, strippedWood,
                leaves,
                planks, add(plankStairsId, settings -> new ParadiseLostStairsBlock(planks.getDefaultState(), settings), plankSettings, flammablePlanks), add(plankSlabId, SlabBlock::new, plankSettings, flammablePlanks),
                add(fenceId, FenceBlock::new, plankSettings, flammablePlanks), add(fenceGateId, settings -> new FenceGateBlock(woodType, settings), plankSettings, flammablePlanks),
                add(doorId, settings -> new ParadiseLostDoorBlock(blockSetType, settings), doorSettings, cutoutMippedRenderLayer), add(trapdoorId, settings -> new ParadiseLostTrapdoorBlock(blockSetType, settings), trapdoorSettings, cutoutMippedRenderLayer),
                add(buttonId, settings -> new ParadiseLostButtonBlock(blockSetType, 30, settings), buttonSettings), add(pressurePlateId, settings -> new ParadiseLostPressurePlateBlock(blockSetType, settings), pressurePlateSettings)
        );
    }

    public record WoodBlockSet(
            SaplingBlock sapling, FlowerPotBlock flowerPot,
            PillarBlock log, PillarBlock wood, PillarBlock strippedLog, PillarBlock strippedWood,
            LeavesBlock leaves,
            Block plank, StairsBlock plankStairs, SlabBlock plankSlab,
            FenceBlock fence, FenceGateBlock fenceGate,
            DoorBlock door, TrapdoorBlock trapdoor,
            ButtonBlock button, PressurePlateBlock pressurePlate
    ) implements Iterable<Block> {
        public @NotNull Iterator<Block> iterator() {
            return Arrays.stream(new Block[]{
                    sapling, flowerPot,
                    log, wood, strippedLog, strippedWood,
                    leaves,
                    plank, plankStairs, plankSlab,
                    fence, fenceGate,
                    door, trapdoor,
                    button, pressurePlate
            }).iterator();
        }
    }

    public static SimpleBlockSet registerSimpleBlockSet(String blockId, AbstractBlock.Settings settings) {
        Block block = add(blockId, Block::new, settings);
        ParadiseLostStairsBlock stairs = add(blockId + "_stairs", s -> new ParadiseLostStairsBlock(block.getDefaultState(), s), settings);
        SlabBlock slab = add(blockId + "_slab", SlabBlock::new, settings);
        return new SimpleBlockSet(block, stairs, slab);
    }

    public record SimpleBlockSet(
            Block block, StairsBlock stairs, SlabBlock slab
    ) implements Iterable<Block> {
        public @NotNull Iterator<Block> iterator() {
            return Arrays.stream(new Block[]{
                    block, stairs, slab
            }).iterator();
        }
    }

    // SIGN SET
    public static SignSet registerSignSet(WoodType woodType) {

        var signSettings = AbstractBlock.Settings.copy(Blocks.OAK_SIGN).instrument(NoteBlockInstrument.BASS);
        var hangingSignSettings = AbstractBlock.Settings.copy(Blocks.OAK_HANGING_SIGN).instrument(NoteBlockInstrument.BASS);

        SignBlock signBlock = add(woodType.name() + "_sign", settings -> new ParadiseSignBlock(settings, woodType), signSettings);
        WallSignBlock wallSignBlock = add(woodType.name() + "_wall_sign", settings -> new ParadiseWallSignBlock(settings, woodType), signSettings.lootTable(signBlock.getLootTableKey()));
        HangingSignBlock hangingSign = add(woodType.name() + "_hanging_sign", settings -> new ParadiseHangingSignBlock(woodType, settings), hangingSignSettings);
        WallHangingSignBlock wallHangingSign = add(woodType.name() + "_wall_hanging_sign", settings -> new ParadiseWallHangingSignBlock(woodType, settings), hangingSignSettings.lootTable(hangingSign.getLootTableKey()));

        return new SignSet(signBlock, wallSignBlock, hangingSign, wallHangingSign);
    }

    public record SignSet(
            SignBlock sign,
            WallSignBlock wallSign,
            HangingSignBlock hangingSign,
            WallHangingSignBlock wallHangingSign
    ) implements Iterable<Block> {
        public @NotNull Iterator<Block> iterator() {
            return Arrays.stream(new Block[]{sign, wallSign, hangingSign, wallHangingSign}).iterator();
        }
    }
}
