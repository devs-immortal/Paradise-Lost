package com.aether.blocks;

import com.aether.Aether;
import com.aether.blocks.natural.AetherGrassBlock;
import com.aether.blocks.natural.EnchantedAetherGrassBlock;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AetherBlocks {
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
    }

    public static void clientInitialization() {
        BlockRenderLayerMap.INSTANCE.putBlock(BLUE_PORTAL, RenderLayer.getTranslucent());
    }
}
