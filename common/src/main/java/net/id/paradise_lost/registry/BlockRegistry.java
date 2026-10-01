package net.id.paradise_lost.registry;

import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.block.ChuteBlock;
import net.id.paradise_lost.block.FloatingBlock;
import net.id.paradise_lost.block.ParadiseLostBlockSets;
import net.id.paradise_lost.block.ParadiseLostPortalBlock;
import net.id.paradise_lost.block.ParadiseLostWoodTypes;
import net.id.paradise_lost.block.decorative.*;
import net.id.paradise_lost.block.decorative.ParadiseHangingSignBlock;
import net.id.paradise_lost.block.decorative.ParadiseSignBlock;
import net.id.paradise_lost.block.decorative.ParadiseWallHangingSignBlock;
import net.id.paradise_lost.block.decorative.ParadiseWallSignBlock;
import net.id.paradise_lost.block.mechanical.*;
import net.id.paradise_lost.block.mechanical.ParadiseLostButtonBlock;
import net.id.paradise_lost.block.mechanical.ParadiseLostDoorBlock;
import net.id.paradise_lost.block.mechanical.ParadiseLostPressurePlateBlock;
import net.id.paradise_lost.block.mechanical.ParadiseLostTrapdoorBlock;
import net.id.paradise_lost.block.natural.*;
import net.id.paradise_lost.block.natural.ParadiseLostMultiSaplingBlock;
import net.id.paradise_lost.block.natural.ParadiseLostSaplingBlock;
import net.id.paradise_lost.block.natural.cloud.*;
import net.id.paradise_lost.block.natural.crop.*;
import net.id.paradise_lost.block.natural.plant.*;
import net.id.paradise_lost.block.natural.tree.*;
import net.id.paradise_lost.block.natural.tree.ParadiseLostLeavesBlock;
import net.id.paradise_lost.registration.RegistrationProvider;
import net.id.paradise_lost.registration.RegistryObject;
import net.id.paradise_lost.tag.ParadiseLostBlockTags;
import net.id.paradise_lost.world.feature.configured_features.ParadiseLostTreeConfiguredFeatures;
import net.id.paradise_lost.world.feature.tree.ParadiseLostSaplingGenerators;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BrushableBlock;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.ChainBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RedStoneOreBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import static net.id.paradise_lost.block.ParadiseLostBlockActions.*;
import static net.id.paradise_lost.item.ParadiseLostItemActions.*;
import static net.minecraft.world.level.block.Blocks.*;
import static net.minecraft.world.level.block.state.BlockBehaviour.Properties.of;
import static net.minecraft.world.level.block.state.BlockBehaviour.Properties.ofFullCopy;

@SuppressWarnings("unused")
public class BlockRegistry {
    public static final RegistrationProvider<Block> BLOCKS =
            RegistrationProvider.get(Registries.BLOCK, ModConstants.MODID);

    private static final List<Runnable> PENDING_BLOCK_ITEMS = new ArrayList<>();

    @SafeVarargs
    public static <V extends Block> RegistryObject<Block, V> add(String id, Supplier<V> block, Consumer<Block>... additionalActions) {
        return add(id, block, new Item.Properties(), List.of(), additionalActions);
    }

    @SafeVarargs
    public static <V extends Block> RegistryObject<Block, V> add(
            String id,
            Supplier<V> block,
            Item.Properties itemProps,
            Consumer<Block>... additionalActions
    ) {
        return add(id, block, itemProps, List.of(), additionalActions);
    }

    @SafeVarargs
    public static <V extends Block> RegistryObject<Block, V> add(
            String id,
            Supplier<V> block,
            List<Consumer<ItemLike>> itemActions,
            Consumer<Block>... additionalActions
    ) {
        return add(id, block, new Item.Properties(), itemActions, additionalActions);
    }

    @SafeVarargs
    public static <V extends Block> RegistryObject<Block, V> add(
            String id,
            Supplier<V> block,
            Item.Properties itemProps,
            List<Consumer<ItemLike>> itemActions,
            Consumer<Block>... additionalActions
    ) {
        RegistryObject<Block, V> reg = BLOCKS.register(id, () -> {
            V instance = block.get();
            for (var action : additionalActions) {
                action.accept(instance);
            }
            return instance;
        });
        PENDING_BLOCK_ITEMS.add(() -> ItemRegistry.ITEMS.register(id, () -> {
            Block b = reg.get();
            BlockItem item = (b instanceof DoorBlock || b instanceof DoublePlantBlock)
                    ? new DoubleHighBlockItem(b, itemProps)
                    : new BlockItem(b, itemProps);
            for (var action : itemActions) {
                action.accept(item);
            }
            return item;
        }));
        return reg;
    }

    @SafeVarargs
    public static <V extends Block> RegistryObject<Block, V> addNoItem(String id, Supplier<V> block, Consumer<Block>... additionalActions) {
        return BLOCKS.register(id, () -> {
            V instance = block.get();
            for (var action : additionalActions) {
                action.accept(instance);
            }
            return instance;
        });
    }

    public static class ParadiseLostFarmlandBlock extends FarmBlock {
        ParadiseLostFarmlandBlock(BlockBehaviour.Properties settings) {
            super(settings);
        }

        public BlockState getStateForPlacement(BlockPlaceContext ctx) {
            return !this.defaultBlockState().canSurvive(ctx.getLevel(), ctx.getClickedPos())
                    ? DIRT.get().defaultBlockState()
                    : super.getStateForPlacement(ctx);
        }
    }

    public static class ParadiseLostPaneBlock extends IronBarsBlock {
        ParadiseLostPaneBlock(BlockBehaviour.Properties settings) {
            super(settings);
        }
    }

    public static class ParadiseLostStairsBlock extends StairBlock {
        ParadiseLostStairsBlock(BlockState baseBlockState, BlockBehaviour.Properties settings) {
            super(baseBlockState, settings);
        }
    }

