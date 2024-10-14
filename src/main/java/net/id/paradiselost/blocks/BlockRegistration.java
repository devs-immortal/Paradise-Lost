package net.id.paradiselost.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.id.paradiselost.blocks.decorative.ParadiseHangingSignBlock;
import net.id.paradiselost.blocks.decorative.ParadiseSignBlock;
import net.id.paradiselost.blocks.decorative.ParadiseWallHangingSignBlock;
import net.id.paradiselost.blocks.decorative.ParadiseWallSignBlock;
import net.id.paradiselost.blocks.mechanical.ParadiseLostButtonBlock;
import net.id.paradiselost.blocks.mechanical.ParadiseLostDoorBlock;
import net.id.paradiselost.blocks.mechanical.ParadiseLostPressurePlateBlock;
import net.id.paradiselost.blocks.mechanical.ParadiseLostTrapdoorBlock;
import net.id.paradiselost.blocks.natural.ParadiseLostSaplingBlock;
import net.id.paradiselost.blocks.natural.tree.FruitingLeavesBlock;
import net.id.paradiselost.blocks.natural.tree.ParadiseLostLeavesBlock;
import net.id.paradiselost.items.ParadiseLostItems;
import net.id.paradiselost.world.feature.tree.ParadiseLostSaplingGenerators;
import net.minecraft.block.*;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Consumer;

import static net.id.paradiselost.ParadiseLost.locate;
import static net.id.paradiselost.blocks.ParadiseLostBlockActions.*;

public class BlockRegistration {

    @SafeVarargs
    public static <V extends Block> V add(String id, V block, Consumer<Block>... additionalActions) {
        var registeredBlock = Registry.register(Registries.BLOCK, locate(id), block);
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
        SaplingBlock sapling = add(id + "_sapling", new ParadiseLostSaplingBlock(ParadiseLostSaplingGenerators.MOTHER_AUREL, saplingSettings), cutoutRenderLayer);
        return registerWoodBlockSet(
                ParadiseLostWoodTypes.MOTHER_AUREL, ParadiseLostBlockSets.MOTHER_AUREL,
                sapling,
                add("potted_" + id + "_sapling", new FlowerPotBlock(sapling, flowerPotSettings), cutoutRenderLayer),
                id + "_log", id + "_wood", "stripped_" + id + "_log", "stripped_" + id + "_wood",
                add(id + "_leaves", new ParadiseLostLeavesBlock(leavesSettings), flammableLeaves, cutoutMippedRenderLayer),
                id + "_planks", id + "_stairs", id + "_slab",
                id + "_fence", id + "_fence_gate",
                id + "_door", id + "_trapdoor",
                id + "_button", id + "_pressure_plate",
                MapColor.GOLD, MapColor.TERRACOTTA_RED
        );
    }

