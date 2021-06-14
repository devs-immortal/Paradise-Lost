package com.aether.registry;

public class AetherAPIRegistry {
/*
    public static void register() {
        AetherAPI registry = AetherAPI.instance();

        registry.register(new AetherEnchantment(AetherItems.SKYROOT_PICKAXE, 225));
        registry.register(new AetherEnchantment(AetherItems.SKYROOT_AXE, 225));
        registry.register(new AetherEnchantment(AetherItems.SKYROOT_SHOVEL, 225));
        registry.register(new AetherEnchantment(AetherItems.SKYROOT_SWORD, 225));

        registry.register(new AetherEnchantment(AetherItems.HOLYSTONE_PICKAXE, 550));
        registry.register(new AetherEnchantment(AetherItems.HOLYSTONE_AXE, 550));
        registry.register(new AetherEnchantment(AetherItems.HOLYSTONE_SHOVEL, 550));
        registry.register(new AetherEnchantment(AetherItems.HOLYSTONE_SWORD, 550));

        registry.register(new AetherEnchantment(AetherItems.ZANITE_PICKAXE, 2250));
        registry.register(new AetherEnchantment(AetherItems.ZANITE_AXE, 2250));
        registry.register(new AetherEnchantment(AetherItems.ZANITE_SHOVEL, 2250));
        registry.register(new AetherEnchantment(AetherItems.ZANITE_SWORD, 2250));

        registry.register(new AetherEnchantment(AetherItems.GRAVITITE_PICKAXE, 5500));
        registry.register(new AetherEnchantment(AetherItems.GRAVITITE_AXE, 5500));
        registry.register(new AetherEnchantment(AetherItems.GRAVITITE_SHOVEL, 5500));
        registry.register(new AetherEnchantment(AetherItems.GRAVITITE_SWORD, 5500));

        registry.register(new AetherEnchantment(AetherItems.ZANITE_HELMET, 6000));
        registry.register(new AetherEnchantment(AetherItems.ZANITE_CHESTPLATE, 6000));
        registry.register(new AetherEnchantment(AetherItems.ZANITE_LEGGINGS, 6000));
        registry.register(new AetherEnchantment(AetherItems.ZANITE_BOOTS, 6000));

        registry.register(new AetherEnchantment(AetherItems.GRAVITITE_HELMET, 13000));
        registry.register(new AetherEnchantment(AetherItems.GRAVITITE_CHESTPLATE, 13000));
        registry.register(new AetherEnchantment(AetherItems.GRAVITITE_LEGGINGS, 13000));
        registry.register(new AetherEnchantment(AetherItems.GRAVITITE_BOOTS, 13000));

        registry.register(new AetherEnchantment(AetherItems.GOLDEN_DART, AetherItems.ENCHANTED_DART, 250));
        registry.register(new AetherEnchantment(AetherItems.GOLDEN_DART_SHOOTER, AetherItems.ENCHANTED_DART_SHOOTER, 500));

        registry.register(new AetherEnchantment(AetherItems.SKYROOT_POISON_BUCKET, AetherItems.SKYROOT_REMEDY_BUCKET, 1000));

        registry.register(new AetherEnchantment(AetherBlocks.HOLYSTONE, AetherItems.HEALING_STONE, 750));
        registry.register(new AetherEnchantment(AetherBlocks.GRAVITITE_ORE, AetherBlocks.ENCHANTED_GRAVITITE, 1000));
        registry.register(new AetherEnchantment(AetherBlocks.QUICKSOIL, AetherBlocks.QUICKSOIL_GLASS, 250));

        registry.register(new AetherEnchantment(AetherItems.blueberry, AetherItems.enchanted_blueberry, 300));

        registry.register(new AetherEnchantment(Items.BOW, 4000));
        registry.register(new AetherEnchantment(Items.FISHING_ROD, 600));

        registry.register(new AetherEnchantment(Items.MUSIC_DISC_11, AetherItems.aether_tune, 2500));
        registry.register(new AetherEnchantment(Items.MUSIC_DISC_13, AetherItems.aether_tune, 2500));
        registry.register(new AetherEnchantment(Items.MUSIC_DISC_BLOCKS, AetherItems.aether_tune, 2500));
        registry.register(new AetherEnchantment(Items.MUSIC_DISC_CAT, AetherItems.aether_tune, 2500));
        registry.register(new AetherEnchantment(Items.MUSIC_DISC_FAR, AetherItems.aether_tune, 2500));
        registry.register(new AetherEnchantment(Items.MUSIC_DISC_MALL, AetherItems.aether_tune, 2500));
        registry.register(new AetherEnchantment(Items.MUSIC_DISC_MELLOHI, AetherItems.aether_tune, 2500));
        registry.register(new AetherEnchantment(Items.MUSIC_DISC_STAL, AetherItems.aether_tune, 2500));
        registry.register(new AetherEnchantment(Items.MUSIC_DISC_STRAD, AetherItems.aether_tune, 2500));
        registry.register(new AetherEnchantment(Items.MUSIC_DISC_WAIT, AetherItems.aether_tune, 2500));
        registry.register(new AetherEnchantment(Items.MUSIC_DISC_WARD, AetherItems.aether_tune, 2500));

        registry.register(new AetherEnchantment(Items.WOODEN_PICKAXE, 225));
        registry.register(new AetherEnchantment(Items.WOODEN_AXE, 225));
        registry.register(new AetherEnchantment(Items.WOODEN_SHOVEL, 225));
        registry.register(new AetherEnchantment(Items.WOODEN_HOE, 225));

        registry.register(new AetherEnchantment(Items.STONE_PICKAXE, 550));
        registry.register(new AetherEnchantment(Items.STONE_AXE, 550));
        registry.register(new AetherEnchantment(Items.STONE_SHOVEL, 550));
        registry.register(new AetherEnchantment(Items.STONE_HOE, 550));

        registry.register(new AetherEnchantment(Items.IRON_PICKAXE, 2250));
        registry.register(new AetherEnchantment(Items.IRON_AXE, 2250));
        registry.register(new AetherEnchantment(Items.IRON_SHOVEL, 2250));
        registry.register(new AetherEnchantment(Items.IRON_HOE, 2250));

        registry.register(new AetherEnchantment(Items.DIAMOND_PICKAXE, 5500));
        registry.register(new AetherEnchantment(Items.DIAMOND_AXE, 5500));
        registry.register(new AetherEnchantment(Items.DIAMOND_SHOVEL, 5500));
        registry.register(new AetherEnchantment(Items.DIAMOND_HOE, 5500));

        registry.register(new AetherEnchantment(Items.LEATHER_HELMET, 550));
        registry.register(new AetherEnchantment(Items.LEATHER_CHESTPLATE, 550));
        registry.register(new AetherEnchantment(Items.LEATHER_LEGGINGS, 550));
        registry.register(new AetherEnchantment(Items.LEATHER_BOOTS, 550));

        registry.register(new AetherEnchantment(Items.IRON_HELMET, 6000));
        registry.register(new AetherEnchantment(Items.IRON_CHESTPLATE, 6000));
        registry.register(new AetherEnchantment(Items.IRON_LEGGINGS, 6000));
        registry.register(new AetherEnchantment(Items.IRON_BOOTS, 6000));

        registry.register(new AetherEnchantment(Items.GOLDEN_HELMET, 2250));
        registry.register(new AetherEnchantment(Items.GOLDEN_CHESTPLATE, 2250));
        registry.register(new AetherEnchantment(Items.GOLDEN_LEGGINGS, 2250));
        registry.register(new AetherEnchantment(Items.GOLDEN_BOOTS, 2250));

        registry.register(new AetherEnchantment(Items.CHAINMAIL_HELMET, 2250));
        registry.register(new AetherEnchantment(Items.CHAINMAIL_CHESTPLATE, 2250));
        registry.register(new AetherEnchantment(Items.CHAINMAIL_LEGGINGS, 2250));
        registry.register(new AetherEnchantment(Items.CHAINMAIL_BOOTS, 2250));

        registry.register(new AetherEnchantment(Items.DIAMOND_HELMET, 10000));
        registry.register(new AetherEnchantment(Items.DIAMOND_CHESTPLATE, 10000));
        registry.register(new AetherEnchantment(Items.DIAMOND_LEGGINGS, 10000));
        registry.register(new AetherEnchantment(Items.DIAMOND_BOOTS, 10000));

        registry.register(new AetherEnchantmentFuel(AetherItems.AMBROSIUM_SHARD, 500));

        registry.register(new AetherFreezable(AetherBlocks.COLD_AERCLOUD, AetherBlocks.BLUE_AERCLOUD, 100));
        registry.register(new AetherFreezable(AetherBlocks.SKYROOT_LEAVES, AetherBlocks.CRYSTAL_LEAVES, 150));
        registry.register(new AetherFreezable(AetherItems.SKYROOT_BUCKET, Blocks.ICE, 500));
        registry.register(new AetherFreezable(AetherItems.ASCENDING_DAWN, AetherItems.WELCOMING_SKIES, 800));
        registry.register(new AetherFreezable(Blocks.ICE, Blocks.PACKED_ICE, 750));
        registry.register(new AetherFreezable(Items.WATER_BUCKET, Blocks.ICE, 500));
        registry.register(new AetherFreezable(Items.LAVA_BUCKET, Blocks.OBSIDIAN, 500));
        registry.register(new AetherFreezable(AetherItems.IRON_RING, AetherItems.ICE_RING, 2500));
        registry.register(new AetherFreezable(AetherItems.GOLDEN_RING, AetherItems.ICE_RING, 2500));
        registry.register(new AetherFreezable(AetherItems.IRON_PENDANT, AetherItems.ICE_PENDANT, 2500));
        registry.register(new AetherFreezable(AetherItems.GOLDEN_PENDANT, AetherItems.ICE_PENDANT, 2500));

        registry.register(new AetherFreezableFuel(AetherBlocks.icestone, 500));

    }

 */
}