    public static WoodBlockSet registerWoodBlockSet(WoodType woodType, BlockSetType blockSetType, TreeGrower saplingGenerator, MapColor woodColor, MapColor barkColor, MapColor leafColor) {
        var name = woodType.name();
        var id = name.contains(":") ? name.split(":", 2)[1] : name;
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
        var saplingSettings = BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING).mapColor(MapColor.GOLD).lightLevel(state -> 3);
        var flowerPotSettings = BlockBehaviour.Properties.ofFullCopy(Blocks.POTTED_OAK_SAPLING).lightLevel(state -> 3);
        var leavesSettings = BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES).mapColor(MapColor.GOLD).lightLevel(state -> 5);
        RegistryObject<Block, SaplingBlock> sapling = add(id + "_sapling",
                () -> new ParadiseLostSaplingBlock(ParadiseLostSaplingGenerators.MOTHER_AUREL, saplingSettings), List.of(compostable30, fuel(100)), cutoutRenderLayer);
        return registerWoodBlockSet(
                ParadiseLostWoodTypes.MOTHER_AUREL, ParadiseLostBlockSets.MOTHER_AUREL,
                sapling,
                addNoItem("potted_" + id + "_sapling", () -> new FlowerPotBlock(sapling.get(), flowerPotSettings), cutoutRenderLayer),
                id + "_log", id + "_wood", "stripped_" + id + "_log", "stripped_" + id + "_wood",
                add(id + "_leaves", () -> new ParadiseLostLeavesBlock(leavesSettings), List.of(compostable30), flammableLeaves, cutoutMippedRenderLayer),
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
                MapColor.SAND, MapColor.COLOR_BROWN
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
            TreeGrower saplingGenerator, MapColor woodColor, MapColor barkColor, MapColor leafColor
    ) {
        var saplingSettings = BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING).mapColor(woodColor);
        var flowerPotSettings = BlockBehaviour.Properties.ofFullCopy(Blocks.POTTED_OAK_SAPLING);
        var logSettings = BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG).mapColor(barkColor).instrument(NoteBlockInstrument.BASS);
        var leavesSettings = BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES).mapColor(leafColor);
        var plankSettings = BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).mapColor(woodColor).instrument(NoteBlockInstrument.BASS);
        var doorSettings = BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_DOOR).mapColor(woodColor).instrument(NoteBlockInstrument.BASS);
        var trapdoorSettings = BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_TRAPDOOR).mapColor(woodColor).instrument(NoteBlockInstrument.BASS);
        var buttonSettings = BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_BUTTON).mapColor(woodColor);
        var pressurePlateSettings = BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PRESSURE_PLATE).mapColor(woodColor).instrument(NoteBlockInstrument.BASS);

        RegistryObject<Block, SaplingBlock> sapling = add(saplingId, () -> new ParadiseLostMultiSaplingBlock(saplingGenerator, saplingSettings, List.of(
                Pair.of(BlockRegistry.MOSSY_FLOESTONE.get(), ParadiseLostSaplingGenerators.MOTTLED_AUREL),
                Pair.of(BlockRegistry.LIVERWORT.get(), ParadiseLostSaplingGenerators.THICKET_AUREL)
        )), List.of(compostable30, fuel(100)), cutoutRenderLayer);
        RegistryObject<Block, RotatedPillarBlock> strippedLog = add(strippedLogId, () -> new RotatedPillarBlock(logSettings), List.of(fuel(300)), flammableLog);
        RegistryObject<Block, RotatedPillarBlock> strippedWood = add(strippedWoodId, () -> new RotatedPillarBlock(logSettings), List.of(fuel(300)), flammableLog);
        RegistryObject<Block, Block> planks = add(plankId, () -> new Block(plankSettings), List.of(fuel(300)), flammablePlanks);
        return new WoodBlockSet(
                sapling, addNoItem(flowerPotId, () -> new FlowerPotBlock(sapling.get(), flowerPotSettings), cutoutRenderLayer),
                add(logId, () -> new RotatedPillarBlock(logSettings), List.of(fuel(300)), flammableLog, stripsTo(strippedLog)),
                add(woodId, () -> new RotatedPillarBlock(logSettings), List.of(fuel(300)), flammableLog, stripsTo(strippedWood)),
                strippedLog, strippedWood,
                add(leavesId, () -> new LeavesBlock(leavesSettings), List.of(compostable30), flammableLeaves, cutoutMippedRenderLayer),
                planks,
                add(plankStairsId, () -> new ParadiseLostStairsBlock(planks.get().defaultBlockState(), plankSettings), List.of(fuel(300)), flammablePlanks),
                add(plankSlabId, () -> new SlabBlock(plankSettings), List.of(fuel(150)), flammablePlanks),
                add(fenceId, () -> new FenceBlock(plankSettings), List.of(fuel(300)), flammablePlanks),
                add(fenceGateId, () -> new FenceGateBlock(woodType, plankSettings), List.of(fuel(300)), flammablePlanks),
                add(doorId, () -> new ParadiseLostDoorBlock(blockSetType, doorSettings), List.of(fuel(200)), cutoutMippedRenderLayer),
                add(trapdoorId, () -> new ParadiseLostTrapdoorBlock(blockSetType, trapdoorSettings), List.of(fuel(300)), cutoutMippedRenderLayer),
                add(buttonId, () -> new ParadiseLostButtonBlock(blockSetType, 30, buttonSettings), List.of(fuel(100))),
                add(pressurePlateId, () -> new ParadiseLostPressurePlateBlock(blockSetType, pressurePlateSettings), List.of(fuel(100)))
        );
    }

    private static WoodBlockSet registerWoodBlockSet(
            WoodType woodType, BlockSetType blockSetType,
            RegistryObject<Block, SaplingBlock> sapling, RegistryObject<Block, FlowerPotBlock> flowerPot,
            String logId, String woodId, String strippedLogId, String strippedWoodId,
            RegistryObject<Block, LeavesBlock> leaves,
            String plankId, String plankStairsId, String plankSlabId,
            String fenceId, String fenceGateId,
            String doorId, String trapdoorId,
            String buttonId, String pressurePlateId,
            MapColor woodColor, MapColor barkColor
    ) {
        var logSettings = BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG).mapColor(barkColor);
        var plankSettings = BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).mapColor(woodColor);
        var doorSettings = BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_DOOR).mapColor(woodColor);
        var trapdoorSettings = BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_TRAPDOOR).mapColor(woodColor);
        var buttonSettings = BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_BUTTON).mapColor(woodColor);
        var pressurePlateSettings = BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PRESSURE_PLATE).mapColor(woodColor);

        RegistryObject<Block, RotatedPillarBlock> strippedLog = add(strippedLogId, () -> new RotatedPillarBlock(logSettings), List.of(fuel(300)), flammableLog);
        RegistryObject<Block, RotatedPillarBlock> strippedWood = add(strippedWoodId, () -> new RotatedPillarBlock(logSettings), List.of(fuel(300)), flammableLog);
        RegistryObject<Block, Block> planks = add(plankId, () -> new Block(plankSettings), List.of(fuel(300)), flammablePlanks);
        return new WoodBlockSet(
                sapling, flowerPot,
                add(logId, () -> new RotatedPillarBlock(logSettings), List.of(fuel(300)), flammableLog, stripsTo(strippedLog)),
                add(woodId, () -> new RotatedPillarBlock(logSettings), List.of(fuel(300)), flammableLog, stripsTo(strippedWood)),
                strippedLog, strippedWood,
                leaves,
                planks,
                add(plankStairsId, () -> new ParadiseLostStairsBlock(planks.get().defaultBlockState(), plankSettings), List.of(fuel(300)), flammablePlanks),
                add(plankSlabId, () -> new SlabBlock(plankSettings), List.of(fuel(150)), flammablePlanks),
                add(fenceId, () -> new FenceBlock(plankSettings), List.of(fuel(300)), flammablePlanks),
                add(fenceGateId, () -> new FenceGateBlock(woodType, plankSettings), List.of(fuel(300)), flammablePlanks),
                add(doorId, () -> new ParadiseLostDoorBlock(blockSetType, doorSettings), List.of(fuel(200)), cutoutMippedRenderLayer),
                add(trapdoorId, () -> new ParadiseLostTrapdoorBlock(blockSetType, trapdoorSettings), List.of(fuel(300)), cutoutMippedRenderLayer),
                add(buttonId, () -> new ParadiseLostButtonBlock(blockSetType, 30, buttonSettings), List.of(fuel(100))),
                add(pressurePlateId, () -> new ParadiseLostPressurePlateBlock(blockSetType, pressurePlateSettings), List.of(fuel(100)))
        );
    }

    public record WoodBlockSet(
            @Nullable RegistryObject<Block, ? extends SaplingBlock> sapling,
            @Nullable RegistryObject<Block, ? extends FlowerPotBlock> flowerPot,
            RegistryObject<Block, ? extends RotatedPillarBlock> log,
            RegistryObject<Block, ? extends RotatedPillarBlock> wood,
            RegistryObject<Block, ? extends RotatedPillarBlock> strippedLog,
            RegistryObject<Block, ? extends RotatedPillarBlock> strippedWood,
            @Nullable RegistryObject<Block, ? extends LeavesBlock> leaves,
            RegistryObject<Block, ? extends Block> plank,
            RegistryObject<Block, ? extends StairBlock> plankStairs,
            RegistryObject<Block, ? extends SlabBlock> plankSlab,
            RegistryObject<Block, ? extends FenceBlock> fence,
            RegistryObject<Block, ? extends FenceGateBlock> fenceGate,
            RegistryObject<Block, ? extends DoorBlock> door,
            RegistryObject<Block, ? extends TrapDoorBlock> trapdoor,
            RegistryObject<Block, ? extends ButtonBlock> button,
            RegistryObject<Block, ? extends PressurePlateBlock> pressurePlate
    ) implements Iterable<Block> {
        public @NotNull Iterator<Block> iterator() {
            List<Block> blocks = new ArrayList<>();
            if (sapling != null) blocks.add(sapling.get());
            if (flowerPot != null) blocks.add(flowerPot.get());
            blocks.add(log.get());
            blocks.add(wood.get());
            blocks.add(strippedLog.get());
            blocks.add(strippedWood.get());
            if (leaves != null) blocks.add(leaves.get());
            blocks.add(plank.get());
            blocks.add(plankStairs.get());
            blocks.add(plankSlab.get());
            blocks.add(fence.get());
            blocks.add(fenceGate.get());
            blocks.add(door.get());
            blocks.add(trapdoor.get());
            blocks.add(button.get());
            blocks.add(pressurePlate.get());
            return blocks.iterator();
        }
    }

    public static SimpleBlockSet registerSimpleBlockSet(String blockId, BlockBehaviour.Properties settings) {
        return registerSimpleBlockSet(blockId, settings, List.of(), List.of(), List.of());
    }

    public static SimpleBlockSet registerSimpleBlockSet(
            String blockId,
            BlockBehaviour.Properties settings,
            List<Consumer<ItemLike>> blockItemActions,
            List<Consumer<ItemLike>> stairsItemActions,
            List<Consumer<ItemLike>> slabItemActions
    ) {
        RegistryObject<Block, Block> block = add(blockId, () -> new Block(settings), blockItemActions);
        RegistryObject<Block, StairBlock> stairs = add(blockId + "_stairs",
                () -> new ParadiseLostStairsBlock(block.get().defaultBlockState(), settings), stairsItemActions);
        RegistryObject<Block, SlabBlock> slab = add(blockId + "_slab", () -> new SlabBlock(settings), slabItemActions);
        return new SimpleBlockSet(block, stairs, slab);
    }

    public record SimpleBlockSet(
            RegistryObject<Block, ? extends Block> block,
            RegistryObject<Block, ? extends StairBlock> stairs,
            RegistryObject<Block, ? extends SlabBlock> slab
    ) implements Iterable<Block> {
        public @NotNull Iterator<Block> iterator() {
            return List.of(block.get(), stairs.get(), slab.get()).iterator();
        }
    }

    public static SignSet registerSignSet(WoodType woodType) {
        var signSettings = BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SIGN).instrument(NoteBlockInstrument.BASS);
        var hangingSignSettings = BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_HANGING_SIGN).instrument(NoteBlockInstrument.BASS);

        String name = woodType.name().contains(":") ? woodType.name().split(":", 2)[1] : woodType.name();

        RegistryObject<Block, StandingSignBlock> signBlock = addNoItem(name + "_sign", () -> new ParadiseSignBlock(signSettings, woodType));
        RegistryObject<Block, WallSignBlock> wallSignBlock = addNoItem(name + "_wall_sign", () -> new ParadiseWallSignBlock(signSettings, woodType));
        RegistryObject<Block, CeilingHangingSignBlock> hangingSign = addNoItem(name + "_hanging_sign", () -> new ParadiseHangingSignBlock(woodType, hangingSignSettings));
        RegistryObject<Block, WallHangingSignBlock> wallHangingSign = addNoItem(name + "_wall_hanging_sign", () -> new ParadiseWallHangingSignBlock(woodType, hangingSignSettings));

        return new SignSet(signBlock, wallSignBlock, hangingSign, wallHangingSign);
    }

    public record SignSet(
            RegistryObject<Block, ? extends StandingSignBlock> sign,
            RegistryObject<Block, ? extends WallSignBlock> wallSign,
            RegistryObject<Block, ? extends CeilingHangingSignBlock> hangingSign,
            RegistryObject<Block, ? extends WallHangingSignBlock> wallHangingSign
    ) implements Iterable<Block> {
        public @NotNull Iterator<Block> iterator() {
            return List.<Block>of(sign.get(), wallSign.get(), hangingSign.get(), wallHangingSign.get()).iterator();
        }
    }

	protected static Properties unbreakable(BlockBehaviour.Properties settings) {
        return settings.strength(-1f, 3600000f);
    }

    private static Properties grassBlock() {
        return ofFullCopy(Blocks.GRASS_BLOCK).mapColor(MapColor.GLOW_LICHEN).strength(0.4f);
    }
    private static Properties permafrost() {
        return ofFullCopy(Blocks.DIRT).strength(2f).sound(SoundType.GILDED_BLACKSTONE);
    }

    public static final RegistryObject<Block, FarmBlock> FARMLAND = add("farmland", () -> new ParadiseLostFarmlandBlock(ofFullCopy(Blocks.FARMLAND)));
    public static final RegistryObject<Block, ParadiseLostDirtPathBlock> DIRT_PATH = add("grass_path", () -> new ParadiseLostDirtPathBlock(ofFullCopy(Blocks.DIRT_PATH).mapColor(MapColor.TERRACOTTA_CYAN), BlockRegistry.DIRT));
    public static final RegistryObject<Block, ParadiseLostDirtPathBlock> PERMAFROST_PATH = add("frozen_path", () -> new ParadiseLostDirtPathBlock(permafrost(), BlockRegistry.PERMAFROST));
    public static final RegistryObject<Block, ParadiseLostGrassBlock> HIGHLANDS_GRASS = add("highlands_grass", () -> new ParadiseLostGrassBlock(grassBlock()), cutoutMippedRenderLayer, tillable(), flattenable(BlockRegistry.DIRT_PATH));
    public static final RegistryObject<Block, ParadiseLostSnowyBlock> FROZEN_GRASS = add("frozen_grass", () -> new ParadiseLostSnowyBlock(grassBlock().mapColor(MapColor.SNOW).strength(2F).sound(SoundType.GILDED_BLACKSTONE)), flattenable(BlockRegistry.PERMAFROST_PATH));

    public static final RegistryObject<Block, Block> DIRT = add("dirt", () -> new Block(ofFullCopy(Blocks.DIRT).strength(0.3f).mapColor(MapColor.COLOR_BROWN)), tillable(), flattenable(BlockRegistry.DIRT_PATH));
    public static final RegistryObject<Block, Block> COARSE_DIRT = add("coarse_dirt", () -> new Block(ofFullCopy(Blocks.DIRT).mapColor(MapColor.COLOR_BROWN).strength(0.3f)), coarseTillable(), flattenable(BlockRegistry.DIRT_PATH));
    public static final RegistryObject<Block, FloatingBlock> LEVITA = add("levita", () -> new FloatingBlock(false, ofFullCopy(Blocks.GRAVEL).mapColor(MapColor.TERRACOTTA_LIGHT_BLUE).strength(0.3f)));
    public static final RegistryObject<Block, Block> PERMAFROST = add("permafrost", () -> new Block(permafrost()), flattenable(BlockRegistry.PERMAFROST_PATH));
    public static final RegistryObject<Block, Block> PACKED_SWEDROOT = add("packed_swedroot", () -> new Block(of().strength(2f).sound(SoundType.SHROOMLIGHT).instrument(NoteBlockInstrument.BANJO)), List.of(compostable85));
    public static final RegistryObject<Block, Block> LIVERWORT = add("liverwort", () -> new Block(ofFullCopy(MOSS_BLOCK).sound(SoundType.AZALEA_LEAVES)), List.of(compostable100, fuel(150)));
    public static final RegistryObject<Block, CarpetBlock> LIVERWORT_CARPET = add("liverwort_carpet", () -> new CarpetBlock(ofFullCopy(MOSS_BLOCK).sound(SoundType.AZALEA_LEAVES)), List.of(compostable65, fuel(100)));

    public static final SimpleBlockSet THATCH_SET = registerSimpleBlockSet("thatch", of().mapColor(MapColor.SAND).strength(0.3f).sound(SoundType.GRASS).instrument(NoteBlockInstrument.BANJO), List.of(compostable15, fuel(100)), List.of(compostable15, fuel(100)), List.of(compostable15, fuel(50)));

    private static Properties cloud() {
        return of().strength(0.2F).sound(SoundType.WOOL).noOcclusion().isRedstoneConductor(never).isSuffocating(never).isViewBlocking(never);
    }

    public static final RegistryObject<Block, ParadiseLostCloudBlock> COLD_CLOUD = add("cold_cloud", () -> new ParadiseLostCloudBlock(cloud().mapColor(MapColor.CLAY)), translucentRenderLayer);
    public static final RegistryObject<Block, BlueParadiseLostCloudBlock> BLUE_CLOUD = add("blue_cloud", () -> new BlueParadiseLostCloudBlock(cloud().mapColor(MapColor.COLOR_CYAN)), translucentRenderLayer);
    public static final RegistryObject<Block, GoldenParadiseLostCloudBlock> GOLDEN_CLOUD = add("golden_cloud", () -> new GoldenParadiseLostCloudBlock(cloud().mapColor(MapColor.GOLD)), translucentRenderLayer);
    public static final RegistryObject<Block, DirectionalParadiseLostCloudBlock> GREEN_CLOUD = add("green_cloud", () -> new DirectionalParadiseLostCloudBlock(cloud().mapColor(MapColor.EMERALD)), translucentRenderLayer);

    private static Properties floestone() {
        return of().mapColor(MapColor.COLOR_LIGHT_GRAY).requiresCorrectToolForDrops().strength(0.5f, 5f).sound(SoundType.STONE).instrument(NoteBlockInstrument.BASEDRUM);
    }

    public static final RegistryObject<Block, Block> FLOESTONE = add("floestone", () -> new Block(floestone()));
    public static final RegistryObject<Block, SlabBlock> FLOESTONE_SLAB = add("floestone_slab", () -> new SlabBlock(floestone()));
    public static final RegistryObject<Block, ParadiseLostStairsBlock> FLOESTONE_STAIRS = add("floestone_stairs", () -> new ParadiseLostStairsBlock(FLOESTONE.get().defaultBlockState(), floestone()));
    public static final RegistryObject<Block, WallBlock> FLOESTONE_WALL = add("floestone_wall", () -> new WallBlock(floestone()));

    private static Properties cobbledFloestone() {
        return floestone().strength(0.4f, 8f);
    }

    public static final RegistryObject<Block, Block> COBBLED_FLOESTONE = add("cobbled_floestone", () -> new Block(cobbledFloestone().mapColor(MapColor.COLOR_LIGHT_GRAY)));
    public static final RegistryObject<Block, SlabBlock> COBBLED_FLOESTONE_SLAB = add("cobbled_floestone_slab", () -> new SlabBlock(cobbledFloestone().mapColor(MapColor.COLOR_LIGHT_GRAY)));
    public static final RegistryObject<Block, ParadiseLostStairsBlock> COBBLED_FLOESTONE_STAIRS = add("cobbled_floestone_stairs", () -> new ParadiseLostStairsBlock(COBBLED_FLOESTONE.get().defaultBlockState(), cobbledFloestone().mapColor(MapColor.COLOR_LIGHT_GRAY)));
    public static final RegistryObject<Block, WallBlock> COBBLED_FLOESTONE_WALL = add("cobbled_floestone_wall", () -> new WallBlock(cobbledFloestone().mapColor(MapColor.COLOR_LIGHT_GRAY)));

    private static Properties mossyCobbledFloestone() {
        return cobbledFloestone().mapColor(MapColor.GRASS);
    }

    public static final RegistryObject<Block, Block> MOSSY_FLOESTONE = add("mossy_floestone", () -> new Block(mossyCobbledFloestone().mapColor(MapColor.GLOW_LICHEN)));
    public static final RegistryObject<Block, Block> GOLDEN_MOSSY_FLOESTONE = add("golden_mossy_floestone", () -> new Block(mossyCobbledFloestone().strength(2f, 6f).mapColor(MapColor.GOLD)));
    public static final RegistryObject<Block, SlabBlock> MOSSY_FLOESTONE_SLAB = add("mossy_floestone_slab", () -> new SlabBlock(mossyCobbledFloestone().mapColor(MapColor.GLOW_LICHEN)));
    public static final RegistryObject<Block, ParadiseLostStairsBlock> MOSSY_FLOESTONE_STAIRS = add("mossy_floestone_stairs", () -> new ParadiseLostStairsBlock(MOSSY_FLOESTONE.get().defaultBlockState(), mossyCobbledFloestone().mapColor(MapColor.GLOW_LICHEN)));
    public static final RegistryObject<Block, WallBlock> MOSSY_FLOESTONE_WALL = add("mossy_floestone_wall", () -> new WallBlock(mossyCobbledFloestone().mapColor(MapColor.GLOW_LICHEN)));

    private static Properties floestoneBrick() {
        return floestone().strength(1.5f, 6f);
    }

    public static final RegistryObject<Block, Block> FLOESTONE_BRICK = add("floestone_brick", () -> new Block(floestoneBrick()));
    public static final RegistryObject<Block, Block> CHISELED_FLOESTONE = add("chiseled_floestone", () -> new Block(floestoneBrick()));
    public static final RegistryObject<Block, SlabBlock> FLOESTONE_BRICK_SLAB = add("floestone_brick_slab", () -> new SlabBlock(floestoneBrick()));
    public static final RegistryObject<Block, ParadiseLostStairsBlock> FLOESTONE_BRICK_STAIRS = add("floestone_brick_stairs", () -> new ParadiseLostStairsBlock(FLOESTONE_BRICK.get().defaultBlockState(), floestoneBrick()));
    public static final RegistryObject<Block, WallBlock> FLOESTONE_BRICK_WALL = add("floestone_brick_wall", () -> new WallBlock(floestoneBrick()));
    public static final RegistryObject<Block, Block> MOSSY_FLOESTONE_BRICK = add("mossy_floestone_brick", () -> new Block(floestoneBrick().mapColor(MapColor.GLOW_LICHEN)));
    public static final RegistryObject<Block, SlabBlock> MOSSY_FLOESTONE_BRICK_SLAB = add("mossy_floestone_brick_slab", () -> new SlabBlock(floestoneBrick().mapColor(MapColor.GLOW_LICHEN)));
    public static final RegistryObject<Block, ParadiseLostStairsBlock> MOSSY_FLOESTONE_BRICK_STAIRS = add("mossy_floestone_brick_stairs", () -> new ParadiseLostStairsBlock(MOSSY_FLOESTONE_BRICK.get().defaultBlockState(), floestoneBrick().mapColor(MapColor.GLOW_LICHEN)));
    public static final RegistryObject<Block, WallBlock> MOSSY_FLOESTONE_BRICK_WALL = add("mossy_floestone_brick_wall", () -> new WallBlock(floestoneBrick().mapColor(MapColor.GLOW_LICHEN)));
    public static final RegistryObject<Block, Block> SMOOTH_FLOESTONE = add("smooth_floestone", () -> new Block(floestoneBrick()));
    public static final RegistryObject<Block, SlabBlock> SMOOTH_FLOESTONE_SLAB = add("smooth_floestone_slab", () -> new SlabBlock(floestoneBrick()));
    public static final RegistryObject<Block, ParadiseLostStairsBlock> SMOOTH_FLOESTONE_STAIRS = add("smooth_floestone_stairs", () -> new ParadiseLostStairsBlock(SMOOTH_FLOESTONE.get().defaultBlockState(), floestoneBrick()));

    public static final RegistryObject<Block, Block> HELIOLITH = add("heliolith", () -> new Block(floestone().mapColor(MapColor.TERRACOTTA_WHITE)));
    public static final RegistryObject<Block, Block> SMOOTH_HELIOLITH = add("smooth_heliolith", () -> new Block(floestone().mapColor(MapColor.TERRACOTTA_WHITE)));
    public static final RegistryObject<Block, SlabBlock> HELIOLITH_SLAB = add("heliolith_slab", () -> new SlabBlock(floestone().mapColor(MapColor.TERRACOTTA_WHITE)));
    public static final RegistryObject<Block, SlabBlock> SMOOTH_HELIOLITH_SLAB = add("smooth_heliolith_slab", () -> new SlabBlock(floestone().mapColor(MapColor.TERRACOTTA_WHITE)));
    public static final RegistryObject<Block, ParadiseLostStairsBlock> HELIOLITH_STAIRS = add("heliolith_stairs", () -> new ParadiseLostStairsBlock(FLOESTONE_BRICK.get().defaultBlockState(), floestone().mapColor(MapColor.TERRACOTTA_WHITE)));
    public static final RegistryObject<Block, ParadiseLostStairsBlock> SMOOTH_HELIOLITH_STAIRS = add("smooth_heliolith_stairs", () -> new ParadiseLostStairsBlock(FLOESTONE_BRICK.get().defaultBlockState(), floestone().mapColor(MapColor.TERRACOTTA_WHITE)));
    public static final RegistryObject<Block, WallBlock> HELIOLITH_WALL = add("heliolith_wall", () -> new WallBlock(floestone()));

    public static final SimpleBlockSet LEVITA_BRICK_SET = registerSimpleBlockSet("levita_brick", of().mapColor(MapColor.CLAY).strength(0.3f, 3f).sound(SoundType.CALCITE).instrument(NoteBlockInstrument.BASEDRUM));
    public static final RegistryObject<Block, Block> CHISELED_LEVITA_BRICK = add("chiseled_levita_brick", () -> new Block(of().mapColor(MapColor.CLAY).strength(0.3f, 3f).sound(SoundType.CALCITE).instrument(NoteBlockInstrument.BASEDRUM)));

    private static Properties burnishedStone() {
        return of().mapColor(MapColor.DEEPSLATE).strength(4f, 6f).requiresCorrectToolForDrops().instrument(NoteBlockInstrument.BASEDRUM);
    }
    public static final SimpleBlockSet BURNISHED_STONE_SET = registerSimpleBlockSet("burnished_stone", burnishedStone());
    public static final RegistryObject<Block, WallBlock> BURNISHED_STONE_WALL = add("burnished_stone_wall", () -> new WallBlock(burnishedStone()));
    public static final RegistryObject<Block, Block> BURNISHED_STONE_PLAQUE = add("burnished_stone_plaque", () -> new Block(burnishedStone()));
    public static final RegistryObject<Block, Block> BURNISHED_STONE_SCRIPT = add("burnished_stone_script", () -> new Block(burnishedStone()));

    private static Properties amberTiles() {
        return of().mapColor(MapColor.GOLD).requiresCorrectToolForDrops().sound(SoundType.CALCITE).strength(2, 6).instrument(NoteBlockInstrument.BASEDRUM);
    }

    public static final RegistryObject<Block, Block> GOLDEN_AMBER_TILE = add("golden_amber_tile", () -> new Block(amberTiles()));
    public static final RegistryObject<Block, SlabBlock> GOLDEN_AMBER_TILE_SLAB = add("golden_amber_tile_slab", () -> new SlabBlock(amberTiles()));
    public static final RegistryObject<Block, ParadiseLostStairsBlock> GOLDEN_AMBER_TILE_STAIRS = add("golden_amber_tile_stairs", () -> new ParadiseLostStairsBlock(GOLDEN_AMBER_TILE.get().defaultBlockState(), amberTiles()));

    public static final RegistryObject<Block, CropGrowthBlock> BLOOMED_CALCITE = add("bloomed_calcite", () -> new CropGrowthBlock(ofFullCopy(CALCITE).instrument(NoteBlockInstrument.BASEDRUM).randomTicks(), 2));
    public static final SimpleBlockSet CALCITE_TILES_SET = registerSimpleBlockSet("calcite_tiles", ofFullCopy(CALCITE).instrument(NoteBlockInstrument.BASEDRUM));
    public static final RegistryObject<Block, WallBlock> CALCITE_TILES_WALL = add("calcite_tiles_wall", () -> new WallBlock(floestone()));
    public static final SimpleBlockSet BLOOMED_CALCITE_TILES_SET = registerSimpleBlockSet("bloomed_calcite_tiles", ofFullCopy(CALCITE).instrument(NoteBlockInstrument.BASEDRUM));
    public static final RegistryObject<Block, WallBlock> BLOOMED_CALCITE_TILES_WALL = add("bloomed_calcite_tiles_wall", () -> new WallBlock(floestone()));

    protected static Properties flowerPot() {
        return ofFullCopy(POTTED_OAK_SAPLING);
    }

    public static final RegistryObject<Block, CalciteFlowerPotBlock> CALCITE_FLOWER_POT = add("calcite_flower_pot", () -> new CalciteFlowerPotBlock(ofFullCopy(FLOWER_POT)));
    public static final RegistryObject<Block, CalciteDecoratedPotBlock> CALCITE_DECORATED_POT = add("calcite_decorated_pot", () -> new CalciteDecoratedPotBlock(ofFullCopy(DECORATED_POT).mapColor(MapColor.SNOW)));

    public static final RegistryObject<Block, CampfireBlock> CHERINE_CAMPFIRE = add("cherine_campfire", () -> new CherineCampfireBlock(false, 1, ofFullCopy(CAMPFIRE).instrument(NoteBlockInstrument.BASS)), cutoutRenderLayer);

    public static final RegistryObject<Block, PalaceDoorBlock> PALACE_DOOR = addNoItem("palace_door", () -> new PalaceDoorBlock(ofFullCopy(BEDROCK).noOcclusion().mapColor(MapColor.COLOR_YELLOW)));
    public static final RegistryObject<Block, PalaceDoorExtensionBlock> PALACE_DOOR_EXTENSION = addNoItem("palace_door_extension", () -> new PalaceDoorExtensionBlock(ofFullCopy(BEDROCK).noOcclusion().mapColor(MapColor.COLOR_YELLOW)));

    protected static Properties leafPile() {
        return of().strength(0.2f).sound(SoundType.VINE).replaceable().noOcclusion().isSuffocating(never).isViewBlocking(never).pushReaction(PushReaction.DESTROY);
    }

    public static final WoodBlockSet AUREL_WOODSTUFF = registerWoodBlockSet(ParadiseLostWoodTypes.AUREL, ParadiseLostBlockSets.AUREL, ParadiseLostSaplingGenerators.AUREL, MapColor.TERRACOTTA_BROWN, MapColor.TERRACOTTA_BROWN, MapColor.GRASS);
    public static final RegistryObject<Block, RotatedPillarBlock> MOTTLED_AUREL_LOG = add("mottled_aurel_log", () -> new RotatedPillarBlock(ofFullCopy(OAK_LOG).instrument(NoteBlockInstrument.BASS).mapColor(MapColor.TERRACOTTA_WHITE)), List.of(fuel(300)), flammableLog, stripsTo(AUREL_WOODSTUFF.strippedLog()));
    public static final RegistryObject<Block, RotatedPillarBlock> MOTTLED_AUREL_WOOD = add("mottled_aurel_wood", () -> new RotatedPillarBlock(ofFullCopy(OAK_LOG).instrument(NoteBlockInstrument.BASS).mapColor(MapColor.SAND)), List.of(fuel(300)), flammableLog, stripsTo(AUREL_WOODSTUFF.strippedWood()));
    public static final RegistryObject<Block, ChuteBlock> MOTTLED_AUREL_FALLEN_LOG = add("mottled_aurel_fallen_log", () -> new ChuteBlock(ofFullCopy(OAK_LOG).instrument(NoteBlockInstrument.BASS).mapColor(MapColor.TERRACOTTA_WHITE)), List.of(fuel(300)), flammableLog, cutoutRenderLayer);
    public static final RegistryObject<Block, LeafPileBlock> AUREL_LEAF_PILE = add("aurel_leaf_pile", () -> new LeafPileBlock(leafPile().mapColor(MapColor.GRASS)), List.of(compostable30), flammableLeaves, cutoutMippedRenderLayer);
    public static final RegistryObject<Block, Block> AUREL_BOOKSHELF = add("aurel_bookshelf", () -> new Block(ofFullCopy(BOOKSHELF).instrument(NoteBlockInstrument.BASS).mapColor(MapColor.TERRACOTTA_BROWN)), List.of(fuel(300)), flammable(30, 20));
    public static final SignSet AUREL_SIGNS = registerSignSet(ParadiseLostWoodTypes.AUREL);

    public static final WoodBlockSet MOTHER_AUREL_WOODSTUFF = registerWoodBlockSetMotherAurel();
    public static final SignSet MOTHER_AUREL_SIGNS = registerSignSet(ParadiseLostWoodTypes.MOTHER_AUREL);

    public static final WoodBlockSet MENTH_WOODSTUFF = registerWoodBlockSet(ParadiseLostWoodTypes.MENTH, ParadiseLostBlockSets.MENTH, ParadiseLostSaplingGenerators.MENTH, MapColor.GLOW_LICHEN, MapColor.GLOW_LICHEN, MapColor.TERRACOTTA_MAGENTA);
    public static final SignSet MENTH_SIGNS = registerSignSet(ParadiseLostWoodTypes.MENTH);

    public static final WoodBlockSet WISTERIA_WOODSTUFF = registerWoodBlockSetWisteria();
    public static final SignSet WISTERIA_SIGNS = registerSignSet(ParadiseLostWoodTypes.WISTERIA);

    protected static Properties wisteriaLeaf() {
        return ofFullCopy(OAK_LEAVES);
    }
    protected static Properties wisteriaHanger() {
        return of().pushReaction(PushReaction.DESTROY).strength(0.2f).noCollission().instabreak().sound(SoundType.GRASS).isSuffocating(never).isViewBlocking(never);
    }
    protected static Properties wisteriaSapling() {
        return ofFullCopy(Blocks.OAK_SAPLING);
    }

    public static final RegistryObject<Block, WisteriaLeavesBlock> ROSE_WISTERIA_LEAVES = add("rose_wisteria_leaves", () -> new WisteriaLeavesBlock(wisteriaLeaf().mapColor(MapColor.COLOR_PINK)), List.of(compostable30), flammableLeaves, cutoutMippedRenderLayer);
    public static final RegistryObject<Block, LeafPileBlock> ROSE_WISTERIA_LEAF_PILE = add("rose_wisteria_leaf_pile", () -> new LeafPileBlock(leafPile().mapColor(MapColor.COLOR_PINK)), List.of(compostable30), flammableLeaves, cutoutMippedRenderLayer);
    public static final RegistryObject<Block, SaplingBlock> ROSE_WISTERIA_SAPLING = add("rose_wisteria_sapling", () -> new ParadiseLostSaplingBlock(ParadiseLostSaplingGenerators.ROSE_WISTERIA, wisteriaSapling().mapColor(MapColor.COLOR_PINK)), List.of(compostable30, fuel(100)), cutoutRenderLayer);
    public static final RegistryObject<Block, FlowerPotBlock> POTTED_ROSE_WISTERIA_SAPLING = addNoItem("potted_rose_wisteria_sapling", () -> new FlowerPotBlock(ROSE_WISTERIA_SAPLING.get(), flowerPot()), cutoutRenderLayer);
    public static final RegistryObject<Block, ParadiseLostHangerBlock> ROSE_WISTERIA_HANGER = add("rose_wisteria_hanger", () -> new ParadiseLostHangerBlock(wisteriaHanger().mapColor(MapColor.COLOR_PINK)), List.of(compostable30), flammableLeaves, cutoutRenderLayer);

    public static final RegistryObject<Block, WisteriaLeavesBlock> FROST_WISTERIA_LEAVES = add("frost_wisteria_leaves", () -> new WisteriaLeavesBlock(wisteriaLeaf().mapColor(MapColor.COLOR_LIGHT_BLUE)), List.of(compostable30), flammableLeaves, cutoutMippedRenderLayer);
    public static final RegistryObject<Block, LeafPileBlock> FROST_WISTERIA_LEAF_PILE = add("frost_wisteria_leaf_pile", () -> new LeafPileBlock(leafPile().mapColor(MapColor.COLOR_LIGHT_BLUE)), List.of(compostable30), flammableLeaves, cutoutMippedRenderLayer);
    public static final RegistryObject<Block, SaplingBlock> FROST_WISTERIA_SAPLING = add("frost_wisteria_sapling", () -> new ParadiseLostSaplingBlock(ParadiseLostSaplingGenerators.FROST_WISTERIA, wisteriaSapling().mapColor(MapColor.COLOR_LIGHT_BLUE)), List.of(compostable30, fuel(100)), cutoutRenderLayer);
    public static final RegistryObject<Block, FlowerPotBlock> POTTED_FROST_WISTERIA_SAPLING = addNoItem("potted_frost_wisteria_sapling", () -> new FlowerPotBlock(FROST_WISTERIA_SAPLING.get(), flowerPot()), cutoutRenderLayer);
    public static final RegistryObject<Block, ParadiseLostHangerBlock> FROST_WISTERIA_HANGER = add("frost_wisteria_hanger", () -> new ParadiseLostHangerBlock(wisteriaHanger().mapColor(MapColor.COLOR_LIGHT_BLUE)), List.of(compostable30), flammableLeaves, cutoutRenderLayer);

    public static final RegistryObject<Block, WisteriaLeavesBlock> LAVENDER_WISTERIA_LEAVES = add("lavender_wisteria_leaves", () -> new WisteriaLeavesBlock(wisteriaLeaf().mapColor(MapColor.COLOR_MAGENTA)), List.of(compostable30), flammableLeaves, cutoutMippedRenderLayer);
    public static final RegistryObject<Block, LeafPileBlock> LAVENDER_WISTERIA_LEAF_PILE = add("lavender_wisteria_leaf_pile", () -> new LeafPileBlock(leafPile().mapColor(MapColor.COLOR_MAGENTA)), List.of(compostable30), flammableLeaves, cutoutMippedRenderLayer);
    public static final RegistryObject<Block, SaplingBlock> LAVENDER_WISTERIA_SAPLING = add("lavender_wisteria_sapling", () -> new ParadiseLostSaplingBlock(ParadiseLostSaplingGenerators.LAVENDER_WISTERIA, wisteriaSapling().mapColor(MapColor.COLOR_MAGENTA)), List.of(compostable30, fuel(100)), cutoutRenderLayer);
    public static final RegistryObject<Block, FlowerPotBlock> POTTED_LAVENDER_WISTERIA_SAPLING = addNoItem("potted_lavender_wisteria_sapling", () -> new FlowerPotBlock(LAVENDER_WISTERIA_SAPLING.get(), flowerPot()), cutoutRenderLayer);
    public static final RegistryObject<Block, ParadiseLostHangerBlock> LAVENDER_WISTERIA_HANGER = add("lavender_wisteria_hanger", () -> new ParadiseLostHangerBlock(wisteriaHanger().mapColor(MapColor.COLOR_MAGENTA)), List.of(compostable30), flammableLeaves, cutoutRenderLayer);

    private static Properties shrub() {
        return ofFullCopy(Blocks.SHORT_GRASS).offsetType(BlockBehaviour.OffsetType.XZ).mapColor(MapColor.GRASS);
    }
    private static Properties crop() {
        return ofFullCopy(Blocks.WHEAT).mapColor(MapColor.GRASS);
    }

    public static final RegistryObject<Block, ParadiseLostBrushBlock> GRASS = add("grass_plant", () -> new ParadiseLostBrushBlock(shrub()), List.of(compostable30), flammablePlant, cutoutRenderLayer);
    public static final RegistryObject<Block, ParadiseLostBrushBlock> GRASS_FLOWERING = add("grass_flowering", () -> new ParadiseLostBrushBlock(shrub().mapColor(MapColor.SNOW)), List.of(compostable30), flammablePlant, cutoutRenderLayer);
    public static final RegistryObject<Block, ParadiseLostBrushBlock> SHORT_GRASS = add("short_grass", () -> new ParadiseLostBrushBlock(shrub()), List.of(compostable30), flammablePlant, cutoutRenderLayer);
    public static final RegistryObject<Block, ParadiseLostTallBrushBlock> TALL_GRASS = add("tall_grass", () -> new ParadiseLostTallBrushBlock(shrub()), List.of(compostable50), flammablePlant, cutoutRenderLayer);
    public static final RegistryObject<Block, ParadiseLostBrushBlock> FERN = add("fern", () -> new ParadiseLostBrushBlock(shrub()), List.of(compostable30), flammablePlant, cutoutRenderLayer);
    public static final RegistryObject<Block, FlowerPotBlock> POTTED_FERN = addNoItem("potted_fern", () -> new FlowerPotBlock(FERN.get(), flowerPot()), cutoutRenderLayer);
    public static final RegistryObject<Block, ParadiseLostBrushBlock> BUSH = add("bush", () -> new ParadiseLostBrushBlock(shrub()), List.of(compostable30), flammablePlant, cutoutRenderLayer);
    public static final RegistryObject<Block, GroundcoverBlock> SHAMROCK = add("shamrock", () -> new GroundcoverBlock(shrub().sound(SoundType.AZALEA_LEAVES)), List.of(compostable50), flammablePlant, cutoutRenderLayer);
    public static final RegistryObject<Block, GroundcoverBlock> MALT_SPRIG = add("malt_sprig", () -> new GroundcoverBlock(shrub().sound(SoundType.AZALEA_LEAVES)), List.of(compostable30), flammablePlant, cutoutRenderLayer);
    public static final RegistryObject<Block, TallWaterPlantBlock> HONEY_NETTLE = add("honey_nettle", () -> new TallWaterPlantBlock(shrub().sound(SoundType.SMALL_DRIPLEAF)), List.of(compostable50), cutoutRenderLayer);

    public static final RegistryObject<Block, WallClingingPlantBlock> ROOTCAP = add("rootcap", () -> new WallClingingPlantBlock(ParadiseLostBlockTags.FUNGI_CLINGABLES, ofFullCopy(BROWN_MUSHROOM)), List.of(compostable65), cutoutRenderLayer);
    public static final RegistryObject<Block, ParadiseLostMushroomPlantBlock> BROWN_SPORECAP = add("brown_sporecap", () -> new ParadiseLostMushroomPlantBlock(BlockTags.MUSHROOM_GROW_BLOCK, ParadiseLostTreeConfiguredFeatures.HUGE_BROWN_SPORECAP, ofFullCopy(BROWN_MUSHROOM)), List.of(compostable65), cutoutRenderLayer);
    public static final RegistryObject<Block, ParadiseLostHangingMushroomPlantBlock> PINK_SPORECAP = add("pink_sporecap", () -> new ParadiseLostHangingMushroomPlantBlock(BlockTags.MUSHROOM_GROW_BLOCK, ofFullCopy(BROWN_MUSHROOM)), List.of(compostable65), cutoutRenderLayer);

    public static final RegistryObject<Block, Block> ROOTCAP_BLOCK = add("rootcap_block", () -> new HugeMushroomBlock(ofFullCopy(BROWN_MUSHROOM_BLOCK).instrument(NoteBlockInstrument.BASS).mapColor(MapColor.TERRACOTTA_BROWN)), List.of(compostable85));
    public static final RegistryObject<Block, Block> BROWN_SPORECAP_BLOCK = add("brown_sporecap_block", () -> new HugeMushroomBlock(ofFullCopy(BROWN_MUSHROOM_BLOCK).instrument(NoteBlockInstrument.BASS).mapColor(MapColor.COLOR_BROWN)), List.of(compostable85));
    public static final RegistryObject<Block, Block> PINK_SPORECAP_BLOCK = add("pink_sporecap_block", () -> new HugeMushroomBlock(ofFullCopy(BROWN_MUSHROOM_BLOCK).instrument(NoteBlockInstrument.BASS).mapColor(MapColor.TERRACOTTA_PINK)), List.of(compostable85));

    public static final RegistryObject<Block, AmadrysCropBlock> AMADRYS = addNoItem("amadrys", () -> new AmadrysCropBlock(crop().mapColor(MapColor.COLOR_PINK)), flammablePlant, cutoutMippedRenderLayer);
    public static final RegistryObject<Block, FlaxCropBlock> FLAX = addNoItem("flax", () -> new FlaxCropBlock(crop().mapColor(MapColor.WOOD)), flammablePlant, cutoutRenderLayer);
    public static final RegistryObject<Block, SwedrootCropBlock> SWEDROOT = addNoItem("swedroot", () -> new SwedrootCropBlock(shrub().offsetType(BlockBehaviour.OffsetType.NONE).mapColor(MapColor.COLOR_BLUE)), flammablePlant, cutoutRenderLayer);
    public static final RegistryObject<Block, NitraCropBlock> NITRA = addNoItem("nitra", () -> new NitraCropBlock(crop().mapColor(MapColor.SAND)), flammablePlant, cutoutMippedRenderLayer);

    public static final RegistryObject<Block, Block> FLAXWEAVE_CUSHION = add("flaxweave_cushion", () -> new FlaxweaveCushionBlock(of().mapColor(MapColor.COLOR_YELLOW).sound(SoundType.WOOL).strength(0.2F).instrument(NoteBlockInstrument.GUITAR)), List.of(fuel(300)), flammable(40, 10));
    public static final RegistryObject<Block, SlabBlock> FLAXWEAVE_CUSHION_SLAB = add("flaxweave_cushion_slab", () -> new FlaxweaveCushionSlabBlock(of().mapColor(MapColor.COLOR_YELLOW).sound(SoundType.WOOL).strength(0.2F).instrument(NoteBlockInstrument.GUITAR)), List.of(fuel(150)), flammable(40, 10));

    public static final RegistryObject<Block, BlackcurrantBushBlock> BLACKCURRANT_BUSH = addNoItem("blackcurrant_bush", () -> new BlackcurrantBushBlock(of().strength(0.2f)
            .randomTicks().sound(SoundType.GRASS).noOcclusion().isSuffocating(never).isViewBlocking(never).noCollission().mapColor(MapColor.COLOR_PURPLE)), flammablePlant, cutoutRenderLayer);

    public static final RegistryObject<Block, FourBiteCakeBlock> CHEESECAKE = add("halflight_cheesecake", () -> new FourBiteCakeBlock(Properties.ofFullCopy(CAKE)));

    public static final RegistryObject<Block, SixFacingBlock> AMADRYS_BUNDLE = add("amadrys_bundle", () -> new SixFacingBlock(Properties.ofFullCopy(HAY_BLOCK)), List.of(compostable85));

    private static Properties flower() {
        return ofFullCopy(DANDELION);
    }

    public static final RegistryObject<Block, FlowerBlock> ANCIENT_FLOWER = add("ancient_flower", () -> new FlowerBlock(MobEffects.ABSORPTION, 20, flower()), List.of(compostable65), flammablePlant, cutoutRenderLayer);
    public static final RegistryObject<Block, FlowerPotBlock> POTTED_ANCIENT_FLOWER = addNoItem("potted_ancient_flower", () -> new FlowerPotBlock(ANCIENT_FLOWER.get(), flowerPot()), cutoutRenderLayer);
    public static final RegistryObject<Block, FlowerBlock> ATARAXIA = add("ataraxia", () -> new FlowerBlock(MobEffects.HARM, 1, flower()), List.of(compostable65), flammablePlant, cutoutRenderLayer);
    public static final RegistryObject<Block, FlowerPotBlock> POTTED_ATARAXIA = addNoItem("potted_ataraxia", () -> new FlowerPotBlock(ATARAXIA.get(), flowerPot()), cutoutRenderLayer);
    public static final RegistryObject<Block, FlowerBlock> CLOUDSBLUFF = add("cloudsbluff", () -> new FlowerBlock(MobEffects.SLOW_FALLING, 6, flower()), List.of(compostable65), flammablePlant, cutoutRenderLayer);
    public static final RegistryObject<Block, FlowerPotBlock> POTTED_CLOUDSBLUFF = addNoItem("potted_cloudsbluff", () -> new FlowerPotBlock(CLOUDSBLUFF.get(), flowerPot()), cutoutRenderLayer);
    public static final RegistryObject<Block, FlowerBlock> DRIGEAN = add("drigean", () -> new FlowerBlock(MobEffects.FIRE_RESISTANCE, 8, flower()), List.of(compostable65, fuel(1600)), flammablePlant, cutoutRenderLayer);
    public static final RegistryObject<Block, FlowerPotBlock> POTTED_DRIGEAN = addNoItem("potted_drigean", () -> new FlowerPotBlock(DRIGEAN.get(), flowerPot()), cutoutRenderLayer);
    public static final RegistryObject<Block, FlowerBlock> LUMINAR = add("luminar", () -> new FlowerBlock(MobEffects.GLOWING, 9, flower().lightLevel(value -> 3)), List.of(compostable65), flammablePlant, cutoutRenderLayer);
    public static final RegistryObject<Block, FlowerPotBlock> POTTED_LUMINAR = addNoItem("potted_luminar", () -> new FlowerPotBlock(LUMINAR.get(), flowerPot().lightLevel(value -> 3)), cutoutRenderLayer);

    public static final RegistryObject<Block, ParadiseLostTallBrushBlock> WILD_FLAX = add("wild_flax", () -> new ParadiseLostTallBrushBlock(flower()), List.of(compostable100), flammablePlant, cutoutMippedRenderLayer);

    public static final RegistryObject<Block, DropExperienceBlock> CHERINE_ORE = add("cherine_ore", () -> new DropExperienceBlock(UniformInt.of(0, 2), of().requiresCorrectToolForDrops().strength(1f, 3f).instrument(NoteBlockInstrument.BASEDRUM)));
    public static final RegistryObject<Block, DropExperienceBlock> OLVITE_ORE = add("olvite_ore", () -> new DropExperienceBlock(UniformInt.of(1, 3), of().requiresCorrectToolForDrops().strength(1.5f, 3f).instrument(NoteBlockInstrument.BASEDRUM)));
    public static final RegistryObject<Block, RedStoneOreBlock> FLOESTONE_REDSTONE_ORE = add("floestone_redstone_ore", () -> new RedStoneOreBlock(ofFullCopy(REDSTONE_ORE).strength(1.5f, 3f).instrument(NoteBlockInstrument.BASEDRUM)));
    public static final RegistryObject<Block, DropExperienceBlock> SURTRUM = add("surtrum", () -> new SurtrumOreBlock(UniformInt.of(2, 5), of().sound(SoundType.NETHER_GOLD_ORE).requiresCorrectToolForDrops().strength(9f, 20f).instrument(NoteBlockInstrument.BASEDRUM)), new Item.Properties().fireResistant());
    public static final RegistryObject<Block, Block> METAMORPHIC_SHELL = add("metamorphic_shell", () -> new Block(of().sound(SoundType.TUFF).requiresCorrectToolForDrops().strength(40f, 6f).instrument(NoteBlockInstrument.BASEDRUM)));
    public static final RegistryObject<Block, PoofBlock> SURTRUM_AIR = addNoItem("surtrum_air", () -> new PoofBlock(of().replaceable().sound(SoundType.NETHER_GOLD_ORE)));
    public static final RegistryObject<Block, FloatingBlock> LEVITA_ORE = add("levita_ore", () -> new FloatingBlock(false, of().requiresCorrectToolForDrops().strength(4f).instrument(NoteBlockInstrument.BASEDRUM), UniformInt.of(4, 7)));
    public static final RegistryObject<Block, Block> CHERINE_BLOCK = add("cherine_block", () -> new Block(of().requiresCorrectToolForDrops().strength(3f, -1f).sound(SoundType.STONE)), List.of(fuel(5000)));
    public static final RegistryObject<Block, Block> OLVITE_BLOCK = add("olvite_block", () -> new Block(of().requiresCorrectToolForDrops().strength(3f, -1f).sound(SoundType.METAL).instrument(NoteBlockInstrument.IRON_XYLOPHONE)));
    public static final RegistryObject<Block, Block> REFINED_SURTRUM_BLOCK = add("refined_surtrum_block", () -> new Block(of().requiresCorrectToolForDrops().strength(4f, -1f).sound(SoundType.METAL).instrument(NoteBlockInstrument.IRON_XYLOPHONE)), new Item.Properties().fireResistant());

    public static final RegistryObject<Block, ButtonBlock> FLOESTONE_BUTTON = add("floestone_button", () -> new ParadiseLostButtonBlock(BlockSetType.STONE, 20, Properties.of().noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY)));
    public static final RegistryObject<Block, PressurePlateBlock> FLOESTONE_PRESSURE_PLATE = add("floestone_pressure_plate", () -> new ParadiseLostPressurePlateBlock(BlockSetType.STONE, ofFullCopy(STONE_PRESSURE_PLATE).instrument(NoteBlockInstrument.BASEDRUM)));
    public static final RegistryObject<Block, OlvitePressurePlateBlock> OLVITE_PRESSURE_PLATE = add("olvite_pressure_plate", () -> new OlvitePressurePlateBlock(of().mapColor(MapColor.GRASS).forceSolidOn().instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY)));
    public static final RegistryObject<Block, FloatingBlock> LEVITATOR = add("levitator", () -> new FloatingBlock(true, of().strength(3f, 3f).sound(SoundType.STONE).instrument(NoteBlockInstrument.BASEDRUM)));
    public static final RegistryObject<Block, ChainBlock> OLVITE_CHAIN = add("olvite_chain", () -> new ChainBlock(ofFullCopy(CHAIN)), cutoutMippedRenderLayer);
    public static final RegistryObject<Block, CherineLanternBlock> CHERINE_LANTERN = add("cherine_lantern", () -> new CherineLanternBlock(ofFullCopy(LANTERN).explosionResistance(1f)), cutoutMippedRenderLayer);
    public static final RegistryObject<Block, ParadiseLostPortalBlock> BLUE_PORTAL = addNoItem("blue_portal", () -> new ParadiseLostPortalBlock(ofFullCopy(NETHER_PORTAL).noOcclusion().isViewBlocking(never).mapColor(MapColor.COLOR_BLUE)), translucentRenderLayer);

    private static Properties cherineTorch() {
        return ofFullCopy(TORCH).randomTicks().lightLevel(state -> 15);
    }

    public static final RegistryObject<Block, CherineTorchBlock> CHERINE_TORCH = addNoItem("cherine_torch", () -> new CherineTorchBlock(cherineTorch()), cutoutRenderLayer);
    public static final RegistryObject<Block, CherineWallTorchBlock> CHERINE_TORCH_WALL = addNoItem("cherine_wall_torch", () -> new CherineWallTorchBlock(ofFullCopy(CHERINE_TORCH.get())), cutoutRenderLayer);
    public static final RegistryObject<Block, GoldenAmberBarsBlock> GOLDEN_AMBER_BARS = add("golden_amber_bars", () -> new GoldenAmberBarsBlock(ofFullCopy(IRON_BARS)), cutoutMippedRenderLayer);

    public static final RegistryObject<Block, BrushableBlock> SUSPICIOUS_DIRT = add("suspicious_dirt", () -> new BrushableBlock(DIRT.get(), SoundEvents.BRUSH_GRAVEL, SoundEvents.BRUSH_GRAVEL_COMPLETED, ofFullCopy(DIRT.get()).strength(0.25F).sound(SoundType.SUSPICIOUS_GRAVEL).pushReaction(PushReaction.DESTROY)));
    public static final RegistryObject<Block, IncubatorBlock> INCUBATOR = add("incubator", () -> new IncubatorBlock(of().mapColor(MapColor.CRIMSON_NYLIUM).strength(2.5f).sound(SoundType.WOOD).noOcclusion()), List.of(fuel(300)), cutoutMippedRenderLayer);
    public static final RegistryObject<Block, IncubatorBlock> NEST = add("nest", () -> new IncubatorBlock(of().mapColor(MapColor.SAND).strength(0.3f).sound(SoundType.GRASS).noOcclusion(), 0.35F), List.of(fuel(300)), cutoutMippedRenderLayer);
    public static final RegistryObject<Block, FoodBowlBlock> FOOD_BOWL = add("food_bowl", () -> new FoodBowlBlock(of().mapColor(MapColor.CRIMSON_NYLIUM).strength(2.5f).sound(SoundType.WOOD).noOcclusion()), List.of(fuel(300)), cutoutMippedRenderLayer);
	public static final RegistryObject<Block, Block> TREE_TAP = add("tree_tap", () -> new TreeTapBlock(of().mapColor(MapColor.PODZOL).strength(2.5f).sound(SoundType.WOOD).noOcclusion().randomTicks()), List.of(fuel(300)), cutoutRenderLayer);
    public static final RegistryObject<Block, NitraBlock> NITRA_BUNCH = add("nitra_bunch", () -> new NitraBlock(of().mapColor(MapColor.SAND).strength(0.5f).sound(SoundType.WET_GRASS)), List.of(fuel(3200)));
    public static final RegistryObject<Block, LevitaRailBlock> LEVITA_RAIL = add("levita_rail", () -> new LevitaRailBlock(of().noCollission().strength(0.7F).sound(SoundType.METAL)), cutoutMippedRenderLayer);

    public static void init() {
        PENDING_BLOCK_ITEMS.forEach(Runnable::run);
        PENDING_BLOCK_ITEMS.clear();
    }

}