    public static WoodBlockSet registerWoodBlockSetOrange() {
        String id = "orange";
        var leavesSettings = AbstractBlock.Settings.copy(Blocks.OAK_LEAVES).mapColor(MapColor.PALE_GREEN).sounds(BlockSoundGroup.AZALEA_LEAVES);
        FruitingLeavesBlock leaves = add(id + "_leaves", new FruitingLeavesBlock(leavesSettings, () -> ParadiseLostItems.ORANGE), flammableLeaves, cutoutMippedRenderLayer);
        return registerWoodBlockSet(
                ParadiseLostWoodTypes.ORANGE, ParadiseLostBlockSets.ORANGE,
                id + "_sapling", "potted_" + id + "_sapling",
                id + "_log", id + "_wood", "stripped_" + id + "_log", "stripped_" + id + "_wood",
                leaves,
                id + "_planks", id + "_stairs", id + "_slab",
                id + "_fence", id + "_fence_gate",
                id + "_door", id + "_trapdoor",
                id + "_button", id + "_pressure_plate",
                ParadiseLostSaplingGenerators.ORANGE, MapColor.TERRACOTTA_LIGHT_GRAY, MapColor.RAW_IRON_PINK
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
        var logSettings = AbstractBlock.Settings.copy(Blocks.OAK_LOG).mapColor(barkColor);
        var leavesSettings = AbstractBlock.Settings.copy(Blocks.OAK_LEAVES).mapColor(leafColor);
        var plankSettings = AbstractBlock.Settings.copy(Blocks.OAK_PLANKS).mapColor(woodColor);
        var doorSettings = AbstractBlock.Settings.copy(Blocks.OAK_DOOR).mapColor(woodColor);
        var trapdoorSettings = AbstractBlock.Settings.copy(Blocks.OAK_TRAPDOOR).mapColor(woodColor);
        var buttonSettings = AbstractBlock.Settings.copy(Blocks.OAK_BUTTON).mapColor(woodColor);
        var pressurePlateSettings = AbstractBlock.Settings.copy(Blocks.OAK_PRESSURE_PLATE).mapColor(woodColor);


        SaplingBlock sapling = add(saplingId, new ParadiseLostSaplingBlock(saplingGenerator, saplingSettings), cutoutRenderLayer);
        PillarBlock strippedLog = add(strippedLogId, new PillarBlock(logSettings), flammableLog);
        PillarBlock strippedWood = add(strippedWoodId, new PillarBlock(logSettings), flammableLog);
        Block planks = add(plankId, new Block(plankSettings), flammablePlanks);
        return new WoodBlockSet(
                sapling, add(flowerPotId, new FlowerPotBlock(sapling, flowerPotSettings), cutoutRenderLayer),
                add(logId, new PillarBlock(logSettings), flammableLog, stripsTo(strippedLog)), add(woodId, new PillarBlock(logSettings), flammableLog, stripsTo(strippedWood)), strippedLog, strippedWood,
                add(leavesId, new LeavesBlock(leavesSettings), flammableLeaves, cutoutMippedRenderLayer),
                planks, add(plankStairsId, new ParadiseLostStairsBlock(planks.getDefaultState(), plankSettings), flammablePlanks), add(plankSlabId, new SlabBlock(plankSettings), flammablePlanks),
                add(fenceId, new FenceBlock(plankSettings), flammablePlanks), add(fenceGateId, new FenceGateBlock(woodType, plankSettings), flammablePlanks),
                add(doorId, new ParadiseLostDoorBlock(blockSetType, doorSettings), cutoutMippedRenderLayer), add(trapdoorId, new ParadiseLostTrapdoorBlock(blockSetType, trapdoorSettings), cutoutMippedRenderLayer),
                add(buttonId, new ParadiseLostButtonBlock(blockSetType, 30, buttonSettings)), add(pressurePlateId, new ParadiseLostPressurePlateBlock(blockSetType, pressurePlateSettings))
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

        PillarBlock strippedLog = add(strippedLogId, new PillarBlock(logSettings), flammableLog);
        PillarBlock strippedWood = add(strippedWoodId, new PillarBlock(logSettings), flammableLog);
        Block planks = add(plankId, new Block(plankSettings), flammablePlanks);
        return new WoodBlockSet(
                sapling, flowerPot,
                add(logId, new PillarBlock(logSettings), flammableLog, stripsTo(strippedLog)), add(woodId, new PillarBlock(logSettings), flammableLog, stripsTo(strippedWood)), strippedLog, strippedWood,
                leaves,
                planks, add(plankStairsId, new ParadiseLostStairsBlock(planks.getDefaultState(), plankSettings), flammablePlanks), add(plankSlabId, new SlabBlock(plankSettings), flammablePlanks),
                add(fenceId, new FenceBlock(plankSettings), flammablePlanks), add(fenceGateId, new FenceGateBlock(woodType, plankSettings), flammablePlanks),
                add(doorId, new ParadiseLostDoorBlock(blockSetType, doorSettings), cutoutMippedRenderLayer), add(trapdoorId, new ParadiseLostTrapdoorBlock(blockSetType, trapdoorSettings), cutoutMippedRenderLayer),
                add(buttonId, new ParadiseLostButtonBlock(blockSetType, 30, buttonSettings)), add(pressurePlateId, new ParadiseLostPressurePlateBlock(blockSetType, pressurePlateSettings))
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

        SaplingBlock sapling = add(saplingId, new ParadiseLostSaplingBlock(saplingGenerator, saplingSettings), cutoutRenderLayer);
        PillarBlock strippedLog = add(strippedLogId, new PillarBlock(logSettings), flammableLog);
        PillarBlock strippedWood = add(strippedWoodId, new PillarBlock(logSettings), flammableLog);
        Block planks = add(plankId, new Block(plankSettings), flammablePlanks);
        return new WoodBlockSet(
                sapling, add(flowerPotId, new FlowerPotBlock(sapling, flowerPotSettings), cutoutRenderLayer),
                add(logId, new PillarBlock(logSettings), flammableLog, stripsTo(strippedLog)), add(woodId, new PillarBlock(logSettings), flammableLog, stripsTo(strippedWood)), strippedLog, strippedWood,
                leaves,
                planks, add(plankStairsId, new ParadiseLostStairsBlock(planks.getDefaultState(), plankSettings), flammablePlanks), add(plankSlabId, new SlabBlock(plankSettings), flammablePlanks),
                add(fenceId, new FenceBlock(plankSettings), flammablePlanks), add(fenceGateId, new FenceGateBlock(woodType, plankSettings), flammablePlanks),
                add(doorId, new ParadiseLostDoorBlock(blockSetType, doorSettings), cutoutMippedRenderLayer), add(trapdoorId, new ParadiseLostTrapdoorBlock(blockSetType, trapdoorSettings), cutoutMippedRenderLayer),
                add(buttonId, new ParadiseLostButtonBlock(blockSetType, 30, buttonSettings)), add(pressurePlateId, new ParadiseLostPressurePlateBlock(blockSetType, pressurePlateSettings))
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
        Block block = add(blockId, new Block(settings));
        ParadiseLostStairsBlock stairs = add(blockId + "_stairs", new ParadiseLostStairsBlock(block.getDefaultState(), settings));
        SlabBlock slab = add(blockId + "_slab", new SlabBlock(settings));
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

        var signSettings = FabricBlockSettings.copy(Blocks.OAK_SIGN);
        var hangingSignSettings = FabricBlockSettings.copy(Blocks.OAK_HANGING_SIGN);

        SignBlock signBlock = new ParadiseSignBlock(signSettings, woodType);
        WallSignBlock wallSignBlock = new ParadiseWallSignBlock(signSettings.dropsLike(signBlock), woodType);
        HangingSignBlock hangingSign = new ParadiseHangingSignBlock(hangingSignSettings, woodType);
        WallHangingSignBlock wallHangingSign = new ParadiseWallHangingSignBlock(hangingSignSettings.dropsLike(hangingSign), woodType);

        add(woodType.name() + "_sign", signBlock);
        add(woodType.name() + "_wall_sign", wallSignBlock);
        add(woodType.name() + "_hanging_sign", hangingSign);
        add(woodType.name() + "_wall_hanging_sign", wallHangingSign);

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
