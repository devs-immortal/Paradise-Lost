package com.aether.registry;

import com.aether.api.AetherAPI;
import com.aether.api.enchantments.AetherEnchantment;
import com.aether.api.enchantments.AetherEnchantmentFuel;
import com.aether.api.freezables.AetherFreezable;
import com.aether.api.freezables.AetherFreezableFuel;
import com.aether.blocks.AetherBlocks;
import com.aether.items.AetherItems;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;

public class AetherAPIRegistry {

    public static void register() {
        AetherAPI registry = AetherAPI.instance();

        registry.register(new AetherEnchantment(AetherItems.skyroot_pickaxe, 225));
        registry.register(new AetherEnchantment(AetherItems.skyroot_axe, 225));
        registry.register(new AetherEnchantment(AetherItems.skyroot_shovel, 225));
        registry.register(new AetherEnchantment(AetherItems.skyroot_sword, 225));

        registry.register(new AetherEnchantment(AetherItems.holystone_pickaxe, 550));
        registry.register(new AetherEnchantment(AetherItems.holystone_axe, 550));
        registry.register(new AetherEnchantment(AetherItems.holystone_shovel, 550));
        registry.register(new AetherEnchantment(AetherItems.holystone_sword, 550));

        registry.register(new AetherEnchantment(AetherItems.zanite_pickaxe, 2250));
        registry.register(new AetherEnchantment(AetherItems.zanite_axe, 2250));
        registry.register(new AetherEnchantment(AetherItems.zanite_shovel, 2250));
        registry.register(new AetherEnchantment(AetherItems.zanite_sword, 2250));

        registry.register(new AetherEnchantment(AetherItems.gravitite_pickaxe, 5500));
        registry.register(new AetherEnchantment(AetherItems.gravitite_axe, 5500));
        registry.register(new AetherEnchantment(AetherItems.gravitite_shovel, 5500));
        registry.register(new AetherEnchantment(AetherItems.gravitite_sword, 5500));

        registry.register(new AetherEnchantment(AetherItems.zanite_helmet, 6000));
        registry.register(new AetherEnchantment(AetherItems.zanite_chestplate, 6000));
        registry.register(new AetherEnchantment(AetherItems.zanite_leggings, 6000));
        registry.register(new AetherEnchantment(AetherItems.zanite_boots, 6000));

        registry.register(new AetherEnchantment(AetherItems.gravitite_helmet, 13000));
        registry.register(new AetherEnchantment(AetherItems.gravitite_chestplate, 13000));
        registry.register(new AetherEnchantment(AetherItems.gravitite_leggings, 13000));
        registry.register(new AetherEnchantment(AetherItems.gravitite_boots, 13000));

        registry.register(new AetherEnchantment(AetherItems.golden_dart, AetherItems.enchanted_dart, 250));
        registry.register(new AetherEnchantment(AetherItems.golden_dart_shooter, AetherItems.enchanted_dart_shooter, 500));

        registry.register(new AetherEnchantment(AetherItems.skyroot_poison_bucket, AetherItems.skyroot_remedy_bucket, 1000));

        registry.register(new AetherEnchantment(AetherBlocks.holystone, AetherItems.healing_stone, 750));
        registry.register(new AetherEnchantment(AetherBlocks.gravitite_ore, AetherBlocks.enchanted_gravitite, 1000));
        registry.register(new AetherEnchantment(AetherBlocks.quicksoil, AetherBlocks.quicksoil_glass, 250));

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

        registry.register(new AetherEnchantmentFuel(AetherItems.ambrosium_shard, 500));

        registry.register(new AetherFreezable(AetherBlocks.cold_aercloud, AetherBlocks.blue_aercloud, 100));
        registry.register(new AetherFreezable(AetherBlocks.skyroot_leaves, AetherBlocks.crystal_leaves, 150));
        registry.register(new AetherFreezable(AetherItems.skyroot_bucket, Blocks.ICE, 500));
        registry.register(new AetherFreezable(AetherItems.ascending_dawn, AetherItems.welcoming_skies, 800));
        registry.register(new AetherFreezable(Blocks.ICE, Blocks.PACKED_ICE, 750));
        registry.register(new AetherFreezable(Items.WATER_BUCKET, Blocks.ICE, 500));
        registry.register(new AetherFreezable(Items.LAVA_BUCKET, Blocks.OBSIDIAN, 500));
        registry.register(new AetherFreezable(AetherItems.iron_ring, AetherItems.ice_ring, 2500));
        registry.register(new AetherFreezable(AetherItems.golden_ring, AetherItems.ice_ring, 2500));
        registry.register(new AetherFreezable(AetherItems.iron_pendant, AetherItems.ice_pendant, 2500));
        registry.register(new AetherFreezable(AetherItems.golden_pendant, AetherItems.ice_pendant, 2500));

        registry.register(new AetherFreezableFuel(AetherBlocks.icestone, 500));
    }
}
