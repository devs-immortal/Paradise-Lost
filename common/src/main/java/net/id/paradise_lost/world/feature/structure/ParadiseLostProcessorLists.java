package net.id.paradise_lost.world.feature.structure;

import net.id.paradise_lost.registry.BlockRegistry;
import net.id.paradise_lost.tag.ParadiseLostBlockTags;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.AlwaysTrueTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.ProcessorRule;
import net.minecraft.world.level.levelgen.structure.templatesystem.ProtectedBlockProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.RandomBlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

import java.util.List;

import static net.id.paradise_lost.ModConstants.id;

public final class ParadiseLostProcessorLists {
    private ParadiseLostProcessorLists() {}

    public static final ResourceKey<StructureProcessorList> BIRDCAGE_PROC = of("birdcage_proc");
    public static final ResourceKey<StructureProcessorList> PALACE_DISSOLVE_PROC = of("palace_dissolve_proc");
    public static final ResourceKey<StructureProcessorList> VAULT_PROC = of("vault_proc");

    public static ResourceKey<StructureProcessorList> of(String name) {
        return ResourceKey.create(Registries.PROCESSOR_LIST, id(name));
    }

    public static void bootstrap(BootstrapContext<StructureProcessorList> context) {
        context.register(BIRDCAGE_PROC, new StructureProcessorList(List.of(new RuleProcessor(List.of(new ProcessorRule(new RandomBlockMatchTest(BlockRegistry.LEVITA_BRICK_SET.block().get(), 0.15f), AlwaysTrueTest.INSTANCE, BlockRegistry.LEVITA.get().defaultBlockState()), new ProcessorRule(new RandomBlockMatchTest(BlockRegistry.COBBLED_FLOESTONE.get(), 0.3f), AlwaysTrueTest.INSTANCE, BlockRegistry.MOSSY_FLOESTONE.get().defaultBlockState()), new ProcessorRule(new RandomBlockMatchTest(BlockRegistry.CHERINE_LANTERN.get(), 0.85f), AlwaysTrueTest.INSTANCE, Blocks.AIR.defaultBlockState()))), new ProtectedBlockProcessor(ParadiseLostBlockTags.STRUCTURES_AVOID))));
        context.register(PALACE_DISSOLVE_PROC, new StructureProcessorList(List.of(new RuleProcessor(List.of(new ProcessorRule(new RandomBlockMatchTest(Blocks.RED_WOOL, 0.95f), AlwaysTrueTest.INSTANCE, BlockRegistry.FLOESTONE_BRICK.get().defaultBlockState()), new ProcessorRule(new BlockMatchTest(Blocks.RED_WOOL), AlwaysTrueTest.INSTANCE, Blocks.AIR.defaultBlockState()))), new RuleProcessor(List.of(new ProcessorRule(new RandomBlockMatchTest(Blocks.ORANGE_WOOL, 0.89f), AlwaysTrueTest.INSTANCE, BlockRegistry.FLOESTONE_BRICK.get().defaultBlockState()), new ProcessorRule(new BlockMatchTest(Blocks.ORANGE_WOOL), AlwaysTrueTest.INSTANCE, Blocks.AIR.defaultBlockState()))), new RuleProcessor(List.of(new ProcessorRule(new RandomBlockMatchTest(Blocks.YELLOW_WOOL, 0.71f), AlwaysTrueTest.INSTANCE, BlockRegistry.FLOESTONE_BRICK.get().defaultBlockState()), new ProcessorRule(new RandomBlockMatchTest(Blocks.YELLOW_WOOL, 0.1f), AlwaysTrueTest.INSTANCE, BlockRegistry.FLOESTONE.get().defaultBlockState()), new ProcessorRule(new BlockMatchTest(Blocks.YELLOW_WOOL), AlwaysTrueTest.INSTANCE, Blocks.AIR.defaultBlockState()))), new RuleProcessor(List.of(new ProcessorRule(new RandomBlockMatchTest(Blocks.LIME_WOOL, 0.35f), AlwaysTrueTest.INSTANCE, BlockRegistry.FLOESTONE_BRICK.get().defaultBlockState()), new ProcessorRule(new RandomBlockMatchTest(Blocks.LIME_WOOL, 0.34f), AlwaysTrueTest.INSTANCE, BlockRegistry.FLOESTONE.get().defaultBlockState()), new ProcessorRule(new BlockMatchTest(Blocks.LIME_WOOL), AlwaysTrueTest.INSTANCE, Blocks.AIR.defaultBlockState()))), new RuleProcessor(List.of(new ProcessorRule(new RandomBlockMatchTest(Blocks.GREEN_WOOL, 0.28f), AlwaysTrueTest.INSTANCE, BlockRegistry.FLOESTONE_BRICK.get().defaultBlockState()), new ProcessorRule(new RandomBlockMatchTest(Blocks.GREEN_WOOL, 0.28f), AlwaysTrueTest.INSTANCE, BlockRegistry.FLOESTONE.get().defaultBlockState()), new ProcessorRule(new BlockMatchTest(Blocks.GREEN_WOOL), AlwaysTrueTest.INSTANCE, Blocks.AIR.defaultBlockState()))), new RuleProcessor(List.of(new ProcessorRule(new RandomBlockMatchTest(Blocks.CYAN_WOOL, 0.21f), AlwaysTrueTest.INSTANCE, BlockRegistry.FLOESTONE_BRICK.get().defaultBlockState()), new ProcessorRule(new RandomBlockMatchTest(Blocks.CYAN_WOOL, 0.2f), AlwaysTrueTest.INSTANCE, BlockRegistry.FLOESTONE.get().defaultBlockState()), new ProcessorRule(new BlockMatchTest(Blocks.CYAN_WOOL), AlwaysTrueTest.INSTANCE, Blocks.AIR.defaultBlockState()))), new RuleProcessor(List.of(new ProcessorRule(new RandomBlockMatchTest(Blocks.LIGHT_BLUE_WOOL, 0.11f), AlwaysTrueTest.INSTANCE, BlockRegistry.FLOESTONE_BRICK.get().defaultBlockState()), new ProcessorRule(new RandomBlockMatchTest(Blocks.LIGHT_BLUE_WOOL, 0.11f), AlwaysTrueTest.INSTANCE, BlockRegistry.FLOESTONE.get().defaultBlockState()), new ProcessorRule(new RandomBlockMatchTest(Blocks.LIGHT_BLUE_WOOL, 0.07f), AlwaysTrueTest.INSTANCE, BlockRegistry.COBBLED_FLOESTONE.get().defaultBlockState()), new ProcessorRule(new BlockMatchTest(Blocks.LIGHT_BLUE_WOOL), AlwaysTrueTest.INSTANCE, Blocks.AIR.defaultBlockState()))), new RuleProcessor(List.of(new ProcessorRule(new RandomBlockMatchTest(Blocks.BLUE_WOOL, 0.1f), AlwaysTrueTest.INSTANCE, BlockRegistry.FLOESTONE.get().defaultBlockState()), new ProcessorRule(new RandomBlockMatchTest(Blocks.BLUE_WOOL, 0.1f), AlwaysTrueTest.INSTANCE, BlockRegistry.COBBLED_FLOESTONE.get().defaultBlockState()), new ProcessorRule(new BlockMatchTest(Blocks.BLUE_WOOL), AlwaysTrueTest.INSTANCE, Blocks.AIR.defaultBlockState()))), new RuleProcessor(List.of(new ProcessorRule(new RandomBlockMatchTest(Blocks.PURPLE_WOOL, 0.05f), AlwaysTrueTest.INSTANCE, BlockRegistry.FLOESTONE.get().defaultBlockState()), new ProcessorRule(new RandomBlockMatchTest(Blocks.PURPLE_WOOL, 0.05f), AlwaysTrueTest.INSTANCE, BlockRegistry.COBBLED_FLOESTONE.get().defaultBlockState()), new ProcessorRule(new BlockMatchTest(Blocks.PURPLE_WOOL), AlwaysTrueTest.INSTANCE, Blocks.AIR.defaultBlockState()))), new RuleProcessor(List.of(new ProcessorRule(new RandomBlockMatchTest(Blocks.MAGENTA_WOOL, 0.02f), AlwaysTrueTest.INSTANCE, BlockRegistry.FLOESTONE.get().defaultBlockState()), new ProcessorRule(new RandomBlockMatchTest(Blocks.MAGENTA_WOOL, 0.03f), AlwaysTrueTest.INSTANCE, BlockRegistry.COBBLED_FLOESTONE.get().defaultBlockState()), new ProcessorRule(new BlockMatchTest(Blocks.MAGENTA_WOOL), AlwaysTrueTest.INSTANCE, Blocks.AIR.defaultBlockState()))))));
        context.register(VAULT_PROC, new StructureProcessorList(List.of()));
    }
}
