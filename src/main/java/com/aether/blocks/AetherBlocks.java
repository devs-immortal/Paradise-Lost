package com.aether.blocks;

import com.aether.Aether;
import com.aether.blocks.natural.AetherGrassBlock;
import com.aether.blocks.natural.EnchantedAetherGrassBlock;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.block.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
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

    public static final Block SKYROOT_LOG = createLogBlock(MaterialColor.WOOD, MaterialColor.SPRUCE);
    public static final Block SKYROOT_PLANK = new Block(AbstractBlock.Settings.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 3.0F).sounds(BlockSoundGroup.WOOD));

    public static final Block HOLYSTONE = new Block(AbstractBlock.Settings.of(Material.STONE, MaterialColor.STONE).requiresTool().strength(1.5F, 6.0F));
    public static final Block HOLYSTONE_BRICK = new Block(AbstractBlock.Settings.of(Material.STONE).requiresTool().strength(1.5F, 6.0F));
    public static final Block MOSSY_HOLYSTONE = new Block(AbstractBlock.Settings.of(Material.STONE).requiresTool().strength(2.0F, 6.0F));

    public static void initialization() {
        // Blocks (Soil)
        Registry.register(Registry.BLOCK, new Identifier(Aether.MODID, "blue_portal"), BLUE_PORTAL);
        Registry.register(Registry.BLOCK, new Identifier(Aether.MODID, "aether_dirt"), AETHER_DIRT);
        Registry.register(Registry.BLOCK, new Identifier(Aether.MODID, "aether_grass"), AETHER_GRASS);
        Registry.register(Registry.BLOCK, new Identifier(Aether.MODID, "enchanted_aether_grass"), AETHER_ENCHANTED_GRASS);

        // Blocks (Stone)
        Registry.register(Registry.BLOCK, new Identifier(Aether.MODID, "holystone"), HOLYSTONE);
        Registry.register(Registry.BLOCK, new Identifier(Aether.MODID, "holystone_brick"), HOLYSTONE_BRICK);
        Registry.register(Registry.BLOCK, new Identifier(Aether.MODID, "mossy_holystone"), MOSSY_HOLYSTONE);

        // Blocks (Wood)
        Registry.register(Registry.BLOCK, new Identifier(Aether.MODID, "skyroot_log"), SKYROOT_LOG);
        Registry.register(Registry.BLOCK, new Identifier(Aether.MODID, "skyroot_plank"), SKYROOT_PLANK);
    }

    private static PillarBlock createLogBlock(MaterialColor topMaterialColor, MaterialColor sideMaterialColor) {
        return new PillarBlock(AbstractBlock.Settings.of(Material.WOOD, (blockState) -> blockState.get(PillarBlock.AXIS) == Direction.Axis.Y ? topMaterialColor : sideMaterialColor).strength(2.0F).sounds(BlockSoundGroup.WOOD));
    }

    public static void clientInitialization() {
        BlockRenderLayerMap.INSTANCE.putBlock(BLUE_PORTAL, RenderLayer.getTranslucent());
    }
}
