package net.id.paradiselost.blocks;

import net.fabricmc.fabric.mixin.lookup.BlockEntityTypeAccessor;
import net.id.incubus_core.util.RegistryQueue;
import net.id.paradiselost.blocks.natural.ParadiseLostSaplingBlock;
import net.id.paradiselost.blocks.natural.tree.FruitingLeavesBlock;
import net.id.paradiselost.blocks.natural.tree.ParadiseLostLeavesBlock;
import net.id.paradiselost.items.ParadiseLostItems;
import net.id.paradiselost.mixin.util.SignTypeAccessor;
import net.id.paradiselost.registry.ParadiseLostRegistryQueues;
import net.id.paradiselost.util.ParadiseLostSignType;
import net.id.paradiselost.world.feature.tree.generator.MotherAurelSaplingGenerator;
import net.id.paradiselost.world.feature.tree.generator.OrangeSaplingGenerator;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.SignType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;

import static net.id.paradiselost.ParadiseLost.MOD_ID;
import static net.id.paradiselost.ParadiseLost.locate;
import static net.id.paradiselost.blocks.ParadiseLostBlockActions.cutoutMippedRenderLayer;
import static net.id.paradiselost.blocks.ParadiseLostBlockActions.cutoutRenderLayer;
import static net.id.paradiselost.blocks.ParadiseLostBlockActions.flammableLeaves;
import static net.id.paradiselost.blocks.ParadiseLostBlockActions.flammableLog;
import static net.id.paradiselost.blocks.ParadiseLostBlockActions.flammablePlanks;
import static net.id.paradiselost.blocks.ParadiseLostBlockActions.stripsTo;

public class BlockRegistration {

    @SafeVarargs
    public static <V extends Block> V add(String id, V block, RegistryQueue.Action<? super V>... additionalActions) {
        return ParadiseLostRegistryQueues.BLOCK.add(locate(id), block, additionalActions);
    }

