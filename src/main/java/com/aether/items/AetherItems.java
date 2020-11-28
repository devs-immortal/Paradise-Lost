package com.aether.items;

import com.aether.Aether;
import com.aether.blocks.AetherBlocks;
import com.aether.entities.AetherEntityTypes;
import com.aether.items.accessories.AccessoryTypes;
import com.aether.items.accessories.ItemAccessory;
import com.aether.items.armor.*;
import com.aether.items.dungeon.DungeonKey;
import com.aether.items.food.*;
import com.aether.items.resources.*;
import com.aether.items.staff.CloudStaff;
import com.aether.items.staff.NatureStaff;
import com.aether.items.tools.*;
import com.aether.items.utils.AetherTiers;
import com.aether.items.weapons.*;
import com.aether.util.EnumHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Formatting;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

public class AetherItems {
    public static final Item ZANITE_GEM, AMBROSIUM_SHARD, GOLDEN_AMBER, AECHOR_PETAL, SWET_BALL;
    public static final Item SKYROOT_STICK/*, VICTORY_MEDAL*/;
    public static final Item SKYROOT_PICKAXE, SKYROOT_AXE, SKYROOT_SHOVEL, SKYROOT_SWORD, SKYROOT_HOE;
    public static final Item HOLYSTONE_PICKAXE, HOLYSTONE_AXE, HOLYSTONE_SHOVEL, HOLYSTONE_SWORD, HOLYSTONE_HOE;
    public static final Item ZANITE_PICKAXE, ZANITE_AXE, ZANITE_SHOVEL, ZANITE_SWORD, ZANITE_HOE;
    public static final Item GRAVITITE_PICKAXE, GRAVITITE_AXE, GRAVITITE_SHOVEL, GRAVITITE_SWORD, GRAVITITE_HOE;
    public static final Item VALKYRIE_PICKAXE, VALKYRIE_AXE, VALKYRIE_SHOVEL, VALKYRIE_HOE;
    public static final ZaniteArmor ZANITE_HELMET, ZANITE_CHESTPLATE, ZANITE_LEGGINGS, ZANITE_BOOTS;
    public static final AetherArmor GRAVITITE_HELMET, GRAVITITE_CHESTPLATE, GRAVITITE_LEGGINGS, GRAVITITE_BOOTS;
    public static final AetherArmor NEPTUNE_HELMET, NEPTUNE_CHESTPLATE, NEPTUNE_LEGGINGS, NEPTUNE_BOOTS;
    public static final AetherArmor PHOENIX_HELMET, PHOENIX_CHESTPLATE, PHOENIX_LEGGINGS, PHOENIX_BOOTS;
    public static final AetherArmor OBSIDIAN_HELMET, OBSIDIAN_CHESTPLATE, OBSIDIAN_LEGGINGS, OBSIDIAN_BOOTS;
    public static final AetherArmor VALKYRIE_HELMET, VALKYRIE_CHESTPLATE, VALKYRIE_LEGGINGS, VALKYRIE_BOOTS;
    public static final Item BLUE_BERRY, ENCHANTED_BLUEBERRY, BLUE_GUMMY_SWET, GOLDEN_GUMMY_SWET, HEALING_STONE, WHITE_APPLE, GINGERBREAD_MAN, CANDY_CANE;
    public static final Item SKYROOT_BUCKET, SKYROOT_WATER_BUCKET, SKYROOT_POISON_BUCKET, SKYROOT_REMEDY_BUCKET, SKYROOT_MILK_BUCKET;
    public static final Item CLOUD_PARACHUTE, GOLDEN_CLOUD_PARACHUTE;
    public static final Item BRONZE_KEY, SILVER_KEY, GOLDEN_KEY;
    public static final Item NATURE_STAFF, CLOUD_STAFF, MOA_EGG;
    public static final Item GOLDEN_DART, ENCHANTED_DART, POISON_DART;
    public static final Item GOLDEN_DART_SHOOTER, ENCHANTED_DART_SHOOTER, POISON_DART_SHOOTER;
    public static final Item PHOENIX_BOW;
    public static final Item FLAMING_SWORD, LIGHTNING_SWORD, HOLY_SWORD;
    public static final Item VAMPIRE_BLADE, PIG_SLAYER, CANDY_CANE_SWORD, /*NOTCH_HAMMER,*/
            VALKYRIE_LANCE;
    public static final Item LEATHER_GLOVES, IRON_GLOVES, GOLDEN_GLOVES, CHAIN_GLOVES, DIAMOND_GLOVES;
    public static final Item ZANITE_GLOVES, GRAVITITE_GLOVES, NEPTUNE_GLOVES, PHOENIX_GLOVES, OBSIDIAN_GLOVES, VALKYRIE_GLOVES;
    public static final Item IRON_RING, GOLDEN_RING, ZANITE_RING, ICE_RING, IRON_PENDANT, GOLDEN_PENDANT, ZANITE_PENDANT, ICE_PENDANT;
    public static final Item RED_CAPE, BLUE_CAPE, YELLOW_CAPE, WHITE_CAPE, SWET_CAPE, INVISIBILITY_CAPE, AGILITY_CAPE;
    public static final Item GOLDEN_FEATHER, REGENERATION_STONE, IRON_BUBBLE;
    public static final Item LIFE_SHARD;
    public static final AetherArmor SENTRY_BOOTS/*, LIGHTNING_KNIFE*/;
    //    PUBLIC STATIC ITEM AETHER_TUNE, ASCENDING_DAWN, WELCOMING_SKIES, LEGACY;
//    PUBLIC STATIC ITEM REPULSION_SHIELD;
    public static final Item LORE_BOOK;
    public static final Item QUICKSOIL_VIAL, AERCLOUD_VIAL;
    public static final Item AECHOR_PLANT_SPAWN_EGG, CHEST_MIMIC_SPAWN_EGG,
            COCKATRICE_SPAWN_EGG, AERBUNNY_SPAWN_EGG, FLYING_COW_SPAWN_EGG,
            MOA_SPAWN_EGG, PHYG_SPAWN_EGG, SHEEPUFF_SPAWN_EGG, GOLD_AERDUST, FROZEN_AERDUST;

