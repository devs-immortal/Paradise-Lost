package com.aether.blocks;

import com.aether.Aether;
import com.aether.blocks.natural.AetherGrassBlock;
import com.aether.blocks.natural.EnchantedAetherGrassBlock;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

public class AetherBlocks {

    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(
            new Identifier(Aether.MODID, "aether_blocks"),
            () -> new ItemStack(AetherBlocks.AETHER_GRASS));

    public static final Block BLUE_PORTAL = new PortalBlock(
            AbstractBlock.Settings.of(
                    Material.PORTAL, MaterialColor.BLUE)
                    .nonOpaque()
                    .noCollision()
                    .ticksRandomly()
                    .dropsNothing()
                    .blockVision(PortalBlock::never)
                    .strength(-1.0F)
                    .sounds(BlockSoundGroup.GLASS)
                    .luminance((state) -> 11));
    public static final Block AETHER_DIRT = new Block(AbstractBlock.Settings.of(Material.SOIL, MaterialColor.DIRT).strength(0.3F).sounds(BlockSoundGroup.GRAVEL));
    public static final Block AETHER_GRASS = new AetherGrassBlock(AbstractBlock.Settings.of(Material.SOLID_ORGANIC));
    public static final Block AETHER_ENCHANTED_GRASS = new EnchantedAetherGrassBlock(AbstractBlock.Settings.of(Material.SOLID_ORGANIC));

    public static void initialization() {
        // Blocks
        Registry.register(Registry.BLOCK, new Identifier(Aether.MODID, "blue_portal"), BLUE_PORTAL);
        Registry.register(Registry.BLOCK, new Identifier(Aether.MODID, "aether_dirt"), AETHER_DIRT);
        Registry.register(Registry.BLOCK, new Identifier(Aether.MODID, "aether_grass"), AETHER_GRASS);
        Registry.register(Registry.BLOCK, new Identifier(Aether.MODID, "enchanted_aether_grass"), AETHER_ENCHANTED_GRASS);

        // Items
        Registry.register(Registry.ITEM, new Identifier(Aether.MODID, "blue_portal"), new BlockItem(BLUE_PORTAL, new Item.Settings().group(null)));
        Registry.register(Registry.ITEM, new Identifier(Aether.MODID, "aether_dirt"), new BlockItem(AETHER_DIRT, new Item.Settings().group(ITEM_GROUP)));
        Registry.register(Registry.ITEM, new Identifier(Aether.MODID, "aether_grass"), new BlockItem(AETHER_GRASS, new Item.Settings().group(ITEM_GROUP)));
        Registry.register(Registry.ITEM, new Identifier(Aether.MODID, "enchanted_aether_grass"), new BlockItem(AETHER_ENCHANTED_GRASS, new Item.Settings().group(ITEM_GROUP).rarity(Rarity.UNCOMMON)));
    }

    public static void clientInitialization() {
        BlockRenderLayerMap.INSTANCE.putBlock(BLUE_PORTAL, RenderLayer.getTranslucent());
    }
}