    static class ParadiseLostFarmlandBlock extends FarmlandBlock {
        ParadiseLostFarmlandBlock(Settings settings) {
            super(settings);
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

    public static WoodBlockSet registerWoodBlockSet(String id, SaplingGenerator saplingGenerator, MapColor woodColor, MapColor barkColor, MapColor leafColor) {
        return registerWoodBlockSet(
                id+"_sapling", "potted_"+id+"_sapling",
                id+"_log", id+"_wood", "stripped_"+id+"_log", "stripped_"+id+"_wood",
                id+"_leaves",
                id+"_planks", id+"_stairs",id+"_slab",
                id+"_fence",id+"_fence_gate",
                id+"_door",id+"_trapdoor",
                id+"_button",id+"_pressure_plate",
                saplingGenerator, woodColor, barkColor, leafColor
        );
    }

    public static WoodBlockSet registerWoodBlockSetMotherAurel() {
        String id = "mother_aurel";
        var saplingSettings = AbstractBlock.Settings.copy(Blocks.OAK_SAPLING).mapColor(MapColor.GOLD).luminance(state -> 3);
        var flowerPotSettings = AbstractBlock.Settings.copy(Blocks.POTTED_OAK_SAPLING).luminance(state -> 3);
        var leavesSettings = AbstractBlock.Settings.copy(Blocks.OAK_LEAVES).mapColor(MapColor.GOLD).luminance(state -> 5);
        SaplingBlock sapling = add(id+"_sapling", new ParadiseLostSaplingBlock(new MotherAurelSaplingGenerator(), saplingSettings));
        return registerWoodBlockSet(
                sapling,
                add("potted_"+id+"_sapling", new FlowerPotBlock(sapling, flowerPotSettings)),
                id+"_log", id+"_wood", "stripped_"+id+"_log", "stripped_"+id+"_wood",
                add(id+"_leaves", new ParadiseLostLeavesBlock(leavesSettings, true), flammableLeaves, cutoutMippedRenderLayer),
                id+"_planks", id+"_stairs",id+"_slab",
                id+"_fence",id+"_fence_gate",
                id+"_door",id+"_trapdoor",
                id+"_button",id+"_pressure_plate",
                MapColor.GOLD, MapColor.TERRACOTTA_RED
        );
    }

    public static WoodBlockSet registerWoodBlockSetOrange() {
        String id = "orange";
        var leavesSettings = AbstractBlock.Settings.copy(Blocks.OAK_LEAVES).mapColor(MapColor.PALE_GREEN).sounds(BlockSoundGroup.AZALEA_LEAVES);
        FruitingLeavesBlock leaves = add(id+"_leaves", new FruitingLeavesBlock(leavesSettings, () -> ParadiseLostItems.ORANGE), flammableLeaves, cutoutMippedRenderLayer);
        return registerWoodBlockSet(
                id+"_sapling", "potted_"+id+"_sapling",
                id+"_log", id+"_wood", "stripped_"+id+"_log", "stripped_"+id+"_wood",
                leaves,
                id+"_planks", id+"_stairs",id+"_slab",
                id+"_fence",id+"_fence_gate",
                id+"_door",id+"_trapdoor",
                id+"_button",id+"_pressure_plate",
                new OrangeSaplingGenerator(), MapColor.TERRACOTTA_LIGHT_GRAY, MapColor.RAW_IRON_PINK
        );
    }

    public static WoodBlockSet registerWoodBlockSetWisteria() {
        String id = "wisteria";
        return registerWoodBlockSet(
                null, null,
                id+"_log", id+"_wood", "stripped_"+id+"_log", "stripped_"+id+"_wood",
                null,
                id+"_planks", id+"_stairs",id+"_slab",
                id+"_fence",id+"_fence_gate",
                id+"_door",id+"_trapdoor",
                id+"_button",id+"_pressure_plate",
                MapColor.PALE_YELLOW, MapColor.BROWN
        );
    }

    private static WoodBlockSet registerWoodBlockSet(
            String saplingId, String flowerPotId,
            String logId, String woodId, String strippedLogId, String strippedWoodId,
            String leavesId,
            String plankId, String plankStairsId, String plankSlabId,
            String fenceId, String fenceGateId,
            String doorId, String trapdoorId,
            String buttonId, String pressurePlateId,
            SaplingGenerator saplingGenerator, MapColor woodColor, MapColor barkColor, MapColor leafColor
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


        SaplingBlock sapling = add(saplingId, new SaplingBlock(saplingGenerator, saplingSettings), cutoutRenderLayer);
        PillarBlock strippedLog = add(strippedLogId, new PillarBlock(logSettings), flammableLog);
        PillarBlock strippedWood = add(strippedWoodId, new PillarBlock(logSettings), flammableLog);
        Block planks = add(plankId, new Block(plankSettings), flammablePlanks);
        return new WoodBlockSet(
                sapling, add(flowerPotId, new FlowerPotBlock(sapling, flowerPotSettings)),
                add(logId, new PillarBlock(logSettings), flammableLog, stripsTo(strippedLog)), add(woodId, new PillarBlock(logSettings), flammableLog, stripsTo(strippedWood)), strippedLog, strippedWood,
                add(leavesId, new LeavesBlock(leavesSettings), flammableLeaves, cutoutMippedRenderLayer),
                planks, add(plankStairsId, new ParadiseLostStairsBlock(planks.getDefaultState(), plankSettings), flammablePlanks), add(plankSlabId, new SlabBlock(plankSettings), flammablePlanks),
                add(fenceId, new FenceBlock(plankSettings), flammablePlanks), add(fenceGateId, new FenceGateBlock(plankSettings), flammablePlanks),
                add(doorId, new DoorBlock(doorSettings)), add(trapdoorId, new TrapdoorBlock(trapdoorSettings)),
                add(buttonId, new WoodenButtonBlock(buttonSettings)), add(pressurePlateId, new PressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, pressurePlateSettings))
        );
    }