    public static final Rarity AETHER_LOOT = EnumHelper.addRarity("aether_loot", Formatting.GREEN);


    static {
        // Resources
        ZANITE_GEM = register("zanite_gemstone", new ZaniteGemstone());
        AMBROSIUM_SHARD = register("ambrosium_shard", new AmbrosiumShard());
        GOLDEN_AMBER = register("golden_amber", new GoldenAmber());
        AECHOR_PETAL = register("aechor_petal", new AechorPetal());
        SWET_BALL = register("swet_ball", new SwetBall());
        SKYROOT_STICK = register("skyroot_stick", new SkyrootStick());
        GOLD_AERDUST = register("gold_aerdust", new GoldAerdust());
        FROZEN_AERDUST = register("frozen_aerdust", new FrozenAerdust());

        // Tools
        SKYROOT_SHOVEL = register("skyroot_shovel", new AetherShovel(AetherTiers.Skyroot, 1.5F, -3.0F));
        SKYROOT_PICKAXE = register("skyroot_pickaxe", new AetherPickaxe(AetherTiers.Skyroot, 1, -2.8F));
        SKYROOT_AXE = register("skyroot_axe", new AetherAxe(AetherTiers.Skyroot, 6.0F, -3.2F));
        SKYROOT_SWORD = register("skyroot_sword", new AetherSword(AetherTiers.Skyroot, 3, -2.4F));
        SKYROOT_HOE = register("skyroot_hoe", new AetherHoe(AetherTiers.Valkyrie, 1));

        HOLYSTONE_SHOVEL = register("holystone_shovel", new AetherShovel(AetherTiers.Holystone, 1.5F, -3.0F));
        HOLYSTONE_PICKAXE = register("holystone_pickaxe", new AetherPickaxe(AetherTiers.Holystone, 1, -2.8F));
        HOLYSTONE_AXE = register("holystone_axe", new AetherAxe(AetherTiers.Holystone, 7.0F, -3.2F));
        HOLYSTONE_SWORD = register("holystone_sword", new AetherSword(AetherTiers.Holystone, 3, -2.4F));
        HOLYSTONE_HOE = register("holystone_hoe", new AetherHoe(AetherTiers.Valkyrie, 2));

        ZANITE_SHOVEL = register("zanite_shovel", new AetherShovel(AetherTiers.Zanite, 1.5F, -3.0F));
        ZANITE_PICKAXE = register("zanite_pickaxe", new AetherPickaxe(AetherTiers.Zanite, 1, -2.8F));
        ZANITE_AXE = register("zanite_axe", new AetherAxe(AetherTiers.Zanite, 6.0F, -3.1F));
        ZANITE_SWORD = register("zanite_sword", new AetherSword(AetherTiers.Zanite, 3, -2.4F));
        ZANITE_HOE = register("zanite_hoe", new AetherHoe(AetherTiers.Valkyrie, 3));

        GRAVITITE_SHOVEL = register("gravitite_shovel", new AetherShovel(AetherTiers.Gravitite, Rarity.RARE, 1.5F, -3.0F));
        GRAVITITE_PICKAXE = register("gravitite_pickaxe", new AetherPickaxe(AetherTiers.Gravitite, Rarity.RARE, 1, -2.8F));
        GRAVITITE_AXE = register("gravitite_axe", new AetherAxe(AetherTiers.Gravitite, Rarity.RARE, 5.0F, -3.0F));
        GRAVITITE_SWORD = register("gravitite_sword", new AetherSword(AetherTiers.Gravitite, Rarity.RARE, 3, -2.4F));
        GRAVITITE_HOE = register("gravitite_hoe", new AetherHoe(AetherTiers.Valkyrie, 4));

        VALKYRIE_SHOVEL = register("valkyrie_shovel", new AetherShovel(AetherTiers.Valkyrie, AETHER_LOOT, 1.5F, -3.0F));
        VALKYRIE_PICKAXE = register("valkyrie_pickaxe", new AetherPickaxe(AetherTiers.Valkyrie, AETHER_LOOT, 1, -2.8F));
        VALKYRIE_AXE = register("valkyrie_axe", new AetherAxe(AetherTiers.Valkyrie, AETHER_LOOT, 4.0F, -2.9F));
        VALKYRIE_LANCE = register("valkyrie_lance", new ValkyrieLance());
        VALKYRIE_HOE = register("valkyrie_hoe", new AetherHoe(AetherTiers.Valkyrie, 5));

        // Armor
        ZANITE_HELMET = (ZaniteArmor) register("zanite_helmet", new ZaniteArmor(AetherArmorType.Zanite, EquipmentSlot.HEAD));
        ZANITE_CHESTPLATE = (ZaniteArmor) register("zanite_chestplate", new ZaniteArmor(AetherArmorType.Zanite, EquipmentSlot.CHEST));
        ZANITE_LEGGINGS = (ZaniteArmor) register("zanite_leggings", new ZaniteArmor(AetherArmorType.Zanite, EquipmentSlot.LEGS));
        ZANITE_BOOTS = (ZaniteArmor) register("zanite_boots", new ZaniteArmor(AetherArmorType.Zanite, EquipmentSlot.FEET));
//
        GRAVITITE_HELMET = (GravititeArmor) register("gravitite_helmet", new GravititeArmor(AetherArmorType.Gravitite, EquipmentSlot.HEAD));
        GRAVITITE_CHESTPLATE = (GravititeArmor) register("gravitite_chestplate", new GravititeArmor(AetherArmorType.Gravitite, EquipmentSlot.CHEST));
        GRAVITITE_LEGGINGS = (GravititeArmor) register("gravitite_leggings", new GravititeArmor(AetherArmorType.Gravitite, EquipmentSlot.LEGS));
        GRAVITITE_BOOTS = (GravititeArmor) register("gravitite_boots", new GravititeArmor(AetherArmorType.Gravitite, EquipmentSlot.FEET));
//
        NEPTUNE_HELMET = (NeptuneArmor) register("neptune_helmet", new NeptuneArmor(AetherArmorType.Neptune, EquipmentSlot.HEAD));
        NEPTUNE_CHESTPLATE = (NeptuneArmor) register("neptune_chestplate", new NeptuneArmor(AetherArmorType.Neptune, EquipmentSlot.CHEST));
        NEPTUNE_LEGGINGS = (NeptuneArmor) register("neptune_leggings", new NeptuneArmor(AetherArmorType.Neptune, EquipmentSlot.LEGS));
        NEPTUNE_BOOTS = (NeptuneArmor) register("neptune_boots", new NeptuneArmor(AetherArmorType.Neptune, EquipmentSlot.FEET));
//
        PHOENIX_HELMET = (PhoenixArmor) register("phoenix_helmet", new PhoenixArmor(AetherArmorType.Phoenix, EquipmentSlot.HEAD));
        PHOENIX_CHESTPLATE = (PhoenixArmor) register("phoenix_chestplate", new PhoenixArmor(AetherArmorType.Phoenix, EquipmentSlot.CHEST));
        PHOENIX_LEGGINGS = (PhoenixArmor) register("phoenix_leggings", new PhoenixArmor(AetherArmorType.Phoenix, EquipmentSlot.LEGS));
        PHOENIX_BOOTS = (PhoenixArmor) register("phoenix_boots", new PhoenixArmor(AetherArmorType.Phoenix, EquipmentSlot.FEET));
//
        OBSIDIAN_HELMET = (ObsidianArmor) register("obsidian_helmet", new ObsidianArmor(AetherArmorType.Obsidian, EquipmentSlot.HEAD));
        OBSIDIAN_CHESTPLATE = (ObsidianArmor) register("obsidian_chestplate", new ObsidianArmor(AetherArmorType.Obsidian, EquipmentSlot.CHEST));
        OBSIDIAN_LEGGINGS = (ObsidianArmor) register("obsidian_leggings", new ObsidianArmor(AetherArmorType.Obsidian, EquipmentSlot.LEGS));
        OBSIDIAN_BOOTS = (ObsidianArmor) register("obsidian_boots", new ObsidianArmor(AetherArmorType.Obsidian, EquipmentSlot.FEET));
//
        VALKYRIE_HELMET = (ValkyrieArmor) register("valkyrie_helmet", new ValkyrieArmor(AetherArmorType.Valkyrie, EquipmentSlot.HEAD));
        VALKYRIE_CHESTPLATE = (ValkyrieArmor) register("valkyrie_chestplate", new ValkyrieArmor(AetherArmorType.Valkyrie, EquipmentSlot.CHEST));
        VALKYRIE_LEGGINGS = (ValkyrieArmor) register("valkyrie_leggings", new ValkyrieArmor(AetherArmorType.Valkyrie, EquipmentSlot.LEGS));
        VALKYRIE_BOOTS = (ValkyrieArmor) register("valkyrie_boots", new ValkyrieArmor(AetherArmorType.Valkyrie, EquipmentSlot.FEET));
//
        SENTRY_BOOTS = (AetherArmor) register("sentry_boots", new AetherArmor("sentry", AetherArmorType.Valkyrie, AETHER_LOOT, EquipmentSlot.FEET));

        // Food
        BLUE_BERRY = register("blue_berry", new Item(new Item.Settings().group(AetherItemGroups.Food).food(AetherFood.DEFAULT)));
        ENCHANTED_BLUEBERRY = register("enchanted_blueberry", new Item(new Item.Settings().group(AetherItemGroups.Food).rarity(Rarity.RARE).food(AetherFood.ENCHANTED_BLUEBERRY)));
        WHITE_APPLE = register("white_apple", new WhiteApple());
        BLUE_GUMMY_SWET = register("blue_gummy_swet", new GummySwet());
        GOLDEN_GUMMY_SWET = register("golden_gummy_swet", new GummySwet());
        HEALING_STONE = register("healing_stone", new HealingStone());
        CANDY_CANE = register("candy_cane", new Item(new Item.Settings().group(AetherItemGroups.Food).food(AetherFood.DEFAULT)));
        GINGERBREAD_MAN = register("ginger_bread_man", new Item(new Item.Settings().group(AetherItemGroups.Food).food(AetherFood.DEFAULT)));

        // Misc + Materials
        GOLDEN_FEATHER = register("golden_feather", new ItemAccessory(AccessoryTypes.MISC, AETHER_LOOT));
        REGENERATION_STONE = register("regeneration_stone", new ItemAccessory(AccessoryTypes.MISC, AETHER_LOOT));
        IRON_BUBBLE = register("iron_bubble", new ItemAccessory(AccessoryTypes.MISC, AETHER_LOOT));
        LIFE_SHARD = register("life_shard", new LifeShard());
        CLOUD_STAFF = register("cloud_staff", new CloudStaff());
        NATURE_STAFF = register("nature_staff", new NatureStaff());
        MOA_EGG = register("moa_egg", new MoaEgg());
        LORE_BOOK = register("lore_book", new BookOfLore((new Item.Settings()).maxCount(1).group(AetherItemGroups.Misc)));
        SKYROOT_BUCKET = register("skyroot_bucket", new SkyrootBucket());
        SKYROOT_WATER_BUCKET = register("skyroot_water_bucket", new SkyrootBucket(Fluids.WATER, SKYROOT_BUCKET));
        SKYROOT_MILK_BUCKET = register("skyroot_milk_bucket", new SkyrootBucket(SKYROOT_BUCKET));
        SKYROOT_POISON_BUCKET = register("skyroot_poison_bucket", new SkyrootBucket(SKYROOT_BUCKET));
        SKYROOT_REMEDY_BUCKET = register("skyroot_remedy_bucket", new SkyrootBucket(SKYROOT_BUCKET));
        QUICKSOIL_VIAL = register("quicksoil_vial", new QuicksoilVial(Fluids.EMPTY, new Item.Settings().group(AetherItemGroups.Misc)));
        AERCLOUD_VIAL = register("aercloud_vial", new QuicksoilVial(AetherBlocks.DENSE_AERCLOUD_STILL, new Item.Settings().group(AetherItemGroups.Misc)));

        CLOUD_PARACHUTE = register("cold_parachute", new Parachute());
        GOLDEN_CLOUD_PARACHUTE = register("golden_parachute", new Parachute(20));

        BRONZE_KEY = register("bronze_key", new DungeonKey());
        SILVER_KEY = register("silver_key", new DungeonKey());
        GOLDEN_KEY = register("golden_key", new DungeonKey());

        // Materials


        // Weapons
        GOLDEN_DART = register("golden_dart", new Dart(Rarity.COMMON));
        ENCHANTED_DART = register("enchanted_dart", new Dart(Rarity.RARE));
        POISON_DART = register("poison_dart", new Dart(Rarity.COMMON));

        GOLDEN_DART_SHOOTER = register("golden_dart_shooter", new DartShooter((Dart) GOLDEN_DART, Rarity.COMMON));
        ENCHANTED_DART_SHOOTER = register("enchanted_dart_shooter", new DartShooter((Dart) ENCHANTED_DART, Rarity.RARE));
        POISON_DART_SHOOTER = register("poison_dart_shooter", new DartShooter((Dart) POISON_DART, Rarity.COMMON));

        PHOENIX_BOW = register("phoenix_bow", new ItemPhoenixBow());

        FLAMING_SWORD = register("flaming_sword", new ElementalSword());
        LIGHTNING_SWORD = register("lightning_sword", new ElementalSword());
        HOLY_SWORD = register("holy_sword", new ElementalSword());

        VAMPIRE_BLADE = register("vampire_blade", new VampireBlade());
        PIG_SLAYER = register("pig_slayer", new PigSlayer());
        CANDY_CANE_SWORD = register("candy_cane_sword", new CandyCaneSword());

        // Spawn Eggs
        AECHOR_PLANT_SPAWN_EGG = register("aechor_plant_spawn_egg", new SpawnEggItem(AetherEntityTypes.AECHOR_PLANT, 0x9fc3f7, 0x29a793, new Item.Settings().group(AetherItemGroups.Misc)));
        CHEST_MIMIC_SPAWN_EGG = null;
        COCKATRICE_SPAWN_EGG = register("cockatrice_spawn_egg", new SpawnEggItem(AetherEntityTypes.COCKATRICE, 0x9fc3f7, 0x3d2338, new Item.Settings().group(AetherItemGroups.Misc)));
        AERBUNNY_SPAWN_EGG = register("aerbunny_spawn_egg", new SpawnEggItem(AetherEntityTypes.AERBUNNY, 0x9fc3f7, 0x3d2338, new Item.Settings().group(AetherItemGroups.Misc)));
        FLYING_COW_SPAWN_EGG = null;
        MOA_SPAWN_EGG = null;
        PHYG_SPAWN_EGG = null;
        SHEEPUFF_SPAWN_EGG = null;

        // Accessories
        LEATHER_GLOVES = register("leather_gloves", new ItemAccessory(AccessoryTypes.GLOVES, 0xC65C35).setDamageMultiplier(1.5F));
        IRON_GLOVES = register("iron_gloves", new ItemAccessory(AccessoryTypes.GLOVES).setDamageMultiplier(2.5F));
        GOLDEN_GLOVES = register("golden_gloves", new ItemAccessory(AccessoryTypes.GLOVES, 0xFBF424).setDamageMultiplier(2.0F));
        CHAIN_GLOVES = register("chain_gloves", new ItemAccessory("chain", AccessoryTypes.GLOVES).setDamageMultiplier(2.0F));
        DIAMOND_GLOVES = register("diamond_gloves", new ItemAccessory(AccessoryTypes.GLOVES, 0x33EBCB).setDamageMultiplier(4.5F));
        ZANITE_GLOVES = register("zanite_gloves", new ItemAccessory(AccessoryTypes.GLOVES, 0x711AE8).setDamageMultiplier(3.0F));
        GRAVITITE_GLOVES = register("gravitite_gloves", new ItemAccessory(AccessoryTypes.GLOVES, Rarity.RARE, 0xE752DB).setDamageMultiplier(4.0F));
        NEPTUNE_GLOVES = register("neptune_gloves", new ItemAccessory(AccessoryTypes.GLOVES, AETHER_LOOT, 0x2654FF).setDamageMultiplier(4.5F));
        PHOENIX_GLOVES = register("phoenix_gloves", new ItemAccessory("phoenix", AccessoryTypes.GLOVES, AETHER_LOOT, 0xff7700).setDamageMultiplier(4.0F));
        OBSIDIAN_GLOVES = register("obsidian_gloves", new ItemAccessory(AccessoryTypes.GLOVES, 0x1B1447).setDamageMultiplier(5.0F));
        VALKYRIE_GLOVES = register("valkyrie_gloves", new ItemAccessory("valkyrie", AccessoryTypes.GLOVES, AETHER_LOOT).setDamageMultiplier(5.0F));

        IRON_RING = register("iron_ring", new ItemAccessory(AccessoryTypes.RING));
        GOLDEN_RING = register("golden_ring", new ItemAccessory(AccessoryTypes.RING, 0xEAEE57));
        ZANITE_RING = register("zanite_ring", new ItemAccessory(AccessoryTypes.RING, 0x711AE8));
        ICE_RING = register("ice_ring", new ItemAccessory(AccessoryTypes.RING, Rarity.RARE, 0x95E6E7));

        IRON_PENDANT = register("iron_pendant", new ItemAccessory(AccessoryTypes.PENDANT));
        GOLDEN_PENDANT = register("golden_pendant", new ItemAccessory(AccessoryTypes.PENDANT, 0xEAEE57));
        ZANITE_PENDANT = register("zanite_pendant", new ItemAccessory(AccessoryTypes.PENDANT, 0x711AE8));
        ICE_PENDANT = register("ice_pendant", new ItemAccessory(AccessoryTypes.PENDANT, Rarity.RARE, 0x95E6E7));

        WHITE_CAPE = register("white_cape", new ItemAccessory(AccessoryTypes.CAPE));
        RED_CAPE = register("red_cape", new ItemAccessory(AccessoryTypes.CAPE, 0xE81111));
        BLUE_CAPE = register("blue_cape", new ItemAccessory(AccessoryTypes.CAPE, 0x137FB7));
        YELLOW_CAPE = register("yellow_cape", new ItemAccessory(AccessoryTypes.CAPE, 0xCDCB0E));
        SWET_CAPE = register("swet_cape", new ItemAccessory("swet", AccessoryTypes.CAPE, AETHER_LOOT));
        AGILITY_CAPE = register("agility_cape", new ItemAccessory("agility", AccessoryTypes.CAPE, AETHER_LOOT));
        INVISIBILITY_CAPE = register("invisibility_cape", new ItemAccessory(AccessoryTypes.CAPE, AETHER_LOOT));
        // Some music, I think the original ones are copyrighted but whatever
        // I'll make a few -d17
        //TODO: Add AetherSounds
        //AETHER_TUNE = register("aether_tune", new AetherDisc(20, AetherSounds.aether_tune));
        //ASCENDING_DAWN = register("ascending_dawn", new AetherDisc(12, AetherSounds.ascending_dawn));
//        WELCOMING_SKIES = register("welcoming_skies", new AetherDisc(10, AetherSounds.welcoming_skies));
//        LEGACY = register("legacy", new AetherDisc(6, AetherSounds.legacy));
    }

    private static Item register(String id, Item item) {
        return Registry.register(Registry.ITEM, Aether.locate(id), item);
    }

    public static void initializeClient() {
        // Empty void. Eternal emptiness
    }
}