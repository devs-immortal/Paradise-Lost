package net.id.paradise_lost.test.server;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Pair;
import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.test.Step;
import net.id.paradise_lost.world.feature.structure.ParadiseLostProcessorLists;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasLookup;
import net.minecraft.world.level.levelgen.structure.pools.DimensionPadding;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static net.id.paradise_lost.test.TestHelpers.*;
import static net.id.paradise_lost.test.server.ServerTestHelpers.ahead;
import static net.id.paradise_lost.test.server.ServerTestHelpers.level;

public final class BirdcageTests {
    private static final ResourceLocation SPAWNER_PIECE = ModConstants.locate("birdcage/spawner");
    private static final String MARKER = ModConstants.locate("spawner_marker").toString();

    private BirdcageTests() {
    }

    private static List<ResourceLocation> pieces() {
        List<ResourceLocation> pieces = new ArrayList<>();
        ServerTestRunner.server().getResourceManager()
                .listResources("structure/birdcage", id -> id.getNamespace().equals(ModConstants.MOD_ID) && id.getPath().endsWith(".nbt"))
                .keySet()
                .forEach(id -> pieces.add(ResourceLocation.fromNamespaceAndPath(id.getNamespace(),
                        id.getPath().substring("structure/".length(), id.getPath().length() - ".nbt".length()))));
        return pieces;
    }

    private static StructureTemplate template(ResourceLocation id) {
        return ServerTestRunner.server().getStructureManager().get(id).orElseThrow(() -> new AssertionError("no template " + id));
    }

    private static Map<BlockPos, BlockState> blocks(StructureTemplate template, List<BlockPos> jigsawsOut, Map<BlockPos, CompoundTag> jigsawNbt) {
        CompoundTag tag = template.save(new CompoundTag());
        ListTag palette = tag.getList("palette", Tag.TAG_COMPOUND);
        List<BlockState> states = new ArrayList<>();
        for (int i = 0; i < palette.size(); i++) {
            states.add(NbtUtils.readBlockState(BuiltInRegistries.BLOCK.asLookup(), palette.getCompound(i)));
        }
        Map<BlockPos, BlockState> blocks = new HashMap<>();
        for (Tag entry : tag.getList("blocks", Tag.TAG_COMPOUND)) {
            CompoundTag block = (CompoundTag) entry;
            ListTag pos = block.getList("pos", Tag.TAG_INT);
            BlockPos at = new BlockPos(pos.getInt(0), pos.getInt(1), pos.getInt(2));
            BlockState state = states.get(block.getInt("state"));
            blocks.put(at, state);
            if (state.is(Blocks.JIGSAW)) {
                jigsawsOut.add(at);
                jigsawNbt.put(at, block.getCompound("nbt"));
            }
        }
        return blocks;
    }

    @ServerTest("every birdcage trial spawner comes from a spawner marker")
    private static Step spawnersComeFromMarkers() {
        return Step.run(0, () -> {
            List<String> wrong = new ArrayList<>();
            List<ResourceLocation> pieces = pieces();
            expect(wrong, pieces.size() > 70, "only found " + pieces.size() + " birdcage pieces");
            int markers = 0;
            for (ResourceLocation id : pieces) {
                List<BlockPos> jigsaws = new ArrayList<>();
                Map<BlockPos, CompoundTag> nbt = new HashMap<>();
                Map<BlockPos, BlockState> blocks = blocks(template(id), jigsaws, nbt);
                if (!id.equals(SPAWNER_PIECE)) {
                    blocks.forEach((pos, state) -> expect(wrong, !state.is(Blocks.TRIAL_SPAWNER), id + " has a trial spawner at " + pos.toShortString()));
                }
                for (BlockPos jigsaw : jigsaws) {
                    if (nbt.get(jigsaw).getString("name").equals(MARKER)) markers++;
                }
            }
            expect(wrong, markers >= 12, "only found " + markers + " spawner markers");
            checkAll(wrong);
        });
    }

    @ServerTest("every spawner marker fills its own cell with a solid block and has room in front for the spawner piece")
    private static Step markersFillTheirCell() {
        return Step.run(0, () -> {
            List<String> wrong = new ArrayList<>();
            for (ResourceLocation id : pieces()) {
                List<BlockPos> jigsaws = new ArrayList<>();
                Map<BlockPos, CompoundTag> nbt = new HashMap<>();
                Map<BlockPos, BlockState> blocks = blocks(template(id), jigsaws, nbt);
                for (BlockPos marker : jigsaws) {
                    CompoundTag tag = nbt.get(marker);
                    if (!tag.getString("name").equals(MARKER)) continue;
                    String where = id + " marker " + marker.toShortString();

                    BlockState finalState;
                    try {
                        finalState = BlockStateParser.parseForBlock(BuiltInRegistries.BLOCK.asLookup(), tag.getString("final_state"), false).blockState();
                    } catch (CommandSyntaxException e) {
                        wrong.add(where + " has the unreadable final state " + tag.getString("final_state"));
                        continue;
                    }
                    expect(wrong, !finalState.isAir(), where + " turns into " + finalState.getBlock());

                    BlockPos front = marker.relative(JigsawBlock.getFrontFacing(blocks.get(marker)));
                    BlockState inFront = blocks.get(front);
                    expect(wrong, inFront != null && inFront.isAir(), where + " has " + (inFront == null ? "nothing" : inFront.getBlock()) + " in front at " + front.toShortString());
                }
            }
            checkAll(wrong);
        });
    }

