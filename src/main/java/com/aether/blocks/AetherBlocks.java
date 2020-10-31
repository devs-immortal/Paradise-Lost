package com.aether.blocks;

import com.aether.Aether;
import com.aether.blocks.natural.AetherGrassBlock;
import com.aether.blocks.natural.EnchantedAetherGrassBlock;
import com.aether.items.AetherItemGroups;
import com.aether.items.AetherItems;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;

public class AetherBlocks {

    public static final Block BLUE_PORTAL;

    public static final Block AETHER_DIRT;
    public static final Block AETHER_GRASS;
    public static final Block AETHER_ENCHANTED_GRASS;

    public static final Block SKYROOT_LOG;
    public static final Block SKYROOT_PLANK;

    public static final Block HOLYSTONE;
    public static final Block HOLYSTONE_BRICK;
    public static final Block MOSSY_HOLYSTONE;

    public static final Block ZANITE_ORE;
    public static final Block AMBROSIUM_ORE;

    static {
        BLUE_PORTAL = register("blue_portal", new PortalBlock(AbstractBlock.Settings.of(Material.PORTAL, MaterialColor.BLUE).nonOpaque().noCollision().ticksRandomly().dropsNothing().blockVision(PortalBlock::never).strength(-1.0F).sounds(BlockSoundGroup.GLASS).luminance((state) -> 11)));

        AETHER_DIRT = register("aether_dirt", new Block(FabricBlockSettings.copy(Blocks.DIRT)), buildingBlock());
        AETHER_GRASS = register("aether_grass", new AetherGrassBlock(FabricBlockSettings.copyOf(Blocks.GRASS_BLOCK).materialColor(MaterialColor.CYAN)), buildingBlock());
        AETHER_ENCHANTED_GRASS = register("enchanted_aether_grass", new EnchantedAetherGrassBlock(FabricBlockSettings.copyOf(AETHER_GRASS).materialColor(MaterialColor.GOLD)), buildingBlock());

        SKYROOT_LOG = register("skyroot_log", createLogBlock(MaterialColor.WOOD, MaterialColor.GREEN), buildingBlock());
        SKYROOT_PLANK = register("skyroot_plank", new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS)), buildingBlock());

        HOLYSTONE = register("holystone", new Block(FabricBlockSettings.copyOf(Blocks.STONE).materialColor(MaterialColor.WHITE)), buildingBlock());
        HOLYSTONE_BRICK = register("holystone_brick", new Block(FabricBlockSettings.copyOf(Blocks.STONE_BRICKS).materialColor(MaterialColor.WHITE)), buildingBlock());
        MOSSY_HOLYSTONE = register("mossy_holystone", new Block(FabricBlockSettings.copyOf(Blocks.MOSSY_COBBLESTONE).materialColor(MaterialColor.WHITE)), buildingBlock());

        ZANITE_ORE = register("zanite_ore", new Block(FabricBlockSettings.copyOf(Blocks.IRON_ORE)), buildingBlock());
        AMBROSIUM_ORE = register("ambrosium_ore", new Block(FabricBlockSettings.copyOf(Blocks.COAL_ORE)), buildingBlock());
    }

    private static Item.Settings buildingBlock() {
        return new FabricItemSettings().group(AetherItemGroups.BLOCKS);
    }

    private static Block register(String id, Block block, Item.Settings settings) {
        Identifier trueId = new Identifier(Aether.MODID, id);
        Registry.register(Registry.BLOCK, trueId, block);
        Registry.register(Registry.ITEM, trueId, new BlockItem(block, settings));
        return block;
    }
    private static Block register(String id, Block block) {
        return Registry.register(Registry.BLOCK, new Identifier(Aether.MODID, id), block);
    }

    private static PillarBlock createLogBlock(MaterialColor topMaterialColor, MaterialColor sideMaterialColor) {
        return new PillarBlock(AbstractBlock.Settings.of(Material.WOOD, (blockState) -> blockState.get(PillarBlock.AXIS) == Direction.Axis.Y ? topMaterialColor : sideMaterialColor).strength(2.0F).sounds(BlockSoundGroup.WOOD));
    }

    public static void clientInitialization() {
        BlockRenderLayerMap.INSTANCE.putBlock(BLUE_PORTAL, RenderLayer.getTranslucent());
    }
}