    private static WoodBlockSet registerWoodBlockSet(
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
                add(fenceId, new FenceBlock(plankSettings), flammablePlanks), add(fenceGateId, new FenceGateBlock(plankSettings), flammablePlanks),
                add(doorId, new DoorBlock(doorSettings)), add(trapdoorId, new TrapdoorBlock(trapdoorSettings)),
                add(buttonId, new WoodenButtonBlock(buttonSettings)), add(pressurePlateId, new PressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, pressurePlateSettings))
        );
    }

    private static WoodBlockSet registerWoodBlockSet(
            String saplingId, String flowerPotId,
            String logId, String woodId, String strippedLogId, String strippedWoodId,
            LeavesBlock leaves,
            String plankId, String plankStairsId, String plankSlabId,
            String fenceId, String fenceGateId,
            String doorId, String trapdoorId,
            String buttonId, String pressurePlateId,
            SaplingGenerator saplingGenerator, MapColor woodColor, MapColor barkColor
    ) {
        var saplingSettings = AbstractBlock.Settings.copy(Blocks.OAK_SAPLING).mapColor(woodColor);
        var flowerPotSettings = AbstractBlock.Settings.copy(Blocks.POTTED_OAK_SAPLING);
        var logSettings = AbstractBlock.Settings.copy(Blocks.OAK_LOG).mapColor(barkColor);
        var plankSettings = AbstractBlock.Settings.copy(Blocks.OAK_PLANKS).mapColor(woodColor);
        var doorSettings = AbstractBlock.Settings.copy(Blocks.OAK_DOOR).mapColor(woodColor);
        var trapdoorSettings = AbstractBlock.Settings.copy(Blocks.OAK_TRAPDOOR).mapColor(woodColor);
        var buttonSettings = AbstractBlock.Settings.copy(Blocks.OAK_BUTTON).mapColor(woodColor);
        var pressurePlateSettings = AbstractBlock.Settings.copy(Blocks.OAK_PRESSURE_PLATE).mapColor(woodColor);

        SaplingBlock sapling = add(saplingId, new SaplingBlock(saplingGenerator, saplingSettings), cutoutRenderLayer);
        PillarBlock strippedLog = add(strippedLogId, new PillarBlock(logSettings), flammableLog);
        PillarBlock strippedWood = add(strippedWoodId, new PillarBlock(logSettings), flammableLog);
        Block planks = add(plankId, new Block(plankSettings), flammablePlanks);
        return new WoodBlockSet(
                sapling, add(flowerPotId, new FlowerPotBlock(sapling, flowerPotSettings)),
                add(logId, new PillarBlock(logSettings), flammableLog, stripsTo(strippedLog)), add(woodId, new PillarBlock(logSettings), flammableLog, stripsTo(strippedWood)), strippedLog, strippedWood,
                leaves,
                planks, add(plankStairsId, new ParadiseLostStairsBlock(planks.getDefaultState(), plankSettings), flammablePlanks), add(plankSlabId, new SlabBlock(plankSettings), flammablePlanks),
                add(fenceId, new FenceBlock(plankSettings), flammablePlanks), add(fenceGateId, new FenceGateBlock(plankSettings), flammablePlanks),
                add(doorId, new DoorBlock(doorSettings)), add(trapdoorId, new TrapdoorBlock(trapdoorSettings)),
                add(buttonId, new WoodenButtonBlock(buttonSettings)), add(pressurePlateId, new PressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, pressurePlateSettings))
        );
    }

    public record WoodBlockSet(
            SaplingBlock sapling, FlowerPotBlock flowerPot,
            PillarBlock log, PillarBlock wood, PillarBlock strippedLog, PillarBlock strippedWood,
            LeavesBlock leaves,
            Block plank, StairsBlock plankStairs, SlabBlock plankSlab,
            FenceBlock fence, FenceGateBlock fenceGate,
            DoorBlock door, TrapdoorBlock trapdoor,
            WoodenButtonBlock button, PressurePlateBlock pressurePlate
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

    // SIGN SET

    public static SignSet registerSignSet(String woodId) {
        SignType signType = SignTypeAccessor.callRegister(new ParadiseLostSignType(MOD_ID + "_" + woodId));

        var signSettings = AbstractBlock.Settings.copy(Blocks.OAK_SIGN);

        SignBlock signBlock = new SignBlock(signSettings, signType);
        WallSignBlock wallSignBlock = new WallSignBlock(signSettings.dropsLike(signBlock), signType);

        ((BlockEntityTypeAccessor) BlockEntityType.SIGN).getBlocks().add(signBlock);
        ((BlockEntityTypeAccessor) BlockEntityType.SIGN).getBlocks().add(wallSignBlock);

        add(woodId + "_sign", signBlock);
        add(woodId + "_wall_sign", wallSignBlock);

        return new SignSet(signType, signBlock, wallSignBlock);
    }

    public record SignSet(
            SignType type,
            SignBlock sign,
            WallSignBlock wallSign
    ) implements Iterable<Block> {
        public @NotNull Iterator<Block> iterator() {
            return Arrays.stream(new Block[]{sign, wallSign}).iterator();
        }
    }
}