    @ServerTest("jigsaw attaches the spawner piece onto the cell of every spawner marker")
    private static Step spawnerPieceLandsOnMarker() {
        return Step.run(0, () -> {
            ServerLevel level = level();
            var generator = level.getChunkSource().getGenerator();
            var templates = level.getStructureManager();
            Holder<StructureTemplatePool> fallback = level.registryAccess().registryOrThrow(Registries.TEMPLATE_POOL).getHolderOrThrow(Pools.EMPTY);

            List<String> wrong = new ArrayList<>();
            int checked = 0;
            for (ResourceLocation id : pieces()) {
                List<BlockPos> jigsaws = new ArrayList<>();
                blocks(template(id), jigsaws, new HashMap<>());
                if (jigsaws.isEmpty()) continue;
                StructureTemplatePool pool = new StructureTemplatePool(fallback,
                        List.of(Pair.of(StructurePoolElement.single(id.toString()), 1)), StructureTemplatePool.Projection.RIGID);
                for (int seed = 0; seed < 4; seed++) {
                    var context = new Structure.GenerationContext(level.registryAccess(), generator, generator.getBiomeSource(),
                            level.getChunkSource().randomState(), templates, seed, new ChunkPos(seed, seed), level, biome -> true);
                    var stub = JigsawPlacement.addPieces(context, Holder.direct(pool), Optional.empty(), 1, new BlockPos(seed * 16, 64, seed * 16),
                            false, Optional.empty(), 80, PoolAliasLookup.EMPTY, DimensionPadding.ZERO, LiquidSettings.IGNORE_WATERLOGGING);
                    if (stub.isEmpty()) {
                        wrong.add(id + " produced no pieces");
                        continue;
                    }
                    List<StructurePiece> pieces = stub.get().getPiecesBuilder().build().pieces();
                    PoolElementStructurePiece start = (PoolElementStructurePiece) pieces.getFirst();
                    for (StructureTemplate.StructureBlockInfo info : start.getElement()
                            .getShuffledJigsawBlocks(templates, start.getPosition(), start.getRotation(), RandomSource.create(seed))) {
                        if (info.nbt() == null || !info.nbt().getString("name").equals(MARKER)) continue;
                        checked++;
                        BlockPos marker = info.pos();
                        BlockPos front = marker.relative(JigsawBlock.getFrontFacing(info.state()));
                        boolean attached = pieces.stream().skip(1).anyMatch(piece -> {
                            BoundingBox box = piece.getBoundingBox();
                            return box.getXSpan() * box.getYSpan() * box.getZSpan() == 2 && box.isInside(marker) && box.isInside(front);
                        });
                        expect(wrong, attached, id + " (seed " + seed + ") got no spawner piece on its marker at " + marker.toShortString());
                    }
                }
            }
            expect(wrong, checked >= 12 * 4, "only checked " + checked + " markers");
            checkAll(wrong);
        });
    }
    private static StructurePlaceSettings spawnerSettings() {
        StructureProcessorList processors = ServerTestRunner.server().registryAccess().registryOrThrow(Registries.PROCESSOR_LIST)
                .getOrThrow(ParadiseLostProcessorLists.BIRDCAGE_SPAWNER_PROC);
        StructurePlaceSettings settings = new StructurePlaceSettings().setIgnoreEntities(true);
        processors.list().forEach(settings::addProcessor);
        return settings;
    }

    private static boolean placeSpawner(ServerLevel level, BlockPos spawnerAt) {
        return template(SPAWNER_PIECE).placeInWorld(level, spawnerAt, spawnerAt, spawnerSettings(), RandomSource.create(1), 2);
    }

    private static void clear(ServerLevel level, BlockPos spawnerAt) {
        for (BlockPos pos : List.of(spawnerAt, spawnerAt.east(), spawnerAt.below())) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
        }
    }

    @ServerTest("spawner generates where it replaces a solid block")
    private static Step spawnerReplacesSolid() {
        return Step.run(0, () -> {
            ServerLevel level = level();
            BlockPos cell = ahead(2, 0).above(10);
            level.setBlock(cell, Blocks.STONE.defaultBlockState(), 2);
            check(placeSpawner(level, cell), "the spawner piece was not placed");
            BlockState placed = level.getBlockState(cell);
            clear(level, cell);
            check(placed.is(Blocks.TRIAL_SPAWNER), "the cell holds " + placed.getBlock() + " instead of a trial spawner");
        });
    }

    @ServerTest("spawner is skipped where it would replace air")
    private static Step spawnerSkippedInAir() {
        return Step.run(0, () -> {
            ServerLevel level = level();
            BlockPos cell = ahead(2, 0).above(10);
            level.setBlock(cell, Blocks.AIR.defaultBlockState(), 2);
            level.setBlock(cell.below(), Blocks.STONE.defaultBlockState(), 2);
            check(placeSpawner(level, cell), "the spawner piece was not placed");
            BlockState placed = level.getBlockState(cell);
            clear(level, cell);
            check(placed.isAir(), "the cell holds " + placed.getBlock() + " even though it replaced air");
        });
    }
}
